package javax.media.j3d;

import java.util.Vector;
import java.util.BitSet;
import java.util.ArrayList;

/**
 * The Appearance object defines all rendering state that can be set
 * as a component object of a Shape3D node.
 */
class ShaderAppearanceRetained extends AppearanceRetained {

    static final int SHADER_PROGRAM = 0x0800;

    static final int SHADER_ATTRIBUTE_SET = 0x1000;

    protected ShaderProgramRetained shaderProgram = null;

    protected ShaderAttributeSetRetained shaderAttributeSet = null;

    protected boolean isMirror = false;

    /**
     * Set the shader program object to the specified object.
     * @param shaderProgram object that specifies the desired shader program
     * and shader program attributes.
     */
    void setShaderProgram(ShaderProgram sp) {
        synchronized (liveStateLock) {
            if (source.isLive()) {
                if (this.shaderProgram != null) {
                    this.shaderProgram.clearLive(refCount);
                    this.shaderProgram.removeMirrorUsers(this);
                }
                if (sp != null) {
                    ((ShaderProgramRetained) sp.retained).setLive(inBackgroundGroup, refCount);
                    ((ShaderProgramRetained) sp.retained).copyMirrorUsers(this);
                }
                sendMessage(SHADER_PROGRAM, (sp != null ? ((ShaderProgramRetained) sp.retained).mirror : null));
            }
            if (sp == null) {
                this.shaderProgram = null;
            } else {
                this.shaderProgram = (ShaderProgramRetained) sp.retained;
            }
        }
    }

    /**
     * Retrieves the current shader program object.
     * @return current shader program object
     */
    ShaderProgram getShaderProgram() {
        return (shaderProgram == null ? null : (ShaderProgram) shaderProgram.source);
    }

    /**
     * Sets the ShaderAttributeSet object to the specified object.  Setting it to
     * null is equivalent to specifying an empty set of attributes.
     *
     * @param shaderAttributeSet object that specifies the desired shader attributes
     */
    void setShaderAttributeSet(ShaderAttributeSet sas) {
        synchronized (liveStateLock) {
            if (source.isLive()) {
                if (this.shaderAttributeSet != null) {
                    this.shaderAttributeSet.clearLive(refCount);
                    this.shaderAttributeSet.removeMirrorUsers(this);
                }
                if (sas != null) {
                    ((ShaderAttributeSetRetained) sas.retained).setLive(inBackgroundGroup, refCount);
                    ((ShaderAttributeSetRetained) sas.retained).copyMirrorUsers(this);
                }
                sendMessage(SHADER_ATTRIBUTE_SET, (sas != null ? ((ShaderAttributeSetRetained) sas.retained).mirror : null));
            }
            if (sas == null) {
                this.shaderAttributeSet = null;
            } else {
                this.shaderAttributeSet = (ShaderAttributeSetRetained) sas.retained;
            }
        }
    }

    /**
     * Retrieves the current ShaderAttributeSet object.
     * @return current ShaderAttributeSet object
     */
    ShaderAttributeSet getShaderAttributeSet() {
        return (shaderAttributeSet == null ? null : (ShaderAttributeSet) shaderAttributeSet.source);
    }

    public boolean equals(Object obj) {
        return ((obj instanceof ShaderAppearanceRetained) && equals((ShaderAppearanceRetained) obj));
    }

    boolean equals(ShaderAppearanceRetained sApp) {
        boolean flag;
        flag = (sApp == this);
        if (flag) return flag;
        flag = ((sApp != null) && (shaderProgram == sApp.shaderProgram) && (shaderAttributeSet == sApp.shaderAttributeSet));
        if (!flag) return flag;
        return super.equals(sApp);
    }

    synchronized void createMirrorObject() {
        if (mirror == null) {
            mirror = new ShaderAppearanceRetained();
            ((ShaderAppearanceRetained) mirror).isMirror = true;
        }
        initMirrorObject();
    }

    /**
     * This routine updates the mirror appearance for this appearance.
     * It also calls the update method for each node component if it
     * is not null.
     */
    synchronized void initMirrorObject() {
        super.initMirrorObject();
        ShaderAppearanceRetained mirrorApp = (ShaderAppearanceRetained) mirror;
        if (shaderProgram != null) {
            mirrorApp.shaderProgram = (ShaderProgramRetained) shaderProgram.mirror;
        } else {
            mirrorApp.shaderProgram = null;
        }
        if (shaderAttributeSet != null) {
            mirrorApp.shaderAttributeSet = (ShaderAttributeSetRetained) shaderAttributeSet.mirror;
        } else {
            mirrorApp.shaderAttributeSet = null;
        }
    }

