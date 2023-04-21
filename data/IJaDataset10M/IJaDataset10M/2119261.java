package net.sf.freenote.mindmap.model;

import java.util.ArrayList;
import java.util.List;
import net.sf.freenote.FreeNoteConstants;
import net.sf.freenote.directedit.DirectEditable;
import net.sf.freenote.model.Connection;
import net.sf.freenote.model.Shape;
import net.sf.freenote.model.ShapesContainer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

/**
 * 分支主题
 * @author levin
 * @since 2008-2-11 下午08:18:11
 */
public class BranchShape extends Shape implements ShapesContainer, DirectEditable {

    private static final Image BRANCH_ICON = createImage("icons/branch.gif");

    private List<BranchShape> children;

    private String desc;

    private int style;

    public BranchShape() {
        super();
        setBackColor(new RGB(250, 250, 250));
    }

    @Override
    public IPropertyDescriptor[] getPropertyDescriptors() {
        IPropertyDescriptor[] propertyDescriptors = super.getPropertyDescriptors();
        IPropertyDescriptor[] ret = new IPropertyDescriptor[propertyDescriptors.length + 2];
        System.arraycopy(propertyDescriptors, 0, ret, 2, propertyDescriptors.length);
        ret[0] = new TextPropertyDescriptor(FreeNoteConstants.DESC_PROPERTY, FreeNoteConstants.DESC_PROPERTY);
        ret[1] = new ComboBoxPropertyDescriptor(FreeNoteConstants.STYLE_PROP, FreeNoteConstants.STYLE_PROP, new String[] { "默认", "竖向", "横向", "星形" });
        return ret;
    }

    @Override
    public Image getIcon() {
        return BRANCH_ICON;
    }

    @Override
    public boolean addChild(Shape s) {
        if (s != null && s instanceof BranchShape) {
            getChildren().add((BranchShape) s);
            Connection conn = new Connection(this, s);
            conn.setTargetDecoration(0);
            ((BranchShape) s).setStyle(this.getStyle());
            firePropertyChange(FreeNoteConstants.CHILD_ADDED_PROP, null, s);
            return true;
        }
        return false;
    }

    @Override
    public List getChildren() {
        return children == null ? children = new ArrayList<BranchShape>() : children;
    }

    @Override
    public boolean removeChild(Shape s) {
        if (s != null && getChildren().remove(s)) {
            firePropertyChange(FreeNoteConstants.CHILD_REMOVED_PROP, null, s);
            return true;
        }
        return false;
    }

    public String getDesc() {
        return desc == null ? desc = "" : desc;
    }

    public void setDesc(String name) {
        this.desc = name;
        firePropertyChange(FreeNoteConstants.DESC_PROPERTY, null, name);
    }

    @Override
    public Object getPropertyValue(Object propertyId) {
        if (propertyId.equals(FreeNoteConstants.DESC_PROPERTY)) return getDesc();
        if (propertyId.equals(FreeNoteConstants.STYLE_PROP)) return getStyle();
        return super.getPropertyValue(propertyId);
    }

    @Override
    public void setPropertyValue(Object propertyId, Object value) {
        if (propertyId.equals(FreeNoteConstants.DESC_PROPERTY)) setDesc(String.valueOf(value));
        if (propertyId.equals(FreeNoteConstants.STYLE_PROP)) setStyle((Integer) value);
        super.setPropertyValue(propertyId, value);
    }

    public int getStyle() {
        return style == 0 ? style = 2 : style;
    }

    public void setStyle(int style) {
        this.style = style;
        firePropertyChange(FreeNoteConstants.STYLE_PROP, null, style);
    }

    public void setChildren(List<BranchShape> children) {
        this.children = children;
    }
}
