package ch.laoe.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import ch.laoe.clip.AClip;
import ch.laoe.clip.ALayer;
import ch.oli4.ui.UiCartesianLayout;

/***********************************************************

This file is part of LAoE.

LAoE is free software; you can redistribute it and/or modify it
under the terms of the GNU General Public License as published
by the Free Software Foundation; either version 2 of the License,
or (at your option) any later version.

LAoE is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with LAoE; if not, write to the Free Software Foundation,
Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA


Class:			GClipLayerChooser
Autor:			olivier g�umann, neuch�tel (switzerland)
JDK:				1.3

Desctription:	a GUI component to choose one layer from 
					any open clip.  

History:
Date:			Description:									Autor:
25.10.00		erster Entwurf									oli4

***********************************************************/
public class GClipLayerChooser extends JPanel implements ActionListener {

    public GClipLayerChooser(Laoe m) {
        main = m;
        initGui("layer");
    }

    public GClipLayerChooser(Laoe m, String title) {
        main = m;
        initGui(title);
    }

    private Laoe main;

    private JComboBox paramLayer, paramClip;

    private void initGui(String title) {
        setLayout(new BorderLayout());
        JPanel pInner = new JPanel();
        UiCartesianLayout ct = new UiCartesianLayout(pInner, 10, 2);
        pInner.setLayout(ct);
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), GLanguage.translate(title)));
        ct.add(new JLabel(GLanguage.translate("clip") + ":"), 0, 0, 3, 1);
        paramClip = new JComboBox();
        ct.add(paramClip, 3, 0, 7, 1);
        ct.add(new JLabel(GLanguage.translate("layer") + ":"), 0, 1, 3, 1);
        paramLayer = new JComboBox();
        ct.add(paramLayer, 3, 1, 7, 1);
        add(pInner, BorderLayout.CENTER);
        paramClip.addActionListener(this);
        paramLayer.addActionListener(this);
        reload();
    }

    private void updateParameterClip() {
        Object c[] = main.getAllClipFrames();
        if (c != null) {
            int maxIndex = c.length;
            int index = paramClip.getSelectedIndex();
            paramClip.removeAllItems();
            for (int i = 0; i < maxIndex; i++) {
                paramClip.addItem(((GClipFrame) c[i]).getClipEditor().getClip());
            }
            if (index < maxIndex) paramClip.setSelectedIndex(index);
        }
    }

    private void updateParameterLayer() {
        AClip c = (AClip) paramClip.getSelectedItem();
        if (c != null) {
            int maxIndex = c.getNumberOfLayers();
            int index = paramLayer.getSelectedIndex();
            paramLayer.removeAllItems();
            for (int i = 0; i < maxIndex; i++) {
                paramLayer.addItem(c.getLayer(i));
            }
            if (index < maxIndex) paramLayer.setSelectedIndex(index);
        }
    }

    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == paramClip) {
                updateParameterLayer();
            } else if (e.getSource() == paramLayer) {
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    /**
	*	returns the actually selected layer
	*/
    public ALayer getSelectedLayer() {
        return (ALayer) paramLayer.getSelectedItem();
    }

    /**
	*	set the layerchooser enabled
	*/
    public void setEnabled(boolean b) {
        paramClip.setEnabled(b);
        paramLayer.setEnabled(b);
        repaint();
    }

    /**
	*	set the layerchooser enabled
	*/
    public boolean isEnabled() {
        return paramClip.isEnabled();
    }

    /**
	*	reload the actual clips and layers
	*/
    public void reload() {
        updateParameterClip();
        updateParameterLayer();
    }
}
