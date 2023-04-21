package ch.ethz.dcg.spamato.filter.simple_filter;

import java.util.Vector;
import ch.ethz.dcg.plugin.*;
import ch.ethz.dcg.spamato.base.common.filter.*;
import ch.ethz.dcg.spamato.factory.common.Mail;

/**
 * A "bad words" spam filter that brands messages as spam if they contain some
 * bad words.
 * 
 * @author keno
 */
public class SimpleFilter extends AbstractSpamFilter implements Disposable {

    static final String BAD_WORDS_KEY = "badwords";

    Vector<String> badWords;

    Configuration config;

    public SimpleFilter(PluginContext context, Configuration config) {
        super(context);
        this.config = config;
        initBadWords();
    }

    public FilterResult checkMail(Mail mail, boolean isFirstCheck) {
        String body = mail.getBody().toLowerCase();
        String id = getID();
        String name = getName();
        String badWordFound = null;
        synchronized (badWords) {
            for (String badWord : badWords) {
                badWord = " " + badWord + " ";
                if (body.contains(badWord)) {
                    badWordFound = badWord;
                    break;
                }
            }
        }
        double result = badWordFound != null ? 1 : 0;
        FilterResult filterResult = new FilterResult(id, name, result);
        filterResult.setTest(true);
        if (badWordFound != null) {
            filterResult.setComment("Bad word found: " + badWordFound + ".");
        } else {
            filterResult.setComment("Everything is fine!");
        }
        return filterResult;
    }

    public Vector<String> getBadWords() {
        return badWords;
    }

    public void addBadWord(String badWord) {
        if (!"".equals(badWord) && !badWords.contains(badWord)) badWords.add(badWord);
    }

    public void removeBadWord(String badWord) {
        badWords.remove(badWord);
    }

    protected void initBadWords() {
        String[] words = config.get(SimpleFilter.BAD_WORDS_KEY, "").split(",");
        badWords = new Vector<String>();
        for (String word : words) {
            if (!"".equals(word)) badWords.add(word);
        }
    }

    protected void saveBadWords() {
        StringBuilder words = new StringBuilder();
        for (String badWord : badWords) {
            words.append(badWord);
            words.append(",");
        }
        if (words.length() > 0) words.deleteCharAt(words.length() - 1);
        config.set(SimpleFilter.BAD_WORDS_KEY, words.toString());
        config.save();
    }

    public void dispose() {
        config.save();
    }
}
