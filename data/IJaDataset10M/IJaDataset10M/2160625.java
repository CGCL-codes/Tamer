package remote.protocol.clientserver;

import java.io.DataInput;
import java.io.DataOutput;
import remote.protocol.MsgIO;
import remote.protocol.MsgPayload;
import remote.protocol.MsgUint32;

public class MsgClientConfirm extends MsgIO {

    private MsgClientCommand command = new MsgClientCommand();

    private MsgResult result = new MsgResult();

    private MsgUint32 mote_id = new MsgUint32();

    private MsgPayload moteMsg = new MsgPayload();

    public MsgClientConfirm() {
        super();
    }

    public void read(DataInput is) throws Exception {
        command.read(is);
        result.read(is);
        mote_id.read(is);
        if (command.getValue() == MsgClientCommand.MSGCLIENTCOMMAND_MOTEMESSAGE && result.getValue() == MsgResult.SUCCESS) {
            moteMsg.read(is);
        }
    }

    public void write(DataOutput os) throws Exception {
        command.write(os);
        result.write(os);
        mote_id.write(os);
        if (command.getValue() == MsgClientCommand.MSGCLIENTCOMMAND_MOTEMESSAGE && result.getValue() == MsgResult.SUCCESS) {
            moteMsg.write(os);
        }
    }

    public MsgPayload getMoteMsg() {
        return moteMsg;
    }

    public MsgClientCommand getCommand() {
        return command;
    }

    public MsgUint32 getMote_id() {
        return mote_id;
    }

    public MsgResult getResult() {
        return result;
    }
}
