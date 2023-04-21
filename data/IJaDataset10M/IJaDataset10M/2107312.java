package joptsimple;

import java.util.Collection;
import java.util.List;

/**
 * <p>Describes options that an option parser recognizes.</p>
 *
 * <p>Instances of this interface are returned by the "fluent interface" methods to allow
 * retrieval of option arguments in a type-safe manner.  Here's an example:</p>
 * <pre><code>
 *     OptionParser parser = new OptionParser();
 *     <strong>OptionSpec&lt;Integer&gt;</strong> count =
 *         parser.accepts( "count" ).withRequiredArg().ofType( Integer.class );
 *     OptionSet options = parser.parse( "--count", "2" );
 *     assert options.has( count );
 *     int countValue = options.valueOf( count );
 *     assert countValue == count.value( options );
 *     List&lt;Integer&gt; countValues = options.valuesOf( count );
 *     assert countValues.equals( count.values( options ) );
 * </code></pre>
 *
 * @param <V> represents the type of the arguments this option accepts
 * @author <a href="mailto:pholser@alumni.rice.edu">Paul Holser</a>
 * @version $Id: OptionSpec.java,v 1.21 2009/04/07 00:21:24 pholser Exp $
 */
public interface OptionSpec<V> {

    /**
     * <p>Gives any arguments associated with the given option in the given set of
     * detected options.</p>
     *
     * @param detectedOptions the detected options to search in
     * @return the arguments associated with this option; an empty list if no such
     * arguments are present, or if this option was not detected
     * @throws OptionException if there is a problem converting this option's arguments
     * to the desired type; for example, if the type does not implement a correct
     * conversion constructor or method
     * @throws NullPointerException if {@code detectedOptions} is {@code null}
     */
    List<V> values(OptionSet detectedOptions);

    /**
     * <p>Gives the argument associated with the given option in the given set of
     * detected options.</p>
     *
     * @param detectedOptions the detected options to search in
     * @return the argument of the this option; {@code null} if no argument is present,
     * or that option was not detected
     * @throws OptionException if more than one argument was detected for the option
     * @throws NullPointerException if {@code detectedOptions} is {@code null}
     * @throws ClassCastException if the arguments of this option are not of the
     * expected type
     */
    V value(OptionSet detectedOptions);

    /**
     * @return the string representations of this option
     */
    Collection<String> options();
}
