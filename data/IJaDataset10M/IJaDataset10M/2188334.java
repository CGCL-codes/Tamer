package com.peterhi.net.msg;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import com.peterhi.beans.ChannelBean;
import com.peterhi.beans.OpenStatus;
import com.peterhi.beans.Role;
import com.peterhi.io.IO;

public class QChnlRsp extends AbstractMSG {

    private ChannelBean[] beans;

    public ChannelBean[] getBeans() {
        return beans;
    }

    public void setBeans(ChannelBean[] beans) {
        this.beans = beans;
    }

    @Override
    protected void readData(DataInputStream in) throws IOException {
        int len = in.readInt();
        if (len <= 0) {
            return;
        }
        int[] hashCodes = IO.readIntArray(in);
        String[] names = IO.readStringArray(in);
        int[] opens = IO.readIntArray(in);
        int[] roles = IO.readIntArray(in);
        beans = new ChannelBean[len];
        for (int i = 0; i < len; i++) {
            beans[i] = new ChannelBean();
            beans[i].setHashCode(hashCodes[i]);
            beans[i].setName(names[i]);
            beans[i].setOpenStatus(OpenStatus.toOpenStatus(opens[i]));
            beans[i].setRole(Role.toRole(roles[i]));
        }
    }

    @Override
    protected void writeData(DataOutputStream out) throws IOException {
        if (beans == null || beans.length <= 0) {
            out.writeInt(-1);
            return;
        }
        int len = beans.length;
        int[] hashCodes = new int[len];
        String[] names = new String[len];
        int[] opens = new int[len];
        int[] roles = new int[len];
        for (int i = 0; i < len; i++) {
            hashCodes[i] = beans[i].getHashCode();
            names[i] = beans[i].getName();
            opens[i] = beans[i].getOpenStatus().ordinal();
            roles[i] = beans[i].getRole().ordinal();
        }
        out.writeInt(len);
        IO.writeIntArray(out, hashCodes);
        IO.writeStringArray(out, names);
        IO.writeIntArray(out, opens);
        IO.writeIntArray(out, roles);
    }

    public int getID() {
        return RSP_QCHNL;
    }
}
