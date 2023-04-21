package diet.server.ConversationController;

import diet.client.WebpageAndImageDisplay;
import diet.message.MessageChatTextFromClient;
import diet.message.MessageKeypressed;
import diet.message.MessagePopup;
import diet.message.MessageWYSIWYGDocumentSyncFromClientInsert;
import diet.parameters.ExperimentSettings;
import diet.server.Conversation;
import diet.server.Participant;
import java.awt.Color;
import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;

/**
 *
 * @author sre
 */
public class CCPICTURESTIMULIBACKUP3 extends DefaultConversationController {

    String sbfolder = "stimuliset4";

    public String redbackground = "<html><head><style type=\"text/css\">body {color: white; background: red;}div { font-size: 200%;}</style></head><body><div>INCORRECT </div></body></html>";

    WebpageAndImageDisplay wid = new WebpageAndImageDisplay(null);

    String s = System.getProperty("user.dir") + File.separatorChar + "webserver" + File.separatorChar + sbfolder;

    File f = new File(s);

    File[] flist = f.listFiles();

    public static int mode = 0;

    public static int mode0START = 0;

    public static int mode1TESTPHASE_DIRECTOR = 1;

    public static int mode2TESTPHASE_POSTDIRECTOR_PREMATCHER = 2;

    public static int mode3TESTPHASE_MATCHER = 3;

    public static int mode4POSTTESTPHASE = 4;

    public static int mode5TRAININGPHASE = 5;

    public static int mode6TRAININGPHASECOMPLETE = 6;

    public static int mode7TESTPHASE_DIRECTOR = 7;

    public static int mode8TESTPHASE_MATCHER = 8;

    @Override
    public boolean requestParticipantJoinConversation(String participantID) {
        System.err.println("CHECKING PARTICIPANTID AGAIN");
        if (participantID.startsWith("-")) return false;
        return true;
    }

    Participant matcher;

    Participant director;

    File directorFile;

    File matcherFile;

    int matchercorrectFaceNumber;

    int directorcorrectFaceNumber;

    String fileName;

    String imgsrcD = "";

    String imgsrcM = "";

    int currSetMatcher;

    int currSetDirector;

    int numberOfIncorrectGuessesForCurrentSet = 0;

    public int maxNumberOfIncorrectGuesses = 2;

    public int numberOfConsecutiveCorrect = 0;

    public synchronized String gotoSet(int srch, boolean alternateDM) {
        if (alternateDM) {
            Participant temp = this.director;
            this.director = this.matcher;
            matcher = temp;
        }
        String num = "" + srch;
        if (srch < 10) {
            num = "0" + num;
        }
        boolean foundmatcher = false;
        boolean founddirector = false;
        int searchindex = 0;
        String matcherSelection = "";
        String directorSelection = "";
        while ((!foundmatcher | !founddirector) & searchindex < flist.length) {
            File f = flist[searchindex];
            System.err.println("FILENAMEIS" + f.getName());
            System.err.println("LOoKIING FOR----d0" + num + "_");
            if (f.getName().startsWith("m0" + num + "_")) {
                foundmatcher = true;
                matcherFile = f;
                matcherSelection = f.getName().substring(f.getName().length() - 6, f.getName().length() - 4);
            }
            if (f.getName().startsWith("d0" + num + "_")) {
                founddirector = true;
                directorFile = f;
                directorSelection = f.getName().substring(f.getName().length() - 6, f.getName().length() - 4);
            }
            searchindex++;
        }
        try {
            this.directorcorrectFaceNumber = Integer.parseInt(directorSelection);
            this.matchercorrectFaceNumber = Integer.parseInt(matcherSelection);
        } catch (Exception e) {
            System.err.println("ERROR PARSING" + directorSelection + "-" + matcherSelection);
            System.exit(-222);
        }
        imgsrcD = ":81/" + sbfolder + "/" + directorFile.getName();
        imgsrcM = ":81/" + sbfolder + "/" + matcherFile.getName();
        try {
            if (matcher == null) System.exit(-1231235);
            c.changeWebpageImage_OnServer(matcher, "ID1", imgsrcM);
            c.changeWebpageImage_OnServer(director, "ID1", imgsrcD);
            wid.changeWebpage(matcher.getUsername(), "<html><img src='" + "http://localhost:81/" + sbfolder + "/" + matcherFile.getName() + "'></img>", "", "");
            wid.changeWebpage(director.getUsername(), "<html><img src='" + "http://localhost:81/" + sbfolder + "/" + directorFile.getName() + "'></img>", "", "");
            numberOfIncorrectGuessesForCurrentSet = 0;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("ERRORINMETHOD");
        }
        currSetMatcher = srch;
        currSetDirector = srch;
        c.saveDataToConversationHistory("DATA:" + srch + "-" + directorFile.getName() + matcherFile.getName());
        return "";
    }

