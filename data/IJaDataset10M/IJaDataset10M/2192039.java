package net.sealisland.gt8.client.ui;

import java.awt.Dimension;
import javax.swing.ToolTipManager;
import net.sealisland.gt8.client.parameter.TypeToolkit;
import net.sealisland.gt8.client.patch.PatchModelled;
import net.sealisland.gt8.client.patch.PatchViewSupport;
import net.sealisland.gt8.model.Parameter;
import net.sealisland.gt8.model.ParameterTable;
import net.sealisland.gt8.model.Patch;
import net.sealisland.gt8.model.Type;
import net.sealisland.swing.dial.DefaultDialRenderer;
import net.sealisland.swing.dial.Dial;
import net.sealisland.swing.layout.Orientation;
import net.sealisland.swing.util.DimensionToolkit;
import net.sealisland.swing.util.DisposableSupport;

public class ParameterDial extends Dial implements ParameterComponent, PatchModelled {

    private String label;

    private Parameter parameter;

    public ParameterDial() {
        super(Orientation.HORIZONTAL);
        setRenderer(new TypeDialRenderer());
        setModel(new ParameterBoundedRangeModel());
        ToolTipManager.sharedInstance().registerComponent(this);
        setPatch(null);
    }

    @Override
    public String getToolTipText() {
        return ParameterComponentSupport.getToolTip(parameter);
    }

    @Override
    public Dimension getMaximumSize() {
        return DimensionToolkit.infiniteWidth(super.getPreferredSize());
    }

    public void setId(String id) {
        ((ParameterBoundedRangeModel) getModel()).setParameterId(id);
        ParameterTable parameterTable = PatchViewSupport.getParameterTable(GT8UI.getInstance().getDevice().getTableMap().getTable("patch"), id);
        Type type = GT8UI.getInstance().getDevice().getTypeMap().get(parameterTable.getTypeId());
        ((TypeDialRenderer) getRenderer()).setType(type);
        setPrototypeValue(TypeToolkit.getPrototypeValue(type));
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setPatch(Patch patch) {
        ((ParameterBoundedRangeModel) getModel()).setPatch(patch);
        setEnabled(patch != null);
    }

    public void dispose() {
        DisposableSupport.dispose(getModel());
    }

    protected static class TypeDialRenderer extends DefaultDialRenderer {

        private Type type;

        public TypeDialRenderer() {
        }

        public void setType(Type type) {
            this.type = type;
        }

        protected void setValue(int value) {
            setText((type == null) ? String.valueOf(value) : type.stringTransform(value));
            setToolTipText(getText());
        }
    }
}
