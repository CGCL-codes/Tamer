package inline.ui;

import java.util.*;
import javax.microedition.lcdui.*;
import inline.ui.ce.*;
import inline.ui.le.*;
import inline.sys.*;

public class NaviKeyRedefiner extends ListFormCanvas {

    private Vector keys;

    private int keypos;

    private static final int DLG_TYPE_ATOB = 67;

    private static final int DLG_TYPE_FINAL = 68;

    private static final int[] queue = { KeyEvent.KE_JOY_UP, KeyEvent.KE_JOY_RIGHT, KeyEvent.KE_JOY_DOWN, KeyEvent.KE_JOY_LEFT, KeyEvent.KE_JOY_PRESS, KeyEvent.KE_SOFT_LEFT, KeyEvent.KE_SOFT_RIGHT, KeyEvent.KE_SOFT_MENU };

    private int[] values;

    public NaviKeyRedefiner(HostCanvas previous) {
        super(previous, "Press a key for...");
        keypos = 0;
        values = new int[queue.length];
        prepareScreen();
        assignSelection();
    }

    private void prepareScreen() {
        Vector vv = new Vector();
        for (int i = 0; i < queue.length; i++) {
            FigureElement fe = new FigureElement(Base.getText(KeyEvent.KE_NAVI_STRINGS_BASE + queue[i]), CanvasElement.FIGURE_EMPTY);
            fe.setElmRamp(60);
            vv.addElement(fe);
        }
        list.setLines(vv);
    }

    private void assignSelection() {
        FigureElement fe = (FigureElement) list.getLines().elementAt(keypos);
        if (fe != null) {
            fe.setFigure(CanvasElement.FIGURE_RARROW);
            fe.setElmRamp(100);
            list.setSelectedIndex(keypos);
            list.repaint();
        }
    }

    private boolean checkAdding(int scanCode) {
        for (int i = 0; i < keypos; i++) {
            if (values[i] == scanCode) {
                dialogShow("Such key was already pressed!", Dialog.ICON_EXCLAMATION | Dialog.BTN_OK | Dialog.TYPE_AUTOCLOSE | Dialog.TYPE_LIMITEDUI);
                return false;
            }
        }
        if ((scanCode >= '0' && scanCode <= '9') || scanCode == '#' || scanCode == '*') {
            Dialog dlg = dialogShow("It is not navigation key. Are you sure to define it?", Dialog.BTN_YES | Dialog.BTN_NO | Dialog.ICON_QUESTION | Dialog.TYPE_LIMITEDUI, DLG_TYPE_ATOB);
            dlg.setLinkedObject(new Integer(scanCode));
            return false;
        }
        return true;
    }

    private void moveNextStepA(int scanCode, boolean store) {
        FigureElement fe = (FigureElement) list.getLines().elementAt(keypos);
        if (fe != null) {
            if (store) {
                if (checkAdding(scanCode)) {
                    moveNextStepB(scanCode, store);
                }
            } else {
                moveNextStepB(scanCode, store);
            }
        }
    }

    private void moveNextStepB(int scanCode, boolean store) {
        FigureElement fe = (FigureElement) list.getLines().elementAt(keypos);
        if (fe != null) {
            if (store) {
                fe.setString(fe.getString() + ": " + scanCode);
                fe.setFigure(CanvasElement.FIGURE_CHECKMARK);
                values[keypos] = scanCode;
            } else {
                fe.setFigure(CanvasElement.FIGURE_ENDASH);
            }
        }
        keypos++;
        if (keypos < queue.length) {
            assignSelection();
        } else {
            dialogShow("Saving setting...", Dialog.ICON_INFO | Dialog.TYPE_AUTOCLOSE, DLG_TYPE_FINAL);
        }
    }

    private void storeNewKeys() {
        this.goBack(true);
    }

    protected boolean keyEventPreview(KeyEvent ke) {
        int scanCode = ke.getKeyCode();
        if (scanCode == '6') {
            moveNextStepA(0, false);
        } else if (scanCode == '0') {
            goBack(false);
        } else {
            moveNextStepA(scanCode, true);
        }
        return true;
    }

    public void selectPreview(Object obj) {
        super.selectPreview(obj);
        if (obj instanceof Dialog) {
            Dialog dlg = (Dialog) obj;
            if (dlg.getTag() == DLG_TYPE_FINAL) {
                storeNewKeys();
            } else if (dlg.getTag() == DLG_TYPE_ATOB) {
                if (dlg.getReturnCode()) {
                    System.err.println(dlg.getLinkedObject());
                    moveNextStepB(((Integer) (dlg.getLinkedObject())).intValue(), true);
                }
            }
        }
    }
}
