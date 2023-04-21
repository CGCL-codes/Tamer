package com.codename1.ui.painter;

import com.codename1.ui.Form;
import com.codename1.ui.Graphics;
import com.codename1.ui.Painter;
import com.codename1.ui.geom.Rectangle;
import java.util.Vector;

/**
 * A painter chain allows us to chain together several painters to provide a
 * "layer" effect where each painter only draws one element.
 *
 * @author Shai Almog
 */
public class PainterChain implements Painter {

    private Painter[] chain;

    /**
     * Create a new painter chain which will paint all of the elements in the chain
     * in sequence from 0 to the last element
     * 
     * @param chain the chain of components in the painter
     */
    public PainterChain(Painter[] chain) {
        this.chain = chain;
    }

    /**
     * Create a new painter chain which will paint all of the elements in the chain
     * in sequence from 0 to the last element
     *
     * @param painter the chain first Painter
     */
    public PainterChain(Painter painter) {
        this.chain = new Painter[] { painter };
    }

    /**
     * Creates a new chain based on the existing chain with the new element added
     * at the end
     * 
     * @param p new painter
     * @return new chain element
     */
    public PainterChain addPainter(Painter p) {
        if (chain.length != 0) {
            Painter[] newChain = new Painter[chain.length + 1];
            System.arraycopy(chain, 0, newChain, 0, chain.length);
            newChain[chain.length] = p;
            return new PainterChain(newChain);
        }
        return new PainterChain(new Painter[] { p });
    }

    /**
     * Creates a new chain based on the existing chain with the new element added
     * at the beginning
     * 
     * @param p new painter
     * @return new chain element
     */
    public PainterChain prependPainter(Painter p) {
        Painter[] newChain = new Painter[chain.length + 1];
        System.arraycopy(chain, 1, newChain, 0, chain.length);
        newChain[0] = p;
        return new PainterChain(newChain);
    }

    /**
     * @inheritDoc
     */
    public void paint(Graphics g, Rectangle rect) {
        for (int iter = 0; iter < chain.length; iter++) {
            chain[iter].paint(g, rect);
        }
    }

    /**
     * Installs a glass pane on the given form making sure to make it a painter
     * chain only if required by existing painter
     * 
     * @param f form on which to install the chain
     * @param p painter to install
     */
    public static void installGlassPane(Form f, Painter p) {
        Painter existing = f.getGlassPane();
        if (existing == null) {
            f.setGlassPane(p);
            return;
        }
        if (existing instanceof PainterChain) {
            f.setGlassPane(((PainterChain) existing).addPainter(p));
        } else {
            PainterChain pc = new PainterChain(new Painter[] { existing, p });
            f.setGlassPane(pc);
        }
    }

    /**
     * Allows us to traverse the painter chain
     * 
     * @return the internal painter chain
     */
    public Painter[] getChain() {
        return chain;
    }

    /**
     * Removes a glass pane from the given form, this is the opposite operation for the
     * install glass pane
     * 
     * @param f form from which to remove the chain
     * @param p painter to remove
     */
    public static void removeGlassPane(Form f, Painter p) {
        Painter existing = f.getGlassPane();
        if (existing == null) {
            return;
        }
        if (existing == p) {
            f.setGlassPane(null);
            return;
        }
        if (existing instanceof PainterChain) {
            PainterChain pc = (PainterChain) existing;
            if (pc.chain.length == 1) {
                f.setGlassPane(null);
            } else {
                Vector v = new Vector();
                for (int iter = 0; iter < pc.chain.length; iter++) {
                    if (pc.chain[iter] != p) {
                        v.addElement(pc.chain[iter]);
                    }
                }
                if (v.size() == 0) {
                    f.setGlassPane(null);
                    return;
                }
                Painter[] newChain = new Painter[v.size()];
                for (int iter = 0; iter < newChain.length; iter++) {
                    newChain[iter] = (Painter) v.elementAt(iter);
                }
                pc.chain = newChain;
                f.repaint();
            }
        }
    }
}
