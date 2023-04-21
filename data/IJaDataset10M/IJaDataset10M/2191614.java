package net.sf.sql2java.fetcher.sql.filter.components;

import javax.swing.SwingWorker;
import javax.swing.tree.DefaultMutableTreeNode;
import net.sf.sql2java.fetcher.sql.filter.beans.DataHolder;

public class DecodeDataHolderTask extends SwingWorker<DataHolder, Void> {

    private DataHolder holder;

    private ResultHandler resultHandler;

    private DefaultMutableTreeNode root;

    public DecodeDataHolderTask(DataHolder holder, DefaultMutableTreeNode root, ResultHandler resultHandler) {
        this.holder = holder;
        this.root = root;
        this.resultHandler = resultHandler;
    }

    @Override
    protected DataHolder doInBackground() throws Exception {
        holder.decode(root);
        return holder;
    }

    @Override
    protected void done() {
        try {
            resultHandler.finished(get());
        } catch (Exception e) {
            resultHandler.error(e);
        }
    }

    public interface ResultHandler {

        public void finished(DataHolder holder);

        public void error(Throwable error);
    }
}
