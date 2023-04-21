package com.jklas.search.index.berkeley;

import com.jklas.search.index.IndexId;
import com.jklas.search.index.ObjectKey;
import com.jklas.search.index.PostingMetadata;
import com.jklas.search.index.Term;

public class ClosedWriterState implements IndexWriterState {

    @Override
    public void handleClose(BerkeleyIndexWriter writer) {
        writer.closeWhenClosed();
    }

    @Override
    public void handleDelete(BerkeleyIndexWriter writer, ObjectKey objectKey) {
        writer.deleteWhenClosed();
    }

    @Override
    public void handleWrite(BerkeleyIndexWriter writer, Term term, ObjectKey key, PostingMetadata metadata) {
        writer.writeWhenClosed();
    }

    @Override
    public void handleOpen(BerkeleyIndexWriter writer, IndexId indexId) {
        writer.openWhenClosed(indexId);
        writer.setState(new OpenWriterState());
    }
}
