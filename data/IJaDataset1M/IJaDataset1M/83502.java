package com.manydesigns.portofino.base.calculations;

import com.manydesigns.portofino.base.MDAttribute;
import com.manydesigns.portofino.base.MDConfigVisitor;
import com.manydesigns.portofino.base.MDObject;
import java.util.Locale;

/**
 *
 * @author Paolo Predonzani - paolo.predonzani@manydesigns.com
 * @author Angelo Lupo      - angelo.lupo@manydesigns.com
 */
public class MDIntConstCalc extends MDCalc {

    public static final String copyright = "Copyright (c) 2005-2009, ManyDesigns srl";

    private final Integer value;

    /** Creates a new instance of MDIntConstCalc */
    public MDIntConstCalc(int id, MDFuncCalc parent, MDAttribute calculatedAttribute, int value, Locale locale) {
        super(id, parent, calculatedAttribute, locale);
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public Object recalculate(MDObject obj) throws Exception {
        return value;
    }

    @Override
    public void visit(MDConfigVisitor visitor) {
        visitor.doIntConstCalc(this);
    }
}
