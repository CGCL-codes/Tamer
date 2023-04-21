package com.cafe.serve.view.orderframe;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Choice;
import java.awt.Label;
import java.awt.List;
import java.awt.Panel;
import java.awt.TextArea;
import com.cafe.serve.client.OrderFrame;
import com.cafe.serve.event.OrderFrameEventHandler;

/**
 * @author admin
 *
 */
public class OrderFramePanel extends Panel {

    /**
     * Das ist die Liste in der alle Getr�nke stehen sollen. Die Getr�nkekarte in einer Liste.
     */
    private java.awt.List listGetraenkeKarte = null;

    /**
     * Das ist ein TextArea-Feld in das die Bestellung noch einmal �bersichtlich zu sehen ist. Wird bei jedem hinzuf�gen
     * oder entfernen eines Getr�nkes aktualisiert.
     */
    private TextArea textAreaBestellungsliste = null;

    /**
     * Vier Tasten damit man Bestellen, Rechnung erstellen, Getr�nk hinzuf�gen oder entfernen kann.
     */
    private Button buttonBestellen = null;

    /**
     * Vier Tasten damit man Bestellen, Rechnung erstellen, Getr�nk hinzuf�gen oder entfernen kann.
     */
    private Button buttonZahlen = null;

    /**
     * Vier Tasten damit man Bestellen, Rechnung erstellen, Getr�nk hinzuf�gen oder entfernen kann.
     */
    private Button buttonPlus = null;

    /**
     * Vier Tasten damit man Bestellen, Rechnung erstellen, Getr�nk hinzuf�gen oder entfernen kann.
     */
    private Button buttonMinus = null;

    /**
     * Die Choice choiceTische wird f�r die Bestellungen der Tische gebraucht. Sobald ein anderer Tisch gew�hlt wird
     * erscheint promt die Bestellung von dem Tisch in dem TextArea und in der Choice choiceBestellung. In der Choice
     * choiceBestellung steht noch einmal die ganze Bestellung. Wenn ein Getr�nk gew�hlt wird dann wird es von der
     * Bestellung entfernt.
     */
    private Choice choiceTische = null;

    /**
     * Die Choice choiceTische wird f�r die Bestellungen der Tische gebraucht. Sobald ein anderer Tisch gew�hlt wird
     * erscheint promt die Bestellung von dem Tisch in dem TextArea und in der Choice choiceBestellung. In der Choice
     * choiceBestellung steht noch einmal die ganze Bestellung. Wenn ein Getr�nk gew�hlt wird dann wird es von der
     * Bestellung entfernt.
     */
    private Choice choiceBestellung = null;

    /**
     * Das sind die Beschriftungen.
     */
    private Label labelBestellung = null;

    /**
     * Das sind die Beschriftungen.
     */
    private Label labelGetraenkeKarte = null;

    /**
     * Das sind die Beschriftungen.
     */
    private Label labelTische = null;

    /**
     * Das Frame.
     */
    private OrderFrame frame = null;

    public OrderFramePanel() {
        layoutOrderFrame();
        initialize();
    }

    private void initialize() {
        getLabelBestellung();
        getLabelGetraenkeKarte();
        getLabelTische();
        getChoiceBestellung();
        getTextAreaBestellungsliste();
        getListGetraenkeKarte();
    }

    public java.awt.List getListGetraenkeKarte() {
        if (null == listGetraenkeKarte) {
            this.listGetraenkeKarte = new List();
            listGetraenkeKarte.setName("listGetraenkeKarte");
        }
        return listGetraenkeKarte;
    }

    public void setListGetraenkeKarte(java.awt.List listGetraenkeKarte) {
        this.listGetraenkeKarte = listGetraenkeKarte;
    }

    public TextArea getTextAreaBestellungsliste() {
        if (null == textAreaBestellungsliste) {
            this.textAreaBestellungsliste = new TextArea(8, 12);
        }
        return textAreaBestellungsliste;
    }

    public void setTextAreaBestellungsliste(TextArea textAreaBestellungsliste) {
        this.textAreaBestellungsliste = textAreaBestellungsliste;
    }

