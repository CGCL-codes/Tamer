package com.xerox.amazonws.ec2;

import java.util.ArrayList;
import java.util.List;
import com.xerox.amazonws.typica.jaxb.DescribeInstanceAttributeResponse;
import com.xerox.amazonws.typica.jaxb.NullableAttributeValueType;
import com.xerox.amazonws.typica.jaxb.NullableAttributeBooleanValueType;
import com.xerox.amazonws.typica.jaxb.InstanceBlockDeviceMappingResponseType;
import com.xerox.amazonws.typica.jaxb.InstanceBlockDeviceMappingResponseItemType;

/**
 * The results of a call to describe snapshot attributes. 
 */
public class DescribeInstanceAttributeResult {

    private String requestId;

    private String instanceId;

    private InstanceType instanceType;

    private String kernelId;

    private String ramdiskId;

    private String userData;

    private boolean disableApiTermination;

    private String instanceInitiatedShutdownBehavior;

    private String rootDeviceName;

    private List<BlockDeviceMapping> blockDeviceMappings;

    public DescribeInstanceAttributeResult(String requestId, String instanceId) {
        this.requestId = requestId;
        this.instanceId = instanceId;
        blockDeviceMappings = new ArrayList<BlockDeviceMapping>();
    }

    DescribeInstanceAttributeResult(DescribeInstanceAttributeResponse response) {
        requestId = response.getRequestId();
        instanceId = response.getInstanceId();
        NullableAttributeValueType val = response.getInstanceType();
        if (val != null) instanceType = InstanceType.getTypeFromString(val.getValue());
        val = response.getKernel();
        if (val != null) kernelId = val.getValue();
        val = response.getRamdisk();
        if (val != null) ramdiskId = val.getValue();
        val = response.getUserData();
        if (val != null) userData = val.getValue();
        NullableAttributeBooleanValueType bool = response.getDisableApiTermination();
        if (bool != null) disableApiTermination = bool.isValue();
        val = response.getInstanceInitiatedShutdownBehavior();
        if (val != null) instanceInitiatedShutdownBehavior = val.getValue();
        val = response.getRootDeviceName();
        if (val != null) rootDeviceName = val.getValue();
        blockDeviceMappings = new ArrayList<BlockDeviceMapping>();
        InstanceBlockDeviceMappingResponseType bdmSet = response.getBlockDeviceMapping();
        if (bdmSet != null) {
            for (InstanceBlockDeviceMappingResponseItemType mapping : bdmSet.getItems()) {
            }
        }
    }

    public String getRequestId() {
        return requestId;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public InstanceType getInstanceType() {
        return this.instanceType;
    }

    public String getKernelId() {
        return kernelId;
    }

    public String getRamdiskId() {
        return ramdiskId;
    }

    public String getUserData() {
        return userData;
    }

    public boolean getDisableApiTermination() {
        return disableApiTermination;
    }

    public String getInstanceInitiatedShutdownBehavior() {
        return instanceInitiatedShutdownBehavior;
    }

    public String getRootDeviceName() {
        return rootDeviceName;
    }

    public List<BlockDeviceMapping> getBlockDeviceMappings() {
        return blockDeviceMappings;
    }
}
