package org.powerfolder.workflow.model.script;

public interface MultiScriptTagConstraintOrTemplate {

    public int getDefaultLength();

    public void setDefaultLength(int length);

    public void setLengthUnbounded(boolean inLengthUnbounded);

    public boolean isLengthUnbounded();

    public void setMinimumLength(int inMinLength);

    public int getMinimumLength();

    public void setMaximumLength(int inMaxLength);

    public int getMaximumLength();
}
