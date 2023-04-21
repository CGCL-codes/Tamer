package jbced.citadellesspring.modele.roles.actions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import jbced.citadellesspring.core.SpringBeans;
import jbced.citadellesspring.core.exceptions.CitadellesException;
import jbced.citadellesspring.modele.exceptions.CitadellesModeleExceptions;
import jbced.citadellesspring.modele.roles.EnumEtatsRole;
import jbced.citadellesspring.modele.roles.EnumRoles;
import jbced.citadellesspring.modele.roles.Role;
import jbced.citadellesspring.modele.roles.RoleFactory;
import jbced.citadellesspring.modele.roles.actions.exceptions.LaSorciereNePeutSEnsorceler;
import org.junit.Before;
import org.junit.Test;
import org.spring.contextholder.ContextHolder;
import org.springframework.context.ApplicationContext;

public class ActionEnsorcelerTest extends ActionJeuTestAbstract {

    private Role roleEnsorcele;

    private Role sorciere;

    @Before
    public void setUp() throws Exception {
        ApplicationContext context = ContextHolder.getApplicationContext();
        RoleFactory rf = (RoleFactory) context.getBean(SpringBeans.roleFactory.name());
        this.sorciere = rf.create(EnumRoles.Sorciere);
        this.sorciere.abonneAuxActions(this);
        this.roleEnsorcele = rf.create(EnumRoles.Navigateur);
    }

    private ActionJeu actionEnsorceler() throws CitadellesException {
        return getActionJeuSpeciale(sorciere, EnumActionsJeu.Ensorceler);
    }

    @Test
    public void ensorcelerUnRoleFonctionne() throws Exception {
        actionEnsorceler().agir();
        assertEquals(true, roleEnsorcele.getEtats().contains(EnumEtatsRole.EstEnsorcele));
    }

    @Test(expected = LaSorciereNePeutSEnsorceler.class)
    public void laSorciereNePeutSEnsorceler() throws Exception {
        roleEnsorcele = sorciere;
        actionEnsorceler().agir();
    }

    @Test
    public void laSorciereNePeutPasSEnsorcelerDoncLActionNAPasEteSupprimeDeLaListe() throws CitadellesException {
        roleEnsorcele = sorciere;
        try {
            actionEnsorceler().agir();
        } catch (LaSorciereNePeutSEnsorceler e) {
            assertNotNull(actionEnsorceler());
        }
    }

    @Test
    public void laSorciereNePeutPasEnsorcelerPlusDUneFois() throws Exception {
        actionEnsorceler().agir();
        assertNull(actionEnsorceler());
    }

    @Override
    public void process(InfoActionJeu info) throws CitadellesModeleExceptions {
        if (info.getAction().equals(EnumActionsJeu.Ensorceler)) {
            super.process(info);
            if (roleEnsorcele.equals(sorciere)) {
                throw new LaSorciereNePeutSEnsorceler();
            }
            roleEnsorcele.subirModificationDEtat(EnumEtatsRole.EstEnsorcele);
            Role role = info.getActionJeu().getRole();
            role.removeActionJeu(info.getActionJeu());
        }
    }
}
