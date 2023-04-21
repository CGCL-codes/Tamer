package eu.redseeds.sc.merging;

import java.util.List;
import eu.redseeds.engine.navigator.providers.AdapterFactory;
import eu.redseeds.engine.navigator.providers.IProvider;
import eu.redseeds.sc.merging.conflicts.MergeConflictObject;
import eu.redseeds.sc.merging.resolvers.IMergeResolver;
import eu.redseeds.sc.merging.resolvers.MergeResolversAdapter;
import eu.redseeds.scl.model.rsl.rsldomainelements.actors.ActorDTO;
import eu.redseeds.scl.model.rsl.rsldomainelements.actors.ActorsPackageDTO;
import eu.redseeds.scl.model.rsl.rsldomainelements.domainelements.DomainSpecificationDTO;
import eu.redseeds.scl.model.rsl.rsldomainelements.notions.NotionDTO;
import eu.redseeds.scl.model.rsl.rsldomainelements.notions.NotionsPackageDTO;
import eu.redseeds.scl.model.rsl.rsldomainelements.systemelements.SystemElementDTO;
import eu.redseeds.scl.model.rsl.rsldomainelements.systemelements.SystemElementsPackageDTO;
import eu.redseeds.scl.model.rsl.rslrequirements.requirementsspecifications.RequirementsPackageDTO;
import eu.redseeds.scl.model.rsl.rslrequirements.requirementsspecifications.RequirementsSpecificationDTO;
import eu.redseeds.scl.model.sclkernel.ArchitecturalModelDTO;
import eu.redseeds.scl.model.sclkernel.ClipboardDTO;
import eu.redseeds.scl.model.sclkernel.DetailedDesignModelDTO;
import eu.redseeds.scl.model.sclkernel.SoftwareCaseDTO;
import eu.redseeds.scl.model.sdsl.PackageDTO;

/**
 * Contains operations used at various stages of merge process.
 *
 */
public class SCMergeManager {

    /**
	 * Check if clipboard is valid for merging
	 * @return
	 */
    public static boolean validateClipboardForMerging(ClipboardDTO clipboard) {
        if (clipboard == null) {
            return false;
        }
        if (clipboard.getRequirementsSpecificationDTO() == null) {
            return false;
        }
        if (clipboard.getRequirementsSpecificationDTO().getDomainSpecificationDTO() == null) {
            return false;
        }
        if (clipboard.getRequirementsSpecificationDTO().getDomainSpecificationDTO().getActorsPackageDTOList().size() > 1) {
            return false;
        }
        if (clipboard.getRequirementsSpecificationDTO().getDomainSpecificationDTO().getNotionsPackageDTOList().size() > 1) {
            return false;
        }
        if (clipboard.getRequirementsSpecificationDTO().getDomainSpecificationDTO().getSystemElementsPackageDTOList().size() > 1) {
            return false;
        }
        if (clipboard.getArchitecturalModelDTO() == null) {
            return false;
        }
        if (clipboard.getDetailedDesignModelDTO() == null) {
            return false;
        }
        return true;
    }

    /**
	 * Check if target SC is valid for merging
	 * @param sc
	 * @return
	 */
    public static boolean validateTargetSCForMerging(SoftwareCaseDTO sc) {
        if (sc == null) {
            return false;
        }
        if (sc.getRequirementsSpecificationDTO() == null) {
            return false;
        }
        if (sc.getRequirementsSpecificationDTO().getDomainSpecificationDTO() == null) {
            return false;
        }
        if (sc.getRequirementsSpecificationDTO().getDomainSpecificationDTO().getActorsPackageDTOList().size() > 1) {
            return false;
        }
        if (sc.getRequirementsSpecificationDTO().getDomainSpecificationDTO().getNotionsPackageDTOList().size() > 1) {
            return false;
        }
        if (sc.getRequirementsSpecificationDTO().getDomainSpecificationDTO().getSystemElementsPackageDTOList().size() > 1) {
            return false;
        }
        if (sc.getArchitecturalModelDTO() == null) {
            return false;
        }
        if (sc.getDetailedDesignModelDTO() == null) {
            return false;
        }
        return true;
    }

    /**
	 * Add parent-child relationships to the supplied list of conflicts (based on elements relationships)
	 * @param conflicts
	 * @return
	 */
    public static void refineMergeConflictsList(List<MergeConflictObject> conflicts) {
        if (conflicts == null) {
            return;
        }
        for (MergeConflictObject mco : conflicts) {
            Object obj = mco.getPastObject();
            IProvider provider = AdapterFactory.adapt(obj, null);
            Object parent = provider.getParent(obj);
            if (parent != null) {
                for (MergeConflictObject otherMco : conflicts) {
                    Object subparent = otherMco.getPastObject();
                    while (parent != null) {
                        if (parent.equals(subparent) && mco.getParent() == null) {
                            mco.setParent(otherMco);
                            otherMco.addChild(mco);
                        }
                        IProvider subprovider = AdapterFactory.adapt(obj, null);
                        parent = subprovider.getParent(parent);
                    }
                    parent = provider.getParent(obj);
                }
            }
        }
    }

