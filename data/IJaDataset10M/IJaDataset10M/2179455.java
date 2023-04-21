package org.j2eebuilder.component;

import java.util.ArrayList;
import java.util.Collection;
import org.j2eebuilder.*;
import org.j2eebuilder.util.LogManager;

/**
 *
 *
 * @author Sandeep Dixit
 * @version 1.3.1
 * @(#)InstanceAttributeBean.java 1.3.1 10/15/2002
 */
public class InstanceAttributeBean extends org.j2eebuilder.model.ManagedTransientObjectImpl {

    private static transient LogManager log = new LogManager(InstanceAttributeBean.class);

    private Integer instanceAttributeID;

    private Integer componentID;

    private ComponentBean componentVO;

    private Integer instanceID;

    private InstanceBean instanceVO;

    private String attributeName;

    /**
     * If this instanceAttribute is of ManagedTransientObject type
     * then primarykey instanceAttributes of the ManagedTransientObject
     * use this . instanceAttributeID
     *
     * In this case isInstance is set to true
     * i.e, child (pk - instance attributes) . instanceID = this . instanceAttributeID &&
     * this . isInstance = true
     */
    private String attributeValue;

    private String isInstance;

    private Collection<InstanceAttributeBean> colOfInstanceAttributeVO = new ArrayList();

    private Integer sequenceNumber;

    private InstanceAttributeTypeBean instanceAttributeTypeVO;

    public InstanceAttributeBean() {
    }

    public Integer getComponentID() {
        return componentID;
    }

    public void setComponentID(Integer componentID) {
        this.componentID = componentID;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }

    public Integer getInstanceID() {
        return instanceID;
    }

    public void setInstanceID(Integer instanceID) {
        this.instanceID = instanceID;
    }

    public ComponentBean getComponentVO() {
        return componentVO;
    }

    public void setComponentVO(ComponentBean componentVO) {
        this.componentVO = componentVO;
    }

    public String getName() {
        return String.valueOf(this.getInstanceID());
    }

    public String getDescription() {
        return this.getAttributeName() + ", " + this.getAttributeValue();
    }

    public InstanceBean getInstanceVO() {
        return instanceVO;
    }

    public void setInstanceVO(InstanceBean instanceVO) {
        this.instanceVO = instanceVO;
    }

    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    /**
     * is InstanceAttribute . attributeValue representing an Instance too?
     * in that case, this InstanceAttribute has child instanceAttributes (pk attributes
     * of the instance it is representing)
     */
    public boolean isInstance() {
        return Boolean.parseBoolean(this.isInstance);
    }

    public String getIsInstance() {
        return isInstance;
    }

    public void setIsInstance(String isInstance) {
        this.isInstance = isInstance;
    }

    public InstanceAttributeTypeBean getInstanceAttributeTypeVO() {
        return instanceAttributeTypeVO;
    }

    public void setInstanceAttributeTypeVO(InstanceAttributeTypeBean instanceAttributeTypeVO) {
        this.instanceAttributeTypeVO = instanceAttributeTypeVO;
    }

    /**
     * used by parent - child relationship
     */
    public Integer getInstanceAttributeID() {
        return instanceAttributeID;
    }

    public void setInstanceAttributeID(Integer instanceAttributeID) {
        this.instanceAttributeID = instanceAttributeID;
    }

    public Collection<InstanceAttributeBean> getColOfInstanceAttributeVO() {
        return colOfInstanceAttributeVO;
    }

    public void setColOfInstanceAttributeVO(Collection<InstanceAttributeBean> colOfInstanceAttributeVO) {
        this.colOfInstanceAttributeVO = colOfInstanceAttributeVO;
    }
}
