package org.jens.cis.gui;

import java.awt.Component;
import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import org.jens.Shorthand.swing.JBrowserLabel;
import org.jens.cis.wikiParser.Skill;

public class SkillTableWikiCellEditor extends AbstractCellEditor implements TableCellEditor {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    JBrowserLabel lbl;

    public SkillTableWikiCellEditor() {
        lbl = new JBrowserLabel();
        lbl.setText("Gehe zum Wiki");
        lbl.setURL("http://www.google.de");
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        Skill skill = (Skill) value;
        lbl.setURL(skill.getWikiPage());
        return lbl;
    }

    @Override
    public Object getCellEditorValue() {
        return null;
    }
}
