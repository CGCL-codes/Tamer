package org.jrazdacha.model.nullmodel;

import org.jrazdacha.model.Warning;

/**
 * Null object representation of Warning class
 * 
 * @author Alexey Rogatkin
 * @author Vitaliy Ruzhnikov
 * @author Alexey Tulin
 */
public class NullWarning extends Warning implements Nullable {

    @Override
    public Long getId() {
        return new Long(-1);
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public int getWeight() {
        return 0;
    }

    @Override
    public void setId(Long id) {
    }

    @Override
    public void setName(String name) {
    }

    @Override
    public void setWeight(int weight) {
    }

    /**
	 * Indicates, when the object is null.
	 * 
	 * @return <code>true</code> if object is null, else return
	 *         <code>false</code>
	 */
    @Override
    public boolean isNull() {
        return true;
    }
}
