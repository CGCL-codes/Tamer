package com.elibera.m.events;

import de.enough.polish.ui.Canvas;
import de.enough.polish.ui.StyleSheet;
import de.enough.polish.ui.Command;
import de.enough.polish.ui.StyleSheet;
import de.enough.polish.ui.CommandListener;
import de.enough.polish.ui.StyleSheet;
import de.enough.polish.ui.Displayable;
import de.enough.polish.ui.StyleSheet;
import de.enough.polish.ui.TextBox;
import de.enough.polish.ui.StyleSheet;
import com.elibera.m.app.MLE;
import com.elibera.m.display.DisplayCanvas;
import com.elibera.m.display.HelperDisplay;
import com.elibera.m.rms.HelperRMSStoreMLibera;
import com.elibera.m.utils.HelperStd;
import com.elibera.m.xml.PageSuite;
import com.elibera.m.xml.display.DisplayElement;
import com.elibera.m.display.HelperMenu;

/**	
 * behandelt die eingabe events für DisplayCanvas
 */
public class InputHandler {

    public static int SCROLL_INTERVALL = 10;

    public static final int[] DEF_KEYS = { Canvas.KEY_NUM5, Canvas.KEY_NUM2, Canvas.KEY_NUM8, Canvas.KEY_NUM7, Canvas.KEY_NUM9, Canvas.KEY_STAR, Canvas.KEY_POUND, Canvas.KEY_NUM0 };

    public static final int MENUE_SOFT_KEY = DEF_KEYS[7];

    /**
	 * Tasten Event
	 * es wird nur ein repaint ausgelöst, wenn true zurückgegeben wird
	 * Achtung dc.prepareForNewPage() oder dc.setPageSuite() müssen aufgerufen worden sein!!
	 */
    public static boolean keyPressed(int key, DisplayCanvas dc) {
        if (dc == null || dc.ps == null) return false;
        PageSuite ps = dc.ps;
        DisplayElement[] curPage = ps.openPage;
        if (curPage.length <= 0) return false;
        int selected = getCurrentSelectedIndex(dc), dcHeight = HelperDisplay.CONTENT_HEIGHT;
        if (selected > -1) {
            if (curPage[selected].proc.keyPressed(curPage[selected], key, dc)) return true;
        }
        if (key == HelperRMSStoreMLibera.appKeys[2] || key == DEF_KEYS[2]) {
            DisplayElement tel = curPage[curPage.length - 1];
            int i = 1;
            while (tel == null) {
                tel = curPage[curPage.length - i];
                i++;
            }
            boolean cantScroll = dc.scrollY + tel.proc.getBottomRightY(tel) <= dcHeight - HelperDisplay.DISPLAYCANVAS_BORDER_Y * 2;
            int sel = getSelectedDisplayElementAndDoSelectionTask(key, dc, true, curPage, cantScroll);
            if (ps.selected >= 0 && ps.selected == sel && !curPage[ps.selected].selected) {
                sel = -1;
                dc.repaint();
            }
            if (sel == -1) {
                if (cantScroll) return true;
                dc.scrollY -= SCROLL_INTERVALL;
                int ty = tel.proc.getBottomRightY(tel);
                if (dc.scrollY + ty < dcHeight - HelperDisplay.DISPLAYCANVAS_BORDER_Y * 2 + 2) dc.scrollY = (ty - dcHeight + HelperDisplay.DISPLAYCANVAS_BORDER_Y * 2) * -1 - 2;
                checkSelectedForVisibility(sel, dc);
                dc.repaint();
                return true;
            } else {
                if (ps.selected >= 0) curPage[ps.selected].selected = false;
                ps.selected = sel;
                curPage[sel].selected = true;
                checkSelectedForVisibility(sel, dc);
                dc.repaint();
                return true;
            }
        } else if (key == HelperRMSStoreMLibera.appKeys[1] || key == DEF_KEYS[1]) {
            int sel = getSelectedDisplayElementAndDoSelectionTask(key, dc, false, curPage, (dc.scrollY >= 0 ? true : false));
            if (sel == -1) {
                dc.scrollY += SCROLL_INTERVALL;
                if (dc.scrollY > 0) dc.scrollY = 0;
                checkSelectedForVisibility(sel, dc);
                dc.repaint();
                return true;
            } else {
                if (ps.selected >= 0) curPage[ps.selected].selected = false;
                ps.selected = sel;
                curPage[sel].selected = true;
                checkSelectedForVisibility(sel, dc);
                dc.repaint();
                return true;
            }
        } else if (selected > -1 && (key == HelperRMSStoreMLibera.appKeys[3] || key == DEF_KEYS[3] || key == HelperRMSStoreMLibera.appKeys[4] || key == DEF_KEYS[4])) {
            int sel = selected - 1;
            boolean rechts = key == HelperRMSStoreMLibera.appKeys[4] || key == DEF_KEYS[4];
            if (rechts) sel = selected + 1;
            while (sel >= 0 && sel < curPage.length && curPage[sel] != null) {
                if (curPage[sel].interaktiv) {
                    int s1 = sel, s2 = selected;
                    if (sel > selected) {
                        s1 = selected;
                        s2 = sel;
                    }
                    if (curPage[s1].proc.getBottomRightY(curPage[s1]) > curPage[s2].proc.getTopLeftY(curPage[s2])) {
                        curPage[ps.selected].selected = false;
                        ps.selected = sel;
                        curPage[sel].selected = true;
                        checkSelectedForVisibility(sel, dc);
                        dc.repaint();
                        return true;
                    } else return false;
                }
                if (rechts) sel++; else sel--;
            }
        }
        return false;
    }

