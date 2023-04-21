package de.spotnik.mail.core.internal.persistence.hibernate;

import de.spotnik.mail.AbstractSpotnikTest;
import de.spotnik.mail.core.model.UserAccount;
import de.spotnik.persistence.DatabaseException;
import java.util.List;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

/**
 * UserAccountDAOTest.
 * 
 * @author Jens Rehp�hler
 * @since 29.04.2006
 */
public class UserAccountDAOTest extends AbstractSpotnikTest {

    /** account. */
    private UserAccount account;

    /** list of accounts. */
    private List<UserAccount> accounts;

    /** dao. */
    private UserAccountDAO dao;

    /**
     * Creates a new instance of <code>UserAccountDAOTest</code>.
     */
    public UserAccountDAOTest() {
        this.dao = (UserAccountDAO) AbstractSpotnikTest.CONTEXT.getBean("persistence.userAccountDAO");
    }

    @Override
    public void setUp() {
        this.account = new UserAccount();
    }

    /**
     * 
     */
    public void testFindById() {
        getTransactionTemplate().execute(new TransactionCallbackWithoutResult() {

            @Override
            @SuppressWarnings("unused")
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    UserAccountDAOTest.this.account = UserAccountDAOTest.this.dao.findById(new Long(1));
                } catch (DatabaseException ex) {
                    fail(ex.getMessage());
                }
            }
        });
        assertNotNull("account with id 1 not found", this.account);
    }

    /**
     * 
     */
    public void testFindByUsername() {
        getTransactionTemplate().execute(new TransactionCallbackWithoutResult() {

            @Override
            @SuppressWarnings("unused")
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    UserAccountDAOTest.this.account = UserAccountDAOTest.this.dao.findByUsername("jre");
                } catch (DatabaseException ex) {
                    fail(ex.getMessage());
                }
            }
        });
        assertNotNull("can't find with username 'jre'", this.account);
    }

    /**
     * 
     */
    public void testFindAll() {
        getTransactionTemplate().execute(new TransactionCallbackWithoutResult() {

            @Override
            @SuppressWarnings("unused")
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                UserAccountDAOTest.this.accounts = UserAccountDAOTest.this.dao.findAll();
            }
        });
        assertNotNull("list is empty", this.accounts);
        assertEquals("list is empty", false, this.accounts.isEmpty());
    }
}
