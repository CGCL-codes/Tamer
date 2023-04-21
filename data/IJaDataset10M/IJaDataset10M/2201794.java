package de.grogra.imp3d.glsl.material;

import de.grogra.imp3d.glsl.material.channel.GLSLChannelMap;
import de.grogra.imp3d.glsl.material.channel.Result;
import de.grogra.math.Channel;
import de.grogra.math.ChannelMap;

public class GLSLSphereTracedInput extends GLSLChannelMap {

    String uv = null, pos = null;

    @Override
    public Result generate(ChannelMap inp, MaterialConfiguration cs, de.grogra.imp3d.glsl.material.channel.GLSLChannelMap inpChan, int channel) {
        assert (inp == null);
        assert (inpChan == null);
        if (pos == null) pos = cs.registerNewTmpVar(Result.ET_VEC3, "(gl_TextureMatrix[2] * vec4(normalize(vec3(TexUnit2, -1.0)), 0.0)).xyz;");
        switch(channel) {
            case Channel.U:
                if (uv == null) uv = cs.registerNewTmpVar(Result.ET_VEC2, "vec2(atan(" + pos + ".y, -" + pos + ".x) * 0.5, " + "acos(-" + pos + ".z) ) / 3.14159265358979323846264 + vec2(0.5, 0.0);");
                return new Result(uv, Result.ET_VEC2);
            case Channel.PX:
            case Channel.X:
                return new Result(pos, Result.ET_VEC3);
            case Channel.NX:
                return new Result("normal", Result.ET_VEC3);
            default:
                break;
        }
        return null;
    }

    @Override
    public Class instanceFor() {
        return null;
    }
}
