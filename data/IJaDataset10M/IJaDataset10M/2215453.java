package com.jme.util.shader.uniformtypes;

import java.io.IOException;
import com.jme.util.export.InputCapsule;
import com.jme.util.export.JMEExporter;
import com.jme.util.export.JMEImporter;
import com.jme.util.export.OutputCapsule;
import com.jme.util.shader.ShaderVariable;

/** ShaderVariableFloat3 */
public class ShaderVariableFloat3 extends ShaderVariable {

    public float value1;

    public float value2;

    public float value3;

    public void write(JMEExporter e) throws IOException {
        super.write(e);
        OutputCapsule capsule = e.getCapsule(this);
        capsule.write(value1, "value1", 0.0f);
        capsule.write(value2, "value2", 0.0f);
        capsule.write(value3, "value3", 0.0f);
    }

    public void read(JMEImporter e) throws IOException {
        super.read(e);
        InputCapsule capsule = e.getCapsule(this);
        value1 = capsule.readFloat("value1", 0.0f);
        value2 = capsule.readFloat("value2", 0.0f);
        value3 = capsule.readFloat("value3", 0.0f);
    }
}
