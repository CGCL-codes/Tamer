package org.lnicholls.galleon.widget;

import java.awt.Color;
import org.apache.log4j.Logger;
import com.almilli.tivo.bananas.hd.HDApplication;
import com.tivo.hme.bananas.BHighlight;
import com.tivo.hme.bananas.BHighlights;
import com.tivo.hme.bananas.BScreen;
import com.tivo.hme.bananas.BText;
import com.tivo.hme.bananas.BView;
import com.tivo.hme.sdk.Resource;

public class DefaultScreen extends BScreen {

    private static final Logger log = Logger.getLogger(DefaultScreen.class.getName());

    protected final int TOP = SAFE_TITLE_V + 80;

    protected final int PAD = 10;

    protected final int BORDER_TOP = TOP + PAD;

    protected final int BORDER_LEFT = SAFE_TITLE_H + PAD;

    protected final int BODY_WIDTH = getWidth() - BORDER_LEFT - (SAFE_TITLE_H);

    protected final int BODY_HEIGHT = getHeight() - 2 * SAFE_TITLE_V;

    private BText mTitle;

    private BText mFooter;

    private HintsView mHints;

    protected BView mBusy;

    protected BView mTitleAnimation;

    static final class HintsView extends BView {

        public HintsView(BView parent, int x, int y, int width, int height, boolean visible) {
            super(parent, x, y, width, height, true);
            setFocusable(true);
            BHighlights h = getHighlights();
            h.setPageHint(H_PAGEUP, A_RIGHT, A_TOP);
            h.setPageHint(H_PAGEDOWN, A_RIGHT, A_BOTTOM);
            BHighlight pageup = h.get(H_PAGEUP);
            BHighlight pagedown = h.get(H_PAGEDOWN);
            if (pageup != null && pagedown != null) {
                pageup.setVisible(H_VIS_TRUE);
                pagedown.setVisible(H_VIS_TRUE);
                h.refresh();
            }
        }

        public boolean getHighlightIsVisible(int visible) {
            return visible == H_VIS_TRUE;
        }
    }

    public DefaultScreen(DefaultApplication app) {
        this(app, "background.jpg");
    }

    public DefaultScreen(DefaultApplication app, String background) {
        this(app, background, null, false);
    }

    public DefaultScreen(DefaultApplication app, boolean hints) {
        this(app, "background.jpg", null, hints);
    }

    public DefaultScreen(DefaultApplication app, String title, boolean hints) {
        this(app, "background.jpg", title, hints);
    }

    public DefaultScreen(DefaultApplication app, String background, String title, boolean hints) {
        super(app);
        setTitle(title);
        if (background != null) setBackground(background);
        if (hints) mHints = new HintsView(getAbove(), SAFE_TITLE_H, SAFE_TITLE_V, getWidth() - 2 * SAFE_TITLE_H, getHeight() - 2 * SAFE_TITLE_V, true);
        mBusy = new BView(this, SAFE_TITLE_H, SAFE_TITLE_V, 32, 32);
        mBusy.setResource(app.getBusyIcon());
        mBusy.setVisible(false);
    }

    public boolean handleAction(BView view, Object action) {
        if (action.equals("pop")) {
            this.getBApp().pop();
            new Thread() {

                public void run() {
                    try {
                        sleep(500);
                        remove();
                    } catch (Exception ex) {
                    }
                }
            }.start();
            return true;
        }
        return super.handleAction(view, action);
    }

    public boolean handleKeyPress(int code, long rawcode) {
        switch(code) {
            case KEY_LEFT:
                getBApp().play("pageup.snd");
                getBApp().flush();
                getBApp().setActive(false);
                return true;
        }
        return super.handleKeyPress(code, rawcode);
    }

    public void setTitle(String value) {
        if (value != null && value.length() > 0) {
            if (mTitle == null) {
                mTitle = new BText(getNormal(), SAFE_TITLE_H, SAFE_TITLE_V, (getWidth() - (SAFE_TITLE_H * 2)) - 20, 110);
                mTitle.setValue(" ");
                mTitle.setColor(Color.yellow);
                mTitle.setShadow(Color.black, 3);
                mTitle.setFlags(RSRC_HALIGN_CENTER | RSRC_TEXT_WRAP | RSRC_VALIGN_TOP);
                mTitle.setFont("default-48.font");
            }
            mTitle.setValue(value);
        }
    }

    public void setSmallTitle(String value) {
        setSmallTitle(value, Color.yellow);
    }

    public void setSmallTitle(String value, Color color) {
        if (value != null && value.length() > 0) {
            if (mTitle == null) {
                mTitle = new BText(getNormal(), SAFE_TITLE_H, SAFE_TITLE_V, (getWidth() - (SAFE_TITLE_H * 2)) - 20, 110);
                mTitle.setValue(" ");
                mTitle.setColor(color);
                mTitle.setShadow(Color.black, 3);
                mTitle.setFlags(RSRC_HALIGN_CENTER | RSRC_TEXT_WRAP | RSRC_VALIGN_TOP);
                mTitle.setFont("default-24.font");
            }
            mTitle.setValue(value);
        }
    }

    public void setFooter(String value, Resource anim) {
        if (mFooter != null) mFooter.setVisible(true);
        setFooter(value);
        if (mFooter != null) mFooter.setVisible(false, anim);
    }

    public void setFooter(String value) {
        if (value != null && value.length() > 0) {
            if (mFooter == null) {
                mFooter = new BText(getAbove(), SAFE_TITLE_H, getHeight() - SAFE_TITLE_V, (getWidth() - (SAFE_TITLE_H * 2)), 20);
                mFooter.setFlags(RSRC_HALIGN_CENTER | RSRC_VALIGN_BOTTOM);
                mFooter.setFont("default-18.font");
            }
            mFooter.setValue(value);
        }
    }

    public void setBackground(String value) {
        if (value != null) {
        }
    }

    protected void updateHints() {
        BHighlights h = getHighlights();
        BHighlight pageup = h.get(H_PAGEUP);
        BHighlight pagedown = h.get(H_PAGEDOWN);
        if (pageup != null && pagedown != null) {
            pageup.setVisible(H_VIS_TRUE);
            pagedown.setVisible(H_VIS_TRUE);
            h.refresh();
        }
    }

    public DefaultApplication getApp() {
        return (DefaultApplication) super.getApp();
    }

    public boolean isHighDef() {
        return getApp().isHighDef();
    }

    public int getSafeTitleHorizontal() {
        return getApp().getSafeTitleHorizontal();
    }

    public int getSafeTitleVertical() {
        return getApp().getSafeTitleVertical();
    }

    public int getSafeActionHorizontal() {
        return getApp().getSafeActionHorizontal();
    }

    public int getSafeActionVertical() {
        return getApp().getSafeActionVertical();
    }

    public int getContentX() {
        return getSafeTitleHorizontal() + 10;
    }

    public int getContentY() {
        if (isHighDef()) {
            return getSafeTitleVertical() + 94;
        } else {
            return getSafeTitleVertical() + 100;
        }
    }

    public int getContentWidth() {
        return getWidth() - getContentX() - getSafeTitleHorizontal();
    }

    public int getContentHeight() {
        if (isHighDef()) {
            return getHeight() - getContentY() - getSafeTitleVertical() - 10;
        } else {
            return getHeight() - getContentY() - getSafeTitleVertical() - 20;
        }
    }

    public int getContentBottom() {
        return getContentY() + getContentHeight();
    }

    public int getContentRight() {
        return getContentX() + getContentWidth();
    }
}
