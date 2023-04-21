package EDU.Washington.grad.noth.cda.constraint;

import EDU.Washington.grad.noth.cda.component.*;
import EDU.Washington.grad.gjb.cassowary.*;

public class AboveConstraint extends AdjacencyConstraint {

    public AboveConstraint(ClSimplexSolver solver, ConstrComponent srcCC, ConstrComponent targetCC) {
        super(solver, srcCC, targetCC);
    }

    public void addConstraints() {
        int a;
        SelPoint sp;
        ConstrComponent srcCC, targetCC;
        ClLinearInequality cli;
        if (ccList.size() != 2) {
            System.out.println("AboveConstr.addConstr: " + ccList.size() + " CC's, not required 2!");
            return;
        }
        srcCC = (ConstrComponent) ccList.elementAt(0);
        targetCC = (ConstrComponent) ccList.elementAt(1);
        if (relConstrs.size() != targetCC.selPoints.size()) {
            if (relConstrs.size() != 0) {
                System.out.println("AboveConstr.addConstr: relConstrs = " + relConstrs + "; should be empty!");
                relConstrs.removeAllElements();
                relConstrs.setSize(targetCC.selPoints.size());
            }
            relConstrs.setSize(targetCC.selPoints.size());
            for (a = 0; a < targetCC.selPoints.size(); a++) {
                sp = (SelPoint) targetCC.selPoints.elementAt(a);
                try {
                    cli = new ClLinearInequality(sp.Y(), CL.Op.LEQ, srcCC.topSP.Y());
                } catch (ExCLInternalError e) {
                    System.out.println("AboveConstr.constructor: ExCLInternalError on #" + a);
                    return;
                }
                relConstrs.setElementAt(cli, a);
            }
        }
        for (a = 0; a < relConstrs.size(); a++) {
            cli = (ClLinearInequality) relConstrs.elementAt(a);
            try {
                if (cli != null) solver.addConstraint(cli);
            } catch (ExCLInternalError e) {
                System.out.println("AboveConstr.addConstr: ExCLInternalError adding #" + a + " = " + cli);
            } catch (ExCLRequiredFailure e) {
                System.out.println("AboveConstr.addConstr: ExCLRequiredFailure " + "adding #" + a + " = " + cli);
            }
        }
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("AboveConstraint: ");
        ConstrComponent srcCC, targetCC;
        if (ccList.size() != 2) {
            sb.append(" ILL-FORMED CONSTRAINT WITH " + ccList.size());
            sb.append(" INSTEAD OF 2 CC's");
        } else {
            srcCC = (ConstrComponent) ccList.elementAt(0);
            targetCC = (ConstrComponent) ccList.elementAt(1);
            sb.append("srcCC = " + srcCC);
            sb.append(", targetCC = " + targetCC);
        }
        return sb.toString();
    }
}
