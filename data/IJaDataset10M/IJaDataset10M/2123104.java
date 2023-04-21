package jp.go.aist.six.oval.core.repository.mongodb;

import java.util.List;
import jp.go.aist.six.oval.model.results.OvalResults;
import jp.go.aist.six.oval.repository.OvalRepositoryException;
import jp.go.aist.six.oval.repository.OvalResultRepository;
import jp.go.aist.six.oval.repository.QueryParams;

/**
 * An implementation of OvalRepository using MongoDB.
 *
 * @author  Akihito Nakamura, AIST
 * @version $Id: MongoOvalDefinitionResultRepository.java 2349 2012-05-02 09:15:58Z nakamura5akihito@gmail.com $
 */
public class MongoOvalDefinitionResultRepository extends MongoOvalDefinitionRepository implements OvalResultRepository {

    /**
     * Constructor.
     */
    public MongoOvalDefinitionResultRepository() {
    }

    @Override
    public OvalResults findOvalResultsById(final String id) throws OvalRepositoryException {
        OvalResults p_object = null;
        try {
            p_object = getDatastore().findById(OvalResults.class, id);
        } catch (Exception ex) {
            throw new OvalRepositoryException(ex);
        }
        return p_object;
    }

    @Override
    public List<OvalResults> findOvalResults() throws OvalRepositoryException {
        List<OvalResults> p_list = null;
        try {
            p_list = getDatastore().find(OvalResults.class);
        } catch (Exception ex) {
            throw new OvalRepositoryException(ex);
        }
        return p_list;
    }

    @Override
    public List<OvalResults> findOvalResults(final QueryParams params) throws OvalRepositoryException {
        List<OvalResults> p_list = null;
        try {
            p_list = getDatastore().find(OvalResults.class, params);
        } catch (Exception ex) {
            throw new OvalRepositoryException(ex);
        }
        return p_list;
    }

    @Override
    public List<String> findOvalResultsIds() throws OvalRepositoryException {
        List<String> list = null;
        try {
            list = getDatastore().findId(OvalResults.class);
        } catch (Exception ex) {
            throw new OvalRepositoryException(ex);
        }
        return list;
    }

    @Override
    public List<String> findOvalResultsIds(final QueryParams params) throws OvalRepositoryException {
        List<String> p_list = null;
        try {
            p_list = getDatastore().findId(OvalResults.class, params);
        } catch (Exception ex) {
            throw new OvalRepositoryException(ex);
        }
        return p_list;
    }

    @Override
    public long countOvalResults() throws OvalRepositoryException {
        long count = 0L;
        try {
            count = getDatastore().count(OvalResults.class);
        } catch (Exception ex) {
            throw new OvalRepositoryException(ex);
        }
        return count;
    }

    @Override
    public String saveOvalResults(final OvalResults oval_results) throws OvalRepositoryException {
        String id = null;
        try {
            id = getDatastore().save(OvalResults.class, oval_results);
        } catch (Exception ex) {
            throw new OvalRepositoryException(ex);
        }
        return id;
    }
}