    int director1Number;

    int director2Number;

    String testIMGSRCD1;

    String testIMGSRCD2;

    File director1File;

    File director2File;

    Participant director1;

    Participant director2;

    public synchronized void processSelection(Participant p, String t) {
        if (p != matcher) {
            c.sendArtificialTurnToRecipient(p, "****You shouldn't be making this selection - your partner needs to make the selection*****", 0);
            return;
        }
        try {
            t = t.replaceAll(" ", "");
            int i = Integer.parseInt(t.substring(1, t.length()));
            if (i == this.matchercorrectFaceNumber) {
                c.changeWebpageTextAndColour(matcher, "ID1", "Correct.\n You have made " + this.numberOfConsecutiveCorrect + " consecutive correct guesses", "green", "white");
                c.changeWebpageTextAndColour(director, "ID1", "Correct.\nYou have made " + this.numberOfConsecutiveCorrect + " consecutive correct guesses", "green", "white");
                c.sendArtificialTurnToRecipient(director, "****Correct. You have made " + this.numberOfConsecutiveCorrect + " consecutive guesses****", 0);
                c.sendArtificialTurnToRecipient(matcher, "****Correct. You have made " + this.numberOfConsecutiveCorrect + " consecutive guesses****", 0);
                Conversation.printWSln("Main", p.getUsername() + " made the CORRECT selection of " + i + "...moving to trial" + this.currSetMatcher + "--" + this.currSetMatcher + "---CONSECUTIVECORRECT:" + this.numberOfConsecutiveCorrect + "---INCORRECTGUESSESFORCURRENTSET" + this.numberOfIncorrectGuessesForCurrentSet);
                Conversation.printWSln("Main", matcher.getUsername() + "---" + director.getUsername());
                Thread.sleep(5000);
                c.changeWebpageTextAndColour(matcher, "ID1", "", "black", "black");
                c.changeWebpageTextAndColour(director, "ID1", "", "black", "black");
                if (this.numberOfIncorrectGuessesForCurrentSet == 0) numberOfConsecutiveCorrect++;
                this.gotoSet(currSetDirector + 1, true);
            } else {
                numberOfConsecutiveCorrect = 0;
                if (this.numberOfIncorrectGuessesForCurrentSet >= this.maxNumberOfIncorrectGuesses) {
                    c.changeWebpageTextAndColour(matcher, "ID1", "Incorrect\nyou used all your guesses", "red", "white");
                    c.changeWebpageTextAndColour(director, "ID1", "Incorrect\nyou used all your guesses", "red", "white");
                    c.sendArtificialTurnToRecipient(director, "****Incorrect. You used all your guesses****", 0);
                    c.sendArtificialTurnToRecipient(matcher, "****Incorrect. You used all your guesses****", 0);
                    Conversation.printWSln("Main", p.getUsername() + " made INCORRECT selection of " + i + "...EXHAUSTED ALL THE GUESSES...MOVING TO TRIAL:" + this.currSetDirector);
                    Thread.sleep(2000);
                    c.changeWebpageTextAndColour(matcher, "ID1", "", "black", "black");
                    c.changeWebpageTextAndColour(director, "ID1", "", "black", "black");
                    this.gotoSet(currSetDirector + 1, true);
                } else {
                    Conversation.printWSln("Main", p.getUsername() + "made INCORRECT selection of" + i + "..still has " + (this.maxNumberOfIncorrectGuesses - this.numberOfIncorrectGuessesForCurrentSet) + "..guesses left on this trial");
                    numberOfIncorrectGuessesForCurrentSet++;
                }
                c.sendArtificialTurnToRecipient(p, "****INCORRECT SELECTION...You have " + (maxNumberOfIncorrectGuesses - numberOfIncorrectGuessesForCurrentSet) + " guesses left*****", 0);
            }
        } catch (Exception e) {
            c.sendArtificialTurnToRecipient(p, "****YOU MADE A TYPO - TYPE BACKSLASH FOLLOWED BY THE NUMBER *****", 0);
        }
    }

