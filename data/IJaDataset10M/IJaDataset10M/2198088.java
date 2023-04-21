package com.wozgonon.eventstore;

import com.wozgonon.math.IHasInvariant;
import com.wozgonon.usecase.Interaction;

final class ActorUseCase implements java.io.Serializable, IHasInvariant {

    private static final long serialVersionUID = -8407780408364247766L;

    private final Interaction interaction;

    private final String name;

    private final String user;

    public ActorUseCase(Interaction interaction, String name, String user) {
        this.interaction = interaction;
        this.name = name.intern();
        this.user = user.intern();
        this.assertClassInvariant();
    }

    public int hashCode() {
        final int code = interaction.hashCode() + name.hashCode() + user.hashCode();
        return code;
    }

    public boolean equals(Object object) {
        if (object == null) return false;
        final ActorUseCase in = (ActorUseCase) object;
        boolean a = this.interaction == in.interaction;
        boolean b = this.name == in.name;
        boolean c = this.user == in.user;
        return this.interaction == in.interaction && this.name == in.name && this.user == in.user;
    }

    public String getUser() {
        return this.user;
    }

    public Interaction getInteraction() {
        return this.interaction;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public void assertClassInvariant() {
        assert this.equals(this);
        assert this.hashCode() == this.hashCode();
    }

    void testCreate() {
        for (Interaction interaction : Interaction.values()) {
            final ActorUseCase aa = new ActorUseCase(interaction, "a", "b");
            final ActorUseCase bb = new ActorUseCase(interaction, "a", "c");
            final ActorUseCase cc = new ActorUseCase(interaction, "b", "c");
            final ActorUseCase dd = new ActorUseCase(interaction, "c", "b");
            assert aa.equals(aa);
            assert bb.equals(bb);
            assert cc.equals(cc);
            assert dd.equals(dd);
            assert !aa.equals(bb);
            assert !bb.equals(aa);
            assert !bb.equals(cc);
            assert !cc.equals(dd);
        }
    }
}
