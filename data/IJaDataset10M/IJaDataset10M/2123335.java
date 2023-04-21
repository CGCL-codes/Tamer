package sk.bielyvlk.vlkgps;

import sk.bielyvlk.gps.Deg;
import sk.bielyvlk.vlkui.VlkUiCanvas;
import sk.bielyvlk.vlkui.VlkUiKeyListener;
import sk.bielyvlk.vlkui.VlkUiPtrListener;

public class MyKeyb implements VlkUiKeyListener, VlkUiPtrListener {

    private VlkGps midlet;

    public MyKeyb(VlkGps midlet) {
        this.midlet = midlet;
    }

    public void keyEvent(int keyCode, int action) {
        switch(keyCode + action) {
            case -1:
            case -1 + VlkUiCanvas.KEY_REPEATED:
            case '/':
            case '/' + VlkUiCanvas.KEY_REPEATED:
                midlet.getNavData().move(0);
                break;
            case -2:
            case -2 + VlkUiCanvas.KEY_REPEATED:
            case '!':
            case '!' + VlkUiCanvas.KEY_REPEATED:
                midlet.getNavData().move(180 * Deg.DEG);
                break;
            case -3:
            case -3 + VlkUiCanvas.KEY_REPEATED:
            case '(':
            case '(' + VlkUiCanvas.KEY_REPEATED:
                midlet.getNavData().move(90 * Deg.DEG);
                break;
            case -4:
            case -4 + VlkUiCanvas.KEY_REPEATED:
            case ')':
            case ')' + VlkUiCanvas.KEY_REPEATED:
                midlet.getNavData().move(270 * Deg.DEG);
                break;
            case -5:
            case -6:
                midlet.getVlkUiCanvas().setCurrentMenu(midlet.getMenu().menuMain);
                break;
            case '1':
            case '1' + VlkUiCanvas.KEY_REPEATED:
                midlet.getNavData().zoomOut();
                break;
            case '2':
                midlet.getNavData().setAutoTrack(true);
                break;
            case '3':
            case '3' + VlkUiCanvas.KEY_REPEATED:
                midlet.getNavData().zoomIn();
                break;
            case '4':
            case '4' + VlkUiCanvas.KEY_REPEATED:
                midlet.getNavData().rotateRight();
                break;
            case '5':
                midlet.getNavData().setAutoDirection(true);
                break;
            case '5' + VlkUiCanvas.KEY_REPEATED:
                midlet.getNavData().setDirection(0);
                break;
            case '6':
            case '6' + VlkUiCanvas.KEY_REPEATED:
                midlet.getNavData().rotateLeft();
                break;
            case '7':
                midlet.getView().setDayMode(!midlet.getView().getDayMode());
                midlet.getNavData().refresh();
                break;
            case '8':
                midlet.getNavData().setOsmMode(true);
                midlet.getNavData().setZoom(5);
                midlet.getNavData().refresh();
                break;
            case '*':
                midlet.getVlkUiCanvas().setCurrentMenu(midlet.getMenu().menuQuickMenu);
                break;
            case '#':
                midlet.getView().nextView();
                break;
            default:
                if (keyCode > ' ') {
                } else {
                }
                break;
        }
    }

    private int ptrdx, ptrdy;

    private boolean ptrmov = false;

    public void ptrEvent(int x, int y, int action) {
        if (y < VlkGps.topStatus.getHeight()) {
            ptrmov = false;
            if (action == VlkUiCanvas.PTR_PRESSED) {
                if (x > VlkUiCanvas.WIDTH / 2) {
                    midlet.getVlkUiCanvas().setCurrentMenu(midlet.getMenu().menuMain);
                } else {
                    midlet.getVlkUiCanvas().setCurrentMenu(midlet.getMenu().menuQuickMenu);
                }
            }
            return;
        } else if (y < VlkGps.topStatus.getHeight() + 20) {
            ptrmov = false;
            if (action == VlkUiCanvas.PTR_PRESSED) {
                if ((x > VlkUiCanvas.WIDTH / 2 - 44) && (x < VlkUiCanvas.WIDTH / 2 - 28)) {
                    midlet.getNavData().rotateLeft();
                } else if ((x > VlkUiCanvas.WIDTH / 2 - 26) && (x < VlkUiCanvas.WIDTH / 2 - 10)) {
                    midlet.getNavData().zoomOut();
                } else if ((x > VlkUiCanvas.WIDTH / 2 - 8) && (x < VlkUiCanvas.WIDTH / 2 + 8)) {
                    midlet.getNavData().setAutoDirection(true);
                } else if ((x > VlkUiCanvas.WIDTH / 2 + 10) && (x < VlkUiCanvas.WIDTH / 2 + 26)) {
                    midlet.getNavData().zoomIn();
                } else if ((x > VlkUiCanvas.WIDTH / 2 + 28) && (x < VlkUiCanvas.WIDTH / 2 + 44)) {
                    midlet.getNavData().rotateRight();
                }
            }
        } else if (y > VlkUiCanvas.HEIGHT - 20) {
            ptrmov = false;
            if (action == VlkUiCanvas.PTR_PRESSED) {
                if (x > VlkUiCanvas.WIDTH / 2) {
                    midlet.getView().nextView();
                } else {
                    midlet.getView().setDayMode(!midlet.getView().getDayMode());
                    midlet.getNavData().refresh();
                }
            }
        } else {
            switch(action) {
                case VlkUiCanvas.PTR_PRESSED:
                    ptrmov = true;
                    ptrdx = x;
                    ptrdy = y;
                    break;
                case VlkUiCanvas.PTR_RELEASED:
                    if (ptrmov) {
                        ptrmov = false;
                        midlet.getNavData().move(Deg.atan2(x - ptrdx, y - ptrdy), (int) Math.sqrt(Deg.sqr(x - ptrdx) + Deg.sqr(y - ptrdy)));
                    }
                    break;
            }
        }
    }
}
