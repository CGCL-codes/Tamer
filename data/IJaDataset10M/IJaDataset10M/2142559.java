package ow.routing.pastry.message;

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import ow.messaging.Message;
import ow.routing.plaxton.RoutingTableRow;

public final class RepRoutingTableRowMessage extends Message {

    public static final String NAME = "REP_ROUTING_TABLE_ROW";

    public static final boolean TO_BE_REPORTED = true;

    public static final Color COLOR = null;

    public RoutingTableRow row;

    public RepRoutingTableRowMessage() {
        super();
    }

    public RepRoutingTableRowMessage(RoutingTableRow row) {
        this.row = row;
    }

    public void encodeContents(ObjectOutputStream oos) throws IOException {
        oos.writeObject(this.row);
    }

    public void decodeContents(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        this.row = (RoutingTableRow) ois.readObject();
    }
}
