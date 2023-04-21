package net.sourceforge.ant4hg.consumers;

import java.util.Vector;
import net.sourceforge.ant4hg.Logger;

/**
 * Processes the hg output stream.
 * 
 * @author Benjamin de Dardel <ant4hg[at]free.fr>
 * @since V0.2
 */
class LogConsumer extends Consumer {

    private String m_user = "";

    private String m_date = "";

    private String m_summary = "";

    private Vector<String> m_files = new Vector<String>();

    private boolean m_isDescription = false;

    private String m_revision = "";

    /**
     * Default constructor.
     */
    public LogConsumer() {
    }

    /**
     * Consumes a line of the stream.
     * 
     * @param line
     *            the line to consume
     */
    @Override
    protected void consume(final String line) {
        if (line.matches("^changeset:.*$")) {
            m_revision = line.substring("changeset:".length(), line.length());
            m_revision = m_revision.substring(m_revision.indexOf(":") - 1, m_revision.indexOf(":"));
        } else if (line.matches("^user:.*$")) {
            m_user = line.substring("user:".length(), line.length()).trim();
        } else if (line.matches("^date:.*$")) {
            m_date = line.substring("date:".length(), line.length()).trim();
        } else if (line.matches("^summary:.*$")) {
            m_summary = line.substring("summary:".length(), line.length()).trim();
        } else if (line.matches("^files:.*$")) {
            m_files.add(line.substring("files:".length(), line.length()).trim());
        } else if (line.matches("^description:.*$")) {
            m_isDescription = true;
            m_summary = line.substring("description:".length(), line.length()).trim();
        } else if (m_isDescription) {
            m_summary += m_summary + " " + line;
        }
    }

    @Override
    protected void postConsume() {
        Logger.info("[" + m_summary.trim() + "] [" + m_user + "] [#" + m_revision + " | " + m_date + "]");
        for (int i = 0; i < m_files.size(); i++) {
            Logger.info("-> " + m_files.get(i));
        }
    }
}
