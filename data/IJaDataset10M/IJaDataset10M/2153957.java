package com.taobao.remote.common.admin;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import com.taobao.remote.common.core.ParserUtils;

/**
 * @version 2007-3-8
 * @author xalinx at gmail dot com
 * 
 */
public class ConnectionStatusResponseParser extends AdminResponseBodyParser<ConnectionStatusResponse> {

    private static final ConnectionStatus[] EMPTY_CONN_STATUS = new ConnectionStatus[0];

    @Override
    protected void writeOther(ConnectionStatusResponse value, DataOutput out) throws IOException {
        writeConns(value.getClients(), out);
        writeConns(value.getAdmins(), out);
    }

    public ConnectionStatusResponse read(DataInput in) throws IOException {
        ConnectionStatusResponse csr = new ConnectionStatusResponse();
        csr.setClients(readConns(in));
        csr.setAdmins(readConns(in));
        return csr;
    }

    private void writeConns(ConnectionStatus[] conns, DataOutput out) throws IOException {
        ParserUtils.writeArrayLength(conns, out);
        for (int i = 0; i < conns.length; i++) {
            ParserUtils.writeString(out, conns[i].getAddress());
        }
    }

    private ConnectionStatus[] readConns(DataInput in) throws IOException {
        ConnectionStatus[] css = null;
        int connLen = in.readInt();
        if (connLen == -1) {
        } else if (connLen == 0) {
            css = EMPTY_CONN_STATUS;
        } else {
            css = new ConnectionStatus[connLen];
            for (int i = 0; i < css.length; i++) {
                String address = ParserUtils.readString(in);
                ConnectionStatus status = new ConnectionStatus();
                status.setAddress(address);
                css[i] = status;
            }
        }
        return css;
    }
}
