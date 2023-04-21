package br.edu.ufcg.msnlab.methods;

import java.util.List;
import br.edu.ufcg.msnlab.misc.Function;

/**
 * TODO
 * 
 * @author Alfeu Buriti
 * @author Brunno Guimar�es
 * 
 * @author Andrea Alves
 * @author Erick Moreno
 * 
 * @author Rodrigo Barbosa
 * @author Samuel de Barros
 *
 */
public interface Solver<T> {

    /**
	 * TODO
	 * @param funcion
	 * @param tolerance
	 * @return
	 */
    public List<T> solve(Function funcion, double tolerance);
}
