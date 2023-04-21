package com.cresus.model;

import java.util.Date;

public interface Operation {

    public String getLabel();

    /**
	 * @brief Return the amount of the operation in cents. This value can be positive or negative
	 * @return
	 */
    public int getAmount();

    public Date getDate();

    public String toString();
}
