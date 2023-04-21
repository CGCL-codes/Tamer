package edu.psu.citeseerx.dao2.logic;

import java.util.List;
import org.springframework.dao.DataAccessException;
import edu.psu.citeseerx.dao2.ExternalMetadataDAO;
import edu.psu.citeseerx.domain.CiteULike;
import edu.psu.citeseerx.domain.DBLP;

/**
 *  CSXExternalMetadataFacadeImplementation
 * @author Juan Pablo Fernandez Ramirez
 * @version $Rev: 1014 $ $Date: 2009-03-06 10:44:04 -0500 (Fri, 06 Mar 2009) $
 */
public class CSXExternalMetadataImpl implements CSXExternalMetadataFacade {

    private ExternalMetadataDAO extMetadataDAO;

    public void setExtMetadataDAO(ExternalMetadataDAO extMetadataDAO) {
        this.extMetadataDAO = extMetadataDAO;
    }

    public void addDBLPRecord(DBLP record) throws DataAccessException {
        extMetadataDAO.addDBLPRecord(record);
    }

    public void deleteDBLP() throws DataAccessException {
        extMetadataDAO.deleteDBLP();
    }

    public List<DBLP> getDBLPRecordsByTitle(String title) throws DataAccessException {
        return extMetadataDAO.getDBLPRecordsByTitle(title);
    }

    public void addCiteULikeRecord(CiteULike record) throws DataAccessException {
        extMetadataDAO.addCiteULikeRecord(record);
    }

    public CiteULike getCiteULikeRecordByDOI(String doi) throws DataAccessException {
        return extMetadataDAO.getCiteULikeRecordByDOI(doi);
    }
}
