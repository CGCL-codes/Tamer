package org.xtoto.action;

import javax.ejb.Local;

@Local
public interface Register {

    String register();

    public String getVerify();

    public void setVerify(String verify);

    void destroy();
}
