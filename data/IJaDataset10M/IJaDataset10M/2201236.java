package si.unimb.isportal07.iiPoll.controller.pollStates;

import java.util.ArrayList;
import java.util.Iterator;
import si.unimb.isportal07.iiPoll.controller.PollController;
import si.unimb.isportal07.iiPoll.dbobj.Answer;
import si.unimb.isportal07.iiPoll.dbobj.Polls;
import si.unimb.isportal07.iiPoll.dbobj.Voting;
import com.jcorporate.expresso.core.controller.Block;
import com.jcorporate.expresso.core.controller.ControllerException;
import com.jcorporate.expresso.core.controller.ControllerRequest;
import com.jcorporate.expresso.core.controller.ControllerResponse;
import com.jcorporate.expresso.core.controller.NonHandleableException;
import com.jcorporate.expresso.core.controller.Output;
import com.jcorporate.expresso.core.controller.State;
import com.jcorporate.expresso.core.controller.Transition;
import com.jcorporate.expresso.services.dbobj.DefaultUserInfo;

public class DetailsPollState extends State {

    private static final long serialVersionUID = 1L;

    public static final String STATE_NAME = "detailsPollState";

    public DetailsPollState() {
        super(DetailsPollState.STATE_NAME, DetailsPollState.STATE_NAME + "Description");
    }

