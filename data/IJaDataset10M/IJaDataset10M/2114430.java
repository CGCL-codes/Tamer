package net.sf.refactorit.classmodel.references;

import net.sf.refactorit.classmodel.BinPackage;
import net.sf.refactorit.classmodel.Project;

/**
 *
 * @author Arseni Grigorjev
 */
public class BinPackageReference extends BinItemReference {

    protected final String qName;

    public BinPackageReference(final BinPackage pack) {
        qName = pack.getQualifiedName();
    }

    public Object findItem(Project project) {
        return project.createPackageForName(qName);
    }
}
