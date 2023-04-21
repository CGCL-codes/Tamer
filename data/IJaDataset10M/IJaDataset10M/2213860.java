package diet.server.CbyC.Sequence;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import diet.message.MessageCBYCTypingUnhinderedRequest;
import diet.server.Conversation;
import diet.server.Participant;
import diet.server.CbyC.DocChange;
import diet.server.CbyC.DocInsert;
import diet.server.CbyC.Sequences;
import diet.server.ConversationController.CCCBYCAbstractProbeCR;
import diet.server.ConversationController.CCCBYCDefaultController;
import diet.utils.Dictionary;
import diet.utils.POSTagRegex;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

/**
 * @author Arash
 * 
 *         This class applies some filters to the sequence of parts of speech of
 *         the words coming into this turn incrementally. If the sequence
 *         matches the filters, the conversation controller is notified of
 *         opportunity to intervene. The filters (regexes) determine whether a
 *         point in that sequence matches the requirements of this experiment.
 *         Mid-constituent condition: we have to have seen a noun phrase, then a
 *         verb and then be in the middle of another (object) noun phrase: So e.g. : [NNS
 *         or NNP or NN][VBT][DT]
 * 
 *         OR
 * 
 *         [DT][NNS or NN][VBT][DT]
 * 
 *         Constituent-Boundary Condition: we have to have seen a noun phrase,
 *         then a verb and then a full noun phrase. [NNS or NNP or
 *         NN][VBT][DT][NN||NNP||NNS]
 * 
 *         OR
 * 
 *         [DT][NNS or NN][VBT][DT][NN||NNP||NNS]
 * 
 * 
 *         This will only work if deletes are disabled. So no DocRemoves coming
 *         in . . . Also, will only work with subclasses of CCCBYCAbstractProbeCR, which
 *         implement the triggerIntervention() method, as desired, in that class.
 * 
 */
public class POSTagFilterSequence extends Sequence {

    String turnSoFar;

    Vector<POSTagRegex> regexes = new Vector<POSTagRegex>();

    MaxentTagger tagger;

    String condition;

    Dictionary dict;

    Map<String, String> misspellings;

    final long waitingTimeBeforeInterventionCheckPoint = 2500;

    public String getType() {
        if (!this.condition.equals("")) return "POSTagFilterSequence(" + condition + ")"; else return "POSTagFilterSequence";
    }

    public POSTagFilterSequence(Sequences sS, CCCBYCDefaultController cC, String sender, MessageCBYCTypingUnhinderedRequest mCTUR, MaxentTagger tagger, Vector<POSTagRegex> r, Dictionary d, Map<String, String> misspellings) {
        super(sS, cC, sender, mCTUR);
        this.dict = d;
        this.misspellings = misspellings;
        this.tagger = tagger;
        this.turnSoFar = "";
        this.regexes = r;
        this.condition = "";
        this.start();
    }

    public POSTagFilterSequence(Sequences sS, CCCBYCDefaultController cC, String sender, Date timeStamp, String elementString, int elementStart, int elementFinish, MaxentTagger tagger, Vector<POSTagRegex> regexes, Dictionary d, Map<String, String> misspellings) {
        super(sS, cC, sender, timeStamp, elementString, elementStart, elementFinish);
        this.dict = d;
        this.misspellings = misspellings;
        this.tagger = tagger;
        this.regexes = regexes;
        this.turnSoFar = "";
        this.condition = "";
        this.start();
    }

    public POSTagFilterSequence(Sequences sS, CCCBYCDefaultController cC, String sender, MessageCBYCTypingUnhinderedRequest mCTUR, MaxentTagger tagger, Vector<POSTagRegex> r, Dictionary d, Map<String, String> misspellings, String condition) {
        super(sS, cC, sender, mCTUR);
        this.dict = d;
        this.tagger = tagger;
        this.turnSoFar = "";
        this.regexes = r;
        this.condition = condition;
        this.misspellings = misspellings;
        this.start();
    }

    public POSTagFilterSequence(Sequences sS, CCCBYCDefaultController cC, String sender, Date timeStamp, String elementString, int elementStart, int elementFinish, MaxentTagger tagger, Vector<POSTagRegex> regexes, Dictionary dict, Map<String, String> misspellings, String condition) {
        super(sS, cC, sender, timeStamp, elementString, elementStart, elementFinish);
        this.dict = dict;
        this.misspellings = misspellings;
        this.tagger = tagger;
        this.regexes = regexes;
        this.turnSoFar = "";
        this.condition = condition;
        this.start();
    }

    public synchronized void setInputClosedEditOfOwnTurn() {
        this.setInputClosed();
        notify();
    }

    public synchronized void setInputClosedSpeakerChange() {
        this.setInputClosed();
        notify();
    }

    long lastDocChangeTimeStamp = new Date().getTime();

