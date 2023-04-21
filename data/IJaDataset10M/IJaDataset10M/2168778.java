package net.sf.orcc.backends.promela;

import net.sf.orcc.ir.TypeInt;
import net.sf.orcc.ir.TypeList;
import net.sf.orcc.ir.TypeUint;
import net.sf.orcc.ir.util.TypePrinter;

/**
 * This class defines a PROMELA type printer.
 * 
 * @author Matthieu Wipliez
 * 
 */
public class PromelaTypePrinter extends TypePrinter {

    @Override
    public String caseTypeInt(TypeInt type) {
        return "int";
    }

    @Override
    public String caseTypeList(TypeList type) {
        return doSwitch(type.getInnermostType());
    }

    @Override
    public String caseTypeUint(TypeUint type) {
        return "uint";
    }
}
