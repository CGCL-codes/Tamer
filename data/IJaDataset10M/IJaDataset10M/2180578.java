package org.shake.lastfm.ui.artist;

import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JList;
import org.shake.lastfm.Resources;
import org.shake.lastfm.model.Picture;
import org.shake.lastfm.model.Picture.PictureSize;
import org.shake.lastfm.swing.AnimatedIcon;
import org.shake.lastfm.swing.PicturePanel;

public class ArtistImageCellRenderer extends DefaultListCellRenderer {

    private final PictureSize size;

    private AnimatedIcon busyRenderer;

    private PicturePanel picturePanel;

    public ArtistImageCellRenderer(PictureSize size) {
        super();
        this.size = size;
        this.busyRenderer = new AnimatedIcon(Resources.getIcon("image.animated"));
        this.picturePanel = new PicturePanel(128, 128);
        setOpaque(false);
    }

    private static final long serialVersionUID = 313478514145164437L;

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, "", index, isSelected, cellHasFocus);
        if (value instanceof Picture) {
            Picture pic = (Picture) value;
            ImageIcon image = pic.getImage(this.size);
            if (image != null) {
                this.picturePanel.setImage(image);
                this.picturePanel.setTitle(pic.getTitle());
                return this.picturePanel;
            } else {
                setIcon(this.busyRenderer);
            }
        }
        return this;
    }
}
