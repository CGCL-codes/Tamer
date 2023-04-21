package whf.framework.web;

import java.io.File;
import javax.servlet.jsp.PageContext;
import org.apache.struts.upload.FormFile;
import whf.file.entity.Folder;
import whf.file.service.FileServiceImp;
import whf.file.service.TypeServiceImp;
import whf.file.util.DiskFileManager;
import whf.file.util.FileManager;
import whf.framework.util.StringUtils;

/**
 * @author wanghaifeng
 *
 */
public final class HttpHelper {

    /**
	 * 返回当前的实际路径
	 * @modify wanghaifeng Nov 11, 2006 8:33:51 AM
	 * @param pageContext
	 * @param name
	 * @return
	 */
    public static String getRealPath(PageContext pageContext, String name) {
        String destPathName = name;
        if (destPathName == null) {
            destPathName = pageContext.getServletContext().getRealPath("/");
        }
        if (destPathName.indexOf("/") != -1) {
            if (destPathName.charAt(destPathName.length() - 1) != '/') destPathName = String.valueOf(destPathName).concat("/");
        } else if (destPathName.charAt(destPathName.length() - 1) != '\\') destPathName = String.valueOf(destPathName).concat("\\");
        return destPathName;
    }

    /**
	 * 判断是否是物理路径
	 * @modify wanghaifeng Nov 11, 2006 8:31:56 AM
	 * @param pageContext
	 * @param filePathName
	 * @param option
	 * @return true：路径存在，false：不存在
	 */
    public static String getPhysicalPath(PageContext pageContext, String filePathName, int option) {
        String path = new String();
        String fileName = new String();
        String fileSeparator = new String();
        boolean isPhysical = false;
        fileSeparator = System.getProperty("file.separator");
        if (filePathName == null || filePathName.equals("")) throw new IllegalArgumentException("There is no specified destination file (1140).");
        if (filePathName.lastIndexOf("\\") >= 0) {
            path = filePathName.substring(0, filePathName.lastIndexOf("\\"));
            fileName = filePathName.substring(filePathName.lastIndexOf("\\") + 1);
        }
        if (filePathName.lastIndexOf("/") >= 0) {
            path = filePathName.substring(0, filePathName.lastIndexOf("/"));
            fileName = filePathName.substring(filePathName.lastIndexOf("/") + 1);
        }
        path = path.length() != 0 ? path : "/";
        File physicalPath = new File(path);
        if (physicalPath.exists()) isPhysical = true;
        if (option == 0) {
            if (isVirtual(pageContext, path)) {
                path = pageContext.getServletContext().getRealPath(path);
                if (path.endsWith(fileSeparator)) path = path + fileName; else path = String.valueOf((new StringBuffer(String.valueOf(path))).append(fileSeparator).append(fileName));
                return path;
            }
            if (isPhysical) {
                return filePathName;
            } else {
                throw new IllegalArgumentException("This path does not exist (1135).");
            }
        }
        if (option == 1) {
            if (isVirtual(pageContext, path)) {
                path = pageContext.getServletContext().getRealPath(path);
                if (path.endsWith(fileSeparator)) path = path + fileName; else path = String.valueOf((new StringBuffer(String.valueOf(path))).append(fileSeparator).append(fileName));
                return path;
            }
            if (isPhysical) throw new IllegalArgumentException("The path is not a virtual path."); else throw new IllegalArgumentException("This path does not exist (1135).");
        }
        if (option == 2) {
            if (isPhysical) return filePathName;
            if (isVirtual(pageContext, path)) throw new IllegalArgumentException("The path is not a physical path."); else throw new IllegalArgumentException("This path does not exist (1135).");
        } else {
            return null;
        }
    }

    /**
	 * 是否是虚拟路径
	 * @modify wanghaifeng Nov 11, 2006 8:30:52 AM
	 * @param pageContext
	 * @param pathName
	 * @return true：如果是并且路径存在；  false：不存在
	 */
    public static boolean isVirtual(PageContext pageContext, String pathName) {
        if (pageContext.getServletContext().getRealPath(pathName) != null) {
            File virtualFile = new File(pageContext.getServletContext().getRealPath(pathName));
            return virtualFile.exists();
        } else {
            return false;
        }
    }

    /**
	 * @author king
	 * @create 2008-2-17 上午10:55:52
	 * @param file
	 * @param folder
	 * @throws Exception
	 */
    public static whf.file.entity.File createFile(Folder folder, FormFile formFile, String remarks) throws Exception {
        if (formFile == null || formFile.getFileSize() == 0) return null;
        whf.file.entity.File file = new whf.file.entity.File();
        String fileName = formFile.getFileName();
        file.setName(fileName);
        int index = fileName.lastIndexOf(".") + 1;
        String ext = fileName.substring(index);
        if (StringUtils.isEmpty(ext)) {
            file.setExt(ext);
        }
        file.setType(TypeServiceImp.getTypeService().findByExt(ext));
        FileManager fileManager = DiskFileManager.getFileManager();
        fileName = fileManager.save(formFile.getInputStream(), formFile.getFileName());
        file.setPhycialPath(fileName);
        file.setSize(formFile.getFileSize());
        file.setRemarks(remarks);
        file.setFolder(folder);
        FileServiceImp.getFileService().create(file);
        return file;
    }
}
