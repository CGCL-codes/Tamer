package com.hack23.cia.web.views.dialogs;

import thinwire.ui.Image;
import thinwire.ui.Label;
import thinwire.ui.Panel;
import thinwire.ui.layout.TableLayout;
import com.hack23.cia.web.ApplicationMessageHolder;
import com.hack23.cia.web.ImageConstants;
import com.hack23.cia.web.ApplicationMessageHolder.MessageConstans;

/**
 * The Class ErrorMessageBox.
 */
public class ErrorMessageBox extends Panel {

    /**
     * The error message.
     */
    private Label errorMessage = new Label();

    /**
     * Instantiates a new error message box.
     */
    public ErrorMessageBox() {
        super();
        setVisible(false);
        setLayout(new TableLayout(new double[][] { { 16, 200 }, { 16 } }, 2, 4));
        Image errorImage = new Image(ImageConstants.ACTION_ERROR);
        errorImage.setSize(ImageConstants.ICON_SIZE, ImageConstants.ICON_SIZE);
        errorImage.setLimit("0,0");
        getChildren().add(errorImage);
        errorMessage.setLimit("1,0");
        errorMessage.setSize(200, 16);
        getChildren().add(errorMessage);
    }

    /**
     * Display error message.
     * 
     * @param message the message
     */
    public final void displayErrorMessage(final String message) {
        errorMessage.setText(" " + ApplicationMessageHolder.getMessage(MessageConstans.ERROR_MESSAGE) + " : " + message);
        setVisible(true);
    }
}
