package org.libreplan.web.users.settings;

import org.libreplan.business.common.exceptions.ValidationException;
import org.libreplan.business.settings.entities.Language;

/**
 * Model for UI operations related to user settings
 *
 * @author Cristina Alvarino Perez <cristina.alvarino@comtecsf.es>
 * @author Ignacio Diaz Teijido <ignacio.diaz@comtecsf.es>
 */
public interface ISettingsModel {

    void setApplicationLanguage(Language applicationLanguage);

    Language getApplicationLanguage();

    void initEditLoggedUser();

    void confirmSave() throws ValidationException;

    void setExpandCompanyPlanningViewCharts(boolean expandCompanyPlanningViewCharts);

    boolean isExpandResourceLoadViewCharts();

    void setExpandResourceLoadViewCharts(boolean expandResourceLoadViewCharts);

    boolean isExpandOrderPlanningViewCharts();

    void setExpandOrderPlanningViewCharts(boolean expandOrderPlanningViewCharts);

    boolean isExpandCompanyPlanningViewCharts();

    void setLastName(String lastName);

    String getLastName();

    void setFirstName(String firstName);

    String getFirstName();

    String getEmail();

    void setEmail(String email);

    String getLoginName();
}
