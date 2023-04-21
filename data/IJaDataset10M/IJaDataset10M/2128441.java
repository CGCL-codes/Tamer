package koala.dynamicjava.tree;

import java.util.*;
import koala.dynamicjava.tree.visitor.*;

/**
 * This class represents the reference type nodes of the syntax tree
 *
 * @author  Stephane Hillion
 * @version 1.0 - 1999/04/24
 */
public class ReferenceTypeName extends TypeName {

    /**
   * The representation of this type
   */
    private String representation;

    /**
   * The representation of this type
   */
    private List<? extends IdentifierToken> identifiers;

    /**
   * Initializes the type
   * @param ids   the list of the tokens that compose the type name
   * @exception IllegalArgumentException if ids is null or empty
   */
    public ReferenceTypeName(List<? extends IdentifierToken> ids) {
        this(ids, SourceInfo.NONE);
    }

    public ReferenceTypeName(IdentifierToken... ids) {
        this(Arrays.asList(ids));
    }

    public ReferenceTypeName(String... names) {
        this(stringsToIdentifiers(names));
    }

    public ReferenceTypeName(String[] names, SourceInfo si) {
        this(Arrays.asList(stringsToIdentifiers(names)), si);
    }

    private static IdentifierToken[] stringsToIdentifiers(String[] names) {
        IdentifierToken[] ids = new IdentifierToken[names.length];
        for (int i = 0; i < names.length; i++) {
            ids[i] = new Identifier(names[i]);
        }
        return ids;
    }

    /**
   * Initializes the type
   * @param ids   the list of the tokens that compose the type name
   * @exception IllegalArgumentException if ids is null or empty
   */
    public ReferenceTypeName(List<? extends IdentifierToken> ids, SourceInfo si) {
        super(si);
        if (ids == null) throw new IllegalArgumentException("ids == null");
        if (ids.size() == 0) throw new IllegalArgumentException("ids.size() == 0");
        identifiers = ids;
        representation = TreeUtilities.listToName(ids);
    }

    /**
   * Returns the representation of this type
   */
    public String getRepresentation() {
        return representation;
    }

    /**
   * Returns the list of identifiers that make up this type
   */
    public List<? extends IdentifierToken> getIdentifiers() {
        return identifiers;
    }

    /**
   * Sets the identifiers of this type
   * @exception IllegalArgumentException if ids is null or empty
   */
    public void setIdentifiers(List<? extends IdentifierToken> ids) {
        if (ids == null) throw new IllegalArgumentException("ids == null");
        if (ids.size() == 0) throw new IllegalArgumentException("ids.size() == 0");
        identifiers = ids;
        representation = TreeUtilities.listToName(ids);
    }

    /**
   * Allows a visitor to traverse the tree
   * @param visitor the visitor to accept
   */
    public <T> T acceptVisitor(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    /**
   * Implementation of toString for use in unit testing
   */
    public String toString() {
        return "(" + getClass().getName() + ": " + toStringHelper() + ")";
    }

    protected String toStringHelper() {
        return getRepresentation();
    }
}
