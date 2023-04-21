package net.sf.ejunitb3.examples;

import javax.ejb.Remote;

@Remote
public interface SessionBeanTestRemote {

    public String testing();
}
