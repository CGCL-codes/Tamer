package audictiv.client.ui.website.blockLeft;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class BlockLeft extends FlowPanel {

    public BlockLeft() {
        StackPanel test = new StackPanel();
        VerticalPanel accountContent = new VerticalPanel();
        VerticalPanel audioContent = new VerticalPanel();
        accountContent.add(new HTML("Manage my account:"));
        accountContent.add(new HTML("Personal info"));
        accountContent.add(new HTML("Public profile"));
        audioContent.add(new HTML("Manage my audio content:"));
        audioContent.add(new HTML("My albums"));
        audioContent.add(new HTML("My songs"));
        test.add(accountContent);
        test.add(audioContent);
        this.add(test);
        test.getElement().setId("leftMenu");
    }
}
