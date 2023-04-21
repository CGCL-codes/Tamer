package com.liferay.portal.upload;

import com.liferay.portal.kernel.util.ProgressTracker;
import com.liferay.portal.kernel.util.Validator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * <a href="LiferayFileUpload.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Myunghun Kim
 * @author Brian Wing Shun Chan
 *
 */
public class LiferayFileUpload extends ServletFileUpload {

    public static final String FILE_NAME = LiferayFileUpload.class.getName() + "_FILE_NAME";

    public static final String PERCENT = ProgressTracker.PERCENT;

    public LiferayFileUpload(FileItemFactory fileItemFactory, HttpServletRequest request) {
        super(fileItemFactory);
        _session = request.getSession();
    }

    public List<LiferayFileItem> parseRequest(HttpServletRequest request) throws FileUploadException {
        _session.removeAttribute(LiferayFileUpload.FILE_NAME);
        _session.removeAttribute(LiferayFileUpload.PERCENT);
        return super.parseRequest(request);
    }

    /**
	 * @deprecated
	 */
    protected FileItem createItem(Map headers, boolean formField) throws FileUploadException {
        LiferayFileItem item = (LiferayFileItem) super.createItem(headers, formField);
        String fileName = item.getFileName();
        if (Validator.isNotNull(fileName)) {
            _session.setAttribute(LiferayFileUpload.FILE_NAME, fileName);
        }
        return item;
    }

    private HttpSession _session;
}
