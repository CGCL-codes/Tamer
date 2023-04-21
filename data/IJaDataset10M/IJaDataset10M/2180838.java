package org.pustefixframework.config.contextxmlservice.parser;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.pustefixframework.config.contextxmlservice.ProcessActionPageRequestConfig;
import org.pustefixframework.config.contextxmlservice.ProcessActionStateConfig;
import org.pustefixframework.config.contextxmlservice.parser.internal.PageRequestProcessActionConfigExtensionImpl;
import org.pustefixframework.config.contextxmlservice.parser.internal.PageRequestProcessActionConfigExtensionPointImpl;
import org.pustefixframework.config.generic.AbstractExtensionParsingHandler;
import org.pustefixframework.extension.PageRequestProcessActionConfigExtension.ProcessActionConfig;
import org.pustefixframework.extension.support.ExtensionTargetInfo;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import com.marsching.flexiparse.parser.HandlerContext;
import com.marsching.flexiparse.parser.exception.ParserException;

/**
 * Creates an extension for a process action configuration extension point.
 * 
 * @author Sebastian Marsching <sebastian.marsching@1und1.de>
 */
public class PageRequestProcessActionConfigExtensionParsingHandler extends AbstractExtensionParsingHandler {

    @Override
    protected BeanDefinition createExtension(String type, Collection<ExtensionTargetInfo> extensionTargetInfos, HandlerContext context) throws ParserException {
        Map<String, ProcessActionStateConfig> processActionStateConfigs = new HashMap<String, ProcessActionStateConfig>();
        List<ProcessActionConfigImpl> processActionConfigs = new LinkedList<ProcessActionConfigImpl>();
        List<Object> processActionConfigObjects = new LinkedList<Object>();
        for (Object o : context.getObjectTreeElement().getObjectsOfTypeFromSubTree(Object.class)) {
            if (o instanceof ProcessActionStateConfig) {
                ProcessActionStateConfig processActionStateConfig = (ProcessActionStateConfig) o;
                processActionStateConfigs.put(processActionStateConfig.getName(), processActionStateConfig);
            } else if (o instanceof ProcessActionPageRequestConfig) {
                ProcessActionConfigImpl processActionConfig = new ProcessActionConfigImpl();
                processActionConfig.processActionPageRequestConfig = (ProcessActionPageRequestConfig) o;
                processActionConfigObjects.add(processActionConfig);
                processActionConfigs.add(processActionConfig);
            } else if (o instanceof PageRequestProcessActionConfigExtensionPointImpl) {
                processActionConfigObjects.add(o);
            }
        }
        for (ProcessActionConfigImpl processActionConfig : processActionConfigs) {
            processActionConfig.processActionStateConfig = processActionStateConfigs.get(processActionConfig.getName());
        }
        BeanDefinitionBuilder beanBuilder = BeanDefinitionBuilder.genericBeanDefinition(PageRequestProcessActionConfigExtensionImpl.class);
        beanBuilder.addPropertyValue("processActionConfigObjects", processActionConfigObjects);
        return beanBuilder.getBeanDefinition();
    }

    private class ProcessActionConfigImpl implements ProcessActionConfig {

        private ProcessActionPageRequestConfig processActionPageRequestConfig;

        private ProcessActionStateConfig processActionStateConfig;

        public String getName() {
            return processActionPageRequestConfig.getName();
        }

        public ProcessActionPageRequestConfig getProcessActionPageRequestConfig() {
            return processActionPageRequestConfig;
        }

        public ProcessActionStateConfig getProcessActionStateConfig() {
            return processActionStateConfig;
        }
    }
}
