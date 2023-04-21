package gnu.xml.validation.xmlschema;

import gnu.xml.validation.datatype.Annotation;
import gnu.xml.validation.datatype.Type;
import javax.xml.namespace.QName;

/**
 * An XML Schema element declaration schema component.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
class ElementDeclaration {

    /**
   * The name of the element to which this declaration refers.
   */
    final QName name;

    /**
   * The type definition corresponding to this element.
   */
    Type datatype;

    /**
   * The scope of this schema component.
   * One of GLOBAL, LOCAL, or ABSENT.
   */
    final int scope;

    /**
   * If scope is LOCAL, the parent element definition.
   */
    final ElementDeclaration parent;

    /**
   * The constraint type.
   * One of NONE, DEFAULT, FIXED.
   */
    final int type;

    /**
   * The value constraint.
   */
    final String value;

    final boolean nillable;

    final QName substitutionGroup;

    final int substitutionGroupExclusions;

    final int disallowedSubstitutions;

    final boolean isAbstract;

    /**
   * The annotation associated with this attribute declaration, if any.
   */
    Annotation annotation;

    ElementDeclaration(QName name, Type datatype, int scope, ElementDeclaration parent, int type, String value, boolean nillable, QName substitutionGroup, int substitutionGroupExclusions, int disallowedSubstitutions, boolean isAbstract) {
        this.name = name;
        this.datatype = datatype;
        this.scope = scope;
        this.parent = parent;
        this.type = type;
        this.value = value;
        this.nillable = nillable;
        this.substitutionGroup = substitutionGroup;
        this.substitutionGroupExclusions = substitutionGroupExclusions;
        this.disallowedSubstitutions = disallowedSubstitutions;
        this.isAbstract = isAbstract;
    }
}
