package net.teqlo.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import net.teqlo.TeqloException;
import orbital.logic.imp.Formula;
import orbital.logic.imp.Logic;
import orbital.logic.sign.ParseException;
import orbital.moon.logic.ClassicalLogic;

/**
 * Provides logic functionality using the Orbital library
 * 
 * @author jthwaites
 * 
 */
public class TeqloLogic {

    private static TeqloLogic instance = new TeqloLogic();

    private Logic logic = new ClassicalLogic();

    /**
	 * Singleton pattern
	 */
    private TeqloLogic() {
    }

    /**
	 * Singleton pattern
	 * 
	 * @return TeqloLogic instance
	 */
    public static TeqloLogic getInstance() {
        return TeqloLogic.instance;
    }

    /**
	 * Converts the supplied formula to DNF
	 * 
	 * @param formulaText
	 * @return
	 * @throws TeqloException
	 */
    public String convertToDnf(String formulaText) throws TeqloException {
        String dnfText = null;
        try {
            Formula formula = (Formula) this.logic.createExpression(formulaText);
            Formula dnf = ClassicalLogic.Utilities.disjunctiveForm(formula, true);
            dnfText = dnf.toString();
        } catch (ParseException e) {
            throw new TeqloException(this, formulaText, e, "Could not parse this formula");
        }
        return dnfText;
    }

    /**
	 * Test
	 * 
	 * @param args
	 * @throws Exception
	 */
    public static void main(String[] args) throws Exception {
        String formula = "";
        while (!formula.equals("quit")) {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            formula = br.readLine();
            String dnf = null;
            try {
                dnf = TeqloLogic.getInstance().convertToDnf(formula);
            } catch (Throwable e) {
                dnf = "Could not parse";
            }
            System.out.println(dnf);
        }
    }
}
