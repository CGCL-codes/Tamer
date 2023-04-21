package ui.context;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

public class MyEventQueue extends EventQueue {

    protected void dispatchEvent(AWTEvent event) {
        super.dispatchEvent(event);
        if (!(event instanceof MouseEvent)) return;
        MouseEvent me = (MouseEvent) event;
        if (!me.isPopupTrigger()) return;
        Component comp = SwingUtilities.getDeepestComponentAt(me.getComponent(), me.getX(), me.getY());
        if (!(comp instanceof JTextComponent)) return;
        if (MenuSelectionManager.defaultManager().getSelectedPath().length > 0) return;
        JTextComponent tc = (JTextComponent) comp;
        JPopupMenu menu = new JPopupMenu();
        menu.add(new CutAction(tc));
        menu.add(new CopyAction(tc));
        menu.add(new PasteAction(tc));
        menu.add(new DeleteAction(tc));
        menu.addSeparator();
        menu.add(new CopyAllAction(tc));
        menu.add(new SelectAllAction(tc));
        Point pt = SwingUtilities.convertPoint(me.getComponent(), me.getPoint(), tc);
        menu.show(tc, pt.x, pt.y);
    }
}

class CutAction extends AbstractAction {

    final JTextComponent comp;

    public CutAction(JTextComponent comp) {
        super("Aussschneiden");
        this.comp = comp;
    }

    public void actionPerformed(ActionEvent e) {
        comp.cut();
    }

    public boolean isEnabled() {
        return comp.isEditable() && comp.isEnabled() && comp.getSelectedText() != null;
    }
}

class PasteAction extends AbstractAction {

    final JTextComponent comp;

    public PasteAction(JTextComponent comp) {
        super("Einf�gen");
        this.comp = comp;
    }

    public void actionPerformed(ActionEvent e) {
        comp.paste();
    }

    public boolean isEnabled() {
        if (comp.isEditable() && comp.isEnabled()) {
            Transferable contents = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(this);
            return contents.isDataFlavorSupported(DataFlavor.stringFlavor);
        } else return false;
    }
}

class DeleteAction extends AbstractAction {

    final JTextComponent comp;

    public DeleteAction(JTextComponent comp) {
        super("L�schen");
        this.comp = comp;
    }

    public void actionPerformed(ActionEvent e) {
        comp.replaceSelection(null);
    }

    public boolean isEnabled() {
        return comp.isEditable() && comp.isEnabled() && comp.getSelectedText() != null;
    }
}

class CopyAction extends AbstractAction {

    final JTextComponent comp;

    public CopyAction(JTextComponent comp) {
        super("Kopieren");
        this.comp = comp;
    }

    public void actionPerformed(ActionEvent e) {
        comp.copy();
    }

    public boolean isEnabled() {
        return comp.isEnabled() && comp.getSelectedText() != null;
    }
}

class SelectAllAction extends AbstractAction {

    final JTextComponent comp;

    public SelectAllAction(JTextComponent comp) {
        super("Alles Markieren");
        this.comp = comp;
    }

    public void actionPerformed(ActionEvent e) {
        comp.selectAll();
    }

    public boolean isEnabled() {
        return comp.isEnabled() && comp.getText().length() > 0;
    }
}

class CopyAllAction extends AbstractAction {

    final JTextComponent comp;

    public CopyAllAction(JTextComponent comp) {
        super("Alles Kopieren");
        this.comp = comp;
    }

    public void actionPerformed(ActionEvent e) {
        comp.selectAll();
        comp.copy();
    }

    public boolean isEnabled() {
        return comp.isEnabled() && comp.getText().length() > 0;
    }
}
