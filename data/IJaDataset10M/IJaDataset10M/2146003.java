package org.vizzini.ui.game.cardgame;

import org.vizzini.game.cardgame.PokerCard;
import org.vizzini.ui.ApplicationSupport;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.ImageIcon;

/**
 * Provides a card center widget which uses an icon for use in card game user
 * interfaces.
 *
 * @author   Jeffrey M. Thompson
 * @version  v0.4
 * @since    v0.1
 */
public class CardCenterIconUI extends AbstractCardCenterUI {

    /** Serial version UID. */
    private static final long serialVersionUID = 1L;

    /** Border width. */
    private int _borderWidth = 1;

    /** Court card icon. */
    private ImageIcon _icon;

    /** Icon bounds. */
    private Rectangle _iconBounds = new Rectangle();

    /**
     * Construct this object.
     *
     * @since  v0.1
     */
    public CardCenterIconUI() {
        setIconFrom("ICON_vizzini");
        ComponentListener listener = new ComponentAdapter() {

            @Override
            public void componentResized(ComponentEvent event) {
                computeParameters();
            }
        };
        addComponentListener(listener);
    }

    /**
     * @see  javax.swing.JComponent#getPreferredSize()
     */
    @Override
    public Dimension getPreferredSize() {
        Dimension size = super.getPreferredSize();
        if (_icon != null) {
            double widthToHeight = (1.0 * _icon.getIconWidth()) / _icon.getIconHeight();
            size.width = _icon.getIconWidth();
            size.height = (int) (_icon.getIconWidth() / widthToHeight);
        }
        return size;
    }

    /**
     * @see  javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (_icon != null) {
            g.drawImage(_icon.getImage(), _iconBounds.x, _iconBounds.y, _iconBounds.width, _iconBounds.height, this);
            if (isFaceUp() && (_borderWidth > 0)) {
                int width = _iconBounds.width + (1 * _borderWidth);
                int height = _iconBounds.height + (1 * _borderWidth);
                g.setColor(Color.BLACK);
                g.drawRect(_iconBounds.x - _borderWidth, _iconBounds.y - _borderWidth, width, height);
            }
        }
    }

    /**
     * @see  org.vizzini.ui.game.cardgame.AbstractCardCenterUI#setCardName(java.lang.String)
     */
    @Override
    public void setCardName(String name) {
        super.setName(name);
        if (isFaceUp()) {
            int index = PokerCard.getIndex(name);
            switch(index) {
                case 11:
                    setIconFrom("ICON_jack");
                    break;
                case 12:
                    setIconFrom("ICON_queen");
                    break;
                case 13:
                    setIconFrom("ICON_king");
                    break;
                default:
                    throw new IllegalArgumentException("unknown index " + index);
            }
        }
    }

    /**
     * @see  org.vizzini.ui.game.cardgame.AbstractCardCenterUI#setFaceUp(boolean)
     */
    @Override
    public void setFaceUp(boolean isFaceUp) {
        super.setFaceUp(isFaceUp);
        if (isFaceUp) {
            setCardName(getName());
        } else {
            setIconFrom("ICON_vizzini");
        }
        repaint();
    }

    /**
     * Compute size parameters.
     *
     * @since  v0.1
     */
    protected void computeParameters() {
        if (_icon != null) {
            Dimension size = getSize();
            if (size.height > 0) {
                double widthToHeight = (1.0 * size.width) / size.height;
                double iconWidthToHeight = (1.0 * _icon.getIconWidth()) / _icon.getIconHeight();
                if (widthToHeight < iconWidthToHeight) {
                    _iconBounds.width = size.width;
                    _iconBounds.height = (int) (size.width / iconWidthToHeight);
                } else {
                    _iconBounds.height = size.height;
                    _iconBounds.width = (int) (size.height * iconWidthToHeight);
                }
                _iconBounds.width -= (2 * _borderWidth);
                _iconBounds.height -= (2 * _borderWidth);
                _iconBounds.x = (int) ((size.width - _iconBounds.width) / 2.0);
                _iconBounds.y = (int) ((size.height - _iconBounds.height) / 2.0);
                repaint();
            }
        }
    }

    /**
     * Set the given icon.
     *
     * @param  iconName  Icon name.
     *
     * @since  v0.1
     */
    protected void setIconFrom(String iconName) {
        _icon = (ImageIcon) ApplicationSupport.getIcon(iconName);
    }
}
