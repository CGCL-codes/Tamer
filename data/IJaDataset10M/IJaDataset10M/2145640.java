package net.sf.ninjakore.packetdata;

import net.sf.ninjakore.utils.perl.PerlUtils;

public class CharacterChoiceBuilder extends AbstractPacketBuilder {

    public CharacterChoiceBuilder(ClientVersion version, int id) {
        super(version, id);
    }

    @Override
    protected void initializeTemplates() {
        templates.put(ClientVersion.ORIGINAL, "v C");
    }

    @Override
    protected String[] keys() {
        return PerlUtils.qw("id slot");
    }

    @Override
    public Class<? extends PacketData> packetClass() {
        return CharacterChoice.class;
    }
}
