package interfaces;

import java.util.Vector;
import javax.swing.event.UndoableEditEvent;
import tools.Elements;
import tools.GuiTools;
import controller.ElementController;
import controller.MainController;
import elements.DoUntilCondition;
import elements.StructElement;

public class AddDoUntilCondition {

    public static DoUntilCondition addToLastPosition(Vector<StructElement> v) {
        DoUntilCondition tcl = new DoUntilCondition();
        tcl.setX(v.elementAt(v.size() - 1).getX());
        tcl.setY(v.elementAt(v.size() - 1).getY() + v.elementAt(v.size() - 1).getHeight());
        tcl.setHeight(ElementController.height_do_while_loop);
        tcl.setWidth(v.elementAt(v.size() - 1).getWidth());
        v.add(tcl);
        tcl.setUndopos(v.size() - 1);
        tcl.setSuperVec(v);
        MainController.undoManager.undoableEditHappened(new UndoableEditEvent(v, tcl));
        Elements.setWidthInVector(ElementController.elements, (tcl.getX() + tcl.getMaxScale()) > ElementController.elements.elementAt(0).getWidth() ? (tcl.getX() + tcl.getWidth()) : ElementController.elements.elementAt(0).getWidth());
        Elements.adjustHeight(ElementController.elements);
        Elements.adjustElements();
        GuiTools.updateScrollBar();
        Elements.getSelectedScrollPane().setViewportView(MainController.ct.structPanelVec.elementAt(MainController.tp.getSelectedIndex()));
        return tcl;
    }

    public static DoUntilCondition insertAt(final int insertpos, Vector<StructElement> v) {
        DoUntilCondition bcl = new DoUntilCondition();
        bcl.setX(v.elementAt(insertpos).getX());
        bcl.setY(v.elementAt(insertpos).getY() + v.elementAt(insertpos).getHeight());
        bcl.setHeight(ElementController.height_do_while_loop);
        bcl.setWidth(v.elementAt(insertpos).getWidth());
        v.insertElementAt(bcl, insertpos + 1);
        bcl.setUndopos(insertpos + 1);
        bcl.setSuperVec(v);
        MainController.undoManager.undoableEditHappened(new UndoableEditEvent(v, bcl));
        Elements.resetClickedElements(Elements.getAllElements(ElementController.elements));
        bcl.setClicked(true);
        Elements.adjustHeight(ElementController.elements);
        Elements.adjustElements();
        Elements.setWidthInVector(ElementController.elements, (bcl.getX() + bcl.getMaxScale()) > ElementController.elements.elementAt(0).getWidth() ? (bcl.getX() + bcl.getWidth()) : ElementController.elements.elementAt(0).getWidth());
        GuiTools.updateScrollBar();
        Elements.getSelectedScrollPane().setViewportView(MainController.ct.structPanelVec.elementAt(MainController.tp.getSelectedIndex()));
        Elements.adjustWidth(ElementController.elements);
        GuiTools.updateScrollBar();
        return bcl;
    }
}
