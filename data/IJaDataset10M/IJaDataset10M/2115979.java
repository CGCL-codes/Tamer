package ch.laoe.plugin;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import ch.laoe.clip.ALayer;
import ch.laoe.ui.Debug;
import ch.laoe.ui.GColorPicker;
import ch.laoe.ui.GGraphicObjects;
import ch.laoe.ui.GLanguage;
import ch.laoe.ui.GToolkit;
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


Class:			GPGraphicDocumentation
Autor:			olivier g�umann, neuch�tel (switzerland)
JDK:				1.4.2

Desctription:	
plugin to draw graphic documentation onto the clip

mouse action table of segment mode:
---------------------------------------------------------
-				press-drag-release	draw freehand line	
shift
ctrl		
---------------------------------------------------------


History:
Date:			Description:									Autor:
06.05.2003	first draft										oli4

***********************************************************/
public class GPGraphicDocumentation extends GPluginFrame {

    public GPGraphicDocumentation(GPluginHandler ph) {
        super(ph);
        initGui();
    }

    public String getName() {
        return "graphicDoc";
    }

    public void start() {
        super.start();
        repaintFocussedClipEditor();
        reloadGui();
    }

    public void reload() {
        super.reload();
        lineButton.setSelected(GGraphicObjects.getOperation() == GGraphicObjects.DRAW_LINE);
        rectangleButton.setSelected(GGraphicObjects.getOperation() == GGraphicObjects.DRAW_RECTANGLE);
        textButton.setSelected(GGraphicObjects.getOperation() == GGraphicObjects.DRAW_TEXT);
        color.setSelectedColor(GGraphicObjects.getCurrentColor());
        repaintFocussedClipEditor();
        reloadGui();
    }

    protected boolean isFocussingMouseEvents() {
        return true;
    }

    private GGraphicObjects graphicObjects = null;

    /**
	 *	mouse events
	 */
    public void mousePressed(MouseEvent e) {
        ALayer l = getFocussedClip().getSelectedLayer();
        int chi = l.getPlotter().getInsideChannelIndex(e.getPoint());
        if (chi >= 0) {
            graphicObjects = l.getChannel(chi).getGraphicObjects();
        }
        try {
            if (graphicObjects != null) {
                if (GGraphicObjects.getOperation() == GGraphicObjects.DRAW_TEXT) {
                    String t = JOptionPane.showInputDialog(GLanguage.translate("enterText"));
                    if (t == null) {
                        t = "";
                    }
                    GGraphicObjects.setCurrentText(t);
                }
                graphicObjects.mousePressed(e);
            }
            reloadGui();
            repaintFocussedClipEditor();
        } catch (Exception exc) {
        }
    }

    public void mouseMoved(MouseEvent e) {
        try {
            if (graphicObjects != null) {
                graphicObjects.mouseMoved(e);
            }
            reloadGui();
            repaintFocussedClipEditor();
        } catch (Exception exc) {
        }
    }

    public void mouseDragged(MouseEvent e) {
        if (graphicObjects != null) {
            graphicObjects.mouseDragged(e);
        }
        reloadGui();
        repaintFocussedClipEditor();
    }

    public void mouseReleased(MouseEvent e) {
        if (graphicObjects != null) {
            graphicObjects.mouseReleased(e);
        }
        {
            reloadGui();
            repaintFocussedClipEditor();
        }
    }

    public void mouseClicked(MouseEvent e) {
        if (graphicObjects != null) {
            graphicObjects.mouseClicked(e);
        }
        reloadGui();
        repaintFocussedClipEditor();
    }

    public void mouseEntered(MouseEvent e) {
        if (graphicObjects != null) {
            graphicObjects.mouseEntered(e);
        }
        reloadGui();
        repaintFocussedClipEditor();
    }

    /**
	 *	graphics
	 */
    public void paintOntoClip(Graphics2D g2d, Rectangle rect) {
        try {
            ALayer l = getSelectedLayer();
            for (int i = 0; i < l.getNumberOfChannels(); i++) {
                l.getChannel(i).getGraphicObjects().paintOntoClip(g2d, rect);
            }
        } catch (Exception exc) {
        }
    }

    /**
	 *	GUI
	 */
    private JToggleButton lineButton, rectangleButton, textButton, eraseButton;

    private GColorPicker color;

    private EventDispatcher eventDispatcher;

    private void initGui() {
        JPanel p = new JPanel();
        UiCartesianLayout l = new UiCartesianLayout(p, 4, 2);
        l.setPreferredCellSize(new Dimension(35, 35));
        p.setLayout(l);
        lineButton = new JToggleButton(GToolkit.loadIcon(this, "drawLine"));
        lineButton.setToolTipText(GLanguage.translate("drawLine"));
        p.add(lineButton, new Rectangle(0, 0, 1, 1));
        rectangleButton = new JToggleButton(GToolkit.loadIcon(this, "drawRectangle"));
        rectangleButton.setToolTipText(GLanguage.translate("drawRectangle"));
        p.add(rectangleButton, new Rectangle(1, 0, 1, 1));
        textButton = new JToggleButton(GToolkit.loadIcon(this, "drawText"));
        textButton.setToolTipText(GLanguage.translate("drawText"));
        p.add(textButton, new Rectangle(2, 0, 1, 1));
        eraseButton = new JToggleButton(GToolkit.loadIcon(this, "erase"));
        eraseButton.setToolTipText(GLanguage.translate("erase"));
        p.add(eraseButton, new Rectangle(3, 0, 1, 1));
        ButtonGroup bg = new ButtonGroup();
        bg.add(lineButton);
        bg.add(rectangleButton);
        bg.add(textButton);
        bg.add(eraseButton);
        color = new GColorPicker(GLanguage.translate("color"), GLanguage.translate("color"));
        p.add(color, new Rectangle(0, 1, 3, 1));
        frame.getContentPane().add(p);
        pack();
        eventDispatcher = new EventDispatcher();
        lineButton.addActionListener(eventDispatcher);
        rectangleButton.addActionListener(eventDispatcher);
        textButton.addActionListener(eventDispatcher);
        eraseButton.addActionListener(eventDispatcher);
        color.addActionListener(eventDispatcher);
    }

    private void reloadGui() {
    }

    private class EventDispatcher implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == lineButton) {
                Debug.println(1, "plugin " + getName() + " [line] clicked");
                GGraphicObjects.setOperation(GGraphicObjects.DRAW_LINE);
            } else if (e.getSource() == rectangleButton) {
                Debug.println(1, "plugin " + getName() + " [rectangle] clicked");
                GGraphicObjects.setOperation(GGraphicObjects.DRAW_RECTANGLE);
            } else if (e.getSource() == textButton) {
                Debug.println(1, "plugin " + getName() + " [text] clicked");
                GGraphicObjects.setOperation(GGraphicObjects.DRAW_TEXT);
            } else if (e.getSource() == eraseButton) {
                Debug.println(1, "plugin " + getName() + " [erase] clicked");
                GGraphicObjects.setOperation(GGraphicObjects.ERASE);
            } else if (e.getSource() == color) {
                Debug.println(1, "plugin " + getName() + " [color chooser] clicked");
                GGraphicObjects.setCurrentColor(color.getSelectedColor());
            }
        }
    }
}
