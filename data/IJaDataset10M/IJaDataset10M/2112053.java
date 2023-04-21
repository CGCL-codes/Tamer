package schemacrawler.tools.main;

public final class BundledDriverHelpOptions extends HelpOptions {

    private static final long serialVersionUID = -2497570007150087268L;

    public BundledDriverHelpOptions(final String title, final String resourceConnections) {
        super(title);
        setResourceConnections(resourceConnections);
        setCommandHelpType(CommandHelpType.without_query);
        setHideConfig(true);
    }
}
