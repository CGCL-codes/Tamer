package net.sourceforge.nrl.parser.ast.constraints;

/**
 * A fragment application is used inside a rule to obtain the value of a
 * fragment. It either applies to the current context, or optionally takes a
 * model reference parameter.
 * 
 * @author Christian Nentwich
 */
public interface IValidationFragmentApplication extends IExpression {

    /**
	 * Return the name of the fragment to check. Use {@link #getFragment()} to
	 * return the actual fragment.
	 * 
	 * @return the fragment name
	 */
    public String getFragmentName();

    /**
	 * Return the fragment to check. Never returns null.
	 * 
	 * @return the fragment
	 */
    public IValidationFragmentDeclaration getFragment();

    /**
	 * Return a parameter. The parameter index must be between 0 and
	 * {@link #getNumParameters()}-1, or a runtime exception will occur.
	 * 
	 * @param index the index
	 * @return the parameter, will not be null if the index was in bounds
	 */
    public IExpression getParameter(int index);

    /**
	 * Return the number of parameters passed to this property.
	 */
    public int getNumParameters();
}
