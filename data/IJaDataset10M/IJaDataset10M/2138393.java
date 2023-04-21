package confreg.ejb.controls;

import confreg.ejb.domain.*;
import confreg.ejb.dto.*;
import javax.ejb.Remote;
import confreg.ejb.exceptions.*;
import java.util.List;

/**
 *
 * @author Administrator
 */
@Remote
public interface RegistrationControlRemote {

    void modifyConferencePack(ConferencePack conferencePack) throws ValidationException, NonExistingUserException;

    void modifyConferencePack(String userEmail, ConferencePack conferencePack) throws ValidationException, NonExistingUserException;

    void modifyPersonalData(AllPersonalData allPersonalData) throws ValidationException, NonExistingUserException, confreg.ejb.exceptions.SecurityException;

    void modifyPersonalData(String userEmail, AllPersonalData allPersonalData) throws ValidationException, NonExistingUserException, confreg.ejb.exceptions.SecurityException;

    void validate(String userEmail, ConferencePack confPack) throws ValidationException;

    /**
     * show personal data
     * @param proceedingOrders
     */
    AllPersonalData getAllPersonalData() throws NonExistingUserException;

    AllPersonalData getAllPersonalData(String userEmail) throws NonExistingUserException;

    /**
     * Show selected events so far
     * @param proceedingOrders
     */
    ConferencePack getConferencePack() throws NonExistingUserException;

    ConferencePack getConferencePack(String userEmail) throws NonExistingUserException;

    User findUserByEmail(String email);

    /**
     * List all tutorials, granting users a list from which they can choose from.
     * @return
     */
    List<Tutorial> getTutorials();

    /**
     * List all workshops, granting users a list from which they can choose from.
     * @return
     */
    List<Workshop> getWorkshops();

    /**
     * List all lunches, granting users a list from which they can choose from.
     * @return
     */
    List<Lunch> getLunches();

    /**
     * List all social events, granting users a list from which they can choose from.
     * @return
     */
    List<SocialEvent> getSocialEvents();

    /**
     * List all proceedings, granting users a list from which they can choose from.
     * @return
     */
    List<Proceeding> getProceedings();

    boolean isRegistrationConfirmed(String securityToken, String userEmail) throws NonExistingUserException, confreg.ejb.exceptions.SecurityException;
}
