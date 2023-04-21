package com.jquery.antilia.grid;

import org.apache.wicket.model.Model;

/**
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 */
public class FirstColumnModel extends Model<Integer> {

    private static final long serialVersionUID = 1L;

    /**
	 * @param object
	 */
    public FirstColumnModel(Integer width) {
        super(width);
    }

    public Integer getWidth() {
        return (Integer) getObject();
    }

    public void setWidth(Integer width) {
        setObject(width);
    }
}
