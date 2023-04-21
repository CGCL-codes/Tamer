package brgm.mapreader.operations;

import brgm.mapreader.Image;
import brgm.mapreader.Operation;
import brgm.mapreader.OperationEvent;

public abstract class NotParameteredOperation extends Operation {

    public NotParameteredOperation(String name) {
        super(name);
    }

    @Override
    public void execute(Image img) {
        notifyForStartExecution();
        doExecute(img);
        notifyForExecuted();
    }

    @Override
    public void execute(Image img1, Image img2) {
    }

    public abstract void doExecute(Image img);

    public final boolean hasParameters() {
        return false;
    }
}
