package com.bluebrim.gui.client;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractButton;

/**
 * Abstract subklass till CoComponentAdaptor som anv�nds f�r att koppla ihop en AbstractButton 
 * med ett v�rdeobjekt. Subklasser till CoButtonAdaptor f�rv�ntas implementera den operation
 * som utf�rs p� v�rdeobjektet n�r knappen aktiveras. CoButtonAdaptor st�der inte hantering
 * av �ndringar i v�rdemodellen (man kan s�ga att det �r en en-v�gs-adaptor).
 */
public class CoButtonAdaptor extends CoComponentAdaptor implements ActionListener {

    AbstractButton m_button;

    CoValueable m_valueModel;

    /**
 * Default-konstruktor.
 */
    public CoButtonAdaptor() {
    }

    /**
 * Konstruktor.
 * @param aValueModel v�rdemodellen
 * @param button knappen
 */
    public CoButtonAdaptor(CoValueable aValueModel, AbstractButton button) {
        this();
        setValueModel(aValueModel);
        setButton(button);
    }

    /**
 * Hantera knappaktivering.
 * H�r f�rv�ntas subklasser implementera operationen p� v�rdemodellen.
 */
    public void actionPerformed(ActionEvent e) {
        getValueModel().setValue(e);
    }

    /**
 * Access-metod till knappen.
 * @return knappen
 */
    public AbstractButton getButton() {
        return m_button;
    }

    protected Component getComponent() {
        return getButton();
    }

    /**
 * Access-metod till v�rdemodellen.
 * @return v�rdemodellen
 */
    public CoValueable getValueModel() {
        return m_valueModel;
    }

    /**
 * Access-metod f�r att byta knapp.
 * @param b den nya knappen
 */
    protected void setButton(AbstractButton b) {
        if (m_button != null) m_button.removeActionListener(this);
        m_button = b;
        m_button.setEnabled(m_valueModel.isEnabled());
        if (m_button != null) m_button.addActionListener(this);
    }

    /**
 * Access-metod f�r att byta v�rdemodell.
 * @param b den nya v�rdemodellen
 */
    protected void setValueModel(CoValueable valueModel) {
        m_valueModel = valueModel;
    }
}
