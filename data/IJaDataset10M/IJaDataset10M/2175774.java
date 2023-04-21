package edu.ucla.stat.SOCR.core;

import java.awt.event.*;
import java.net.*;
import java.util.*;
import edu.ucla.stat.SOCR.games.wavelet.*;
import JSci.maths.wavelet.*;
import javax.swing.*;

/** This class implements the wavelet loading dynamically */
public class WaveletLoader implements ActionListener {

    protected FWT fwt;

    private SOCRJComboBox implementedCombo;

    protected URL codeBase;

    protected String implementedFile = null;

    public Object getCurrentItem() {
        return fwt;
    }

    private Observable observable = new Observable() {

        public void notifyObservers() {
            try {
                System.out.println("notifying observers");
                super.setChanged();
                super.notifyObservers();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public WaveletLoader(URL url) {
        codeBase = url;
        implementedFile = "implementedWavelets.txt";
        try {
            if (implementedFile != null) {
                implementedCombo = new SOCRJComboBox(new URL(codeBase, implementedFile).openStream());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        implementedCombo.addActionListener(this);
        itemChanged(implementedCombo.getSelectedClassName());
    }

    public void start() {
    }

    protected void itemChanged(String className) {
        try {
            fwt = (FWT) Class.forName(className).newInstance();
            observable.notifyObservers();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void addObserver(Observer o) {
        observable.addObserver(o);
    }

    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == implementedCombo) {
            itemChanged(implementedCombo.getSelectedClassName());
        }
    }

    public JComboBox getJComboBox() {
        return this.implementedCombo;
    }

    /** updates the collected information of distribution */
    public void updateStatus() {
    }
}
