package org.jabusuite.article;

import java.io.Serializable;
import javax.persistence.Entity;
import org.jabusuite.core.utils.JbsObject;

/**
 * @author hilwers
 * @date 29.03.2007
 *
 */
@Entity
public class DiscountGroup extends JbsObject implements Serializable {

    private static final long serialVersionUID = -6337272720506611408L;

    private String name;

    private float discountPercent;

    /**
	 * @return the discountPercent
	 */
    public float getDiscountPercent() {
        return discountPercent;
    }

    /**
	 * @param discountPercent the discountPercent to set
	 */
    public void setDiscountPercent(float discountPercent) {
        this.discountPercent = discountPercent;
    }

    /**
	 * @return the name
	 */
    public String getName() {
        return name;
    }

    /**
	 * @param name the name to set
	 */
    public void setName(String name) {
        this.name = name;
    }
}
