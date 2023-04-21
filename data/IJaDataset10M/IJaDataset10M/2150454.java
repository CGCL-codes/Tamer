package nu.community.ida.framework.modules.menumaker;

import nu.community.ida.framework.BaseBean;

/**
 * @todo Write javadoc!
 * 
 * @author P&aring;l Brattberg <a href="mailto:pal@eminds.se">&lt;pal@eminds.se&gt;</a>
 * @version $Id: MenuViewBean.java,v 1.2 2004/06/27 22:19:40 pal Exp $
 */
public class MenuViewBean extends BaseBean {

    private WeekDay[] weekdays;

    private int maxNumberOfMeals;

    public String getBeanName() {
        return "MenuViewBean";
    }

    /**
     * @return
     */
    public int getMaxNumberOfMeals() {
        return maxNumberOfMeals;
    }

    /**
     * @return
     */
    public WeekDay[] getWeekdays() {
        return weekdays;
    }

    /**
     * @param i
     */
    public void setMaxNumberOfMeals(int i) {
        maxNumberOfMeals = i;
    }

    /**
     * @param daies
     */
    public void setWeekdays(WeekDay[] daies) {
        weekdays = daies;
    }
}
