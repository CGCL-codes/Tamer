package com.inspiresoftware.lib.dto.geda.test;

import com.inspiresoftware.lib.dto.geda.DTOSupport;
import com.inspiresoftware.lib.dto.geda.DTOSupportAwareAdapter;
import com.inspiresoftware.lib.dto.geda.adapter.ValueConverter;

/**
 * .
 * <p/>
 * User: denispavlov
 * Date: Feb 21, 2012
 * Time: 9:45:31 AM
 */
public interface ExposedValueConverter extends ValueConverter, DTOSupportAwareAdapter {

    /**
     * @return support bean
     */
    DTOSupport getDtoSupport();
}
