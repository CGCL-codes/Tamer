package net.sourceforge.picdev.components;

import net.sourceforge.picdev.core.Project;
import net.sourceforge.picdev.annotations.EComponent;
import java.awt.*;

@EComponent(name = "LCD4x20", description = "LCD 4x20")
public class LCD4x20 extends LCD {

    net.sourceforge.picdev.core.Project parent;

    static int cellLHSOffset = 27;

    static int cellTopOfset = 50;

    static int LEDTopOffset = 180;

    static int LEDLHSOffset = 82;

    static int LEDSeperation = 17;

    String tabTitle = "LCD 4x20";

    public LCD4x20() {
    }

    public void init(Project parent) {
        this.parent = parent;
        super.tabTitle = this.tabTitle;
        super.init(parent);
        setSize(290, 220);
        setLocation(40, 400);
        setTitle("LCD 4x20");
        top = Toolkit.getDefaultToolkit().getImage("images/lcd4.gif");
        MediaTracker mt = new MediaTracker(this);
        mt.addImage(top, 0);
        try {
            mt.waitForID(0);
        } catch (InterruptedException ie) {
        }
        super.cellLHSOffset = this.cellLHSOffset;
        super.cellTopOfset = this.cellTopOfset;
    }

    /** Get the name of this component.
 *
 *  @return component's name
 */
    public String getName() {
        return "LCD 4x20";
    }

    public void update(Graphics g) {
        int i;
        int j;
        int row = 0;
        g.drawImage(top, 15, 35, this);
        for (j = 0; j < 4; j++) for (i = 0; i < 20; i++) g.drawImage(lcdcell, cellLHSOffset + LCD.cellWidth * i, cellTopOfset + j * LCD.cellHeight, this);
        for (i = 0; i < 11; i++) if (input.state[i]) g.drawImage(led_on, LEDLHSOffset + LEDSeperation * i, LEDTopOffset, this); else g.drawImage(led_off, LEDLHSOffset + LEDSeperation * i, LEDTopOffset, this);
        if (BusyFlag) g.drawImage(Busy, 197, 25, this);
        if (OptionDisplayOn) {
            if (OptionTwoLines) {
                row = 0;
                for (int ii = 0; ii < 4; ii++) {
                    for (i = 0; i < 20; i++) {
                        if (ii < 2) {
                            if ((j = DisplayPosition + (ii * 20) + i) > 39) j -= 40;
                        } else {
                            if ((j = DisplayPosition + (ii * 20) + i) > 79) j -= 40;
                        }
                        drawLetter(g, row, i, displayBuffer[j]);
                        if (OptionCursorOn) {
                            if (CursorPosition == j) {
                                if (Option5x10 && (!OptionTwoLines)) {
                                    drawLetterLow(g, row, i, LCD.cursor);
                                    if (cursorBlinked) {
                                        drawLetter(g, row, i, LCD.cursorFull);
                                        drawLetterLow(g, row, i, LCD.cursorFull);
                                    }
                                } else {
                                    drawLetter(g, row, i, LCD.cursor);
                                    if (cursorBlinked) {
                                        drawLetter(g, row, i, LCD.cursorFull);
                                    }
                                }
                            }
                        }
                    }
                    if (ii == 0) {
                        row = 2;
                    }
                    if (ii == 1) {
                        row = 1;
                    }
                    if (ii == 2) {
                        row = 3;
                    }
                }
            } else {
                row = 0;
                for (int ii = 0; ii < 2; ii++) {
                    for (i = 0; i < 20; i++) {
                        if ((j = DisplayPosition + (ii * 20) + i) > 79) j = j - 80;
                        drawLetter(g, row, i, displayBuffer[j]);
                        drawLetterLow(g, row, i, displayBuffer[j]);
                        if (OptionCursorOn) {
                            if (CursorPosition == j) {
                                if (Option5x10 && (!OptionTwoLines)) {
                                    drawLetterLow(g, row, i, LCD.cursor);
                                    if (cursorBlinked) {
                                        drawLetter(g, row, i, LCD.cursorFull);
                                        drawLetterLow(g, row, i, LCD.cursorFull);
                                    }
                                } else {
                                    drawLetter(g, row, i, LCD.cursor);
                                    if (cursorBlinked) {
                                        drawLetter(g, row, i, LCD.cursorFull);
                                    }
                                }
                            }
                        }
                    }
                    row = 2;
                }
            }
            if (OptionCursorBlinkOn && timerToc) cursorBlinked = true; else cursorBlinked = false;
        }
        if (ShowError) {
            g.drawImage(Error, 10, 25, this);
            g.drawString(errorNo, 80, 35);
        } else if (ShowInitialiseFail) g.drawImage(InitialiseFailed, 10, 25, this);
        if (Option8BitData) g.drawImage(EightBit, 234, 25, this); else g.drawImage(FourBit, 234, 25, this);
    }
}
