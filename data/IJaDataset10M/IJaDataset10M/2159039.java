package com.inspiresoftware.lib.dto.geda.assembler.examples.generics;

import com.inspiresoftware.lib.dto.geda.annotations.Dto;
import com.inspiresoftware.lib.dto.geda.annotations.DtoField;
import org.junit.Ignore;

/**
 * Test DTO for Assembler.
 *
 * @author Denis Pavlov
 * @since 1.1.1
 * 
 * @param <V> simple value
 *
 */
@Dto
@Ignore
public class TestDto18aClass<V> {

    @DtoField("myProp")
    private V myProp;

    /**
	 * @return property
	 */
    public V getMyProp() {
        return myProp;
    }

    /**
	 * @param myProp property
	 */
    public void setMyProp(final V myProp) {
        this.myProp = myProp;
    }
}