    public synchronized Sequence addDocChange(DocChange dc) {
        lastDocChangeTimeStamp = new Date().getTime();
        if (!dc.sender.equalsIgnoreCase(sender)) {
            if (dc instanceof DocInsert) {
                DocInsert di = (DocInsert) dc;
                Conversation.printWSln("ERROR", "New DocChange from: " + dc.getSender() + ", " + di.str);
            }
            Conversation.printWSln("ERROR", "Existing Sequence: " + sender + ", " + this.getFinalText());
            Conversation.printWSln("ERROR", "______________________________________________________");
        }
        Vector v = new Vector();
        this.docChangesBySender.addElement(dc);
        if (dc instanceof DocInsert) {
            DocInsert di = (DocInsert) dc;
            String insert = di.getStr();
            this.turnSoFar += insert;
            notify();
        } else Conversation.printWSln("ERROR", "DocRemove in POSTagFilterSequence. Edit and deletes should be disabled.");
        Participant senderP = cC.getC().getParticipants().findParticipantWithUsername(dc.sender);
        Vector recipients = cC.getC().getParticipants().getAllOtherParticipants(cC.getC().getParticipants().findParticipantWithUsername(dc.getSender()));
        for (int i = 0; i < recipients.size(); i++) {
            Participant p = (Participant) recipients.elementAt(i);
            DocChange dcCopy = dc.returnCopy();
            dcCopy.recipient = p.getUsername();
            if (dc instanceof DocInsert) {
                DocInsert di = (DocInsert) dcCopy;
                int unique = sS.getStyleManager().getUniqueIntForRecipient(p, senderP);
                di.a = "n" + unique;
            }
            v.addElement(dcCopy);
        }
        fb.enqueue(v);
        return null;
    }

    public boolean isInputFinished() {
        if (fb.isInputCompleted()) return true; else return false;
    }

    protected String fixSpelling() {
        String split[] = this.turnSoFar.split("\\s+");
        String result = "";
        for (int i = 0; i < split.length; i++) {
            String w = split[i];
            String lowerCase = w.toLowerCase();
            Pattern p = Pattern.compile("([\\\\/\\.\\?!,;\\(\\)]*)([^\\\\/\\.\\?!,;\\(\\)]+)([\\\\/\\.\\?!,;\\(\\)]*)");
            Pattern p1 = Pattern.compile("([\\\\/\\.\\?!,;\\(\\)]*)([^\\\\/\\.\\?!,;\\(\\)]+)([\\\\/\\.\\?!,;\\(\\)]+)([^\\\\/\\.\\?!,;\\(\\)]+)([\\\\/\\.\\?!,;\\(\\)]*)");
            Matcher m = p.matcher(lowerCase);
            Matcher m1 = p1.matcher(lowerCase);
            if (m.matches()) {
                if (misspellings.containsKey(m.group(2))) {
                    String replacement = misspellings.get(m.group(2));
                    result += m.group(1) + replacement + m.group(3) + " ";
                } else {
                    result += m.group(1) + m.group(2) + m.group(3) + " ";
                }
            } else if (m1.matches()) {
                result += m1.group(1) + (misspellings.containsKey(m1.group(2)) ? misspellings.get(m1.group(2)) : m1.group(2)) + m1.group(3) + (misspellings.containsKey(m1.group(4)) ? misspellings.get(m1.group(4)) : m1.group(4)) + m1.group(5) + " ";
            } else {
                if (misspellings.containsKey(lowerCase)) {
                    String replacement = misspellings.get(lowerCase);
                    result += replacement + " ";
                } else {
                    if (!w.matches("[A-Z][a-z]*")) result += lowerCase + " "; else result += w + " ";
                }
            }
        }
        if (turnSoFar.endsWith(" ")) return result; else return result.trim();
    }

    public String getFragForIntervention() {
        String turnSpellingCorrected = this.fixSpelling();
        List<ArrayList<? extends HasWord>> sentences = MaxentTagger.tokenizeText(new StringReader(turnSpellingCorrected));
        if (sentences.isEmpty()) return null;
        ArrayList<? extends HasWord> lastSentence = sentences.get(sentences.size() - 1);
        ArrayList<TaggedWord> lastTaggedSentence = tagger.tagSentence(lastSentence);
        String frag = runFiltersAndGetFrag(lastTaggedSentence);
        return frag;
    }

    boolean interventionTriggered = false;

