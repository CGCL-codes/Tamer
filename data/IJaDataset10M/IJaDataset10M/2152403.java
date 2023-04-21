package nl.gridshore.samples.raffle.business;

import nl.gridshore.samples.raffle.business.exceptions.ParticipantIsAWinnerException;
import nl.gridshore.samples.raffle.business.exceptions.PrizeDoesNotHaveAWinnerException;
import nl.gridshore.samples.raffle.business.exceptions.UnknownRaffleException;
import nl.gridshore.samples.raffle.business.exceptions.WinnerHasBeenSelectedException;
import nl.gridshore.samples.raffle.domain.Participant;
import nl.gridshore.samples.raffle.domain.Prize;
import nl.gridshore.samples.raffle.domain.Raffle;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: jettro
 * Date: Nov 18, 2007
 * Time: 9:32:46 PM
 * Business service exposing all raffle related services.
 */
public interface RaffleService {

    /**
     * Returns  list with al available raffles
     *
     * @return List containing all raffles.
     */
    List<Raffle> giveAllRaffles();

    /**
     * Return the raffle belonging to the specified id, throws an Unknown raffle exception
     * if the id does not exist
     *
     * @param raffleId Long representing the id of a raffle instance
     * @return Raffle found belonging to the provided id
     * @throws nl.gridshore.samples.raffle.business.exceptions.UnknownRaffleException
     *          when the raffle for
     *          provided id does not exist.
     */
    Raffle giveRaffleById(Long raffleId) throws UnknownRaffleException;

    /**
     * Store the provided raffle to be able to obtain it later on
     *
     * @param raffle Raffle object to store
     */
    void storeRaffle(Raffle raffle);

    /**
     * Removes the raffle from the storage
     *
     * @param raffle Raffle to remove
     */
    void removeRaffle(Raffle raffle);

    /**
     * Remove a specific participant, a participant that is a winner cannot be removed. A participantIsAWinner
     * exception will be thrown
     *
     * @param participant Participant to remove
     * @throws nl.gridshore.samples.raffle.business.exceptions.ParticipantIsAWinnerException
     *          if the provided participant
     *          is a winner it cannot be deleted.
     */
    void removeParticipantFromRaffle(Participant participant) throws ParticipantIsAWinnerException;

    /**
     * Remove the provided prize from the raffle
     *
     * @param prize prize to be removed
     */
    void removePrizeFromRaffle(Prize prize);

    /**
     * Chooses a winner for the current provided prize and throws an exception if the winner is already available
     *
     * @param prize prize to choose the raffle for
     * @return prize with the winner
     * @throws nl.gridshore.samples.raffle.business.exceptions.WinnerHasBeenSelectedException
     *          thrown if a winner is
     *          allready available
     */
    Prize chooseWinnerForPrize(Prize prize) throws WinnerHasBeenSelectedException;

    void removeWinnerFromPrize(Prize prize) throws PrizeDoesNotHaveAWinnerException;

    /**
     * Returns a list with the specified amount of random participants
     *
     * @param raffle          Object to obtain the participants for
     * @param numParticipants the number of participants to obtain
     * @return List of participants
     */
    List<Participant> giveRandomParticipants(Raffle raffle, Integer numParticipants);
}
