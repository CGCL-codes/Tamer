package mixins;

import groovy.lang.GroovyObject;
import groovy.lang.webobjects.WOComponentCloner;
import javassist.gluonj.Glue;
import javassist.gluonj.Refine;
import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver._private.WOComponentReference;

@Glue
public class WOComponentReferenceAdditions {

    public static boolean isReloadable(Object object) {
        if (object == null) return false;
        Class<?>[] interfaces = object.getClass().getInterfaces();
        for (int i = 0; i < interfaces.length; i++) {
            if (interfaces[i].equals(GroovyObject.class)) {
                return true;
            }
        }
        return false;
    }

    @Refine
    public static class WOGroovyComponentReference extends WOComponentReference {

        public WOGroovyComponentReference() {
            super(null, null, null);
        }

        public void _pushComponentInContext(WOContext aContext) {
            WOComponent aParent = aContext.component();
            WOComponent aChild = null;
            if (aParent != null) aChild = aParent._subcomponentForElementWithID(aContext.elementID());
            if (isReloadable(aChild)) {
                aChild = WOComponentCloner.clone(aChild);
                if (aParent != null) aParent._setSubcomponent(aChild, aContext.elementID());
            }
            super._pushComponentInContext(aContext);
        }
    }
}
