package de.icehorsetools.iceoffice.workflow.participant;

import org.ugat.wiser.dialog.MessageBoxHandler;
import org.ugat.wiser.language.Lang;
import org.ugat.wiser.workflow.implementations.DefaultDialogWorkflow;
import de.icehorsetools.dataAccess.objects.Participant;
import de.icehorsetools.dataAccess.objects.Person;
import de.icehorsetools.iceoffice.service.DefaultSvFactory;
import de.icehorsetools.iceoffice.service.participant.CombinationExistExcepition;
import de.icehorsetools.iceoffice.service.participant.HorseNotSetException;
import de.icehorsetools.iceoffice.service.participant.IParticipantSv;
import de.icehorsetools.iceoffice.service.participant.PersonNotSetException;

/**
 * @author tkr
 * @version $Id: ParticipantForPersonAddWf.java 312 2009-02-22 02:58:51Z
 *          kruegertom $
 */
public class ParticipantForPersonAddWf extends DefaultDialogWorkflow {

    private boolean isNewParticipant;

    protected Object preSetData(Object xData) {
        Participant participant = null;
        IParticipantSv sv = (IParticipantSv) DefaultSvFactory.getInstance().getParticipantSv();
        if (xData instanceof Person) {
            participant = new Participant();
            participant.setPerson((Person) xData);
            ((Person) xData).getParticipants().add(participant);
        } else if (xData instanceof Participant) {
            participant = (Participant) xData;
        }
        isNewParticipant = participant.getId() == null;
        if (isNewParticipant) {
            sv.initNewParticipant(participant);
        }
        return participant;
    }

    @Override
    public boolean preSave() {
        if (this.getData() instanceof Participant) {
            Participant participant = (Participant) this.getDataProxy();
            IParticipantSv participantSv = DefaultSvFactory.getInstance().getParticipantSv();
            try {
                participantSv.validateParticipant(participant);
            } catch (CombinationExistExcepition e) {
                new MessageBoxHandler().showInfo(Lang.get(this.getClass().getName(), "msgCombinationExists_title"), e.getMessage(), null, 500, 200);
                return false;
            } catch (PersonNotSetException e) {
                return false;
            } catch (HorseNotSetException e) {
                return false;
            }
        }
        return true;
    }
}
