package ow.routing.linearwalker.message;

import java.awt.Color;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import ow.messaging.Message;

public final class ReqSuccAndPredMessage extends Message {

    public static final String NAME = "REQ_SUCC_AND_PRD";

    public static final boolean TO_BE_REPORTED = true;

    public static final Color COLOR = null;

    public void encodeContents(ObjectOutputStream oos) {
    }

    public void decodeContents(ObjectInputStream ois) {
    }
}
