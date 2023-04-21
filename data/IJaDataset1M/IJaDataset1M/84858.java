package org.schnelln.gui.sidePanel;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.schnelln.communication.Packet;
import org.schnelln.config.Messages;
import org.schnelln.config.gui.Colors;
import org.schnelln.config.gui.SizesResizable;
import org.schnelln.gui.mainPanel.GKartendeck;
import org.schnelln.gui.sidePanel.images.Images;
import org.schnelln.spiel.farbe.Farbe;

public class ButtonPanel extends Observable implements ActionListener, Observer {

    protected Log log = LogFactory.getLog(this.getClass());

    private static final long serialVersionUID = 3454715084662993633L;

    private JPanel sticheAnsagen = null, farbeAnsagen = null, tauschen = null;

    private ButtonPanelTauschen buttonPanelTauschen = null;

    private JComboBox sticheAnzahl = null;

    private JButton sticheOk = new JButton(Messages.getMessage("Button.confirm"));

    private JLabel anzahlStiche = new JLabel(Messages.getMessage("SidePanel.anzahlStiche"));

    private JButton schell = new JButton();

    private JButton nichts = new JButton();

    private JButton eichel = new JButton();

    private JButton laub = new JButton();

    private JButton herz = new JButton();

    private JPanel buttonPanel = null;

    public ButtonPanel() {
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        buttonPanel.setBackground(Colors.STATUS_BUTTONPANEL_COLOR);
        buttonPanel.setPreferredSize(SizesResizable.BUTTONFIELD.getDimension());
        buttonPanel.validate();
    }

    private JPanel initStiche() {
        final String[] stiche = { "0", "2", "3", "4", "5" };
        sticheAnzahl = new JComboBox(stiche);
        sticheAnzahl.setPreferredSize(SizesResizable.BUTTONFIELD_BUTTON_ANZAHLSTICHE.getDimension());
        sticheOk.addActionListener(this);
        if (sticheAnsagen == null) {
            sticheAnsagen = new JPanel();
            sticheAnsagen.setBackground(Colors.STATUS_BUTTONPANEL_COLOR);
            sticheAnzahl.setSelectedIndex(0);
            sticheAnsagen.add(anzahlStiche);
            sticheAnsagen.add(sticheAnzahl);
            sticheAnsagen.add(sticheOk);
            changeStatusStiche(false);
        }
        return sticheAnsagen;
    }

    public void changeStatusStiche(boolean flag) {
        if (flag) {
            sticheAnzahl.setSelectedIndex(0);
        }
        sticheAnzahl.setEnabled(flag);
        anzahlStiche.setEnabled(flag);
        sticheOk.setEnabled(flag);
    }

    private JPanel initFarbe() {
        if (farbeAnsagen == null) {
            herz.setIcon(new ImageIcon(Images.herz.getScaledInstance(SizesResizable.BUTTONFIELD_BUTTON_FARBE.getDimension().width, SizesResizable.BUTTONFIELD_BUTTON_FARBE.getDimension().height, Image.SCALE_AREA_AVERAGING)));
            herz.setPreferredSize(SizesResizable.BUTTONFIELD_BUTTON_FARBE.getDimension());
            herz.addActionListener(this);
            laub.setIcon(new ImageIcon(Images.laub.getScaledInstance(SizesResizable.BUTTONFIELD_BUTTON_FARBE.getDimension().width, SizesResizable.BUTTONFIELD_BUTTON_FARBE.getDimension().height, Image.SCALE_AREA_AVERAGING)));
            laub.setPreferredSize(SizesResizable.BUTTONFIELD_BUTTON_FARBE.getDimension());
            laub.addActionListener(this);
            eichel.setIcon(new ImageIcon(Images.eichel.getScaledInstance(SizesResizable.BUTTONFIELD_BUTTON_FARBE.getDimension().width, SizesResizable.BUTTONFIELD_BUTTON_FARBE.getDimension().height, Image.SCALE_AREA_AVERAGING)));
            eichel.setPreferredSize(SizesResizable.BUTTONFIELD_BUTTON_FARBE.getDimension());
            eichel.addActionListener(this);
            nichts.setIcon(new ImageIcon(Images.nichts.getScaledInstance(SizesResizable.BUTTONFIELD_BUTTON_FARBE.getDimension().width, SizesResizable.BUTTONFIELD_BUTTON_FARBE.getDimension().height, Image.SCALE_AREA_AVERAGING)));
            nichts.setPreferredSize(SizesResizable.BUTTONFIELD_BUTTON_FARBE.getDimension());
            nichts.addActionListener(this);
            schell.setIcon(new ImageIcon(Images.schell.getScaledInstance(SizesResizable.BUTTONFIELD_BUTTON_FARBE.getDimension().width, SizesResizable.BUTTONFIELD_BUTTON_FARBE.getDimension().height, Image.SCALE_AREA_AVERAGING)));
            schell.setPreferredSize(SizesResizable.BUTTONFIELD_BUTTON_FARBE.getDimension());
            schell.addActionListener(this);
            farbeAnsagen = new JPanel();
            farbeAnsagen.setBackground(Colors.STATUS_BUTTONPANEL_COLOR);
            farbeAnsagen.add(herz);
            farbeAnsagen.add(laub);
            farbeAnsagen.add(eichel);
            farbeAnsagen.add(schell);
            farbeAnsagen.add(nichts);
            changeStatusFarbe(false);
        }
        return farbeAnsagen;
    }

