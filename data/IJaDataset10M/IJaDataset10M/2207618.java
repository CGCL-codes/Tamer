package org.opengis.wrapper.proj4;

import java.util.Set;
import java.util.Collection;
import java.util.Collections;
import org.opengis.util.GenericName;
import org.opengis.util.InternationalString;
import org.opengis.metadata.extent.Extent;
import org.opengis.referencing.IdentifiedObject;
import org.opengis.referencing.ReferenceIdentifier;

/**
 * Base class of GeoAPI implementations in this Proj4 wrappers package. This class defines
 * empty skeleton for the GeoAPI methods that we do not support in this package.
 *
 * @author  Martin Desruisseaux (Geomatys)
 * @version 3.1
 * @since   3.1
 */
class PJObject implements IdentifiedObject {

    /**
     * The name of this referencing object, or {@code null} if none.
     */
    final ReferenceIdentifier name;

    /**
     * The aliases, or an empty list if none.
     */
    final Collection<GenericName> aliases;

    /**
     * Creates a new identified object of the given name.
     *
     * @param identifier The name of the new object, or {@code null} if none.
     */
    PJObject(final ReferenceIdentifier name) {
        this.name = name;
        this.aliases = Collections.emptyList();
    }

    /**
     * Creates a new identified object of the given name and aliases.
     *
     * @param identifier The name of the new object, or {@code null} if none.
     */
    PJObject(final ReferenceIdentifier name, final Collection<GenericName> aliases) {
        this.name = name;
        this.aliases = aliases;
    }

    /**
     * Creates a new identified object as a copy of the given one.
     */
    PJObject(final IdentifiedObject object) {
        name = object.getName();
        aliases = object.getAlias();
    }

    /**
     * Returns the name of this referencing object, or {@code null} if none.
     * Note that this attribute is mandatory according ISO 19111, but this
     * simple Proj.4 wrapper is lenient about that.
     */
    @Override
    public ReferenceIdentifier getName() {
        return name;
    }

    /**
     * Returns the name in a singleton set, since we don't have anything better for identifiers.
     * The objects created by {@link PJFactory} will use {@code "EPSG:xxxx"} identifiers, so this
     * is rather the name which is quite inaccurate.
     */
    @Override
    public Set<ReferenceIdentifier> getIdentifiers() {
        return Collections.singleton(name);
    }

    /**
     * Returns the aliases, or an empty set if none. The aliases are determined from
     * the {@code "parameter-names.txt"} or {@code "projection-names.txt"} resources
     * files.
     */
    @Override
    public Collection<GenericName> getAlias() {
        return aliases;
    }

    /**
     * Returns {@code null} since we do not define any scope in our Proj4 wrappers.
     * Note that this method is not inherited from {@link IdentifiedObject}, but is
     * defined in sub-interfaces like {@link org.opengis.referencing.crs.SingleCRS}.
     */
    public InternationalString getScope() {
        return null;
    }

    /**
     * Returns {@code null} since we do not define any domain of validity.
     * Note that this method is not inherited from {@link IdentifiedObject}, but is
     * defined in sub-interfaces like {@link org.opengis.referencing.crs.SingleCRS}.
     */
    public Extent getDomainOfValidity() {
        return null;
    }

    /**
     * Returns {@code null} since there is no remarks associated with our Proj4 wrappers.
     */
    @Override
    public InternationalString getRemarks() {
        return null;
    }

    /**
     * Throws unconditionally the exception since we do not support WKT formatting.
     */
    @Override
    public String toWKT() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns a string representation of this object, mostly for debugging purpose.
     * This string representation may change in any future version.
     */
    @Override
    public String toString() {
        return getClass().getSimpleName() + '[' + name + ']';
    }
}
