package org.coode.cloud.view;

/**
 * Author: Nick Drummond<br>
 * http://www.cs.man.ac.uk/~drummond<br><br>
 * <p/>
 * The University Of Manchester<br>
 * Bio Health Informatics Group<br>
 * Date: Sep 26, 2006<br><br>
 * <p/>
 */
public abstract class AbstractClassCloudView extends AbstractCloudView {

    private static final long serialVersionUID = -3770469508306941079L;

    public void initialiseView() throws Exception {
        super.initialiseView();
    }

    protected final boolean isOWLClassView() {
        return true;
    }
}
