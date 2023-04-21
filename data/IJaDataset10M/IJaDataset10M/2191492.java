package at.pollaknet.api.facile.symtab.symbols.meta;

import at.pollaknet.api.facile.symtab.symbols.Instance;
import at.pollaknet.api.facile.symtab.symbols.TypeRef;
import at.pollaknet.api.facile.util.Pair;

/**
 * <p/>The CustomSttribute class represents a custom attribute as specified in 
 * See ECMA 335 revision 4 - Partition II, 23.3 Custom attributes
 * <a href="http://www.ecma-international.org/publications/files/ECMA-ST/Ecma-335.pdf#page=288&view=FitH">
 * http://www.ecma-international.org/publications/files/ECMA-ST/Ecma-335.pdf</a>.
 * 
 * <p/>The following sample shows a abstracted custom attribute in C#:
 * {@code [MyAttribute(AFixedArgument, ANamedFiled = "test", ANamedProperty = true)]}
 *
 * @author Thomas Pollak
 * <p/>Email: <i>http://code.google.com/p/facile-api/people/detail?u=103590059941737035763</i>
 */
public interface CustomAttribute {

    /**
	 * Returns the the fixed arguments (mandatory) of a custom attribute as array of
	 * {@link at.pollaknet.api.facile.symtab.symbols.Instance}.
	 * @return The fixed arguments.
	 */
    public abstract Instance[] getFixedArguments();

    /**
	 * Returns the named fields of a custom attribute as
	 * {@link at.pollaknet.api.facile.util.Pair}<String, Instance> array containing the name as string and
	 * the value of that named field as {@link at.pollaknet.api.facile.symtab.symbols.Instance}.
	 * @return The named fields of the custom attribute.
	 */
    public abstract Pair<String, Instance>[] getNamedFields();

    /**
	 * Returns the named properties of a custom attribute as
	 * {@link at.pollaknet.api.facile.util.Pair}<String, Instance> array containing the name as string and
	 * the value of that named properties as {@link at.pollaknet.api.facile.symtab.symbols.Instance}.
	 * @return The named properties of the custom attribute.
	 */
    public abstract Pair<String, Instance>[] getNamedProperties();

    /**
	 * Returns the {@link at.pollaknet.api.facile.symtab.symbols.TypeRef} or
	 * {@link at.pollaknet.api.facile.symtab.symbols.Type} of the custom attribute.
	 * @return The custom attribute class.
	 */
    public abstract TypeRef getTypeRef();

    public abstract boolean equals(Object obj);

    public abstract int hashCode();

    int compareTo(CustomAttribute a);
}
