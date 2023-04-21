package com.netflexitysolutions.amazonws.ec2.internal.operations;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import com.amazonaws.ec2.doc._2009_04_04.MonitorInstancesResponseSetItemType;
import com.amazonaws.ec2.doc._2009_04_04.MonitorInstancesResponseType;
import com.amazonaws.ec2.doc._2009_04_04.MonitorInstancesSetItemType;
import com.amazonaws.ec2.doc._2009_04_04.MonitorInstancesSetType;
import com.amazonaws.ec2.doc._2009_04_04.MonitorInstancesType;
import com.netflexitysolutions.amazonws.ec2.EC2;
import com.netflexitysolutions.amazonws.ec2.InstanceMonitoringInfo;
import com.netflexitysolutions.amazonws.ec2.InstanceMonitoringState;

public class MonitorInstancesOperation extends ObjectSetOperationExecutor<MonitorInstancesType, List<InstanceMonitoringInfo>> {

    public MonitorInstancesOperation(EC2 ec2, Set<String> instanceIds) {
        super(ec2, instanceIds);
    }

    @Override
    protected List<InstanceMonitoringInfo> call(MonitorInstancesType request) {
        request.setInstancesSet(createInstancesSetElement());
        MonitorInstancesResponseType response = getEC2().getService().monitorInstances(request);
        List<InstanceMonitoringInfo> result = new ArrayList<InstanceMonitoringInfo>();
        if (response.getInstancesSet() != null) {
            List<MonitorInstancesResponseSetItemType> list = response.getInstancesSet().getItem();
            for (MonitorInstancesResponseSetItemType item : list) {
                result.add(new InstanceMonitoringInfo(item.getInstanceId(), InstanceMonitoringState.instanceMonitoringStateForName(item.getMonitoring().getState())));
            }
        }
        return result;
    }

    private MonitorInstancesSetType createInstancesSetElement() {
        MonitorInstancesSetType type = new MonitorInstancesSetType();
        List<MonitorInstancesSetItemType> list = type.getItem();
        for (String instanceId : objectIds) {
            MonitorInstancesSetItemType item = new MonitorInstancesSetItemType();
            item.setInstanceId(instanceId);
            list.add(item);
        }
        return type;
    }
}
