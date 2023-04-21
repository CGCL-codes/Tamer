package org.xith3d.render.states.units;

import org.xith3d.render.states.StateMap;
import org.xith3d.render.states.StateUnit;
import org.xith3d.scenegraph.GLSLContext;
import org.xith3d.scenegraph.GLSLShaderProgram;
import org.xith3d.scenegraph.ShaderProgramContext;

/**
 * @author Abdul
 * @author Yuri Vl. Gushchin
 * @author Marvin Froehlich (aka Qudus)
 */
public class ShaderProgramStateUnit extends StateUnit {

    public static final int STATE_TYPE = StateMap.newStateType();

    private static final StateMap shaderStateMap = new StateMap();

    private static final ShaderProgramStateUnit DEFAULT_PROG = new ShaderProgramStateUnit(new GLSLContext(new GLSLShaderProgram(false)), true);

    private ShaderProgramContext<?> shaderProgram;

    public final ShaderProgramContext<?> getShaderProgram() {
        return (shaderProgram);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ShaderProgramContext<?> getNodeComponent() {
        return (shaderProgram);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean isTranslucent() {
        return (false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final long getStateId() {
        return (shaderProgram.getStateId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return (this.getClass().getSimpleName() + "( state-type: " + getStateType() + ", " + "state-ID: " + getStateId() + " )");
    }

    public void update(ShaderProgramContext<?> shaderProgram) {
        this.shaderProgram = shaderProgram;
        shaderStateMap.assignState(shaderProgram);
    }

    private ShaderProgramStateUnit(ShaderProgramContext<?> shaderProgram, boolean isDefault) {
        super(STATE_TYPE, isDefault);
        update(shaderProgram);
    }

    public static ShaderProgramStateUnit makeShaderProgramStateUnit(ShaderProgramContext<?> shaderProgram, StateUnit[] cache) {
        if (shaderProgram == null) {
            return (DEFAULT_PROG);
        } else if (cache[STATE_TYPE] != null) {
            final ShaderProgramStateUnit stateUnit = ((ShaderProgramStateUnit) cache[STATE_TYPE]);
            stateUnit.update(shaderProgram);
            return (stateUnit);
        } else {
            ShaderProgramStateUnit stateUnit = new ShaderProgramStateUnit(shaderProgram, false);
            cache[STATE_TYPE] = stateUnit;
            return (stateUnit);
        }
    }
}
