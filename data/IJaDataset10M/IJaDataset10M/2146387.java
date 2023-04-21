package org.gwtoolbox.widget.client.popup.dialog;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.*;
import org.gwtoolbox.widget.client.button.SimpleButton;
import org.gwtoolbox.widget.client.notification.NotificationType;
import org.gwtoolbox.widget.client.panel.LayoutUtils;
import org.gwtoolbox.widget.client.support.CssFloat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Uri Boness
 */
public class MessageBox extends Dialog {

    public enum Button {

        YES, NO, OK, CANCEL
    }

    public enum OptionType {

        OK, YES_NO, YES_NO_CANCEL, OK_CANCEL
    }

    private Image typeImage;

    private Label messageLabel;

    private FlowPanel buttons;

    private MessageBox() {
        super(false, true);
        setClosable(true);
        setResizable(false);
        VerticalPanel main = new VerticalPanel();
        main.setVerticalAlignment(VerticalPanel.ALIGN_TOP);
        HorizontalPanel contentPanel = new HorizontalPanel();
        contentPanel.setStyleName("Content");
        contentPanel.setVerticalAlignment(HorizontalPanel.ALIGN_TOP);
        typeImage = new Image();
        contentPanel.add(typeImage);
        messageLabel = new Label();
        messageLabel.setStylePrimaryName("Message");
        contentPanel.add(messageLabel);
        contentPanel.setCellWidth(messageLabel, "100%");
        main.add(contentPanel);
        main.setCellWidth(contentPanel, "100%");
        buttons = new FlowPanel();
        LayoutUtils.setCssFloat(buttons.getElement(), CssFloat.RIGHT);
        buttons.setStylePrimaryName("Buttons");
        main.add(buttons);
        main.setCellWidth(buttons, "100%");
        main.setWidth("100%");
        setWidget(main);
        addStyleName("MessageDialog");
        setWidthPx(300);
    }

    public static void show(NotificationType type, OptionType optionType, String title, String message, final OptionCallback callback) {
        final MessageBox box = new MessageBox();
        box.setCaption(title);
        box.messageLabel.setText(message);
        type.getLargeIcon().applyTo(box.typeImage);
        box.typeImage.setStylePrimaryName("TypeImage");
        FocusWidget focusButton = null;
        List<SimpleButton> buttons = new ArrayList<SimpleButton>();
        switch(optionType) {
            case OK_CANCEL:
                SimpleButton cancelButton = new SimpleButton("Cancel", new ClickHandler() {

                    public void onClick(ClickEvent event) {
                        callback.handle(Button.CANCEL);
                        box.hide();
                    }
                });
                LayoutUtils.setCssFloat(cancelButton.getElement(), CssFloat.RIGHT);
                DOM.setStyleAttribute(cancelButton.getElement(), "marginRight", "5px");
                buttons.add(cancelButton);
            case OK:
                SimpleButton okButton = new SimpleButton("OK", new ClickHandler() {

                    public void onClick(ClickEvent event) {
                        callback.handle(Button.OK);
                        box.hide();
                    }
                });
                LayoutUtils.setCssFloat(okButton.getElement(), CssFloat.RIGHT);
                DOM.setStyleAttribute(okButton.getElement(), "marginRight", "5px");
                buttons.add(0, okButton);
                focusButton = okButton;
                break;
            case YES_NO_CANCEL:
                cancelButton = new SimpleButton("Cancel", new ClickHandler() {

                    public void onClick(ClickEvent event) {
                        callback.handle(Button.CANCEL);
                        box.hide();
                    }
                });
                LayoutUtils.setCssFloat(cancelButton.getElement(), CssFloat.RIGHT);
                DOM.setStyleAttribute(cancelButton.getElement(), "marginRight", "5px");
                buttons.add(cancelButton);
            case YES_NO:
                SimpleButton noButton = new SimpleButton("No", new ClickHandler() {

                    public void onClick(ClickEvent event) {
                        callback.handle(Button.NO);
                        box.hide();
                    }
                });
                LayoutUtils.setCssFloat(noButton.getElement(), CssFloat.RIGHT);
                DOM.setStyleAttribute(noButton.getElement(), "marginRight", "5px");
                buttons.add(0, noButton);
                SimpleButton yesButton = new SimpleButton("Yes", new ClickHandler() {

                    public void onClick(ClickEvent event) {
                        callback.handle(Button.YES);
                        box.hide();
                    }
                });
                LayoutUtils.setCssFloat(yesButton.getElement(), CssFloat.RIGHT);
                DOM.setStyleAttribute(yesButton.getElement(), "marginRight", "5px");
                buttons.add(0, yesButton);
                focusButton = yesButton;
        }
        for (SimpleButton simpleButton : buttons) {
            box.buttons.add(simpleButton);
        }
        box.center();
        if (focusButton != null) {
            focusButton.setFocus(true);
        }
    }

    public static interface OptionCallback {

        void handle(Button button);
    }
}
