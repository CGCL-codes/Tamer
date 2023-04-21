package tudresden.ocl20.codegen.java;

import tudresden.ocl20.*;
import tudresden.ocl20.core.OclModel;
import tudresden.ocl20.core.util.Naming;
import tudresden.ocl20.core.jmi.ocl.commonmodel.*;
import tudresden.ocl20.core.jmi.ocl.expressions.*;
import tudresden.ocl20.core.jmi.ocl.types.*;
import tudresden.ocl20.core.jmi.ocl.OclPackage;
import java.util.*;

/**
 * Generates code for all invariants of a contextual classifier into 
 * one standalone class having an "evaluate"-Method for every invariant.
 *
 *@author  Stefan Ocke
 */
public class JmiClassCodeGenerator {

    private OclModel model;

    private String evalPackage;

    private static final String EVLPCKG = "tudresden.ocl20.eval";

    private static final String JMIIMPORT = "javax.jmi.reflect";

    private static final String CLASSNAMEPRFX = "OclEval";

    /** Creates a new instance of JmiClassCodeGenerator */
    public JmiClassCodeGenerator(OclModel model) {
        this.model = model;
        this.evalPackage = EVLPCKG;
    }

    /**
     *This class provides the generated code of {@link JmiClassCodeGenerator JmiClassCodeGenerator}. 
     */
    public static class SourceClass {

        /**the pathname of the class*/
        public String pathname;

        /**throws source code for the class*/
        public String srcCode;
    }

    public String getJavaPathname(Classifier context) {
        return evalPackage + "." + CLASSNAMEPRFX + context.getNameA();
    }

    /**Generates the Java-Code for a class that contains evaluate-Methods for each invariant in the classifier context.*/
    public SourceClass getSourceClass(Classifier context) {
        StringBuffer result = new StringBuffer();
        String classname = CLASSNAMEPRFX + context.getNameA();
        String packagename = evalPackage;
        result.append("package ");
        result.append(packagename);
        result.append(";\n");
        result.append("\n");
        appendImport(result, JMIIMPORT);
        appendImport(result, CodeGenerator.DEFAULTPKGPREFIX);
        result.append("\n");
        result.append("public class ");
        result.append(classname);
        result.append("{\n");
        result.append("private RefPackage model;\n\n");
        result.append("public ");
        result.append(classname);
        result.append("(RefPackage model){\nthis.model=model;\n}\n\n");
        Collection expressions = ((tudresden.ocl20.core.jmi.mof14.model.ModelPackage) model.getModel()).getMof14ocl().getOcl().getExpressions().getAContextualClassifierExpressionInOcl().getExpressionInOcl(context);
        Set constraintNames = new HashSet();
        Iterator it = expressions.iterator();
        while (it.hasNext()) {
            ExpressionInOcl eio = (ExpressionInOcl) it.next();
            Constraint c = eio.getConstraintA();
            OclExpression bodyExp = eio.getBodyExpression();
            if (c != null && c.getStereotypeNameA().equals("invariant")) {
                result.append("//invariant ");
                result.append(c.getNameA());
                result.append(" : ");
                result.append(eio.getBodyA());
                result.append("\n");
                result.append("public boolean evaluate");
                String constraintName = c.getNameA();
                constraintName = Naming.makeUniqueName(constraintName, constraintNames);
                constraintNames.add(constraintName);
                result.append(constraintName);
                result.append("(){\n");
                JmiCodeGenerator jcg = new JmiCodeGenerator(model, false);
                result.append(jcg.getCode(bodyExp));
                result.append("return ");
                result.append(jcg.getExpId(bodyExp));
                result.append(".isTrue();\n}\n");
            }
        }
        result.append("public boolean evaluate(String constraintName){\n");
        it = constraintNames.iterator();
        while (it.hasNext()) {
            String constraintName = (String) it.next();
            result.append("if (constraintName.equals(\"");
            result.append(constraintName);
            result.append("\")){\n");
            result.append("return evaluate");
            result.append(constraintName);
            result.append("();\n");
            result.append("}\n");
        }
        result.append("throw new RuntimeException(\"Unknown constraint: \"+constraintName);\n");
        result.append("}\n");
        result.append("\n");
        result.append("private static final String [] constraintNames = new String[]{");
        it = constraintNames.iterator();
        while (it.hasNext()) {
            String constraintName = (String) it.next();
            result.append('"');
            result.append(constraintName);
            result.append('"');
            if (it.hasNext()) {
                result.append(',');
            }
        }
        result.append("};\n");
        result.append("public String[] getConstraintNames(){return constraintNames;}\n");
        result.append("}\n");
        SourceClass srcClass = new SourceClass();
        srcClass.srcCode = result.toString();
        srcClass.pathname = packagename + "." + classname;
        return srcClass;
    }

    private void appendImport(StringBuffer sb, String packageName) {
        sb.append("import ");
        sb.append(packageName);
        if (packageName.endsWith(".")) {
            sb.append("* ;\n");
        } else {
            sb.append(".* ;\n");
        }
    }
}
