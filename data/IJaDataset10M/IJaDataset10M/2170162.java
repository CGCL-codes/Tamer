package net.ponec.pekiline.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.ponec.pekiline.bo.Chapter;
import net.ponec.pekiline.bo.Item;
import net.ponec.pekiline.bo.Parameters;
import org.ujorm.UjoAction;

/**
 * Testing class
 * @author pavel
 */
public class Testing {

    public static final String NO_MORE_ITEMS = "No more words";

    /** Hexa constant blue color for Peki.   */
    public static final String BLUE = "000080";

    private Chapter chapter;

    private List<Item> wordStack;

    private List<Item> wordFailed;

    private Random random = new Random();

    private boolean randomEnabled = true;

    private boolean lastSucces = true;

    /** Current selected word */
    int selectedWord = 0;

    /** Count of true answers */
    int okCount = 0;

    /** Count of true answers */
    int faildCount = 0;

    public Testing() {
        this(new Chapter(), false, true);
    }

    /** New word Test */
    public Testing(Chapter chapter, boolean marked, boolean random) {
        this.chapter = chapter;
        this.randomEnabled = random;
        List<Item> aWordStack = Chapter.P_ITEMS.getList(chapter);
        initialize(aWordStack, marked);
    }

    public int getFaildCount() {
        return faildCount;
    }

    public int getOkCount() {
        return okCount;
    }

    public Item getSelectedItem() {
        if (selectedWord < 0) {
            return wordFailed.get(wordFailed.size() - 1);
        } else if (hasNext()) {
            return wordStack.get(selectedWord);
        } else {
            return null;
        }
    }

    /** Get a test word. */
    public String getSelectedWord(boolean toEnglish, boolean showSelected) {
        if (hasNext()) {
            try {
                Item item = getSelectedItem();
                String result = toEnglish ? Item.P_TRANSLATION.of(item) : Item.P_FOREIGN.of(item);
                if (showSelected && Item.P_MARK.getValue(item)) {
                    result = result + "  *";
                }
                return result;
            } catch (Throwable e) {
                return "";
            }
        } else {
            return "";
        }
    }

    /** Initialize words */
    private void initialize(List<Item> aWordStack, boolean marked) {
        wordStack = new ArrayList<Item>(aWordStack.size());
        wordFailed = new ArrayList<Item>();
        this.wordStack = new ArrayList<Item>();
        for (Item item : aWordStack) {
            if (marked ? Item.P_MARK.of(item) : true) {
                wordStack.add(item);
            }
        }
    }

    /** Have the object a next word? */
    public boolean hasNext() {
        return wordStack.size() > 0 || wordFailed.size() > 0;
    }

    /** Select new word */
    public Item selectNewWord() {
        lastSucces = true;
        if (!hasNext()) {
            throw new IllegalStateException(NO_MORE_ITEMS);
        }
        if (wordStack.isEmpty()) {
            initialize(wordFailed, false);
        }
        selectedWord = nextWordIndex();
        return wordStack.get(selectedWord);
    }

    /** Returns next random index of the word list  */
    private int nextWordIndex() {
        if (randomEnabled) {
            return random.nextInt(wordStack.size());
        } else {
            return 0;
        }
    }

    /** Remove selected word */
    public boolean removeSelectedWord(Boolean ok) {
        if (selectedWord < 0) {
            return hasNext();
        }
        lastSucces = Boolean.TRUE.equals(ok);
        Item item = wordStack.remove(selectedWord);
        if (ok == null) {
            wordFailed.add(item);
            selectedWord = -1;
        } else if (ok) {
            ++okCount;
        } else {
            ++faildCount;
            wordFailed.add(item);
            selectedWord = -1;
        }
        return hasNext();
    }

    public String getProgressInfo() {
        int remains = wordStack.size() + wordFailed.size();
        int testCount = okCount + faildCount;
        return "" + Math.min(testCount + 1, testCount + remains) + "/" + (testCount + remains);
    }

    /** How many word remains */
    public String getRemaind() {
        int remains = wordStack.size() + wordFailed.size();
        return String.valueOf(remains);
    }

    /** Returns Item in HTML format */
    public String getItemInHtmlFormat(ApplContext context) {
        Item item = getSelectedItem();
        String colorPron = "#" + context.getParameters().readValueString(Parameters.P_COLOR_PRONUNCIATION, UjoAction.DUMMY);
        StringBuilder result = new StringBuilder(256);
        result.append("<html>");
        result.append("<head>");
        result.append("<style type=\"text/css\">");
        result.append("body { padding: 0px 5px 0px; font-size: 20px; }");
        result.append("div  { padding-bottom: 5px; }");
        result.append(".c2  { color: ").append(colorPron).append("; padding-left: 10px;}");
        result.append(".c3  { color: " + BLUE + "; }");
        result.append(".c4  { color: gray; }");
        result.append("</style>");
        result.append("</head>");
        result.append("<body>");
        result.append("<div class=\"c1\">").append(escape(Item.P_FOREIGN.of(item))).append("</div>");
        result.append("<div class=\"c2\">").append(escape(Item.P_PRONUNCIATION.of(item))).append("</div>");
        result.append("<div class=\"c3\">").append(escape(Item.P_TRANSLATION.of(item))).append("</div>");
        result.append("<div class=\"c4\">").append(escape(Item.P_DESCR.of(item))).append("</div>");
        result.append("</body>");
        result.append("</html>");
        return result.toString();
    }

    /** Escape a text parameter */
    protected String escape(Object aText) {
        boolean fixSpace = false;
        String text = String.valueOf(aText);
        if (aText == null || text.length() == 0) {
            return "&nbsp;";
        }
        StringBuilder sb = new StringBuilder(text.length() + 8);
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            switch(c) {
                case '<':
                    sb.append("&lt;");
                    break;
                case '>':
                    sb.append("&gt;");
                    break;
                case '&':
                    sb.append("&amp;");
                    break;
                case '"':
                    sb.append("&quot;");
                    break;
                case '\'':
                    sb.append("&apos;");
                    break;
                case ' ':
                    sb.append(fixSpace ? "&nbsp;" : " ");
                    break;
                default:
                    {
                        if (c < 32) {
                            sb.append("&#");
                            sb.append(Integer.toString(c));
                            sb.append(';');
                        } else {
                            sb.append(c);
                        }
                    }
            }
        }
        return sb.toString();
    }

    /** Returns the laset Success */
    public boolean isLastSucces() {
        return lastSucces;
    }

    /** Get test chapter */
    public Chapter getChapter() {
        return chapter;
    }
}
