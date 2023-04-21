package com.germinus.xpression.content_editor.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import javax.portlet.RenderRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.germinus.xpression.cms.contents.Content;
import com.germinus.xpression.cms.directory.DirectoryFile;
import com.germinus.xpression.cms.directory.DirectoryPersister;
import com.germinus.xpression.cms.directory.ZipUtils;
import com.germinus.xpression.cms.educative.ImportExportFileUtil;
import com.germinus.xpression.cms.educative.WebFileUtil;
import com.germinus.xpression.cms.model.ContentTypes;
import com.germinus.xpression.cms.util.ManagerRegistry;

public class WebFileContentExporter extends ContentExporter {

    private static Log log = LogFactory.getLog(WebFileContentExporter.class);

    private static final String WEBFILE_PREFIX = "webfile";

    private String indexJCLICFileName;

    private static final String VIEW_CHAPTERS_CONTENT_JSP_PATH = "/html/portlet/book_viewer/view_chapters.jsp";

    public WebFileContentExporter(Content content, HttpServletRequest httpReq, HttpServletResponse httpRes) {
        super(content, httpReq, httpRes);
    }

    @Override
    protected String prefix() {
        return WEBFILE_PREFIX;
    }

    @Override
    protected ImportExportFileUtil importExportFileUtilFactory() {
        return new WebFileUtil();
    }

    @Override
    public String exportContent(RenderRequest request) throws Exception {
        String indexJCLICFileName = getIndexNameIfJCLICContent();
        if (StringUtils.isNotEmpty(indexJCLICFileName)) {
            return exportJCLICContent(indexJCLICFileName);
        } else {
            return super.exportContent(request);
        }
    }

    private String exportJCLICContent(String indexJCLICFileName) throws Exception {
        File targetFile = targetFile();
        OutputStream outputStream;
        DirectoryPersister directoryPersister = ManagerRegistry.getDirectoryPersister();
        DirectoryFile jclicFile = directoryPersister.getFile(content.getResourcesFolder(), indexJCLICFileName);
        byte[] jclicFileBytes = directoryPersister.getFileDataFromFile(jclicFile);
        outputStream = new FileOutputStream(targetFile);
        outputStream.write(jclicFileBytes);
        outputStream.flush();
        outputStream.close();
        return exportedFilePath(targetFile);
    }

    private String getIndexNameIfJCLICContent() {
        if (this.indexJCLICFileName == null) {
            this.indexJCLICFileName = "";
            boolean isWebFileType = content.getContentTypeId().equals(ContentTypes.WEB_FILES);
            if (isWebFileType) {
                String indexFileName = null;
                try {
                    indexFileName = (String) PropertyUtils.getProperty(content, "contentData.indexFile.fileName");
                } catch (Exception e) {
                    log.info("Error recovering index file name.", e);
                    return this.indexJCLICFileName;
                }
                if ((indexFileName != null) && ((indexFileName.contains(ZipUtils.JCLIC_FILE_EXTENSION)) && (indexFileName.endsWith(ZipUtils.ZIP_FILE_EXTENSION)))) this.indexJCLICFileName = indexFileName;
            }
            return this.indexJCLICFileName;
        } else {
            return this.indexJCLICFileName;
        }
    }

    @Override
    protected String fileBaseName() {
        String indexJCLICFileName = getIndexNameIfJCLICContent();
        String fileBaseName;
        if (StringUtils.isEmpty(indexJCLICFileName)) {
            fileBaseName = formatExportFileBaseName(content.getName().trim());
        } else fileBaseName = indexJCLICFileName.substring(0, indexJCLICFileName.length() - 4);
        return fileBaseName;
    }

    public String getViewPathToExport() {
        if (exportedContentIsChaptersType()) return VIEW_CHAPTERS_CONTENT_JSP_PATH; else return VIEW_CONTENT_JSP_PATH;
    }
}
