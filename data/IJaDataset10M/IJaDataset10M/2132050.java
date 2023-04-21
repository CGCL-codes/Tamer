package net.sourceforge.greenvine.testmodel.data.entities.comparators;

import java.util.Comparator;
import net.sourceforge.greenvine.testmodel.data.entities.Contract;

public class ContractTermsComparator implements Comparator<Contract> {

    @Override
    public int compare(Contract thisObj, Contract thatObj) {
        int cmp = thisObj.getTerms().compareTo(thatObj.getTerms());
        if (cmp != 0) return cmp;
        return thisObj.getEmployeeId().compareTo(thatObj.getEmployeeId());
    }
}
