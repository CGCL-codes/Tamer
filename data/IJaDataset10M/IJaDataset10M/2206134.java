package com.clanwts.bncs.codec.standard.messages;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import org.jboss.netty.buffer.ChannelBuffer;
import com.clanwts.bncs.codec.AbstractMessage;

public final class AccountLogonServer extends AbstractMessage {

    /**
   * 
   */
    private static final long serialVersionUID = -7591974933786558965L;

    public enum Status {

        LOGON_ACCEPTED(0x00, "logon accepted, requires proof"), NO_ACCOUNT(0x01, "account does not exist"), UPGRADE_ACCOUNT(0x05, "account requires an upgrade"), UNKNOWN_ERROR(-1, "an unknown error occurred");

        private static final Map<Integer, Status> lookup = new HashMap<Integer, Status>();

        static {
            for (Status s : EnumSet.allOf(Status.class)) {
                lookup.put(s.getCode(), s);
            }
        }

        public static Status forCode(int code) {
            Status s = lookup.get(code);
            if (s == null) {
                s = UNKNOWN_ERROR;
            }
            return s;
        }

        private int code;

        private String desc;

        private Status(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public String getDescription() {
            return desc;
        }
    }

    public static final int id = 0x53;

    public Status status;

    public byte[] salt = new byte[32];

    public byte[] serverKey = new byte[32];

    @Override
    public int id() {
        return id;
    }

    @Override
    public void readFrom(ChannelBuffer b) {
        status = Status.forCode(b.readInt());
        b.readBytes(salt);
        b.readBytes(serverKey);
    }

    @Override
    public int size() {
        return 68;
    }

    @Override
    public void writeTo(ChannelBuffer b) {
        b.writeInt(status.getCode());
        b.writeBytes(salt, 0, 32);
        b.writeBytes(serverKey, 0, 32);
    }
}
