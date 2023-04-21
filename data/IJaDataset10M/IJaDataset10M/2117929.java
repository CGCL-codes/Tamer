package in.as.webosite.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Frame;

class DocumentationPanel extends Composite {

    private final VerticalPanel panel = new VerticalPanel();

    private final HTML declare = new HTML("<a href='http://iwebo.sourceforge.net/docs/' target='_blank'>Documentation</a> " + "of the project sources generated using Doxygen is shown below (alternately click on the underlined word)." + "I cannot guarantee that this is in sync with the sources as I havent found a kewl " + "way to auto-generate it on source update to the repository, but I will try to keep it " + "current with any major code changes.");

    private final Frame docFrame;

    public DocumentationPanel() {
        panel.add(declare);
        docFrame = new Frame("http://iwebo.sourceforge.net/docs/");
        docFrame.setStyleName("doc-frame");
        panel.add(docFrame);
        initWidget(panel);
    }
}
