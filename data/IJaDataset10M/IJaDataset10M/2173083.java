package edu.washington.mysms.server;

import java.sql.Timestamp;
import edu.washington.mysms.Message;
import edu.washington.mysms.SendRetrieveDB;
import edu.washington.mysms.coding.ResultTable;
import edu.washington.mysms.security.SqlAccount;

public class OutboundAckMessage extends Message<String> {

    private SqlAccount adminAccount = null;

    public OutboundAckMessage(InboundQueryMessage query, boolean encrypt) {
        this(query, null, encrypt);
    }

    public OutboundAckMessage(InboundQueryMessage query, ResultTable table, boolean encrypt) {
        super(query.getAddress(), new Timestamp(System.currentTimeMillis()), query.getStream(), query.getMessageId(), Type.ACKNOWLEDGMENT, encrypt, "", query.getEncode(), query.getModule(), query.getCharLimit(), "E");
    }

    @Override
    protected String convertBytesToObject(byte[] bytes) {
        return null;
    }

    @Override
    protected byte[] convertObjectToBytes(String object) {
        return null;
    }

    public void setAdminAccount(SqlAccount adminAccount) {
        this.adminAccount = adminAccount;
    }

    @Override
    public boolean isRunReady() {
        return adminAccount != null;
    }

    @Override
    public void run() {
        String contents = "Acknowledged";
        String msg = "";
        if (this.msgEncode.equalsIgnoreCase("E")) msg += (char) Message.Type.ACKNOWLEDGMENT.ordinal();
        msg += contents;
        try {
            SendRetrieveDB con = new SendRetrieveDB(adminAccount);
            con.SendMessage(msg, this);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }
}
