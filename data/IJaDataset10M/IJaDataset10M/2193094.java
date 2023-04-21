package au.id.jericho.lib.html;

final class StartTagTypeServerCommon extends StartTagTypeGenericImplementation {

    static final StartTagTypeServerCommon INSTANCE = new StartTagTypeServerCommon();

    private StartTagTypeServerCommon() {
        super("common server tag", "<%", "%>", null, true);
    }
}
