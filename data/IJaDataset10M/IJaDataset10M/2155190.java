package org.softsmithy.lib.swing.customizer;

import java.util.*;
import org.softsmithy.lib.swing.table.*;

/**
 *
 * @author  puce
 */
public abstract class AbstractCustomizerPropertyTableModel extends PropertyTableModel {

    /** Creates a new instance of AbstractCustomizerPropertyTableModel */
    public AbstractCustomizerPropertyTableModel(List properties, AbstractCustomizer activeCustomizer, String propertiesRBBaseName, Locale locale) {
        super(properties, activeCustomizer, propertiesRBBaseName, locale);
    }

    /**  This empty implementation is provided so users don't have to implement
   *  this method if their data model is not editable.
   *
   *  @param  aValue   value to assign to cell
   *  @param  rowIndex   row of cell
   *  @param  columnIndex  column of cell
   *
   */
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        super.setValueAt(aValue, rowIndex, columnIndex);
        getActiveCustomizer().repaint();
    }

    public AbstractCustomizer getActiveCustomizer() {
        return (AbstractCustomizer) getBean();
    }
}
