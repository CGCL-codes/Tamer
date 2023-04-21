package jgloss.dictionary;

/**
 * Interface describing a parameter of a search. Each search in a {@link Dictionary Dictionary}
 * takes a {@link SearchMode search mode} argument and a set of parameters specified by the search
 * mode. Instances of this interface describe a parameter type. Constant objects implementing
 * this interface are used to define "well known" parameters, which should have user interface
 * widgets to control their value.
 *
 * @author Michael Koch
 * @see StandardSearchParameter
 */
public interface SearchParameter {

    /**
     * Get the class which objects used as values of this parameter type must be instances of.
     */
    Class getParameterClass();

    /**
     * Return a short description of what this parameter controls. This could be as short as a single
     * word.
     */
    String getDescription();
}
