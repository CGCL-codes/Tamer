package com.dukesoftware.viewlon3.net.request.server;

import java.nio.channels.SocketChannel;
import java.util.Iterator;
import com.dukesoftware.viewlon3.data.common.DataManagerCore;
import com.dukesoftware.viewlon3.data.internal.RealObject;
import com.dukesoftware.viewlon3.net.request.template.ComRequest;

public class ReRequestGetObjectInitializeData extends ComRequest {

    private final DataManagerCore array;

    public ReRequestGetObjectInitializeData(String name, SocketChannel socketChannel, DataManagerCore array) {
        super(name, socketChannel);
        this.array = array;
    }

    public void execute() {
        for (Iterator<RealObject> it = array.objectIterator(); it.hasNext(); ) {
            new ReRequestObjectUseData(name, socketChannel, array, it.next().getID()).execute();
        }
    }
}
