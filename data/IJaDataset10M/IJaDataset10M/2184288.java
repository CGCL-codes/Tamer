package diet.server.ConversationController;

import client.MazeGameController2WAY;
import diet.debug.Debug;
import diet.message.MessageCBYCDocChangeFromClient;
import diet.message.MessageCBYCTypingUnhinderedRequest;
import diet.parameters.ExperimentSettings;
import diet.parameters.IntParameter;
import diet.server.Conversation;
import diet.server.Participant;
import diet.server.CbyC.DocChange;
import diet.server.CbyC.DocInsert;
import diet.server.CbyC.FloorHolderAdvanced;
import diet.server.CbyC.Sequence.MazeGameAcknowledgement;
import diet.server.CbyC.Sequence.MazeGameCapturesSequenceWaitingForTypingLull;
import diet.server.CbyC.Sequences;
import diet.server.CbyC.Sequence.MazeGameClarificationRequest;
import diet.server.CbyC.Sequence.MazeGameDefaultSequence;
import diet.server.CbyC.Sequence.MazeGameDummyBlockingOnTextEntry;
import diet.server.CbyC.Sequence.Sequence;
import diet.server.conversationhistory.turn.CBYCMAZEGAMETURN;
import diet.server.conversationhistory.turn.Turn;
import diet.textmanipulationmodules.CyclicRandomTextGenerators.CyclicRandomAcknowledgmentGenerator;
import diet.textmanipulationmodules.CyclicRandomTextGenerators.CyclicRandomCRClarifyingIntentions;
import diet.textmanipulationmodules.CyclicRandomTextGenerators.CyclicRandomCRClarifyingIntentions2;
import diet.textmanipulationmodules.mazegame_location_description_detection.PhraseFilter;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

public class CCCBYCMazeGameFakeEndOfTurnCRGenerator1 extends CCCBYCMazeGameFakeEdit {

    boolean interventionEARLY = true;

    boolean interventionFRAG = true;

    IntParameter lowerBoundForInter = new IntParameter("Lower bound for intervention (different participant)", 10);

    IntParameter lowerBoundForIntra = new IntParameter("Lower bound for intervention (same participant)", 20);

    IntParameter turnsSinceLastIntervention = new IntParameter("Turns elapsed since last intervention", 5);

    Participant lastRecipientOfCR = null;

    CyclicRandomCRClarifyingIntentions cycCRIN_participant1 = new CyclicRandomCRClarifyingIntentions2();

    CyclicRandomAcknowledgmentGenerator cycACK = new CyclicRandomAcknowledgmentGenerator();

    FloorHolderAdvanced fh2 = (FloorHolderAdvanced) fh;

    IntParameter maxInterventionsPerMaze = new IntParameter("Max Interventions per p. per maze", 2);

    @Override
    public void initialize(Conversation c, ExperimentSettings expSettings) {
        super.initialize(c, expSettings);
        super.setIsTypingTimeOut(250);
        super.setProcessLoopSleepTime(80);
        expSettings.addParameter(lowerBoundForIntra);
        expSettings.addParameter(lowerBoundForInter);
        expSettings.addParameter(this.turnsSinceLastIntervention);
        expSettings.addParameter(timeoutCR_RESP);
        expSettings.addParameter(timeoutRESP_ACK);
        expSettings.addParameter(ackDelayMINIMALVALUE);
        expSettings.addParameter(ackDelayPLUSRANDOMAMOUNT);
        expSettings.addParameter(crDelayMINIMALVALUE);
        expSettings.addParameter(crDelayPLUSRANDOMAMOUNT);
        expSettings.addParameter(createDummyIntervention);
        expSettings.addParameter(dummyInterventionMaxLength);
        expSettings.addParameter(maxInterventionsPerMaze);
    }

    public void fI() {
        this.forceIntervention = true;
    }

    public void fNI() {
        this.forceIntervention = false;
    }

    @Override
    public void processCBYCTypingUnhinderedRequest(Participant sender, MessageCBYCTypingUnhinderedRequest mWTUR) {
        if (c.getParticipants().getAllParticipants().size() < 2) return;
        synchronized (this) {
            if (state == 1) return;
        }
        super.processCBYCTypingUnhinderedRequest(sender, mWTUR);
    }

