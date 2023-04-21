package com.loribel.commons.util.collection;

import java.util.ArrayList;

/**
 * List with count items management.
 *
 * See Test : GB_ArrayListWithCountTest
 * 
 * @author Gregory Borelli 
 */
public class GB_ArrayListWithCount extends GB_CountListDecorator {

    public GB_ArrayListWithCount() {
        super(new ArrayList());
    }
}
