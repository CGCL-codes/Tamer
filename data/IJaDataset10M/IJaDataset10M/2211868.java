package ssg.tools.common.fileUtilities.gui.actions;

import ssg.tools.common.fileUtilities.gui.actions.IActionExecutor.ACTION_DOMAIN;

/**
 * Methods to manage domain-specific status/data.
 * Use *ChangedDomain* methods to indicate/clear domain changes (e.g. to enforce specific GUI updates).
 * Use *ModelForDomain* methods to register/unregister data structures belonging to domain (e.g. for updates).
 *
 * @author ssg
 */
public interface IDomainTracker {

    ACTION_DOMAIN[] getChangedDomains();

    boolean isChangedDomain(String domain);

    boolean isChangedDomain(ACTION_DOMAIN domain);

    void resetChangedDomain(String domain);

    void resetChangedDomain(ACTION_DOMAIN domain);

    void setChangedDomain(String domain);

    void setChangedDomain(ACTION_DOMAIN domain);

    void registerModelForDomain(Object model, ACTION_DOMAIN domain);

    void removeRegisteredModelForDomain(Object model);

    Object[] getRegisteredModelsForDomain(ACTION_DOMAIN domain);

    void updateDomainModels(ACTION_DOMAIN[] domains);
}
