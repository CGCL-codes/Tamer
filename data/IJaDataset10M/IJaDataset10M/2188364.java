package com.seitenbau.testing.templates;

import com.seitenbau.testing.shared.templateengine.ITemplateParams;
import com.seitenbau.testing.shared.templateengine.IGenerator;
import com.seitenbau.testing.instrumentation.generator.*;
import com.seitenbau.testing.shared.config.*;

public class Record_AspectIncludeGenerator implements IGenerator {

    protected static String nl;

    public static synchronized Record_AspectIncludeGenerator create(String lineSeparator) {
        nl = lineSeparator;
        Record_AspectIncludeGenerator result = new Record_AspectIncludeGenerator();
        nl = null;
        return result;
    }

    public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;

    protected final String TEXT_1 = NL + "aspect ";

    protected final String TEXT_2 = "Aspect extends ";

    protected final String TEXT_3 = "Aspect {" + NL + "" + NL + "\tprotected ITracer createAspectTracer() {" + NL + "\t\tMethodConfiguration mcfg=getConfiguration().getMethodConfiguration().get(";

    protected final String TEXT_4 = ");" + NL + "\t\treturn TracerFactory.createTracer( mcfg,getStaticDaoFactory() );" + NL + "\t}" + NL + "" + NL + "\tprotected pointcut pc_CallToTrace() :" + NL + "\t\t\t\texecution(";

    protected final String TEXT_5 = " ";

    protected final String TEXT_6 = ".";

    protected final String TEXT_7 = "(";

    protected final String TEXT_8 = "))" + NL + "//\t\t\t\t&& pc_Exclude()" + NL + "\t\t\t;" + NL + "" + NL + "\tprotected pointcut pc_allMethodCalls() : call(* *(..))" + NL + "\t\t\t\t&& !within(";

    protected final String TEXT_9 = "*Aspect)" + NL + "\t\t\t\t&&  withincode(";

    protected final String TEXT_10 = " ";

    protected final String TEXT_11 = ".";

    protected final String TEXT_12 = "(";

    protected final String TEXT_13 = "))" + NL + "//\t\t\t\t&& pc_Exclude()" + NL + "\t\t\t;";

    protected final String TEXT_14 = NL + NL + "\tprotected pointcut pc_allNewCalls() : call(* .new(..))" + NL + "\t\t\t\t&& !within(";

    protected final String TEXT_15 = "*Aspect)" + NL + "\t\t\t\t&&  withincode(";

    protected final String TEXT_16 = " ";

    protected final String TEXT_17 = ".";

    protected final String TEXT_18 = "(";

    protected final String TEXT_19 = "))" + NL + "//\t\t\t\t&& pc_Exclude()" + NL + "\t\t\t;" + NL + "" + NL + "}";

    protected final String TEXT_20 = NL;

    @SuppressWarnings("unchecked")
    public String generate(ITemplateParams argument) {
        final StringBuffer stringBuffer = new StringBuffer();
        RecorderParams param = (RecorderParams) argument;
        RecorderConfiguration rcfg = param.getConfig();
        int imcfg = -1;
        for (MethodConfiguration mcfg : rcfg.getMethodConfiguration()) {
            ++imcfg;
            String packageName = mcfg.getPackageName();
            String packageAndClassName = mcfg.getClassName();
            String baseName = rcfg.getBaseName() + mcfg.getClassName();
            if (packageName != null && packageName.length() > 0) {
                packageAndClassName = packageName + "." + mcfg.getClassName();
            }
            stringBuffer.append(TEXT_1);
            stringBuffer.append(baseName);
            stringBuffer.append(imcfg);
            stringBuffer.append(TEXT_2);
            stringBuffer.append(baseName);
            stringBuffer.append(TEXT_3);
            stringBuffer.append(imcfg);
            stringBuffer.append(TEXT_4);
            stringBuffer.append(mcfg.getReturnType());
            stringBuffer.append(TEXT_5);
            stringBuffer.append(packageAndClassName);
            stringBuffer.append(TEXT_6);
            stringBuffer.append(mcfg.getMethodName());
            stringBuffer.append(TEXT_7);
            stringBuffer.append(mcfg.getParameters());
            stringBuffer.append(TEXT_8);
            stringBuffer.append(packageAndClassName);
            stringBuffer.append(TEXT_9);
            stringBuffer.append(mcfg.getReturnType());
            stringBuffer.append(TEXT_10);
            stringBuffer.append(packageAndClassName);
            stringBuffer.append(TEXT_11);
            stringBuffer.append(mcfg.getMethodName());
            stringBuffer.append(TEXT_12);
            stringBuffer.append(mcfg.getParameters());
            stringBuffer.append(TEXT_13);
            stringBuffer.append(TEXT_14);
            stringBuffer.append(packageAndClassName);
            stringBuffer.append(TEXT_15);
            stringBuffer.append(mcfg.getReturnType());
            stringBuffer.append(TEXT_16);
            stringBuffer.append(packageAndClassName);
            stringBuffer.append(TEXT_17);
            stringBuffer.append(mcfg.getMethodName());
            stringBuffer.append(TEXT_18);
            stringBuffer.append(mcfg.getParameters());
            stringBuffer.append(TEXT_19);
        }
        stringBuffer.append(TEXT_20);
        return stringBuffer.toString();
    }
}
