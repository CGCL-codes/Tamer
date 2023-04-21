package ch.iserver.ace.application;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

public class DocumentItemCellRenderer extends JPanel implements ListCellRenderer {

    private DocumentItem value;

    protected ImageIcon iconLocal, iconPublished, iconRemote;

    public DocumentItemCellRenderer(LocaleMessageSource messageSource) {
        iconLocal = messageSource.getIcon("iViewFileLocal");
        iconPublished = messageSource.getIcon("iViewFilePublic");
        iconRemote = messageSource.getIcon("iViewFileRemote");
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        setOpaque(true);
        this.value = (DocumentItem) value;
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

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Color textColor = g.getColor();
        int itemHeight = getHeight();
        int itemWidth = getWidth();
        int imageHeight = 16;
        int imageWidth = 16;
        int imagePosX = 1;
        int imagePosY = (itemHeight / 2) - (imageHeight / 2);
        switch(value.getType()) {
            case DocumentItem.LOCAL:
                g.drawImage(iconLocal.getImage(), imagePosX, imagePosY, imageHeight, imageWidth, this);
                break;
            case DocumentItem.PUBLISHED:
                g.drawImage(iconPublished.getImage(), imagePosX, imagePosY, imageHeight, imageWidth, this);
                break;
            case DocumentItem.REMOTE:
                g.drawImage(iconRemote.getImage(), imagePosX, imagePosY, imageHeight, imageWidth, this);
                break;
        }
        g.setColor(textColor);
        int textAscent = g.getFontMetrics().getAscent();
        int textDescent = g.getFontMetrics().getDescent();
        int textPosX = imagePosX + imageWidth + 5;
        int textPosY = (itemHeight / 2) + (textAscent / 2) - textDescent + 1;
        if (value.isDirty()) {
            g.drawString(value.getTitle() + " *", textPosX, textPosY);
        } else {
            g.drawString(value.getTitle(), textPosX, textPosY);
        }
    }

    public String getToolTipText() {
        return value.getToolTip();
    }

    public Dimension getPreferredSize() {
        return new Dimension(0, 20);
    }
}
