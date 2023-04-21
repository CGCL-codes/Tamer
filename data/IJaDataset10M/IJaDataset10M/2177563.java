package net.sourceforge.smokestack.ejb.ex03;

import javax.ejb.Local;

@Local
public interface DataStoreLocal {

    public String getData();
}
