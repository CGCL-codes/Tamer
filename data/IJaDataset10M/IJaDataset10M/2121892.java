package org.plazmaforge.bsolution.contact.common.beans;

import org.plazmaforge.framework.core.data.Dictionary;

/**
 * @author Oleh Hapon
 * Date: 8/10/2003
 * Time: 17:17:35
 */
public class LocalityType extends Dictionary {

    public static final Integer CITY = new Integer(1);

    public LocalityType() {
    }

    public LocalityType(Integer id) {
        setId(id);
    }
}
