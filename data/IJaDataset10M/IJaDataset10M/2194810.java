package flex.data.messages;

import flex.messaging.io.PropertyProxy;
import flex.messaging.messages.AsyncMessage;
import java.util.Map;

public class DataMessage extends AsyncMessage {

    public DataMessage() {
        timestamp = System.currentTimeMillis();
    }

    public Map getIdentity() {
        return identity;
    }

    public void setIdentity(Map identity) {
        this.identity = identity;
    }

    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }

    protected String toStringFields(int indentLevel) {
        String s = super.toStringFields(indentLevel);
        String sep = getFieldSeparator(indentLevel);
        s = sep + "operation = " + operationToString(operation) + sep + "id = " + identity + s;
        return s;
    }

    public Object unwrapBody() {
        Object body = super.getBody();
        if (body instanceof PropertyProxy) return ((PropertyProxy) body).getDefaultInstance(); else return body;
    }

    public static String operationToString(int operation) {
        if (operation < 0 || operation > operationNames.length) return "invalid"; else return operationNames[operation];
    }

    public String logCategory() {
        return "Message.Data." + operationToString(operation);
    }

    private static final long serialVersionUID = -233562911865716601L;

    public static final String LOG_CATEGORY = "Message.Data";

    public static final int CREATE_OPERATION = 0;

    public static final int FILL_OPERATION = 1;

    public static final int GET_OPERATION = 2;

    public static final int UPDATE_OPERATION = 3;

    public static final int DELETE_OPERATION = 4;

    public static final int BATCHED_OPERATION = 5;

    public static final int MULTI_BATCH_OPERATION = 6;

    public static final int TRANSACTED_OPERATION = 7;

    public static final int PAGE_OPERATION = 8;

    public static final int COUNT_OPERATION = 9;

    public static final int GET_OR_CREATE_OPERATION = 10;

    public static final int CREATE_AND_SEQUENCE_OPERATION = 11;

    public static final int GET_SEQUENCE_ID_OPERATION = 12;

    public static final int FILLIDS_OPERATION = 15;

    public static final int REFRESH_FILL_OPERATION = 16;

    public static final int UPDATE_COLLECTION_OPERATION = 17;

    public static final int RELEASE_COLLECTION_OPERATION = 18;

    public static final int RELEASE_ITEM_OPERATION = 19;

    public static final int PAGE_ITEMS_OPERATION = 20;

    public static final int UPDATE_BODY_CHANGES = 0;

    public static final int UPDATE_BODY_PREV = 1;

    public static final int UPDATE_BODY_NEW = 2;

    public static final String DYNAMIC_SIZING_HEADER = "dynamicSizing";

    static final String operationNames[] = { "create", "fill", "get", "update", "delete", "batched", "multi_batch", "transacted", "page", "count", "get_or_create", "create_and_sequence", "get_sequence_id", "unused13", "unused14", "fillids", "refresh_fill", "update_collection", "release_collection", "release_item", "page_items" };

    private Map identity;

    private int operation;
}
