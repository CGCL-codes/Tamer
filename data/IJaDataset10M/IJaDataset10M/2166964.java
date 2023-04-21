package diet.task.react;

import diet.message.MessageTask;
import diet.server.ConversationController.DefaultConversationController;
import diet.server.Participant;
import diet.task.TaskController;
import java.awt.Color;
import java.awt.Color;
import java.util.Date;
import java.util.Random;
import java.util.Vector;

/**
 *
 * @author sre
 */
public class ReactTaskController extends TaskController {

    Random r = new Random();

    DefaultConversationController cC;

    public Participant pA;

    public Participant pB;

    JReactTaskFrame jRTFA;

    JReactTaskFrame jRTFB;

    SelectionState ss = new SelectionState(this);

    Game gam;

    Color borderSelectColour = Color.WHITE;

    Color borderNormalColour = Color.darkGray;

    public ReactTaskController(DefaultConversationController cC) {
        this.cC = cC;
    }

    int thiscount = 0;

    public void nextSet() {
        gam.nextSet();
    }

    public void setSize(int i) {
        gam.setSETSIZ(i);
    }

    public Game getGame() {
        return gam;
    }

    @Override
    public void participantJoinedConversation(Participant pJoined) {
        MessageNewReactTask mnr = new MessageNewReactTask("server", "server");
        cC.getC().sendTaskMoveToParticipant(pJoined, mnr);
        cC.getC().getParticipants().sendMessageToParticipant(pJoined, mnr);
        cC.getC().displayNEWWebpage(pJoined, "ID1", "Instructions", "", 1150, 400, false, true);
        if (pA == null) {
            pA = pJoined;
            jRTFA = new JReactTaskFrame(this, pA);
        } else if (pB == null) {
            pB = pJoined;
            jRTFB = new JReactTaskFrame(this, pB);
            gam = new Game(this);
        }
    }

    public void changeColourOfWord(Participant p, String word, Color foreground, Color background) {
        MessageChangeColourOfWord mccw = new MessageChangeColourOfWord("server", "server", word, foreground, background);
        cC.getC().sendTaskMoveToParticipant(p, mccw);
    }

    public void changeColourOfButtonXY(Participant p, int x, int y, Color foreground, Color background, boolean ignoreAndDOALLXY) {
        MessageChangeColourOfButtonXY mcbXY = new MessageChangeColourOfButtonXY("server", "server", x, y, foreground, background, ignoreAndDOALLXY);
        cC.getC().sendTaskMoveToParticipant(p, mcbXY);
    }

    public void changeForegroundColourOnButtonDEPRECATED(Participant p, int x, int y, Color c) {
        MessageChangeColourOfButton msbtc = new MessageChangeColourOfButton("server", "server", x, y, c, MessageChangeColourOfButton.foreground, MessageChangeColourOfButton.senderHASNEITHERSELECTEDNORDESELECTED);
        cC.getC().sendTaskMoveToParticipant(p, msbtc);
    }

    public void changeBackgroundColourOnButtonDEPRECATED(Participant p, int x, int y, Color c) {
        MessageChangeColourOfButton msbtc = new MessageChangeColourOfButton("server", "server", x, y, c, MessageChangeColourOfButton.background, MessageChangeColourOfButton.senderHASNEITHERSELECTEDNORDESELECTED);
        cC.getC().sendTaskMoveToParticipant(p, msbtc);
    }

    public void swap(String id, int size) {
        gam.swap(id, size);
    }

