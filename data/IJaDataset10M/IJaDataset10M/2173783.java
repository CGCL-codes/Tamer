package com.pp.cameleon.api.document.service.request.practice;

import com.pp.cameleon.api.document.dto.Practice;
import com.pp.cameleon.api.service.request.impl.ListRequest;

/**
 * User: P. Lalonde
 * Date: 24-Jan-2008
 */
public class PracticeListRequest extends ListRequest {

    /**
     * @return The document bean.
     */
    public Practice getPractice() {
        return (Practice) getPrototypeBean();
    }

    /**
     * @param practice The document bean.
     */
    public void setPractice(Practice practice) {
        setPrototypeBean(practice);
    }
}
