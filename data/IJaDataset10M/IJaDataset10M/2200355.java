package no.ugland.utransprod.gui.model;

import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellRenderer;
import no.ugland.utransprod.model.ProcentDone;
import no.ugland.utransprod.util.Util;

public class TextPaneRendererProcentDone extends JTextField implements TableCellRenderer {

    private static final int MAX_LENGHT = 50;

    public final Component getTableCellRendererComponent(final JTable table, final Object object, final boolean isSelected, final boolean hasFocus, final int row, final int column) {
        setBorder(BorderFactory.createEmptyBorder());
        if (object != null) {
            ProcentDone procentDone = (ProcentDone) object;
            StringBuffer toolTip = new StringBuffer();
            setText(Util.convertIntegerToString(procentDone.getProcent()) + "%");
            setCommentAsTooltip(procentDone.getProcentDoneComment(), toolTip);
            if (toolTip.length() > 0) {
                toolTip.insert(0, "<html>");
                toolTip.append("</html>");
                setToolTipText(toolTip.toString());
            } else {
                setToolTipText("");
            }
            setBackgrounAndForeground(table, isSelected);
        } else {
            setText("");
            setToolTipText("");
            setBackgrounAndForeground(table, isSelected);
        }
        return this;
    }

    private void setCommentAsTooltip(final String comment, final StringBuffer toolTip) {
        if (comment != null && comment.length() != 0) {
            addCommentToTooltip(toolTip, comment);
        }
    }

    private void addCommentToTooltip(final StringBuffer toolTip, final String comment) {
        String multilineComment = Util.splitLongStringIntoLinesWithBr(comment, MAX_LENGHT);
        toolTip.append(multilineComment);
    }

    private void setBackgrounAndForeground(final JTable table, final boolean isSelected) {
        if (isSelected) {
            setBackground(table.getSelectionBackground());
            setForeground(table.getSelectionForeground());
        } else {
            setBackground(table.getBackground());
            setForeground(table.getForeground());
        }
    }
}