    @Override
    public void processTaskMove(MessageTask mtm, Participant origin) {
        if (mtm instanceof MessageMousePressFromClient) {
            MessageMousePressFromClient mmpfc = (MessageMousePressFromClient) mtm;
            this.changeColourOfButtonXY(origin, mmpfc.getX(), mmpfc.getY(), Color.white, Color.black, true);
            this.changeColourOfButtonXY(getOther(origin), mmpfc.getX(), mmpfc.getY(), Color.white, Color.black, true);
            MessageChangeColourOfButton msbtc = new MessageChangeColourOfButton("server", "server", mmpfc.x, mmpfc.y, borderSelectColour, MessageChangeColourOfButton.border, MessageChangeColourOfButton.senderHasSELECTED);
            Participant other = getOther(origin);
            cC.getC().sendTaskMoveToParticipant(other, msbtc);
            ss.updateSelectionPressed(origin, mmpfc.getButtonname());
            Object[] evalpair = gam.evaluateStateChangeButtonPressed(origin, ss, mtm.getTimeStamp().getTime());
            String evaluationFOROUTPUTFILE = (String) evalpair[1];
            Move moveMostRecent = (Move) evalpair[0];
            evaluationFOROUTPUTFILE = evaluationFOROUTPUTFILE + moveMostRecent.getInfo();
            Vector v = new Vector();
            v.addElement("" + gam.getSet());
            v.addElement("" + gam.getConsecutiveCorrect());
            v.addElement(mmpfc.getButtonname());
            v.addElement("" + mmpfc.getX());
            v.addElement("" + mmpfc.getY());
            v.addElement("" + mmpfc.getLocalTimeOfPress());
            v.addElement("ISDELIBERATELYEMPTY");
            v.addElement("" + mmpfc.getPriorPressBySelf());
            v.addElement("" + mmpfc.getPriorReleaseBySelf());
            v.addElement("" + mmpfc.getPriorSelectByOther());
            v.addElement("" + mmpfc.getPriorreleaseByOther());
            v.addElement(mmpfc.getPriorSelectedStringByOther());
            v.addElement("" + mmpfc.getPriorSelectByOtherX());
            v.addElement("" + mmpfc.getPriorSelectByOtherY());
            v.addElement(evaluationFOROUTPUTFILE);
            cC.getC().saveDataToFile("MOUSEPRESSFROMCLIENT" + evalpair[2], origin.getParticipantID(), origin.getUsername(), mtm.getTimeStamp().getTime(), mtm.getTimeStamp().getTime(), "", v);
            if (origin == this.pA) jRTFA.setUniqueBorderSelected(mmpfc.getX(), mmpfc.getY(), borderSelectColour, MessageChangeColourOfButton.senderHasSELECTED); else if (origin == this.pB) jRTFB.setUniqueBorderSelected(mmpfc.getX(), mmpfc.getY(), borderSelectColour, MessageChangeColourOfButton.senderHasSELECTED);
        } else if (mtm instanceof MessageMouseReleaseFromClient) {
            MessageMouseReleaseFromClient mmprfc = (MessageMouseReleaseFromClient) mtm;
            System.err.println("------" + mmprfc.buttonname);
            MessageChangeColourOfButton msbtc = new MessageChangeColourOfButton("server", "server", mmprfc.x, mmprfc.y, borderNormalColour, MessageChangeColourOfButton.border, MessageChangeColourOfButton.senderHasDESELECTED);
            Participant other = getOther(origin);
            cC.getC().sendTaskMoveToParticipant(other, msbtc);
            ss.updateSelectionReleased(origin, mmprfc.getButtonname());
            Vector v = new Vector();
            v.addElement("" + gam.getSet());
            v.addElement("" + gam.getConsecutiveCorrect());
            v.addElement(mmprfc.getButtonname());
            v.addElement("" + mmprfc.getX());
            v.addElement("" + mmprfc.getY());
            v.addElement("ISDELIBERATELYEMPTY");
            v.addElement("" + mmprfc.getLocalTimeOfRelease());
            v.addElement("" + mmprfc.getPriorPressBySelf());
            v.addElement("" + mmprfc.getPriorReleaseBySelf());
            v.addElement("" + mmprfc.getPriorSelectByOther());
            v.addElement("" + mmprfc.getPriorreleaseByOther());
            v.addElement(mmprfc.getPriorSelectedStringByOther());
            v.addElement("" + mmprfc.getPriorSelectByOtherX());
            v.addElement("" + mmprfc.getPriorSelectByOtherY());
            cC.getC().saveDataToFile("MOUSERELEASEFROMCLIENT", origin.getParticipantID(), origin.getUsername(), mtm.getTimeStamp().getTime(), mtm.getTimeStamp().getTime(), "", v);
            if (origin == this.pA) jRTFA.setUniqueBorderSelected(mmprfc.getX(), mmprfc.getY(), borderNormalColour, MessageChangeColourOfButton.senderHasDESELECTED); else if (origin == this.pB) jRTFB.setUniqueBorderSelected(mmprfc.getX(), mmprfc.getY(), borderNormalColour, MessageChangeColourOfButton.senderHasDESELECTED);
        } else if (mtm instanceof MessageMouseEntryFromClient) {
            MessageMouseEntryFromClient mmefc = (MessageMouseEntryFromClient) mtm;
            Vector v = new Vector();
            v.addElement("" + gam.getSet());
            v.addElement("" + gam.getConsecutiveCorrect());
            v.addElement(mmefc.getButtonname());
            v.addElement("" + mmefc.getX());
            v.addElement("" + mmefc.getY());
            v.addElement("" + mmefc.getTime());
            cC.getC().saveDataToFile("MOUSEENTRYFROMCLIENT", origin.getParticipantID(), origin.getUsername(), mtm.getTimeStamp().getTime(), mtm.getTimeStamp().getTime(), "", v);
        }
        gam.displayMoves();
    }

    public Participant getOther(Participant p) {
        if (p == this.pA) return pB;
        if (p == this.pB) return pA;
        return null;
    }
}
