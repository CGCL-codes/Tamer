package net.sourceforge.tapestryjfly.dojotapestry5.mixins;

import net.sourceforge.tapestryjfly.dojotapestry5.annotations.DojoInitialization;
import net.sourceforge.tapestryjfly.dojotapestry5.annotations.DojoRequire;
import net.sourceforge.tapestryjfly.dojotapestry5.annotations.DojoType;
import net.sourceforge.tapestryjfly.dojotapestry5.services.DojoRenderSupport;
import java.lang.annotation.Annotation;
import java.util.List;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.dom.Node;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.runtime.Component;

/**
 * Mixin class that enables your component to be decorated with
 * {@link net.sourceforge.tapestryjfly.dojotapestry5.annotations.DojoRequire} and
 * {@link net.sourceforge.tapestryjfly.dojotapestry5.annotations.DojoType} annotations.
 * @author kiuma
 */
public class DojoWidget {

    @Inject
    private DojoRenderSupport dojoRenderSupport;

    @Inject
    private ComponentResources componentResources;

    private String dojoType;

    private <T extends Annotation> T getDojoAnnotation(Class<T> annotationClass, Component component) {
        ComponentResources cr = component.getComponentResources();
        Annotation dojoAnnotation = component.getClass().getAnnotation(annotationClass);
        if (dojoAnnotation != null) {
            return annotationClass.cast(dojoAnnotation);
        } else {
            return null;
        }
    }

    void setupRender(MarkupWriter writer) {
        Component container = componentResources.getContainer();
        DojoInitialization dojoInitializationAnnotation = getDojoAnnotation(DojoInitialization.class, container);
        if (dojoInitializationAnnotation != null) {
            dojoRenderSupport.setDojoInitialization(dojoInitializationAnnotation.dojoSource(), dojoInitializationAnnotation.djConfig(), dojoInitializationAnnotation.html5Stye());
        }
        DojoRequire dojoRequire = getDojoAnnotation(DojoRequire.class, container);
        if (dojoRequire != null) {
            for (String require : dojoRequire.value()) {
                dojoRenderSupport.addDojoRequire(require);
            }
        }
        DojoType dojoTypeAnnotation = getDojoAnnotation(DojoType.class, container);
        if (dojoTypeAnnotation != null) {
            this.dojoType = dojoTypeAnnotation.value();
        }
    }

    void afterRender(MarkupWriter writer) {
        if (this.dojoType != null) {
            Element parentNode = writer.getElement();
            List<Node> children = parentNode.getChildren();
            Element node = (Element) children.get(children.size() - 1);
            node.attribute(dojoRenderSupport.getDojoTypeAttr(), dojoType);
        }
    }
}