    @Override
    public void initialize(Conversation c, ExperimentSettings expSettings) {
        super.initialize(c, expSettings);
        Arrays.sort(flist);
    }

    @Override
    public void processKeyPress(Participant sender, MessageKeypressed mkp) {
        if (this.mode == mode5TRAININGPHASE) super.processKeyPress(sender, mkp);
    }

    @Override
    public void processPopupResponse(final Participant origin, MessagePopup mp) {
        this.popupReceiptcount++;
        if (this.popupReceiptcount == 2 & this.mode == mode0START) {
            popupReceiptcount = 0;
            this.initiateTestPhaseDirector(this.firstteststartindex, this.firsttestlength, startofnormaltrials);
        } else if (this.popupReceiptcount == 2 & this.mode == this.mode2TESTPHASE_POSTDIRECTOR_PREMATCHER) {
            popupReceiptcount = 0;
            this.initiateTestPhaseMatcher(this.firstteststartindex, this.firsttestlength, startofnormaltrials);
        } else if (this.popupReceiptcount == 2 & this.mode == this.mode4POSTTESTPHASE) {
            popupReceiptcount = 0;
            this.initiateTrainingPhase(startofnormaltrials);
        }
    }

    public void showTestPopupsOnClient(String popupMessage) {
        this.popupReceiptcount = 0;
        c.showPopupOnClientQueryInfo(director, popupMessage, new String[] { "START" }, "Test trial", 0);
        c.showPopupOnClientQueryInfo(matcher, popupMessage, new String[] { "START" }, "Test trial", 0);
    }

    public synchronized String gotoSet2D(int srch) {
        currSetDirector = srch;
        currSetMatcher = 10 + srch;
        String numD = "" + srch;
        if (srch < 10) {
            numD = "0" + numD;
        }
        String numM = "" + (srch + 10);
        boolean foundmatcher = false;
        boolean founddirector = false;
        int searchindex = 0;
        String matcherSelection = "";
        String directorSelection = "";
        while ((!foundmatcher | !founddirector) & searchindex < flist.length) {
            File f = flist[searchindex];
            if (f.getName().startsWith("d0" + numM + "_")) {
                foundmatcher = true;
                matcherFile = f;
                matcherSelection = f.getName().substring(f.getName().length() - 6, f.getName().length() - 4);
            }
            if (f.getName().startsWith("d0" + numD + "_")) {
                founddirector = true;
                directorFile = f;
                directorSelection = f.getName().substring(f.getName().length() - 6, f.getName().length() - 4);
            }
            searchindex++;
        }
        try {
            this.directorcorrectFaceNumber = Integer.parseInt(directorSelection);
            this.matchercorrectFaceNumber = Integer.parseInt(matcherSelection);
        } catch (Exception e) {
            System.err.println("ERROR PARSING" + directorSelection + "-" + matcherSelection);
            System.exit(-223);
        }
        imgsrcD = ":81/" + sbfolder + "/" + directorFile.getName();
        imgsrcM = ":81/" + sbfolder + "/" + matcherFile.getName();
        try {
            if (matcher == null) System.exit(-1231235);
            c.changeWebpageImage_OnServer(matcher, "ID1", imgsrcM);
            c.changeWebpageImage_OnServer(director, "ID1", imgsrcD);
            wid.changeWebpage(matcher.getUsername(), "<html><img src='" + "http://localhost:81/" + sbfolder + "/" + matcherFile.getName() + "'></img>", "", "");
            wid.changeWebpage(director.getUsername(), "<html><img src='" + "http://localhost:81/" + sbfolder + "/" + directorFile.getName() + "'></img>", "", "");
            numberOfIncorrectGuessesForCurrentSet = 0;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("ERRORINMETHOD");
        }
        currSetDirector = srch;
        c.saveDataToConversationHistory("DATA(2D):" + srch + "-" + directorFile.getName() + "--" + matcherFile.getName());
        return "";
    }

