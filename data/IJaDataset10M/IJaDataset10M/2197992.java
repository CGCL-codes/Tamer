package com.ivis.xprocess.ui.workflowdesigner.print;

import org.eclipse.draw2d.geometry.Dimension;
import com.ivis.xprocess.ui.processdesigner.print.PrintOptions;
import com.ivis.xprocess.ui.properties.WorkflowDesignerMessages;

class ConstraintChecker {

    static void checkConstraints(Dimension paperSize, float zoom, int top, int left, int bottom, int right) throws PrintException {
        checkMargins(top, left, bottom, right);
        checkPaperSize(paperSize);
        checkPrintZoom(zoom);
        if (((paperSize.width - ((left + right) * PrintOptions.POINT_TO_INCH)) < 0) || ((paperSize.height - ((top + bottom) * PrintOptions.POINT_TO_INCH) - PrintOptionsHelper.LABEL_SIZE) < 0)) {
            throw new PrintException(WorkflowDesignerMessages.ConstraintChecker_invalidSettingError);
        }
    }

    static void checkMargins(int top, int left, int bottom, int right) throws PrintException {
        if ((top < 0) || (left < 0) || (bottom < 0) || (right < 0)) {
            throw new PrintException(WorkflowDesignerMessages.ConstraintChecker_invalidMarginsError);
        }
    }

    static void checkPaperSize(Dimension paperSize) throws PrintException {
        if ((paperSize.width <= 0) || (paperSize.height <= 0)) {
            throw new PrintException(WorkflowDesignerMessages.ConstraintChecker_invalidPaperSizeError);
        }
    }

    static void checkPrintZoom(float zoom) throws PrintException {
        if (zoom <= 0.0f) {
            throw new PrintException(WorkflowDesignerMessages.PrintPreferencePage_invalidZoomError);
        }
    }
}
