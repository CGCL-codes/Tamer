package self.amigo.elem.uml;

import java.util.HashMap;
import java.util.Map;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import self.awt.ILineRenderInfo;
import self.gee.IGraphicElement;
import self.lang.StringUtils;
import self.xml.DomUtils;

public class StateTransitionView extends ElementryLinkView {

    /**
	 * If the internal state of this class ever changes in such a way that it can't be defaulted,
	 * then the {@link #serialVersionUID} should be incremented to ensure serialized instances cleanly fail.  
	 */
    private static final long serialVersionUID = 1;

    public static final String STEREOTYPE_PROP = "stereotype";

    public static final String EVENT_SIG_PROP = "eventsignature";

    public static final String GUARD_COND_PROP = "guardcondition";

    public static final String ACTION_EXPR_PROP = "actionexpression";

    public StateTransitionView() {
        ((ConfigurableCore) delegate).setSideHead(false, ILineRenderInfo.OPEN_ARROW_HEAD);
    }

    protected IGraphicElement createDelegate() {
        TransitionCore line = new TransitionCore();
        return line;
    }

    protected class TransitionCore extends ConfigurableCore {

        /**
	 * If the internal state of this class ever changes in such a way that it can't be defaulted,
	 * then the {@link #serialVersionUID} should be incremented to ensure serialized instances cleanly fail.  
	 */
        private static final long serialVersionUID = 1;

        protected String eventSignature = "";

        protected String guardCondition = "";

        protected String actionExpression = "";

        public void getProperties(Map store) {
            store.put(STEREOTYPE_PROP, header);
            store.put(EVENT_SIG_PROP, eventSignature);
            store.put(GUARD_COND_PROP, guardCondition);
            store.put(ACTION_EXPR_PROP, actionExpression);
        }

        private void resetName() {
            StringBuffer transString = new StringBuffer(eventSignature);
            if (!StringUtils.isNullOrEmpty(guardCondition)) {
                transString.append(" [");
                transString.append(guardCondition);
                transString.append(']');
            }
            if (!StringUtils.isNullOrEmpty(actionExpression)) {
                transString.append(" /");
                transString.append(actionExpression);
            }
            name = transString.toString().trim();
        }

        public void setProperties(Map data) {
            header = (String) data.get(STEREOTYPE_PROP);
            eventSignature = (String) data.get(EVENT_SIG_PROP);
            guardCondition = (String) data.get(GUARD_COND_PROP);
            actionExpression = (String) data.get(ACTION_EXPR_PROP);
            resetName();
            layer.setDirty();
        }

        protected void readGeneralLineInfo(Node self, HashMap idObjLookUp) throws DOMException {
            header = DomUtils.getElementAttribute(self, STEREOTYPE_PROP);
            eventSignature = DomUtils.getElementAttribute(self, EVENT_SIG_PROP);
            guardCondition = DomUtils.getElementAttribute(self, GUARD_COND_PROP);
            actionExpression = DomUtils.getElementAttribute(self, ACTION_EXPR_PROP);
            resetName();
        }

        protected void writeGeneralLineInfo(Document doc, Element self, HashMap objIdLookup) throws DOMException {
            DomUtils.setElementAttribute(self, STEREOTYPE_PROP, StringUtils.toEmptyIfNull(header));
            DomUtils.setElementAttribute(self, EVENT_SIG_PROP, StringUtils.toEmptyIfNull(eventSignature));
            DomUtils.setElementAttribute(self, GUARD_COND_PROP, StringUtils.toEmptyIfNull(guardCondition));
            DomUtils.setElementAttribute(self, ACTION_EXPR_PROP, StringUtils.toEmptyIfNull(actionExpression));
        }
    }
}