    Hashtable htLastTraversalCount = new Hashtable();

    MazeGameController2WAY mgc;

    int[] participant1mazeInterventions = new int[12];

    int[] participant2mazeInterventions = new int[12];

    boolean blockALLCR = false;

    public void unblockAll() {
        this.blockALLCR = false;
    }

    public void blockAll() {
        this.blockALLCR = true;
    }

    @Override
    public Conversation getC() {
        return super.getC();
    }

    PhraseFilter pf = new PhraseFilter();

    String possCR = "";

    public void reload() {
        pf.loadRawPhrases();
    }

    public boolean isCR_OK(Participant p) {
        mgc = (MazeGameController2WAY) this.getC().getTaskController();
        if (mgc.getMazeNo() > 5) {
            Conversation.printWSln("Main3", "ISCROKABORTED(0)");
            return false;
        }
        possCR = pf.getAClarificationFastest(this.mostRecentTextCONTIG);
        Conversation.printWSln("Main3", this.mostRecentTextCONTIG);
        Conversation.printWSln("Main3", possCR);
        Conversation.printWSln("Main3", "---------------");
        if (possCR == null) {
            Conversation.printWSln("Main3", "ISCROKABORTED(1)");
            return false;
        }
        if (possCR.length() == 0) {
            Conversation.printWSln("Main3", "ISCROKABORTED(2)");
            return false;
        }
        if (this.blockALLCR) {
            Conversation.printWSln("Main3", "ISCROKABORTED(3)");
            return false;
        }
        Conversation.printWSln("Main", showSummary());
        int currentTraversalCount = mgc.getSwitchTraversalCount(p.getUsername()) + mgc.getMazeNo();
        Integer oldTraversalCount = (Integer) this.htLastTraversalCount.get(p);
        if (oldTraversalCount == null) {
            return true;
        }
        if (oldTraversalCount >= currentTraversalCount) return false;
        int idx = this.getC().getParticipants().getAllParticipants().indexOf(p);
        if (idx == 0) {
            if (this.participant1mazeInterventions[mgc.getMazeNo()] >= maxInterventionsPerMaze.getValue()) {
                Conversation.printWSln("Main", "ISCROKABORTED(5A) " + participant1mazeInterventions[mgc.getMazeNo()] + "..." + maxInterventionsPerMaze.getValue());
                return false;
            }
            return true;
        } else if (idx == 1) {
            if (this.participant2mazeInterventions[mgc.getMazeNo()] >= maxInterventionsPerMaze.getValue()) {
                Conversation.printWSln("Main", "ISCROKABORTED(5B) " + participant2mazeInterventions[mgc.getMazeNo()] + "..." + maxInterventionsPerMaze.getValue());
                return false;
            }
            return true;
        }
        Conversation.printWSln("Main", "QUITE A BAD ERROR...." + p.getUsername());
        return true;
    }

    public String showSummary() {
        if (c.getParticipants().getAllParticipants().size() < 2) return "";
        mgc = (MazeGameController2WAY) this.getC().getTaskController();
        String summary = "";
        Vector vParticipants = c.getParticipants().getAllParticipants();
        for (int i = 0; i < vParticipants.size(); i++) {
            Participant p = (Participant) vParticipants.elementAt(i);
            Integer oldTRAVS = (Integer) this.htLastTraversalCount.get(p);
            if (oldTRAVS == null) oldTRAVS = 0;
            summary = summary + p.getUsername() + ": (Trigger: " + oldTRAVS + " switch traversal count must be less than: " + mgc.getSwitchTraversalCount(p.getUsername()) + mgc.getMazeNo() + ")";
            int idx = this.getC().getParticipants().getAllParticipants().indexOf(p);
            if (idx == 0) {
                summary = summary + "..has received: " + participant1mazeInterventions[mgc.getMazeNo()] + "..interventions in maze no." + mgc.getMazeNo() + " --Max:" + this.maxInterventionsPerMaze.getValue();
            } else if (idx == 1) {
                summary = summary + "..has received: " + participant2mazeInterventions[mgc.getMazeNo()] + "..interventions in maze No." + mgc.getMazeNo() + " --Max:" + this.maxInterventionsPerMaze.getValue();
            } else {
                summary = summary + "BAD ERROR...CAN'T FIND PARTICIPANT " + p.getUsername();
            }
            summary = summary + "\n-------------------------------------------------------------------------\n";
        }
        return summary;
    }

