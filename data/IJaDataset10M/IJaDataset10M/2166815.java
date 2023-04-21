package homura.hde.impl.lwjgl.gfx.state.records;

import homura.hde.core.scene.state.record.StateRecord;
import homura.hde.impl.lwjgl.gfx.state.LWJGLShaderObjectsState;

public class ShaderObjectsStateRecord extends StateRecord {

    LWJGLShaderObjectsState reference = null;

    public LWJGLShaderObjectsState getReference() {
        return reference;
    }

    public void setReference(LWJGLShaderObjectsState reference) {
        this.reference = reference;
    }

    @Override
    public void invalidate() {
        super.invalidate();
        reference = null;
    }
}
