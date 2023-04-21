package com.google.code.jqwicket.ui.colorpicker;

import com.google.code.jqwicket.api.AbstractJQOptions;
import com.google.code.jqwicket.api.IJQFunction;
import com.google.code.jqwicket.api.IJQStatement;
import com.google.code.jqwicket.api.JQOptions;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import static com.google.code.jqwicket.api.JQuery.$f;
import static com.google.code.jqwicket.api.JQuery.js;

/**
 * @author mkalina
 */
public class ColorPickerOptions extends AbstractJQOptions<ColorPickerOptions> {

    private static final long serialVersionUID = 1L;

    public static final JavaScriptResourceReference JS_RESOURCE = new JavaScriptResourceReference(ColorPickerOptions.class, "js/colorpicker.js");

    public static final CssResourceReference CSS_RESOURCE = new CssResourceReference(ColorPickerOptions.class, "css/colorpicker.css");

    public ColorPickerOptions() {
        this.setJsResourceReferences(JS_RESOURCE);
        this.setCssResourceReferences(CSS_RESOURCE);
    }

    /**
     * The desired event to trigger the colorpicker. Default: 'click'
     *
     * @param eventName
     * @return
     */
    public ColorPickerOptions eventName(CharSequence eventName) {
        super.put("eventName", eventName);
        return this;
    }

    /**
     * The default color. String for hex color or hash for RGB and HSB ({r:255, r:0, b:0}) . Default: 'ff0000'
     *
     * @param color
     * @return
     */
    public ColorPickerOptions color(CharSequence color) {
        super.put("color", color);
        return this;
    }

    /**
     * The default color. String for hex color or hash for RGB and HSB ({r:255, r:0, b:0}) . Default: 'ff0000'
     *
     * @param r
     * @param g
     * @param b
     * @return
     */
    public ColorPickerOptions color(int r, int g, int b) {
        super.put("color", new JQOptions().put("r", r).put("g", g).put("b", b));
        return this;
    }

    /**
     * Whatever if the color picker is appended to the element or triggered by an event. Default false
     *
     * @param flat
     * @return
     */
    public ColorPickerOptions flat(boolean flat) {
        super.put("flat", flat);
        return this;
    }

    /**
     * Whatever if the color values are filled in the fields while changing values on selector or a field. If false it may
     * improve speed. Default true
     *
     * @param livePreview
     * @return
     */
    public ColorPickerOptions livePreview(boolean livePreview) {
        super.put("livePreview", livePreview);
        return this;
    }

    /**
     * Callback function triggered when the color picker is shown
     *
     * @param callbackBody
     * @return
     */
    public ColorPickerOptions onShowEvent(CharSequence callbackBody) {
        return this.onShowEvent(js(callbackBody));
    }

    /**
     * Callback function triggered when the color picker is shown
     *
     * @param callbackBody
     * @return
     */
    public ColorPickerOptions onShowEvent(IJQStatement callbackBody) {
        return this.onShowEvent($f(callbackBody).withParams("colpkr"));
    }

    /**
     * Callback function triggered when the color picker is shown
     *
     * @param callback
     * @return
     */
    public ColorPickerOptions onShowEvent(IJQFunction callback) {
        super.put("onShow", callback);
        return this;
    }

    /**
     * Callback function triggered before the color picker is shown
     *
     * @param callbackBody
     * @return
     */
    public ColorPickerOptions onBeforeShowEvent(CharSequence callbackBody) {
        return this.onBeforeShowEvent(js(callbackBody));
    }

    /**
     * Callback function triggered before the color picker is shown
     *
     * @param callbackBody
     * @return
     */
    public ColorPickerOptions onBeforeShowEvent(IJQStatement callbackBody) {
        return this.onBeforeShowEvent($f(callbackBody));
    }

    /**
     * Callback function triggered before the color picker is shown
     *
     * @param callback
     * @return
     */
    public ColorPickerOptions onBeforeShowEvent(IJQFunction callback) {
        super.put("onBeforeShow", callback);
        return this;
    }

    /**
     * Callback function triggered when the color picker is hidden
     *
     * @param callbackBody
     * @return
     */
    public ColorPickerOptions onHideEvent(CharSequence callbackBody) {
        return this.onHideEvent(js(callbackBody));
    }

    /**
     * Callback function triggered when the color picker is hidden
     *
     * @param callbackBody
     * @return
     */
    public ColorPickerOptions onHideEvent(IJQStatement callbackBody) {
        return this.onHideEvent($f(callbackBody).withParams("colpkr"));
    }

    /**
     * Callback function triggered when the color picker is hidden
     *
     * @param callback
     * @return
     */
    public ColorPickerOptions onHideEvent(IJQFunction callback) {
        super.put("onHide", callback);
        return this;
    }

    /**
     * Callback function triggered when the color is changed
     *
     * @param callbackBody
     * @return
     */
    public ColorPickerOptions onChangeEvent(CharSequence callbackBody) {
        return this.onChangeEvent(js(callbackBody));
    }

    /**
     * Callback function triggered when the color is changed
     *
     * @param callbackBody
     * @return
     */
    public ColorPickerOptions onChangeEvent(IJQStatement callbackBody) {
        return this.onChangeEvent($f(callbackBody).withParams("hsb", "hex", "rgb"));
    }

    /**
     * Callback function triggered when the color is changed
     *
     * @param callback
     * @return
     */
    public ColorPickerOptions onChangeEvent(IJQFunction callback) {
        super.put("onChange", callback);
        return this;
    }

    /**
     * Callback function triggered when the color it is chosen
     *
     * @param callbackBody
     * @return
     */
    public ColorPickerOptions onSubmitEvent(CharSequence callbackBody) {
        return this.onSubmitEvent(js(callbackBody));
    }

    /**
     * Callback function triggered when the color it is chosen
     *
     * @param callbackBody
     * @return
     */
    public ColorPickerOptions onSubmitEvent(IJQStatement callbackBody) {
        return this.onSubmitEvent($f(callbackBody));
    }

    /**
     * Callback function triggered when the color it is chosen
     *
     * @param callback
     * @return
     */
    public ColorPickerOptions onSubmitEvent(IJQFunction callback) {
        super.put("onSubmit", callback);
        return this;
    }
}
