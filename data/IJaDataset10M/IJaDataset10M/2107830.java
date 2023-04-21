package org.j2eebuilder.component;

import com.ohioedge.j2ee.api.attribute.DataConstraintBean;
import com.ohioedge.j2ee.api.attribute.DataTypeBean;
import org.j2eebuilder.util.LogManager;

/**
 * @(#)InstanceAttributeTypeBean.java	1.3.1 10/15/2002
 * InstanceAttributeTypeBean is a java bean with the main function of facilitating
 * communication between JSPs and ActivityTypeDataInfo EJB
 * @version 1.3.1
 *
 * Used when custom field is a not a managedComponent, for e.g., Integer, String
 * For managedComponent dataSize is irrelevant
 */
public class InstanceAttributeTypeBean extends org.j2eebuilder.model.ManagedTransientObjectImpl {

    private static transient LogManager log = new LogManager(InstanceAttributeTypeBean.class);

    private Integer componentID;

    private Integer instanceID;

    private String name;

    private String description;

    private Integer dataTypeID;

    private Integer dataConstraintID;

    private Integer dataSize;

    private DataTypeBean dataTypeVO;

    private DataConstraintBean dataConstraintVO;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDataTypeID() {
        return this.dataTypeID;
    }

    public void setDataTypeID(Integer dataTypeID) {
        this.dataTypeID = dataTypeID;
    }

    public DataTypeBean getDataTypeVO() {
        return this.dataTypeVO;
    }

    public void setDataTypeVO(DataTypeBean dataTypeVO) {
        this.dataTypeVO = dataTypeVO;
    }

    public Integer getDataConstraintID() {
        return this.dataConstraintID;
    }

    public void setDataConstraintID(Integer dataConstraintID) {
        this.dataConstraintID = dataConstraintID;
    }

    public DataConstraintBean getDataConstraintVO() {
        return this.dataConstraintVO;
    }

    public void setDataConstraintVO(DataConstraintBean dataConstraintVO) {
        this.dataConstraintVO = dataConstraintVO;
    }

    public Integer getDataSize() {
        return this.dataSize;
    }

    public void setDataSize(Integer dataSize) {
        this.dataSize = dataSize;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public InstanceAttributeTypeBean() {
    }

    public Integer getComponentID() {
        return componentID;
    }

    public void setComponentID(Integer componentID) {
        this.componentID = componentID;
    }

    public Integer getInstanceID() {
        return instanceID;
    }

    public void setInstanceID(Integer instanceID) {
        this.instanceID = instanceID;
    }
}
