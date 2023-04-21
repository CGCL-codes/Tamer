package de.cue4net.eventservice.model.registration.service;

import java.util.List;
import de.cue4net.eventservice.controller.command.EventRegistrationCommand;
import de.cue4net.eventservice.model.location.Location;
import de.cue4net.eventservice.model.registration.Registration;
import de.cue4net.eventservice.model.user.User;

/**
 * @author Keino Uelze - cue4net
 * @version $Id: RegistrationService.java,v 1.14 2008-06-05 12:19:09 keino Exp $
 */
public interface RegistrationService {

    public boolean createRegistration(User currentUser, EventRegistrationCommand eventRegistrationCommand, StringBuffer registrationNumber);

    public boolean createRegistration(User currentUser, User targetUser, EventRegistrationCommand eventRegistrationCommand, StringBuffer registrationNumber, Boolean check);

    public boolean updateRegistration(User _currentUser, User _targetUser, EventRegistrationCommand _eventRegistrationCommand);

    public List<Registration> getCurrentRegistrationsByUser(User user);

    public List<Long> getLocationIDsForUserRegistrations(User user);

    public Registration getRegistrationByUserAndLocation(User user, Location location, int registrationStatus);

    public Registration getInitializedRegistrationByID(Long registrationID);

    public void appendEventRegistrationByEventIDandRegistrationID(Long regid, Long eventid);

    public boolean hasConflictingResidenceRegistration(User currentUser, EventRegistrationCommand eventRegistrationCommand);
}
