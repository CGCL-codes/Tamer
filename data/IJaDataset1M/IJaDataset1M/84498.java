package com.sksdpt.kioskjui.gui.screen;

import my.jutils.string.Str;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;
import com.sksdpt.kioskjui.bean.MobileDeviceBean;
import com.sksdpt.kioskjui.bean.ScreenBean;
import com.sksdpt.kioskjui.gui.forward.Filters;
import com.sksdpt.kioskjui.gui.forward.ForwardListeners;
import com.sksdpt.kioskjui.gui.forward.ForwardRunnables;
import com.sksdpt.kioskjui.gui.forward.Mappers;
import com.sksdpt.kioskjui.gui.forward.SetOfORM;

/**
 * Scr30SelectModel
 * 
 * @author "Luis Alfonso Vega Garcia" <vegacom@gmail.com>
 */
public class Scr30SelectModel extends BaseScreen {

    public static final ScreenBean<MobileDeviceBean> screenInfo;

    private static I18n i18n = I18nFactory.getI18n(Scr30SelectModel.class);

    static {
        SetOfORM.R30_MOBILE_DEVICES.setFilter(Filters.F30_SELMDL_KWSEARCH);
        BaseScreen obj = new Scr30SelectModel();
        screenInfo = new ScreenBean<MobileDeviceBean>();
        screenInfo.setMapper(Mappers.M30_BINFO);
        screenInfo.setOrm(SetOfORM.R30_MOBILE_DEVICES);
        screenInfo.setGotoPage(ForwardListeners.goToPage(obj));
        screenInfo.setAfterSearchModified(ForwardRunnables.goToSearch(obj));
        i18n.trn("model", "models", 0);
        screenInfo.trn("model", "models");
    }

    @Override
    public void paintIt() {
        String title = i18n.trn("model", "models", 2);
        String tipTitle = i18n.tr("select model");
        String tipMessage = i18n.tr("select your mobile phone model" + " to show compatible contents");
        paintTitle(Str.capFirst(title));
        paintTip(Str.capFirst(tipTitle), Str.capFirst(tipMessage));
        addBodyList(screenInfo);
        paintHome();
        paintBack(ForwardListeners.goTo(new Scr20SelectBrand(), null));
    }

    @Override
    public ScreenBean getScreenInfo() {
        return screenInfo;
    }
}
