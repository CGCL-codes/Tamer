package es.ua.dlsi.tradubi.main.client.apppanels;

import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import es.ua.dlsi.tradubi.main.client.TradubiMain;
import es.ua.dlsi.tradubi.main.client.i18n.I18nConstants;
import es.ua.dlsi.tradubi.main.client.i18n.I18nConstantsFactory;

/**
 * @author jft3
 *
 */
public class ErrorPanel extends AppPanel {

    /**
	 * @uml.property name="constants"
	 * @uml.associationEnd
	 */
    protected final I18nConstants constants = I18nConstantsFactory.getI18nConstants();

    private VerticalPanel vPanel;

    private Label lbError;

    public ErrorPanel(String error) {
        content.setStyleName("ContentPanel");
        super.getTable().getFirstChildElement().getFirstChildElement().getNextSiblingElement().getFirstChildElement().addClassName("content-cell");
        this.setPanelTitle(constants.getString(error));
        lbError = new Label(constants.getString("error_notfoundgeneric"));
        lbError.addStyleName("formLabel");
        vPanel = new VerticalPanel();
        Image notfound = new Image(TradubiMain.getImageBundle().notfound());
        vPanel.add(notfound);
        vPanel.add(lbError);
        vPanel.setCellHorizontalAlignment(notfound, ALIGN_CENTER);
        vPanel.setCellHorizontalAlignment(lbError, ALIGN_CENTER);
        vPanel.setWidth("100%");
        content.add(vPanel);
    }
}
