package org.tigr.cloe.model.facade.consensusFacade;

import java.util.Arrays;
import org.tigr.cloe.model.facade.datastoreFacade.authentication.UserCredentials;
import org.tigr.cloe.model.facade.datastoreFacade.dao.DAO;
import org.tigr.cloe.model.facade.datastoreFacade.dao.DAOException;
import org.tigr.seq.seqdata.SeqdataException;

/**
 * This is a Dummy implementation of the 
 * <code>ConsensusFacade</code>
 * This does not actually compute consensus
 * for the region, instead it will return all
 * the bases as 'N' with quality classes of 22 (ambiguous).
 * @see <a href="http://slicetools.sourceforge.net/libSlice/qualityclasses.html">
 * UnderStanding Quality Classes</a>
 * 
 * 
 * @author dkatzel
 *
 *
 */
public class DummyConsensusFacade implements ConsensusFacade {

    private DAO dao;

    public DummyConsensusFacade(DAO dao) {
        this.dao = dao;
    }

    @Override
    public IConsensus computeConsensus(IAssemblySliceRange pAssemblySliceRange, ConsensusFacadeParameters sliceParams) throws ConsensusFacadeException {
        int start;
        try {
            start = pAssemblySliceRange.getStartCoordinate();
            int length = pAssemblySliceRange.getEndCoordinate() - start + 1;
            char[] bases = dao.getGappedConsensusData(pAssemblySliceRange.getReferenceAssembly().getDatastoreAssemblyID(), UserCredentials.getProjectName(), start, length).toCharArray();
            short[] qualities = new short[length];
            Arrays.fill(qualities, (short) 22);
            return new Consensus(pAssemblySliceRange.getStartCoordinate(), bases, qualities);
        } catch (Exception e) {
            throw new ConsensusFacadeException("Error computing Consensus", e);
        }
    }
}
