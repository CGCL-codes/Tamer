package com.openbravo.pos.printer;

import com.openbravo.pos.util.PrinterUtil;

/**
 *
 * @author adrianromero
 */
public class BlinkAnimator extends BaseAnimator {

    public BlinkAnimator(String line1, String line2) {
        baseLine1 = PrinterUtil.alignLeft(line1, 20);
        baseLine2 = PrinterUtil.alignLeft(line2, 20);
    }

    public void setTiming(int i) {
        if ((i % 10) < 5) {
            currentLine1 = "";
            currentLine2 = "";
        } else {
            currentLine1 = baseLine1;
            currentLine2 = baseLine2;
        }
    }
}
