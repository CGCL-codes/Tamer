package citadelles.metier.roles.impl;

import citadelles.metier.roles.EnumRoles;
import citadelles.metier.roles.actions.EnumActionsJeu;

class Sorciere extends RoleAbstract {

    public Sorciere(final EnumRoles role) {
        super(role);
    }

    @Override
    public void initialiserRole() {
        super.initialiserRole();
        this.actionsSpeciales.add(ajf.create(EnumActionsJeu.Ensorceler, this));
    }
}
