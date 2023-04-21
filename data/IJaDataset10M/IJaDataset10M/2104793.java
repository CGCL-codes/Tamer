package org.pustefixframework.config.contextxmlservice.parser;

import java.util.Collection;
import java.util.List;
import org.pustefixframework.config.contextxmlservice.parser.internal.AuthConstraintExtensionImpl;
import org.pustefixframework.config.contextxmlservice.parser.internal.AuthConstraintExtensionPointImpl;
import org.pustefixframework.config.generic.AbstractExtensionParsingHandler;
import org.pustefixframework.extension.support.ExtensionTargetInfo;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedList;
import com.marsching.flexiparse.parser.HandlerContext;
import com.marsching.flexiparse.parser.exception.ParserException;
import de.schlund.pfixcore.auth.AuthConstraint;

/**
 * Creates an extension for a authconstraint extension point.  
 * 
 * @author mleidig@schlund.de
 */
public class AuthConstraintExtensionParsingHandler extends AbstractExtensionParsingHandler {

    @Override
    protected BeanDefinition createExtension(String type, Collection<ExtensionTargetInfo> extensionTargetInfos, HandlerContext context) throws ParserException {
        @SuppressWarnings("unchecked") List<Object> authConstraintObjects = new ManagedList();
        for (Object o : context.getObjectTreeElement().getObjectsOfTypeFromSubTree(Object.class)) {
            if (o instanceof AuthConstraint) {
                authConstraintObjects.add(o);
            } else if (o instanceof AuthConstraintExtensionPointImpl) {
                authConstraintObjects.add(o);
            }
        }
        BeanDefinitionBuilder beanBuilder = BeanDefinitionBuilder.genericBeanDefinition(AuthConstraintExtensionImpl.class);
        beanBuilder.addPropertyValue("authConstraintObjects", authConstraintObjects);
        return beanBuilder.getBeanDefinition();
    }
}
