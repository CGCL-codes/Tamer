package ch.photoindex.servlet.elements;

import java.util.ArrayList;
import java.util.List;
import ch.photoindex.db.virtual.Upload;
import ch.photoindex.localization.Dictionary;
import ch.photoindex.servlet.pages.UploadsPage;
import ch.photoindex.tools.DateTools;

/**
 * An upload selector.
 * 
 * @author Lukas Blunschi
 * 
 */
public class UploadSelector implements Element {

    public static final String P_UPLOAD = "upload";

    private final List<Upload> uploads;

    private final String selValue;

    public UploadSelector(List<Upload> uploads, String selValue) {
        this.uploads = uploads;
        this.selValue = selValue;
    }

    public void appendHtml(StringBuffer html, Dictionary dict) {
        List<String> values = new ArrayList<String>();
        List<String> texts = new ArrayList<String>();
        for (Upload upload : uploads) {
            values.add(DateTools.filenameFormatter.format(upload.uploadTime));
            texts.add(DateTools.viewDatetimeFormatter.format(upload.uploadTime) + " (" + upload.count + ")");
        }
        html.append("<!-- upload selector -->\n");
        html.append("<div class='upload-selector-box sidebar-box'>\n");
        html.append("<div class='sidebar-header'>");
        html.append(dict.uploadsAvailableUploads() + ":");
        html.append("</div>\n");
        String js = "javascript:document.getElementById(\"upload_selector_form\").submit()";
        html.append("<div class='upload-selector sidebar-element'>\n");
        html.append("<form id='upload_selector_form' action='?' method='get'>\n");
        html.append("<input type='hidden' name='page' value='" + UploadsPage.PAGENAME + "' />\n");
        html.append("<select id='upload_selector' name='" + P_UPLOAD + "' size='5' onchange='" + js + "'>\n");
        for (int i = 0; i < values.size(); i++) {
            String value = values.get(i);
            String text = texts.get(i);
            if (selValue != null && selValue.equals(value)) {
                html.append("<option value='" + value + "' selected='selected'>" + text + "</option>\n");
            } else {
                html.append("<option value='" + value + "'>" + text + "</option>\n");
            }
        }
        html.append("</select>\n");
        html.append("</form>\n");
        html.append("</div>\n");
        html.append("</div>\n\n");
    }
}