    public void performedIntervention(Participant p) {
        mgc = (MazeGameController2WAY) this.getC().getTaskController();
        htLastTraversalCount.put(p, mgc.getSwitchTraversalCount(p.getUsername()));
        int idx = this.getC().getParticipants().getAllParticipants().indexOf(p);
        if (idx == 0) {
            participant1mazeInterventions[mgc.getMazeNo()]++;
            Conversation.printWSln("Main", "Updating score for (1) " + p.getUsername() + " to " + participant2mazeInterventions[mgc.getMazeNo()]);
            return;
        } else if (idx == 1) {
            participant2mazeInterventions[mgc.getMazeNo()]++;
            Conversation.printWSln("Main", "Updating score for (2) " + p.getUsername() + " to " + participant2mazeInterventions[mgc.getMazeNo()]);
            return;
        }
        Conversation.printWSln("Main", "serious error...can't find" + p.getUsername());
    }

    public void setState(int i) {
        this.state = i;
        timeOfLastStateChange = new Date().getTime();
    }

    private int state = 0;

    long timeOfLastStateChange;

    Participant apparentOrigin;

    MazeGameCapturesSequenceWaitingForTypingLull mgcSWFT;

    public Participant mgcSWFTREcipient;

    IntParameter timeoutCR_RESP = new IntParameter("CR_to_RESP_TIMEOUT", 10000);

    IntParameter timeoutRESP_ACK = new IntParameter("RESP_to_ACK_TIMEOUT", 4000);

    long timeOfLastState3DocChange = new Date().getTime();

    IntParameter ackDelayMINIMALVALUE = new IntParameter("ACKDELAY_MINIMAL_VALUE", 110);

    IntParameter ackDelayPLUSRANDOMAMOUNT = new IntParameter("ACKDELAY_PLUS:_RANDOM_AMOUNT", 400);

    IntParameter crDelayMINIMALVALUE = new IntParameter("CRDELAY_MINIMALVALUE", 110);

    IntParameter crDelayPLUSRANDOMAMOUNT = new IntParameter("CRDELAY_PLUSRANDOMAMOUNT", 400);

    IntParameter createDummyIntervention = new IntParameter("", 30);

    IntParameter dummyInterventionMaxLength = new IntParameter("", 3000);

    public void panic() {
        setState(0);
        FloorHolderAdvanced fh2 = (FloorHolderAdvanced) fh;
        fh2.resumeNormalOperation();
    }

    @Override
    public void processLoop() {
        FloorHolderAdvanced fh2 = (FloorHolderAdvanced) fh;
        long currTime = new Date().getTime();
        if (state == 0) {
            fh.openFloorAfterTimeOut(super.getIsTypingTimeOut());
        } else if (state == 1) {
        } else if (state == 2) {
            System.err.println("COUNTING DOWN TO RESPONSE" + ((currTime - this.timeOfLastStateChange) - this.timeoutCR_RESP.getValue()));
            synchronized (this) {
                if (currTime - this.timeOfLastStateChange > this.timeoutCR_RESP.getValue()) {
                    fh2.resumeNormalOperation();
                    String addn = "-";
                    if (Debug.showEOFCRSTATES) addn = " ((NORESPONSE: ABORTED-SERVERSTATE FROM 2 to 0))";
                    getC().getParticipants().sendAndEnableLabelDisplayToParticipant(apparentOrigin, "Please type " + addn, false, true);
                    getC().getParticipants().sendAndEnableLabelDisplayToParticipant(this.mgcSWFTREcipient, "Please type " + addn, false, true);
                    setState(0);
                }
            }
        } else if (state == 3) {
            System.err.println("COUNTING DOWN TO ACK" + ((currTime - this.timeOfLastState3DocChange) - this.timeoutRESP_ACK.getValue()));
            System.err.println("COUNTING DOWN:" + (currTime - timeOfLastState3DocChange));
            synchronized (sS) {
                if (timeOfLastState3DocChange > 0 && (currTime - this.timeOfLastState3DocChange > this.timeoutRESP_ACK.getValue())) {
                    fh2.blockAllIncomingFloorRequests(true);
                    if (Debug.trackLockedInterface) Conversation.printWSln("Main", "COUNTED DOWN (4) FROM RESPONSE TO ACK");
                    fh2.forciblyOpenFloor();
                    if (Debug.trackLockedInterface) Conversation.printWSln("Main", "COUNTED DOWN (5)FROM RESPONSE TO ACK");
                    MazeGameAcknowledgement mga = new MazeGameAcknowledgement(sS, this, this.mgcSWFTREcipient.getUsername(), this.apparentOrigin.getUsername(), this.cycACK.getNext(this.mgcSWFTREcipient), this.ackDelayMINIMALVALUE.getValue(), this.ackDelayPLUSRANDOMAMOUNT.getValue());
                    mga.setType("ACK");
                    sS.addRecordedSequenceFromApparentOrigin(mga);
                    setState(4);
                }
            }
        }
    }

