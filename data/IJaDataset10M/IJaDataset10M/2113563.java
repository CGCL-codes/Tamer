package com.rapidminer.gui.look.ui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import com.rapidminer.gui.look.InternalFrameTitlePane;
import com.rapidminer.gui.look.borders.Borders;

/**
 * The UI for internal frames.
 *
 * @author Ingo Mierswa
 */
public class InternalFrameUI extends BasicInternalFrameUI {

    public static ComponentUI createUI(JComponent c) {
        return new InternalFrameUI((JInternalFrame) c);
    }

    public InternalFrameUI(JInternalFrame b) {
        super(b);
    }

    @Override
    public void installDefaults() {
        super.installDefaults();
        this.frame.setBorder(Borders.getInternalFrameBorder());
    }

    @Override
    public void installUI(JComponent c) {
        super.installUI(c);
        c.setOpaque(false);
    }

    @Override
    public void uninstallDefaults() {
        this.frame.setBorder(null);
        super.uninstallDefaults();
    }

    @Override
    protected LayoutManager createLayoutManager() {
        return new BasicInternalFrameUI.InternalFrameLayout() {

            @Override
            public void layoutContainer(Container c) {
                Insets i = InternalFrameUI.this.frame.getInsets();
                int cx = i.left;
                int cy = 0;
                int cw = InternalFrameUI.this.frame.getWidth() - i.left - i.right;
                int ch = InternalFrameUI.this.frame.getHeight() - i.bottom;
                if (getNorthPane() != null) {
                    Dimension size = getNorthPane().getPreferredSize();
                    getNorthPane().setBounds(0, 0, InternalFrameUI.this.frame.getWidth(), size.height);
                    cy += size.height;
                    ch -= size.height;
                }
                if (getSouthPane() != null) {
                    Dimension size = getSouthPane().getPreferredSize();
                    getSouthPane().setBounds(cx, InternalFrameUI.this.frame.getHeight() - i.bottom - size.height, cw, size.height);
                    ch -= size.height;
                }
                if (getWestPane() != null) {
                    Dimension size = getWestPane().getPreferredSize();
                    getWestPane().setBounds(cx, cy, size.width, ch);
                    cw -= size.width;
                    cx += size.width;
                }
                if (getEastPane() != null) {
                    Dimension size = getEastPane().getPreferredSize();
                    getEastPane().setBounds(cw - size.width, cy, size.width, ch);
                    cw -= size.width;
                }
                if (InternalFrameUI.this.frame.getRootPane() != null) {
                    InternalFrameUI.this.frame.getRootPane().setBounds(cx, cy, cw, ch);
                }
            }
        };
    }

    @Override
    protected JComponent createNorthPane(JInternalFrame w) {
        this.titlePane = new InternalFrameTitlePane(w);
        return this.titlePane;
    }
}
