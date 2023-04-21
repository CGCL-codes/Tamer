package org.nomicron.suber.event;

import org.nomicron.suber.model.bean.EventObjectType;
import org.nomicron.suber.model.bean.ValidationResponse;
import org.nomicron.suber.model.object.BallotItem;
import org.nomicron.suber.model.object.Turn;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import java.util.List;
import java.util.Map;

/**
 * Event for processing all BallotItems marked to be submitted at the start of the turn.
 */
public class DraftsToBallotItemsEvent extends AbstractDraftToBallotItemEvent {

    private static final Logger log = LogManager.getLogger(DraftsToBallotItemsEvent.class);

    private MailSender mailSender;

    private SimpleMailMessage baseEmailMessage;

    public void setBaseEmailMessage(SimpleMailMessage baseEmailMessage) {
        this.baseEmailMessage = baseEmailMessage;
    }

    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void execute(Map<EventObjectType, Object> objectMap) throws Exception {
        Turn currentTurn = getGameState().getCurrentTurn();
        List<BallotItem> draftList = getMetaFactory().getBallotItemFactory().getProposalsSubmittedForTurn(currentTurn);
        for (BallotItem draftBallotItem : draftList) {
            BallotItem ballotItem = createBallotItemFromDraft(currentTurn, draftBallotItem);
            if (ballotItem != null) {
                sendBallotItemNotification(ballotItem);
            } else {
                sendProposalRejectionEmail(draftBallotItem, getValidationMessageList(draftBallotItem));
            }
        }
    }

    /**
     * Create an email to a player that a proposal of theirs has been rejected.
     *
     * @param draft                  rejected draft
     * @param validationResponseList list of validation responses that prevented the draft from being accepted
     */
    private void sendProposalRejectionEmail(BallotItem draft, List<ValidationResponse> validationResponseList) {
        SimpleMailMessage message = new SimpleMailMessage(this.baseEmailMessage);
        message.setSubject("Draft " + draft.getDesignation() + " not accepted");
        message.setTo(draft.getPlayer().getEmail());
        StringBuilder body = new StringBuilder();
        body.append("The following draft was not accepted as a valid submission due to the following:\n\n");
        for (ValidationResponse reason : validationResponseList) {
            body.append(reason.getValidationMessage());
            body.append("\n");
        }
        body.append("\n\n-------------------------\n\n");
        body.append(draft.getEmailText());
        message.setText(body.toString());
        this.mailSender.send(message);
    }
}
