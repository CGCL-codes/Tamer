package org.authorsite.domain.service.bib;

import java.util.Date;
import java.util.List;
import org.acegisecurity.annotation.Secured;
import org.authorsite.domain.AbstractHuman;
import org.authorsite.domain.bib.WebResource;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jking
 *
 */
public interface WebResourceService {

    @Transactional(readOnly = true)
    public abstract int countWebResources() throws DataAccessException;

    @Transactional(readOnly = true)
    public abstract List<WebResource> findAllWebResources() throws DataAccessException;

    @Transactional(readOnly = true)
    public abstract List<WebResource> findAllWebResources(int pageNumber, int pageSize) throws DataAccessException;

    @Transactional(readOnly = true)
    public abstract WebResource findById(long id) throws DataAccessException;

    @Transactional(readOnly = true)
    public abstract List<WebResource> findWebResourcesAfterDate(Date date) throws DataAccessException;

    @Transactional(readOnly = true)
    public abstract List<WebResource> findWebResourcesBeforeDate(Date date) throws DataAccessException;

    @Transactional(readOnly = true)
    public abstract List<WebResource> findWebResourcesBetweenDates(Date startDate, Date endDate) throws DataAccessException;

    @Transactional(readOnly = true)
    public abstract List<WebResource> findWebResourcesByDomain(String domain) throws DataAccessException;

    @Transactional(readOnly = true)
    public abstract List<WebResource> findWebResourcesByTitle(String title) throws DataAccessException;

    @Transactional(readOnly = true)
    public abstract List<WebResource> findWebResourcesByTitleWildcard(String titleWildcard) throws DataAccessException;

    @Transactional(readOnly = true)
    public abstract List<WebResource> findWebResourcesWithAuthor(AbstractHuman author) throws DataAccessException;

    @Transactional(readOnly = true)
    public abstract List<WebResource> findWebResourcesWithAuthorOrEditor(AbstractHuman human) throws DataAccessException;

    @Transactional(readOnly = true)
    public abstract List<WebResource> findWebResourcesWithEditor(AbstractHuman editor) throws DataAccessException;

    @Transactional(readOnly = true)
    public abstract List<WebResource> findWebResourcesWithPublisher(AbstractHuman publisher) throws DataAccessException;

    @Secured({ "ROLE_ADMINISTRATOR", "ROLE_EDITOR" })
    public abstract void saveWebResource(WebResource WebResource) throws DataAccessException;

    @Secured({ "ROLE_ADMINISTRATOR", "ROLE_EDITOR" })
    public abstract WebResource updateWebResource(WebResource WebResource) throws DataAccessException;

    @Secured({ "ROLE_ADMINISTRATOR", "ROLE_EDITOR" })
    public abstract void deleteWebResource(WebResource WebResource) throws DataAccessException;
}