    String mostRecentText = "";

    String mostRecentTextCONTIG = "";

    @Override
    public Turn getTurnTypeForIO() {
        return new CBYCMAZEGAMETURN();
    }

    @Override
    public void processCBYCDocChange(Participant sender, MessageCBYCDocChangeFromClient mCDC) {
        if (Debug.inCRENDOFTURNJUMPTOMAZE6) {
        }
        super.processCBYCDocChange(sender, mCDC);
        if (state == 3) this.timeOfLastState3DocChange = new Date().getTime();
        System.err.println("The state on the server is: " + state);
        mostRecentText = "";
        if (mCDC.getDocChange() instanceof diet.server.CbyC.DocInsert) {
            DocInsert di = (DocInsert) mCDC.getDocChange();
            if (di.getOffs() == 0) {
                mostRecentText = "" + mCDC.getDocChange().elementString + di.str;
                mostRecentText = mostRecentText.replaceAll("\\n", "");
                Conversation.printWSln("Main3", "----RECEIVINGDOCCHANGE: " + mostRecentText);
            }
        }
    }

    boolean forceIntervention = false;

    int speakerChanges = 0;

    @Override
    public synchronized Sequence getNextSequence_Speaker_Change(Sequence prior, int sequenceNo, Sequences ss, Participant sender, MessageCBYCTypingUnhinderedRequest mCTUR) {
        speakerChanges++;
        if (Debug.showEOFCRSTATES) Conversation.printWSlnLog("Main", sender.getUsername() + " is requesting turnSPEAKERCHANGE, the state is: " + this.state);
        if (state == 2) {
            Object oldLock = mgcSWFT;
            if (oldLock == null) oldLock = "";
            synchronized (oldLock) {
                mgcSWFT = new MazeGameCapturesSequenceWaitingForTypingLull(sS, this, sender.getUsername(), mCTUR);
                mgcSWFT.setType("RESPONSE_NEXTSEQUENCESPEAKERCHANGE");
                this.mgcSWFTREcipient = sender;
                setState(3);
                this.timeOfLastState3DocChange = new Date().getTime();
                Conversation.printWSln("MODOS", "(B)MazeGameCapturesSequenceWaitingForTypingLull:" + sender.getUsername());
                this.mostRecentTextCONTIG = "";
                Conversation.printWSln("Main3", "RESETTINGCONTIGSPEAKERCHANGE");
                return mgcSWFT;
            }
        }
        if (state == 00 && r.nextInt(this.createDummyIntervention.getValue()) == 1) {
            Participant pRecipient = (Participant) getC().getParticipants().getAllOtherParticipants(sender).elementAt(0);
            Conversation.printWSln("MODOS", "(A2)MazeGameClarificationRequest:" + sender.getUsername());
            this.mostRecentTextCONTIG = "";
            Conversation.printWSln("Main3", "RESETTINGCONTIGSPEAKERCHANGE");
            return new MazeGameDummyBlockingOnTextEntry(sS, this, pRecipient.getUsername(), sender.getUsername(), mCTUR, this.dummyInterventionMaxLength.getValue());
        }
        if (speakerChanges > 3 && this.getC().getParticipants().getAllParticipants().size() > 1) {
            int rnd = 2;
            Conversation.printWSln("Main", "Trying for intervention...Possible CR is:" + this.possCR);
            Participant pRecipient = (Participant) getC().getParticipants().getAllOtherParticipants(sender).elementAt(0);
            if (mostRecentTextCONTIG.length() == 0) {
                mostRecentTextCONTIG = mostRecentText;
            }
            boolean crISOK = this.isCR_OK(sender);
            this.mostRecentTextCONTIG = "";
            Conversation.printWSln("Main3", "RESETTINGCONTIGSPEAKERCHANGE_AFTERCHECKINGFORCR");
            if ((forceIntervention) || ((crISOK && ((rnd == 2 && lastRecipientOfCR != pRecipient) || (lastRecipientOfCR == pRecipient && rnd == 2 && turnsSinceLastIntervention.getValue() > 2))))) {
                lastRecipientOfCR = pRecipient;
                this.turnsSinceLastIntervention.setValue(0);
                expSettings.generateParameterEvent(turnsSinceLastIntervention);
                String cr = "" + this.possCR + "?";
                possCR = "";
                cr = this.cycCRIN_participant1.getNext(pRecipient);
                this.setState(1);
                apparentOrigin = sender;
                this.mgcSWFTREcipient = pRecipient;
                fh.setInformOthersOfTyping(false);
                Conversation.printWSln("MODOS", "(A)MAZEGAMECR recipient" + pRecipient.getUsername() + " Sender: " + sender.getUsername());
                MazeGameClarificationRequest mgcr = new MazeGameClarificationRequest(sS, this, pRecipient.getUsername(), sender.getUsername(), mCTUR, cr, this.crDelayMINIMALVALUE.getValue(), this.crDelayPLUSRANDOMAMOUNT.getValue());
                this.performedIntervention(sender);
                forceIntervention = false;
                speakerChanges = 0;
                mgcr.setType("CLARIFICATION_NEXTSEQUENCESPEAKERCHANGE");
                return mgcr;
            } else {
                Conversation.printWSln("Main", "Random number was: " + rnd + " should be 2 for intervention");
                Conversation.printWSln("Main", "ISCROK: " + Boolean.toString(crISOK));
                if (lastRecipientOfCR != null) Conversation.printWSln("Main", "LASTRECIPIENT: " + lastRecipientOfCR.getUsername());
                Conversation.printWSln("Main", "PRECIPIENT: " + pRecipient.getUsername());
                Conversation.printWSln("Main", "TURNSSINCELASTINTERVENTION: " + turnsSinceLastIntervention.getValue());
                Conversation.printWSln("Main", "POSSIBLECR: " + possCR);
            }
        }
        this.turnsSinceLastIntervention.setValue(turnsSinceLastIntervention.getValue() + 1);
        expSettings.generateParameterEvent(turnsSinceLastIntervention);
        Conversation.printWSln("Main", "Normal turn");
        Conversation.printWSln("MODOS", "(C)MazeGameDefaultSequence" + sender.getUsername());
        MazeGameDefaultSequence mgds = new MazeGameDefaultSequence(sS, this, sender.getUsername(), mCTUR);
        mgds.setType("DEFAULTNORMAL_NEXTSEQUENCESPEAKERCHANGE");
        this.mostRecentTextCONTIG = "";
        Conversation.printWSln("Main3", "RESETTINGCONTIGSPEAKERCHANGE_AFTERCHECKINGFORCR");
        return mgds;
    }

