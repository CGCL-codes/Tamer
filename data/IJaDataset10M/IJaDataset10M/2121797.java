package com.volatileengine.platform;

import java.util.List;
import com.volatileengine.material.shader.ShaderProgram;
import com.volatileengine.material.shader.variable.ShaderVariable;

/**
 * @author Ahmed
 * 
 */
public class ReadUniformsReadCommand extends ReadCommand<List<ShaderVariable<?>>> {

    private ShaderProgram shaderProgram;

    public ReadUniformsReadCommand(ShaderProgram prog) {
        shaderProgram = prog;
    }

    @Override
    public void execute(GPU executioner) {
        object = executioner.getShaderUniforms(shaderProgram);
    }
}
