package org.odlabs.wiquery.plugin.jquertytools.tooltip;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.protocol.http.WebRequestCycle;
import org.odlabs.wiquery.core.javascript.JsScopeContext;
import org.odlabs.wiquery.plugin.jquertytools.JQueryToolsUiEvent;
import org.odlabs.wiquery.plugin.jquertytools.tooltip.TooltipBehavior.Effect;
import org.odlabs.wiquery.plugin.jquertytools.tooltip.TooltipBehavior.Offset;
import org.odlabs.wiquery.plugin.jquertytools.tooltip.TooltipBehavior.Position;

/**
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public class TooltipAjaxBehaviour extends AbstractDefaultAjaxBehavior {

    private static final long serialVersionUID = 1L;

    private static String EVENT = "ttEvent";

    private static String TOP = "ttTop";

    private static String LEFT = "ttLEFT";

    /**
	 * List of tooltip's events.
	 */
    private static enum TooltipEvent {

        onShow, onBeforeHide, onHide, onBeforeShow
    }

    private static class JQueryToolsAjaxUiEvent extends JQueryToolsUiEvent {

        private static final long serialVersionUID = 1L;

        private TooltipAjaxBehaviour behaviour;

        private TooltipEvent event;

        public JQueryToolsAjaxUiEvent(TooltipAjaxBehaviour behaviour, TooltipEvent event) {
            this.behaviour = behaviour;
            this.event = event;
        }

        @Override
        protected void execute(JsScopeContext scopeContext) {
            scopeContext.append(new StringBuffer().append("var url = '").append(behaviour.getCallbackUrl(true)).append("&").append(EVENT).append("=").append(event.name()).append("wicketAjaxGet(url, null,null, function() {return true;})").toString());
        }
    }

    private static class JQueryToolsOnBeforeShowUiAjaxUiEvent extends JQueryToolsOnBeforeShowUiEvent {

        private static final long serialVersionUID = 1L;

        private TooltipAjaxBehaviour behaviour;

        private TooltipEvent event;

        public JQueryToolsOnBeforeShowUiAjaxUiEvent(TooltipAjaxBehaviour behaviour, TooltipEvent event) {
            this.behaviour = behaviour;
            this.event = event;
        }

        @Override
        protected void execute(JsScopeContext scopeContext) {
            scopeContext.append(new StringBuffer().append("var top = position.top+'';").append("var left = position.left+'';").append("var url = '").append(behaviour.getCallbackUrl(true)).append("&").append(EVENT).append("=").append(event.name()).append("&").append(TOP).append("=").append("'+top+'")).append("&").append(LEFT).append("=").append("'+left;").append("wicketAjaxGet(url, null,null, function() {return true;})").toString();
        }
    }

    /**
	 * Base interface for events.
	 * 
	 * @author Ernesto Reinaldo Barreiro
	 *
	 */
    public static interface IToolTipEventHandler extends Serializable {
    }

    /**
	 * Event handler.
	 */
    public static interface IToolTipUIEventHandler extends IToolTipEventHandler {

        public void onEvent(AjaxRequestTarget target, Component component);
    }

    /**
	 * Event handler.
	 */
    public static interface IToolTipOnBeforeShowUIEventHandler extends IToolTipEventHandler {

        public void onEvent(AjaxRequestTarget target, Component component, int top, int left);
    }

    private TooltipBehavior inner;

    private Map<TooltipEvent, IToolTipEventHandler> events = new HashMap<TooltipEvent, IToolTipEventHandler>();

    /**
	 * Constructor.
	 */
    public TooltipAjaxBehaviour() {
        inner = new TooltipBehavior();
    }

    @Override
    protected void onBind() {
        super.onBind();
        getComponent().add(inner);
    }

    @Override
    protected final void respond(AjaxRequestTarget target) {
        String event = WebRequestCycle.get().getRequest().getParameter(EVENT);
        if (!isEmpty(event)) {
            IToolTipEventHandler ajaxHandler = events.get(TooltipEvent.valueOf(event));
            if (ajaxHandler instanceof IToolTipOnBeforeShowUIEventHandler) {
                int top = (int) Float.parseFloat(WebRequestCycle.get().getRequest().getParameter(TOP));
                int left = (int) Float.parseFloat(WebRequestCycle.get().getRequest().getParameter(LEFT));
                ((IToolTipOnBeforeShowUIEventHandler) ajaxHandler).onEvent(target, this.getComponent(), top, left);
            } else if (ajaxHandler instanceof IToolTipUIEventHandler) {
                ((IToolTipUIEventHandler) ajaxHandler).onEvent(target, this.getComponent());
            }
        }
    }

    public static boolean isEmpty(String str) {
        return (str == null || str.trim().length() == 0);
    }

    /**
	 *  Triggered after the tooltip is shown.
	 * 
	 * @param onShow The onShow event handler.
	 * @return instance of the current behavior
	 */
    public TooltipAjaxBehaviour setOnShowAjaxEvent(IToolTipUIEventHandler onShow) {
        this.events.put(TooltipEvent.onShow, onShow);
        this.inner.setOnShowEvent(new JQueryToolsAjaxUiEvent(this, TooltipEvent.onShow));
        return this;
    }

    /**
	 * Triggered  before the tooltip is hidden.
	 * 
	 * @param onBeforeHide The on onBeforeHide event handler.
	 * @return instance of the current behavior
	 */
    public TooltipAjaxBehaviour setOnBeforeHideEvent(IToolTipUIEventHandler onBeforeHide) {
        this.events.put(TooltipEvent.onBeforeHide, onBeforeHide);
        this.inner.setOnBeforeHideEvent(new JQueryToolsAjaxUiEvent(this, TooltipEvent.onBeforeHide));
        return this;
    }

    /**
	 * Triggered when the tooltip is hidden.
	 * 
	 * @param onHide  The on onHide event handler.
	 * @return instance of the current behavior
	 */
    public TooltipAjaxBehaviour setOnHideEvent(IToolTipUIEventHandler onHide) {
        this.events.put(TooltipEvent.onHide, onHide);
        this.inner.setOnHideEvent(new JQueryToolsAjaxUiEvent(this, TooltipEvent.onHide));
        return this;
    }

    /**
	 * Triggered before the tooltip is shown. second argument is the tooltip 
	 * position to be used. This is an object with values 
	 * {top: integer, left: integer}
	 * 
	 * @param onBeforeShow The on onHide event handler.
	 * @return instance of the current behavior
	 */
    public TooltipAjaxBehaviour setOnBeforeShow(IToolTipOnBeforeShowUIEventHandler onBeforeShow) {
        this.events.put(TooltipEvent.onBeforeShow, onBeforeShow);
        this.inner.setOnBeforeShow(new JQueryToolsOnBeforeShowUiAjaxUiEvent(this, TooltipEvent.onBeforeShow));
        return this;
    }

    public TooltipBehavior getInner() {
        return inner;
    }

    /** Set's delay (default of 30).
	 * @param delay The delay to set
	 * @return instance of the current behavior
	 */
    public TooltipAjaxBehaviour setDelay(int delay) {
        getInner().setDelay(delay);
        return this;
    }

    /**
	 * @return The delay
	 */
    public int getDelay() {
        return getInner().getDelay();
    }

    /** Set's effect.
	 * @param effect The effect
	 * @return instance of the current behavior
	 */
    public TooltipAjaxBehaviour setEffect(Effect effect) {
        this.getInner().setEffect(effect);
        return this;
    }

    /**
	 * @return The effect option
	 */
    public Effect getEffect() {
        return this.getInner().getEffect();
    }

    /** 
	 * A jQuery selector for a single tooltip element. 
	 * For example #mytip. This option is only valid if you want to manually 
	 * define a single tooltip for multiple trigger elements.   
	 * 
	 * @param tip The tip.
	 * @return instance of the current behavior
	 */
    public TooltipAjaxBehaviour setTip(String tip) {
        this.getInner().setTip(tip);
        return this;
    }

    /** 
	 * Since 1.2.0 the HTML layout for the generated tooltip. The HTML can 
	 * be as complex a layout as you wish. You can, for example, add a nested 
	 * span element as a placeholder for an arrow. 
	 * 
	 * @param layout The layout.
	 * @return instance of the current behavior
	 */
    public TooltipAjaxBehaviour setLayout(String layout) {
        this.getInner().setLayout(layout);
        return this;
    }

    /**
	 * @return The layout
	 */
    public String getLayout() {
        return getInner().getLayout();
    }

    /** 
	 * Specifies the delay (in milliseconds) before the tooltip is shown. 
	 * By default there is no delay. 
	 * 
	 * @param predelay The delay to set
	 * @return instance of the current behavior
	 */
    public TooltipAjaxBehaviour setPredelay(int predelay) {
        getInner().setPredelay(predelay);
        return this;
    }

    /**
	 * @return The predelay
	 */
    public int getPredelay() {
        return getInner().getPredelay();
    }

    /** 
	 * The transparency of the tooltip. For example, 0 means invisible, 1 means no 
	 * transparency (fully visible) and 0.4 means that 40% of the tooltip is shown. 
	 * If your tooltip uses a CSS background image, you can set the transparency of 
	 * the image if it has been saved in the PNG24 graphics format. Remember that 
	 * Internet Explorer 6 does not natively support PNG transparency.
	 * 
	 * @param opacity The opacity to set (default 1)
	 * @return instance of the current behavior
	 */
    public TooltipAjaxBehaviour setOpacity(float opacity) {
        getInner().setOpacity(opacity);
        return this;
    }

    /**
	 * @return The opacity
	 */
    public float getOpacity() {
        return getInner().getOpacity();
    }

    /**
	 * Use a component as tip.
	 * 
	 * @param tip The component to use as tip.
	 * @return
	 */
    public TooltipAjaxBehaviour setTip(Component tip) {
        this.getInner().setTip(tip);
        return this;
    }

    /**
	 * @return The tip option
	 */
    public String getTip() {
        String tip = this.getInner().getTip();
        return tip;
    }

    /** 
	 * A jQuery selector for a single tooltip element. 
	 * For example #mytip. This option is only valid if you want to manually 
	 * define a single tooltip for multiple trigger elements.   
	 * 
	 * @param tipClass The tipClass option.
	 * @return instance of the current behavior
	 */
    public TooltipAjaxBehaviour setTipClass(String tipClass) {
        this.getInner().setTipClass(tipClass);
        return this;
    }

    /**
	 * @return The relative option
	 */
    public String getTipClass() {
        return getInner().getTipClass();
    }

    /** Set's the position.
	 * @param effect The position
	 * @return instance of the current behavior
	 */
    public TooltipAjaxBehaviour setPosition(Position position) {
        this.getInner().setPosition(position);
        return this;
    }

    /**
	 * @return The position option
	 */
    public Position getPosition() {
        return this.getInner().getPosition();
    }

    /** Set's the position.
	 * @param effect The position
	 * @return instance of the current behavior
	 */
    public TooltipAjaxBehaviour setOffset(Offset offset) {
        this.getInner().setOffset(offset);
        return this;
    }

    /**
	 * @return The offset option
	 */
    public Offset getOffset() {
        return getInner().getOffset();
    }

    /** 
	 * Since 1.1.1. by default the tooltip position is now determined relative 
	 * to the document (by using the offset() method of jQuery). By enabling this 
	 * property the tooltip position is determined relative to the parent element  
	 * 
	 * @param relative The relative option (default false).
	 * @return instance of the current behavior
	 */
    public TooltipAjaxBehaviour setRelative(boolean relative) {
        this.getInner().setRelative(relative);
        return this;
    }

    /**
	 * @return The relative option
	 */
    public boolean getRelative() {
        return getInner().getRelative();
    }
}
