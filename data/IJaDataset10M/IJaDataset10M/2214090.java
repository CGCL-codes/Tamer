package mercurius.fitness.client.rb.message;

import java.util.ArrayList;
import java.util.Arrays;
import mercurius.fitness.client.util.DataRowComparator;
import mercurius.fitness.client.util.Logger;
import net.pleso.framework.client.bl.exceptions.FrameworkRuntimeException;
import net.pleso.framework.client.dal.SelectParams;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class MessageDataSet {

    public MessageDataSet() {
    }

    private static ArrayList messages = new ArrayList();

    public void select(final SelectParams params, final AsyncCallback callback) {
        int offset = params.getOffset();
        int limit = params.getLimit();
        String orderByColumnName = params.getOrderByColumnName();
        boolean orderByDirection = params.getOrderByDirection();
        if (offset > messages.size() - 1) {
            offset = messages.size() - 1;
            if (messages.size() == 0) {
                offset = 0;
            }
        }
        if (offset + limit > messages.size() - 1) limit = messages.size() - offset;
        Message[] result = new Message[limit];
        for (int i = 0; i < limit; i++) {
            result[i] = (Message) messages.get(i + offset);
        }
        Arrays.sort(result, new DataRowComparator(orderByColumnName, orderByDirection));
        callback.onSuccess(result);
    }

    public void selectCount(AsyncCallback callback) {
        callback.onSuccess(new Integer(size()));
    }

    public static int size() {
        return messages.size();
    }

    public void add(final Message message, AsyncCallback callback) {
        add(message);
        callback.onSuccess(new Integer(1));
    }

    public static void add(Message message) {
        messages.add(message);
    }

    public void update(final Message message, AsyncCallback callback) {
    }

    public void get(final Integer id, final AsyncCallback callback) {
        try {
            for (int i = 0; i < messages.size(); i++) {
                if (((Message) messages.get(i)).getID() == id) {
                    callback.onSuccess(messages.get(i));
                    return;
                }
            }
            throw new IllegalArgumentException("No such message.");
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
            callback.onFailure(new FrameworkRuntimeException(e.getMessage(), e));
        }
    }

    public void delete(Integer id, AsyncCallback callback) {
    }
}
