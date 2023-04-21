package maze.gwt.complex.html_panel.factory;

import maze.gwt.complex.basics.HtmlStringTransformer;
import com.google.gwt.user.client.ui.HTMLPanel;

/**
 * @author Normunds Mazurs
 * 
 */
public interface HtmlPanelFactory {

    HTMLPanel createHTMLPanel(HtmlStringTransformer htmlCodeTransformer);

    HTMLPanel createHTMLPanel();
}
