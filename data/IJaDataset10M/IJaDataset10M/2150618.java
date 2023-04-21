package com.hack23.cia.model.api.application.factory;

import java.util.Date;
import com.hack23.cia.model.api.application.events.ActionEvent;
import com.hack23.cia.model.api.application.events.ApplicationErrorEventData;
import com.hack23.cia.model.api.application.events.ConfigurationOperationType;
import com.hack23.cia.model.api.application.events.ExternalUrlOperationType;
import com.hack23.cia.model.api.application.events.LanguageContentOperationType;
import com.hack23.cia.model.api.application.events.LanguageOperationType;
import com.hack23.cia.model.api.application.events.MonitorOperationType;
import com.hack23.cia.model.api.application.events.PortalOperationType;
import com.hack23.cia.model.api.application.events.TopListOperationType;
import com.hack23.cia.model.api.application.events.UserAccountOperationType;
import com.hack23.cia.model.api.application.events.UserOperationType;
import com.hack23.cia.model.api.common.ModelFactory;

/**
 * A factory for creating ApplicationModel objects.
 */
public interface ApplicationEventsModelFactory extends ModelFactory {

    /**
	 * Creates a new ApplicationModel object.
	 * 
	 * @param date the date
	 * @param operation the operation
	 * @param agencyId the agency id
	 * 
	 * @return the action event
	 */
    ActionEvent createAgencyActionEvent(Date date, ConfigurationOperationType operation, Long agencyId);

    /**
	 * Creates a new ApplicationModel object.
	 * 
	 * @param date the date
	 * 
	 * @return the action event
	 */
    ActionEvent createAgentDeploymentActionEvent(Date date);

    /**
	 * Creates a new ApplicationModel object.
	 * 
	 * @param date the date
	 * 
	 * @return the action event
	 */
    ActionEvent createApplicationActionEvent(Date date);

    /**
	 * Creates a new ApplicationModel object.
	 * 
	 * @param date the date
	 * @param string the string
	 * @param errorMessage the error message
	 * 
	 * @return the action event
	 */
    ActionEvent createApplicationErrorActionEvent(Date date, String string, String errorMessage);

    /**
	 * Creates a new SwedenModel object.
	 * 
	 * @param date the date
	 * @param ballotId the ballot id
	 * 
	 * @return the action event
	 */
    ActionEvent createBallotActionEvent(Date date, Long ballotId);

    /**
	 * Creates a new SwedenModel object.
	 * 
	 * @param date the date
	 * @param committeeReportId the committee report id
	 * 
	 * @return the action event
	 */
    ActionEvent createCommitteeReportActionEvent(Date date, Long committeeReportId);

    /**
	 * Creates a new SwedenModel object.
	 * 
	 * @param date the date
	 * 
	 * @return the action event
	 */
    ActionEvent createCommitteeReportsActionEvent(Date date);

    /**
	 * Creates a new ApplicationModel object.
	 * 
	 * @param date the date
	 * @param operation the operation
	 * 
	 * @return the action event
	 */
    ActionEvent createExternalUrlActionEvent(Date date, ExternalUrlOperationType operation);

    /**
	 * Creates a new ApplicationModel object.
	 * 
	 * @param date the date
	 * @param id the id
	 * @param id2 the id2
	 * @param operation the operation
	 * 
	 * @return the action event
	 */
    ActionEvent createLanguageAgencyActionEvent(Date date, Long id, Long id2, LanguageOperationType operation);

    /**
	 * Creates a new ApplicationModel object.
	 * 
	 * @param date the date
	 * @param id the id
	 * @param id2 the id2
	 * @param operation the operation
	 * 
	 * @return the action event
	 */
    ActionEvent createLanguageContentActionEvent(Date date, Long id, Long id2, LanguageContentOperationType operation);

    /**
	 * Creates a new ApplicationModel object.
	 * 
	 * @param date the date
	 * @param operation the operation
	 * 
	 * @return the action event
	 */
    ActionEvent createMonitorActionEvent(Date date, MonitorOperationType operation);

    /**
	 * Creates a new SwedenModel object.
	 * 
	 * @param date the date
	 * @param parliamentMemberId the parliament member id
	 * 
	 * @return the action event
	 */
    ActionEvent createParliamentMemberActionEvent(Date date, Long parliamentMemberId);

    /**
	 * Creates a new SwedenModel object.
	 * 
	 * @param date the date
	 * @param party the party
	 * @param operation the operation
	 * 
	 * @return the action event
	 */
    ActionEvent createPartyActionEvent(Date date, String party, TopListOperationType operation);

    /**
	 * Creates a new ApplicationModel object.
	 * 
	 * @param date the date
	 * @param operation the operation
	 * @param id the id
	 * 
	 * @return the action event
	 */
    ActionEvent createPortalActionEvent(Date date, PortalOperationType operation, Long id);

    /**
	 * Creates a new SwedenModel object.
	 * 
	 * @param date the date
	 * @param searchArgument the search argument
	 * 
	 * @return the action event
	 */
    ActionEvent createSearchActionEvent(Date date, String searchArgument);

    /**
	 * Creates a new ApplicationModel object.
	 * 
	 * @param date the date
	 * @param operation the operation
	 * 
	 * @return the action event
	 */
    ActionEvent createTopListActionEvent(Date date, TopListOperationType operation);

    /**
	 * Creates a new ApplicationModel object.
	 * 
	 * @param date the date
	 * @param operation the operation
	 * 
	 * @return the action event
	 */
    ActionEvent createUserAccountActionEvent(Date date, UserAccountOperationType operation);

    /**
	 * Creates a new ApplicationModel object.
	 * 
	 * @param date the date
	 * @param operation the operation
	 * 
	 * @return the action event
	 */
    ActionEvent createUserActionEvent(Date date, UserOperationType operation);

    /**
	 * Gets the action event spec.
	 * 
	 * @return the action event spec
	 */
    Class<? extends ActionEvent> getActionEventSpec();

    /**
	 * Gets the application error event data spec.
	 * 
	 * @return the application error event data spec
	 */
    Class<? extends ApplicationErrorEventData> getApplicationErrorEventDataSpec();
}
