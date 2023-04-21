package com.peterhi.servlet.nio.handlers;

import java.net.SocketAddress;
import org.xsocket.stream.INonBlockingConnection;
import com.peterhi.net.Protocol;
import com.peterhi.net.msg.RemoveElement;
import com.peterhi.servlet.Kernel;
import com.peterhi.servlet.Store;
import com.peterhi.servlet.classrooms.RuntimeClassroom;
import com.peterhi.servlet.nio.DatagramComponent;
import com.peterhi.servlet.nio.DatagramHandler;
import com.peterhi.servlet.nio.NioComponent;
import com.peterhi.servlet.persist.PrevaylerComponent;
import com.peterhi.servlet.persist.tx.RemoveElementTx;

public class RemoveElementHandler implements DatagramHandler {

    public void handle(Kernel kernel, DatagramComponent comp, SocketAddress sa, byte[] data) {
        try {
            final RemoveElement e = new RemoveElement();
            e.deserialize(data, Protocol.RUDP_CURSOR);
            final PrevaylerComponent pComp = kernel.getComponent(PrevaylerComponent.class);
            NioComponent nioComp = (NioComponent) kernel.getComponent(NioComponent.class);
            INonBlockingConnection nioConn = nioComp.getConnection(e.getID());
            if (nioConn == null) return;
            Store store = (Store) nioConn.getAttachment();
            if (store.MEMBER == null) return;
            final RuntimeClassroom cls = store.MEMBER.getRuntimeClassroom();
            RemoveElementTx tx = new RemoveElementTx(cls.getName(), e.getName());
            pComp.getPrevayler().execute(tx);
            comp.multicastReliable(store.MEMBER, e);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
