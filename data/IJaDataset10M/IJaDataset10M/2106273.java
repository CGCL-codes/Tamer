package org.springframework.aop.framework.autoproxy.metadata;

import java.io.IOException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.metadata.commons.CommonsAttributeCompilerUtils;

/**
 * Metadata auto proxy creator test that sources attributes 
 * using Jakarta Commons Attributes.
 *
 * <p><b>Requires source-level metadata compilation.</b>
 * See the commonsBuild.xml Ant build script.
 *
 * @author Rod Johnson
 * @since 13.03.2003
 */
public class CommonsAttributesMetadataAutoProxyTests extends AbstractMetadataAutoProxyTests {

    static {
        CommonsAttributeCompilerUtils.compileAttributesIfNecessary("**/autoproxy/metadata/*.java");
    }

    protected BeanFactory getBeanFactory() throws IOException {
        return new ClassPathXmlApplicationContext(new String[] { "/org/springframework/aop/framework/autoproxy/metadata/commonsAttributes.xml", "/org/springframework/aop/framework/autoproxy/metadata/enterpriseServices.xml" });
    }
}
