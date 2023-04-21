package com.magneticreason.fitnium.api;

import com.magneticreason.fitnium.FitniumFixture;

public class FitniumCheckBoxAPI {

    /**
     * Click a check box
     * @param loc Locator of element
     *
     * <br/><br/>
     * | set check box | locator | to state | checked|unchecked |
    */
    public static final void setCheckBoxToState(FitniumFixture fitnium, String loc, String state) {
        String actualState = FitniumVariableAPI.replaceAnyVars(state);
        if (actualState.equalsIgnoreCase("checked")) fitnium.getSelenium().check(FitniumVariableAPI.replaceAnyVars(loc)); else if (actualState.equalsIgnoreCase("unchecked")) fitnium.getSelenium().uncheck(FitniumVariableAPI.replaceAnyVars(loc)); else System.err.println("Unknown check box state : " + state);
    }

    /**
     * Click a check box
     * @param loc Locator of element
     *
     * <br/><br/>
     * | set check box | locator | to click | clicked|unclicked |
    */
    public static final void setCheckBoxToClick(FitniumFixture fitnium, String loc, String state) {
        String actualState = FitniumVariableAPI.replaceAnyVars(state);
        String locator = FitniumVariableAPI.replaceAnyVars(loc);
        if (actualState.equalsIgnoreCase("clicked")) {
            if (!fitnium.getSelenium().isChecked(locator)) {
                fitnium.getSelenium().click(locator);
            }
        } else if (actualState.equalsIgnoreCase("unclicked")) {
            if (fitnium.getSelenium().isChecked(locator)) {
                fitnium.getSelenium().click(locator);
            }
        } else {
            System.err.println("Unknown check box click state : " + state);
        }
    }

    /**
     * Click a check box
     * @param loc Locator of element
     * 
     * <br/><br/>
     * | click check box | locator | 
    */
    public static final void clickCheckBox(FitniumFixture fitnium, String loc) {
        fitnium.getSelenium().check(FitniumVariableAPI.replaceAnyVars(loc));
    }

    /**
     * Unclick a check box
     * @param loc Locator of element
     * 
     * <br/><br/>
     * | unclick check box | locator | 
     */
    public static final void unclickCheckBox(FitniumFixture fitnium, String loc) {
        fitnium.getSelenium().uncheck(FitniumVariableAPI.replaceAnyVars(loc));
    }

    /**
     * Check if check box is checked !!
     * @param loc Locator of element
     * @return true if check box is checked
     * 
     * <br/><br/>
     * | is check box checked | locator | 
     */
    public static final boolean isCheckBoxChecked(FitniumFixture fitnium, String loc) {
        return fitnium.getSelenium().isChecked(FitniumVariableAPI.replaceAnyVars(loc));
    }

    /**
     * Stores if a check box is checked in a fitnim variable
     * @param loc Locator of element
     * @param name Name of the variable to set
     * 
     * <br/><br/>
     * | store is check box | locator | checked in | name | 
     */
    public static final void storeIsCheckBoxCheckedIn(FitniumFixture fitnium, String loc, String name) {
        FitniumVariableAPI.setFitniumVariableCalledWithValue(fitnium, name, Boolean.toString(FitniumCheckBoxAPI.isCheckBoxChecked(fitnium, loc)));
    }
}
