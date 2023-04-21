package com.lizardtech.djvubean.keys;

import com.lizardtech.djvubean.*;
import java.awt.event.*;

/**
 * This class implements a keyboard short cuts  the user may use to navigate the
 * DjVu Document.
 *
 * @author Bill C. Riemers
 * @version $Revision: 1.3 $
 */
public class DjVuKeys implements KeyListener {

    protected final DjVuBean bean;

    /**
   * Creates a new MenuBar object.
   *
   * @param bean DjVuBean to add this menu to.
   */
    public DjVuKeys(final DjVuBean bean) {
        this.bean = bean;
        bean.addKeyListener(this);
        final java.awt.TextArea text = bean.getTextArea();
        if (text != null) {
            text.addKeyListener(this);
        }
        bean.properties.put("addOn.keys", bean.properties.getProperty("keys", "true"));
    }

    /**
   * Query if keyboard shortcuts are enabled.
   */
    public boolean isEnabled() {
        return DjVuBean.stringToBoolean(bean.properties.getProperty("addOn.keys"), true);
    }

    /** Handle the key typed event from the text field. */
    public void keyTyped(KeyEvent e) {
        if (isEnabled()) {
            switch(e.getKeyChar()) {
                case '1':
                    bean.setZoom(DjVuBean.ZOOM100);
                    break;
                case '2':
                    bean.setZoom(DjVuBean.ZOOM150);
                    break;
                case '3':
                    bean.setZoom(DjVuBean.ZOOM300);
                    break;
                case '4':
                    bean.setZoom("400%");
                    break;
                case '5':
                    bean.setZoom("500%");
                    break;
                case '6':
                    bean.setZoom("600%");
                    break;
                case '7':
                    bean.setZoom("700%");
                    break;
                case '8':
                    bean.setZoom("800%");
                    break;
                case '9':
                    bean.setZoom("900%");
                    break;
                case '-':
                case '_':
                    bean.setZoom(DjVuBean.ZOOM_OUT);
                    break;
                case '=':
                case '+':
                    bean.setZoom(DjVuBean.ZOOM_IN);
                    break;
                case ' ':
                    bean.setPage(bean.getPage() + bean.getVisiblePageCount());
                    break;
                case 'g':
                    bean.properties.put("addOn.toolbar.page", "true");
                    break;
                case 'f':
                    String v = bean.properties.getProperty("addOn.finder");
                    if ("true".equalsIgnoreCase(v)) {
                        bean.properties.put("addOn.finder", "false");
                    } else if ("false".equalsIgnoreCase(v)) {
                        bean.properties.put("addOn.finder", "true");
                    }
                    break;
                case '0':
                case 'p':
                    bean.setZoom(DjVuBean.ZOOM_FIT_PAGE);
                    break;
                case 'w':
                    bean.setZoom(DjVuBean.ZOOM_FIT_WIDTH);
                    break;
                case KeyEvent.VK_BACK_SPACE:
                    bean.setPage(bean.getPage() - bean.getVisiblePageCount());
                    break;
            }
        }
    }

    /** Handle the key pressed event from the text field. */
    public void keyPressed(KeyEvent e) {
        if (isEnabled()) {
            switch(e.getKeyCode()) {
                case KeyEvent.VK_PAGE_DOWN:
                    bean.setScroll(DjVuBean.SCROLL_PAGE_DOWN);
                    break;
                case KeyEvent.VK_PAGE_UP:
                    bean.setScroll(DjVuBean.SCROLL_PAGE_UP);
                    break;
                case KeyEvent.VK_DOWN:
                    bean.setScroll(DjVuBean.SCROLL_DOWN);
                    break;
                case KeyEvent.VK_UP:
                    bean.setScroll(DjVuBean.SCROLL_UP);
                    break;
                case KeyEvent.VK_LEFT:
                    bean.setScroll(DjVuBean.SCROLL_LEFT);
                    break;
                case KeyEvent.VK_RIGHT:
                    bean.setScroll(DjVuBean.SCROLL_RIGHT);
                    break;
            }
        }
    }

    /** Handle the key released event from the text field. */
    public void keyReleased(KeyEvent e) {
    }
}
