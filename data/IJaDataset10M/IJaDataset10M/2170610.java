package jbomberman.server.command;

import jbomberman.server.GameManager;
import jbomberman.server.GameServer;
import jbomberman.server.SendException;
import jbomberman.server.ServerException;
import jbomberman.util.ByteArrayReader;
import jbomberman.util.ByteArrayWriter;
import jbomberman.util.Log;
import java.io.IOException;

public class CECUserReady extends ClientEventCommand {

    public static final byte SID = 109;

    protected String game_Name_;

    protected boolean is_User_Ready_;

    protected byte player_ID_;

    public CECUserReady(String game_Name, boolean is_User_Ready, byte player_ID) {
        super();
        game_Name_ = game_Name;
        is_User_Ready_ = is_User_Ready;
        player_ID_ = player_ID;
    }

    public CECUserReady(ByteArrayReader bar) throws ServerException {
        super(bar);
        try {
            game_Name_ = bar.readString();
            is_User_Ready_ = bar.readBoolean();
            player_ID_ = bar.readByte();
        } catch (IOException e) {
            throw new ServerException();
        }
    }

    public byte[] serialize() throws IOException {
        ByteArrayWriter baw = new ByteArrayWriter();
        baw.writeString(game_Name_);
        baw.writeBoolean(is_User_Ready_);
        baw.writeByte(player_ID_);
        return baw.getBytes();
    }

    public void action(GameServer game_Server) {
        GameManager game_Manager = null;
        try {
            game_Manager = game_Server.getGameManager(game_Name_);
        } catch (ServerException e) {
            EventCommand cmd = new SECError(host_ID_, SECError.INTERNAL_USER_READY_ERROR);
            try {
                game_Server.sendData(cmd);
                return;
            } catch (SendException e1) {
                Log.error("Error on sending event 'SECError' in class 'CECUserReady'.\n");
                return;
            }
        }
        game_Manager.userReady(player_ID_, is_User_Ready_);
    }

    protected byte getUniqueSID() {
        return SID;
    }
}
