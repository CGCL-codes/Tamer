package psparser.psobject.psoperator;

import psparser.psobject.*;

public class PSOperatorAdd extends PSOperator {

    public PSOperatorAdd() {
        System.out.println("Nouvelle Instruction Add");
        this.name = "add";
        this.argnum = 2;
        this.addarg(2, PSInteger.class, PSReal.class);
    }

    public void localProcess(PSStack psstack) throws PSOperatorException {
        double i;
        try {
            i = ((Number) psstack.operandPop().getValue()).doubleValue() + ((Number) psstack.operandPop().getValue()).doubleValue();
        } catch (Exception e) {
            throw new PSOperatorUndefinedResultException();
        }
        System.out.println("I:" + i + " " + (new Double(i)).floatValue());
        if (i - (new Double(i)).intValue() == 0) {
            psstack.operandPush(new PSInteger((int) i));
        } else {
            psstack.operandPush(new PSReal(i));
        }
    }
}
