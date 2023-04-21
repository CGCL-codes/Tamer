package org.atlantal.impl.cms.content;

import java.util.Map;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.atlantal.api.cms.content.ChildrenContent;
import org.atlantal.utils.AtlantalMapArray;
import org.atlantal.utils.AtlantalObjectArray;

/**
 * <p>Titre : Ewaloo</p>
 * <p>Description : Moteur de recherche structur�</p>
 * <p>Copyright : Copyright (c) 2002</p>
 * <p>Soci�t� : Mably Multim�dia</p>
 * @author Fran�ois MASUREL
 * @version 1.0
 */
public abstract class ChildrenContentInstance extends ListContentInstance implements ChildrenContent {

    private static Logger logger = Logger.getLogger(ChildrenContentInstance.class);

    static {
        logger.setLevel(Level.INFO);
    }

    /**
     * Constructor
     */
    public ChildrenContentInstance() {
    }

    /**
     * @return resultsArray
     */
    public Map getResultsMap() {
        return null;
    }

    /**
     * @return resultsArray
     */
    public AtlantalMapArray getResultsArray() {
        return this.getResults().getArray();
    }

    /**
     * {@inheritDoc}
     */
    public AtlantalObjectArray getResultsKeys() throws Exception {
        return null;
    }
}
