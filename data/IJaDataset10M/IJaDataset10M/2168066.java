package org.cantaloop.jiomask.factory.javacode;

import org.cantaloop.cgimlet.lang.FirstLowerCaseIdentifierFactory;
import org.cantaloop.cgimlet.lang.FirstUpperCaseIdentifierFactory;
import org.cantaloop.cgimlet.lang.IdentifierFactory;
import org.cantaloop.cgimlet.lang.LinkedIdentifierFactory;
import org.cantaloop.cgimlet.lang.SuffixIdentifierFactory;

/**
 * DefaultJavaPDFactory.java
 *
 *
 * @created 2002-04-15 21:40:55 CEST
 *
 * @author <a href="mailto:stefan@cantaloop.org">Stefan Heimann</a>
 *
 * @version @version@ ($Revision: 1.2 $)
 */
public class JavaMetaNodeDescriptorFactoryImpl implements JavaMetaNodeDescriptorFactory {

    private final IdentifierFactory s_metaNodeClassNameFactory;

    private Context m_context;

    public JavaMetaNodeDescriptorFactoryImpl(Context context) {
        m_context = context;
        s_metaNodeClassNameFactory = new LinkedIdentifierFactory(FirstUpperCaseIdentifierFactory.getInstance(), new SuffixIdentifierFactory("MetaNode"));
    }

    public JavaStructureMetaNodeDescriptor getJavaStructureMetaNodeDescriptor(JavaMetaNodeDescriptor parent, JavaXFormsNode xformsNode) {
        JavaStructureMetaNodeDescriptor node = new JavaStructureMetaNodeDescriptor(parent, m_context, xformsNode);
        configure(node);
        return node;
    }

    public JavaListMetaNodeDescriptor getJavaListMetaNodeDescriptor(JavaMetaNodeDescriptor parent, JavaXFormsNode xformsNode) {
        JavaListMetaNodeDescriptor node = new JavaListMetaNodeDescriptor(parent, m_context, xformsNode);
        configure(node);
        return node;
    }

    public JavaSimpleMetaNodeDescriptor getJavaSimpleMetaNodeDescriptor(JavaMetaNodeDescriptor parent, JavaXFormsNode xformsNode) {
        JavaSimpleMetaNodeDescriptor node = new JavaSimpleMetaNodeDescriptor(parent, m_context, xformsNode);
        configure(node);
        return node;
    }

    private void configure(AbstractJavaMetaNodeDescriptor node) {
        node.setMetaNodeClassNameIdentifierFactory(s_metaNodeClassNameFactory);
    }
}