    /**
	 * übernimmt die Scroll Logik
	 * key --> keyEvent
	 * top --> sollen wir uns das untere Ende anschauen oder das obere des Canvas, davon hängt es ab, welche Elemente als valid gefunden werden
	 * cantScroll --> wenn wir beim Scrollen am Ende angelangt sind, dann spielt top keine Rolle mehr
	 * gibt die position in curPage des selektierten Elements zurück
	 * -1 wenn kein Element selektiert ist, oder kein neues selektiert wurde
	 * sprich, wenn -1 kommt, dann kann gescrollt werden, ansonsten ist zu einem neuen Element gesprungen wurden
	 */
    private static int getSelectedDisplayElementAndDoSelectionTask(int key, DisplayCanvas dc, boolean top, DisplayElement[] curPage, boolean cantScroll) {
        int curY = dc.scrollY * -1;
        int height = HelperDisplay.CONTENT_HEIGHT;
        int selected = -1;
        int[] valid = new int[0];
        int[] interaktiv = dc.ps.interaktivElementsCurPage;
        if (cantScroll) valid = interaktiv;
        for (int a = 0; a < interaktiv.length; a++) {
            int i = interaktiv[a];
            DisplayElement tel = curPage[i];
            if (tel == null) continue;
            if (tel.selected) selected = i;
            if (cantScroll) continue;
            if (top) {
                if (tel.proc.getTopLeftY(tel) >= curY && (tel.proc.getBottomRightY(tel) - curY <= height / 2 || (tel.proc.getBottomRightY(tel) - tel.proc.getTopLeftY(tel) >= height / 2 && tel.proc.getTopLeftY(tel) < curY + height / 2))) {
                    valid = HelperStd.incArray(valid, i);
                }
            } else {
                if ((tel.proc.getTopLeftY(tel) - curY >= height / 2 && tel.proc.getBottomRightY(tel) - curY < height - 10) || (tel.proc.getBottomRightY(tel) - tel.proc.getTopLeftY(tel) >= height / 2 && tel.proc.getBottomRightY(tel) - curY < height && tel.proc.getBottomRightY(tel) - curY > height / 2 - 10)) {
                    valid = HelperStd.incArray(valid, i);
                }
            }
        }
        if (valid.length <= 0) return -1;
        if (key == HelperRMSStoreMLibera.appKeys[2] || key == DEF_KEYS[2]) {
            if (selected > -1 && valid[valid.length - 1] <= selected) return -1;
            if (selected == -1) {
                int c = 0;
                while (c < valid.length && !checkIfElementIsVisible(curPage[valid[c]], dc)) {
                    c++;
                }
                if (c < valid.length && checkIfElementIsVisible(curPage[valid[c]], dc)) return valid[c]; else return -1;
            }
            int ns = -1;
            for (int i = 0; i < valid.length; i++) {
                if (valid[i] == selected && i < valid.length - 1) ns = valid[i + 1];
            }
            if (ns == selected) return -1;
            if (selected != -1) curPage[selected].selected = false;
            return ns;
        } else if (key == HelperRMSStoreMLibera.appKeys[1] || key == DEF_KEYS[1]) {
            if (selected > -1 && valid[0] >= selected) return -1;
            if (selected == -1) {
                int c = 1;
                while (c <= valid.length && !checkIfElementIsVisible(curPage[valid[valid.length - c]], dc)) {
                    c++;
                }
                if (c <= valid.length && checkIfElementIsVisible(curPage[valid[valid.length - c]], dc)) return valid[valid.length - c]; else return -1;
            }
            int ns = -1;
            for (int i = 0; i < valid.length; i++) {
                if (valid[i] == selected && i - 1 >= 0 && checkIfElementIsVisible(curPage[valid[i]], dc)) ns = valid[i - 1];
            }
            if (ns == -1 && selected == curPage.length - 1 && checkIfElementIsVisible(curPage[valid[valid.length - 1]], dc)) ns = valid[valid.length - 1];
            if (ns == selected) return -1;
            if (selected != -1) curPage[selected].selected = false;
            return ns;
        }
        return -1;
    }

