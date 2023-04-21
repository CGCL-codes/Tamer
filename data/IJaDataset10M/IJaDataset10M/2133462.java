package xxx;

import com.google.protobuf.Message;

public interface NetClient<M extends Message> {

    void send(Message message);
}
