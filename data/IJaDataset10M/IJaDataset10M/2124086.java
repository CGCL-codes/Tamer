package psparser.psobject.psoperator;

import java.util.*;
import psparser.psobject.*;

public class PSOperatorAstore extends PSOperator {

    public PSOperatorAstore() {
        System.out.println("Nouvelle Instruction Astore");
        name = "astore";
        argnum = 1;
        this.addarg(1, PSArray.class);
    }

    public void localProcess(PSStack psstack) throws PSOperatorException {
        int i;
        int t;
        PSArray array = (PSArray) psstack.operandPop();
        t = ((ArrayList) array.getValue()).size();
        for (i = t - 1; i >= 0; i--) {
            array.set(i, psstack.operandPop());
        }
        psstack.operandPush(array);
    }

    public void process(PSStack psstack) throws PSOperatorException {
        if (psstack.operandSize() < 1) {
            throw new PSOperatorException("StackUnderFlow");
        }
        if (!this.checkType(psstack)) {
            throw new PSOperatorException("CheckType");
        }
        PSArray array = (PSArray) psstack.operandPeek();
        if (psstack.operandSize() < ((ArrayList) array.getValue()).size() + 1) {
            throw new PSOperatorException("StackUnderFlow");
        }
        try {
            this.localProcess(psstack);
        } catch (Exception e) {
            System.out.println("Processing Operator error: " + e);
            e.printStackTrace();
        }
    }
}
