package scrabble.client.cView;

import java.util.ArrayList;
import scrabble.client.cController.PlayerController;
import scrabble.client.cModel.events.PlayerEvent;
import scrabble.client.cModel.listeners.PlayerHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PlayerGWT extends Composite implements PlayerHandler, ClickHandler {

    private final Button buttonDefault = new Button("Default");

    private final Button buttonOK = new Button("Ok");

    private final Label labelNumberOfPlayers = new Label("Number of Players: ");

    private Label response;

    private static final int NUMBER_MAX = 4;

    private final Label[] labelNames = new Label[NUMBER_MAX];

    private final TextBox[] textFieldNames = new TextBox[NUMBER_MAX];

    private ListBox comboNbPlayers;

    private String message;

    private int nbUsers;

    public PlayerGWT() {
        comboNbPlayers = new ListBox();
        DockPanel mainPanel = new DockPanel();
        VerticalPanel verticalPanel = new VerticalPanel();
        HorizontalPanel horizontalPanel = new HorizontalPanel();
        horizontalPanel.setSize("300", "40");
        horizontalPanel.add(labelNumberOfPlayers);
        horizontalPanel.add(comboNbPlayers);
        horizontalPanel.setCellHorizontalAlignment(mainPanel, HorizontalPanel.ALIGN_CENTER);
        mainPanel.add(horizontalPanel, DockPanel.NORTH);
        for (int i = 0; i < NUMBER_MAX; ++i) {
            if (i != 0) comboNbPlayers.addItem(String.valueOf(i + 1));
            textFieldNames[i] = new TextBox();
            labelNames[i] = new Label("Name Player[" + (i + 1) + "]: ");
            horizontalPanel = new HorizontalPanel();
            horizontalPanel.add(labelNames[i]);
            horizontalPanel.add(textFieldNames[i]);
            verticalPanel.add(horizontalPanel);
        }
        DockPanel panelSouth = new DockPanel();
        horizontalPanel = new HorizontalPanel();
        horizontalPanel.setSize("300", "60");
        horizontalPanel.add(buttonOK);
        horizontalPanel.setCellHorizontalAlignment(buttonOK, HorizontalPanel.ALIGN_CENTER);
        horizontalPanel.setCellVerticalAlignment(buttonOK, HorizontalPanel.ALIGN_MIDDLE);
        buttonDefault.addClickHandler(this);
        horizontalPanel.add(buttonDefault);
        horizontalPanel.setCellHorizontalAlignment(buttonDefault, HorizontalPanel.ALIGN_CENTER);
        horizontalPanel.setCellVerticalAlignment(buttonDefault, HorizontalPanel.ALIGN_MIDDLE);
        response = new Label("");
        panelSouth.add(horizontalPanel, DockPanel.CENTER);
        RootPanel.get("response").add(response);
        horizontalPanel.setCellHorizontalAlignment(mainPanel, HorizontalPanel.ALIGN_CENTER);
        mainPanel.add(verticalPanel, DockPanel.CENTER);
        mainPanel.add(panelSouth, DockPanel.SOUTH);
        this.comboNbPlayers.addClickHandler(this);
        initWidget(mainPanel);
        updateLabelPlayer("2");
        reinitialize();
        message = "Partie créée avec ";
    }

    /**
         * Cette fonction cache ou rend accessible les champs en fonction du
         * nombre de joueurs.
         * @param nbPlayer
         */
    public void updateLabelPlayer(String nbPlayer) {
        int nbUsers = Integer.parseInt(nbPlayer);
        for (int i = 3; i > nbUsers - 1; --i) {
            labelNames[i].setWordWrap(false);
            textFieldNames[i].setEnabled(false);
            String label = labelNames[i].getText().replaceFirst(":", "");
            textFieldNames[i].setText(label);
        }
        for (int i = 0; i < nbUsers; ++i) {
            labelNames[i].setWordWrap(true);
            textFieldNames[i].setEnabled(true);
        }
    }

    /**
         * Cette fonction réinitialise les composants du panneau.
         * Correspond à l'utilisation du bouton Default.
         */
    public void reinitialize() {
        comboNbPlayers.setSelectedIndex(0);
        for (int i = 0; i < NUMBER_MAX; ++i) {
            String label = labelNames[i].getText().replaceFirst(":", "");
            textFieldNames[i].setText(label);
        }
        updateLabelPlayer(String.valueOf(2));
    }

    public void setController(PlayerController controller) {
        buttonOK.addClickHandler(controller);
    }

    @Override
    public void playerChanged(PlayerEvent event) {
        if (nbUsers-- > 1) {
            message += event.getMsg() + " - ";
        } else {
            response.setText(message + event.getMsg());
            response.setStyleName("green");
            message = "Partie créée avec ";
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        Object object = event.getSource();
        if (object instanceof ListBox) {
            ListBox combo = (ListBox) object;
            updateLabelPlayer(String.valueOf(combo.getSelectedIndex() + 2));
        } else if (object instanceof Button) {
            reinitialize();
        }
    }

    /**
         * Retourne le contenu des champs des joueurs
         * @return
         */
    public ArrayList<String> getTextFieldNames() {
        ArrayList<String> listNames = new ArrayList<String>();
        nbUsers = comboNbPlayers.getSelectedIndex() + 2;
        for (int i = 0; i < nbUsers; ++i) listNames.add(textFieldNames[i].getText());
        return listNames;
    }
}
