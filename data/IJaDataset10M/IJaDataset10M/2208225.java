package edu.ucdavis.cs.dblp.service;

import java.util.List;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import edu.ucdavis.cs.dblp.data.Keyword;
import edu.ucdavis.cs.dblp.data.Publication;
import edu.ucdavis.cs.taxonomy.Category;

/**
 * @author pfishero
 *
 */
public class DemuxContentService implements ContentService {

    private static final Logger logger = Logger.getLogger(DemuxContentService.class);

    private List<ContentService> contentServices;

    private final NullContentService nullContentService = new NullContentService();

    public void setContentServices(List<ContentService> contentServices) {
        this.contentServices = contentServices;
    }

    @Override
    public String retrieveAbstract(Publication pub) {
        final String theAbstract = acceptingService(pub).retrieveAbstract(pub);
        if (StringUtils.isEmpty(theAbstract)) {
            logger.error("unable to retrieve abstract for " + pub.getKey() + " - " + pub.getEe());
        }
        return theAbstract;
    }

    @Override
    public void retrieveAll(Publication pub) {
        acceptingService(pub).retrieveAll(pub);
    }

    @Override
    public Set<Category> retrieveClassification(Publication pub) {
        return acceptingService(pub).retrieveClassification(pub);
    }

    @Override
    public Set<String> retrieveGeneralTerms(Publication pub) {
        return acceptingService(pub).retrieveGeneralTerms(pub);
    }

    @Override
    public Set<Keyword> retrieveKeywords(Publication pub) {
        return acceptingService(pub).retrieveKeywords(pub);
    }

    public ContentService acceptingService(Publication pub) {
        for (ContentService contentService : contentServices) {
            if (contentService.accepts(pub)) {
                logger.debug(contentService + " accepts (and will process request for) " + pub.getKey());
                return contentService;
            }
        }
        logger.error("unable to find contentservice for " + pub.getKey() + " - " + pub.getEe());
        return nullContentService;
    }

    @Override
    public boolean accepts(Publication pub) {
        boolean accept = false;
        for (ContentService contentService : contentServices) {
            if (contentService.accepts(pub)) {
                accept = true;
                logger.debug("pub " + pub.getKey() + " accepted by " + contentService);
                break;
            }
        }
        return accept;
    }
}
