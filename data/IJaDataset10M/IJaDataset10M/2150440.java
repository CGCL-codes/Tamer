package testingapplication.tabbedPanel;

import com.softaspects.jsf.component.listmodel.ListDataModelImpl;
import com.softaspects.jsf.component.label.model.LabelModel;
import com.softaspects.jsf.component.label.model.LabelModelImpl;
import com.softaspects.jsf.component.tabbedPanel.Tab;
import com.softaspects.jsf.component.tabbedPanel.TabbedPanelDataModel;

public class VerticalLayoutTabbedPanelDataModelBean extends TabbedPanelDataModel {

    private static final String IMAGE_PATH = "images/tabbedPanel/large/";

    private static final String SMALL_IMAGE_PATH = "images/tabbedPanel/small/";

    private LabelModel createTab(String text, String hint, String image, String hoverImage) {
        LabelModel tab = new LabelModelImpl();
        tab.setText(text);
        tab.setHintText(hint);
        tab.setDefaultImage(new StringBuffer(IMAGE_PATH).append(image).toString());
        tab.setDefaultSmallImage(new StringBuffer(SMALL_IMAGE_PATH).append(image).toString());
        tab.setHightLightImage(new StringBuffer(IMAGE_PATH).append(hoverImage).toString());
        tab.setHightLightSmallImage(new StringBuffer(SMALL_IMAGE_PATH).append(hoverImage).toString());
        return tab;
    }

    public VerticalLayoutTabbedPanelDataModelBean() {
        addValue(createTab("Create mail", "Create new mail", "mail.gif", "mail_write.gif"));
        addValue(createTab("View mail", "View mail", "mail2_view.gif", "mail_view.gif"));
        addValue(createTab("Delete mail", "Delete mail", "mail_delete.gif", "mail_delete.gif"));
    }
}