    @Override
    public Sequence getNextSequence_NewLine_By_Same_Speaker(Sequence prior, int sequenceNo, Sequences sS, String sender, MessageCBYCDocChangeFromClient mCDC) {
        this.mostRecentTextCONTIG = mostRecentTextCONTIG + " " + this.mostRecentText;
        Conversation.printWSln("Main3", "ADDING NEWLINECONTIG....[" + this.mostRecentTextCONTIG + "] ");
        DocChange dc = mCDC.getDocChange();
        String sendername = prior.getSender();
        Participant p = this.c.getParticipants().findParticipantWithUsername(sendername);
        try {
        } catch (Exception e) {
        }
        if (state == 2 || state == 3) {
            mgcSWFT = new MazeGameCapturesSequenceWaitingForTypingLull(sS, this, sender, mCDC.getTimeStamp(), dc.elementString, dc.elementStart, dc.elementFinish);
            mgcSWFT.setType("RESPONSE_NEWLINE");
            this.mgcSWFTREcipient = p;
            this.timeOfLastState3DocChange = new Date().getTime();
            setState(3);
            Conversation.printWSln("MODOS", "(B)MazeGameCapturesSequenceWaitingForTypingLull:" + sender);
            return mgcSWFT;
        }
        if (Debug.showEOFCRSTATES) Conversation.printWSlnLog("Main", sender + " is requesting NEWLINETURN, the state is: " + this.state + "B");
        Conversation.printWSln("Main", "Returning Normal turn");
        this.turnsSinceLastIntervention.setValue(turnsSinceLastIntervention.getValue() + 1);
        expSettings.generateParameterEvent(turnsSinceLastIntervention);
        Conversation.printWSln("MODOS", "(B)MazeGameDefaultSequence:" + sender);
        MazeGameDefaultSequence mgds = new MazeGameDefaultSequence(sS, this, sender, mCDC.getTimeStamp(), dc.elementString, dc.elementStart, dc.elementFinish);
        mgds.setType("default_NEWLINE");
        return mgds;
    }

