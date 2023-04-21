package xbird.engine.request;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import xbird.engine.Request;
import xbird.xquery.ext.grid.DispatchQueryExecTask;

/**
 * 
 * <DIV lang="en"></DIV>
 * <DIV lang="ja"></DIV>
 * 
 * @author Makoto YUI (yuin405+xbird@gmail.com)
 */
public final class GridQueryTaskRequest extends Request {

    private DispatchQueryExecTask queryTask;

    public GridQueryTaskRequest() {
    }

    public GridQueryTaskRequest(DispatchQueryExecTask queryTask) {
        super(ReturnType.AUTO);
        this.queryTask = queryTask;
    }

    @Override
    public Signature getSignature() {
        return Signature.GRID_QTASK;
    }

    public DispatchQueryExecTask getTask() {
        return queryTask;
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        this.queryTask = (DispatchQueryExecTask) in.readObject();
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
        out.writeObject(queryTask);
    }
}
