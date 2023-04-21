package au.id.jericho.lib.html;

final class StartTagTypeNormal extends StartTagTypeGenericImplementation {

    static final StartTagTypeNormal INSTANCE = new StartTagTypeNormal();

    private StartTagTypeNormal() {
        super("normal", START_DELIMITER_PREFIX, ">", EndTagType.NORMAL, false, true, true);
    }

    public boolean atEndOfAttributes(final Source source, final int pos, final boolean isClosingSlashIgnored) {
        final ParseText parseText = source.getParseText();
        return parseText.charAt(pos) == '>' || (!isClosingSlashIgnored && parseText.containsAt("/>", pos));
    }
}