    /**
   * Update the "component" field of the mirror object with the
   *  given "value"
   */
    synchronized void updateMirrorObject(int component, Object value) {
        super.updateMirrorObject(component, value);
        ShaderAppearanceRetained mirrorApp = (ShaderAppearanceRetained) mirror;
        if ((component & SHADER_PROGRAM) != 0) {
            mirrorApp.shaderProgram = (ShaderProgramRetained) value;
        } else if ((component & SHADER_ATTRIBUTE_SET) != 0) {
            mirrorApp.shaderAttributeSet = (ShaderAttributeSetRetained) value;
        }
    }

    /**
     * This method calls the setLive method of all appearance bundle
     * objects.
     */
    void doSetLive(boolean backgroundGroup, int refCount) {
        if (shaderProgram != null) {
            shaderProgram.setLive(backgroundGroup, refCount);
        }
        if (shaderAttributeSet != null) {
            shaderAttributeSet.setLive(backgroundGroup, refCount);
        }
        super.doSetLive(backgroundGroup, refCount);
    }

    /**
     * This clearLive routine first calls the superclass's method, then
     * it removes itself to the list of lights
     */
    void clearLive(int refCount) {
        super.clearLive(refCount);
        if (shaderProgram != null) {
            shaderProgram.clearLive(refCount);
        }
        if (shaderAttributeSet != null) {
            shaderAttributeSet.clearLive(refCount);
        }
    }

    synchronized void addAMirrorUser(Shape3DRetained shape) {
        super.addAMirrorUser(shape);
        if (shaderProgram != null) shaderProgram.addAMirrorUser(shape);
        if (shaderAttributeSet != null) shaderAttributeSet.addAMirrorUser(shape);
    }

    synchronized void removeAMirrorUser(Shape3DRetained shape) {
        super.removeAMirrorUser(shape);
        if (shaderProgram != null) shaderProgram.removeAMirrorUser(shape);
        if (shaderAttributeSet != null) shaderAttributeSet.removeAMirrorUser(shape);
    }

    final void sendMessage(int attrMask, Object attr) {
        ArrayList univList = new ArrayList();
        ArrayList gaList = Shape3DRetained.getGeomAtomsList(mirror.users, univList);
        J3dMessage createMessage = new J3dMessage();
        createMessage.threads = J3dThread.UPDATE_RENDERING_ATTRIBUTES;
        createMessage.type = J3dMessage.SHADER_APPEARANCE_CHANGED;
        createMessage.universe = null;
        createMessage.args[0] = this;
        createMessage.args[1] = new Integer(attrMask);
        createMessage.args[2] = attr;
        createMessage.args[3] = new Integer(changedFrequent);
        VirtualUniverse.mc.processMessage(createMessage);
        for (int i = 0; i < univList.size(); i++) {
            createMessage = new J3dMessage();
            createMessage.threads = J3dThread.UPDATE_RENDER;
            createMessage.type = J3dMessage.SHADER_APPEARANCE_CHANGED;
            createMessage.universe = (VirtualUniverse) univList.get(i);
            createMessage.args[0] = this;
            createMessage.args[1] = new Integer(attrMask);
            createMessage.args[2] = attr;
            ArrayList gL = (ArrayList) gaList.get(i);
            GeometryAtom[] gaArr = new GeometryAtom[gL.size()];
            gL.toArray(gaArr);
            createMessage.args[3] = gaArr;
            VirtualUniverse.mc.processMessage(createMessage);
        }
    }

    boolean isStatic() {
        if (!super.isStatic()) {
            return false;
        }
        boolean flag = source.capabilityBitsEmpty() && ((shaderProgram == null) || shaderProgram.source.capabilityBitsEmpty()) && ((shaderAttributeSet == null) || shaderAttributeSet.source.capabilityBitsEmpty());
        return flag;
    }

    void compile(CompileState compState) {
        super.compile(compState);
        if (shaderProgram != null) {
            shaderProgram.compile(compState);
        }
        if (shaderAttributeSet != null) {
            shaderAttributeSet.compile(compState);
        }
    }

    boolean isOpaque(int geoType) {
        if (!super.isOpaque(geoType)) {
            return false;
        }
        return true;
    }

    void handleFrequencyChange(int bit) {
        super.handleFrequencyChange(bit);
        int mask = 0;
        if (bit == ShaderAppearance.ALLOW_SHADER_PROGRAM_WRITE) mask = SHADER_PROGRAM; else if (bit == ShaderAppearance.ALLOW_SHADER_ATTRIBUTE_SET_WRITE) mask = SHADER_ATTRIBUTE_SET;
        if (mask != 0) setFrequencyChangeMask(bit, mask);
    }
}
