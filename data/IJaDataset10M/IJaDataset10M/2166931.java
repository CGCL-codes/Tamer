package com.zzsoft.app.base.sysuser.log;

import framework.zze2p.mod.Pojo_0I;

public interface LogAdmI {

    public abstract String logOp(Pojo_0I pojo);

    public abstract String logSession(Pojo_0I pojo);

    public abstract String logOPUnLogin(Pojo_0I pojo);
}
