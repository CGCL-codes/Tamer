package si.ibloc.cms.logic.interfaces;

import java.util.Date;

/**
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public interface Indexer {

    Date getLastIndexingTime();

    void index();

    void stop();
}
