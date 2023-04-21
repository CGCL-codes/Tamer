package net.sf.kmoviecataloger.genericstructures;

import net.sf.kmoviecataloger.cataloger.Movie;

/**
 *
 * @author Knitter
 */
public class CompareMovieByOriginalTitleInfinit extends CompareMovieByOriginalTitle implements ComparacaoInfimo {

    private static final Movie INFIMO = new Movie("zzzzzzzzzzzzzzzzzzzzzzzzzzz");

    public Object infimo() {
        return INFIMO;
    }
}
