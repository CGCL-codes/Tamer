package org.lindenb.foafexplorer;

import java.awt.FontMetrics;
import java.awt.Graphics2D;
import javax.swing.JComponent;

/**
 * @author lindenb
 *
 */
public class GC {

    private Graphics2D g;

    int step;

    FontMetrics fm;

    JComponent observer;

    boolean paintRelationships;

    GC(Graphics2D g, JComponent observer) {
        this.g = g;
        this.observer = observer;
        this.fm = g.getFontMetrics();
        this.paintRelationships = true;
    }

    Graphics2D g() {
        return this.g;
    }
}
