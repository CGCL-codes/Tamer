package com.discourse;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.lang.String;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.StreamTokenizer;

/**
 * Discourse Interactions relate words appropriately to sentences they can fit.
 * Servers should use DiscourseInteractions directly.
 *
 * @author tbrick
 */
public class DiscourseInteraction {

    protected boolean DEBUG = false;

    protected Map<String, DiscourseOption> wordSyntax;

    protected Map<String, DiscourseMatcher> wordSenses;

    protected Map<String, DiscourseSentence> syntaxNames;

    protected Map<String, String> typeTypes;

    protected DiscourseNode current;

    protected String name = "";

    protected DiscourseStructure thisDiscourse;

    protected boolean isSubInteraction = false;

    protected boolean isSimulation = false;

    protected boolean complete = false;

    protected boolean waiting = false;

    protected String binding = "";

    protected String command;

    private long key;

    public boolean act() {
        return act(true);
    }

    /** Register the action associated with the current utterance, and respond in kind.
     *  There is some discussion over which parts of the act() function should be taken on
     *      by the DiscourseInteraction, and which should be taken on by the DiscourseSentence.
     *      Tough to say, just now.
     *  @param complete true if the utterance is complete.
     *  @return a boolean stating whether the action was attempted.
     */
    public boolean act(boolean complete) {
        int endInd = 0;
        if (thisDiscourse.DEBUG > 7) {
            System.out.println("DiscInt: ACTING! Current is: " + current);
        }
        if (thisDiscourse.DEBUG > 5) {
            System.out.print(name + " invoking the command--");
        }
        if (current == null) {
            if (thisDiscourse.DEBUG > 5) {
                System.out.println("but no current node exists.");
            }
            return false;
        }
        if (waiting) {
            if (thisDiscourse.DEBUG > 5) {
                System.out.println("waiting");
            }
            String newBinding = thisDiscourse.getPastReferentWithType(binding);
            if (newBinding.equals("")) {
                if (thisDiscourse.DEBUG > 7) {
                    System.out.println(name + " waiting but unbound; resetting and returning.");
                }
                reset();
                return false;
            }
            command = command.replace("?" + binding, newBinding);
            waiting = false;
        } else {
            if (thisDiscourse.DEBUG > 5) {
                System.out.print("Not waiting, trimming command " + command);
            }
            try {
                command = current.act(complete).trim();
            } catch (Exception ex) {
                System.out.println("Act exception: " + ex);
                ex.printStackTrace();
            }
            if (thisDiscourse.DEBUG > 5) {
                System.out.println(" to " + command);
            }
            setKey(-1);
        }
        if (complete && thisDiscourse.myServer.beImmediate && thisDiscourse.myServer.useVision) {
            if (command.startsWith("look") || command.startsWith("siftCheck") || command.startsWith("siftCount")) {
                thisDiscourse.myServer.pubCallEx(thisDiscourse.myServer.seer, "stopTracking");
            } else {
                thisDiscourse.myServer.pubCallEx(thisDiscourse.myServer.seer, "panTilt", 90, 75);
            }
        }
        if (command == null) {
            if (thisDiscourse.DEBUG > 3) {
                System.out.println("Null returned from action, somehow.");
            }
        }
        if (command.contains(System.getProperty("line.separator"))) {
            command = command.split(System.getProperty("line.separator"))[0];
            if (thisDiscourse.DEBUG > 6) {
                System.out.println("Multiple command responses.  Returning only the first: " + command);
            }
        }
        if (!command.equals("")) {
            if (thisDiscourse.DEBUG > 5) {
                System.out.println("Preprocessing Command: " + command);
            }
            if (command.startsWith("start") || command.startsWith("moveTo")) {
                if (command.startsWith("moveTo")) {
                    if (thisDiscourse.DEBUG > 7) {
                        System.out.println("Recognized moveTo Command.");
                    }
                    String words[] = command.split(":");
                    System.out.println("moveTo command: " + command + ", words[2]: " + words[2]);
                    if (words[2].trim().equalsIgnoreCase("here")) {
                        words[0] = words[0].replace("moveTo", "moveToLoc");
                        System.out.println("Replaced words[0] with: " + words[0]);
                        command = words[0] + ":" + words[1] + ":" + words[2];
                    }
                } else if (!command.startsWith("startmove")) {
                    if (thisDiscourse.DEBUG > 7) {
                        System.out.println("Recognized Turn Command.");
                    }
                    command = command.replace("ward", "");
                    command = command.replace("for", "forward");
                    command = command.replace("front", "forward");
                    command = command.replace("feet", "foot");
                    command = command.replace("meters", "meter");
                    command = command.replace("degrees", "degree");
                    command = command.replace("foot", "?units");
                    command = command.replace("meter", "?units");
                    if (complete && command.substring(5).startsWith("edge")) {
                        if (!(command.substring(9).startsWith("left") || command.substring(9).startsWith("right") || command.substring(9).startsWith("forward") || command.substring(9).startsWith("back") || command.substring(9).startsWith("?"))) {
                            command = "";
                        }
                    } else if (complete && (!(command.substring(5).startsWith("left") || command.substring(5).startsWith("right") || command.substring(4).startsWith("?")))) {
                        command = "";
                    } else if (complete && ((endInd = command.lastIndexOf(":?extent:?units")) > 0)) {
                        command = command.substring(0, endInd);
                    }
                } else {
                    if (thisDiscourse.DEBUG > 7) {
                        System.out.println("Recognized Move Command.");
                    }
                    command = command.replace("up", "upwards");
                    command = command.replace("down", "downwards");
                    command = command.replace("straight", "");
                    command = command.replace("forward", "");
                    command = command.replace("front", "");
                    command = command.replace("backward", "back");
                    command = command.replace("feet", "foot");
                    command = command.replace("meters", "meter");
                    command = command.replace("degrees", "degree");
                    command = command.replace("degree", "?units");
                    if (complete && (command.substring(9).equals("upwards") || command.substring(9).equals("downwards") || command.substring(8).startsWith("?"))) {
                        if (thisDiscourse.DEBUG > 7) {
                            System.out.println("Altering MOVE command.");
                        }
                        command = "";
                    } else if (complete && ((endInd = command.lastIndexOf(":?extent:?units")) > 0)) {
                        command = command.substring(0, endInd);
                    }
                }
            } else if (command.startsWith("look") && !command.startsWith("lookAt")) {
                if (thisDiscourse.DEBUG > 7) {
                    System.out.println("Recognized Look Command.");
                }
                int end = command.indexOf(":");
                if (!(command.substring(4).contains("left") || command.substring(4).contains("right") || command.substring(4).contains("up") || command.substring(4).contains("down") || command.substring(3).startsWith("?"))) {
                    if (thisDiscourse.DEBUG > 7) {
                        System.out.println("Altering LOOK command.");
                    }
                    command = "lookforward";
                }
                if (complete && thisDiscourse.myServer.beImmediate) {
                    if (thisDiscourse.DEBUG > 7 || true) {
                        System.out.println("Look command is " + command);
                    }
                    if (command.equals("lookforward")) {
                        thisDiscourse.myServer.moveCameras(150, 115);
                    } else {
                        String words[] = command.split(":");
                        int pan = 150;
                        int tilt = 0;
                        if (words[0].substring(4).equals("down")) {
                        }
                    }
                }
            } else if (command.startsWith("lookAt")) {
            } else if (command.startsWith("upload")) {
                if (thisDiscourse.DEBUG > 7) {
                    System.out.println("Recognized upload command.");
                }
            } else if (command.startsWith("siftCheck") || command.startsWith("siftCount")) {
                if (thisDiscourse.DEBUG > 7) {
                    System.out.println("Recognized count command.");
                }
                if (complete && thisDiscourse.myServer.beImmediate) {
                    String words[] = command.split(":");
                    Set<DiscourseReferent> drs = new TestDiscourseVision().getObjectsWithRestrictionAndType(null, words[3]);
                    DiscourseReferent dr = null;
                    if (drs == null) {
                        thisDiscourse.myServer.speakWords("I do not know what a " + words[3] + " is.");
                        if (thisDiscourse.myServer.useVision) {
                            thisDiscourse.myServer.pubCallEx(thisDiscourse.myServer.seer, "panTilt", 90, 75);
                            thisDiscourse.myServer.pubCallEx(thisDiscourse.myServer.seer, "startTracking");
                        }
                    } else {
                        for (DiscourseReferent dr1 : drs) {
                            dr = dr1;
                        }
                        if (dr != null) {
                            thisDiscourse.myServer.confirmReferent(dr);
                            thisDiscourse.myServer.speakWords("Yes.  I see one.");
                        } else {
                            thisDiscourse.myServer.speakWords("No, I do not.");
                        }
                        if (thisDiscourse.myServer.useVision) {
                            thisDiscourse.myServer.pubCallEx(thisDiscourse.myServer.seer, "panTilt", 90, 75);
                        }
                    }
                }
            } else if (command.startsWith("siftVisionLearn")) {
                if (thisDiscourse.DEBUG > 7) {
                    System.out.println("Recognized learn command.");
                }
                if (complete && thisDiscourse.myServer.beImmediate) {
                    String words[] = command.split(":");
                    TestDiscourseVision.learnedBook = true;
                    thisDiscourse.myServer.speakWords("Okay. " + words[3]);
                }
            } else if (complete && command.contains("Between")) {
                if (thisDiscourse.DEBUG > 7) {
                    System.out.println("Recognized complex relationship-getting command.");
                }
                String words[] = command.split(":");
                if (words.length < 6) {
                    if (thisDiscourse.DEBUG > 7) {
                        System.out.println("Relationship-command (" + command + ") missing arguments.");
                    }
                }
                if (thisDiscourse.DEBUG > 7) {
                    System.out.println("words[0] = " + words[0]);
                }
                if (!words[3].equals("?theme")) {
                    thisDiscourse.setPastReferent("theme", words[3]);
                }
                if (words[4].equals("?reference")) {
                    words[0] = words[0].replace("Between", "Is");
                    words[4] = "";
                }
                if (words[5].equals("?perspectivename")) {
                    words[5] = "me";
                }
                if (words[6].contains("front") || words[6].contains("forward") || words[6].contains("straight")) {
                    words[6] = "infront";
                }
                if (words[6].contains("back") || words[6].contains("behind") || words[6].contains("backward")) {
                    words[6] = "behind";
                }
                command = words[0] + ":" + words[1] + ":" + words[2] + ":" + words[3] + (words[4].equals("") ? "" : ":" + words[4]);
                if (words.length > 5) {
                    command = command + ":" + words[5];
                    if (words.length > 6) {
                        command = command + ":" + words[6];
                    }
                }
                if (false && complete) {
                    String rel = new TestDiscourseVision().getRelationship(words[3], words[4], words[5]);
                    if (rel.contains(words[6])) {
                        thisDiscourse.myServer.speakWords("Yes.");
                    } else {
                        thisDiscourse.myServer.speakWords("No.");
                        if (thisDiscourse.DEBUG > 7) {
                            System.out.println(words[6] + " != " + rel);
                        }
                    }
                }
            } else if (command.contains("CheckPreconditions")) {
                if (complete) {
                    String words[] = command.split(":");
                    if (words.length > 5) {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                        if (checkPreconditions(Integer.parseInt(words[3]), words[4], Integer.parseInt(words[5]))) {
                            thisDiscourse.myServer.speakWords("Yes. One could");
                        } else {
                            thisDiscourse.myServer.speakWords("No. A precondition is not met.");
                        }
                    }
                }
                if (command.contains("put")) {
                    String words[] = command.split(":");
                    if (!words[3].contains("?")) {
                        words[3] = "box" + words[3];
                    }
                    if (!words[5].contains("?")) {
                        words[5] = "box" + words[5];
                    }
                    command = words[0] + ":" + words[1] + ":" + words[2] + ":" + words[3] + ":" + words[4] + ":" + words[5];
                }
            } else if (complete && (command.startsWith("beginQuestionnaire") || command.startsWith("beginquestionnaire"))) {
                if (command.equalsIgnoreCase("beginQuestionnaire:?actor")) {
                    thisDiscourse.myServer.changeInteraction("Questionnaire_Interaction");
                    command = "beginQuestionnaire";
                } else {
                    command = "";
                }
            } else if (command.startsWith("begin")) {
                if (thisDiscourse.DEBUG > 7) {
                    System.out.println("Recognized begin command.");
                }
                if (command.substring(5).equalsIgnoreCase("rounds:?actor")) {
                    command = "beginpatrol:?actor";
                }
            } else if (complete && command.contains("greeting")) {
                if (thisDiscourse.myServer.useVision) {
                    thisDiscourse.myServer.pubCallEx(thisDiscourse.myServer.seer, "panTilt", 90, 75);
                    thisDiscourse.myServer.pubCallEx(thisDiscourse.myServer.seer, "startTracking");
                }
                thisDiscourse.myServer.speakWords("Hello.  Knock knock.");
            } else if (complete && (command.contains("what") || command.contains("who"))) {
                thisDiscourse.myServer.speakWords("The Interrupting Cow");
            } else if (command.contains("interrupt")) {
                thisDiscourse.myServer.speakWords("Moo.  Ha ha ha ha ha");
            } else {
            }
        } else {
            String matchString = current.showMatch();
            if (thisDiscourse.DEBUG > 5) {
                System.out.println("Found Non-command.  Checking matches: " + matchString);
            }
            String matches[] = matchString.split(" ");
            for (String match : matches) {
                int splitter = match.indexOf('|');
                if (splitter < 0) {
                    continue;
                }
                if (complete && match.substring(0, splitter).equalsIgnoreCase("integer") && (Integer.parseInt(match.substring(splitter + 1)) > 6 || Integer.parseInt(match.substring(splitter + 1)) < 0)) {
                    thisDiscourse.reply(match.substring(splitter + 1) + " is too " + ((Integer.parseInt(match.substring(splitter + 1)) < 0) ? "low." : "high."), "", null);
                    return false;
                }
                if (complete && thisDiscourse.addBindingIfRequested(match.substring(splitter + 1), match.substring(0, splitter))) {
                    if (thisDiscourse.DEBUG > 7) {
                        System.out.println("Tried to match " + match.substring(splitter + 1) + " with type " + match.substring(0, splitter));
                    }
                    command = command + " " + match.substring(0, splitter) + " = " + match.substring(splitter + 1);
                }
            }
            if (thisDiscourse.DEBUG > 5) {
                System.out.println("Postprocessed Non-command Utterance: " + command);
            }
            return true;
        }
        if (thisDiscourse.DEBUG > 7) {
            System.out.println("Entering Secondary Processing of command: " + command);
        }
        if (!command.equals("")) {
            try {
                if (thisDiscourse.DEBUG > 8) {
                    System.out.println("Completing action: " + command);
                }
                String commandParts[] = command.split(":");
                command = "";
                for (int i = 0; i < commandParts.length; i++) {
                    if (commandParts[i].contains("?")) {
                        if (thisDiscourse.DEBUG > 8) {
                            System.out.print("Replacing substring " + commandParts[i].substring(1) + " ");
                        }
                        String s = thisDiscourse.getPastReferentWithType(commandParts[i].substring(1));
                        if (!s.equals("")) {
                            commandParts[i] = s;
                            if (thisDiscourse.DEBUG > 8) {
                                System.out.print(" with " + s + ".");
                            }
                        }
                        if (thisDiscourse.DEBUG > 8) {
                            System.out.println();
                        }
                    }
                    command = command + (i == 0 ? "" : ":") + commandParts[i];
                }
                if (thisDiscourse.DEBUG > 8) {
                    System.out.println("Completed action: " + command);
                }
                if (complete && command.contains("?") && false) {
                    int begin = command.indexOf('?');
                    int end = command.length();
                    if (command.substring(begin).contains(":")) {
                        end = command.indexOf(':') + begin;
                    }
                    String newType = command.substring(begin + 1, end);
                    binding = newType;
                    boolean which = (newType.equals("direction") || newType.equals("datatype"));
                    newSubinteraction(newType, which ? new HashSet<String>() : null, true);
                }
                if (thisDiscourse.DEBUG > 7) {
                    System.out.println("Command for output is: " + command + " (complete: " + complete + ")");
                }
                thisDiscourse.addCommand(key, command, (complete ? 1 : .5), this);
                return true;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        if (thisDiscourse.DEBUG > 5) {
            System.out.println("but none exists.");
        }
        return false;
    }

    public boolean checkPreconditions(int theme, String relationship, int goal) {
        if (theme == 4 || goal == 4) {
            return false;
        }
        return true;
    }

    public void setKey(long theKey) {
        if (thisDiscourse.DEBUG > 9) {
            System.out.println("Setting key for " + name + " to + " + theKey + " from " + key);
        }
        key = theKey;
    }

    public boolean newSubinteraction(String type, Set<String> possibles, boolean prepsAllowed) {
        return newSubinteraction(type, possibles, prepsAllowed, null) > 0;
    }

    public long newSubinteraction(String type, Set<String> possibles, boolean prepsAllowed, String question) {
        long key = -1;
        try {
            if (thisDiscourse.DEBUG > 8) {
                System.out.println("Creating new subinteraction for type " + type);
            }
            ArrayList<DiscourseNode> sentence = new ArrayList<DiscourseNode>();
            DiscourseInteraction newInteraction = new DiscourseInteraction(type + " Finder", thisDiscourse, true);
            if (type.equalsIgnoreCase("Integer")) {
                if (wordSenses.containsKey("$DIGIT$")) {
                    sentence.add(wordSenses.get("$DIGIT$").clone());
                    DiscourseSentence ds = new DiscourseSentence(sentence, false, thisDiscourse);
                    newInteraction.addSyntax("$DIGIT$", ds);
                }
                if (question == null) {
                    question = "How many " + type;
                }
            } else {
                if (prepsAllowed) {
                    sentence.add(new DiscourseNode("preposition", "", true, null, "", "", thisDiscourse));
                }
                sentence.add(new DiscourseNode("article", "", true, null, "", "definite", thisDiscourse));
                sentence.add(new DiscourseNoun(type, "", false, null, "", "", "", thisDiscourse));
                if (type.endsWith("type")) {
                    sentence.add(new DiscourseNode(type.substring(0, type.indexOf("type")), "", true, null, "", "", thisDiscourse));
                }
                DiscourseSentence ds = new DiscourseSentence(sentence, false, thisDiscourse);
                for (String s : wordSenses.keySet()) {
                    DiscourseMatcher dm = wordSenses.get(s);
                    String theRole = dm.getRole();
                    if (theRole.contains("article") || theRole.contains(type) || theRole.contains("preposition") || theRole.contains("data")) {
                        newInteraction.addSyntax(wordSenses.get(s), ds);
                    }
                }
            }
            DiscourseSentence ds = new DiscourseSentence(sentence, false, thisDiscourse);
            if (question == null) {
                question = (possibles == null ? "What " : "Which ") + type;
            }
            key = thisDiscourse.reply(question, type, newInteraction);
            waiting = true;
            complete = false;
            return key;
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    public boolean respond() {
        if (current == null) {
            return false;
        }
        if (current.isComplete()) {
            String response = current.respond();
            if (!response.equals("")) {
                thisDiscourse.addResponse(response);
                return true;
            }
        }
        return false;
    }

    public boolean isWaiting() {
        return waiting;
    }

    public void setWaiting(boolean isWaiting) {
        waiting = isWaiting;
    }

    public void setDiscourseStructure(DiscourseStructure ds) {
        thisDiscourse = ds;
    }

    public JPanel getGUIData() {
        if (current == null) {
            if (thisDiscourse.pastMatchers().size() == 0) {
                JPanel nilPanel = thisDiscourse.getLastUtterance().getGUIData(false);
                nilPanel.add(new JTextField("No Current Utterance."));
                return nilPanel;
            } else {
                JPanel floatPanel = new JPanel();
                floatPanel.setLayout(new javax.swing.BoxLayout(floatPanel, javax.swing.BoxLayout.X_AXIS));
                for (DiscourseMatcher dm : thisDiscourse.pastMatchers()) {
                    JPanel nullPanel = dm.getGUIData(true);
                    if (nullPanel != null) floatPanel.add(nullPanel);
                }
            }
        }
        return current.getGUIData(false);
    }

    public String logData() {
        if (current == null) {
            StringBuilder logged = new StringBuilder();
            boolean atLeastOne = false;
            for (DiscourseMatcher dm : thisDiscourse.pastMatchers()) {
                if (atLeastOne) {
                    logged.append(" ");
                }
                logged.append(dm.logData(true));
                atLeastOne = true;
            }
            return logged.toString();
        } else {
            return current.logData(false);
        }
    }

    public String showMatch() {
        if (current == null) {
            return "No current match.";
        } else {
            return current.showMatch();
        }
    }

    public void unMatch() {
        current.unMatch();
    }

    public void setDebug(boolean in) {
    }

    public String getName() {
        return name;
    }

    public boolean isComplete() {
        if (thisDiscourse.DEBUG > 7) {
            System.out.println("Is " + name + " complete?  Current is:" + current + ".");
        }
        if (current != null) {
            return current.isComplete();
        }
        if (waiting) {
            if (thisDiscourse.isBound(key)) {
                complete = true;
                reset();
            }
            return complete;
        }
        return thisDiscourse.pastMatchers().isEmpty();
    }

    public boolean complete() {
        boolean completed = false;
        if (isSubInteraction) {
            if (thisDiscourse.DEBUG > 8) {
                System.out.println("SubInteraction Completing.");
            }
            if (current != null) {
                String matches = current.showMatch();
                if (thisDiscourse.DEBUG > 8) {
                    System.out.println("Subinteraction has match: " + matches);
                }
                String words[] = matches.split(" ");
                for (String s : words) {
                    if (s.contains(command)) {
                        if (thisDiscourse.DEBUG > 8) {
                            System.out.println("Found command in segment: " + s);
                        }
                        int end = s.indexOf("|");
                    }
                }
            }
            thisDiscourse.removeSubInteraction();
            return completed;
        }
        if (current != null) {
            completed = current.complete();
            if (thisDiscourse.DEBUG > 7) {
                System.out.println(name + (completed ? "" : " not") + " completed.");
            }
            complete = true;
            return true;
        }
        return false;
    }

    public void reset() {
        if (current != null) {
            thisDiscourse.setLastUtterance(current);
            current.reset();
            thisDiscourse.newSentence();
            if (isSubInteraction) {
                thisDiscourse.myServer.removeSubInteraction();
            }
            current = null;
            complete = false;
            waiting = false;
        }
        setKey(-1);
        thisDiscourse.myServer.logData(name + " beginning new utterance.");
        if (thisDiscourse.DEBUG > 6) {
            System.out.println(name + " has reset.");
        }
    }

    public String getNextRoles() {
        if (current == null) {
            return new String("");
        }
        return current.getNextRoles();
    }

    public boolean match(String incoming) {
        DiscourseMatcher dm = null;
        if (waiting) {
            reset();
        }
        if (incoming.equals("")) {
            return false;
        }
        if (thisDiscourse.DEBUG > 8) {
            System.out.println(name + " matching word " + incoming + ".");
        }
        boolean isNumber = false;
        if (Character.isDigit(incoming.charAt(0))) {
            isNumber = true;
        }
        if (isNumber) {
            if (thisDiscourse.DEBUG > 5) {
                System.out.println("Found numerical string.  Value is " + Integer.parseInt(incoming));
            }
            if (wordSenses.containsKey("$DIGIT$")) {
                dm = wordSenses.get("$DIGIT$").clone();
                Integer value = Integer.parseInt(incoming);
                dm.setWords(value.toString());
                if (wordSenses.containsKey(incoming)) {
                    dm.add(wordSenses.get(incoming));
                }
                if (thisDiscourse.DEBUG > 5) {
                    System.out.println("Found " + dm.getWords() + ", with role " + dm.getRole() + ".");
                }
            }
        } else if (wordSenses.containsKey(incoming)) {
            dm = wordSenses.get(incoming).clone();
            if (thisDiscourse.DEBUG > 5) {
                System.out.println("Found word " + dm.getWords() + " with role " + dm.getRole() + ".");
            }
        } else {
            if (thisDiscourse.DEBUG > 5) {
                System.out.println(name + " couldn't match word " + incoming + ".  Assuming it's part of a name.");
            }
            if (incoming.lastIndexOf("s") == incoming.length()) {
                incoming = incoming.substring(0, incoming.length() - 2);
                if (thisDiscourse.DEBUG > 5) {
                    System.out.println(name + " found name, probably plural.  Truncating to " + incoming);
                }
            }
            dm = new DiscourseMatcher(new DiscourseNoun("name", incoming, false, null, incoming, "", "+inanimate", thisDiscourse), thisDiscourse, thisDiscourse.DEBUG > 7);
            dm.add(new DiscourseMatcher(new DiscourseNoun("name", incoming, false, null, incoming, "", "+animated", thisDiscourse), thisDiscourse, thisDiscourse.DEBUG > 7));
        }
        if (dm == null) {
            if (thisDiscourse.DEBUG > 5) {
                System.out.println("Interaction match totally failed.");
            }
            thisDiscourse.myServer.logData("Interaction match failed.");
            return false;
        }
        thisDiscourse.addMatcher(dm);
        if (current == null) {
            if (thisDiscourse.DEBUG > 6) {
                System.out.print("No current syntax tree: ");
            }
            if (wordSyntax.containsKey(incoming)) {
                if (thisDiscourse.DEBUG > 6) {
                    System.out.println(" Using syntax for " + incoming + " as current tree.");
                }
                current = wordSyntax.get(incoming).clone();
            } else {
                if (thisDiscourse.DEBUG > 6) {
                    System.out.println(" None found for word " + incoming + ".  Returning.");
                }
                thisDiscourse.myServer.logData("No verb.  So far: " + logData());
                return true;
            }
            if (!isSubInteraction) {
                for (DiscourseMatcher node : thisDiscourse.pastMatchers()) {
                    if (!current.match(node)) {
                        if (thisDiscourse.DEBUG > 5) {
                            System.out.println("Previously stacked word " + node.getWords() + " did not match. Aborting.");
                        }
                        thisDiscourse.myServer.logData("Stacked word failure.  So far: " + logData());
                        return false;
                    }
                }
            }
        }
        boolean output = current.match(dm);
        thisDiscourse.myServer.logData(logData());
        return output;
    }

    public Set<DiscourseReferent> getPossibleReferents() {
        if (current instanceof DiscourseNounPhrase) {
            return ((DiscourseNounPhrase) current).getPossibleReferents();
        } else if (current instanceof DiscourseNoun) {
            return ((DiscourseNoun) current).getPossibleReferents();
        } else return new HashSet<DiscourseReferent>();
    }

    public boolean constrainPossibleReferents(String incoming) {
        if (isSimulation && (current instanceof DiscourseNounPhrase)) {
            return ((DiscourseNounPhrase) current).constrainPossibleReferents(incoming);
        } else return false;
    }

    /** Creates a new instance of DiscourseInteraction */
    public DiscourseInteraction(String interactionName, DiscourseStructure structure) {
        name = interactionName;
        thisDiscourse = structure;
        isSubInteraction = false;
        syntaxNames = new HashMap<String, DiscourseSentence>();
        wordSenses = new HashMap<String, DiscourseMatcher>();
        wordSyntax = new HashMap<String, DiscourseOption>();
        typeTypes = new HashMap<String, String>();
    }

    public DiscourseInteraction(String interactionName, DiscourseStructure structure, boolean subInteraction) {
        name = new String(interactionName);
        thisDiscourse = structure;
        isSubInteraction = subInteraction;
        syntaxNames = new HashMap<String, DiscourseSentence>();
        wordSenses = new HashMap<String, DiscourseMatcher>();
        wordSyntax = new HashMap<String, DiscourseOption>();
        typeTypes = new HashMap<String, String>();
    }

    public DiscourseInteraction(String interactionName, Map<String, DiscourseMatcher> senses, Map<String, String> typer, DiscourseStructure theDiscourse, boolean subInteraction, String bindingToFind, Map<String, DiscourseOption> syntax, boolean shouldDebug) {
        if (theDiscourse.DEBUG > 5) {
            System.out.println("Interaction " + interactionName + " cloning.");
        }
        wordSenses = senses;
        wordSyntax = syntax;
        syntaxNames = new HashMap<String, DiscourseSentence>();
        typeTypes = typer;
        name = new String(interactionName);
        thisDiscourse = theDiscourse;
        isSubInteraction = subInteraction;
        command = bindingToFind;
    }

    public DiscourseInteraction(DiscourseStructure structure, String filename) {
        if (structure != null) {
            thisDiscourse = structure;
        } else {
            thisDiscourse = new DiscourseStructure((DiscourseServerImpl) null);
        }
        if (!filename.trim().equalsIgnoreCase("")) {
            System.out.println("Loading file '" + filename + "'");
            this.fileRead(filename);
        } else {
            name = "Unnamed";
            isSubInteraction = false;
            wordSenses = new HashMap<String, DiscourseMatcher>();
            wordSyntax = new HashMap<String, DiscourseOption>();
            syntaxNames = new HashMap<String, DiscourseSentence>();
            typeTypes = new HashMap<String, String>();
        }
    }

    public DiscourseInteraction(boolean shouldDebug, Map<String, DiscourseOption> wordSyntaxes, Map<String, DiscourseMatcher> wordSense, Map<String, String> typeType, String theName, DiscourseStructure theDiscourse, boolean subInteraction) {
        wordSenses = new HashMap<String, DiscourseMatcher>();
        wordSyntax = new HashMap<String, DiscourseOption>();
        syntaxNames = new HashMap<String, DiscourseSentence>();
        typeTypes = new HashMap<String, String>();
        for (String s : wordSyntaxes.keySet()) {
            wordSyntax.put(new String(s), wordSyntaxes.get(s).clone());
            wordSyntax.get(s).reset();
        }
        for (String s : wordSense.keySet()) {
            wordSenses.put(new String(s), wordSense.get(s).clone());
            wordSenses.get(s).reset();
        }
        for (String s : typeType.keySet()) {
            typeTypes.put(new String(s), new String(typeType.get(s)));
        }
        name = new String(theName + "2");
        thisDiscourse = theDiscourse;
        isSubInteraction = subInteraction;
        waiting = false;
        complete = false;
    }

    public boolean addSyntax(DiscourseNode word, DiscourseNode newSyntax) {
        if (word == null) {
            return false;
        }
        String text = word.getWords();
        String type = word.getRole();
        if (wordSenses.containsKey(text)) {
            if (thisDiscourse.DEBUG > 7) {
                System.out.print(name + ": " + word.getRole() + " added to existing roles for " + word.getWords());
            }
            wordSenses.get(text).add(word.clone());
        } else if (word instanceof DiscourseMatcher) {
            if (thisDiscourse.DEBUG > 7) {
                System.out.print(name + ": " + word.getRole() + " created for " + word.getWords());
            }
            wordSenses.put(text, ((DiscourseMatcher) word).clone());
        } else {
            if (thisDiscourse.DEBUG > 7) {
                System.out.print(name + ": " + word.getRole() + " made for " + word.getWords());
            }
            wordSenses.put(text, new DiscourseMatcher(word, thisDiscourse, thisDiscourse.DEBUG > 7));
        }
        if (newSyntax != null) {
            if (thisDiscourse.DEBUG > 7) {
                System.out.print(" and syntax " + newSyntax.getRole() + " attached.");
            }
            if (wordSyntax.containsKey(text)) {
                wordSyntax.get(text).add(newSyntax);
            } else if (newSyntax instanceof DiscourseOption) {
                wordSyntax.put(text, (DiscourseOption) newSyntax);
            } else {
                wordSyntax.put(text, new DiscourseOption(newSyntax, false, thisDiscourse, DEBUG));
            }
        }
        if (thisDiscourse.DEBUG > 7) {
            System.out.println();
        }
        return true;
    }

    public boolean addSyntax(String word, DiscourseNode newSyntax) {
        if (word.equals("")) {
            return false;
        }
        if (newSyntax != null) {
            if (thisDiscourse.DEBUG > 7) {
                System.out.print("Word " + word + " gets syntax " + newSyntax.getRole() + " attached.");
            }
            if (wordSyntax.containsKey(word)) {
                wordSyntax.get(word).add(newSyntax);
            } else if (newSyntax instanceof DiscourseOption) {
                wordSyntax.put(word, (DiscourseOption) newSyntax);
            } else {
                wordSyntax.put(word, new DiscourseOption(newSyntax, false, thisDiscourse, DEBUG));
            }
        }
        if (thisDiscourse.DEBUG > 7) {
            System.out.println();
        }
        return true;
    }

    public void printNextRoles() {
        if (current != null) {
            if (thisDiscourse.DEBUG > 5) {
                System.out.println(current.getNextRoles());
            }
        }
    }

    public boolean isSubInteraction() {
        return isSubInteraction;
    }

    public boolean isFinished() {
        return false;
    }

    public DiscourseInteraction clone() {
        DiscourseInteraction di = new DiscourseInteraction(thisDiscourse.DEBUG > 5, wordSyntax, wordSenses, typeTypes, name, thisDiscourse, isSubInteraction);
        return di;
    }

    public DiscourseInteraction simulate() {
        DiscourseInteraction di = new DiscourseInteraction(thisDiscourse.DEBUG > 5, wordSyntax, wordSenses, typeTypes, name, thisDiscourse, isSubInteraction);
        di.isSimulation = true;
        return di;
    }

    public DiscourseInteraction createSubInteraction(String binding, Map<String, DiscourseOption> newSyntax, Map<String, DiscourseMatcher> newSenses) {
        if (thisDiscourse.DEBUG > 5) {
            System.out.println("Creating Subinteraction.");
        }
        thisDiscourse.myServer.logData("Creating subinteraction.");
        DiscourseInteraction newInteraction = new DiscourseInteraction(binding + "Finder", newSenses, typeTypes, thisDiscourse, true, binding, newSyntax, DEBUG);
        return newInteraction;
    }

    /** File Reading Functions **/
    public void fileRead(String filename) {
        StreamTokenizer stok;
        if (thisDiscourse.DEBUG > 5) {
            System.out.println("Creating Discourse Interaction from file " + filename + ".");
        }
        wordSenses = new HashMap<String, DiscourseMatcher>();
        wordSyntax = new HashMap<String, DiscourseOption>();
        typeTypes = new HashMap<String, String>();
        syntaxNames = new HashMap<String, DiscourseSentence>();
        if (filename.length() != 0) {
            try {
                LineNumberReader lnr = new LineNumberReader(new FileReader(filename));
                stok = new StreamTokenizer(lnr);
                stok.wordChars('!', '}');
                stok.commentChar('#');
                stok.eolIsSignificant(true);
            } catch (FileNotFoundException ex) {
                System.err.println("File '" + filename + "' not found.");
                return;
            }
            try {
                int mode = 0;
                while (stok.nextToken() != StreamTokenizer.TT_EOF) {
                    switch(stok.ttype) {
                        case StreamTokenizer.TT_EOL:
                            break;
                        case StreamTokenizer.TT_WORD:
                            if (stok.sval.equals("BEGIN")) {
                                nextTokenNotEnd(stok, filename);
                                if (mode <= 0 && stok.sval.equalsIgnoreCase("INTERACTION")) {
                                    mode = 1;
                                    nextTokenNotEnd(stok, filename);
                                    name = stok.sval;
                                    System.out.println("Now named " + name);
                                    nextTokenEnd(stok, filename);
                                } else if (mode > 0 && stok.sval.equalsIgnoreCase("FRAMES")) {
                                    mode = 2;
                                    System.out.println("Reading frames....");
                                    nextTokenEnd(stok, filename);
                                } else if (mode == 2 && stok.sval.equalsIgnoreCase("FRAME")) {
                                    nextTokenNotEnd(stok, filename);
                                    String frameName = stok.sval;
                                    nextTokenEnd(stok, filename);
                                    syntaxNames.put(frameName, readSentence(stok, filename));
                                } else if (mode > 0 && stok.sval.equalsIgnoreCase("VOCAB")) {
                                    mode = 3;
                                    System.out.println("Reading Vocab....");
                                    nextTokenEnd(stok, filename);
                                } else if (mode > 0 && stok.sval.equalsIgnoreCase("SYNTAX")) {
                                    nextTokenNotEnd(stok, filename);
                                    nextTokenEnd(stok, filename);
                                    System.out.println("Reading syntax....");
                                    mode = 4;
                                } else {
                                    System.err.println("Error reading file " + filename + ": " + stok + " BEGIN What?");
                                    return;
                                }
                            } else {
                                switch(mode) {
                                    case 3:
                                        try {
                                            addSyntax(readMatcher(stok, filename), null);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        break;
                                    case 4:
                                        String wordName = stok.sval.trim();
                                        wordName = wordName.substring(0, wordName.length() - 1);
                                        while (stok.nextToken() != StreamTokenizer.TT_EOF && stok.ttype != StreamTokenizer.TT_EOL) {
                                            if (!syntaxNames.containsKey(stok.sval)) {
                                                System.err.println("Error reading file " + filename + ": " + stok + ": Missing syntax key");
                                                return;
                                            }
                                            addSyntax(wordName, syntaxNames.get(stok.sval));
                                        }
                                        break;
                                    default:
                                        System.err.println("Error reading file " + filename + ": " + stok + " Bad... mode (" + mode + ")?  What the...");
                                        return;
                                }
                            }
                            break;
                        default:
                            System.err.println("Error reading file " + filename + ": " + stok + ": unrecognized token.");
                            return;
                    }
                }
            } catch (IOException e) {
                System.out.println("IOException: " + e);
            } catch (Exception e) {
                return;
            }
        }
        return;
    }

    private void nextTokenNotEnd(StreamTokenizer stok, String filename) throws Exception {
        if (stok.nextToken() == StreamTokenizer.TT_EOF || stok.ttype == StreamTokenizer.TT_EOL) {
            System.err.println("Error reading file " + filename + ": " + stok + " should not be EOL/EOF.");
            throw new Exception();
        }
    }

    private void nextTokenEnd(StreamTokenizer stok, String filename) throws Exception {
        if (stok.nextToken() != StreamTokenizer.TT_EOF && stok.ttype != StreamTokenizer.TT_EOL) {
            System.err.println("Error reading file " + filename + ": " + stok + " expected EOL/EOF.");
            throw new Exception();
        }
    }

    private String getTokenLine(StreamTokenizer stok, String filename) {
        StringBuilder sb = new StringBuilder("");
        try {
            while (stok.nextToken() != StreamTokenizer.TT_EOF && stok.ttype != StreamTokenizer.TT_EOL) {
                sb.append(stok.sval + " ");
            }
        } catch (IOException e) {
            System.err.println("Error reading file " + filename + ": " + stok);
            return null;
        }
        return sb.toString();
    }

    private List<String> getSlices(String inString) {
        List<String> output = new ArrayList<String>();
        String main = inString.trim();
        String newString = "";
        try {
            while (main.length() > 0) {
                char firstChar = main.charAt(0);
                System.out.println("Slicing " + main);
                int max;
                boolean act = false;
                switch(firstChar) {
                    case '{':
                        newString = getUntilCharacter(main, '{', '}');
                        act = true;
                        break;
                    case '(':
                        newString = getUntilCharacter(main, '(', ')');
                        act = true;
                        break;
                    case '[':
                        newString = getUntilCharacter(main, '[', ']');
                        act = true;
                        break;
                    case '<':
                        newString = getUntilCharacter(main, '<', '>');
                        act = true;
                        break;
                    case '|':
                        main = main.substring(1);
                        break;
                    default:
                        String[] stack = main.split("\\s+", 2);
                        output.add(stack[0].trim());
                        if (stack.length > 1) {
                            main = stack[1];
                        } else {
                            main = "";
                        }
                }
                if (act) {
                    if (newString.equals("")) {
                        main = "";
                    } else {
                        output.add(newString.trim());
                        max = newString.length() + 1;
                        if (max >= main.length()) max = main.length();
                        main = main.substring(max);
                    }
                }
                main = main.trim();
                System.out.println("\t" + output + ":\t" + main);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(output);
        return output;
    }

    private String getUntilCharacter(String inString, char startChar, char endChar) {
        int end = inString.indexOf(endChar);
        int begin = 1;
        int at = inString.indexOf(endChar);
        if (at < 0) return "";
        while (inString.substring(begin, end).indexOf(startChar) > 0) {
            begin = inString.indexOf(startChar, begin + 1);
            at = inString.indexOf(endChar, end + 1);
            if (at < 0) return "";
        }
        return inString.substring(0, at);
    }

    public boolean fileWrite(String filename) {
        FileWriter fw;
        try {
            fw = new FileWriter(filename, false);
        } catch (IOException e) {
            System.err.println("Error opening file " + filename + ": " + e);
            return false;
        }
        StringBuilder output = new StringBuilder("");
        int counter = 0;
        Map<String, String> syntaxMap = new HashMap<String, String>();
        output.append("BEGIN INTERACTION " + name + System.getProperty("line.separator"));
        output.append("BEGIN FRAMES" + System.getProperty("line.separator"));
        Set<String> optSet = wordSyntax.keySet();
        for (String opt : optSet) {
            Set<DiscourseNode> dnSet = wordSyntax.get(opt).getOptions();
            StringBuilder mapString = new StringBuilder("");
            for (DiscourseNode sentence : dnSet) {
                StringBuilder sb = new StringBuilder("");
                sb.append(opt + (++counter));
                mapString.append(" " + sb);
                output.append("BEGIN FRAME " + sb.toString() + System.getProperty("line.separator"));
                output.append(writeNextNode(sentence) + System.getProperty("line.separator"));
            }
            syntaxMap.put(opt, mapString.toString());
        }
        output.append("BEGIN VOCAB" + System.getProperty("line.separator"));
        optSet = wordSenses.keySet();
        for (String opt : optSet) {
            String subOutput = writeMatcher(wordSenses.get(opt));
            output.append(subOutput + System.getProperty("line.separator"));
        }
        output.append("BEGIN SYNTAX MAP" + System.getProperty("line.separator"));
        Set<String> mapSet = syntaxMap.keySet();
        for (String word : mapSet) {
            output.append(word + ":" + syntaxMap.get(word) + System.getProperty("line.separator"));
        }
        try {
            fw.write(output.toString());
            fw.close();
        } catch (IOException e) {
            System.err.println("Error writing file " + filename + ": " + e);
            return false;
        }
        return true;
    }

    public String writeNextNode(DiscourseNode dn) {
        StringBuilder output = new StringBuilder("");
        if (dn instanceof DiscourseNounPhrase) {
            output.append("<" + dn.getRole() + ";NP;" + dn.getSelRes() + "> ");
        } else if (dn instanceof DiscoursePronoun) {
            output.append("<" + dn.getRole() + ";PRONOUN;" + dn.getSelRes() + "> ");
        } else if (dn instanceof DiscourseNoun) {
            output.append("<" + dn.getRole() + ";NOUN;");
            DiscourseNode theDefault = dn.getDefault();
            if (theDefault == null) output.append("NULL"); else output.append(theDefault.getWords());
            output.append(";" + dn.getSelRes() + "> ");
        } else if (dn instanceof DiscourseVerb) {
            output.append("<" + dn.getRole() + ";VERB;" + ((DiscourseVerb) dn).getAction() + ";" + dn.getSelRes() + "> ");
        } else if (dn instanceof DiscoursePrep) {
            output.append("<" + (dn.getRole().equals("") ? "preposition" : dn.getRole()) + ";PREP;" + ((DiscoursePrep) dn).getRelationship() + ";" + dn.getSelRes() + "> ");
        } else if (dn instanceof DiscourseAdjective) {
            output.append("<" + dn.getRole() + ";ADJ;" + ((DiscourseAdjective) dn).getDescriptors().toString() + ";" + dn.getSelRes() + "> ");
        } else if (dn instanceof DiscourseSequence) {
            output.append("{");
            for (DiscourseNode subnode : ((DiscourseSequence) dn).getSequence()) {
                output.append(writeNextNode(subnode));
            }
            output.append("} ");
        } else if (dn instanceof DiscourseOption) {
            output.append("[");
            for (DiscourseNode subnode : ((DiscourseOption) dn).getOptions()) {
                output.append(writeNextNode(subnode) + " | ");
            }
            output.delete(output.length() - 2, output.length());
            output.append(" ]");
        } else {
            output.append("<" + dn.getRole() + ";" + dn.getPOS() + ";" + dn.getSelRes() + ">");
        }
        if (dn.isOptional()) {
            output.insert(0, "(");
            output.append(") ");
        }
        return output.toString();
    }

    public String writeMatcher(DiscourseMatcher dm) {
        StringBuilder output = new StringBuilder("");
        Set<DiscourseNode> options = dm.getOptions();
        for (DiscourseNode dn : options) {
            output.append(dn.getWords() + " " + writeNextNode(dn) + System.getProperty("line.separator"));
        }
        return output.toString();
    }

    public DiscourseMatcher readMatcher(StreamTokenizer stok, String filename) {
        String words = stok.sval;
        String theLine = getTokenLine(stok, filename);
        if (theLine.length() > 2) {
            theLine = theLine.trim().substring(1, theLine.trim().length() - 1);
        }
        List<String> parts = new ArrayList<String>();
        int subLoc = theLine.indexOf("<");
        String theSubstring = "";
        if (subLoc > 0) {
            theSubstring = getUntilCharacter(theLine.substring(subLoc), '<', '>');
            if (!theSubstring.equals("")) {
            }
        }
        String[] partArray = theLine.split(";");
        System.out.println("Line: " + theLine + " split into " + partArray.length + " parts:" + partArray);
        for (int i = 0; i < 6; i++) {
            if (partArray.length > i) {
                if (partArray[i].trim().startsWith("%SAVE%")) {
                    parts.add(theSubstring);
                } else {
                    parts.add(partArray[i]);
                }
            } else {
                parts.add("");
            }
        }
        DiscourseNode dn;
        String POS = parts.get(1);
        String role = parts.get(0);
        if (POS.equalsIgnoreCase("NP")) {
            dn = new DiscourseNounPhrase(role, false, thisDiscourse, thisDiscourse.DEBUG > 5);
        } else if (POS.equalsIgnoreCase("PRONOUN")) {
            dn = new DiscoursePronoun(role, words, false, null, "", "", parts.get(2), thisDiscourse);
        } else if (POS.equalsIgnoreCase("NOUN")) {
            dn = new DiscourseNoun(role, words, false, (parts.get(2).equalsIgnoreCase("NULL") ? new DiscourseNoun(role, parts.get(2), false, null, "", "", parts.get(3), thisDiscourse) : null), words, "", parts.get(3), thisDiscourse);
        } else if (POS.equalsIgnoreCase("VERB")) {
            dn = new DiscourseVerb(role, words, false, null, parts.get(2), "", parts.get(3), thisDiscourse);
        } else if (POS.equalsIgnoreCase("PREP")) {
            dn = new DiscoursePrep(role, words, false, null, parts.get(2), parts.get(3), thisDiscourse);
        } else if (POS.equalsIgnoreCase("ADJ")) {
            dn = new DiscourseAdjective(role, words, false, null, parts.get(2), thisDiscourse, thisDiscourse.DEBUG > 5);
        } else {
            dn = new DiscourseNode(role, words, false, null, POS, parts.get(2), thisDiscourse);
        }
        return new DiscourseMatcher(dn, thisDiscourse);
    }

    public DiscourseNode readNextNode(String s, int isOptional, String filename) {
        System.out.println("Reading Node with string:" + s);
        if (s.equals("") || s.startsWith("}") || s.startsWith("|") || s.startsWith(")") || s.startsWith(">") || s.startsWith("]")) return null;
        if (s.equals("NULL")) return null;
        char firstChar = s.charAt(0);
        switch(firstChar) {
            case '(':
                List<String> slices = getSlices(s.substring(1));
                if (slices.isEmpty()) {
                    return null;
                }
                return readNextNode(slices.get(0), 1, filename);
            case '[':
                Set<DiscourseNode> options = new HashSet<DiscourseNode>();
                int lastStart = 1;
                int openCount = 0;
                for (int currentChar = 1; currentChar < s.length(); currentChar++) {
                    if (s.charAt(currentChar) == '[') {
                        openCount++;
                        continue;
                    }
                    if (s.charAt(currentChar) == ']') {
                        openCount--;
                        continue;
                    }
                    if (s.charAt(currentChar) == '|' && openCount == 0) {
                        DiscourseNode dn = readNextNode("{" + s.substring(lastStart, currentChar).trim(), 0, filename);
                        if (dn != null) options.add(dn);
                        lastStart = currentChar + 2;
                    }
                }
                DiscourseNode dn2 = readNextNode('{' + s.substring(lastStart).trim(), 0, filename);
                if (dn2 != null) options.add(dn2);
                return new DiscourseOption(options, isOptional > 0, thisDiscourse);
            case '{':
                List<DiscourseNode> nodes = new ArrayList<DiscourseNode>();
                List<String> steps = getSlices(s.substring(1));
                for (String step : steps) {
                    DiscourseNode dn = readNextNode(step, 0, filename);
                    if (dn != null) nodes.add(dn);
                }
                return DiscourseSequence.makeDiscourseSequence(nodes, isOptional > 0, thisDiscourse);
            case '<':
                String theLine = "";
                if (s.length() > 2) {
                    theLine = s.trim().substring(1).trim();
                }
                List<String> parts = new ArrayList<String>();
                int subLoc = theLine.indexOf("<");
                String theSubstring = "";
                if (subLoc > 0) {
                    theSubstring = getUntilCharacter(theLine.substring(subLoc), '<', '>');
                }
                String[] partArray = theLine.split(";");
                System.out.println("Line: " + theLine + " split into " + partArray.length + "  parts:" + partArray);
                for (int i = 0; i < 6; i++) {
                    if (partArray.length > i) {
                        if (partArray[i].trim().startsWith("%SAVE%")) {
                            parts.add(theSubstring);
                        } else {
                            parts.add(partArray[i]);
                        }
                    } else {
                        parts.add("");
                    }
                }
                DiscourseNode dn;
                String POS = parts.get(1);
                if (POS.equalsIgnoreCase("NP")) {
                    return new DiscourseNounPhrase(parts.get(0), isOptional > 0, thisDiscourse, thisDiscourse.DEBUG > 5);
                } else if (POS.equalsIgnoreCase("PRONOUN")) {
                    return new DiscoursePronoun(parts.get(0), "", isOptional > 0, null, "", "", parts.get(2), thisDiscourse);
                } else if (POS.equalsIgnoreCase("NOUN")) {
                    return new DiscourseNoun(parts.get(0), "", isOptional > 0, (parts.get(2).equalsIgnoreCase("NULL") ? null : new DiscourseNoun(parts.get(0), parts.get(2), isOptional > 0, null, "", "", parts.get(3), thisDiscourse)), "", "", parts.get(3), thisDiscourse);
                } else if (POS.equalsIgnoreCase("VERB")) {
                    return new DiscourseVerb(parts.get(0), "", isOptional > 0, null, parts.get(2), "", parts.get(3), thisDiscourse);
                } else if (POS.equalsIgnoreCase("PREP")) {
                    return new DiscoursePrep(parts.get(0), "", isOptional > 0, null, parts.get(2), parts.get(3), thisDiscourse);
                } else if (POS.equalsIgnoreCase("ADJ")) {
                    return new DiscourseAdjective(parts.get(0), "", isOptional > 0, null, parts.get(2), thisDiscourse);
                } else {
                    return new DiscourseNode(parts.get(0), "", isOptional > 0, null, parts.get(1), parts.get(2), thisDiscourse);
                }
            default:
                System.err.println("Error reading file: got string: " + s);
                return null;
        }
    }

    public DiscourseSentence readSentence(StreamTokenizer stok, String filename) {
        try {
            String theLine = getTokenLine(stok, filename).trim();
            boolean isOptional = false;
            if (theLine.charAt(0) == '(') {
                isOptional = true;
            }
            List<String> slices;
            theLine = theLine.trim().substring(1, theLine.length() - 2).trim();
            System.out.println(theLine);
            slices = getSlices(theLine);
            List<DiscourseNode> sentence = new ArrayList<DiscourseNode>();
            for (String s : slices) {
                DiscourseNode dn = readNextNode(s, 0, filename);
                if (dn != null) sentence.add(dn);
            }
            return new DiscourseSentence(sentence, isOptional, thisDiscourse);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
