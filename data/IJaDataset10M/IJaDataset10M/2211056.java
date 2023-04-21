package diff.tela;

import java.awt.*;
import javax.swing.*;
import diff.misc.*;

public class FileOverview extends JPanel {

    private ParsedFile parsedFile = null;

    private boolean showNoMatchAsDeleted;

    public FileOverview(boolean showNoMatchAsDeleted) {
        this.showNoMatchAsDeleted = showNoMatchAsDeleted;
    }

    public void setParsedFile(ParsedFile parsedFile) {
        this.parsedFile = parsedFile;
        repaint();
    }

    public void paintComponent(Graphics g) {
        Dimension dim = getSize();
        g.setColor(Color.white);
        g.fillRect(0, 0, dim.width, dim.height);
        int lastY = 0;
        if (parsedFile != null) {
            Font font = getFont();
            FontMetrics fm = getFontMetrics(font);
            int lineHeight = fm.getHeight();
            FileLine[] lines = parsedFile.getLines();
            if (lines != null && lines.length > 0) {
                int w = dim.width;
                float hFloat = dim.height;
                float lCountFloat = lines.length;
                int hLine = (int) (hFloat / lCountFloat);
                int hLineUsed = Math.max(hLine, 3);
                hLineUsed = Math.min(hLineUsed, lineHeight);
                for (int i = 0; i < lines.length; i++) {
                    if (lines[i].getIndex() > -1) {
                        int yLine;
                        if (hLineUsed == lineHeight) {
                            yLine = (int) (i * lineHeight);
                        } else {
                            yLine = (int) (((float) dim.height) - ((float) (lines.length - i)) * hFloat / lCountFloat);
                        }
                        FileLine line = lines[i];
                        boolean display = false;
                        Color color = Color.white;
                        if (line.getStatus() == FileLine.NO_MATCH) {
                            if (showNoMatchAsDeleted) {
                                color = LineCellRenderer.INSERTED_COLOR;
                            } else {
                                color = LineCellRenderer.DELETED_COLOR;
                            }
                            display = true;
                        } else if (line.getStatus() == FileLine.MOVED) {
                            color = LineCellRenderer.MOVED_COLOR;
                            display = true;
                        } else if (line.getStatus() == FileLine.MODIFIED) {
                            color = LineCellRenderer.MODIFIED_COLOR;
                            display = true;
                        } else if (line.getStatus() == FileLine.DELETED_ON_OTHER_SIDE) {
                            color = LineCellRenderer.DELETED_COLOR;
                            display = true;
                        } else if (line.getStatus() == FileLine.INSERTED_ON_OTHER_SIDE) {
                            color = LineCellRenderer.INSERTED_COLOR;
                            display = true;
                        }
                        if (display) {
                            g.setColor(color);
                            for (int z = 0; z < dim.height && z <= hLineUsed; z++) {
                                g.drawLine(0, yLine + z, w, yLine + z);
                            }
                        }
                        lastY = yLine + hLineUsed - 1;
                    }
                }
            }
        } else {
            super.paintComponent(g);
        }
    }
}
