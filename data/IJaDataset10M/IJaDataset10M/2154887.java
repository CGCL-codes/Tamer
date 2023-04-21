package org.sekomintory.server.webui.gwt.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Liang ZHANG
 * 
 */
public abstract class SubmitDialogBox<T> extends DialogBox {

    public interface CancelEventHandler<X> {

        void onCancelEvent(X dialogBox);
    }

    public interface SubmitEventHandler<X> {

        void onSubmitEvent(X dialogBox);
    }

    private class UserLoginPanel extends VerticalPanel {

        private class ButtonPanel extends HorizontalPanel {

            /**
			 * A reference to a button for user to click to submit register
			 * information.
			 */
            private Button submitButton = new Button("Submit");

            private Button cancelButton = new Button("Cancel");

            public ButtonPanel() {
                this.add(this.submitButton);
                this.add(this.cancelButton);
            }

            /**
			 * @return the submitButton
			 */
            public Button getSubmitButton() {
                return this.submitButton;
            }

            /**
			 * @return the cancelButton
			 */
            public Button getCancelButton() {
                return this.cancelButton;
            }
        }

        private ButtonPanel buttonPanel = new ButtonPanel();

        public UserLoginPanel() {
            this.add(this.buttonPanel);
        }

        public ButtonPanel getButtonPanel() {
            return this.buttonPanel;
        }
    }

    private UserLoginPanel userLoginPanel = new UserLoginPanel();

    private CancelEventHandler<T> cancelEventhandler;

    private SubmitEventHandler<T> submitEventHandler;

    public SubmitDialogBox() {
        this.setModal(true);
        this.setWidget(this.userLoginPanel);
        this.userLoginPanel.getButtonPanel().getCancelButton().addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                if (SubmitDialogBox.this.cancelEventhandler != null) {
                    SubmitDialogBox.this.cancelEventhandler.onCancelEvent((T) SubmitDialogBox.this);
                }
            }
        });
        this.userLoginPanel.getButtonPanel().getSubmitButton().addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                if (SubmitDialogBox.this.submitEventHandler != null) {
                    SubmitDialogBox.this.submitEventHandler.onSubmitEvent((T) SubmitDialogBox.this);
                }
            }
        });
    }

    /**
	 * @param submitEventHandler
	 *            the submitEventHandler to set
	 */
    public void setSubmitEventHandler(SubmitEventHandler<T> submitEventHandler) {
        this.submitEventHandler = submitEventHandler;
    }

    public void setCancelEventHandler(CancelEventHandler<T> cancelEventHandler) {
        this.cancelEventhandler = cancelEventHandler;
    }

    /**
	 * @return the userLoginPanel
	 */
    public UserLoginPanel getUserLoginPanel() {
        return userLoginPanel;
    }

    public void setInnerWidget(Widget widget) {
        this.userLoginPanel.insert(widget, 0);
    }
}