    /**
	 * Merge valid requirements specifications
	 * @return
	 */
    public static void mergeRequirements(ClipboardDTO clipboard, SoftwareCaseDTO sc) {
        RequirementsSpecificationDTO sourceReqSpec = clipboard.getRequirementsSpecificationDTO();
        RequirementsSpecificationDTO targetReqSpec = sc.getRequirementsSpecificationDTO();
        if (sourceReqSpec == null || targetReqSpec == null) {
            return;
        }
        for (RequirementsPackageDTO reqPack : sourceReqSpec.getRequirementsPackagesDTOList()) {
            reqPack.setRequirementsSpecificationParent(targetReqSpec);
        }
        DomainSpecificationDTO domSpec = sourceReqSpec.getDomainSpecificationDTO();
        if (domSpec == null) {
            return;
        }
        ActorsPackageDTO rootTargetActorsPackage = targetReqSpec.getDomainSpecificationDTO().getActorsPackageDTOList().get(0);
        if (rootTargetActorsPackage != null && domSpec.getActorsPackageDTOList().size() > 0) {
            for (ActorsPackageDTO actPack : domSpec.getActorsPackageDTOList().get(0).getActorsPackageDTOList()) {
                actPack.setParent(rootTargetActorsPackage);
            }
            for (ActorDTO act : domSpec.getActorsPackageDTOList().get(0).getActorDTOList()) {
                act.setParent(rootTargetActorsPackage);
            }
        }
        NotionsPackageDTO rootTargetNotionsPackage = targetReqSpec.getDomainSpecificationDTO().getNotionsPackageDTOList().get(0);
        if (rootTargetNotionsPackage != null && domSpec.getNotionsPackageDTOList().size() > 0) {
            for (NotionsPackageDTO notPack : domSpec.getNotionsPackageDTOList().get(0).getNotionsPackageDTOList()) {
                notPack.setParent(rootTargetNotionsPackage);
            }
            for (NotionDTO not : domSpec.getNotionsPackageDTOList().get(0).getNotionDTOList()) {
                not.setParent(rootTargetNotionsPackage);
            }
        }
        SystemElementsPackageDTO rootTargetSystemElementsPackage = targetReqSpec.getDomainSpecificationDTO().getSystemElementsPackageDTOList().get(0);
        if (rootTargetSystemElementsPackage != null && domSpec.getSystemElementsPackageDTOList().size() > 0) {
            for (SystemElementsPackageDTO notPack : domSpec.getSystemElementsPackageDTOList().get(0).getSystemElementsPackageDTOList()) {
                notPack.setParent(rootTargetSystemElementsPackage);
            }
            for (SystemElementDTO not : domSpec.getSystemElementsPackageDTOList().get(0).getSystemElementDTOList()) {
                not.setParent(rootTargetSystemElementsPackage);
            }
        }
    }

    /**
	 * Merge valid architecture models 
	 * @return
	 */
    public static void mergeArchitecture(ClipboardDTO clipboard, SoftwareCaseDTO sc) {
        ArchitecturalModelDTO sourceArch = clipboard.getArchitecturalModelDTO();
        ArchitecturalModelDTO targetArch = sc.getArchitecturalModelDTO();
        for (PackageDTO archPack : sourceArch.getPackageDTOList()) {
            archPack.setParent(null);
            targetArch.addPackage(archPack);
        }
    }

    /**
	 * Merge valid detailed design models
	 * @return
	 */
    public static void mergeDesign(ClipboardDTO clipboard, SoftwareCaseDTO sc) {
        DetailedDesignModelDTO sourceDD = clipboard.getDetailedDesignModelDTO();
        DetailedDesignModelDTO targetDD = sc.getDetailedDesignModelDTO();
        for (PackageDTO ddPack : sourceDD.getPackageDTOList()) {
            ddPack.setParent(null);
            targetDD.addPackage(ddPack);
        }
    }

    /**
	 * Check SC and clipboard after the merge operation. Does basic clean-up if necessary
	 * @param clipboard
	 * @param sc
	 * @return
	 */
    public static boolean postMergeValidation(ClipboardDTO clipboard, SoftwareCaseDTO sc) {
        return true;
    }

    /**
	 * Resolves given list of conflicts.
	 * @param conflicts
	 */
    public static void resolveConflicts(List<MergeConflictObject> conflicts) {
        if (conflicts == null) {
            return;
        }
        for (MergeConflictObject c : conflicts) {
            IMergeResolver resolver = MergeResolversAdapter.adapt(c);
            resolver.resolve(c);
        }
    }
}
