package org.monet.docservice.docprocessor.operations;

import org.monet.docservice.docprocessor.worker.WorkQueueItem;

public interface Operation {

    public static final String OPERATIONS_FACTORY = "OperationsFactory";

    public static final int OPERATION_UPDATE_DOCUMENT = 1;

    public static final int OPERATION_CONSOLIDATE_DOCUMENT = 2;

    public static final int OPERATION_UPLOAD_PREVIEW_DOCUMENT = 3;

    public static final int OPERATION_EXTRACT_ATTACHMENT = 4;

    void setTarget(WorkQueueItem item);

    void execute();
}
