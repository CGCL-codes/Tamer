package com.manydesigns.portofino.base.calculations;

import com.manydesigns.portofino.base.MDConfig;
import java.util.List;

/**
 *
 * @author Paolo Predonzani - paolo.predonzani@manydesigns.com
 * @author Angelo Lupo      - angelo.lupo@manydesigns.com
 */
public class MDFuncCalcType {

    public static final String copyright = "Copyright (c) 2005-2009, ManyDesigns srl";

    private final int id;

    private final String name;

    private final String javaClass;

    private final Function function;

    /** Creates a new instance of MDFunctionType */
    public MDFuncCalcType(MDConfig config, int id, String name, String javaClass) throws Exception {
        this.id = id;
        this.name = name;
        this.javaClass = javaClass;
        function = (Function) (Class.forName(javaClass).newInstance());
        function.setConfig(config);
    }

    public Object compute(List args) throws Exception {
        return function.compute(args);
    }

    public String getUnaryOperator() {
        return function.getUnaryOperator();
    }

    public String getBinaryOperator() {
        return function.getBinaryOperator();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getJavaClass() {
        return javaClass;
    }
}
