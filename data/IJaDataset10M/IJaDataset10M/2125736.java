package org.plazmaforge.framework.core.criteria;

/** 
 * @author Oleh Hapon
 * $Id: CriteriaSorter.java,v 1.1 2010/12/05 07:51:26 ohapon Exp $
 */
public class CriteriaSorter extends CriteriaElement implements ICriteriaSorter {

    private String order = ICriteria.ASC;

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = ICriteria.ASC.equals(order) ? ICriteria.ASC : ICriteria.DESC;
    }

    public boolean isDown() {
        return ICriteria.ASC.equals(order);
    }

    public void setDown(boolean down) {
        this.order = down ? ICriteria.ASC : ICriteria.DESC;
    }

    public int getOrderInt() {
        return ICriteria.ASC.equals(order) ? 1 : -1;
    }
}
