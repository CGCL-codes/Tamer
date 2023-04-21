package org.mc4j.console.bean.attribute;

import org.mc4j.console.Refreshable;
import org.mc4j.console.bean.RefreshAction;
import org.mc4j.console.install.ExplorerUtil;
import org.mc4j.ems.connection.bean.EmsBean;
import org.openide.nodes.AbstractNode;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.SystemAction;

/**
 * The MBeanNode child that holds the attributes.
 *
 * @author Greg Hinkle (ghinkle@users.sourceforge.net), January 2002
 * @version $Revision: 570 $($Author: ghinkl $ / $Date: 2006-04-12 15:14:16 -0400 (Wed, 12 Apr 2006) $)
 */
public class AttributeSetNode extends AbstractNode implements Refreshable {

    public static final String NAME = "Attributes";

    private boolean hasUnsupportedType = false;

    private EmsBean emsBean;

    public AttributeSetNode(EmsBean emsBean) {
        super(new AttributeSetChildren(emsBean));
        this.emsBean = emsBean;
        setIconBase("org/mc4j/console/bean/attribute/AttributeSetNodeIcon");
        setName(NAME);
        setDisplayName(NbBundle.getMessage(AttributeSetNode.class, "LBL_node"));
        setShortDescription(NbBundle.getMessage(AttributeSetNode.class, "HINT_node"));
    }

    public void refresh() {
        if (ExplorerUtil.isExpanded(this)) {
            retrieveData();
        }
    }

    public void retrieveData() {
        emsBean.refreshAttributes();
    }

    protected SystemAction[] createActions() {
        return new SystemAction[] { SystemAction.get(RefreshAction.class) };
    }

    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    protected AttributeSetChildren getAttributeSetChildren() {
        return (AttributeSetChildren) getChildren();
    }
}
