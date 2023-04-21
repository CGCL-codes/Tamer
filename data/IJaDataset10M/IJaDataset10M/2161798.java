package org.epo.gui.jphoenix.bean;

/**
 * Insert the type's description here.
 * Creation date: 02/2001
 * @author: INFOTEL
 */
public class JPhoenixEnvironmentList extends org.epo.gui.jphoenix.generic.BeanObject {

    private JPhoenixEnvironment[] jPhoenixEnvironments = null;

    /**
 * ServerList constructor comment.
 */
    public JPhoenixEnvironmentList() {
        super();
    }

    /**
 * Insert the method's description here.
 * Creation date: (28/02/01 15:18:39)
 */
    public JPhoenixEnvironmentList(JPhoenixEnvironment[] theJPhoenixEnvironments) {
        super();
        setJPhoenixEnvironments(theJPhoenixEnvironments);
    }

    /**
 * Insert the method's description here.
 * Creation date: (22/01/02 15:27:31)
 * @return org.epo.gui.jphoenix.bean.JPhoenixEnvironment[]
 */
    public JPhoenixEnvironment[] getJPhoenixEnvironments() {
        return jPhoenixEnvironments;
    }

    /**
 * Insert the method's description here.
 * Creation date: (28/02/01 14:29:18)
 * @return int
 */
    public int getNbEnvironments() {
        return jPhoenixEnvironments.length;
    }

    /**
 * Insert the method's description here.
 * Creation date: (22/01/02 15:27:31)
 * @param newJPhoenixEnvironment org.epo.gui.jphoenix.bean.JPhoenixEnvironment[]
 */
    private void setJPhoenixEnvironments(JPhoenixEnvironment[] newJPhoenixEnvironments) {
        jPhoenixEnvironments = newJPhoenixEnvironments;
    }
}
