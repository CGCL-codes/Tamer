package de.fau.cs.dosis.service.drug;

import java.util.Set;
import java.util.TreeSet;
import de.fau.cs.dosis.drug.ActiveIngredientManager;
import de.fau.cs.dosis.drug.ActiveIngredientNotFoundException;
import de.fau.cs.dosis.drug.BrandNameNotFoundException;
import de.fau.cs.dosis.drug.SubstanceClassNameException;
import de.fau.cs.dosis.model.ActiveIngredient;
import de.fau.cs.dosis.model.ActiveIngredientRevision;
import de.fau.cs.dosis.model.ActiveIngredientStatus;
import de.fau.cs.dosis.model.BrandName;
import de.fau.cs.dosis.model.TechnicalStatus;
import de.fau.cs.dosis.model.User;
import de.fau.cs.dosis.model.UserTasks;
import de.fau.cs.dosis.service.AccessDeniedException;

public class ActiveIngredientCreateService {

    private final ActiveIngredientManager activeIngredientManager;

    public ActiveIngredientCreateService(ActiveIngredientManager activeIngredientManager) {
        this.activeIngredientManager = activeIngredientManager;
    }

    private ActiveIngredientManager getActiveIngredientManager() {
        return activeIngredientManager;
    }

    /**
	 * Creates a new ActiveIngredient
	 * 
	 * @param user
	 * @param name
	 * @param brandNames
	 * @param substanceClasses
	 * @param dosage
	 * @param application
	 * @param effectSpectrum
	 * @param sideEffects
	 * @param interactionEffects
	 * @param contraIndications
	 * @param pharmacoDynamics
	 * @param comment
	 * @param references
	 * @throws AccessDeniedException
	 */
    public void createActiveIngredient(User user, String name, Set<String> brandNames, String substanceClasses, String dosage, String application, String effectSpectrum, String sideEffects, String interactionEffects, String contraIndications, String pharmacoDynamics, String comment, String references) throws AccessDeniedException {
        if (UserTasks.CREATE_ENTRY.allowed(user.getRole())) {
            ActiveIngredient ai = new ActiveIngredient();
            ActiveIngredientRevision air = new ActiveIngredientRevision();
            ai.setName(name);
            ai.setTechnicalStatus(TechnicalStatus.NEW);
            ai.setBrandNames(BrandName.buildSetFromStringSet(brandNames, ai));
            ai.setSubstanceClasses(substanceClasses);
            air.setApplication(application);
            air.setComment(comment);
            air.setReferences(references);
            air.setContraIndications(contraIndications);
            air.setDosage(dosage);
            air.setEffectSpectrum(effectSpectrum);
            air.setInteractionEffects(interactionEffects);
            air.setPharmacoDynamics(pharmacoDynamics);
            air.setSideEffects(sideEffects);
            air.setStatus(ActiveIngredientStatus.AWAITING);
            getActiveIngredientManager().createActiveIngredient(user, ai, air);
        } else {
            throw new AccessDeniedException();
        }
    }

    /**
	 * Adds a New ActiveIngredientRevision to an existing ActiveIngredient
	 * 
	 * @param user
	 * @param baseRevision
	 * @param dosage
	 * @param application
	 * @param comment
	 * @param references
	 * @param contraIndications
	 * @param interactionEffects
	 * @param sideEffects
	 * @param pharmacoDynamics
	 * @param effectSpectrum
	 * @param brandNames
	 * @param substanceClasses
	 * @return
	 * @throws AccessDeniedException
	 * @throws ActiveIngredientNotFoundException 
	 * @throws SubstanceClassNameException 
	 */
    public ActiveIngredientRevision addActiveIngredientRevision(User user, ActiveIngredientRevision baseRevision, String dosage, String application, String comment, String references, String contraIndications, String interactionEffects, String sideEffects, String pharmacoDynamics, String effectSpectrum, Set<String> brandNames, String substanceClasses) throws AccessDeniedException, ActiveIngredientNotFoundException, SubstanceClassNameException {
        if (UserTasks.CREATE_ENTRY.allowed(user.getRole())) {
            if (null == baseRevision) {
                throw new IllegalArgumentException("Invalid baseRevision");
            }
            ActiveIngredientRevision revision = new ActiveIngredientRevision();
            revision.setActiveIngredient(baseRevision.getActiveIngredient());
            revision.setDosage(dosage);
            revision.setApplication(application);
            revision.setComment(comment);
            revision.setReferences(references);
            revision.setContraIndications(contraIndications);
            revision.setInteractionEffects(interactionEffects);
            revision.setSideEffects(sideEffects);
            revision.setPharmacoDynamics(pharmacoDynamics);
            revision.setEffectSpectrum(effectSpectrum);
            revision.setPreviousRevisionNumber(baseRevision.getRevisionNumber());
            if (UserTasks.CHANGE_NOT_REVIEWED_FIELDS.allowed(user)) {
                if (brandNames != null) {
                    activeIngredientManager.setActiveIngredientsBrandNames(revision.getActiveIngredient(), brandNames);
                }
                setActiveIngredientSubstanceClassIntern(user, revision.getActiveIngredient(), substanceClasses);
            } else {
            }
            return getActiveIngredientManager().addRevision(user, baseRevision.getActiveIngredient(), revision, false);
        } else {
            throw new AccessDeniedException();
        }
    }

