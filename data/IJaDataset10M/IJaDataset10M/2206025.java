package cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes;

import java.util.List;

/**
 * The node representing a module declaration.
 *
 * @author Jiri Schejbal
 */
public class ModuleDeclNode extends ModuleChildNode {

    public ModuleDeclNode(String namespacePrefix, String targetNameSpace) {
        assert (namespacePrefix != null);
        assert (targetNameSpace != null);
        addAttribute(AttrNames.ATTR_NAMESPACE_PREFIX, namespacePrefix);
        addAttribute(AttrNames.ATTR_TARGET_NAMESPACE, targetNameSpace);
    }

    @Override
    protected String getElementName() {
        return NodeNames.NODE_MODULE_DECL;
    }

    @Override
    public List<XQNode> getSubnodes() {
        return null;
    }
}
