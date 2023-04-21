package jfigure.geom2D.transformation;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.*;
import jfigure.util.*;

/**
 * Mod�le de table pour les propri�t�s sur les objet <code>Displayable</code>
 */
public class ApplicationPropertiesTableModel extends AbstractTableModel {

    private ApplicationProperties properties = new ApplicationProperties();

    private final String[] columnsNames = new String[] { "Propri�t�", "Valeur" };

    private boolean isLocked;

    /**
     * Actualise le mod�le de table
     * @param properties
     */
    public final void updateProperties(ApplicationProperties properties, boolean isLocked) {
        this.properties = properties;
        this.isLocked = isLocked;
    }

    /**
     * R�cup�ration des propri�t�s
     * @return
     */
    public final ApplicationProperties getProperties() {
        return this.properties;
    }

    /**
     * Retourne le nombre de colonnes
     */
    public final int getColumnCount() {
        return this.columnsNames.length;
    }

    /**
     * Retourne le nombre de lignes
     */
    public int getRowCount() {
        return this.properties.size();
    }

    /**
     * Retourne le nom d'une colonne
     */
    public String getColumnName(int column) {
        return this.columnsNames[column];
    }

    public Class getColumnClass(int columnIndex) {
        if (columnIndex == 0) {
            return java.lang.String.class;
        } else {
            return java.lang.String.class;
        }
    }

    /**
     * Retourne une valeur dans la table 
     */
    public Object getValueAt(int rowIndex, int columnIndex) {
        ApplicationProperties.AProperty p = this.properties.getAllProperties()[rowIndex];
        if (columnIndex == 0) {
            return p.getLabel();
        } else {
            return p.getValue();
        }
    }

    /**
     * D�finit une valeur dans la table
     */
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        ApplicationProperties.AProperty p = this.properties.getAllProperties()[rowIndex];
        if (columnIndex == 0) {
            return;
        } else {
            if (p.getType() == ApplicationProperties.AProperty.STRING_INPUT) {
                p.setValue(aValue.toString());
            } else if (p.getType() == ApplicationProperties.AProperty.DOUBLE_INPUT) {
                try {
                    p.setValue(Double.valueOf(aValue.toString()));
                } catch (Exception err) {
                    JOptionPane.showMessageDialog(new JFrame(), "Le param�tre " + aValue + " est incorrect");
                }
            } else if (p.getType() == ApplicationProperties.AProperty.BOOLEAN_INPUT) {
                p.setValue(Boolean.valueOf(aValue.toString()));
            } else if (p.getType() == ApplicationProperties.AProperty.STRING) {
            } else GuiLog.error("Type inconnu pour la propri�t�s " + p);
        }
    }

    /**
     * Si les cellules du tableaux sont �ditables
     */
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (this.isLocked) return false;
        ApplicationProperties.AProperty p = this.properties.getAllProperties()[rowIndex];
        if (columnIndex == 0) {
            return false;
        }
        if (p.getType() == ApplicationProperties.AProperty.STRING) return false;
        return true;
    }
}
