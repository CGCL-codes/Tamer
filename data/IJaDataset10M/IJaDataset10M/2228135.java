package org.fudaa.ebli.palette;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import org.fudaa.ebli.ressource.EbliResource;

/**
 * Une palette de formes.
 *
 * @version $Revision: 1.9 $ $Date: 2006-09-19 14:55:51 $ by $Author: deniger $
 * @author Axel von Arnim
 */
public class BPaletteForme extends JPanel implements ActionListener {

    JToggleButton[] boutons_;

    ButtonGroup bg_;

    GridLayout layout_;

    String[] icones_ = { "trait.gif", "ligne.gif", "polyg.gif", "rect.gif", "carre.gif", "ellip.gif", "cerc.gif", "main.gif", "courb.gif", "text.gif" };

    String[] alt_ = { "tr", "li", "po", "re", "ca", "el", "ce", "ma", "co", "te" };

    public BPaletteForme() {
        super();
        layout_ = new GridLayout(2, 5);
        setLayout(layout_);
        boutons_ = new JToggleButton[10];
        bg_ = new ButtonGroup();
        ImageIcon icon;
        for (int i = 0; i < boutons_.length; i++) {
            icon = EbliResource.EBLI.getIcon(icones_[i]);
            if (icon == null) {
                boutons_[i] = new JToggleButton(alt_[i]);
                final Font f = boutons_[i].getFont();
                boutons_[i].setFont(new Font(f.getName(), f.getStyle(), (int) (f.getSize() * 0.8)));
            } else {
                boutons_[i] = new JToggleButton(icon);
            }
            boutons_[i].setMargin(new Insets(1, 1, 1, 1));
            boutons_[i].setActionCommand("" + i);
            bg_.add(boutons_[i]);
            add(boutons_[i]);
            boutons_[i].addActionListener(this);
        }
    }

    public void actionPerformed(final ActionEvent _evt) {
        setTypeForme(Integer.parseInt(_evt.getActionCommand()));
    }

    private int typeForme_;

    public int getTypeForme() {
        return typeForme_;
    }

    public void setTypeForme(final int _typeForme) {
        if (typeForme_ != _typeForme) {
            final int vp = typeForme_;
            typeForme_ = _typeForme;
            firePropertyChange("TypeForme", vp, typeForme_);
        }
    }
}
