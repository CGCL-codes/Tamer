package org.deved.antlride.debug.model.event;

import org.deved.antlride.debug.model.AntlrDebugTarget;

public class AntlrDebugMarkEvent extends AntlrDebugEvent {

    private int index;

    public AntlrDebugMarkEvent(AntlrDebugTarget debugTarget, AntlrDebugEventKind eventKind, String... properties) {
        super(debugTarget, eventKind);
        this.index = Integer.parseInt(properties[1]);
    }

    public int getIndex() {
        return index;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(super.toString());
        builder.append(index);
        return builder.toString();
    }
}
