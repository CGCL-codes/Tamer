package au.id.jericho.lib.html;

final class StartTagTypePHPScript extends StartTagTypeGenericImplementation {

    protected static final StartTagTypePHPScript INSTANCE = new StartTagTypePHPScript();

    private StartTagTypePHPScript() {
        super("PHP script", "<script", ">", EndTagType.NORMAL, true, true, false);
    }

    protected Tag constructTagAt(final Source source, final int pos) {
        final StartTag startTag = (StartTag) super.constructTagAt(source, pos);
        if (startTag == null) return null;
        if (!"php".equalsIgnoreCase(startTag.getAttributes().getValue("language"))) return null;
        return startTag;
    }
}
