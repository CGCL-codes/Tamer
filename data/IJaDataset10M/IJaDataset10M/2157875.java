package com.bluebrim.layout.impl.shared.view;

import java.awt.*;

/**
 * Class for rendering color capability in the form of a row of colored boxes.
 * Also see CoSingleColorCapabilityRenderer
 * 
 * @author: Dennis
 */
public class CoColorCapabilityRenderer {

    private CoSingleColorCapabilityRenderer[] m_renderers;

    public float getWidth() {
        return m_renderers.length * CoSingleColorCapabilityRenderer.SIZE;
    }

    public void set(boolean hasBlack, boolean hasCyan, boolean hasMagenta, boolean hasYellow, boolean hasFourColor, boolean hasOneOptionalColor, boolean hasTwoOptionalColors) {
        if (hasFourColor) {
            m_renderers = new CoSingleColorCapabilityRenderer[4];
            m_renderers[0] = new CoSingleColorCapabilityRenderer(Color.black);
            m_renderers[1] = new CoSingleColorCapabilityRenderer(Color.cyan);
            m_renderers[2] = new CoSingleColorCapabilityRenderer(Color.yellow);
            m_renderers[3] = new CoSingleColorCapabilityRenderer(Color.magenta);
        } else {
            int count = 0;
            if (hasBlack) count++;
            if (hasCyan) count++;
            if (hasMagenta) count++;
            if (hasYellow) count++;
            if (hasOneOptionalColor) count++; else if (hasTwoOptionalColors) count += 2;
            m_renderers = new CoSingleColorCapabilityRenderer[(count == 1) ? 2 : count];
            int i = 0;
            if (hasBlack) m_renderers[i++] = new CoSingleColorCapabilityRenderer(Color.black);
            if (hasCyan) m_renderers[i++] = new CoSingleColorCapabilityRenderer(Color.cyan);
            if (hasMagenta) m_renderers[i++] = new CoSingleColorCapabilityRenderer(Color.magenta);
            if (hasYellow) m_renderers[i++] = new CoSingleColorCapabilityRenderer(Color.yellow);
            if (hasOneOptionalColor) m_renderers[i++] = new CoSingleColorCapabilityRenderer(); else if (hasTwoOptionalColors) {
                m_renderers[i++] = new CoSingleColorCapabilityRenderer();
                m_renderers[i++] = new CoSingleColorCapabilityRenderer();
            }
            if (count == 1) {
                m_renderers[1] = new CoSingleColorCapabilityRenderer(Color.white);
            }
        }
    }

    public void paint(Graphics2D g) {
        g.setStroke(new BasicStroke(0));
        for (int i = 0; i < m_renderers.length; i++) {
            m_renderers[i].paint(g, i);
        }
    }
}
