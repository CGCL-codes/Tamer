package org.jpedal.objects.acroforms.actions;

import org.jpedal.objects.raw.FormObject;
import org.jpedal.objects.acroforms.rendering.AcroRenderer;

/**
 * shared non component-specific code
 */
public class PDFListener {

    public static final boolean debugMouseActions = false;

    public FormObject formObject;

    public AcroRenderer acrorend;

    public ActionHandler handler;

    public PDFListener(FormObject form, AcroRenderer acroRend, ActionHandler formsHandler) {
        formObject = form;
        acrorend = acroRend;
        handler = formsHandler;
    }

    public void mouseReleased(Object e) {
        if (debugMouseActions) System.out.println("PDFListener.mouseReleased()");
        handler.A(e, formObject, ActionHandler.MOUSERELEASED);
        handler.U(e, formObject);
    }

    public void mouseClicked(Object e) {
        if (debugMouseActions) System.out.println("PDFListener.mouseClicked()");
        handler.A(e, formObject, ActionHandler.MOUSECLICKED);
    }

    public void mousePressed(Object e) {
        if (debugMouseActions) System.out.println("PDFListener.mousePressed()");
        handler.A(e, formObject, ActionHandler.MOUSEPRESSED);
        handler.D(e, formObject);
    }

    public void keyReleased(Object e) {
        if (debugMouseActions) System.out.println("PDFListener.keyReleased()");
        handler.K(e, formObject, ActionHandler.MOUSERELEASED);
        handler.V(e, formObject, ActionHandler.MOUSERELEASED);
    }

    public void focusLost(Object e) {
        if (debugMouseActions) System.out.println("PDFListener.focusLost()");
        handler.Bl(e, formObject);
        handler.K(e, formObject, ActionHandler.FOCUS_EVENT);
        handler.V(e, formObject, ActionHandler.FOCUS_EVENT);
        handler.F(formObject);
    }

    public void focusGained(Object e) {
        if (debugMouseActions) System.out.println("PDFListener.focusGained()");
        handler.Fo(e, formObject);
        String fieldRef = formObject.getObjectRefAsString();
        Object lastUnformattedValue = acrorend.getCompData().getLastUnformattedValue(fieldRef);
        if (lastUnformattedValue != null && !lastUnformattedValue.equals(acrorend.getCompData().getValue(fieldRef))) {
            acrorend.getCompData().setValue(fieldRef, lastUnformattedValue, false, false, false);
        }
    }
}
