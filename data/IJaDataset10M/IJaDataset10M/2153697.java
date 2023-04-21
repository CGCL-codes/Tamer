package org.qtitools.qti.group.result;

import java.util.List;
import org.qtitools.qti.group.AbstractNodeGroup;
import org.qtitools.qti.node.XmlNode;
import org.qtitools.qti.node.result.AbstractResult;
import org.qtitools.qti.node.result.ItemVariable;
import org.qtitools.qti.node.result.ItemVariableType;

/**
 * Group of assessmentSection children.
 * 
 * @author Jiri Kajaba
 */
public class ItemVariableGroup extends AbstractNodeGroup {

    private static final long serialVersionUID = 1L;

    /**
	 * Constructs group.
	 *
	 * @param parent parent of created group
	 */
    public ItemVariableGroup(AbstractResult parent) {
        super(parent, ItemVariable.DISPLAY_NAME, null, null);
        getAllSupportedClasses().clear();
        for (ItemVariableType itemVariableType : ItemVariableType.values()) getAllSupportedClasses().add(itemVariableType.toString());
    }

    @Override
    public boolean isGeneral() {
        return true;
    }

    /**
	 * Gets list of all children.
	 *
	 * @return list of all children
	 */
    @SuppressWarnings("unchecked")
    public List<ItemVariable> getItemVariables() {
        return (List<ItemVariable>) ((List<? extends XmlNode>) getChildren());
    }

    /**
	 * Creates child with given QTI class name.
	 * <p>
	 * Parameter classTag is needed only if group can contain children with different QTI class names.
	 *
	 * @param classTag QTI class name (this parameter is ignored)
	 * @return created child
	 */
    public ItemVariable create(String classTag) {
        return ItemVariableType.getInstance((AbstractResult) getParent(), classTag);
    }
}