    public synchronized String gotoSet2M(int srch) {
        currSetDirector = srch;
        currSetMatcher = 10 + srch;
        String numM = "" + (10 + srch);
        String numD = "" + srch;
        if (srch < 10) {
            numD = "0" + srch;
        }
        boolean foundmatcher = false;
        boolean founddirector = false;
        int searchindex = 0;
        String matcherSelection = "";
        String directorSelection = "";
        while ((!foundmatcher | !founddirector) & searchindex < flist.length) {
            File f = flist[searchindex];
            if (f.getName().startsWith("m0" + numM + "_")) {
                foundmatcher = true;
                matcherFile = f;
                matcherSelection = f.getName().substring(f.getName().length() - 6, f.getName().length() - 4);
            }
            if (f.getName().startsWith("m0" + numD + "_")) {
                founddirector = true;
                directorFile = f;
                directorSelection = f.getName().substring(f.getName().length() - 6, f.getName().length() - 4);
            }
            searchindex++;
        }
        try {
            this.directorcorrectFaceNumber = Integer.parseInt(directorSelection);
            this.matchercorrectFaceNumber = Integer.parseInt(matcherSelection);
        } catch (Exception e) {
            System.err.println("ERROR PARSING" + directorSelection + "-" + matcherSelection + "/---" + numD + "---" + numM);
            e.printStackTrace();
            System.exit(-223);
        }
        imgsrcD = ":81/" + sbfolder + "/" + directorFile.getName();
        imgsrcM = ":81/" + sbfolder + "/" + matcherFile.getName();
        try {
            if (matcher == null) System.exit(-1231235);
            c.changeWebpageImage_OnServer(matcher, "ID1", imgsrcM);
            c.changeWebpageImage_OnServer(director, "ID1", imgsrcD);
            wid.changeWebpage(matcher.getUsername(), "<html><img src='" + "http://localhost:81/" + sbfolder + "/" + matcherFile.getName() + "'></img>", "", "");
            wid.changeWebpage(director.getUsername(), "<html><img src='" + "http://localhost:81/" + sbfolder + "/" + directorFile.getName() + "'></img>", "", "");
            numberOfIncorrectGuessesForCurrentSet = 0;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("ERRORINMETHOD");
        }
        currSetDirector = srch;
        c.saveDataToConversationHistory("DATA(2M):" + srch + "-" + directorFile.getName() + "--" + matcherFile.getName());
        return "";
    }

    String subfolder = "";

    @Override
    public void participantJoinedConversation(Participant p) {
        c.displayNEWWebpage(p, "ID1", "Faces", "", 1420, 401, false, false);
        c.changeJProgressBar(p, "ID1", "", Color.gray, 100);
        wid.displayNEWWebpage(p.getUsername(), p.getUsername(), "", 1420, 401, false, false, false);
        if (c.getParticipants().getAllParticipants().size() < 2) {
            director = p;
            c.changeWebpage(p, "ID1", "", "Waiting for other participant to log in...please wait", "");
        } else if (c.getParticipants().getAllParticipants().size() == 2) {
            matcher = p;
            this.initiateTrainingPhase(21);
        }
    }

    @Override
    public void participantRejoinedConversation(Participant p) {
    }

    @Override
    public void processLoop() {
        super.processLoop();
        if (mode == CCPICTURESTIMULIBACKUP3.mode5TRAININGPHASE) c.resetFromIsTypingtoNormalChatAllAllowedParticipants(super.getIsTypingTimeOut());
    }

