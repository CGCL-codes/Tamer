package swtproto;

import data.*;
import sale.*;
import util.swing.*;
import java.awt.*;
import java.text.*;
import javax.swing.*;

/**
     *
     * Editor f�r DM-Geldbetr�ge in Tabellen.
     *
     * @version 1.0
     * @author Stefan Ulbrich
     *
     */
public class DEMCellEditor extends DefaultCellEditor {

    private String[] m_asResult;

    /**
       * Konstruktor. Erstellt einen neuen DEMCellEditor.
       */
    public DEMCellEditor(String[] asResult, String sInit) {
        super(new JTextInput(asResult, sInit));
        m_asResult = asResult;
    }

    /**
       * Gibt die zum Editieren verwendete Komponente korrekt initialisiert zur�ck.
       */
    public Component getTableCellEditorComponent(JTable jtTable, Object oValue, boolean fIsSelected, int nRow, int nColumn) {
        Component cmp = super.getTableCellEditorComponent(jtTable, oValue, fIsSelected, nRow, nColumn);
        ((JTextInput) cmp).setText(((Currency) Shop.getTheShop().getCatalog("DEM")).toString((NumberValue) oValue));
        return cmp;
    }

    /**
       * Versucht, den eingegebenen Text als Geldbetrag zu interpretieren.
       */
    public Object getCellEditorValue() {
        try {
            return ((Currency) Shop.getTheShop().getCatalog("DEM")).parse(m_asResult[0]);
        } catch (ParseException pexc) {
            return new IntegerValue(0);
        }
    }

    /**
       * Versucht, das Editieren zu beenden und gibt eine Bewertung des Erfolges zur�ck.
       */
    public boolean stopCellEditing() {
        try {
            ((Currency) Shop.getTheShop().getCatalog("DEM")).parse(m_asResult[0]);
        } catch (ParseException pexc) {
            return false;
        }
        return super.stopCellEditing();
    }
}
