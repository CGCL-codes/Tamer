package org.docflower.page.bind;

import java.util.ArrayList;
import org.apache.ws.commons.schema.XmlSchemaType;
import org.docflower.ui.INavigableUIPart;
import org.docflower.ui.UpdateInfo;
import org.docflower.util.ActionHandlerException;
import org.docflower.util.value.AbstractValueManager;
import org.docflower.util.value.ValueFactory;
import org.docflower.xml.DOMUtils;
import org.docflower.xml.ExpressionManager;
import org.docflower.xml.typeinfo.TypeInfo;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.w3c.dom.Node;

public class CellBinding extends AbstractBinding {

    public enum CellType {

        Static, Label, Edit
    }

    ;

    private CellType type;

    private Image image;

    private int ident;

    private int group = 0;

    private Font font;

    private Node valueNode;

    public CellBinding(INavigableUIPart parent, String xpath, String expressionUsedParam) {
        super(parent, xpath, expressionUsedParam);
    }

    public CellBinding(INavigableUIPart parent) {
        super(parent, null, null);
    }

    @Override
    public void fillPathPart(ArrayList<String> pathList) {
        if (getParent() != null) {
            getParent().fillPathPart(pathList);
        }
        int colNum = getTableViewer().getColumnViewerEditor().getFocusCell().getColumnIndex();
        pathList.add(Integer.toString(colNum));
    }

    @Override
    public INavigableUIPart getSelectedChild() {
        return null;
    }

    @Override
    public boolean isSelected() {
        if (!getTableViewer().getSelection().isEmpty()) {
            int colNum = getTableViewer().getColumnViewerEditor().getFocusCell().getColumnIndex();
            return getParent().getChildren().get(colNum) == this;
        }
        return false;
    }

    @Override
    protected int doNavigation(UpdateInfo updateInfo, int currentNavigationPart) {
        return 0;
    }

    @Override
    public void pullValue() {
    }

    @Override
    public void pushValue() {
    }

    private TableViewer getTableViewer() {
        return ((TableBinding) (getParent().getParent())).getTableViewer();
    }

    public void clearCachedData() {
        valueNode = null;
    }

    public CellType getType() {
        return type;
    }

    public void setType(CellType type) {
        this.type = type;
    }

    public void setType(String typeStr) {
        if ("textedit".equals(typeStr)) {
            this.type = CellType.Edit;
        } else {
            this.type = CellType.Label;
        }
    }

    public String getLocalValue(TableBinding binding) {
        if (getType() == CellType.Static) {
            return getXpath();
        } else {
            XmlSchemaType type = DOMUtils.getRealXmlSchemaType(getValueNode(binding), binding.getContext().getDataModel().getDescriptor().getSchemasManager(), binding.getContext().getDataModel().getDataNSContext());
            TypeInfo typeInfo = binding.getContext().getDataModel().getDescriptor().getSchemasManager().getTypeRestrictions(type);
            String valueStr = getValueNode(binding).getTextContent();
            AbstractValueManager valueManager = ValueFactory.getSingleton().getValueManager(typeInfo.getSimpleTypeName());
            if (valueManager != null) {
                valueStr = valueManager.internalToLocal(valueStr);
            }
            return valueStr;
        }
    }

    public void setValue(String value, TableBinding binding) {
        if ((getType() != CellType.Static) && (getType() != CellType.Label)) {
            String valueStr = getValueNode(binding).getTextContent();
            if (!valueStr.equals(value)) {
                try {
                    binding.getContext().getDataModel().setValue(getPath(), getValueNode(binding), value);
                } catch (ActionHandlerException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Node getValueNode(TableBinding binding) {
        if (valueNode == null) {
            ExpressionManager expressionManager = binding.getContext().getDataModel().getDescriptor().getExpressionManager();
            valueNode = expressionManager.getNodeByExpression(binding.getBaseNode(), getXpath(), binding.getNamespaceContext(), isExpressionUsed(), false);
        }
        return valueNode;
    }

    public void setValueNode(Node valueNode) {
        this.valueNode = valueNode;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Image getImage() {
        return image;
    }

    public void setStaticText(String text) {
        this.type = CellType.Static;
        setXpath(text);
    }

    public void setIdent(int ident) {
        this.ident = ident;
    }

    public int getIdent() {
        return ident;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public int getGroup() {
        return group;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public Font getFont() {
        return font;
    }
}
