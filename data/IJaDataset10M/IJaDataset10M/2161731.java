package com.rapidminer.gui.look.ui;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTextPaneUI;
import com.rapidminer.gui.look.ClipboardActionsPopup;

/**
 * The UI for text panes.
 *
 * @author Ingo Mierswa
 */
public class TextPaneUI extends BasicTextPaneUI {

    private class TextPanePopupListener extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            evaluateClick(e);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            evaluateClick(e);
        }

        private void evaluateClick(MouseEvent e) {
            if (e.isPopupTrigger()) {
                showPopup(e.getPoint());
            }
        }
    }

    private ClipboardActionsPopup popup = null;

    private TextPanePopupListener popupListener = new TextPanePopupListener();

    public static ComponentUI createUI(JComponent c) {
        return new TextPaneUI();
    }

    @Override
    protected void installDefaults() {
        super.installDefaults();
        getComponent().addMouseListener(this.popupListener);
    }

    @Override
    protected void uninstallDefaults() {
        super.installDefaults();
        getComponent().removeMouseListener(this.popupListener);
        this.popup = null;
    }

    @Override
    public void update(Graphics g, JComponent c) {
        super.update(g, c);
    }

    private void showPopup(Point p) {
        if (!getComponent().isEnabled()) {
            return;
        }
        if (this.popup == null) {
            this.popup = new ClipboardActionsPopup(getComponent());
        }
        getComponent().requestFocus();
        this.popup.show(getComponent(), (int) p.getX(), (int) p.getY());
    }
}
