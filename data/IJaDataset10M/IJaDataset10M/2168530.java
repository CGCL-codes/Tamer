package pl.wcislo.sbql4j.lang.db4o.codegen.nostacks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import com.db4o.internal.ObjectContainerBase;
import com.db4o.internal.Transaction;
import pl.wcislo.sbql4j.java.model.compiletime.BindResult;
import pl.wcislo.sbql4j.java.model.compiletime.NestedInfo;
import pl.wcislo.sbql4j.java.utils.Pair;
import pl.wcislo.sbql4j.lang.codegen.CodeGenerator;
import pl.wcislo.sbql4j.lang.codegen.nostacks.QueryCodeGenNoStacks;
import pl.wcislo.sbql4j.lang.db4o.codegen.CodeGeneratorDb4o;
import pl.wcislo.sbql4j.lang.parser.expression.DotExpression;
import pl.wcislo.sbql4j.lang.parser.expression.Expression;
import pl.wcislo.sbql4j.lang.parser.expression.NameExpression;
import pl.wcislo.sbql4j.lang.pretty.QueryPretty;
import pl.wcislo.sbql4j.tools.javac.code.Symbol;
import pl.wcislo.sbql4j.tools.javac.code.Type;
import pl.wcislo.sbql4j.tools.javac.code.Symbol.ClassSymbol;
import pl.wcislo.sbql4j.tools.javac.code.Symbol.VarSymbol;
import pl.wcislo.sbql4j.tools.javac.code.Type.ArrayType;
import pl.wcislo.sbql4j.tools.javac.tree.JCTree.JCImport;
import pl.wcislo.sbql4j.tools.javac.tree.JCTree.JCSbqlExpression;

public class CodeGenDb4oNoStacks extends CodeGeneratorDb4o {

    private int index;

    private List<Pair<String>> varsToInclude = new ArrayList<Pair<String>>();

    private DotExpression rootExpr;

    public CodeGenDb4oNoStacks(CodeGenerator parentCodeGen, int index) {
        super(parentCodeGen);
        this.index = index;
    }

