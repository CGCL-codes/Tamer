package org.adapit.wctoolkit.uml.classes.kernel;

import java.io.Serializable;

@SuppressWarnings({ "serial" })
public class PackageImport extends DirectedRelationship implements Serializable {

    private VisibilityKind visibility;

    private Namespace importingNamespace;

    private org.adapit.wctoolkit.uml.ext.core.Package importedPackage;

    public PackageImport() {
    }

    public org.adapit.wctoolkit.uml.ext.core.Package getImportedPackage() {
        return importedPackage;
    }

    public void setImportedPackage(org.adapit.wctoolkit.uml.ext.core.Package importedPackage) {
        this.importedPackage = importedPackage;
    }

    public Namespace getImportingNamespace() {
        return importingNamespace;
    }

    public void setImportingNamespace(Namespace importingNamespace) {
        this.importingNamespace = importingNamespace;
    }

    public VisibilityKind getVisibility() {
        return visibility;
    }

    public void setVisibility(VisibilityKind visibility) {
        this.visibility = visibility;
    }
}
