package L1;

import L1.NamedElement;
import java.util.List;
import L1.ElementImport;
import L1.PackageImport;
import L1.Constraint;
import L1.PackageableElement;

/**
 * A namespace is an element in a model that contains a set of named elements that can be identified by name.
 */
public interface Namespace extends NamedElement {

    /**
    * References the ElementImports owned by the Namespace.
    */
    public abstract List<ElementImport> getElementImport();

    /**
    * References the ElementImports owned by the Namespace.
    */
    public abstract void setElementImport(List<ElementImport> elementimport);

    /**
    * References the PackageImports owned by the Namespace.
    */
    public abstract List<PackageImport> getPackageImport();

    /**
    * References the PackageImports owned by the Namespace.
    */
    public abstract void setPackageImport(List<PackageImport> packageimport);

    /**
    * Specifies a set of Constraints owned by this Namespace.
    */
    public abstract List<Constraint> getOwnedRule();

    /**
    * Specifies a set of Constraints owned by this Namespace.
    */
    public abstract void setOwnedRule(List<Constraint> ownedrule);

    /**
    * A collection of NamedElements identifiable within the Namespace, either by being owned or by being introduced by importing or inheritance.
    */
    public abstract List<NamedElement> getMember();

    /**
    * A collection of NamedElements identifiable within the Namespace, either by being owned or by being introduced by importing or inheritance.
    */
    public abstract void setMember(List<NamedElement> member);

    /**
    * References the PackageableElements that are members of this Namespace as a result of either PackageImports or ElementImports.
    */
    public abstract List<PackageableElement> getImportedMember();

    /**
    * References the PackageableElements that are members of this Namespace as a result of either PackageImports or ElementImports.
    */
    public abstract void setImportedMember(List<PackageableElement> importedmember);

    /**
    * A collection of NamedElements owned by the Namespace.
    */
    public abstract List<NamedElement> getOwnedMember();

    /**
    * A collection of NamedElements owned by the Namespace.
    */
    public abstract void setOwnedMember(List<NamedElement> ownedmember);
}