    public void disableAll() {
        buttonPanelTauschen.changeStatusTauschen(false);
        buttonPanelTauschen.changeStatusAussteigen(false);
        changeStatusFarbe(false);
        changeStatusStiche(false);
    }

    public void changeStatusFarbe(boolean flag) {
        schell.setEnabled(flag);
        herz.setEnabled(flag);
        eichel.setEnabled(flag);
        laub.setEnabled(flag);
        nichts.setEnabled(flag);
    }

    private JPanel initTauschen() {
        if (tauschen == null) {
            buttonPanelTauschen = new ButtonPanelTauschen();
            buttonPanelTauschen.changeStatusTauschen(false);
            buttonPanelTauschen.changeStatusAussteigen(false);
            tauschen = buttonPanelTauschen.getTauschenPanel();
        }
        return tauschen;
    }

    public ButtonPanelTauschen getButtonPanelTauschen() {
        return buttonPanelTauschen;
    }

    public ButtonPanel getButtonPanel() {
        this.buttonPanel.add(initStiche());
        this.buttonPanel.add(initFarbe());
        this.buttonPanel.add(initTauschen());
        getButtonPanelTauschen().addObserver(this);
        return this;
    }

    public JPanel get() {
        return this.buttonPanel;
    }

    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        log.debug("client.gui.sidePanel.ButtonPanel.actionPerformed: " + o);
        if (o.equals(sticheOk)) {
            this.setChanged();
            int stiche = new Integer((String) sticheAnzahl.getSelectedItem());
            this.notifyObservers(new Packet(Packet.ASTICHE, stiche));
        } else if (o.equals(herz)) {
            this.setChanged();
            this.notifyObservers(new Packet(Packet.AFARBE, Farbe.HERZ));
        } else if (o.equals(laub)) {
            this.setChanged();
            this.notifyObservers(new Packet(Packet.AFARBE, Farbe.LAUB));
        } else if (o.equals(schell)) {
            this.setChanged();
            this.notifyObservers(new Packet(Packet.AFARBE, Farbe.SCHELL));
        } else if (o.equals(eichel)) {
            this.setChanged();
            this.notifyObservers(new Packet(Packet.AFARBE, Farbe.EICHEL));
        } else if (o.equals(nichts)) {
            this.setChanged();
            this.notifyObservers(new Packet(Packet.AFARBE, Farbe.STEINER));
        }
    }

    public void update(Observable arg0, Object arg1) {
        if (arg0 instanceof GKartendeck) {
            if (arg1 instanceof Vector) {
                Vector zuTauschendeKarten = (Vector) arg1;
                for (int i = 0; i < zuTauschendeKarten.size(); i++) {
                    log.debug("client.gui.sidePanel.ButtonPanel.update: " + zuTauschendeKarten.get(i).toString());
                }
                this.setChanged();
                this.notifyObservers(new Packet(Packet.TKARTEN, zuTauschendeKarten));
                buttonPanelTauschen.changeStatusTauschen(false);
                buttonPanelTauschen.changeStatusAussteigen(false);
            } else if (arg1 instanceof Boolean) {
                this.setChanged();
                this.notifyObservers(false);
            }
        }
    }
}
