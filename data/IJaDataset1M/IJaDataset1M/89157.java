package org.nakedobjects.object.security;

import org.nakedobjects.object.Naked;
import org.nakedobjects.object.NakedObject;
import org.nakedobjects.object.control.Hint;
import org.nakedobjects.object.reflect.AbstractOneToOnePeer;
import org.nakedobjects.object.reflect.MemberIdentifier;
import org.nakedobjects.object.reflect.OneToOnePeer;

public class OneToOneAuthorisation extends AbstractOneToOnePeer {

    private final AuthorisationManager authorisationManager;

    public OneToOneAuthorisation(OneToOnePeer local, AuthorisationManager authorisationManager) {
        super(local);
        this.authorisationManager = authorisationManager;
    }

    public Hint getHint(MemberIdentifier identifier, NakedObject inObject, Naked associate) {
        Hint hint = super.getHint(identifier, inObject, associate);
        return AuthorisationHint.hint(identifier, hint, authorisationManager);
    }

    public boolean hasHint() {
        return true;
    }
}
