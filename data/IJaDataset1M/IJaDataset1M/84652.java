package shellkk.qiq.jdm.modeldetail.naivebayes;

import java.util.ArrayList;
import java.util.List;
import javax.datamining.data.AttributeType;

public class AttributeEstimator {

    protected Long id;

    protected Integer hversion;

    protected TargetEstimator targetEstimator;

    protected String attrName;

    protected String attrTypeName;

    protected Double meanValue;

    protected Double variance;

    protected double weight;

    protected List<AttributeEstimatorItem> items = new ArrayList();

    public AttributeEstimator getCopy() {
        AttributeEstimator copy = new AttributeEstimator();
        copy.setAttrName(attrName);
        copy.setAttrTypeName(attrTypeName);
        copy.setMeanValue(meanValue);
        copy.setVariance(variance);
        copy.setWeight(weight);
        for (AttributeEstimatorItem item : items) {
            copy.getItems().add(item.getCopy());
        }
        return copy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getHversion() {
        return hversion;
    }

    public void setHversion(Integer hversion) {
        this.hversion = hversion;
    }

    public TargetEstimator getTargetEstimator() {
        return targetEstimator;
    }

    public void setTargetEstimator(TargetEstimator targetEstimator) {
        this.targetEstimator = targetEstimator;
    }

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    public String getAttrTypeName() {
        return attrTypeName;
    }

    public void setAttrTypeName(String attrTypeName) {
        this.attrTypeName = attrTypeName;
    }

    public Double getMeanValue() {
        return meanValue;
    }

    public void setMeanValue(Double meanValue) {
        this.meanValue = meanValue;
    }

    public Double getVariance() {
        return variance;
    }

    public void setVariance(Double variance) {
        this.variance = variance;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public List<AttributeEstimatorItem> getItems() {
        return items;
    }

    public void setItems(List<AttributeEstimatorItem> items) {
        this.items = items;
    }

    public boolean isCategorical() {
        return AttributeType.categorical.name().equals(attrTypeName);
    }

    public AttributeEstimatorItem getItem(Object obj) {
        String value = obj == null ? null : obj.toString();
        for (AttributeEstimatorItem item : items) {
            if (value == null ? item.getValue() == null : value.equals(item.getValue())) {
                return item;
            }
        }
        return null;
    }
}
