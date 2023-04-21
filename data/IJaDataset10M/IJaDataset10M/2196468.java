package com.jme.util.shader;

import java.io.IOException;
import com.jme.util.export.InputCapsule;
import com.jme.util.export.JMEExporter;
import com.jme.util.export.JMEImporter;
import com.jme.util.export.OutputCapsule;
import com.jme.util.export.Savable;

/**
 * An utily class to store shader's uniform variables content.
 */
public class ShaderVariable implements Savable {

    /** Name of the uniform variable. * */
    public String name;

    /** ID of uniform. * */
    public int variableID = -1;

    /** Needs to be refreshed */
    public boolean needsRefresh = true;

    public boolean equals(Object obj) {
        if (obj instanceof ShaderVariable) {
            ShaderVariable temp = (ShaderVariable) obj;
            if (name.equals(temp.name)) return true;
        }
        return false;
    }

    public void write(JMEExporter e) throws IOException {
        OutputCapsule capsule = e.getCapsule(this);
        capsule.write(name, "name", "");
        capsule.write(variableID, "variableID", -1);
    }

    public void read(JMEImporter e) throws IOException {
        InputCapsule capsule = e.getCapsule(this);
        name = capsule.readString("name", "");
        variableID = capsule.readInt("variableID", -1);
    }

    public Class getClassTag() {
        return this.getClass();
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
