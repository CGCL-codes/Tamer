package gov.sns.xal.model.alg;

import gov.sns.tools.data.DataAdaptor;
import gov.sns.tools.data.DataFormatException;
import gov.sns.xal.model.IAlgorithm;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility factory class for the online model algorithm objects.
 *
 * @author  Christopher K. Allen
 * 
 * @deprecated Functionality put into base class gov.sns.xal.model.alg.Tracker
 */
@Deprecated
public final class AlgorithmFactory {

    /** data node label for algorithm data */
    public static final String NODETAG_ALG = "algorithm";

    /** attribute label for type string identifier */
    public static final String ATTRTAG_TYPE = "type";

    /** map of algorithm class types */
    private static Map<String, Class<? extends IAlgorithm>> s_mapTypes = new HashMap<String, Class<? extends IAlgorithm>>();

    /**
     *  Call registerClass() on all algorithm classes to be handled by algorithm 
     *  manager.
     */
    static {
        s_mapTypes.put(SynchronousTracker.s_strTypeId, SynchronousTracker.class);
        s_mapTypes.put(TransferMapTracker.s_strTypeId, TransferMapTracker.class);
        s_mapTypes.put(ParticleTracker.s_strTypeId, ParticleTracker.class);
        s_mapTypes.put(EnvelopeTracker.s_strTypeId, EnvelopeTracker.class);
        s_mapTypes.put(EnvTrackerAdapt.s_strTypeId, EnvTrackerAdapt.class);
        s_mapTypes.put(EnsembleTracker.s_strTypeId, EnsembleTracker.class);
        s_mapTypes.put(DiagnosticTracker.s_strTypeId, DiagnosticTracker.class);
        s_mapTypes.put(Trace3dTracker.s_strTypeId, Trace3dTracker.class);
        s_mapTypes.put(gov.sns.xal.model.alg.resp.ParticleResponse.s_strTypeId, gov.sns.xal.model.alg.resp.ParticleResponse.class);
    }

    /**
     * Create and initialize a new algorithm object from a data source exposing
     * the <code>DataAdaptor</code> interface.  It is assumed that the algorithm
     * described in the <code>daptArchive</code> argument is a subclass of the
     * <code>Tracker</code> algorithm base class, or at least an algorithm registered
     * to the <code>AlgorithmFactory</code> and exposing the <code>IArchive<code>
     * interface for loading parameters.
     * 
     * @param daptArchive   data source describing algorithm
     * 
     * @return  new, initialized <code>IAlgorithm<code> object
     * 
     * @throws DataFormatException      the data describing the algorithm is malformed
     * 
     * @see AlgorithmFactory#createEmpty
     */
    public static IAlgorithm createFrom(DataAdaptor daptArchive) throws DataFormatException {
        DataAdaptor daptAlg = daptArchive;
        String strType = daptAlg.stringValue(AlgorithmFactory.ATTRTAG_TYPE);
        IAlgorithm alg;
        try {
            alg = AlgorithmFactory.createEmpty(strType);
        } catch (ClassNotFoundException e) {
            throw new DataFormatException("AlgorithmFactory#createFrom() - 'type' value unknown");
        }
        alg.load(daptAlg);
        return alg;
    }

    /**
     *  Creates a new, empty algorithm object from its type identifier.
     *
     *  @param  strTypeId   string type identifier for algorithm
     *
     *  @return     new algorithm object
     */
    public static IAlgorithm createEmpty(String strTypeId) throws ClassNotFoundException {
        String strExcept = "AlgorithmFactory::create() - Unknown algorithm type id " + strTypeId;
        if (!s_mapTypes.containsKey(strTypeId)) throw new ClassNotFoundException(strExcept);
        IAlgorithm alg;
        Class<? extends IAlgorithm> cls = s_mapTypes.get(strTypeId);
        try {
            alg = cls.newInstance();
        } catch (Throwable e) {
            throw new AssertionError(e.getMessage());
        }
        return alg;
    }
}
