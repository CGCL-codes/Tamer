package de.icehorsetools.iceoffice.service.participant;

import java.util.List;
import de.icehorsetools.dataAccess.objects.Financearticle;
import de.icehorsetools.dataAccess.objects.Participant;
import de.icehorsetools.dataAccess.objects.Person;
import de.icehorsetools.dataAccess.objects.Test;

/**
 * @author tkr
 * @version $Id: IParticipantSv.java 274 2008-10-27 01:15:29Z kruegertom $
 */
public interface IParticipantSv {

    /**
	 * get the next free startingnumber
	 * 
	 * @return
	 *  the next possible startingmunber as string
	 */
    public String getNextStartingNumber();

    /**
   * initialize a new {@link Participant} with standard values
   * 
	 * @param participant
	 */
    public void initNewParticipant(Participant participant);

    /**
	 * validates the participant
	 * @param participant
	 * @return
	 */
    public void validateParticipant(Participant participant) throws CombinationExistExcepition, PersonNotSetException, HorseNotSetException;

    /**
	 * delete a exist {@link Participant}
	 * @param participant
	 */
    public void deleteParticipant(Participant participant);

    /**
   * assign all costs out of {@link Financearticle} which are assignable for person
   * @param person
   */
    public void assignCosts(Participant participant);

    /**
   * checks if exists a participant with the given person horse combination
   * @param participant
   * @return
   *  true, if the combination exists
   */
    public boolean existCombination(Participant participant);

    /**
   * get the list of remaining tests for the given participant
   * (tests may be assigned only one time to the participant)
   * @return
   *  the reduced list of tests
   */
    public List<Test> getRemainingTestsForParticipant(Participant participant);

    /**
   * 
   * @param person
   * @return
   */
    public Double getFinancecostSumWithHorse(Participant participant);
}
