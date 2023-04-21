package fr.gouv.defense.terre.esat.formathlon.persistence.ldap;

import fr.gouv.defense.terre.esat.formathlon.entity.Utilisateur;
import fr.gouv.defense.terre.esat.formathlon.persistence.exception.LdapConnexionException;
import fr.gouv.defense.terre.esat.formathlon.persistence.exception.LdapNameNotFoundException;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.naming.Name;
import org.springframework.ldap.NameNotFoundException;
import org.springframework.ldap.core.DistinguishedName;

/**
 * Service de persistence.
 * @author maxime.guinchard
 * @version 1.0
 */
@Stateless
@LocalBean
public class LdapPersistence {

    /**
     * Acces au singleton permettant d'etablir une connexion avec le ldap.
     */
    @EJB
    private LdapConnectorSingleton ldapConnectorSingleton;

    /**
     * Name.
     * @param login login
     * @return Name
     */
    private Name buildDn(String login) {
        DistinguishedName dn = new DistinguishedName();
        String[] tabKey = ldapConnectorSingleton.getProperties().getProperty("dnLoginKey").split(",");
        String[] tabValue = ldapConnectorSingleton.getProperties().getProperty("dnLoginValue").split(",");
        String uid = ldapConnectorSingleton.getProperties().getProperty("dnUid");
        for (int i = 0; i < tabKey.length; i++) {
            dn.add(tabKey[i], tabValue[i]);
        }
        dn.add(uid, login);
        return dn;
    }

    /**
     * Recupere un utilisateur dans le ldap par rapport a son uid.
     * @param uid login.
     * @return  Utilisateur.
     */
    public Utilisateur findByPrimaryKey(String uid) {
        Utilisateur utilisateur = null;
        try {
            utilisateur = (Utilisateur) ldapConnectorSingleton.getLdapTemplate().lookup(buildDn(uid), new UtililisateurMapper(ldapConnectorSingleton.getProperties()));
        } catch (NameNotFoundException ex) {
            throw new LdapNameNotFoundException();
        } catch (Exception ex) {
            throw new LdapConnexionException();
        }
        return utilisateur;
    }
}
