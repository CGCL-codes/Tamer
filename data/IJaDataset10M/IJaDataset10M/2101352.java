package de.objectcode.time4u.client.ui.dnd;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.TransferData;
import de.objectcode.time4u.client.store.api.RepositoryFactory;
import de.objectcode.time4u.server.api.data.WorkItem;

public class WorkItemTransfer extends ByteArrayTransfer {

    private static WorkItemTransfer INSTANCE = new WorkItemTransfer();

    private static final String TYPE_NAME = "time4u-workitem-transfer-format";

    private static final int TYPEID = registerType(TYPE_NAME);

    public static WorkItemTransfer getInstance() {
        return INSTANCE;
    }

    private WorkItemTransfer() {
    }

    @Override
    protected int[] getTypeIds() {
        return new int[] { TYPEID };
    }

    @Override
    protected String[] getTypeNames() {
        return new String[] { TYPE_NAME };
    }

    @Override
    protected void javaToNative(final Object object, final TransferData transferData) {
        try {
            @SuppressWarnings("unchecked") final List<WorkItem> workItems = (List<WorkItem>) object;
            String temp = "";
            for (final WorkItem workItem : workItems) {
                temp += workItem.getId() + ",";
            }
            final byte[] bytes = temp.getBytes("UTF-8");
            if (bytes != null) {
                super.javaToNative(bytes, transferData);
            }
        } catch (final Exception e) {
        }
    }

    @Override
    protected Object nativeToJava(final TransferData transferData) {
        final byte[] bytes = (byte[]) super.nativeToJava(transferData);
        return fromByteArray(bytes);
    }

    protected List<WorkItem> fromByteArray(final byte[] bytes) {
        final List<WorkItem> workItems = new ArrayList<WorkItem>();
        try {
            WorkItem workitem = null;
            final String[] workItemIds = new String(bytes, "UTF-8").split(",");
            for (final String workItemId : workItemIds) {
                workitem = RepositoryFactory.getRepository().getWorkItemRepository().getWorkItem(workItemId);
                workItems.add(workitem);
            }
            return workItems;
        } catch (final Exception e) {
            return null;
        }
    }
}
