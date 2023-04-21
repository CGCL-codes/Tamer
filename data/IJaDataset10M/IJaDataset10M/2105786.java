package net.sf.refactorit.transformations.view;

import net.sf.refactorit.classmodel.BinCIType;
import net.sf.refactorit.classmodel.BinMember;
import net.sf.refactorit.classmodel.BinPackage;
import net.sf.refactorit.classmodel.BinTypeRef;
import net.sf.refactorit.source.SourceHolder;
import net.sf.refactorit.transformations.view.triads.NameTriad;
import net.sf.refactorit.transformations.view.triads.OwnerTriad;
import net.sf.refactorit.transformations.view.triads.PackageTriad;
import net.sf.refactorit.transformations.view.triads.SourceHolderTriad;
import java.util.List;

/**
 *  Part of the frozen Transformation Framework. Is a wrapper object for
 *  {@link ProjectView}, that provides many useful methods for performing
 *  different queries.
 *
 * @author  Arseni Grigorjev
 */
public class ProjectViewQuery {

    private ProjectView projectView;

    public ProjectViewQuery(ProjectView projectView) {
        this.projectView = projectView;
    }

    public SourceHolder getSourceHolder(BinTypeRef typeRef) {
        return getSourceHolder(typeRef.getBinCIType());
    }

    public SourceHolder getSourceHolder(BinCIType citype) {
        BinTypeRef ownerType = getOwner(citype);
        if (ownerType != null) {
            return getSourceHolder(ownerType);
        } else {
            List triads = projectView.getTriads(citype, SourceHolderTriad.class, null);
            if (triads.size() > 0) {
                return (SourceHolder) ((Triad) triads.get(triads.size() - 1)).getObject();
            } else {
                return citype.getCompilationUnit();
            }
        }
    }

    public BinPackage getPackage(SourceHolder holder) {
        List triads = projectView.getTriads(holder, PackageTriad.class, null);
        if (triads.size() > 0) {
            Triad lastTriad = (Triad) triads.get(triads.size() - 1);
            return (BinPackage) lastTriad.getObject();
        } else {
            return holder.getPackage();
        }
    }

    public BinPackage getPackage(BinCIType type) {
        final SourceHolder sourceHolder = getSourceHolder(type);
        if (sourceHolder == null) {
            return type.getPackage();
        }
        return getPackage(sourceHolder);
    }

    public BinPackage getPackage(BinTypeRef binTypeRef) {
        return getPackage(binTypeRef.getBinCIType());
    }

    public String getName(BinMember member) {
        final String newName = findNewNameFor(member);
        return (newName != null) ? newName : member.getName();
    }

    public String getQualifiedName(BinPackage pack) {
        final String newName = findNewNameFor(pack);
        return (newName != null) ? newName : pack.getQualifiedDisplayName();
    }

    public String getQualifiedName(BinCIType type) {
        String packageName = getQualifiedName(getPackage(type));
        String typeName = getName(type);
        BinTypeRef owner = getOwner(type);
        if (owner == null) {
            return packageName + "." + typeName;
        } else {
            String ownerName = getName(owner.getBinCIType());
            return packageName + "." + ownerName + "$" + typeName;
        }
    }

    public String getQualifiedName(BinTypeRef binTypeRef) {
        return getQualifiedName(binTypeRef.getBinCIType());
    }

    public BinTypeRef getOwner(BinMember member) {
        List triads = projectView.getTriads(member, OwnerTriad.class, null);
        if (triads.size() != 0) {
            Triad lastTriad = (Triad) triads.get(triads.size() - 1);
            if (lastTriad.getObject() instanceof BinTypeRef) {
                return (BinTypeRef) lastTriad.getObject();
            } else {
                return null;
            }
        } else {
            return member.getOwner();
        }
    }

    private String findNewNameFor(Object item) {
        List triads = projectView.getTriads(item, NameTriad.class, null);
        if (triads.size() != 0) {
            Triad lastTriad = (Triad) triads.get(triads.size() - 1);
            return (String) lastTriad.getObject();
        } else {
            return null;
        }
    }
}
