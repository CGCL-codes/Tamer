package com.liferay.portlet.messageboards.smtp;

import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ObjectValuePair;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.util.Html;
import java.util.ArrayList;
import java.util.List;

/**
 * <a href="MBMailMessage.java.html"><b><i>View Source</i></b></a>
 *
 * @author Jorge Ferrer
 */
public class MBMailMessage {

    public void addFile(String fileName, byte[] data) {
        _files.add(new ObjectValuePair(fileName, data));
    }

    public List getFiles() {
        return _files;
    }

    public String getHtmlBody() {
        return _htmlBody;
    }

    public void setHtmlBody(String htmlBody) {
        _htmlBody = htmlBody;
    }

    public String getPlainBody() {
        return _plainBody;
    }

    public void setPlainBody(String plainBody) {
        _plainBody = plainBody;
    }

    public String getBody() {
        if (Validator.isNotNull(_plainBody)) {
            return GetterUtil.getString(_plainBody);
        } else if (Validator.isNotNull(_htmlBody)) {
            return Html.stripHtml(_htmlBody);
        } else {
            return "-";
        }
    }

    private String _htmlBody;

    private String _plainBody;

    private List _files = new ArrayList();
}
