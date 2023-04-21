package org.embl.ebi.SoaplabServer;

import org.embl.ebi.SoaplabShare.*;
import org.embl.ebi.analysis.OutputPropertyDef;
import embl.ebi.tools.DBConnectionManager;
import java.util.*;

/**
 * An interface defining how to store results (and some job
 * characterictics in a local relational database.
 *<P>
 * Recommended constructor:
 *<pre>
 *   PersistenceManagerImpl (SOAPToolkit toolkit)
 *      throws SoaplabException {...}
 *</pre>
 * Getting an implementation instance of a surrounding 'toolkit'
 * allows access to the parameters defining the database storage.
 *<p>
 * @author <A HREF="mailto:senger@ebi.ac.uk">Martin Senger</A>
 * @version $Id: PersistenceManager.java,v 1.2 2006/11/29 14:29:07 mahmutuludag Exp $
 */
public interface PersistenceManager {

    /**************************************************************************
     *
     **************************************************************************/
    Job getJob(String jobId) throws SoaplabException;

    /**************************************************************************
     *
     **************************************************************************/
    void setJob(Job job) throws SoaplabException;

    /**************************************************************************
     *
     **************************************************************************/
    void removeJob(String jobId) throws SoaplabException;

    /**************************************************************************
     *
     **************************************************************************/
    Map getResults(Job job) throws SoaplabException;

    /**************************************************************************
     *
     **************************************************************************/
    Map getResults(Job job, String[] names) throws SoaplabException;

    /**************************************************************************
     * Return null if 'name' result does not exist.
     **************************************************************************/
    java.lang.Object getResult(Job job, String name) throws SoaplabException;

    /**************************************************************************
     *
     **************************************************************************/
    void setResults(Job job, ResultContainer[] results) throws SoaplabException;

    /**************************************************************************
     *
     **************************************************************************/
    void localize(Job job, OutputPropertyDef[] outputDefs) throws SoaplabException;

    public DBConnectionManager getDbManager();
}
