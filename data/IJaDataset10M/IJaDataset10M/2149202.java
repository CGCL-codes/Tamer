package org.equanda.tapestry.messages;

/**
 * Grouping contexts for equanda base pages.
 *
 * @author <a href="mailto:andrei@paragon-software.ro">Andrei Chiritescu</a>
 */
public class EquandaMessagesGroup implements MessagesGroup {

    public final String[] pages = { "equanda-table", "equanda-fixed", "equanda-skin", "equanda-exceptions", "equanda-application-exceptions", "ReportList" };

    private static EquandaMessagesGroup instance = new EquandaMessagesGroup();

    public static EquandaMessagesGroup getInstance() {
        return instance;
    }

    private EquandaMessagesGroup() {
    }

    public String[] getPages() {
        return pages;
    }
}
