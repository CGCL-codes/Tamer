package jalview.gui;

import jalview.gui.schemes.*;
import jalview.gui.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class ColourChooserPanel extends Panel implements ActionListener, MouseListener {

    Vector cp;

    ColourPanel none;

    Color[] color;

    ColorSelectPanel cs;

    Hashtable hash;

    Frame f;

    AlignFrame af;

    AlignViewport av;

    Controller controller;

    public ColourChooserPanel(AlignFrame af, AlignViewport av, Controller controller, Color[] color) {
        this.af = af;
        this.av = av;
        this.color = color;
        this.cs = new ColorSelectPanel();
        this.controller = controller;
        hash = new Hashtable();
        makeHash(color);
        cp = new Vector(hash.size());
        Enumeration e = hash.keys();
        int count = 0;
        while (e.hasMoreElements()) {
            Color c = (Color) e.nextElement();
            String s = (String) hash.get(c);
            cp.addElement(new ColourPanel(c, s));
            count++;
        }
        Panel p = new Panel();
        p.setLayout(new GridLayout(cp.size() + 1, 1));
        for (int i = 0; i < cp.size(); i++) {
            p.add((ColourPanel) cp.elementAt(i));
            ((ColourPanel) cp.elementAt(i)).getButton().addActionListener(this);
            ((ColourPanel) cp.elementAt(i)).getCanvas().addMouseListener(this);
        }
        p.resize(250, (cp.size() + 1) * 50);
        p.invalidate();
        setLayout(new BorderLayout());
        add("Center", p);
        Panel p2 = new Panel();
        p2.setLayout(new BorderLayout());
        p2.add("Center", cs);
        cs.resize(size().width, 150);
        p2.resize(size().width, 150);
        add("South", p2);
    }

    public void makeHash(Color[] color) {
        for (int i = 0; i < color.length; i++) {
            if (hash.containsKey(color[i])) {
                String tmp = (String) hash.get(color[i]);
                String res = findResidueString(i);
                hash.put(color[i], tmp + res);
            } else {
                String res = findResidueString(i);
                hash.put(color[i], res);
            }
        }
    }

    public void updateHash() {
        for (int i = 0; i < cp.size(); i++) {
            Color tmp = ((ColourPanel) cp.elementAt(i)).cc.c;
            ((ColourPanel) cp.elementAt(i)).setString((String) hash.get(tmp));
        }
    }

    public String findResidueString(int i) {
        Enumeration e = ResidueProperties.aaSpecialsHash.keys();
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            int num = ((Integer) ResidueProperties.aaSpecialsHash.get(key)).intValue();
            if (i == num) {
                if (Config.DEBUG) System.out.println(" returning " + key + " for " + i);
                return key;
            }
        }
        e = ResidueProperties.aaHash.keys();
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            int num = ((Integer) ResidueProperties.aaHash.get(key)).intValue();
            if (i == num) {
                if (Config.DEBUG) System.out.println(" returning " + key + " for " + i);
                return key;
            }
        }
        System.out.println("No res string for " + i);
        return null;
    }

    public void makeArray() {
        Enumeration e = hash.keys();
        while (e.hasMoreElements()) {
            Color key = (Color) e.nextElement();
            String restr = (String) hash.get(key);
            String res;
            for (int i = 0; i < restr.length(); i++) {
                res = restr.substring(i, i + 1);
                int num = ResidueProperties.aaHash.size();
                try {
                    num = ((Integer) ResidueProperties.aaHash.get(res)).intValue();
                    color[num] = key;
                } catch (Exception ex) {
                }
            }
        }
    }

    public void changeColour(ColourPanel cp) {
        String oldres = cp.s;
        String resstr = cp.getString();
        for (int i = 0; i < oldres.length(); i++) {
            String tmp = oldres.substring(i, i + 1);
            if (resstr.indexOf(tmp) < 0) {
                hash.put(Color.white, hash.get(Color.white) + tmp);
            }
        }
        for (int i = 0; i < resstr.length(); i++) {
            String tmp = resstr.substring(i, i + 1);
            Enumeration e = hash.keys();
            while (e.hasMoreElements()) {
                Color key = (Color) e.nextElement();
                if (((String) hash.get(key)).indexOf(tmp) >= 0) {
                    String tmp2 = (String) hash.get(key);
                    tmp2 = tmp2.substring(0, tmp2.indexOf(tmp)) + tmp2.substring(tmp2.indexOf(tmp) + 1);
                    hash.put(key, tmp2);
                }
            }
        }
        hash.put(cp.cc.c, resstr);
        updateHash();
    }

    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() instanceof Button) {
            Button b = (Button) evt.getSource();
            if (b.getParent() instanceof ColourPanel) {
                ColourPanel tmp = (ColourPanel) b.getParent();
                changeColour(tmp);
                if (af != null) {
                    makeArray();
                    System.out.println("NOTE: Color setting not implemented");
                    controller.handleAlignViewportEvent(new AlignViewportEvent(this, av, AlignViewportEvent.COLOURING));
                }
            }
        } else if (evt.getSource() instanceof TextField) {
            return;
        }
    }

    public void mouseReleased(MouseEvent evt) {
    }

    public void mouseEntered(MouseEvent evt) {
    }

    public void mouseExited(MouseEvent evt) {
    }

    public void mouseClicked(MouseEvent evt) {
    }

    public void mousePressed(MouseEvent evt) {
        if (evt.getSource() instanceof ColourCanvas) {
            ColourCanvas cc = (ColourCanvas) evt.getSource();
            if ((evt.getModifiers() & Event.META_MASK) != 0) {
                cs.setColor(cc.c);
                cs.colorPanel.pogCanvas.paint(cs.colorPanel.pogCanvas.getGraphics());
            } else {
                Color c = cs.getColor();
                cc.c = cs.getColor();
                cc.repaint();
            }
        }
    }

    public Dimension getPreferredSize() {
        return new Dimension(((ColourPanel) cp.elementAt(0)).getPreferredSize().width, cp.size() * ((ColourPanel) cp.elementAt(0)).getPreferredSize().height + 150);
    }

    public Dimension getMinimumSize() {
        return getPreferredSize();
    }
}
