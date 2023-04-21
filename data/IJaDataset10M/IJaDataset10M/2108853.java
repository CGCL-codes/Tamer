package net.sf.bootstrap.metamodel.uml;

import java.util.ArrayList;
import java.util.Collection;
import net.sf.bootstrap.metamodel.facade.IModel;

/**
 * <p>Represents the UML model.</p>
 *
 * @author Mark Moloney
 */
public class Model extends MObject implements IModel {

    private Collection packages;

    public Model() {
        super();
    }

    public Model(Long id) {
        super(id);
    }

    public Collection getPackages() {
        return packages;
    }

    public void addPackage(MPackage pkg) {
        if (packages == null) {
            packages = new ArrayList();
        }
        packages.add(pkg);
    }

    public void addDataType(MDataType dataType) {
        ModelContext.getContext().addDataType(dataType);
    }

    public void addStereotype(MStereotype stereotype) {
        ModelContext.getContext().addStereotype(stereotype);
    }

    public void addTagDefinition(TagDefinition tag) {
        ModelContext.getContext().addTagDefinition(tag);
    }

    public void addGeneralization(Generalization generalization) {
        ModelContext.getContext().addGeneralization(generalization);
    }

    public void init() {
        MPackage lang = new MPackage();
        lang.setName("lang");
        MPackage java = new MPackage();
        java.setName("java");
        java.addPackage(lang);
        this.addPackage(java);
    }
}
