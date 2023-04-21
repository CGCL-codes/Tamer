package au.id.jericho.lib.html;

final class EndTagTypeMasonNamedBlock extends EndTagTypeGenericImplementation {

    protected static final EndTagTypeMasonNamedBlock INSTANCE = new EndTagTypeMasonNamedBlock();

    private EndTagTypeMasonNamedBlock() {
        super("/mason named block", "</%", ">", true, false);
    }

    public StartTagType getCorrespondingStartTagType() {
        return MasonTagTypes.MASON_NAMED_BLOCK;
    }
}
