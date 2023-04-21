package com.liferay.util.servlet.fileupload;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import com.liferay.util.Validator;

/**
 * <a href="LiferayFileUpload.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Myunghun Kim
 * @author  Brian Wing Shun Chan
 * @version $Revision: 1.4 $
 *
 */
public class LiferayFileUpload extends ServletFileUpload {

    public static final String FILE_NAME = LiferayFileUpload.class.getName() + "_FILE_NAME";

    public static final String PERCENT = LiferayFileUpload.class.getName() + "_PERCENT";

    public LiferayFileUpload(FileItemFactory fileItemFactory, HttpServletRequest req) {
        super(fileItemFactory);
        _req = req;
        _ses = req.getSession();
    }

    public List parseRequest(HttpServletRequest req) throws FileUploadException {
        _ses.removeAttribute(LiferayFileUpload.FILE_NAME);
        _ses.removeAttribute(LiferayFileUpload.PERCENT);
        return super.parseRequest(req);
    }

    protected FileItem createItem(Map headers, boolean formField) throws FileUploadException {
        LiferayFileItem item = (LiferayFileItem) super.createItem(headers, formField);
        String fileName = item.getFileName();
        if (Validator.isNotNull(fileName)) {
            _ses.setAttribute(LiferayFileUpload.FILE_NAME, fileName);
        }
        return item;
    }

    private HttpServletRequest _req;

    private HttpSession _ses;
}
