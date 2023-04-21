package spindles.gwt.client;

import java.util.ArrayList;
import java.util.List;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class QuestionDialog extends DialogBox implements ClickListener, Publisher {

    private boolean answer;

    private List subscribers = new ArrayList();

    public QuestionDialog(String question) {
        setText(question);
        VerticalPanel outer = new VerticalPanel();
        HTML text = new HTML("This may take a while...");
        text.setStyleName("spindles-AboutText");
        FlowPanel buttonsPanel = new FlowPanel();
        Button yes = new Button("Yes");
        yes.addClickListener(this);
        Button no = new Button("No");
        no.addClickListener(this);
        buttonsPanel.add(yes);
        buttonsPanel.add(no);
        outer.add(text);
        outer.add(buttonsPanel);
        outer.setCellHorizontalAlignment(buttonsPanel, VerticalPanel.ALIGN_CENTER);
        outer.setWidth("100%");
        setWidget(outer);
    }

    public void onClick(Widget sender) {
        String text = ((Button) sender).getText();
        answer = text.equals("Yes") ? true : false;
        hide();
        notifySubscribers();
    }

    public void addSubscriber(Subscriber s) {
        subscribers.add(s);
    }

    public void notifySubscribers() {
        for (int i = 0; i < subscribers.size(); i++) {
            ((Subscriber) subscribers.get(i)).update(Boolean.valueOf(answer));
        }
    }
}
