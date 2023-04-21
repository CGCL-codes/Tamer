package abc.weaving.aspectinfo;

import polyglot.util.Position;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import soot.*;

/** An intertype constructor declaration. 
  * 
  * given an intertype constructor declaration of the form
  *     mods A.new(formal1, ...,formaln) {
  * 		ccall(E1,E2,...,Ek);  // optional call to super or this
  *         init;
  *     }
  * 
  * the frontend transforms it into the following shape
  * 
  * 	mods A.new(formal1, ..., formaln) {
  *         qualifier.ccall(e1(this,formal1,...,formaln), ..., ek(this,formal1,...,formaln)); // no longer optional
  * 		body(this,formal1,...,formaln);
  *     }
  * where e1,...,ek and body are newly generated methods in the 
  * originating aspect.
  * 
  * The class below encodes the latter scheme, for code generation in the target class.
  * 
  * @author Aske Simon Christensen
  * @author Oege de Moor
  */
public class IntertypeConstructorDecl extends InAspect {

    public static int SUPER = 0;

    public static int THIS = 1;

    private AbcClass target;

    private int mod;

    private List formalTypes;

    private List throwTypes;

    private AbcClass qualifier;

    private int kind;

    private List arguments;

    private MethodSig body;

    private boolean hasMangleParam;

    private int origmod;

    public IntertypeConstructorDecl(AbcClass target, Aspect aspct, int mod, int origmod, boolean hasMangleParam, List formalTypes, List throwTypes, AbcClass qualifier, int kind, List arguments, MethodSig body, Position pos) {
        super(aspct, pos);
        this.target = target;
        this.mod = mod;
        this.origmod = origmod;
        this.formalTypes = formalTypes;
        this.throwTypes = throwTypes;
        this.qualifier = qualifier;
        this.kind = kind;
        this.arguments = arguments;
        this.body = body;
        this.hasMangleParam = hasMangleParam;
    }

    /** Get the target where of the intertype decl */
    public AbcClass getTarget() {
        return target;
    }

    /** Get the modifiers of intertype constructor */
    public int getModifiers() {
        return mod;
    }

    /** Get the modifiers of intertype constructor */
    public int getOriginalModifiers() {
        return origmod;
    }

    /** Does this constructor have an additional last parameter for mangling purposes? */
    public boolean hasMangleParam() {
        return hasMangleParam;
    }

    /** Get the formal types of the intertype constructor.
	   *  @return a list of {@link abc.weaving.aspectinfo.AbcType} objects.
	   */
    public List getFormalTypes() {
        return formalTypes;
    }

    List sexc;

    /** Get the exceptions thrown by the method.
	   *  @return a list of {@link soot.SootClass} objects.
	   */
    public List getExceptions() {
        if (sexc == null) {
            sexc = new ArrayList();
            Iterator ei = throwTypes.iterator();
            while (ei.hasNext()) {
                String e = (String) ei.next();
                sexc.add(Scene.v().getSootClass(e));
            }
        }
        return sexc;
    }

    public AbcClass getQualifier() {
        return qualifier;
    }

    public int getKind() {
        return kind;
    }

    public List getArguments() {
        return arguments;
    }

    public MethodSig getBody() {
        return body;
    }

    public String toString() {
        return (target + ".new(" + formalTypes + ") {...} from " + getAspect());
    }
}
