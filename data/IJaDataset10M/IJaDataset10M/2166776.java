package com.eastidea.qaforum.home;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;
import com.eastidea.qaforum.entity.LogCheckPoint;

@Name("logCheckPointHome")
public class LogCheckPointHome extends EntityHome<LogCheckPoint> {

    private static final long serialVersionUID = 5664728106737684563L;

    @In(create = true)
    TestCaseHome testCaseHome;

    @In(create = true)
    TestRoundHome testRoundHome;

    public void setLogCheckPointId(Long id) {
        setId(id);
    }

    public Long getLogCheckPointId() {
        return (Long) getId();
    }

    @Override
    protected LogCheckPoint createInstance() {
        LogCheckPoint logCheckPoint = new LogCheckPoint();
        return logCheckPoint;
    }

    public void wire() {
        getInstance();
    }

    public boolean isWired() {
        return true;
    }

    public LogCheckPoint getDefinedInstance() {
        return isIdDefined() ? getInstance() : null;
    }
}
