package de.grogra.imp3d.glsl.material.channel;

import de.grogra.imp3d.glsl.material.MaterialConfiguration;
import de.grogra.imp3d.shading.Granite;
import de.grogra.math.Channel;
import de.grogra.math.ChannelMap;

public class GLSLGranite extends GLSLVolumeFunction {

    static final String graniteSig = "float granite(vec3 pos)";

    static final String granite = "float freq4 = 4.0;" + "float noise = 0.0, freqi = 1.0;" + "for (int i = 5; i >= 0; i--)" + "{" + "float t = freq4;" + "t = inoise (t * pos);" + "noise += ((t > 0.0) ? t - 0.5 : -0.5 - t) * freqi;" + "freq4 *= 2.0;" + "freqi *= 0.5;" + "}" + "return noise;";

    @Override
    public Result generateVolumeData(ChannelMap inp, MaterialConfiguration cs, GLSLChannelMap inpChan, int channel) {
        assert (inp instanceof Granite);
        SimplexNoise.registerNoiseFunctions(cs);
        Granite ch = (Granite) inp;
        cs.registerFunc(graniteSig, granite);
        GLSLChannelMap input = GLSLChannelMap.getGLSLObject(ch.getInput());
        Result res = input != null ? input.generate(ch.getInput(), cs, inpChan, Channel.X) : inpChan.generate(null, cs, null, Channel.X);
        String pos = res.convert(Result.ET_VEC3);
        return new Result("granite(" + pos + ")", Result.ET_FLOAT);
    }

    @Override
    public Class instanceFor() {
        return Granite.class;
    }
}