    public void run() {
        while (!this.isInputFinished()) {
            synchronized (this) {
                try {
                    if (this.interventionTriggered) break;
                    long beforeWait = new Date().getTime();
                    wait(this.waitingTimeBeforeInterventionCheckPoint);
                    if (this.turnSoFar == null || this.turnSoFar.equals("")) {
                        continue;
                    }
                    if (this.isInputFinished()) break;
                    long afterWait = new Date().getTime();
                    if (afterWait - beforeWait >= this.waitingTimeBeforeInterventionCheckPoint || turnSoFar.endsWith(" ")) {
                        String frag = this.getFragForIntervention();
                        if (frag != null) {
                            if (this.cC instanceof CCCBYCAbstractProbeCR) {
                                ((CCCBYCAbstractProbeCR) this.cC).triggerFragSending(sender, frag);
                                this.interventionTriggered = true;
                            } else {
                                Conversation.printWSln("ERROR", "Tried to intervene. But controller class is wrong. Should be CCCBYCAbstractProbeCR");
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected static String findFrag(String group3, String group1, ArrayList<TaggedWord> tagged) {
        if (group3 == null || group3.equals("")) return null;
        String[] split3 = group3.trim().split(" ");
        int group1Length;
        if (group1 == null || group1.equals("")) group1Length = 0; else group1Length = group1.trim().split(" ").length;
        int group3Length = split3.length;
        List<TaggedWord> fragSubList = tagged.subList(group1Length, group1Length + group3Length);
        String result = "";
        for (TaggedWord w : fragSubList) result += (w.word() + " ");
        return result.trim();
    }

    protected String runFiltersAndGetFrag(ArrayList<TaggedWord> tagged) {
        for (POSTagRegex p : this.regexes) {
            String tagSequence = this.getTagSequence(tagged, p.endInTagOrWord);
            Matcher m = p.regex.matcher(tagSequence);
            if (m.matches()) {
                String fragPOSGroup = m.group(3);
                Conversation.printWSln("POSTagFilterSeq", "Matched. Frag POSSEQ:" + fragPOSGroup);
                String group1 = m.group(1);
                List<String> groupSplit = Arrays.asList(fragPOSGroup.trim().split(" "));
                String frag = findFrag(fragPOSGroup, group1, tagged);
                Conversation.printWSln("POSTagFilterSeq", "The Frag (findFrag returns):" + frag);
                List<String> fragSplit = Arrays.asList(frag.toLowerCase().trim().split(" "));
                int PRPIndex = groupSplit.indexOf("PRP$");
                if (PRPIndex >= 0) {
                    String pronoun = fragSplit.get(PRPIndex);
                    if (pronoun.equalsIgnoreCase("my")) {
                        fragSplit.set(PRPIndex, "your");
                        frag = this.getListAsString(fragSplit);
                    } else if (pronoun.equalsIgnoreCase("your")) {
                        fragSplit.set(PRPIndex, "my");
                        frag = this.getListAsString(fragSplit);
                    }
                }
                if (fragSplit.size() == 1 || groupSplit.get(groupSplit.size() - 1).equals("NNP")) return frag.toLowerCase(); else if (groupSplit.get(groupSplit.size() - 1).equals("NNS")) return frag.toLowerCase(); else if (groupSplit.get(groupSplit.size() - 1).equals("DT")) {
                    if (frag.equalsIgnoreCase("this") || frag.equalsIgnoreCase("that")) return frag.toLowerCase(); else {
                        Conversation.printWSln("POSTagFilterSeq", "Aborting intervention. Frag is determiner but not this/that");
                        return null;
                    }
                } else {
                    if (this.dict.hasWord(fragSplit.get(fragSplit.size() - 1))) return frag; else {
                        Conversation.printWSln("POSTagFilterSeq", "Aborting intervention. Frag end not in dicitonary.");
                        return null;
                    }
                }
            }
        }
        return null;
    }

    protected boolean matches() {
        String turnSpellingCorrected = this.fixSpelling();
        Conversation.printWSln("POSTagFilterSeq", "Tagging Text (after spell check):\n" + turnSpellingCorrected);
        List<ArrayList<? extends HasWord>> sentences = MaxentTagger.tokenizeText(new StringReader(turnSpellingCorrected));
        if (sentences.isEmpty()) return false;
        ArrayList<? extends HasWord> lastSentence = sentences.get(sentences.size() - 1);
        ArrayList<TaggedWord> lastTaggedSentence = tagger.tagSentence(lastSentence);
        for (POSTagRegex p : this.regexes) {
            String tagSequence = this.getTagSequence(lastTaggedSentence, p.endInTagOrWord);
            Matcher m = p.regex.matcher(tagSequence);
            if (m.matches()) {
                Conversation.printWSln("POSTagFilterSeq", "MATCHED!");
                return true;
            }
        }
        return false;
    }

    protected String getListAsString(List<String> l) {
        String result = "";
        for (String s : l) {
            result += s + " ";
        }
        return result.trim();
    }

    protected String getTagSequence(ArrayList<TaggedWord> lastSent, String endInTag) {
        String result = "";
        for (int i = 0; i < lastSent.size(); i++) {
            TaggedWord word = lastSent.get(i);
            if (i == lastSent.size() - 1 && !endInTag.equalsIgnoreCase("EndsInTag")) result += word.word(); else result += (word.tag() + " ");
        }
        return result.trim();
    }

    protected String getTagSequence(ArrayList<TaggedWord> lastSent) {
        String result = "";
        for (TaggedWord word : lastSent) {
            result += word.tag() + " ";
        }
        return result.trim();
    }

    public String getTurnSofar() {
        return this.turnSoFar;
    }

    public void setCondition(String string) {
        this.condition = string;
    }
}
