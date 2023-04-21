package org.vosao.business;

import java.util.List;
import org.vosao.entity.PageAttributeEntity;
import org.vosao.entity.PageEntity;

public interface PageAttributeBusiness {

    /**
	 * Get all page attributes definitions including inherited from parent pages.
	 * @param pageUrl - page friendly URL.
	 * @return list of found page attributes definitions.
	 */
    List<PageAttributeEntity> getByPage(final String pageUrl);

    /**
	 * Get page attribute definition including inherited from parent pages.
	 * @param pageUrl - page friendly URL.
	 * @param name - attribute name.
	 * @return list of found page attributes definitions.
	 */
    PageAttributeEntity getByPage(final String pageUrl, final String name);

    void setAttribute(PageEntity page, String name, String language, String value, boolean applyToChildren);

    List<String> validateBeforeUpdate(PageAttributeEntity entity);
}
