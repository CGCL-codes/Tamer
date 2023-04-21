package com.dj.persistence;

import com.dj.persistence.internal.EntityManagerFactoryProvider;
import com.dj.persistence.entity.Account;
import com.dj.persistence.entity.PhoneNumber;
import com.dj.persistence.entity.Address;
import com.dj.persistence.entity.AccountHolder;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.*;
import java.math.BigDecimal;

/**
 * User: Jacob
 * Date: Jul 19, 2008
 * Time: 12:34:42 PM
 */
public class Main {

    public static void main(String[] args) {
        insertAccount();
        Account account = removeAccountHolder();
        iterateAccountHolders(account);
        removeEntity();
    }

    private static void removeEntity() {
        EntityManagerFactory entityManagerFactory = EntityManagerFactoryProvider.getEntityManagerFactoryIntance();
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        Account account = entityManager.find(Account.class, new Long(1000));
        entityManager.remove(account);
        transaction.commit();
        entityManager.close();
    }

    private static void iterateAccountHolders(Account account) {
        List<AccountHolder> accountHolders = account.getAccountHolders();
        System.out.println("--------------------------------------------------------------------");
        for (Iterator<AccountHolder> iterator = accountHolders.iterator(); iterator.hasNext(); ) {
            AccountHolder aHolder = iterator.next();
            String firstName = aHolder.getFirstName();
            System.out.println("First Name : " + firstName);
        }
        System.out.println("--------------------------------------------------------------------");
    }

    private static void createNewAccountHolder(Account account, List<AccountHolder> accountHolders) {
        AccountHolder accountHolder = new AccountHolder();
        accountHolder.setFirstName("Sona");
        accountHolder.setLastName("Mohan");
        accountHolder.setId(new Long(5000));
        accountHolders.add(accountHolder);
        accountHolder.setAccount(account);
        account.setAccountHolders(accountHolders);
    }

    private static void createNewAccountHolderAndMerge(Account account) {
        System.out.println("\n TRYING TO MERGE the ENTITIES...\n");
        List<AccountHolder> accountHolders = account.getAccountHolders();
        AccountHolder accountHolder = new AccountHolder();
        accountHolder.setFirstName("Sona U");
        accountHolder.setLastName("Mohan ");
        accountHolder.setId(new Long(5000));
        accountHolders.add(accountHolder);
        account.setAccountType(Account.AccountType.CURRENT);
        accountHolder.setAccount(account);
        account.setAccountHolders(accountHolders);
        EntityManagerFactory entityManagerFactory = EntityManagerFactoryProvider.getEntityManagerFactoryIntance();
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.merge(account);
        transaction.commit();
        entityManager.close();
    }

    private static Account removeAccountHolder() {
        System.out.println("\n TRYING TO MERGE the ENTITIES..removeAccountHolder() \n");
        EntityManagerFactory entityManagerFactory = EntityManagerFactoryProvider.getEntityManagerFactoryIntance();
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        Account account = entityManager.find(Account.class, new Long(1000));
        System.out.println("inside removeAccountHolder()");
        iterateAccountHolders(account);
        List<AccountHolder> accountHolders = account.getAccountHolders();
        AccountHolder h1 = (AccountHolder) accountHolders.get(1);
        account.removeAccountHolder(h1);
        iterateAccountHolders(account);
        transaction.commit();
        entityManager.close();
        return account;
    }

    private static Account getAccount(Long id) {
        EntityManagerFactory entityManagerFactory = EntityManagerFactoryProvider.getEntityManagerFactoryIntance();
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        Account account = entityManager.find(Account.class, new Long(1000));
        transaction.commit();
        entityManager.close();
        return account;
    }

    private static void insertAccount() {
        EntityManagerFactory entityManagerFactory = EntityManagerFactoryProvider.getEntityManagerFactoryIntance();
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(getAccount());
        transaction.commit();
        entityManager.close();
    }

    private static Account getAccount() {
        final Account account = new Account();
        account.setId(new Long(1000));
        account.setAccountType(Account.AccountType.SAVINGS);
        account.setBalance(new BigDecimal("1000.00"));
        account.setCloseDate(new Date());
        account.setOpenDate(new Date());
        AccountHolder accountHolder = new AccountHolder();
        accountHolder.setFirstName("Deepak");
        accountHolder.setLastName("Jacob");
        Address permAddress = new Address();
        PhoneNumber homePhone = new PhoneNumber("91", "481", "2509329");
        permAddress.setLandlineNo(homePhone);
        permAddress.setDoorNo("Karingnamattom House");
        permAddress.setState("Kerala");
        permAddress.setCountry("India");
        accountHolder.setAddress1(permAddress);
        accountHolder.setAccount(account);
        List<AccountHolder> accountHolderList = new ArrayList<AccountHolder>(10);
        AccountHolder accountHolder2 = new AccountHolder();
        accountHolder2.setFirstName("Renjith");
        accountHolder2.setLastName("Kurian");
        accountHolder2.setAccount(account);
        accountHolderList.add(accountHolder);
        accountHolderList.add(accountHolder2);
        account.setAccountHolders(accountHolderList);
        return account;
    }
}
