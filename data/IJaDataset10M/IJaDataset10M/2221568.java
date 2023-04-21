package dex.compiler.checker;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import dex.compiler.checker.type.MemberType;
import dex.compiler.checker.type.Type;
import dex.compiler.model.base.Identifiers;
import dex.compiler.model.definition.ClassDef;
import dex.compiler.model.definition.Constructor;
import dex.compiler.model.definition.DefinitionVisitor;
import dex.compiler.model.definition.Function;
import dex.compiler.model.definition.Import;
import dex.compiler.model.definition.Macro;
import dex.compiler.model.definition.Record;
import dex.compiler.model.definition.TypedName;
import dex.compiler.model.definition.Unit;
import dex.compiler.model.program.Path;
import dex.compiler.model.statement.Block;
import dex.compiler.model.statement.Comment;
import dex.compiler.model.statement.Statement;
import dex.compiler.model.type.BasicTypeNode;
import dex.compiler.model.type.TypeNode;
import dex.misc.Strings;
import static dex.compiler.checker.DefinitionProblems.*;

/**
 * Checks that definitions are valid.
 */
public class DefinitionChecker extends StatementChecker implements DefinitionVisitor {

    private MemberType currentClass;

    private List<Set<String>> variables;

    private List<Set<String>> functions;

    private Set<String> userTypes;

    private Type currentReturnType;

    public DefinitionChecker(CheckerConfig config) {
        super(config);
        variables = new ArrayList<Set<String>>();
        variables.add(new HashSet<String>());
        functions = new ArrayList<Set<String>>();
        functions.add(new HashSet<String>());
        userTypes = new HashSet<String>();
        currentReturnType = null;
    }

    protected Type getCurrentReturnType() {
        return currentReturnType;
    }

    private void addVariableLayer() {
        variables.add(new HashSet<String>());
    }

    private void removeVariableLayer() {
        variables.remove(variables.size() - 1);
    }

    private boolean testVariable(String name) {
        Set<String> mostRecent = variables.get(variables.size() - 1);
        return mostRecent.add(name);
    }

    private void addFunctionLayer() {
        functions.add(new HashSet<String>());
    }

    private void removeFunctionLayer() {
        functions.remove(new HashSet<String>());
    }

    private boolean testFunction(Function f) {
        String prototype = f.getName() + Strings.list(f.getParameterTypes());
        Set<String> mostRecent = functions.get(functions.size() - 1);
        return mostRecent.add(prototype);
    }

    private boolean isVoid(TypeNode tn) {
        if (!(tn instanceof BasicTypeNode)) {
            return false;
        }
        BasicTypeNode basic = (BasicTypeNode) tn;
        if (basic.getName().toString().equals("void")) {
            return true;
        }
        return false;
    }

    public void visitImport(Import d) {
        if (!program.unitExists(d.getUnitName())) {
            reporter.error(MISSING_IMPORT, d.getPlace(), d.getUnitName());
        }
    }

    public void visitMacro(Macro d) {
        reporter.error(BAD_MACRO, d.getPlace(), d.getName());
    }

    public void enterFunction(Function d) {
        if (Identifiers.isQualified(d.getName())) {
            reporter.error(BAD_FUNCTION_NAME, d.getPlace(), d.getName());
        }
        if (!testFunction(d)) {
            reporter.error(FUNCTION_DUPE, d.getPlace(), d.getName() + Strings.list(d.getParameterTypes()));
        }
        scope.enter();
        addVariableLayer();
        currentReturnType = isVoid(d.getReturnType()) ? null : manager.toType(unit, d.getReturnType());
        if (currentClass != null) {
            scope.addLocalVariable("this", currentClass);
        }
        for (TypedName p : d.getParameters()) {
            Type t = manager.toType(unit, p.getType());
            scope.addLocalVariable(p.getName(), t);
        }
    }

    private static Statement getLast(Block block) {
        for (int i = block.getStatements().size() - 1; i >= 0; i--) {
            Statement s = block.getStatements().get(i);
            if (!(s instanceof Comment)) {
                return s;
            }
        }
        return null;
    }

    public void exitFunction(Function d) {
        scope.exit();
        removeVariableLayer();
        if (isVoid(d.getReturnType())) {
            return;
        }
        Statement last = getLast(d.getBody());
        if ((last == null) || !last.terminates()) {
            reporter.error(NO_RETURN, d.getPlace(), d.getName());
        }
    }

    public void enterRecord(Record d) {
        if (Identifiers.isQualified(d.getName())) {
            reporter.error(BAD_RECORD_NAME, d.getPlace(), d.getName());
        }
        if (!userTypes.add(d.getName())) {
            reporter.error(RECORD_DUPE, d.getPlace(), d.getName());
        }
        addVariableLayer();
    }

    public void exitRecord(Record d) {
        removeVariableLayer();
    }

    public void enterClassDef(ClassDef d) {
        if (Identifiers.isQualified(d.getName())) {
            reporter.error(BAD_CLASS_NAME, d.getPlace(), d.getName());
        }
        if (!userTypes.add(d.getName())) {
            reporter.error(CLASS_DUPE, d.getPlace(), d.getName());
        }
        Path path = new Path(program, unit, unit, d);
        currentClass = manager.getMemberType(path);
        addVariableLayer();
        addFunctionLayer();
    }

    public void exitClassDef(ClassDef d) {
        currentClass = null;
        removeVariableLayer();
        removeFunctionLayer();
    }

    public void visitTypedName(TypedName d) {
        manager.toType(unit, d.getType());
        if (Identifiers.isQualified(d.getName())) {
            reporter.error(BAD_VARIABLE_NAME, d.getPlace(), d.getName());
            return;
        }
        if (!testVariable(d.getName())) {
            reporter.error(VARIABLE_DUPE, d.getPlace(), d.getName());
        }
    }

    public void visitUnit(Unit d) {
    }

    public void enterConstructor(Constructor d) {
    }

    public void exitConstructor(Constructor d) {
    }
}
