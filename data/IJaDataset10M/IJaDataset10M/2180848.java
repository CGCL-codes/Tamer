package org.javizy.ui.widget;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.AbstractListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class HoursListModelAndRenderer extends AbstractListModel implements ListCellRenderer {

    private int fromhour = 8;

    private int tohour = 20;

    private int indexSubdivisions = 0;

    private int nbSubdivisions[] = { 1, 2, 3, 4, 5, 6, 12, 15, 20, 30, 60 };

    private JLabel rendLabel = new JLabel();

    private Color background = new Color(255, 253, 208);

    private static Font normalFont = Font.decode("ARIAL-12");

    /**
     * create HoursListModelAndRenderer for hours going from fromhour to tohour
     * with at least nb_subdivisions of the hour (will be modified if necessary
     * to have a round subdivision)
     * 
     * @param fromhour
     * @param tohour
     * @param nb_subdivisions
     */
    public HoursListModelAndRenderer(int fromhour, int tohour, int nb_subdivisions) {
        this.fromhour = fromhour;
        this.tohour = tohour;
        for (indexSubdivisions = 0; indexSubdivisions < nbSubdivisions.length; indexSubdivisions++) {
            if (nbSubdivisions[indexSubdivisions] >= nb_subdivisions) {
                break;
            }
        }
        rendLabel.setOpaque(true);
        rendLabel.setFont(normalFont);
    }

    private static final NumberFormat MINUTES_FORMAT = new DecimalFormat("00");

    @Override
    public Component getListCellRendererComponent(JList arg0, Object arg1, int row, boolean isSelected, boolean arg4) {
        int hour = getHour(row);
        int minute = getMinute(row);
        if (minute == 0) {
            rendLabel.setText(hour + "h");
            rendLabel.setHorizontalAlignment(JLabel.LEFT);
        } else {
            rendLabel.setText(MINUTES_FORMAT.format(minute));
            rendLabel.setHorizontalAlignment(JLabel.RIGHT);
        }
        if (isSelected) {
            rendLabel.setBackground(background.darker());
        } else {
            rendLabel.setBackground(background);
        }
        arg0.setBackground(background);
        return rendLabel;
    }

    private int getHour(int row) {
        return fromhour + row / nbSubdivisions[indexSubdivisions];
    }

    private int getMinute(int row) {
        return (60 * (row % nbSubdivisions[indexSubdivisions])) / nbSubdivisions[indexSubdivisions];
    }

    private Calendar cal = Calendar.getInstance();

    /**
     * return a new instance of date, cloned from date argument, with time set
     * to reflect time represented by row
     * 
     * @param date
     * @param row
     * @return new date, based on date arg
     */
    public Date setDate(Date date, int row) {
        cal.setTime(date);
        cal.set(Calendar.HOUR, getHour(row));
        cal.set(Calendar.MINUTE, getMinute(row));
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

    @Override
    public Object getElementAt(int row) {
        return getHour(row) + ":" + getMinute(row);
    }

    @Override
    public int getSize() {
        return 1 + (tohour - fromhour) * nbSubdivisions[indexSubdivisions];
    }

    /**
     * augment the resolution of chooser
     */
    public void increment() {
        indexSubdivisions++;
        if (indexSubdivisions >= nbSubdivisions.length) {
            indexSubdivisions = nbSubdivisions.length - 1;
        }
        super.fireContentsChanged(this, 0, getSize());
    }

    /**
     * decrease the resolution of chooser
     */
    public void decrement() {
        indexSubdivisions--;
        if (indexSubdivisions < 0) {
            indexSubdivisions = 0;
        }
        super.fireContentsChanged(this, 0, getSize());
    }

    /**
     * Get background (selected background is "darker")
     * 
     * @return Color
     */
    public Color getBackground() {
        return background;
    }

    /**
     * Set background (selected background is "darker")
     * 
     * @param background
     */
    public void setBackground(Color background) {
        this.background = background;
    }
}
