package com.icesoft.faces.context.effects;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Element;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.util.Map;
import java.util.List;
import com.icesoft.faces.renderkit.dom_html_basic.DomBasicRenderer;

/**
 * Encode an effect call to an javascript event
 */
public class LocalEffectEncoder {

    private static final String[] ALL_EVENTS = { "onblur", "onchange", "onclick", "oncontextmenu", "ondblclick", "onfocus", "onkeydown", "onkeypress", "onkeyup", "onmousedown", "onmousemove", "onmouseover", "onmouseup", "onmouseout", "onreset", "onselect", "onsubmit" };

    private static final String[] ALL_EFFECTS = { null, "onchangeeffect", "onclickeffect", null, "ondblclickeffect", null, "onkeydowneffect", "onkeypresseffect", "onkeyupeffect", "onmousedowneffect", "onmousemoveeffect", "onmouseovereffect", "onmouseupeffect", "onmouseouteffect", "onreseteffect", null, "onsubmiteffect" };

    private static final String[] EVENTS = { "click", "dblclick", "mousedown", "mouseup", "mousemove", "mouseover", "mouseout", "change", "reset", "submit", "keypress", "keydown", "keyup" };

    private static String[] ATTRIBUTES = new String[EVENTS.length];

    private static final String ATTRIBUTE_PREFIX = "on";

    private static String[] EFFECTS = new String[EVENTS.length];

    private static final String EFFECT_SUFFIX = "effect";

    static {
        for (int index = 0; index < EVENTS.length; index++) {
            ATTRIBUTES[index] = ATTRIBUTE_PREFIX + EVENTS[index];
            EFFECTS[index] = ATTRIBUTES[index] + EFFECT_SUFFIX;
        }
    }

    private static final Log log = LogFactory.getLog(LocalEffectEncoder.class);

    /**
     * We only want to try processing the events that are relevant to each
     *  specific component, so we want the intersection of the complete event 
     *  list, and the component's pass-through attributes. So that the indexes 
     *  still match up, we just null out the undesired entries that we return.
     * 
     * @param passthruAttributes Appropriate attributes from ExtendedAttributeConstants
     * @return The intersection of the complete event list and passthruAttributes. Whatever's in both lists.
     */
    public static String[] maskEvents(String[] passthruAttributes) {
        int len = ALL_EVENTS.length;
        String[] events = new String[len];
        for (int i = 0; i < len; i++) {
            String curr = ALL_EVENTS[i];
            for (int j = passthruAttributes.length - 1; j >= 0; j--) {
                if (curr.equals(passthruAttributes[j])) {
                    events[i] = curr;
                    break;
                }
            }
        }
        return events;
    }

    /**
     * For every javascript event in the events parameter, combine the 
     *  effect javascript if it's appropriate , the application javascript 
     *  from the passthrough attribute, and the component renderers value 
     *  from the rendererValues parameter, in that sequence, and output it 
     *  to the DOM Element or the ResponseWriter, whichever is appropriate.
     * 
     * @param facesContext
     * @param comp
     * @param events The result of calling maskEvents(String[])
     * @param rendererValues Map where they keys are entries in ALL_EVENTS, and the values are the javascript that the component renderer wishes to emit
     * @param rootNode
     * @param writer
     */
    public static void encode(FacesContext facesContext, UIComponent comp, String[] events, Map rendererValues, Element rootNode, ResponseWriter writer) {
        Map atts = comp.getAttributes();
        try {
            for (int i = 0; i < events.length; i++) {
                String currentEvent = events[i];
                if (currentEvent == null) continue;
                String effectValue = null;
                if (ALL_EFFECTS[i] != null) {
                    Effect fx = (Effect) atts.get(ALL_EFFECTS[i]);
                    if (fx == null) {
                        if (comp.getValueBinding(ALL_EFFECTS[i]) != null) {
                            fx = new BlankEffect();
                        }
                    }
                    if (fx != null) {
                        effectValue = JavascriptContext.applyEffect(fx, comp.getClientId(facesContext), facesContext);
                    }
                }
                String applicationValue = (String) atts.get(currentEvent);
                String rendererValue = rendererValues == null ? null : (String) rendererValues.get(currentEvent);
                String value = DomBasicRenderer.combinedPassThru(DomBasicRenderer.combinedPassThru(effectValue, applicationValue), rendererValue);
                if (value != null) {
                    if (rootNode != null) {
                        rootNode.setAttribute(currentEvent, value);
                    } else if (writer != null) {
                        writer.writeAttribute(currentEvent, value, null);
                    }
                }
            }
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error(e.getMessage(), e);
            }
        }
    }

    public static void encodeLocalEffects(UIComponent comp, Element rootNode, FacesContext facesContext, ResponseWriter writer, boolean attribTracking, List attributesThatAreSet) {
        if (attribTracking && (attributesThatAreSet == null || attributesThatAreSet.size() == 0)) {
            return;
        }
        Map atts = comp.getAttributes();
        try {
            for (int i = 0; i < EVENTS.length; i++) {
                if (attribTracking && (attributesThatAreSet == null || !attributesThatAreSet.contains(EFFECTS[i]))) {
                    continue;
                }
                Effect fx = (Effect) atts.get(EFFECTS[i]);
                if (fx == null) {
                    if (comp.getValueBinding(EFFECTS[i]) != null) {
                        fx = new BlankEffect();
                    }
                }
                if (fx != null) {
                    String value = JavascriptContext.applyEffect(fx, comp.getClientId(facesContext), facesContext);
                    String original;
                    if (attribTracking && (attributesThatAreSet == null || !attributesThatAreSet.contains(ATTRIBUTES[i]))) {
                        original = null;
                    } else {
                        original = (String) atts.get(ATTRIBUTES[i]);
                    }
                    String together = DomBasicRenderer.combinedPassThru(value, original);
                    if (together != null) {
                        if (rootNode != null) {
                            rootNode.setAttribute(ATTRIBUTES[i], together);
                        } else if (writer != null) {
                            writer.writeAttribute(ATTRIBUTES[i], together, null);
                        }
                    }
                }
            }
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error(e.getMessage(), e);
            }
        }
    }

    public static void encodeLocalEffects(UIComponent comp, Element rootNode, FacesContext facesContext) {
        encodeLocalEffects(comp, rootNode, facesContext, null, false, null);
    }

    public static void encodeLocalEffects(UIComponent comp, ResponseWriter writer, FacesContext facesContext) {
        encodeLocalEffects(comp, null, facesContext, writer, false, null);
    }

    public static void encodeLocalEffect(String id, Effect fx, String event, FacesContext facesContext) {
        String value = JavascriptContext.applyEffect(fx, id, facesContext);
        String js = "$('" + id + "').on" + event + "=function(){" + value + "};";
        JavascriptContext.addJavascriptCall(facesContext, js);
    }
}
