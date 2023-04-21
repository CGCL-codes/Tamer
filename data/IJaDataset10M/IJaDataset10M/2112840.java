package de.grogra.imp3d.glsl.material.channel;

import java.util.Vector;
import de.grogra.imp3d.glsl.material.MaterialConfiguration;
import de.grogra.imp3d.shading.BlendItem;
import de.grogra.imp3d.shading.ChannelBlend;
import de.grogra.math.Channel;
import de.grogra.math.ChannelMap;

public class GLSLChannelBlend extends GLSLChannelMapNode {

    @Override
    public Result generate(ChannelMap inp, MaterialConfiguration cs, GLSLChannelMap inpChan, int channel) {
        assert (inp instanceof ChannelBlend);
        ChannelBlend cb = (ChannelBlend) inp;
        BlendItem last = null;
        int maxRT = Result.ET_FLOAT;
        GLSLChannelMap bChannel;
        Result bChannelRes;
        Result inpData = generateResultWithChannelDefault(cb.getInput(), cs, inpChan, Channel.X);
        String t = cs.registerNewTmpVar(Result.ET_FLOAT, inpData.getReturnType() == Result.ET_FLOAT ? inpData.toString() : "(" + inpData + ").x");
        Vector<Result> results = new Vector<Result>();
        for (BlendItem b = cb.getBlend(); b != null; b = b.getNext()) {
            bChannel = GLSLChannelMap.getGLSLObject(b.getInput());
            bChannelRes = bChannel != null ? bChannel.generate(b.getInput(), cs, inpChan, channel) : new Result("0.0", Result.ET_FLOAT);
            results.add(bChannelRes);
            maxRT = Math.max(maxRT, bChannelRes.getReturnType());
        }
        String s = "";
        int no = 0;
        String curString = null, lastString = null;
        for (BlendItem b = cb.getBlend(); b != null; b = b.getNext()) {
            curString = results.elementAt(no).convert(maxRT);
            s += "(" + b.getValue() + ">" + t + ")?";
            if (lastString != null) {
                String mix = "(" + b.getValue() + "-" + t + ")/" + (b.getValue() - last.getValue());
                s += "mix(" + curString + "," + lastString + "," + mix + "):";
            } else {
                s += curString + ":";
            }
            last = b;
            lastString = curString;
            no++;
        }
        if (lastString != null) s += lastString; else s += t;
        return new Result(s, maxRT);
    }

    @Override
    public Class<ChannelBlend> instanceFor() {
        return ChannelBlend.class;
    }
}
