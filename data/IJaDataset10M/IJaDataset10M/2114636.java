package progranet.ganesa.metamodel;

import java.io.Serializable;
import progranet.ganesa.model.Ganesa;
import progranet.omg.cmof.Element;
import progranet.omg.core.types.Type;

public class RuleNativeListenerImpl extends RuleImpl implements RuleNativeListener, Serializable {

    private static final long serialVersionUID = -8039764107838416836L;

    public Listener getNativeListener() {
        String listenerClassName = (String) this.get(Ganesa.RULE_NATIVE_LISTENER_LISTENER_CLASS_NAME);
        try {
            return (Listener) java.lang.Class.forName(listenerClassName).newInstance();
        } catch (InstantiationException e) {
            logger.error("Editor instantiation exception", e);
        } catch (IllegalAccessException e) {
            logger.error("Editor instantiation exception", e);
        } catch (ClassNotFoundException e) {
            logger.error("Editor instantiation exception", e);
        }
        return null;
    }

    public void onPost(Element element, Event event, Facet facet) {
        this.getNativeListener().onPost(element, event, facet);
    }

    public boolean onPre(Element element, Account account, Facet facet) {
        return this.getNativeListener().onPre(element, account, facet);
    }

    public Type getClassifier() {
        return Ganesa.RULE_NATIVE_LISTENER;
    }

    public String getName() {
        return "NativeListener@" + this.getClazz().getQualifiedName();
    }

    public void validate() throws Exception {
        super.validate();
        @SuppressWarnings("unused") Listener listener = (Listener) java.lang.Class.forName((String) this.get(Ganesa.RULE_NATIVE_LISTENER_LISTENER_CLASS_NAME)).newInstance();
    }
}
