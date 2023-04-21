package org.imogene.common.dao;

import java.util.List;
import org.imogene.common.dao.EntityDao;
import org.imogene.sync.localizedtext.LocalizedText;

/**
 * Implements a Hibernate DAO for the LocalizedText 
 * @author Medes-IMPS
 */
public interface LocalizedTextDao extends EntityDao {

    /**
	 * Load the entities of type LocalizedText for the field fieldId
	 * @param fieldId the id of the field for which localized texts should be gotten
	 * @return a list of localizedText
	 */
    public List<LocalizedText> listLocalizedText(String fieldId);
}
