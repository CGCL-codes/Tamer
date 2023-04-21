package model;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.fh.auge.chart.model.Data;

public class Block extends ChartObject {

    private Chart chart;

    public Chart getChart() {
        return chart;
    }

    public Block(Chart chart) {
        super();
        this.chart = chart;
    }

    private String name;

    private List<Indicator> dataSets = new ArrayList<Indicator>();

    private int preferredHeight = -1;

    private int margin = 25;

    public int getMargin() {
        return margin;
    }

    public int getPreferredHeight() {
        return preferredHeight;
    }

    public void setPreferredHeight(int preferredHeight) {
        this.preferredHeight = preferredHeight;
        propertyChangeSupport.firePropertyChange("preferredHeight", null, preferredHeight);
    }

    public List<Indicator> getDataSets() {
        return dataSets;
    }

    public String getName() {
        StringBuffer sb = new StringBuffer();
        String and = "";
        for (Indicator indicator : dataSets) {
            sb.append(and);
            sb.append(indicator.getName());
            and = " / ";
        }
        return sb.toString();
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean addChild(Indicator dataSet) {
        if (dataSet != null && dataSets.add(dataSet)) {
            propertyChangeSupport.firePropertyChange("indicators", null, dataSet);
            return true;
        }
        return false;
    }

    public boolean removeChild(Indicator dataSet) {
        if (dataSet != null && dataSets.remove(dataSet)) {
            propertyChangeSupport.firePropertyChange("indicators", null, dataSet);
            return true;
        }
        return false;
    }

    @Override
    public ValueRange calculateValueRange(DateRange dateRange) {
        ValueRange verticalRange;
        if (input != null) {
            verticalRange = null;
            for (ChartObject dataSet : dataSets) {
                ValueRange range = dataSet.calculateValueRange(dateRange);
                if (range != null) {
                    verticalRange = (verticalRange == null) ? range : verticalRange.merge(range);
                }
            }
        } else {
            verticalRange = null;
        }
        return verticalRange;
    }

    @Override
    protected void inputChanged(Data oldInput, Data newInput) {
        for (Indicator dataSet : dataSets) {
            dataSet.setInput(newInput);
        }
    }

    @Override
    protected IPropertySource createPropertySource() {
        return new BlockPropertySource();
    }

    public class BlockPropertySource implements IPropertySource {

        private IPropertyDescriptor[] propertyDescriptors;

        public Object getEditableValue() {
            return null;
        }

        public Object getPropertyValue(Object id) {
            if (id.equals("name")) {
                return getName();
            }
            if (id.equals("height")) {
                return "" + getPreferredHeight();
            }
            return id;
        }

        public boolean isPropertySet(Object id) {
            return false;
        }

        public void resetPropertyValue(Object id) {
        }

        public void setPropertyValue(Object id, Object value) {
            if (id.equals("name")) {
                setName((String) value);
            }
            if (id.equals("height")) {
                setPreferredHeight(Integer.parseInt((String) value));
            }
        }

        public IPropertyDescriptor[] getPropertyDescriptors() {
            if (propertyDescriptors == null) {
                List<IPropertyDescriptor> descs = new ArrayList<IPropertyDescriptor>();
                PropertyDescriptor layerDescriptor = new PropertyDescriptor("name", "Name");
                layerDescriptor.setCategory("Info");
                descs.add(layerDescriptor);
                layerDescriptor = new TextPropertyDescriptor("height", "Height");
                layerDescriptor.setCategory("Info");
                descs.add(layerDescriptor);
                propertyDescriptors = descs.toArray(new IPropertyDescriptor[0]);
            }
            return propertyDescriptors;
        }
    }

    public void childChanged() {
        propertyChangeSupport.firePropertyChange("children", null, null);
    }

    public void setMargin(int margin) {
        this.margin = margin;
    }
}