    @Override
    public String generateQueriesClass(int i, int j, List<JCImport> imports) {
        String className = getParentCodeGen().getQueriesClassName() + i + "Db4o" + j;
        StringBuilder sb = new StringBuilder();
        sb.append("package ").append(getQueriesClassPackage()).append(";").append("\n");
        sb.append("\n");
        sb.append("import java.util.*;").append("\n");
        sb.append("import pl.wcislo.sbql4j.exception.*;").append("\n");
        sb.append("import pl.wcislo.sbql4j.java.model.runtime.*;").append("\n");
        sb.append("import pl.wcislo.sbql4j.java.model.runtime.factory.*;").append("\n");
        sb.append("import pl.wcislo.sbql4j.java.utils.ArrayUtils;").append("\n");
        sb.append("import pl.wcislo.sbql4j.java.utils.OperatorUtils;").append("\n");
        sb.append("import pl.wcislo.sbql4j.java.utils.Pair;").append("\n");
        sb.append("import pl.wcislo.sbql4j.lang.codegen.simple.*;").append("\n");
        sb.append("import pl.wcislo.sbql4j.lang.codegen.nostacks.*;").append("\n");
        sb.append("import pl.wcislo.sbql4j.lang.types.*;").append("\n");
        sb.append("import pl.wcislo.sbql4j.lang.db4o.*;").append("\n");
        sb.append("import pl.wcislo.sbql4j.lang.db4o.codegen.*;").append("\n");
        sb.append("import pl.wcislo.sbql4j.lang.db4o.codegen.interpreter.*;").append("\n");
        sb.append("import pl.wcislo.sbql4j.lang.db4o.codegen.nostacks.*;").append("\n");
        sb.append("import pl.wcislo.sbql4j.util.Utils;").append("\n");
        sb.append("import pl.wcislo.sbql4j.model.*;").append("\n");
        sb.append("import pl.wcislo.sbql4j.model.collections.*;").append("\n");
        sb.append("import pl.wcislo.sbql4j.util.*;").append("\n");
        sb.append("import pl.wcislo.sbql4j.xml.model.*;").append("\n");
        sb.append("import pl.wcislo.sbql4j.xml.parser.store.*;").append("\n");
        sb.append("import pl.wcislo.sbql4j.lang.parser.terminals.operators.*;");
        sb.append("import pl.wcislo.sbql4j.lang.parser.expression.OrderByParamExpression.SortType;");
        sb.append("import org.apache.commons.collections.CollectionUtils;");
        sb.append("import pl.wcislo.sbql4j.lang.parser.expression.*;");
        sb.append("import pl.wcislo.sbql4j.lang.parser.terminals.*;");
        sb.append("import com.db4o.*;");
        sb.append("import com.db4o.internal.*;");
        sb.append("import com.db4o.internal.btree.*;");
        sb.append("import com.db4o.foundation.*;\n");
        for (JCImport im : imports) {
            sb.append(im.toString()).append("\n");
        }
        sb.append("\n");
        sb.append("public class ").append(className).append(" implements Db4oSBQLQuery { \n");
        for (Pair<String> field : varsToInclude) {
            sb.append("private ").append(field.val).append(" " + field.name).append("; \n");
        }
        sb.append("    public ").append(className).append("(").append("\n");
        for (Iterator<Pair<String>> fieldIt = varsToInclude.iterator(); fieldIt.hasNext(); ) {
            Pair<String> field = fieldIt.next();
            sb.append(field.val.toString()).append(" " + field.name.toString());
            if (fieldIt.hasNext()) {
                sb.append(",");
            }
            sb.append("\n");
        }
        sb.append(") {").append("\n");
        for (Pair<String> field : varsToInclude) {
            sb.append("this.").append(field.name.toString()).append(" = ");
            sb.append(field.name.toString()).append("; \n");
        }
        sb.append("    }").append("\n");
        sb.append("\n");
        QueryCodeGenDb4oNoStacks queryPretty = new QueryCodeGenDb4oNoStacks(getParentCodeGen(), this);
        sb.append("/**\n * query='");
        QueryPretty qp = new QueryPretty();
        qp.evaluateExpression(rootExpr);
        sb.append(qp.toString()).append("'\n");
        sb.append("' \n *");
        sb.append("*/\n");
        sb.append("        public ").append(rootExpr.getSignature().getJavaTypeString()).append(" executeQuery(ObjectContainerBase ocb, Transaction t) {\n");
        sb.append(queryPretty.evaluateExpression(rootExpr.ex2));
        sb.append("        }");
        sb.append("}").append("\n");
        sb.append("\n");
        return sb.toString();
    }

    @Override
    public String generateQueryInvocation(int i, int j, Expression tree) {
        StringBuilder sb = new StringBuilder();
        String genDb4oClassName = getParentCodeGen().getQueriesClassName() + i + "Db4o" + j;
        setQueriesClassName(genDb4oClassName);
        DotExpression dExpr = (DotExpression) tree;
        this.rootExpr = dExpr;
        String resJavaType = dExpr.ex2.getSignature().getJavaTypeString();
        String resIdent = dExpr.getSignature().getResultName();
        String dbConnIdent = dExpr.ex1.getSignature().getResultName();
        String className = getParentCodeGen().getQueriesClassName() + getParentCodeGen().getCurrentQueryIndex() + "Db4o" + index;
        sb.append(resJavaType).append(" ").append(resIdent).append("= ").append(dbConnIdent).append(".query( new ").append(getQueriesClassName()).append("(");
        for (Iterator<BindResult> fieldIt = dExpr.getVarToIncludeToDb4oQuery().iterator(); fieldIt.hasNext(); ) {
            BindResult field = fieldIt.next();
            NestedInfo ni = field.boundValueInfo;
            NameExpression bindExpr = (NameExpression) field.boundValue.getAssociatedExpression();
            String varName = null;
            if (ni != null) {
                varName = ni.nestedFrom.getResultName();
            }
            if (varName == null) {
                varName = bindExpr.l.val;
            }
            String varType = bindExpr.getSignature().getJavaTypeString();
            Pair<String> var = new Pair<String>(varName, varType);
            if (!varsToInclude.contains(var)) {
                varsToInclude.add(var);
                sb.append(varName);
                if (fieldIt.hasNext()) {
                    sb.append(",");
                }
            }
        }
        sb.append("));\n");
        return sb.toString();
    }

    @Override
    public int getGeneratedQueriesCount() {
        return 0;
    }

    @Override
    public boolean isGeneratingQueryClasses() {
        return true;
    }
}
