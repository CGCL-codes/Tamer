package com.sksdpt.kioskjui.gui.screen;

import my.jutils.string.Str;
import org.eclipse.swt.graphics.Rectangle;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;
import com.sksdpt.kioskjui.bean.ButtonInfoBean;
import com.sksdpt.kioskjui.bean.ScreenBean;
import com.sksdpt.kioskjui.control.config.VSess;
import com.sksdpt.kioskjui.gui.forward.Mappers;
import com.sksdpt.kioskjui.gui.forward.SetOfORM;

/**
 * StartScreen
 * 
 * @author "Luis Alfonso Vega Garcia" <vegacom@gmail.com>
 */
public class Scr10Start extends BaseScreen {

    public static final ScreenBean<ButtonInfoBean> screenInfo;

    private static I18n i18n = I18nFactory.getI18n(Scr10Start.class);

    static {
        screenInfo = new ScreenBean<ButtonInfoBean>();
        screenInfo.setMapper(Mappers.M10_BINFO);
        screenInfo.setOrm(SetOfORM.R10_START);
    }

    @Override
    public void paintIt() {
        VSess.sngltn().getPref().reset();
        String title = i18n.tr("welcome");
        paintTitle(Str.capFirst(title));
        paintButtons();
    }

    private void paintButtons() {
        int x = (int) (bg.getBounds().width * 0.1);
        int y = (int) (bg.getBounds().height * 0.2);
        int w = (int) (bg.getBounds().width * 0.8);
        int h = (int) (bg.getBounds().height * 0.7);
        paintCenteredButtons(new Rectangle(x, y, w, h));
    }

    @Override
    public ScreenBean getScreenInfo() {
        return screenInfo;
    }
}
