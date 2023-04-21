package org.restlet.client.engine.http.header;

import java.util.List;
import org.restlet.client.data.Warning;
import org.restlet.client.engine.util.DateUtils;

/**
 * Warning header writer.
 * 
 * @author Thierry Boileau
 */
public class WarningWriter extends HeaderWriter<Warning> {

    /**
     * Writes a warning.
     * 
     * @param warnings
     *            The list of warnings to format.
     * @return The formatted warning.
     */
    public static String write(List<Warning> warnings) {
        return new WarningWriter().append(warnings).toString();
    }

    @Override
    public WarningWriter append(Warning warning) {
        String agent = warning.getAgent();
        String text = warning.getText();
        if (warning.getStatus() == null) {
            throw new IllegalArgumentException("Can't write warning. Invalid status code detected");
        }
        if ((agent == null) || (agent.length() == 0)) {
            throw new IllegalArgumentException("Can't write warning. Invalid agent detected");
        }
        if ((text == null) || (text.length() == 0)) {
            throw new IllegalArgumentException("Can't write warning. Invalid text detected");
        }
        append(Integer.toString(warning.getStatus().getCode()));
        append(" ");
        append(agent);
        append(" ");
        appendQuotedString(text);
        if (warning.getDate() != null) {
            appendQuotedString(DateUtils.format(warning.getDate()));
        }
        return this;
    }
}
