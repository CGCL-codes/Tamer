package com.netstoke.core.commerce.market;

import com.netstoke.common.IEntity;

/**
 * <p>The <code>IItemCode</code> interface provides a mechanism for identifying 
 * items beyond id.
 * <p>ItemCode's will usually be used for things like coupon codes, etc..</p>
 * @author kmckee &lt;<a href="mailto:kevin.mckee@netstoke.com">kevin.mckee@netstoke.com</a>&gt;
 * @version 1.0
 * @since 1.0
 */
public interface IItemCode extends IEntity {

    /**
	 * Sets the code value.
	 * @param code String
	 */
    public void setCode(String code);

    /**
	 * Returns the code value.
	 * @return String
	 */
    public String getCode();
}
