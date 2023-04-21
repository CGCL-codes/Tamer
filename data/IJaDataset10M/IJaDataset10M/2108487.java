package gov.esporing.ost.presentation.web;

import java.awt.GridBagConstraints;
import org.wings.SButton;
import org.wings.SDimension;
import org.wings.SGridBagLayout;
import org.wings.SList;
import org.wings.SPanel;

/**
 * This panel displays a list of messages received from other parties, and allows the user
 * to open and store them. The logic of the class is handled in the ReceivedMessages-
 * PanelController.
 * 
 * @author Arnt Christian Wolden, arntchri@stud.ntnu.no
 */
public class RecievedMessagePanel extends SPanel {

    public final SList receivedMessagesList = new SList();

    public final SButton openButton = new SButton();

    public final SButton saveButton = new SButton();

    /**The constructor sets up the layout of the panel, and places the components.*/
    public RecievedMessagePanel() {
        receivedMessagesList.setPreferredSize(new SDimension(500, 150));
        SGridBagLayout receivedMessagesPanelLayout = new SGridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        this.setLayout(receivedMessagesPanelLayout);
        c.gridy = 0;
        this.add(receivedMessagesList, c);
        openButton.setText("Open message");
        saveButton.setText("Save message");
        SPanel buttonPanel = new SPanel();
        buttonPanel.add(openButton);
        buttonPanel.add(saveButton);
        c.gridy++;
        this.add(buttonPanel, c);
    }
}
