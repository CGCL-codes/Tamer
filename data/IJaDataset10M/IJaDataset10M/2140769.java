package com.inspiresoftware.lib.dto.geda.test;

import com.inspiresoftware.lib.dto.geda.DTOSupport;
import com.inspiresoftware.lib.dto.geda.DTOSupportAwareAdapter;
import com.inspiresoftware.lib.dto.geda.adapter.EntityRetriever;

/**
 * .
 * <p/>
 * User: denispavlov
 * Date: Feb 21, 2012
 * Time: 9:47:23 AM
 */
public interface ExposedEntityRetriever extends EntityRetriever, DTOSupportAwareAdapter {

    /**
     * @return support bean
     */
    DTOSupport getDtoSupport();
}
