package com.evasion.plugin.account;

import com.evasion.entity.Account;
import com.evasion.entity.Civilite;
import com.evasion.entity.Individual;
import com.evasion.entity.User;
import com.evasion.exception.PersistenceViolationException;
import com.evasion.plugin.junit.persistence.AbstractPersistentTest;
import java.util.Date;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author sebastien.glon
 */
public class AccountManagerTest extends AbstractPersistentTest {

    User user;

    Individual person;

    @Before
    public void setUp() throws Exception {
        tx.begin();
        HashSet<String> auth = new HashSet<String>();
        auth.add("My auth");
        user = new User();
        user.setUsername("my UserName");
        user.setPassword("My Password");
        user.setEnabled(true);
        user.setAuthorities(auth);
        person = new Individual();
        person.setNom("Mon nom");
        person.setPrenom("Mon premon");
        person.setTitre(Civilite.maitre);
        person.setEmail("mon.email@test.fr");
        person.setAnniversaire(new Date());
    }

    @Test
    public void createAccount() throws NamingException {
        Account account = new Account(user, person);
        AccountManager service = new AccountManager(em);
        try {
            service.createAccount(account);
        } catch (PersistenceViolationException ex) {
            Logger.getLogger(AccountManagerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
        assertNotNull(account.getLastConnect());
        assertNotNull(account.getUser().getCreationDate());
        assertNotNull(account.getPerson().getId());
    }
}
