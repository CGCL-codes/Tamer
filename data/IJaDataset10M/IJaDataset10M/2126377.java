package org.gzigzag;

/** A base class for referentially transparent functions of cell groups.
 * The idea of Function objects is to enable transparent optimization and caching
 * of values interpreted from the structure without losing clarity.
 * All Function objects <b>must</b> obey the following rule:
 * <pre>
 * 	For all Function f, Function g
 * 		if
 * 			f.equals(g)
 * 		then
 * 			For all X,
 * 				f(X).equals(g(X))
 * </pre>
 * Also, there must be absolutely NO side (detectable) side effects to
 * <pre>
 * 	For all Function f, Function g
 * 		For all X, Y
 * 			A = g(X)
 * 			B = f(Y)
 * 			C = g(X)
 * 			MUST RESULT IN A.equals(C)
 * </pre>
 * A side effect of caching some value <b>is</b> allowable if it does not alter
 * any evaluation results.
 * <p>
 * Functions are not allowed to modify the contents of their argument stepper.
 * <p>
 * This function class is the base. Due to Java's restrictions, we automatically
 * generate subclasses of the form Function_Type which have a method apply that 
 * returns an object of type Type.
 */
public interface Function {

    Object apply(Stepper s);
}
