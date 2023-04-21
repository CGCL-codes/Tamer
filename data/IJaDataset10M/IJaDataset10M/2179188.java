package ch.photoindex.servlet.elements.buttons;

import ch.photoindex.PhotoindexConstants;
import ch.photoindex.localization.Dictionary;
import ch.photoindex.servlet.elements.Element;
import ch.photoindex.servlet.pages.edit.TopicPage;

/**
 * A button to add a topic.
 * 
 * @author Lukas Blunschi
 * 
 */
public class TopicAddButton implements Element {

    public void appendHtml(StringBuffer html, Dictionary dict) {
        html.append("<!-- topic add button -->\n");
        String href = "?page=" + TopicPage.PAGENAME + "&amp;id=" + PhotoindexConstants.VAL_NEW;
        html.append("<div class='button'>\n");
        html.append("<a href='" + href + "'>").append(dict.add()).append("</a>\n");
        html.append("</div>\n\n");
    }
}
