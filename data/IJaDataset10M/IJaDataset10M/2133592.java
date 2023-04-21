package dk.syscall.yamsie.ui.renderer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import dk.syscall.yamsie.model.Movie;

public class MovieCellRenderer implements ListCellRenderer {

    private static final long serialVersionUID = 1L;

    private static final int MAX_TITLE_LENGTH = 30;

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean hasFocus) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        Movie movie = (Movie) value;
        Color foreground = Color.WHITE;
        Font font = new Font("Arial", Font.PLAIN, 14);
        if (isSelected) {
            p.setBackground(Color.LIGHT_GRAY);
            foreground = Color.BLACK;
        } else {
            p.setBackground(list.getBackground());
            p.setForeground(list.getForeground());
        }
        JLabel thumbnail = new JLabel(new ImageIcon(movie.getSelectedPoster().getThumbnail()));
        thumbnail.setAlignmentX(Component.CENTER_ALIGNMENT);
        String trimmedTitle = movie.getTitle();
        if (trimmedTitle.length() > MAX_TITLE_LENGTH) {
            trimmedTitle = trimmedTitle.substring(0, MAX_TITLE_LENGTH - 3) + "...";
        }
        JLabel title = new JLabel(trimmedTitle);
        title.setForeground(foreground);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(font);
        p.add(thumbnail);
        p.add(title, BorderLayout.CENTER);
        return p;
    }
}
