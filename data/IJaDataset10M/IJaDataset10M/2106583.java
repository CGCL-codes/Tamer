package com.netflexitysolutions.amazonws.ec2.internal.operations;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import com.amazonaws.ec2.doc._2009_04_04.BundleInstanceTaskType;
import com.amazonaws.ec2.doc._2009_04_04.DescribeBundleTasksInfoType;
import com.amazonaws.ec2.doc._2009_04_04.DescribeBundleTasksItemType;
import com.amazonaws.ec2.doc._2009_04_04.DescribeBundleTasksResponseType;
import com.amazonaws.ec2.doc._2009_04_04.DescribeBundleTasksType;
import com.netflexitysolutions.amazonws.ec2.BundleInstanceInfo;
import com.netflexitysolutions.amazonws.ec2.EC2;
import com.netflexitysolutions.amazonws.ec2.internal.ModelUtil;

public class DescribeBundleTasksOperation extends ObjectSetOperationExecutor<DescribeBundleTasksType, List<BundleInstanceInfo>> {

    public DescribeBundleTasksOperation(EC2 ec2, Set<String> bundleIds) {
        super(ec2, bundleIds);
    }

    @Override
    protected List<BundleInstanceInfo> call(DescribeBundleTasksType request) {
        request.setBundlesSet(createTasksElement());
        DescribeBundleTasksResponseType response = getEC2().getService().describeBundleTasks(request);
        List<BundleInstanceInfo> result = null;
        if (response.getBundleInstanceTasksSet() != null) {
            result = new ArrayList<BundleInstanceInfo>();
            List<BundleInstanceTaskType> list = response.getBundleInstanceTasksSet().getItem();
            for (BundleInstanceTaskType item : list) {
                result.add(ModelUtil.toDomainType(item));
            }
        }
        return result;
    }

    private DescribeBundleTasksInfoType createTasksElement() {
        DescribeBundleTasksInfoType type = new DescribeBundleTasksInfoType();
        List<DescribeBundleTasksItemType> list = type.getItem();
        for (String bundleId : objectIds) {
            DescribeBundleTasksItemType item = new DescribeBundleTasksItemType();
            item.setBundleId(bundleId);
            list.add(item);
        }
        return type;
    }
}
