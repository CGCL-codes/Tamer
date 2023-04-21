package com.googlecode.servus.content;

import com.google.common.collect.Lists;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ObservableInputStream extends FilterInputStream {

    protected List<TransferListener> transferListeners = Lists.newLinkedList();

    public ObservableInputStream(InputStream inputStream, TransferListener... transferListeners) {
        super(inputStream);
        for (TransferListener transferListener : transferListeners) {
            addTransferListener(transferListener);
        }
    }

    public void addTransferListener(TransferListener transferListener) {
        transferListeners.add(transferListener);
    }

    @Override
    public int read() throws IOException {
        int data = super.read();
        for (TransferListener transferListener : transferListeners) {
            transferListener.onByteRead(data);
        }
        return data;
    }
}
