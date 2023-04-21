package com.rubecula.beanpot;

import com.rubecula.beanpot.BeanPotTest.SpecialProperty;

/**
 * @author Robin Hillyard
 * 
 */
public interface Messagey {

    /**
	 * @return the id
	 */
    public abstract long getId();

    /**
	 * @return a {@link SpecialProperty}
	 */
    public abstract SpecialProperty getMessage();
}
