package jbced.citadellesspring.modele.roles.actions;

import static org.junit.Assert.assertEquals;
import jbced.citadellesspring.core.SpringBeans;
import jbced.citadellesspring.modele.exceptions.CitadellesModeleExceptions;
import jbced.citadellesspring.modele.joueurs.Cite;
import jbced.citadellesspring.modele.joueurs.Joueur;
import jbced.citadellesspring.modele.quartiers.QuartierEnum;
import jbced.citadellesspring.modele.roles.Role;
import jbced.citadellesspring.modele.roles.RolesFacade;
import jbced.citadellesspring.modele.roles.RolesDisponibles;
import org.junit.Before;
import org.junit.Test;
import org.spring.contextholder.ContextHolder;
import org.springframework.context.ApplicationContext;

public class ActionToucherRentesTest extends ActionJeuTestAbstract {

    private Joueur joueur;

    @Before
    public void setUp() throws Exception {
        ApplicationContext context = ContextHolder.getApplicationContext();
        joueur = (Joueur) context.getBean(SpringBeans.joueurHumain.name());
        Cite cite = joueur.cite();
        cite.construire(QuartierEnum.comptoir);
        cite.construire(QuartierEnum.eglise);
        cite.construire(QuartierEnum.taverne);
        cite.construire(QuartierEnum.caserne);
        cite.construire(QuartierEnum.chateau);
        cite.construire(QuartierEnum.cathedrale);
        cite.construire(QuartierEnum.dracoport);
        cite.construire(QuartierEnum.echoppe);
        cite.construire(QuartierEnum.marche);
        cite.construire(QuartierEnum.tourDeGuet);
        cite.construire(QuartierEnum.prison);
        cite.construire(QuartierEnum.ecoleDeMagie);
    }

    private Role createRole(int noRole) {
        ApplicationContext context = ContextHolder.getApplicationContext();
        RolesFacade rf = (RolesFacade) context.getBean(SpringBeans.rolesFacade.name());
        RolesDisponibles rolesDispo = rf.createRolesDisponibles();
        Role resultat = null;
        int i = 0;
        for (Role role : rolesDispo) {
            if (i == noRole) {
                resultat = role;
            }
            i++;
        }
        resultat.setJoueur(joueur);
        return resultat;
    }

    private ActionJeu toucherRentes(final Role role) {
        ActionJeu action = getActionJeuSpeciale(role, EnumActionsJeu.ToucherRentesPourQuartiersDeMemeCouleur);
        action.add(this);
        return action;
    }

    @Test
    public void unJoueurQuiAChoisiLeRoiToucheDesRentesPourChaqueQuartierDeCouleurJaune() throws Exception {
        int orAvant = joueur.or();
        Role roi = createRole(3);
        toucherRentes(roi).agir();
        assertEquals(orAvant + 2, joueur.or());
    }

    @Test
    public void unJoueurQuiAChoisiLEvequeToucheDesRentesPourChaqueQuartierDeCouleurBleu() throws Exception {
        int orAvant = joueur.or();
        Role eveque = createRole(4);
        toucherRentes(eveque).agir();
        assertEquals(orAvant + 3, joueur.or());
    }

    @Test
    public void unJoueurQuiAChoisiLeMarchandToucheDesRentesPourChaqueQuartierDeCouleurVerte() throws Exception {
        int orAvant = joueur.or();
        Role marchand = createRole(5);
        toucherRentes(marchand).agir();
        assertEquals(orAvant + 5, joueur.or());
    }

    @Test
    public void unJoueurQuiAChoisiLeCondottiereToucheDesRentesPourChaqueQuartierDeCouleurRouge() throws Exception {
        int orAvant = joueur.or();
        Role condottiere = createRole(7);
        toucherRentes(condottiere).agir();
        assertEquals(orAvant + 4, joueur.or());
    }

    @Override
    public void process(InfoActionJeu info) throws CitadellesModeleExceptions {
        if (info.getAction().equals(EnumActionsJeu.ToucherRentesPourQuartiersDeMemeCouleur)) {
            super.process(info);
            Role r = info.getActionJeu().getRole();
            Joueur j = r.getJoueur();
            Cite c = j.cite();
            int orGagne = 0;
            for (QuartierEnum q : c) {
                if (q.getCouleur().equals(r.getCouleur()) || q.equals(QuartierEnum.ecoleDeMagie)) {
                    orGagne++;
                }
            }
            j.toucherOr(orGagne);
        }
    }
}
