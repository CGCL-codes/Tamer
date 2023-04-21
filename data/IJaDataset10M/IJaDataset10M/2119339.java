package neon.tools;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import neon.objects.resources.RData;

@SuppressWarnings("serial")
public class ModCellRenderer extends JLabel implements ListCellRenderer<RData> {

    /**
	 * Returns this renderer with the right properties (color, font, background color).
	 * If the cell does not contain a resource from the currently active mod, the
	 * text is rendered in italics.
	 * 
	 * @param list			the list that contains the cell
	 * @param value			the object that is contained in the cell
	 * @param index			the index of the cell in the list
	 * @param isSelected	whether the cell is selected
	 * @param cellHasFocus	whether the cell has keyboard focus
	 * @return				this <code>ModCellRenderer</code>
	 */
    public Component getListCellRendererComponent(JList<? extends RData> list, RData value, int index, boolean isSelected, boolean cellHasFocus) {
        setOpaque(true);
        String text = value.toString();
        if (value.getPath()[0].equals(Editor.getStore().getActive().get("id"))) {
            setText(text);
        } else {
            setText("<html><i>" + text + "</i></html>");
        }
        setBackground(isSelected ? Color.blue : Color.white);
        setForeground(isSelected ? Color.white : Color.black);
        return this;
    }
}
