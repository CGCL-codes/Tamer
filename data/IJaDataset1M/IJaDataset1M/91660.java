package com.vss.client;

import java.util.Map;
import com.vss.core.Client;
import com.vss.core.IOperate;
import com.vss.core.IRequest;
import com.vss.core.IResponse;
import com.vss.core.impl.OS;
import com.vss.core.impl.RequestImpl;

public class VSSDeploy implements IOperate {

    boolean g = true;

    public void setG(boolean g) {
        this.g = g;
    }

    boolean o = false;

    public void setO(boolean o) {
        this.o = o;
    }

    boolean r = false;

    public void setR(boolean r) {
        this.r = r;
    }

    boolean v = false;

    public void setV(boolean v) {
        this.v = v;
    }

    boolean w = true;

    public void setW(boolean w) {
        this.w = w;
    }

    public IResponse operate(IRequest request) {
        StringBuffer buff = new StringBuffer();
        Map<String, Object> env = request.getOptions();
        buff.append(env.get(OS.OPTIONS));
        buff.append(g ? " -G " : "");
        buff.append(o ? " -O " : "");
        buff.append(r ? " -R " : "");
        buff.append(v ? " -V " : "");
        buff.append(w ? " -W " : "");
        env.put(OS.OPTIONS, buff.toString());
        return new Client().operate(new RequestImpl("Deploy " + VSSUtils.escape(env.get(OS.FILE)), env));
    }
}
