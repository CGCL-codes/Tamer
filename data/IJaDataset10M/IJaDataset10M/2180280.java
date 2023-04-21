package com.infinity.wavemvc.sample.client;

import org.cobogw.gwt.waveapi.gadget.client.WaveGadget;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.gadgets.client.Gadget;
import com.google.gwt.gadgets.client.UserPreferences;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.infinity.wavemvc.client.mvc.WaveBean;
import com.infinity.wavemvc.client.mvc.WaveBeanCreationListener;
import com.infinity.wavemvc.client.mvc.WaveClientHelper;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
@Gadget.ModulePrefs(title = "WaveMVC Sample", author = "Jeff Richley", author_email = "jeffrichley@gmail.com", height = 300)
@Gadget.InjectContent(files = { "WaveMVCSample.html" })
public class WaveMVCSample extends WaveGadget<UserPreferences> {

    /**
	 * Will contain all comments from users
	 */
    final Panel commentContainer = new VerticalPanel();

    /**
	 * My personal comment bean
	 */
    private CommentBean myComment = null;

    @Override
    protected void init(UserPreferences preferences) {
        final Button sendButton = new Button("Send");
        final TextBox commentField = new TextBox();
        commentField.setText("Here is my comment");
        RootPanel.get("nameFieldContainer").add(commentField);
        RootPanel.get("sendButtonContainer").add(sendButton);
        RootPanel.get("commentContainer").add(commentContainer);
        commentField.setFocus(true);
        commentField.selectAll();
        sendButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (myComment == null) {
                    myComment = GWT.create(CommentBean.class);
                    Label commentLabel = new Label("comment coming in...");
                    commentContainer.add(commentLabel);
                    new CommentController(myComment, commentLabel);
                }
                myComment.setComment(commentField.getText());
                myComment.setCount(myComment.getCount() + 1);
            }
        });
        WaveClientHelper.initialize(getWave());
        WaveClientHelper.setWorkingLocal(false);
        WaveClientHelper.addWaveBeanCreationListener(new WaveBeanCreationListener() {

            @Override
            public void handleWaveBeanCreation(WaveBean bean) {
                if (bean instanceof CommentBean) {
                    Label commentLabel = new Label(((CommentBean) bean).getComment());
                    commentContainer.add(commentLabel);
                    new CommentController((CommentBean) bean, commentLabel);
                }
            }
        });
    }
}
