package ez_squeeze;

import java.io.Serializable;

public class Pitcher implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 9012722274127105674L;

    private int lemons = 0;

    private int ice = 0;

    private int sugar = 0;

    private int rIce = 0;

    private int rLemons = 0;

    private int rSugar = 0;

    private int cups;

    private int cups_per_pitcher = 14;

    private int cupsLeft = 0;

    Pitcher() {
        check();
    }

    public void serve() {
        check();
        if (cupsLeft == 0) {
            refill();
        }
        cupsLeft -= 1;
        cups--;
        if (ice - rIce < 0) {
            rIce = 0;
        }
        ice -= rIce;
        System.out.println("served 1");
        reset();
    }

    public void refill() {
        if (lemons - rLemons < 0) {
            rLemons = 0;
        }
        if (sugar - rSugar < 0) {
            rSugar = 0;
        }
        lemons -= rLemons;
        sugar -= rSugar;
        cupsLeft = cups_per_pitcher;
        System.out.println("refilled");
        reset();
    }

    public void dump() {
        cupsLeft = 0;
    }

    public void check() {
        rLemons = EZ_Squeeze_EmpireView.getRecipeLemons();
        rIce = EZ_Squeeze_EmpireView.getRecipeIce();
        rSugar = EZ_Squeeze_EmpireView.getRecipeSugar();
        cups = EZ_Squeeze_EmpireView.getCups();
        lemons = EZ_Squeeze_EmpireView.getLemons();
        ice = EZ_Squeeze_EmpireView.getIce();
        sugar = EZ_Squeeze_EmpireView.getSugar();
    }

    public void reset() {
        EZ_Squeeze_EmpireView.setRecipeLemons(rLemons);
        EZ_Squeeze_EmpireView.setRecipeIce(rIce);
        EZ_Squeeze_EmpireView.setRecipeSugar(rSugar);
        EZ_Squeeze_EmpireView.setCups(cups);
        EZ_Squeeze_EmpireView.setLemons(lemons);
        EZ_Squeeze_EmpireView.setIce(ice);
        EZ_Squeeze_EmpireView.setSugar(sugar);
    }
}
