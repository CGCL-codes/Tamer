package myComponents.myTableCellRenderers;

import database.EpisodesRecord;
import java.awt.Component;
import java.awt.image.BufferedImage;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import myComponents.MyUsefulFunctions;

/**
 *
 * @author ssoldatos
 */
public class MyDownloadedCellRenderer extends DefaultTableCellRenderer {

    private static final long serialVersionUID = 7897867559L;

    public static final String AVI = "avi";

    public static final String MKV = "mkv";

    public static final String MPG = "mpg";

    public static final String MP4 = "mp4";

    public static final String SAMPLE = "sample";

    public static final String OTHER = "other";

    public static final String NONE = "none";

    public static final int IMAGE_WIDTH = 27;

    public static final int IMAGE_HEIGHT = 16;

    public static final int GAP = 2;

    private final int episodeColumn;

    public MyDownloadedCellRenderer(int episodeColumn) {
        this.episodeColumn = episodeColumn;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (value instanceof Boolean) {
            Boolean val = (Boolean) value;
            setText("");
            if (val) {
                EpisodesRecord ep = (EpisodesRecord) table.getValueAt(row, this.episodeColumn);
                String[] types = MyUsefulFunctions.getVideoFileTypes(ep, true);
                if (types != null) {
                    setIcon(createIcon(types));
                } else {
                    setIcon(createIcon(new String[] { OTHER }));
                }
            } else {
                setIcon(null);
            }
        }
        setVerticalAlignment(SwingConstants.CENTER);
        return this;
    }

    private Icon createIcon(String[] types) {
        BufferedImage buff = new BufferedImage(types.length * IMAGE_WIDTH + types.length * GAP, IMAGE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < types.length; i++) {
            if (types[i].equals(NONE)) {
                types[i] = OTHER;
            }
            ImageIcon im = new ImageIcon(getClass().getResource("/images/" + types[i] + ".png"));
            buff.getGraphics().drawImage(im.getImage(), (i * IMAGE_WIDTH) + (i * GAP), 0, this);
        }
        return new ImageIcon(buff);
    }
}
