package org.owasp.jxt.tag.core;

import org.owasp.jxt.JxtNode;
import org.owasp.jxt.JxtNodeFactory;

/**
 * CoreNodeFactory
 *
 * @author Jeffrey Ichnowski
 * @version $Revision: 8 $
 */
public final class CoreNodeFactory implements JxtNodeFactory {

    public JxtNode createNode(String name) {
        if ("page".equals(name)) {
            return new CorePageNode();
        } else if ("template".equals(name)) {
            return new CoreTemplateNode();
        } else if ("forEach".equals(name)) {
            return new CoreForEachNode();
        } else if ("code".equals(name)) {
            return new CoreCodeNode();
        } else if ("declaration".equals(name)) {
            return new CoreDeclarationNode();
        } else if ("if".equals(name)) {
            return new CoreIfNode();
        } else if ("choose".equals(name)) {
            return new CoreChooseNode();
        } else if ("when".equals(name)) {
            return new CoreWhenNode();
        } else if ("otherwise".equals(name)) {
            return new CoreOtherwiseNode();
        } else if ("catch".equals(name)) {
            return new CoreCatchNode();
        } else if ("out".equals(name)) {
            return new CoreOutNode();
        } else if ("comment".equals(name)) {
            return new CoreCommentNode();
        } else if ("set".equals(name)) {
            return new CoreSetNode();
        } else if ("declare".equals(name)) {
            return new CoreDeclareNode();
        } else {
            throw new IllegalArgumentException("Unknown node type: " + name);
        }
    }
}
