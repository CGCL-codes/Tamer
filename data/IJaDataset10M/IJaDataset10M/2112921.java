package net.sf.cotta.ftp;

import net.sf.cotta.jbehave.BehavioursLoader;
import org.jbehave.core.behaviour.Behaviours;

public class AllBehaviours implements Behaviours {

    public Class[] getBehaviours() {
        return new BehavioursLoader(getClass()).loadBehaviours();
    }
}