    /**
	 * setzt das Array ps.interaktivElementsCurPage mit den interaktiven Elementen von ps.curPage
	 */
    public static void setInteraktiveElementsForThisPage(PageSuite ps, DisplayCanvas dc) {
        DisplayElement[] curPage = ps.openPage;
        ps.interaktivElementsCurPage = new int[0];
        for (int i = 0; i < curPage.length; i++) {
            if (curPage[i] == null) continue;
            if (curPage[i].interaktiv) ps.interaktivElementsCurPage = HelperStd.incArray(ps.interaktivElementsCurPage, i);
        }
    }

    /**
	 * überprüft ob das Element sichtbar ist
	 * bei übergroßen elementen müssen mindestens ein teil sichtbar sein
	 */
    public static boolean checkIfElementIsVisible(DisplayElement el, DisplayCanvas dc) {
        if (el == null) return false;
        int curY = dc.scrollY * -1;
        int height = HelperDisplay.CONTENT_HEIGHT, ty = el.proc.getTopLeftY(el), by = el.proc.getBottomRightY(el);
        if (ty >= curY && by - curY < height) return true;
        if ((by - ty >= height / 2 && by - curY > height / 2 - 10) && ty < curY + height - 20) return true;
        return false;
    }

    /**
	 * überprüft ob dieses element der aktuellen Seite sichtbar ist
	 * wenn nicht wird selected auf false gesetzt
	 * ist selected == -1 dann wird ein element in der aktuellen Seite gesucht, das selected ist
	 */
    private static void checkSelectedForVisibility(int selected, DisplayCanvas dc) {
        if (selected < 0) {
            selected = getCurrentSelectedIndex(dc);
        }
        if (selected < 0) return;
        if (dc.ps.openPage[selected] == null) return;
        if (!checkIfElementIsVisible(dc.ps.openPage[selected], dc)) dc.ps.openPage[selected].selected = false;
    }

    /**
	 * gibt das aktuell selektierte Element (den index in der akt. page) zurück
	 */
    private static int getCurrentSelectedIndex(DisplayCanvas dc) {
        for (int i = 0; i < dc.ps.interaktivElementsCurPage.length; i++) {
            DisplayElement el = dc.ps.openPage[dc.ps.interaktivElementsCurPage[i]];
            if (el != null && el.selected) {
                return dc.ps.interaktivElementsCurPage[i];
            }
        }
        return -1;
    }

    /**
	 * führt eine Pointer Aktion für die PageSuite aus
	 * es wird nur ein repaint ausgelöst, wenn true zurückgegeben wird
	 * Achtung dc.prepareForNewPage() oder dc.setPageSuite() müssen aufgerufen worden sein!!
	 */
    public static boolean pointerClicked(int x, int y, DisplayCanvas dc) {
        if (dc == null || dc.ps == null) return false;
        DisplayElement[] curPage = dc.ps.openPage;
        boolean firsttry = true;
        for (int i = 0; i < dc.ps.interaktivElementsCurPage.length; i++) {
            DisplayElement el = curPage[dc.ps.interaktivElementsCurPage[i]];
            if (el.proc.isCLicked(el, x, y, dc)) {
                boolean ret = el.proc.pointerClicked(el, x, y, dc, i);
                if (ret) return true;
                if (!firsttry) return false;
                firsttry = false;
            }
        }
        return false;
    }