    public void run(ControllerRequest request, ControllerResponse response) throws ControllerException, NonHandleableException {
        super.run(request, response);
        try {
            Transition errorTrans = new Transition();
            errorTrans.setControllerObject(PollController.class);
            errorTrans.setState(ErrorPollState.STATE_NAME);
            errorTrans.setName("errorTrans");
            setErrorTransition(errorTrans);
            Polls pollDB = new Polls();
            pollDB.setField(Polls.POLL_ID, request.getParameter(Polls.POLL_ID));
            pollDB.retrieve();
            Output headerDetailsOutput = new Output();
            headerDetailsOutput.setName("headerDetailsOutput");
            headerDetailsOutput.setLabel(getString("headerDetails"));
            response.add(headerDetailsOutput);
            Answer answerDB = new Answer();
            ArrayList allAnswer = answerDB.searchAndRetrieveList();
            Iterator iAnswer = allAnswer.iterator();
            String idPoll = new String(pollDB.getField(Polls.POLL_ID));
            Output idOutput = new Output();
            idOutput.setName("idOutput");
            idOutput.setContent(pollDB.getField(Polls.POLL_ID));
            idOutput.setLabel(getString("Aktivacija_zaporedna_stevilka"));
            response.add(idOutput);
            Output questionOutput = new Output();
            questionOutput.setName("questionOutput");
            questionOutput.setContent(pollDB.getField(Polls.QUESTION));
            questionOutput.setLabel(getString("Vprasanje"));
            response.add(questionOutput);
            Output dateCreatedOutput = new Output();
            dateCreatedOutput.setName("dateCreatedOutput");
            dateCreatedOutput.setContent(pollDB.getField(Polls.DATE_CREATED));
            dateCreatedOutput.setLabel(getString("Datum_vnosa_ankete"));
            response.add(dateCreatedOutput);
            Output dateModifiedOutput = new Output();
            dateModifiedOutput.setName("dateModifiedOutput");
            dateModifiedOutput.setContent(pollDB.getField(Polls.DATE_MODIFIED));
            dateModifiedOutput.setLabel(getString("Datum_zadnje_spremembe"));
            response.add(dateModifiedOutput);
            Output activateOutput = new Output();
            activateOutput.setName("activateOutput");
            if (pollDB.getField(Polls.IS_ACTIVE).equals("true")) activateOutput.setContent("Da"); else activateOutput.setContent("Ne");
            activateOutput.setLabel(getString("Aktivacija_aktivna"));
            response.add(activateOutput);
            Output dateFromOutput = new Output();
            dateFromOutput.setName("dateFromOutput");
            dateFromOutput.setContent(pollDB.getField(Polls.DATE_FROM));
            dateFromOutput.setLabel(getString("Datum_aktivacije"));
            response.add(dateFromOutput);
            Output dateToOutput = new Output();
            dateToOutput.setName("dateToOutput");
            dateToOutput.setContent(pollDB.getField(Polls.DATE_TO));
            dateToOutput.setLabel(getString("Datum_deaktivacije"));
            response.add(dateToOutput);
            Output categoryOutput = new Output();
            categoryOutput.setName("categoryOutput");
            categoryOutput.setLabel(getString("Kategorija"));
            categoryOutput.setContent(pollDB.getValidValueDescrip(Polls.CATEGORY_ID));
            response.add(categoryOutput);
            Output detailsOdgovoriOutput = new Output();
            detailsOdgovoriOutput.setName("detailsOdgovoriOutput");
            detailsOdgovoriOutput.setLabel(getString("Brisanje_odgovori"));
            response.add(detailsOdgovoriOutput);
            Block pollBlock = new Block();
            pollBlock.setName("pollBlock");
            Block pollRow;
            int count = 0;
            int glasovi = 0;
            while (iAnswer.hasNext()) {
                answerDB = (Answer) iAnswer.next();
                String s = new String(answerDB.getField(Answer.POLL_ID));
                if (s.equals(idPoll)) {
                    count++;
                    pollRow = new Block("pollRow" + count);
                    Output idAnswerOutput = new Output();
                    idAnswerOutput.setName("idAnswerOutput");
                    idAnswerOutput.setContent(answerDB.getField(Answer.ANSWER_ID));
                    idAnswerOutput.setLabel("Zaporedna stevilka odgovora je ");
                    pollRow.addNested(idAnswerOutput);
                    Output answerOutput = new Output();
                    answerOutput.setName("answerOutput");
                    answerOutput.setContent(answerDB.getField(Answer.TEXT));
                    answerOutput.setLabel("Odgovor " + answerDB.getField(Answer.ANSWER_ID) + ": ");
                    pollRow.addNested(answerOutput);
                    Voting VotingDB = new Voting();
                    VotingDB.setField(Voting.ID_ANSWER, answerDB.getField(Answer.ANSWER_ID));
                    VotingDB.search();
                    Output answerVoteOutput = new Output();
                    answerVoteOutput.setName("answerVoteOutput");
                    answerVoteOutput.setContent(Long.toString(VotingDB.getFoundCount()));
                    answerVoteOutput.setLabel("Stevilo glasov za " + answerDB.getField(Answer.ANSWER_ID) + " odgovor je: ");
                    pollRow.addNested(answerVoteOutput);
                    glasovi += Integer.parseInt(Long.toString(VotingDB.getFoundCount()));
                    pollBlock.addNested(pollRow);
                }
            }
            String glas = String.valueOf(glasovi);
            response.add(pollBlock);
            Output voteOutput = new Output();
            voteOutput.setName("voteOutput");
            voteOutput.setContent(glas);
            response.add(voteOutput);
            Output detailsVotesCountOutput = new Output();
            detailsVotesCountOutput.setName("detailsVotesCountOutput");
            detailsVotesCountOutput.setLabel(getString("VotesCount"));
            response.add(detailsVotesCountOutput);
            if (pollDB.getField(Polls.IS_ACTIVE).equals("true")) {
                Transition lockPollTransition = new Transition();
                lockPollTransition.setControllerObject(PollController.class.getName());
                lockPollTransition.setState(LockPollState.STATE_NAME);
                lockPollTransition.setLabel(getString("Zakleni_anketo"));
                lockPollTransition.setName("lockPollTransition");
                lockPollTransition.addParam(Polls.POLL_ID, pollDB.getField(Polls.POLL_ID));
                response.add(lockPollTransition);
            } else {
                Transition activatePollTransition = new Transition();
                activatePollTransition.setControllerObject(PollController.class.getName());
                activatePollTransition.setState(ActivatePollState.STATE_NAME);
                activatePollTransition.setLabel(getString("Aktiviraj_anketo"));
                activatePollTransition.setName("activatePollTransition");
                activatePollTransition.addParam(Polls.POLL_ID, pollDB.getField(Polls.POLL_ID));
                response.add(activatePollTransition);
                Transition activatePollTransition_advanced = new Transition();
                activatePollTransition_advanced.setControllerObject(PollController.class.getName());
                activatePollTransition_advanced.setState(ActivatePollState.STATE_NAME + "&style=activatePollState_advanced");
                activatePollTransition_advanced.setLabel(getString("Aktiviraj_anketo"));
                activatePollTransition_advanced.setName("activatePollTransition_advanced");
                activatePollTransition_advanced.addParam(Polls.POLL_ID, pollDB.getField(Polls.POLL_ID));
                response.add(activatePollTransition_advanced);
            }
            Transition editPollTransition = new Transition();
            editPollTransition.setControllerObject(PollController.class.getName());
            editPollTransition.setState(PromptEditPollState.STATE_NAME);
            editPollTransition.setLabel(getString("Spremeni_anketo"));
            editPollTransition.setName("promptEditPollTransition");
            editPollTransition.addParam(Polls.POLL_ID, pollDB.getField(Polls.POLL_ID));
            response.add(editPollTransition);
            Transition deletePollTransition = new Transition();
            deletePollTransition.setControllerObject(PollController.class.getName());
            deletePollTransition.setState(PromptDeletePollState.STATE_NAME);
            deletePollTransition.setLabel(getString("Izbrisi_anketo"));
            deletePollTransition.setName("deletePollTransition");
            deletePollTransition.addParam(Polls.POLL_ID, pollDB.getField(Polls.POLL_ID));
            response.add(deletePollTransition);
            Transition statsPollStateTransition = new Transition();
            statsPollStateTransition.setControllerObject(PollController.class.getName());
            statsPollStateTransition.setState(StatsPollState.STATE_NAME);
            statsPollStateTransition.setLabel(getString("Pokazi_statistiko"));
            statsPollStateTransition.setName("statsPollStateTransition");
            statsPollStateTransition.addParam(Polls.POLL_ID, pollDB.getField(Polls.POLL_ID));
            statsPollStateTransition.addParam(Answer.ANSWER_ID, "F");
            response.add(statsPollStateTransition);
            Transition listPollTransition = new Transition();
            listPollTransition.setControllerObject(PollController.class.getName());
            listPollTransition.setState(ListPollState.STATE_NAME);
            listPollTransition.setLabel(getString("Seznam_anket"));
            listPollTransition.setName("listPollTransition");
            response.add(listPollTransition);
            Transition editPollTransition_advanced = new Transition();
            editPollTransition_advanced.setControllerObject(PollController.class.getName());
            editPollTransition_advanced.setState(PromptEditPollState.STATE_NAME + "&style=editPollState_advanced");
            editPollTransition_advanced.setLabel(getString("Spremeni_anketo"));
            editPollTransition_advanced.setName("promptEditPollTransition_advanced");
            editPollTransition_advanced.addParam(Polls.POLL_ID, pollDB.getField(Polls.POLL_ID));
            response.add(editPollTransition_advanced);
            Transition deletePollTransition_advanced = new Transition();
            deletePollTransition_advanced.setControllerObject(PollController.class.getName());
            deletePollTransition_advanced.setState(PromptDeletePollState.STATE_NAME + "&style=deletePollState_advanced");
            deletePollTransition_advanced.setLabel(getString("Izbrisi_anketo"));
            deletePollTransition_advanced.setName("deletePollTransition_advanced");
            deletePollTransition_advanced.addParam(Polls.POLL_ID, pollDB.getField(Polls.POLL_ID));
            response.add(deletePollTransition_advanced);
            Transition statsPollStateTransition_advanced = new Transition();
            statsPollStateTransition_advanced.setControllerObject(PollController.class.getName());
            statsPollStateTransition_advanced.setState(StatsPollState.STATE_NAME + "&style=statsPollState_advanced");
            statsPollStateTransition_advanced.setLabel(getString("Pokazi_statistiko"));
            statsPollStateTransition_advanced.setName("statsPollStateTransition_advanced");
            statsPollStateTransition_advanced.addParam(Polls.POLL_ID, pollDB.getField(Polls.POLL_ID));
            statsPollStateTransition_advanced.addParam(Answer.ANSWER_ID, "F");
            response.add(statsPollStateTransition_advanced);
            Transition listPollTransition_advanced = new Transition();
            listPollTransition_advanced.setControllerObject(PollController.class.getName());
            listPollTransition_advanced.setState(ListPollState.STATE_NAME + "&style=listPollState_advanced");
            listPollTransition_advanced.setLabel(getString("Seznam_anket"));
            listPollTransition_advanced.setName("listPollTransition_advanced");
            response.add(listPollTransition_advanced);
        } catch (Exception e) {
        } finally {
        }
    }
}
