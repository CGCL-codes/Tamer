package com.alexmcchesney.poster.account;

import java.io.*;
import java.util.*;
import javax.xml.bind.*;
import com.alexmcchesney.poster.*;
import com.alexmcchesney.poster.account.jaxb.*;

/**
 * Class representing the configuration xml file containing
 * information about the user's accounts.
 * @author amcchesney
 *
 */
public class AccountFile implements IAccountConfig {

    /** Name of the account file */
    public static final String FILENAME = "accounts.xml";

    /** Accounts in the system */
    private ArrayList<AccountRecord> m_accounts = new ArrayList<AccountRecord>();

    /** Path to the config file */
    private File m_configFile = null;

    /** Singleton jaxb context */
    private static JAXBContext m_context;

    /**
	 * Constructor
	 * @param path	Path to the config file
	 */
    public AccountFile(File path) throws LoadConfigException {
        m_configFile = path;
        load();
    }

    /**
	 * Saves the configuration file
	 * @throws SaveConfigException thrown if an error should occur
	 */
    public synchronized void save() throws SaveConfigException {
        if (!m_configFile.exists()) {
            try {
                m_configFile.createNewFile();
            } catch (IOException e) {
                throw new SaveConfigException(m_configFile, e);
            }
        }
        ObjectFactory factory = new ObjectFactory();
        FileOutputStream outStream = null;
        try {
            Accounts root = factory.createAccounts();
            Iterator<AccountRecord> it = m_accounts.iterator();
            List<Account> accountList = root.getAccount();
            while (it.hasNext()) {
                try {
                    accountList.add(it.next().toJAXB(factory));
                } catch (UnsupportedEncodingException encEx) {
                    throw new SaveConfigException(m_configFile, encEx);
                }
            }
            if (m_context == null) {
                m_context = JAXBContext.newInstance("com.alexmcchesney.poster.account.jaxb", this.getClass().getClassLoader());
            }
            Marshaller marshaller = m_context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            outStream = new FileOutputStream(m_configFile);
            marshaller.marshal(root, outStream);
        } catch (JAXBException jaxbEx) {
            throw new SaveConfigException(m_configFile, jaxbEx);
        } catch (FileNotFoundException fileEx) {
            throw new SaveConfigException(m_configFile, fileEx);
        } finally {
            if (outStream != null) {
                try {
                    outStream.close();
                } catch (IOException closeEx) {
                    closeEx.printStackTrace();
                }
            }
        }
    }

    /**
	 * Loads the configuration from the file
	 * @throws LoadConfigException Thrown if an error should occur
	 */
    public void load() throws LoadConfigException {
        if (!m_configFile.exists() || m_configFile.length() == 0) {
            return;
        }
        Accounts root = null;
        try {
            if (m_context == null) {
                m_context = JAXBContext.newInstance("com.alexmcchesney.poster.account.jaxb", this.getClass().getClassLoader());
            }
            Unmarshaller u = m_context.createUnmarshaller();
            root = (Accounts) u.unmarshal(new FileInputStream(m_configFile.getAbsolutePath()));
        } catch (JAXBException jaxbEx) {
            throw new LoadConfigException(m_configFile, jaxbEx);
        } catch (FileNotFoundException fileEx) {
            throw new LoadConfigException(m_configFile, fileEx);
        }
        ArrayList<AccountRecord> accounts = new ArrayList<AccountRecord>();
        List<Account> accountList = (List<Account>) root.getAccount();
        Iterator<Account> accountIt = accountList.iterator();
        while (accountIt.hasNext()) {
            try {
                accounts.add(new AccountRecord(accountIt.next()));
            } catch (UnsupportedEncodingException encEx) {
                throw new LoadConfigException(m_configFile, encEx);
            }
        }
        m_accounts = accounts;
    }

    /**
	 * Gets the accounts in the config
	 * @return	Array of account records
	 */
    public AccountRecord[] getAccounts() {
        return m_accounts.toArray(new AccountRecord[m_accounts.size()]);
    }

    /**
	 * Sets the accounts in the config
	 * @return	Array of account records
	 */
    public void setAccounts(AccountRecord[] accounts) {
        m_accounts.clear();
        m_accounts.addAll(Arrays.asList(accounts));
    }

    /**
	 * Adds a new account to the config
	 * @param newAccount
	 */
    public void addAccount(AccountRecord newAccount) {
        m_accounts.add(newAccount);
    }

    /**
	 * Gets all accounts as a collection.
	 * @return
	 */
    public AccountCollection getAccountCollection() {
        return getAccountCollection(null);
    }

    /**
	 * Gets a collection of accounts, which optionally
	 * implement the given class.
	 *  
	 * @param filterClass
	 * @return
	 */
    public AccountCollection getAccountCollection(Class<?> filterClass) {
        AccountRecord[] accountArray = getAccounts();
        int iTotalAccounts = accountArray.length;
        AccountCollection collection = new AccountCollection();
        for (int i = 0; i < iTotalAccounts; i++) {
            try {
                IAccount account = accountArray[i].getAccount();
                if (filterClass == null || filterClass.isInstance(account)) {
                    collection.add(account);
                }
            } catch (Throwable t) {
            }
        }
        return collection;
    }
}
