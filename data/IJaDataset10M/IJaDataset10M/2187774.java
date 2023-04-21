package interfaces;

import java.util.Vector;
import javax.swing.event.UndoableEditEvent;
import tools.Elements;
import tools.GuiTools;
import controller.ElementController;
import controller.MainController;
import elements.DoWhileCondition;
import elements.StructElement;

public class AddDoWhileCondition {

    public static DoWhileCondition addToLastPosition(Vector<StructElement> v) {
        DoWhileCondition tcl = new DoWhileCondition();
        tcl.setX(v.elementAt(v.size() - 1).getX());
        tcl.setY(v.elementAt(v.size() - 1).getY() + v.elementAt(v.size() - 1).getHeight());
        tcl.setHeight(ElementController.height_for_loop);
        tcl.setWidth(v.elementAt(v.size() - 1).getWidth());
        v.add(tcl);
        tcl.setUndopos(v.size() - 1);
        tcl.setSuperVec(v);
        MainController.undoManager.undoableEditHappened(new UndoableEditEvent(v, tcl));
        Elements.setWidthInVector(ElementController.elements, (tcl.getX() + tcl.getMaxScale()) > ElementController.elements.elementAt(0).getWidth() ? (tcl.getX() + tcl.getWidth()) : ElementController.elements.elementAt(0).getWidth());
        Elements.adjustElements();
        GuiTools.updateScrollBar();
        Elements.getSelectedScrollPane().setViewportView(MainController.ct.structPanelVec.elementAt(MainController.tp.getSelectedIndex()));
        return tcl;
    }

    public static DoWhileCondition insertAt(final int insertpos, Vector<StructElement> v) {
        DoWhileCondition tcl = new DoWhileCondition();
        tcl.setX(v.elementAt(insertpos).getX());
        tcl.setY(v.elementAt(insertpos).getY() + v.elementAt(insertpos).getHeight());
        tcl.setHeight(ElementController.height_for_loop);
        tcl.setWidth(v.elementAt(insertpos).getWidth());
        v.insertElementAt(tcl, insertpos + 1);
        tcl.setUndopos(insertpos + 1);
        tcl.setSuperVec(v);
        MainController.undoManager.undoableEditHappened(new UndoableEditEvent(v, tcl));
        Elements.resetClickedElements(Elements.getAllElements(ElementController.elements));
        tcl.setClicked(true);
        Elements.adjustHeight(ElementController.elements);
        Elements.adjustElements();
        Elements.setWidthInVector(ElementController.elements, (tcl.getX() + tcl.getMaxScale()) > ElementController.elements.elementAt(0).getWidth() ? (tcl.getX() + tcl.getWidth()) : ElementController.elements.elementAt(0).getWidth());
        GuiTools.updateScrollBar();
        Elements.getSelectedScrollPane().setViewportView(MainController.ct.structPanelVec.elementAt(MainController.tp.getSelectedIndex()));
        Elements.adjustWidth(ElementController.elements);
        GuiTools.updateScrollBar();
        return tcl;
    }
}
