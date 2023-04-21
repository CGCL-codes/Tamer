package uk.ac.ebi.intact.sanity.commons.rules;

import uk.ac.ebi.intact.sanity.commons.report.SanityReport;
import java.util.ArrayList;
import java.util.Collection;

/**
 * TODO comment this
*
* @author Bruno Aranda (baranda@ebi.ac.uk)
* @version $Id$
*/
public class RuleRunnerReport {

    private static final ThreadLocal<RuleRunnerReport> instance = new ThreadLocal<RuleRunnerReport>() {

        @Override
        protected RuleRunnerReport initialValue() {
            return new RuleRunnerReport();
        }
    };

    public static RuleRunnerReport getInstance() {
        return instance.get();
    }

    private Collection<GeneralMessage> messages;

    public RuleRunnerReport() {
        this.messages = new ArrayList<GeneralMessage>();
    }

    public void addMessage(GeneralMessage message) {
        messages.add(message);
    }

    public void addMessages(Collection<GeneralMessage> message) {
        messages.addAll(message);
    }

    public Collection<GeneralMessage> getMessages() {
        return messages;
    }

    public void clear() {
        messages.clear();
    }

    public SanityReport toSanityReport() {
        return toSanityReport(false);
    }

    public SanityReport toSanityReport(boolean clearRuleRunnerReport) {
        SanityReport report = MessageUtils.toSanityReport(messages);
        if (clearRuleRunnerReport) {
            clear();
        }
        return report;
    }
}