    /**
	 * deselektiert alle Elemente die vielleicht selektiert sind in der aktuellen Seite
	 * @param curPage
	 */
    public static void unselectAll(DisplayElement[] curPage, PageSuite ps) {
        for (int i = 0; i < ps.interaktivElementsCurPage.length; i++) {
            if (curPage[ps.interaktivElementsCurPage[i]].selected) curPage[ps.interaktivElementsCurPage[i]].selected = false;
        }
    }

    /**
	 * überprüft, ob ein Shortcut gedrückt wurde
	 */
    public static boolean checkShortCutKeys(int key, DisplayCanvas dc) {
        if (dc.ps != null) {
            if ((key == HelperRMSStoreMLibera.appKeys[3] || key == DEF_KEYS[3]) && dc.ps.curPage > 0) {
                dc.ps.curPage--;
                if (dc.ps.curPage < 0) {
                    dc.ps.curPage = 0;
                    return true;
                }
                HelperDisplay.prepareForNewPage(dc);
                dc.repaint();
                return true;
            } else if ((key == HelperRMSStoreMLibera.appKeys[4] || key == DEF_KEYS[4]) && dc.ps.curPage + 1 < dc.ps.getCountPages()) {
                dc.ps.curPage++;
                int max = dc.ps.getCountPages();
                if (dc.ps.backrgoundParsingThread != null) max--;
                if (dc.ps.curPage >= max) {
                    dc.ps.curPage = max - 1;
                    return true;
                }
                HelperDisplay.prepareForNewPage(dc);
                dc.repaint();
                return true;
            }
        }
        if (key == HelperRMSStoreMLibera.appKeys[7] || key == DEF_KEYS[7] || key == MENUE_SOFT_KEY) {
            MLE midlet = MLE.midlet;
            if (dc.equals(midlet.menu)) midlet.setCurrent(midlet.menu.lastDC); else {
                HelperMenu.showMenu(dc);
            }
            return true;
        }
        return false;
    }

    public static boolean processPointerDragEvent(DisplayCanvas dc, int dragX, int dragY, int x, int y) {
        int[] val = HelperDisplay.calculateDirectionForPointerDrag(15, dragX, dragY, x, y);
        int dir = val[0], len = val[1];
        if (dir == 0) return false;
        boolean upDown = true;
        if (dir % 7 == 0) upDown = false; else if (dir % 5 == 0) dir *= -1;
        if (upDown) {
            DisplayElement[] curPage = dc.ps.openPage;
            int dcHeight = HelperDisplay.CONTENT_HEIGHT;
            if (dir > 0) {
                dc.scrollY += len;
                if (dc.scrollY > 0) dc.scrollY = 0;
            } else {
                DisplayElement tel = curPage[curPage.length - 1];
                boolean cantScroll = dc.scrollY + tel.proc.getBottomRightY(tel) <= dcHeight - HelperDisplay.DISPLAYCANVAS_BORDER_Y * 2;
                if (cantScroll) return true;
                dc.scrollY -= len;
                int ty = tel.proc.getBottomRightY(tel);
                if (dc.scrollY + ty < dcHeight - HelperDisplay.DISPLAYCANVAS_BORDER_Y * 2 + 2) dc.scrollY = (ty - dcHeight + HelperDisplay.DISPLAYCANVAS_BORDER_Y * 2) * -1 - 2;
            }
            checkSelectedForVisibility(dc.ps.selected, dc);
            dc.repaint();
            return true;
        }
        if (dir < 0) {
            dc.ps.curPage--;
            if (dc.ps.curPage < 0) {
                dc.ps.curPage = 0;
                return true;
            }
        } else {
            dc.ps.curPage++;
            if (dc.ps.curPage >= dc.ps.getCountPages()) {
                dc.ps.curPage = dc.ps.getCountPages() - 1;
                return true;
            }
        }
        HelperDisplay.prepareForNewPage(dc);
        dc.repaint();
        return true;
    }
}