    public Sequence getNextSequence_Edit_By_Different_Speaker(Sequence prior, int sequenceNo, Sequences ss, Participant p, MessageCBYCTypingUnhinderedRequest mCTUR) {
        this.mostRecentTextCONTIG = "";
        Conversation.printWSln("Main3", "RESETTINGCONTIG");
        String sendername = prior.getSender();
        if (state == 2 || state == 3) {
            mgcSWFT = new MazeGameCapturesSequenceWaitingForTypingLull(sS, this, p.getUsername(), mCTUR.getTimeStamp(), mCTUR.getElementString(), mCTUR.getElementStart(), mCTUR.getElementFinish());
            mgcSWFT.setType("RESPONSE_EDITBYDIFFERENT");
            this.mgcSWFTREcipient = p;
            this.timeOfLastState3DocChange = new Date().getTime();
            setState(3);
            Conversation.printWSln("MODOS", "(C)MazeGameCapturesSequenceWaitingForTypingLull:" + sendername);
            return mgcSWFT;
        }
        if (Debug.showEOFCRSTATES) Conversation.printWSlnLog("Main", p.getUsername() + " is requesting EDITBYDIFFERENT, the state is: " + this.state);
        Conversation.printWSln("MODOS", "EDITBYDIFFERENT");
        MazeGameDefaultSequence mgds = new MazeGameDefaultSequence(sS, this, p.getUsername(), mCTUR);
        mgds.setType("DEFAULTNORMAL_NEXTSEQUENCE_EDITBYDIFFERENT");
        return mgds;
    }

    public Sequence getNextSequence_Edit_By_Same_Speaker(Sequence prior, int sequenceNo, Sequences sS, String sender, MessageCBYCDocChangeFromClient mCDC) {
        DocChange dc = mCDC.getDocChange();
        String sendername = prior.getSender();
        Participant p = this.c.getParticipants().findParticipantWithUsername(sendername);
        if (state == 2 || state == 3) {
            mgcSWFT = new MazeGameCapturesSequenceWaitingForTypingLull(sS, this, sender, mCDC.getTimeStamp(), dc.elementString, dc.elementStart, dc.elementFinish);
            mgcSWFT.setType("RESPONSE_EDITBYSAME");
            this.mgcSWFTREcipient = p;
            this.timeOfLastState3DocChange = new Date().getTime();
            setState(3);
            Conversation.printWSln("MODOS", "(B)MazeGameCapturesSequenceWaitingForTypingLull:" + sender);
            return mgcSWFT;
        }
        if (Debug.showEOFCRSTATES) Conversation.printWSlnLog("Main", sender + " is requesting EDITBYSAME, the state is: " + this.state);
        Conversation.printWSln("MODOS", "EDITBYSAME");
        MazeGameDefaultSequence mgds = new MazeGameDefaultSequence(sS, this, sender, mCDC.getTimeStamp(), dc.elementString, dc.elementStart, dc.elementFinish);
        mgds.setType("default_EDITBYSAME");
        return mgds;
    }
}
