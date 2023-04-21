package hu.ihash.apps.dupecompare.view.images;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.border.LineBorder;

public class GroupedCellRenderer implements ListCellRenderer {

    private static final Color SELECTION_COLOR = new Color(120, 150, 240);

    private static final Color BACKGROUND_COLOR = Color.LIGHT_GRAY;

    private static final String STARTMARK = "<html>";

    private static final String ENDMARK = "</html>";

    private static final String SEPARATOR = "<br>";

    private static final int MAXLEN = 5;

    private JLabel result = new JLabel();

    private String label = null;

    private Object content = null;

    public GroupedCellRenderer() {
        result.setBorder(new LineBorder(Color.GRAY, 1));
        result.setOpaque(true);
        result.setFont(new Font("Sans", Font.PLAIN, 10));
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean hasFocus) {
        ArrayList alist = (ArrayList) value;
        int max = alist.size();
        int i = 0;
        label = STARTMARK;
        do {
            if (i > 0) label += SEPARATOR;
            content = alist.get(i++);
            if (content instanceof File) label += ((File) content).getName(); else label += content.toString();
        } while (i < max && i < MAXLEN);
        if (i < max) label += SEPARATOR + "...";
        label += ENDMARK;
        result.setText(label);
        if (isSelected) result.setBackground(SELECTION_COLOR); else result.setBackground(BACKGROUND_COLOR);
        return result;
    }
}
