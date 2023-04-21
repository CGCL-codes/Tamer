package com.netflexitysolutions.amazonws.ec2.internal.operations;

import com.amazonaws.ec2.doc._2009_04_04.CreateSnapshotResponseType;
import com.amazonaws.ec2.doc._2009_04_04.CreateSnapshotType;
import com.netflexitysolutions.amazonws.ec2.EC2;
import com.netflexitysolutions.amazonws.ec2.Snapshot;
import com.netflexitysolutions.amazonws.ec2.SnapshotStatus;
import com.netflexitysolutions.amazonws.ec2.internal.ModelUtil;

public class CreateSnapshotOperation extends SingleObjectOperationExecutor<CreateSnapshotType, Snapshot> {

    public CreateSnapshotOperation(EC2 ec2, String volumeId) {
        super(ec2, volumeId);
    }

    @Override
    protected Snapshot call(CreateSnapshotType request) {
        request.setVolumeId(objectId);
        CreateSnapshotResponseType response = getEC2().getService().createSnapshot(request);
        Snapshot result = new Snapshot(response.getSnapshotId(), response.getVolumeId(), SnapshotStatus.snapshotStatusForName(response.getStatus()), ModelUtil.toDomainType(response.getStartTime()), response.getProgress());
        return result;
    }
}
