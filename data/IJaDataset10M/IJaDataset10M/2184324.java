package eu.mpower.framework.interoperability.calendarsynchronizer;

import eu.mpower.framework.interoperability.calendarsynchronizer.soap.ICalendarSynchronizer;
import eu.mpower.framework.interoperability.calendarsynchronizer.soap.Styles;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebService;
import mpower_export.ExportCalendar;
import mpower_export.Hash;
import mpower_util.Configurator;
import mpower_hibernate.Person;
import mpower_hibernate.User;
import mpower_hibernate.UserPerson;
import mpower_hibernate.UserPersonFacade;
import java.util.List;
import mpower_hibernate.PersonFacade;

/**
 *
 * @author Roedl
 */
@WebService(serviceName = "CalendarSynchronizer", portName = "iCalendarSynchronizer", endpointInterface = "eu.mpower.framework.interoperability.calendarsynchronizer.soap.ICalendarSynchronizer", targetNamespace = "http://soap.CalendarSynchronizer.interoperability.framework.mpower.eu", wsdlLocation = "WEB-INF/wsdl/CalendarSynchronizer/calendarsynchronizer_20081031.wsdl")
public class CalendarSynchronizer implements ICalendarSynchronizer {

    private static Configurator configurator = new Configurator("configuration.xml");

    private static Configurator configuratorError = new Configurator("configuration.error.xml");

    public eu.mpower.framework.interoperability.calendarsynchronizer.soap.GetICalSubscriptionURLResponseMessage getICalSubscriptionURL(eu.mpower.framework.interoperability.calendarsynchronizer.soap.GetICalSubscriptionURLMessage message) {
        eu.mpower.framework.interoperability.calendarsynchronizer.soap.GetICalSubscriptionURLResponseMessage response = new eu.mpower.framework.interoperability.calendarsynchronizer.soap.GetICalSubscriptionURLResponseMessage();
        String UserHash = Hash.GetHashForUser(message.getMpowerUserId());
        String Url = configurator.getProperty("export.google.calendar.subscription.root") + "/calendar/";
        Boolean AddFilename = true;
        if (UserHash.length() > 0) {
            if (message.getStyle().equals(Styles.WEBCAL_FULL)) {
                Url = configurator.getProperty("export.google.calendar.subscription.protocol.webcal") + Url + message.getMpowerUserId() + "/" + UserHash + "/full/";
            } else if (message.getStyle().equals(Styles.WEBCAL_FUTURE)) {
                Url = configurator.getProperty("export.google.calendar.subscription.protocol.webcal") + Url + message.getMpowerUserId() + "/" + UserHash + "/future/";
            } else if (message.getStyle().equals(Styles.HTTP_FULL)) {
                Url = configurator.getProperty("export.google.calendar.subscription.protocol.http") + Url + message.getMpowerUserId() + "/" + UserHash + "/full/";
            } else if (message.getStyle().equals(Styles.HTTP_FUTURE)) {
                Url = configurator.getProperty("export.google.calendar.subscription.protocol.http") + Url + message.getMpowerUserId() + "/" + UserHash + "/future/";
            } else {
                Url = configurator.getProperty("export.google.calendar.subscription.protocol.http") + Url + "error/" + configuratorError.getProperty("error.code.wrongformatparam") + "/";
                AddFilename = false;
            }
            if (AddFilename == true) {
                if (message.getFilename() != null && message.getFilename().length() > 0) {
                    response.setCalendarURL(Url + "ical/" + message.getFilename());
                } else {
                    response.setCalendarURL(Url);
                }
            } else {
                response.setCalendarURL(Url);
            }
        } else {
            response.setCalendarURL(configurator.getProperty("export.google.calendar.subscription.protocol.http") + Url + "error/" + configuratorError.getProperty("error.code.usernotfound") + "/");
        }
        return response;
    }

    public eu.mpower.framework.interoperability.calendarsynchronizer.soap.Status exportToGoogleCalendar(eu.mpower.framework.interoperability.calendarsynchronizer.soap.ExportToGoogleCalendarMessage message) {
        eu.mpower.framework.interoperability.calendarsynchronizer.soap.Status response = new eu.mpower.framework.interoperability.calendarsynchronizer.soap.Status();
        try {
            ExportCalendar generator = new ExportCalendar();
            generator.sendToGoogle(message.getMpowerUserId(), message.getGoogleUsername(), message.getGooglePassword());
            response.setResult(0);
        } catch (Exception ex) {
            Logger.getLogger(CalendarSynchronizer.class.getName()).log(Level.SEVERE, null, ex);
            response.setResult(1);
        }
        return response;
    }

    public eu.mpower.framework.interoperability.calendarsynchronizer.soap.GetAvailableUsersResponseMessage getAvailableUsers(eu.mpower.framework.interoperability.calendarsynchronizer.soap.GetAvailableUsersMessage message) {
        eu.mpower.framework.interoperability.calendarsynchronizer.soap.GetAvailableUsersResponseMessage response = new eu.mpower.framework.interoperability.calendarsynchronizer.soap.GetAvailableUsersResponseMessage();
        eu.mpower.framework.interoperability.calendarsynchronizer.soap.User tempUserEntry = new eu.mpower.framework.interoperability.calendarsynchronizer.soap.User();
        Person tempPerson = new Person();
        User tempUser = new User();
        PersonFacade personF = new PersonFacade();
        List<Person> persons = personF.findAll();
        if (persons != null) {
            if (persons.size() > 0) {
                for (int i = 0; i < persons.size(); i++) {
                    try {
                        tempPerson = persons.get(i);
                        UserPersonFacade userPersonFacade = new UserPersonFacade();
                        List<UserPerson> up = (List<UserPerson>) userPersonFacade.findByPersonId(tempPerson.getId());
                        tempUser = ((UserPerson) up.get(0)).getUser();
                        if ((tempPerson != null) && (tempUser != null)) {
                            tempUserEntry = new eu.mpower.framework.interoperability.calendarsynchronizer.soap.User();
                            tempUserEntry.setGivenName(tempPerson.getGivenNames());
                            tempUserEntry.setFamilyName(tempPerson.getFamilyName());
                            tempUserEntry.setUserId(tempUser.getUserID());
                            response.getUser().add(tempUserEntry);
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(CalendarSynchronizer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        return response;
    }
}