    @Override
    public void processChatText(Participant sender, MessageChatTextFromClient mct) {
        if (mode == CCPICTURESTIMULIBACKUP3.mode5TRAININGPHASE && mct.getText().startsWith("/")) {
            this.processSelection(sender, mct.getText());
        } else if (mode == CCPICTURESTIMULIBACKUP3.mode5TRAININGPHASE) {
            super.processChatText(sender, mct);
        }
    }

    public synchronized void initiateTestPhaseDirector(final int startindex, final int numberoftrials, final int returnindex) {
        Thread t = new Thread() {

            public void run() {
                mode = CCPICTURESTIMULIBACKUP3.mode1TESTPHASE_DIRECTOR;
                for (int i = startindex; i < startindex + numberoftrials; i++) {
                    gotoSet2D(i);
                    singleDirectorTrial(i);
                    try {
                        Thread.sleep(pauseBetweenSetsDirectorTrial);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                mode = CCPICTURESTIMULIBACKUP3.mode2TESTPHASE_POSTDIRECTOR_PREMATCHER;
                showTestPopupsOnClient(message2AfterTestSetDirectorBeforeTestSetMatcher);
                c.changeWebpageTextAndColour(director, "ID1", message2AfterTestSetDirectorBeforeTestSetMatcherWINDOW, "black", "white");
                c.changeWebpageTextAndColour(matcher, "ID1", message2AfterTestSetDirectorBeforeTestSetMatcherWINDOW, "black", "white");
            }
        };
        t.start();
    }

    public synchronized void initiateTestPhaseMatcher(final int startindex, final int numberoftrials, final int returnindex) {
        Thread t = new Thread() {

            public void run() {
                mode = CCPICTURESTIMULIBACKUP3.mode3TESTPHASE_MATCHER;
                for (int i = startindex; i < startindex + numberoftrials; i++) {
                    singleMatcherTrial(i);
                    try {
                        Thread.sleep(pauseBetweenSetsMatcherTrial);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                mode = CCPICTURESTIMULIBACKUP3.mode4POSTTESTPHASE;
                c.changeWebpageTextAndColour(director, "ID1", message3AfterTestPhaseWindow, "black", "white");
                c.changeWebpageTextAndColour(matcher, "ID1", message3AfterTestPhaseWindow, "black", "white");
                c.changeJProgressBar(director, "ID1", "", Color.gray, 0);
                c.changeJProgressBar(matcher, "ID1", "", Color.GRAY, 0);
                showTestPopupsOnClient(message3AfterTestPhase);
            }
        };
        t.start();
    }

    public synchronized void initiateTrainingPhase(final int trainingphasestart) {
        this.mode = this.mode5TRAININGPHASE;
        Thread t = new Thread() {

            public void run() {
                c.sendAndEnableLabelDisplayToALLStatusWindowsOfParticipant(matcher, "Status: OK", false, true);
                gotoSet(trainingphasestart, true);
                c.changeClientInterface_allowENTERSEND(matcher, true);
                c.changeClientInterface_allowENTERSEND(director, true);
                c.changeClientInterface_enableTextEntry(matcher);
                c.changeClientInterface_enableTextEntry(director);
            }
        };
        t.start();
    }

    public synchronized void singleMatcherTrial(int startIndex) {
        long startTime = new Date().getTime();
        gotoSet2M(startIndex);
        String sD = (String) htDescriptions.get(currSetDirector);
        c.changeClientInterface_clearMaintextEntryWindow(matcher);
        c.changeClientInterface_clearMaintextEntryWindow(director);
        String sM = (String) htDescriptions.get(currSetMatcher);
        if (sD != null) c.sendArtificialTurnToRecipient(matcher, sD, 0);
        if (sM != null) c.sendArtificialTurnToRecipient(director, sM, 0);
        c.changeClientInterface_allowENTERSEND(director, true);
        c.changeClientInterface_allowENTERSEND(matcher, true);
        c.changeClientInterface_enableTextEntry(matcher);
        c.changeClientInterface_enableTextEntry(director);
        c.sendLabelDisplayToParticipantInOwnStatusWindow(matcher, "Now you have to choose which face is being described", true);
        c.sendLabelDisplayToParticipantInOwnStatusWindow(director, "Now you have to choose which face is being described", true);
        while (new Date().getTime() - startTime < lengthOfEachMatcherTrial) {
            long proportionOfTrialElapsed = 100 * (new Date().getTime() - startTime) / this.lengthOfEachMatcherTrial;
            Color cColor = Color.GREEN;
            if (proportionOfTrialElapsed > 50) cColor = Color.ORANGE;
            if (proportionOfTrialElapsed > 80) cColor = Color.RED;
            long timeleftsecs = ((startTime + lengthOfEachMatcherTrial) - new Date().getTime()) / 1000;
            c.changeJProgressBar(director, "ID1", "You have " + (timeleftsecs) + " seconds remaining", cColor, 100 - (int) proportionOfTrialElapsed);
            c.changeJProgressBar(matcher, "ID1", "You have " + (timeleftsecs) + " seconds remaining", cColor, 100 - (int) proportionOfTrialElapsed);
            try {
                wait(500);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            c.changeClientInterface_disableTextEntry(matcher);
            c.changeClientInterface_disableTextEntry(director);
            c.sendLabelDisplayToParticipantInOwnStatusWindow(matcher, "Loading next set of faces...please wait", true);
            c.sendLabelDisplayToParticipantInOwnStatusWindow(director, "Loading next set of faces...please wait", true);
            c.changeClientInterface_clearTextEntryField(director);
            c.changeClientInterface_clearTextEntryField(matcher);
            c.changeWebpageTextAndColour(director, "ID1", "Loading next set of faces...please wait", "black", "white");
            c.changeWebpageTextAndColour(matcher, "ID1", "Loading next set of faces...please wait", "black", "white");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.err.println("(A)SINGLEMATCHERTRIALLOOKINGFOR: " + sM + "------" + matcherFile.getName() + currSetMatcher);
        System.err.println("(B)SINGLEMATCHERTRIALLOOKINGFOR: " + sD + "------" + directorFile.getName() + currSetDirector);
    }

    public void saveData() {
        Object[] o = htDescriptions.values().toArray();
        for (int i = 0; i < o.length; i++) {
            String is = (String) o[i];
            System.err.println("VALUENINCOLLECTION" + i + ":::::" + is);
        }
        Object[] o2 = htDescriptions.keySet().toArray();
        for (int i = 0; i < o2.length; i++) {
            Integer is2 = (Integer) o2[i];
            System.err.println("KEYSINCOLLECTION" + i + ":::::" + is2);
        }
    }

    public synchronized void singleDirectorTrial(int startIndex) {
        long startTime = new Date().getTime();
        c.changeClientInterface_enableTextEntry(matcher);
        c.changeClientInterface_enableTextEntry(director);
        c.sendLabelDisplayToParticipantInOwnStatusWindow(matcher, "Please type your description of the face into the box below", true);
        c.sendLabelDisplayToParticipantInOwnStatusWindow(director, "Please type your description of the face into the box below", true);
        c.changeClientInterface_allowENTERSEND(director, false);
        c.changeClientInterface_allowENTERSEND(matcher, false);
        while (new Date().getTime() - startTime < lengthOfEachDirectorTrial) {
            long proportionOfTrialElapsed = 100 * (new Date().getTime() - startTime) / this.lengthOfEachDirectorTrial;
            Color cColor = Color.GREEN;
            if (proportionOfTrialElapsed > 50) cColor = Color.ORANGE;
            if (proportionOfTrialElapsed > 80) cColor = Color.RED;
            long timeleftsecs = ((startTime + lengthOfEachDirectorTrial) - new Date().getTime()) / 1000;
            c.changeJProgressBar(director, "ID1", "You have " + (timeleftsecs) + " seconds remaining", cColor, 100 - (int) proportionOfTrialElapsed);
            c.changeJProgressBar(matcher, "ID1", "You have " + (timeleftsecs) + " seconds remaining", cColor, 100 - (int) proportionOfTrialElapsed);
            try {
                wait(500);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            c.changeClientInterface_disableTextEntry(matcher);
            c.changeClientInterface_disableTextEntry(director);
            c.sendLabelDisplayToParticipantInOwnStatusWindow(matcher, "Loading next set of faces...please wait", true);
            c.sendLabelDisplayToParticipantInOwnStatusWindow(director, "Loading next set of faces...please wait", true);
            c.changeClientInterface_clearTextEntryField(director);
            c.changeClientInterface_clearTextEntryField(matcher);
            c.changeWebpageTextAndColour(director, "ID1", "Loading next set of faces...please wait", "black", "white");
            c.changeWebpageTextAndColour(matcher, "ID1", "Loading next set of faces...please wait", "black", "white");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void processWYSIWYGTextInserted(Participant sender, MessageWYSIWYGDocumentSyncFromClientInsert mWYSIWYGkp) {
        System.err.println(mWYSIWYGkp.getTextTyped() + "---" + mWYSIWYGkp.getAllTextInWindow());
        if (sender == director) {
            htDescriptions.put(currSetDirector, mWYSIWYGkp.getAllTextInWindow());
        } else if (sender == matcher) {
            htDescriptions.put(currSetMatcher, mWYSIWYGkp.getAllTextInWindow());
        }
    }

    Hashtable htDescriptions = new Hashtable();

    Hashtable htOrigins = new Hashtable();

    int firstteststartindex = 1;

    int firsttestlength = 5;

    int startofnormaltrials = 21;

    public long lengthOfEachDirectorTrial = 10000;

    public long lengthOfEachMatcherTrial = 10000;

    public long pauseBetweenSetsDirectorTrial = 1000;

    public long pauseBetweenSetsMatcherTrial = 1000;

    int popupReceiptcount = 0;

    int numberOfTrainingTrials = 80;

    int maxPointsbeforeFinalTest = 200;

    String message1BeforeTestSetDirector = "Please read this all of this message.\n" + "After you and the other participant press START,\n" + "the experiment will automatically play multiple\n" + "sets of faces.\n\n " + "For the first 5 sets of faces, you must write a description\n" + "of a particular face which would allow your partner to uniquely\n" + "identify it \n\n" + "for the following 5 sets " + "" + "will be shown " + "" + "5 more sets of faces. for each set of faces you will also be given a description\n " + "a description of one particular face.\n" + "You must try to identify which particular face " + "you must read a description of a face\n" + "and work out" + "" + "\n" + "You will be shown a set of five faces. You will be asked to write a description of one of the faces that will allow your partner to identify which one you are talking about\n" + "" + "must write a description of a that will allow your partner to uniquely identify it\n" + "Set2: You must write a description of a that will allow your partner to uniquely identify it\n" + "" + "" + "\nPlease be as quick and as accurate as possible" + "";

    String message2AfterTestSetDirectorBeforeTestSetMatcherWINDOW = "Next stage";

    String message2AfterTestSetDirectorBeforeTestSetMatcher = "OK. You have now finished describing faces\n" + "You will now be given your partner's descriptions of faces\n" + "and your task is to work out which face they are describing\n" + "For each set you will need to select the face you think they're describing\n" + "One of the faces fits the description - which one is it?";

    String message3AfterTestPhaseWindow = "The test phase is finished. <br>" + "In the next phase you need to cooperate with your partner<br> " + "you see the same faces - one of you has a box round the face<br>" + "the other person has a set of numbered faces<br>" + "The person with the numbers needs to select the same face<br>" + "<br>";

    String message3AfterTestPhase = "OK. You have now finished the test phase" + "Now you need to collaborate with the other partner\n" + "On each trial, both of you will have the same faces in front of you.\n" + "The order of the faces is randomly jumbled up\n" + "One of you will have a set with one face that is selected in a box\n" + "The other person will have a set with only numbers\n" + "Your task is to complete it as quickly and as accurately as possible\n" + "Once you have reached " + this.maxPointsbeforeFinalTest + " points, you are free to go\n" + "So if you finish all the sets in 30 mins you will still get paid in full!\n" + "So please be as quick and as accurate as possible!";
}
