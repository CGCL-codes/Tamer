package gxbind.extjs.ext.config;

import gxbind.extjs.ext.BaseConfig;
import gxbind.extjs.ext.JavaScriptHelper;

public class QuickTipsConfig extends BaseConfig implements IQuickTipsWritable {

    public void setAnimate(boolean isAnimate) {
        JavaScriptHelper.addBooleanValueToHash(configHash, "animate", isAnimate);
    }

    public void setAutoDismiss(boolean isAutoDismiss) {
        JavaScriptHelper.addBooleanValueToHash(configHash, "autoDismiss", isAutoDismiss);
    }

    public void setAutoHide(boolean isAutoHide) {
        JavaScriptHelper.addBooleanValueToHash(configHash, "autoHide", isAutoHide);
    }

    public void setCls(String cls) {
        JavaScriptHelper.addStringValueToHash(configHash, "cls", cls);
    }

    public void setDelay(int delay) {
        JavaScriptHelper.addIntValueToHash(configHash, "delay", delay);
    }

    public void setHideDelay(int hideDelay) {
        JavaScriptHelper.addIntValueToHash(configHash, "hideDelay", hideDelay);
    }

    public void setHideOnClick(boolean isHideOnClick) {
        JavaScriptHelper.addBooleanValueToHash(configHash, "hideOnClick", isHideOnClick);
    }

    public void setInterceptTitles(boolean isInterceptTitles) {
        JavaScriptHelper.addBooleanValueToHash(configHash, "interceptTitles", isInterceptTitles);
    }

    public void setMaxWidth(int maxWidth) {
        JavaScriptHelper.addIntValueToHash(configHash, "maxWidth", maxWidth);
    }

    public void setMinWidth(int minWidth) {
        JavaScriptHelper.addIntValueToHash(configHash, "minWidth", minWidth);
    }

    public void setShowDelay(int showDelay) {
        JavaScriptHelper.addIntValueToHash(configHash, "showDelay", showDelay);
    }

    public void setText(String text) {
        JavaScriptHelper.addStringValueToHash(configHash, "text", text);
    }

    public void setTitle(String title) {
        JavaScriptHelper.addStringValueToHash(configHash, "title", title);
    }

    public void setTrackMouse(boolean isTrackMouse) {
        JavaScriptHelper.addBooleanValueToHash(configHash, "trackMouse", isTrackMouse);
    }

    public void setWidth(int width) {
        JavaScriptHelper.addIntValueToHash(configHash, "width", width);
    }
}
