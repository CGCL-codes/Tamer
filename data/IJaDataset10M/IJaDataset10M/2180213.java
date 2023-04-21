package de.uni_muenster.cs.sev.lethal.symbol.common;

/**
 * Special variable for use in tree homomorphisms.<br>
 * The homomorphism is given by a function hom : F -> {variable trees
 * containing variables and symbols of the alphabet G}, where each symbol f of
 * arity n is assigned a tree with symbols of G and at most n variables
 * x_0,...,x_{n-1}. So each variable has a concrete number.<br>
 * The number i is used to find the i-th tree or tree automaton in a list and
 * replace the variable with number i by the tree or the language given by the
 * automaton.
 * 
 * @author Dorothea, Irene. Martin
 */
public interface Variable extends Symbol {

    /**
	 * The number describing the position in a list of objects (trees or tree
	 * automata) which shall replace the variables.<br>
	 * 
	 * @return the componentNumber of the represented variable
	 */
    public int getComponentNumber();
}
