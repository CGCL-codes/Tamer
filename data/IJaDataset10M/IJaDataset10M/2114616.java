package net.sourceforge.exclusive.client.gui.filetransferstatus.cellrenderer;

import java.awt.Component;
import java.awt.Font;
import java.awt.Toolkit;
import java.util.Random;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import net.sourceforge.exclusive.util.JProgressBarPieces;

public class FinishedCellRenderer extends JProgressBarPieces implements TableCellRenderer {

    private static final long serialVersionUID = -558588457457582237L;

    public FinishedCellRenderer() {
        super();
        int screenRes = Toolkit.getDefaultToolkit().getScreenResolution();
        int fontSize = (int) Math.round(13.0 * screenRes / 72.0);
        Font font = new Font("Arial", Font.BOLD, fontSize);
        this.setFont(font);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        boolean[] data = (boolean[]) value;
        Random rand = new Random(2);
        for (int i = 0; i < data.length; i++) data[i] = rand.nextBoolean();
        this.setWidth(table.getColumnModel().getColumn(column).getWidth());
        this.setHeight(table.getRowHeight(row));
        this.update(data);
        int downloaded = 0;
        for (boolean b : data) if (b) downloaded++;
        Double finished = new Double(downloaded) / new Double(data.length);
        if (Double.isNaN(finished) || Double.isInfinite(finished)) finished = 0.0;
        finished *= 100.0;
        String text = ((finished < 10.0) ? " " : "") + finished;
        if (text.length() < 4) text += ".0";
        text = text.substring(0, 4) + "%";
        this.setText(text);
        return this;
    }
}
