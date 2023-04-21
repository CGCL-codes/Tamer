package sketch.specs.symbc;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import sketch.ast.ASTUtils;
import sketch.main.SketchTestOptions;
import sketch.ounit.OUnit;
import sketch.specs.SymbolicValueVisitor;
import sketch.specs.WriteJPFSymbolicTestDriver;
import sketch.util.Checker;

public class SymbolicExecDriverCreator {

    public static final String SYMBOLIC = "_symbolic";

    public final CompilationUnit unit;

    public final TypeDeclaration type;

    public final PackageDeclaration package_decl;

    public final String outputDir;

    private List<MethodDeclaration> methodsNeedDrivers = new LinkedList<MethodDeclaration>();

    private Map<MethodDeclaration, List<MethodInvocation>> methodsAndSymbolicValues = new LinkedHashMap<MethodDeclaration, List<MethodInvocation>>();

    private Map<MethodDeclaration, TypeDeclaration> methodsAndTypes = new LinkedHashMap<MethodDeclaration, TypeDeclaration>();

    private Map<MethodDeclaration, MethodDeclaration> symbolic_concrete_method_map = new LinkedHashMap<MethodDeclaration, MethodDeclaration>();

    public SymbolicExecDriverCreator(CompilationUnit unit) {
        this(unit, SketchTestOptions.output_dir);
    }

    public SymbolicExecDriverCreator(CompilationUnit unit, String outputDir) {
        Checker.checkNull(unit, "The unit is not null.");
        Checker.checkTrue(unit.types().size() == 1, "There should be only one type in unit.");
        this.unit = unit;
        this.outputDir = outputDir;
        Checker.checkTrue(unit.types().size() == 1, "The size of types in compilation unit should 1.");
        this.type = (TypeDeclaration) unit.types().get(0);
        this.package_decl = unit.getPackage();
        File f = new File(this.outputDir);
        Checker.checkTrue(f.isDirectory() && f.exists(), "The file should be a dir");
        this.traverse_unit();
        List<AbstractTypeDeclaration> types = this.unit.types();
        for (AbstractTypeDeclaration type : types) {
            type.getName().setIdentifier(type.getName().toString() + SYMBOLIC);
        }
    }

    private void traverse_unit() {
        List<MethodDeclaration> allmethods = ASTUtils.getMethodsWithAnnotation(unit, OUnit.class.getName());
        for (MethodDeclaration method : allmethods) {
            MethodDeclaration method_back_with_symbolic = ASTUtils.deepClone(method);
            SymbolicValueVisitor visitor = new SymbolicValueVisitor();
            method.accept(visitor);
            if (visitor.getNumOfReplace() > 0) {
                this.methodsNeedDrivers.add(method_back_with_symbolic);
                this.symbolic_concrete_method_map.put(method_back_with_symbolic, method);
                TypeDeclaration t = ASTUtils.findOutType(unit, method);
                Checker.checkNull(t, "The type can not be null for method: " + method.getName());
                this.methodsAndTypes.put(method_back_with_symbolic, t);
                Checker.checkTrue(!this.methodsAndSymbolicValues.containsKey(method_back_with_symbolic), "The map should not cotnain method: " + method_back_with_symbolic.getName());
                this.methodsAndSymbolicValues.put(method_back_with_symbolic, visitor.getSymbolicIntDeclarationsAsList());
            }
        }
    }

    public CompilationUnit getUnit() {
        return unit;
    }

    public String getOutputDir() {
        return outputDir;
    }

    public boolean hasSetUp() {
        return hasMethodByName("setUp");
    }

    public boolean hasTearDown() {
        return hasMethodByName("tearDown");
    }

    private boolean hasMethodByName(String name) {
        List<MethodDeclaration> methods = ASTUtils.getAllMethods(this.unit);
        for (MethodDeclaration method : methods) {
            if (method.getName().toString().equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
	 * The output includes:
	 * 1. method -> file name
	 * 2. method -> symbolic name
	 * */
    public List<MethodDeclaration> getMethodDeclarationsForTestDriver() {
        return this.methodsNeedDrivers;
    }

    public Map<MethodDeclaration, List<MethodInvocation>> getMethodsAndSymbolicValues() {
        return this.methodsAndSymbolicValues;
    }

    public List<MethodInvocation> getSymbolicValuesForMethod(MethodDeclaration m) {
        if (!this.methodsAndSymbolicValues.containsKey(m)) {
            return new LinkedList<MethodInvocation>();
        } else {
            return this.methodsAndSymbolicValues.get(m);
        }
    }

    public TypeDeclaration getTypeDeclarationForMethod(MethodDeclaration m) {
        return this.methodsAndTypes.get(m);
    }

    public MethodDeclaration getOriginalMethodDeclaration(MethodDeclaration m) {
        return this.symbolic_concrete_method_map.get(m);
    }

    public List<File> createExecDriver(String[] extraImports) {
        return createExecDriver(extraImports, false, false);
    }

    public List<File> createExecDriver(String[] extraImports, boolean hasSetUp, boolean hasTearDown) {
        List<MethodDeclaration> methodsForTestDriver = this.getMethodDeclarationsForTestDriver();
        String[] methods = new String[methodsForTestDriver.size()];
        for (int i = 0; i < methods.length; i++) {
            methods[i] = methodsForTestDriver.get(i).getName().getIdentifier();
        }
        return WriteJPFSymbolicTestDriver.writeTestDriver(this.package_decl, type, this.outputDir, methods, null, extraImports, hasSetUp, hasTearDown);
    }

    public static String removeSymbolic(String str) {
        if (str == null) {
            return str;
        }
        int index = str.indexOf(SYMBOLIC);
        if (index != -1) {
            return str.substring(0, index);
        }
        return str;
    }
}