    /**
	 * Sets an active ingredient's brandname-list to the given one.
	 * Overrides old list!
	 * @param ai
	 * @param bns
	 * @throws ActiveIngredientNotFoundException 
	 * @throws BrandNameNotFoundException 
	 * @throws AccessDeniedException 
	 * 
	 * @deprecated Service should not work on ActiveIngredient objects directly. Modify AIR with addActiveIngredientRevision
	 */
    @Deprecated
    public void setActiveIngredientsBrandNames(User user, ActiveIngredient ai, Set<String> bns) throws ActiveIngredientNotFoundException, BrandNameNotFoundException, AccessDeniedException {
        if (UserTasks.CHANGE_NOT_REVIEWED_FIELDS.allowed(user)) {
            activeIngredientManager.setActiveIngredientsBrandNames(ai, bns);
        } else {
            throw new AccessDeniedException(UserTasks.CHANGE_NOT_REVIEWED_FIELDS);
        }
    }

    /**
	 * Sets an active ingredient's substanceclass list. Expects comma separated values in scs.
	 * @param ai
	 * @param scs
	 * @throws ActiveIngredientNotFoundException
	 * @throws SubstanceClassNameException
	 * @throws AccessDeniedException 
	 * 
	 * @deprecated Service should not work on ActiveIngredient objects directly. Modify AIR with addActiveIngredientRevision
	 */
    @Deprecated
    public void setActiveIngredientsSubstanceClasses(User user, ActiveIngredient ai, String scs) throws ActiveIngredientNotFoundException, SubstanceClassNameException, AccessDeniedException {
        if (UserTasks.CHANGE_NOT_REVIEWED_FIELDS.allowed(user)) {
            TreeSet<String> set = new TreeSet<String>();
            for (String part : scs.split(",")) {
                part = part.trim();
                if (!part.isEmpty()) {
                    set.add(part);
                }
            }
            this.setActiveIngredientsSubstanceClasses(user, ai, set);
        } else {
            throw new AccessDeniedException(UserTasks.CHANGE_NOT_REVIEWED_FIELDS);
        }
    }

    private void setActiveIngredientSubstanceClassIntern(User user, ActiveIngredient ai, String substanceClass) throws SubstanceClassNameException, ActiveIngredientNotFoundException {
        if (substanceClass == null || substanceClass.trim().isEmpty()) {
            if (ai.getSubstanceClasses() == null) {
                return;
            } else {
                ActiveIngredient sai = getActiveIngredientManager().getActiveIngredientByName(ai.getName());
                sai.setSubstanceClasses((String) null);
                getActiveIngredientManager().updateActiveIngredient(sai);
                return;
            }
        } else {
            TreeSet<String> set = new TreeSet<String>();
            for (String part : substanceClass.split(",")) {
                part = part.trim();
                if (!part.isEmpty()) {
                    set.add(part);
                }
            }
            for (String sc : set) {
                ActiveIngredient.validateSubstanceClassName(sc);
            }
            ActiveIngredient sai = getActiveIngredientManager().getActiveIngredientByName(ai.getName());
            sai.setSubstanceClasses(set);
            getActiveIngredientManager().updateActiveIngredient(sai);
        }
    }

    /**
	 * Service should not work on ActiveIngredient objects directly. Modify AIR with addActiveIngredientRevision
	 */
    @Deprecated
    public void setActiveIngredientsSubstanceClasses(User user, ActiveIngredient ai, Set<String> scset) throws ActiveIngredientNotFoundException, SubstanceClassNameException, AccessDeniedException {
        if (UserTasks.CHANGE_NOT_REVIEWED_FIELDS.allowed(user)) {
            for (String sc : scset) {
                ActiveIngredient.validateSubstanceClassName(sc);
            }
            ActiveIngredient sai = getActiveIngredientManager().getActiveIngredientByName(ai.getName());
            sai.setSubstanceClasses(scset);
            getActiveIngredientManager().updateActiveIngredient(sai);
        } else {
            throw new AccessDeniedException(UserTasks.CHANGE_NOT_REVIEWED_FIELDS);
        }
    }
}
