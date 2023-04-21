package edu.umn.cs.nlp.mt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.je.DatabaseException;
import edu.umn.cs.nlp.mt.chiang2007.BilingualTranslationRuleBinding;
import edu.umn.cs.nlp.mt.chiang2007.Parser;
import edu.umn.cs.nlp.mt.chiang2007.SGlueRule;
import edu.umn.cs.nlp.mt.chiang2007.UnknownWordRule;
import edu.umn.cs.nlp.parser.PRuleDatabase;
import edu.umn.cs.nlp.util.CommandLineParser;
import edu.umn.cs.nlp.util.CommandLineParser.Option;

/**
 *
 * @author Lane Schwartz
 * @version $LastChangedDate: 2007-12-12 12:07:54 -0500 (Wed, 12 Dec 2007) $
 */
public class TranslationRuleDatabase implements TranslationRules {

    private final PRuleDatabase<TranslationRule> rules;

    private static final Collection<TranslationRule> empty = Collections.emptyList();

    public TranslationRuleDatabase(String databaseFile, String environmentDirectory, String encoding, boolean allowDuplicates, TupleBinding binding, int kbestListSize) {
        rules = new PRuleDatabase<TranslationRule>(databaseFile, environmentDirectory, encoding, allowDuplicates, binding, kbestListSize);
    }

    public TranslationRuleDatabase(PRuleDatabase<TranslationRule> ruleDatabase) {
        rules = ruleDatabase;
    }

    public long size() throws DatabaseException {
        return rules.size();
    }

    public boolean isOpen() {
        return rules.isOpen();
    }

    public NBestList<TranslationRule> getNBest(String key) {
        NBestList<TranslationRule> best_rules = rules.getNBest(key);
        if (key.equals("S X")) {
            best_rules.add(new SGlueRule());
        }
        return best_rules;
    }

    public Collection<TranslationRule> getAll(String key) {
        Collection<TranslationRule> all_rules = new ArrayList<TranslationRule>(rules.getAll(key));
        if (key.equals("S X")) {
            all_rules.add(new SGlueRule());
        }
        return all_rules;
    }

    public Collection<TranslationRule> getGlueRule(String key) {
        if (key.equals("S X")) return Collections.<TranslationRule>singletonList(new SGlueRule()); else return empty;
    }

    public Collection<TranslationRule> getUnknownWordRule() {
        return Collections.<TranslationRule>singletonList(new UnknownWordRule());
    }

    public void close() {
        rules.close();
    }

    public boolean incompleteMatchExists(String key) {
        return rules.incompleteMatchExists(key);
    }

    public static void main(String[] args) {
        CommandLineParser commandLine = new CommandLineParser();
        Option<String> grammar_directory = commandLine.addStringOption('r', "rules", "DIRECTORY_NAME", "grammar rules database directory");
        Option<String> encoding = commandLine.addStringOption('e', "encoding", "ENCODING", "UTF-8", "database encoding");
        commandLine.parse(args);
        TranslationRuleDatabase db = new TranslationRuleDatabase(Parser.DB_NAME, commandLine.getValue(grammar_directory), commandLine.getValue(encoding), true, new BilingualTranslationRuleBinding(), 100);
        for (TranslationRule rule : db.getAll("das")) {
            System.out.println(rule);
        }
    }
}
