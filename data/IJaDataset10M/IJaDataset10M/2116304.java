package nuts.demo.action.images;

import java.io.InputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nuts.core.io.IOUtils;
import nuts.core.servlet.HttpServletSupport;
import nuts.demo.action.AbstractAction;
import nuts.demo.model.bean.SampleFile;
import nuts.demo.model.dao.SampleFileDAO;
import nuts.exts.struts2.util.StrutsContextUtils;

/**
 */
@SuppressWarnings("serial")
public class SampleFileFileAction extends AbstractAction {

    protected Long id;

    /**
	 * fileName
	 */
    protected String fileName;

    /**
	 * fileType
	 */
    protected String fileType;

    /**
	 * fileStream
	 */
    protected InputStream fileStream;

    /**
	 * @return the fileName
	 */
    public String getFileName() {
        return fileName;
    }

    /**
	 * @param fileName the fileName to set
	 */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
	 * @return the fileType
	 */
    public String getFileType() {
        if (fileType == null) {
            fileType = IOUtils.getContentTypeFor(fileName);
        }
        return fileType;
    }

    /**
	 * @param fileType the fileType to set
	 */
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    /**
	 * @return the fileStream
	 */
    public InputStream getFileStream() {
        return fileStream;
    }

    /**
	 * @param fileStream the fileStream to set
	 */
    public void setFileStream(InputStream fileStream) {
        this.fileStream = fileStream;
    }

    /**
	 * @return the id
	 */
    public Long getId() {
        return id;
    }

    /**
	 * @param id the id to set
	 */
    public void setId(Long id) {
        this.id = id;
    }

    /**
	 * execute
	 * @return result name
	 * @throws Exception if an error occurs
	 */
    public String execute() throws Exception {
        HttpServletRequest req = StrutsContextUtils.getServletRequest();
        HttpServletResponse res = StrutsContextUtils.getServletResponse();
        if (id != null) {
            SampleFileDAO dao = new SampleFileDAO(getDataAccessSession());
            SampleFile sf = dao.selectByPrimaryKey(id);
            if (sf != null && sf.getFileField() != null && sf.getFileField().getData() != null) {
                HttpServletSupport hsrs = new HttpServletSupport(req, res);
                hsrs.setFileName("SampleFile_File_" + id);
                hsrs.setContentLength(sf.getFileField().getData().length);
                hsrs.writeResponseHeader();
                hsrs.writeResponseData(sf.getFileField().getData());
                return NONE;
            }
        }
        res.sendError(HttpServletResponse.SC_NOT_FOUND);
        return NONE;
    }
}
