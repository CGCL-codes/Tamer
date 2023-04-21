package org.jactr.core.production.six;

import org.jactr.core.production.ISubsymbolicProduction;

public interface ISubsymbolicProduction6 extends ISubsymbolicProduction {

    public static final String EXPECTED_UTILITY_PARAM = "ExpectedUtility";

    public static final String UTILITY_PARAM = "Utility";

    public static final String REWARD_PARAM = "Reward";

    /**
   * return the computed expected utility
   * @return
   */
    public double getExpectedUtility();

    public void setExpectedUtility(double utility);

    /**
   * return the predefined utility of the production
   * 
   * @return
   */
    public double getUtility();

    public void setUtility(double utility);

    /**
   * return the reward value associated with this production or Double.NaN if
   * there is no reward explicitly defined for this production
   * 
   * @return
   */
    public double getReward();

    public void setReward(double reward);
}
