package org.fpdev.apps.cart.gui;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JInternalFrame;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import org.fpdev.apps.cart.CartEventProcessor;
import org.fpdev.apps.cart.Editor;
import org.fpdev.apps.cart.gui.map.Canvas;

/**
 *
 * @author demory
 */
public class EditorFrame extends JInternalFrame {

    private Canvas canvas_;

    private Editor ed_;

    private CartGUI gui_;

    public EditorFrame(CartEventProcessor cep, Editor ed, CartGUI gui, String title) {
        super(title, true, true, true, true);
        canvas_ = new Canvas(cep, ed, gui);
        ed_ = ed;
        gui_ = gui;
        ed.setFrame(this);
        canvas_.addComponentListener(new ComponentAdapter() {

            @Override
            public void componentResized(ComponentEvent e) {
                canvas_.resized();
            }
        });
        this.addInternalFrameListener(new InternalFrameAdapter() {

            @Override
            public void internalFrameActivated(InternalFrameEvent e) {
                gui_.setActiveEditorFrame(EditorFrame.this);
                gui_.updateUndoRedo(ed_);
            }

            @Override
            public void internalFrameClosed(InternalFrameEvent e) {
                gui_.editorFrameClosed();
            }
        });
        this.add(canvas_);
        gui.editorFrameOpened();
    }

    public Editor getEditor() {
        return ed_;
    }

    public Canvas getCanvas() {
        return canvas_;
    }
}
