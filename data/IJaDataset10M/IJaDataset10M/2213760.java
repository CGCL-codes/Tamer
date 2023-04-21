package ch.iserver.ace.application;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.BorderFactory;
import javax.swing.Scrollable;
import java.awt.Rectangle;

public class BrowseItemCellRenderer extends JPanel implements ListCellRenderer {

    private BrowseItem value;

    private LocaleMessageSource messageSource;

    protected ImageIcon iconRemote;

    public BrowseItemCellRenderer(LocaleMessageSource messageSource) {
        this.messageSource = messageSource;
        iconRemote = messageSource.getIcon("iViewFileRemote");
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        setOpaque(true);
        this.value = (BrowseItem) value;
        if (isSelected) {
            setForeground(list.getSelectionForeground());
            setBackground(list.getSelectionBackground());
            setBorder(BorderFactory.createLineBorder(list.getSelectionBackground().darker(), 1));
        } else {
            setForeground(list.getForeground());
            setBackground(list.getBackground());
            setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        }
        return this;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Color textColor = g.getColor();
        int itemHeight = getHeight();
        int itemWidth = getWidth();
        int imageHeight = 16;
        int imageWidth = 16;
        int imagePosX = 1;
        int imagePosY = (itemHeight / 2) - (imageHeight / 2);
        g.drawImage(iconRemote.getImage(), imagePosX, imagePosY, imageHeight, imageWidth, this);
        g.setColor(textColor);
        int textAscent = g.getFontMetrics().getAscent();
        int textDescent = g.getFontMetrics().getDescent();
        int textPosX = imagePosX + imageWidth + 5;
        int textTitlePosY = (itemHeight / 2) + (textAscent / 2) - textDescent - 3;
        g.drawString(value.getTitle(), textPosX, textTitlePosY);
        int textPublisherPosY = textTitlePosY + 12;
        g.setFont(g.getFont().deriveFont(10.0f));
        g.setFont(g.getFont().deriveFont(Font.PLAIN & Font.BOLD));
        g.drawString(value.getPublisher(), textPosX, textPublisherPosY);
    }

    public String getToolTipText() {
        return value.getTitle();
    }

    public Dimension getPreferredSize() {
        return new Dimension(0, 30);
    }
}
