package polyglot.ext.jl5.types;

import java.util.Collections;
import java.util.List;
import polyglot.frontend.Job;
import polyglot.types.ClassDef;
import polyglot.types.ClassDef.Kind;
import polyglot.types.ClassType;
import polyglot.types.ClassType_c;
import polyglot.types.Flags;
import polyglot.types.Name;
import polyglot.types.Ref;
import polyglot.types.Resolver;
import polyglot.types.StructType;
import polyglot.types.Type;
import polyglot.types.TypeObject;
import polyglot.types.TypeSystem;
import polyglot.types.Types;
import polyglot.util.Position;

/**
 * 
 * A TypeVariable as 'T' in the type declaration Collection<T>
 *
 */
public class TypeVariable_c extends ClassType_c implements TypeVariable, SignatureType {

    protected Name name;

    protected Flags flags;

    protected Type lowerBound;

    protected IntersectionType upperBound;

    protected TVarDecl declaredIn;

    protected ClassType declaringClass;

    protected JL5ProcedureInstance declaringProcedure;

    public TypeVariable_c(TypeSystem ts, Position pos, Name id, Ref<? extends ClassDef> def, List<Ref<? extends Type>> bounds) {
        super(ts, pos, def);
        this.name = id;
        this.upperBound = ((JL5TypeSystem) ts).intersectionType(bounds);
        upperBound.boundOf(this);
        flags = Flags.NONE;
    }

    public void declaringProcedure(JL5ProcedureInstance pi) {
        declaredIn = TVarDecl.PROCEDURETV;
        declaringProcedure = pi;
        declaringClass = null;
    }

    public void declaringClass(ClassType ct) {
        declaredIn = TVarDecl.CLASSTV;
        declaringProcedure = null;
        declaringClass = ct;
    }

    public TVarDecl declaredIn() {
        if (declaredIn == null) {
            declaredIn = TVarDecl.SYNTHETICTV;
        }
        return declaredIn;
    }

    public ClassType declaringClass() {
        if (declaredIn.equals(TVarDecl.CLASSTV)) return declaringClass;
        return null;
    }

    public JL5ProcedureInstance declaringProcedure() {
        if (declaredIn.equals(TVarDecl.PROCEDURETV)) return declaringProcedure;
        return null;
    }

    public List<Type> bounds() {
        return upperBound().boundsTypes();
    }

    public void bounds(List<Ref<? extends Type>> b) {
        JL5TypeSystem ts = ((JL5TypeSystem) typeSystem());
        upperBound = ts.intersectionType(b);
        upperBound.boundOf(this);
    }

    public Kind kind() {
        return TYPEVARIABLE;
    }

    public ClassType outer() {
        return null;
    }

    public Name name() {
        return name;
    }

    public void name(Name name) {
        this.name = name;
    }

    public polyglot.types.Package package_() {
        if (TVarDecl.CLASSTV.equals(declaredIn)) {
            return declaringClass().package_();
        }
        if (TVarDecl.PROCEDURETV.equals(declaredIn)) {
            return ((ClassDef) declaringProcedure().def()).asType().package_();
        }
        return null;
    }

    public Flags flags() {
        return flags;
    }

    public List constructors() {
        return Collections.emptyList();
    }

    public List memberClasses() {
        return Collections.emptyList();
    }

    public List methods() {
        return Collections.emptyList();
    }

    public List fields() {
        return Collections.emptyList();
    }

    public List interfaces() {
        return Collections.emptyList();
    }

    public boolean inStaticContext() {
        return false;
    }

    public String translate(Resolver c) {
        return name().toString();
    }

    public String toString() {
        return name.toString();
    }

    public IntersectionType upperBound() {
        return upperBound;
    }

    public void upperBound(IntersectionType b) {
        upperBound = b;
        upperBound.boundOf(this);
    }

    public boolean equalsImpl(TypeObject other) {
        if (!(other instanceof TypeVariable)) return super.equalsImpl(other);
        TypeVariable arg2 = (TypeVariable) other;
        if (this.name.equals(arg2.name())) {
            if (declaredIn().equals(TVarDecl.SYNTHETICTV)) {
                return arg2.declaredIn().equals(TVarDecl.SYNTHETICTV);
            } else if (declaredIn().equals(TVarDecl.PROCEDURETV)) {
                return (arg2.declaredIn().equals(TVarDecl.PROCEDURETV)) && declaringProcedure() == arg2.declaringProcedure();
            } else if (declaredIn().equals(TVarDecl.CLASSTV)) {
                return (arg2.declaredIn().equals(TVarDecl.CLASSTV)) && ts.equals((TypeObject) declaringClass(), (TypeObject) arg2.declaringClass());
            }
            return true;
        }
        return false;
    }

    public boolean equivalentImpl(TypeObject other) {
        if (!(other instanceof TypeVariable)) return super.equalsImpl(other);
        TypeVariable arg2 = (TypeVariable) other;
        if (this.name.equals(arg2.name())) return true;
        return false;
    }

    public boolean isEquivalent(TypeObject arg2) {
        if (arg2 instanceof TypeVariable) {
            if (this.erasureType() instanceof ParameterizedType && ((TypeVariable) arg2).erasureType() instanceof ParameterizedType) {
                return typeSystem().equals((TypeObject) ((ParameterizedType) this.erasureType()).baseType(), (TypeObject) ((ParameterizedType) ((TypeVariable) arg2).erasureType()).baseType());
            } else {
                return typeSystem().equals((TypeObject) this.erasureType(), (TypeObject) ((TypeVariable) arg2).erasureType());
            }
        }
        return false;
    }

    public Type erasureType() {
        return ((JL5TypeSystem) typeSystem()).erasure(bounds().get(0));
    }

    public ClassType toClass() {
        return this;
    }

    public String signature() {
        return "T" + name + ";";
    }

    public Type lowerBound() {
        if (lowerBound == null) return typeSystem().Null();
        return lowerBound;
    }

    public void lowerBound(Type lowerBound) {
        this.lowerBound = lowerBound;
    }

    @Override
    public ClassType flags(Flags flags) {
        TypeVariable_c t = (TypeVariable_c) copy();
        t.flags = flags;
        return t;
    }

    @Override
    public ClassType container(StructType container) {
        assert false;
        return null;
    }

    @Override
    public Job job() {
        assert false;
        return null;
    }

    @Override
    public Type superClass() {
        return this.upperBound();
    }

    public boolean isTypeVariable() {
        return true;
    }
}
