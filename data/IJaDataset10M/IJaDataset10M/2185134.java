package bsh;

class BlockNameSpace extends NameSpace {

    public BlockNameSpace(NameSpace parent) throws EvalError {
        super(parent, parent.getName() + "/BlockNameSpace");
    }

    public void setVariable(String name, Object value, boolean strictJava, boolean recurse) throws UtilEvalError {
        if (weHaveVar(name)) super.setVariable(name, value, strictJava, false); else getParent().setVariable(name, value, strictJava, recurse);
    }

    /**
		Set an untyped variable in the block namespace.
		The BlockNameSpace would normally delegate this set to the parent.
		Typed variables are naturally set locally.
		This is used in try/catch block argument. 
	*/
    public void setBlockVariable(String name, Object value) throws UtilEvalError {
        super.setVariable(name, value, false, false);
    }

    /**
		We have the variable: either it was declared here with a type, giving
		it block local scope or an untyped var was explicitly set here via
		setBlockVariable().
	*/
    private boolean weHaveVar(String name) {
        try {
            return super.getVariableImpl(name, false) != null;
        } catch (UtilEvalError e) {
            return false;
        }
    }

    /** do we need this? */
    private NameSpace getNonBlockParent() {
        NameSpace parent = super.getParent();
        if (parent instanceof BlockNameSpace) return ((BlockNameSpace) parent).getNonBlockParent(); else return parent;
    }

    /**
		Get a 'this' reference is our parent's 'this' for the object closure.
		e.g. Normally a 'this' reference to a BlockNameSpace (e.g. if () { } )
		resolves to the parent namespace (e.g. the namespace containing the
		"if" statement). 
		@see #getBlockThis( Interpreter )
	*/
    This getThis(Interpreter declaringInterpreter) {
        return getNonBlockParent().getThis(declaringInterpreter);
    }

    /**
		super is our parent's super
	*/
    public This getSuper(Interpreter declaringInterpreter) {
        return getNonBlockParent().getSuper(declaringInterpreter);
    }

    /**
		delegate import to our parent
	*/
    public void importClass(String name) {
        getParent().importClass(name);
    }

    /**
		delegate import to our parent
	*/
    public void importPackage(String name) {
        getParent().importPackage(name);
    }

    public void setMethod(String name, BshMethod method) throws UtilEvalError {
        getParent().setMethod(name, method);
    }
}
