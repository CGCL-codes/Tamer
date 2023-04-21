package abc.weaving.matching;

import java.util.*;
import soot.*;
import soot.jimple.*;
import soot.util.Chain;
import soot.tagkit.Host;
import polyglot.util.InternalCompilerError;
import abc.weaving.aspectinfo.*;
import abc.weaving.residues.*;
import abc.soot.util.Restructure;
import abc.weaving.weaver.*;

/** An execution join point shadow.
 *  abc does a front-end transformation that means that static initialization
 *  and advice execution join point shadows are also treated as execution shadows
 *  @author Ganesh Sittampalam
 *  @author Ondrej Lhotak
 */
public class ExecutionShadowMatch extends BodyShadowMatch {

    public ShadowMatch inline(ConstructorInliningMap cim) {
        ShadowMatch ret = cim.map(this);
        if (ret != null) return ret;
        if (cim.inlinee() != container) throw new InternalCompilerError("inlinee " + cim.inlinee() + " doesn't match container " + container);
        ret = construct(cim.target());
        cim.add(this, ret);
        if (sp != null) ret.sp = sp.inline(cim);
        return ret;
    }

    private ExecutionShadowMatch(SootMethod container) {
        super(container);
    }

    private static Hashtable esms = new Hashtable();

    public static void reset() {
        esms = new Hashtable();
    }

    static ExecutionShadowMatch construct(SootMethod container) {
        if (esms.containsKey(container)) return (ExecutionShadowMatch) esms.get(container);
        ExecutionShadowMatch esm = new ExecutionShadowMatch(container);
        esms.put(container, esm);
        return esm;
    }

    public static ExecutionShadowMatch matchesAt(MethodPosition pos) {
        if (!(pos instanceof WholeMethodPosition)) return null;
        if (abc.main.Debug.v().traceMatcher) System.err.println("Execution");
        return construct(pos.getContainer());
    }

    public List getExceptions() {
        return container.getExceptions();
    }

    public SJPInfo makeSJPInfo() {
        String jpKind;
        String sigClass;
        String sigMethod;
        String sig;
        if (isStaticInitializer()) {
            jpKind = "staticinitialization";
            sigClass = "org.aspectj.lang.reflect.InitializerSignature";
            sigMethod = "makeInitializerSig";
            sig = AbcSJPInfo.makeStaticInitializerSigData(container);
        } else if (isConstructor()) {
            jpKind = "constructor-execution";
            sigClass = "org.aspectj.lang.reflect.ConstructorSignature";
            sigMethod = "makeConstructorSig";
            sig = AbcSJPInfo.makeConstructorSigData(container);
        } else if (isAdviceBody()) {
            jpKind = "advice-execution";
            sigClass = "org.aspectj.lang.reflect.AdviceSignature";
            sigMethod = "makeAdviceSig";
            sig = AbcSJPInfo.makeAdviceSigData(container);
        } else {
            jpKind = "method-execution";
            sigClass = "org.aspectj.lang.reflect.MethodSignature";
            sigMethod = "makeMethodSig";
            sig = AbcSJPInfo.makeMethodSigData(container);
        }
        return abc.main.Main.v().getAbcExtension().createSJPInfo(jpKind, sigClass, sigMethod, sig, getHost());
    }

    private boolean isStaticInitializer() {
        return container.getName().equals(SootMethod.staticInitializerName);
    }

    private boolean isConstructor() {
        return container.getName().equals(SootMethod.constructorName);
    }

    private boolean isAdviceBody() {
        return MethodCategory.adviceBody(container);
    }

    protected AdviceApplication doAddAdviceApplication(MethodAdviceList mal, AbstractAdviceDecl ad, Residue residue) {
        ExecutionAdviceApplication aa = new ExecutionAdviceApplication(ad, residue);
        mal.addBodyAdvice(aa);
        return aa;
    }

    public ContextValue getReturningContextValue() {
        if (container.getName().equals(SootMethod.staticInitializerName) || container.getName().equals(SootMethod.constructorName)) return super.getReturningContextValue();
        Stmt nop = Restructure.restructureReturn(container);
        Chain units = container.getActiveBody().getUnits();
        Stmt ret = (Stmt) units.getSuccOf(nop);
        if (ret instanceof ReturnVoidStmt) return super.getReturningContextValue(); else if (ret instanceof ReturnStmt) return new JimpleValue((Immediate) ((ReturnStmt) ret).getOp()); else throw new RuntimeException("restructureReturn didn't restructure returns correctly: " + ret.getClass() + "(" + ret + ")");
    }

    public boolean supportsAround() {
        if (isStaticInitializer() && container.getDeclaringClass().isInterface()) return false;
        return true;
    }

    public String joinpointName() {
        if (isStaticInitializer()) return container.getDeclaringClass().isInterface() ? "interface static initialization" : "static initialization";
        if (isConstructor()) return "constructor execution";
        if (isAdviceBody()) return "advice execution";
        return "method execution";
    }
}
