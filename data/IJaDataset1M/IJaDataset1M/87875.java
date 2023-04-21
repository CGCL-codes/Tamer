package ai;

import java.util.*;

/**
 *
 * @author root
 */
public class OrExp extends BoolExp {

    public OrExp() {
        mExpList = new LinkedList<BoolExp>();
    }

    public OrExp(LinkedList<BoolExp> pExpList) {
        mExpList = pExpList;
    }

    public void addExp(LexExp pExp) throws ParseException {
        if (pExp instanceof BoolExp) {
            mExpList.add((BoolExp) pExp);
        } else {
            throw new ParseException("OrExp.addExp(Expression pExp): pExp is not a BoolExp!!!");
        }
    }

    public LexExp substitute(String pLabelName, String pString) {
        LinkedList<BoolExp> lList = new LinkedList<BoolExp>();
        for (BoolExp be : mExpList) {
            lList.add((BoolExp) be.substitute(pLabelName, pString));
        }
        return new OrExp(lList);
    }

    public boolean evaluate() {
        for (BoolExp exp : mExpList) {
            if (exp.evaluate() == true) {
                return true;
            }
        }
        return false;
    }

    private LinkedList<BoolExp> mExpList;
}
