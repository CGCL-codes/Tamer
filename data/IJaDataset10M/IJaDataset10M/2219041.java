package ow.routing.impl.message;

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import ow.messaging.Message;

public final class ReqNeighborsMessage extends Message {

    public static final String NAME = "REQ_NEIGHBORS";

    public static final boolean TO_BE_REPORTED = false;

    public static final Color COLOR = null;

    public int num;

    public ReqNeighborsMessage() {
        super();
    }

    public ReqNeighborsMessage(int num) {
        this.num = num;
    }

    public void encodeContents(ObjectOutputStream oos) throws IOException {
        oos.writeInt(this.num);
    }

    public void decodeContents(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        this.num = ois.readInt();
    }
}