    public Button getButtonBestellen() {
        if (null == buttonBestellen) {
            buttonBestellen = new Button("Order");
        }
        return buttonBestellen;
    }

    public void setButtonBestellen(Button buttonBestellen) {
        this.buttonBestellen = buttonBestellen;
    }

    public Button getButtonZahlen() {
        if (null == buttonZahlen) {
            buttonZahlen = new Button("Pay");
        }
        return buttonZahlen;
    }

    public void setButtonZahlen(Button buttonZahlen) {
        this.buttonZahlen = buttonZahlen;
    }

    public Button getButtonPlus() {
        if (null == buttonPlus) {
            buttonPlus = new Button("+ plus");
        }
        return buttonPlus;
    }

    public void setButtonPlus(Button buttonPlus) {
        this.buttonPlus = buttonPlus;
    }

    public Button getButtonMinus() {
        if (null == buttonMinus) {
            buttonMinus = new Button("- minus");
        }
        return buttonMinus;
    }

    public void setButtonMinus(Button buttonMinus) {
        this.buttonMinus = buttonMinus;
    }

    public Choice getChoiceTische() {
        if (null == choiceTische) {
            choiceTische = new Choice();
        }
        return choiceTische;
    }

    public void setChoiceTische(Choice choiceTische) {
        this.choiceTische = choiceTische;
    }

    public Choice getChoiceBestellung() {
        if (null == choiceBestellung) {
            choiceBestellung = new Choice();
            choiceBestellung.setName("choiceBestellung");
        }
        return choiceBestellung;
    }

    public void setChoiceBestellung(Choice choiceBestellung) {
        this.choiceBestellung = choiceBestellung;
    }

    public Label getLabelBestellung() {
        if (labelBestellung == null) {
            labelBestellung = new Label("Bestellung");
        }
        return labelBestellung;
    }

    public void setLabelBestellung(Label labelBestellung) {
        this.labelBestellung = labelBestellung;
    }

    public Label getLabelGetraenkeKarte() {
        if (null == labelGetraenkeKarte) {
            labelGetraenkeKarte = new Label("Getr�nkeKarte");
        }
        return labelGetraenkeKarte;
    }

    public void setLabelGetraenkeKarte(Label labelGetraenkeKarte) {
        this.labelGetraenkeKarte = labelGetraenkeKarte;
    }

    public Label getLabelTische() {
        if (null == labelTische) {
            labelTische = new Label("Tisch");
        }
        return labelTische;
    }

    public void setLabelTische(Label labelTische) {
        this.labelTische = labelTische;
    }

    private void layoutOrderFrame() {
        setLayout(new BorderLayout());
        Panel panelNorth = new Panel();
        panelNorth.setLayout(new BorderLayout());
        panelNorth.add("West", getLabelBestellung());
        panelNorth.add("East", getLabelGetraenkeKarte());
        Panel panelButtonCenter = new Panel();
        panelButtonCenter.setLayout(new BorderLayout());
        panelButtonCenter.add("West", getButtonPlus());
        panelButtonCenter.add("Center", getButtonMinus());
        Panel panelButton = new Panel();
        panelButton.setLayout(new BorderLayout());
        panelButton.add("West", getButtonBestellen());
        panelButton.add("Center", panelButtonCenter);
        panelButton.add("East", getButtonZahlen());
        Panel panelTisch = new Panel();
        panelTisch.setLayout(new BorderLayout());
        panelTisch.add("North", panelButton);
        panelTisch.add("East", getLabelTische());
        panelTisch.add("Center", getChoiceTische());
        Panel panel = new Panel();
        panel.setLayout(new BorderLayout());
        panel.add("North", getChoiceBestellung());
        panel.add("Center", getTextAreaBestellungsliste());
        Panel panelList = new Panel();
        panelList.setLayout(new BorderLayout());
        panelList.add("Center", getListGetraenkeKarte());
        add("South", panelTisch);
        add("West", panel);
        add("Center", panelList);
    }

    public OrderFrame getFrame() {
        return frame;
    }

    public void setFrame(OrderFrame frame) {
        this.frame = frame;
    }
}
