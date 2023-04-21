package com.cell.gfx.gui;

import com.cell.CObject;
import com.cell.gfx.AScreen;
import com.cell.gfx.IGraphics;
import com.cell.gfx.gui.RichTextBox.Scripts;
import com.cell.gfx.gui.RichTextBox.ScriptsUI;

public class Cursor extends CObject {

    public int MaxTipWidth = 256;

    public int BackColor = 0;

    protected RichTextBox RichTip;

    {
        RichTip = new RichTextBox("", MaxTipWidth, 32);
        RichTip.UserRect.BackColor = 0xD0000000;
        RichTip.UserRect.BorderColor = 0x00000000;
        RichTip.TextColor = 0xffffffff;
    }

    Form Top;

    protected Item Focused;

    protected int ScreenX;

    protected int ScreenY;

    public int W = 4;

    public int H = 8;

    private boolean IsHoldShow = false;

    private int HoldX = 0;

    private int HoldY = 0;

    private String FirstCauted;

    String LastRendered;

    public void update(FormManager forms) {
        FirstCauted = null;
        IsHoldShow = false;
        ScreenX = AScreen.getPointerX();
        ScreenY = AScreen.getPointerY();
        Top = forms.getTopForm();
        if (Top == null || !Top.isEnabled() || !Top.includePoint(ScreenX, ScreenY) || Form.isPointerHold()) {
            setFormItemTip(null);
            Focused = null;
        } else {
            Item focus = Top.getVisibleItem(ScreenX, ScreenY);
            if (focus == null) {
                setFormItemTip(null);
            } else {
                if (focus.Click == null) {
                    setFormItemTip(null);
                } else {
                    setFormItemTip(focus.Click.LongLable);
                }
            }
            Focused = focus;
        }
    }

    public int getX() {
        return ScreenX;
    }

    public int getY() {
        return ScreenY;
    }

    public int getTipW() {
        return RichTip.getWidth();
    }

    public int getTipH() {
        return RichTip.getHeight();
    }

    public void showInHold(int x, int y) {
        IsHoldShow = true;
        HoldX = x;
        HoldY = y;
    }

    public void render(IGraphics g) {
        if (BackColor != 0) {
            g.setColor(BackColor);
            g.fillTriangle(ScreenX + 0, ScreenY + 0, ScreenX + 0, ScreenY + H, ScreenX + W, ScreenY + H);
        }
        if (FirstCauted == null) return;
        if (IsHoldShow) {
            if (RichTip.getText() != null && RichTip.getText().length() > 0) {
                RichTip.render(g, HoldX, HoldY);
            }
        } else {
            if (RichTip.getText() != null && RichTip.getText().length() > 0) {
                int px = ScreenX + W;
                int py = ScreenY + H;
                px = Math.min(px, Form.SCREEN_WIDTH - RichTip.getWidth());
                py = Math.min(py, Form.SCREEN_HEIGHT - RichTip.getHeight());
                px = Math.max(px, 0);
                py = Math.max(py, 0);
                RichTip.render(g, px, py);
            }
        }
    }

    int count = 0;

    private void setFormItemTip(String msg) {
        if (FirstCauted == null && msg != null) {
            FirstCauted = msg;
            if (!msg.equals(LastRendered)) {
                LastRendered = msg;
                RichTip.resize(MaxTipWidth, 32);
                if (msg.length() >= 3) {
                    msg = Control.processReplaceTable(msg);
                    msg = Control.LocalCovert.covert(msg);
                    ScriptsUI scriptui = RichTextBox.buildScriptUI(msg);
                    if (scriptui.BackColor != 0) {
                        RichTip.UserRect.BackColor = scriptui.BackColor;
                        msg = scriptui.Text;
                    } else {
                        RichTip.UserRect.BackColor = 0xD0000000;
                    }
                    Scripts script = RichTextBox.buildScript(msg);
                    RichTip.setText(script.Text, script.Attributes);
                } else {
                    RichTip.setText(msg);
                }
                try {
                    RichTip.maxSize(AScreen.CurGraphics);
                } catch (Exception e) {
                }
            }
        }
    }

    public void trySetTip(String msg) {
        if (msg != null && msg.length() > 0) {
            setFormItemTip(msg);
        }
    }

    public static void tickTimer() {
        AScreen.tickTimer();
    }

    public static void resetTimer() {
        AScreen.resetTimer();
    }

    public static int getTimer() {
        return AScreen.getTimer();
    }
}
