package shellkk.qiq.jdm.data;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.datamining.JDMException;
import javax.datamining.SortOrder;
import javax.datamining.data.AttributeDataType;
import javax.datamining.data.AttributeType;
import javax.datamining.data.ModelSignature;
import javax.datamining.data.SignatureAttribute;

public class ModelSignatureImpl implements ModelSignature {

    protected Long id;

    protected Integer hversion;

    protected Map<String, AbstractSignatureAttribute> signatureAttributes = new HashMap();

    private LogicalDataImpl logicalData;

    public ModelSignatureImpl getCopy() {
        ModelSignatureImpl copy = new ModelSignatureImpl();
        for (String key : signatureAttributes.keySet()) {
            AbstractSignatureAttribute attr = signatureAttributes.get(key);
            AbstractSignatureAttribute attrCopy = attr.getCopy();
            attrCopy.setModelSignature(copy);
            copy.getSignatureAttributes().put(attrCopy.getName(), attrCopy);
        }
        return copy;
    }

    public void copyProps(LogicalDataImpl data) {
        for (String key : signatureAttributes.keySet()) {
            AbstractSignatureAttribute sigAttr = signatureAttributes.get(key);
            LogicalAttributeImpl logicAttr = data.getLogicalAttributes().get(key);
            AttributeType attrType = logicAttr.getAttributeType();
            sigAttr.setAttributeType(attrType);
            if (AttributeType.categorical.equals(attrType)) {
                sigAttr.setDataType(AttributeDataType.stringType);
            } else if (AttributeType.numerical.equals(attrType)) {
                sigAttr.setDataType(AttributeDataType.doubleType);
            } else if (AttributeType.ordinal.equals(attrType)) {
                sigAttr.setDataType(AttributeDataType.integerType);
            } else if (AttributeTypeEx.text.equals(attrType)) {
                sigAttr.setDataType(AttributeDataTypeEx.textType);
            } else if (AttributeType.notSpecified.equals(attrType)) {
                sigAttr.setDataType(AttributeDataType.unknownType);
            } else {
            }
            sigAttr.setDescription(logicAttr.getDescription());
            CategorySetImpl cs = (CategorySetImpl) logicAttr.getCategorySet();
            if (cs != null) {
                sigAttr.setCategorySet(cs.getCopy());
            }
        }
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

    public Map<String, AbstractSignatureAttribute> getSignatureAttributes() {
        return signatureAttributes;
    }

    public void setSignatureAttributes(Map<String, AbstractSignatureAttribute> signatureAttributes) {
        this.signatureAttributes = signatureAttributes;
    }

    public SignatureAttribute getAttribute(String attributeName) throws JDMException {
        return signatureAttributes.get(attributeName);
    }

    public Collection getAttributes() throws JDMException {
        return signatureAttributes.values();
    }

    public Collection getAttributesByRank(SortOrder ordering) throws JDMException {
        return null;
    }

    public LogicalDataImpl getLogicalData() throws JDMException {
        if (logicalData == null) {
            logicalData = LogicalDataImpl.createWith(this);
        }
        return logicalData;
    }
}
