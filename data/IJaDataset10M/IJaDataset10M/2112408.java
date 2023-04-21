package com.netflexitysolutions.commons.amazonws.sdb;

import java.util.Date;
import java.util.List;
import java.util.Set;
import junit.framework.Assert;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.netflexitysolutions.amazonws.sample.domain.Account;
import com.netflexitysolutions.amazonws.sample.domain.Party;
import com.netflexitysolutions.amazonws.sample.domain.Role;
import com.netflexitysolutions.amazonws.sample.domain.User;
import com.netflexitysolutions.amazonws.sdb.orm.SimpleDBSession;
import com.netflexitysolutions.amazonws.sdb.orm.filter.Predicate;
import com.netflexitysolutions.amazonws.sdb.orm.filter.Predicates;
import com.netflexitysolutions.amazonws.sdb.orm.filter.TypeFilter;
import com.netflexitysolutions.amazonws.sdb.orm.internal.util.RecordUtil;
import com.netflexitysolutions.amazonws.sdb.orm.metadata.AttributeMetadata;
import com.netflexitysolutions.amazonws.sdb.orm.metadata.IdMetadata;
import com.netflexitysolutions.amazonws.sdb.orm.metadata.ItemMetadata;
import com.netflexitysolutions.amazonws.sdb.orm.metadata.ManyToManyMetadata;
import com.netflexitysolutions.amazonws.sdb.orm.metadata.ManyToOneMetadata;
import com.netflexitysolutions.amazonws.sdb.orm.metadata.OneToManyMetadata;
import com.netflexitysolutions.amazonws.sdb.orm.metadata.SimpleDBMapping;
import com.netflexitysolutions.commons.amazonws.sdb.dao.RoleDaoImpl;

/**
 * @author netflexity
 *
 */
public class SimpleDBSessionTest {

    private static ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

    @Test
    public void testMetadataParsing() {
        SimpleDBSession simpledbSession = (SimpleDBSession) context.getBean("simpledbSessionFactory");
        SimpleDBMapping mapping = simpledbSession.getFactory().getMapping();
        List<ItemMetadata> itemMetadata = mapping.getItemMetadata();
        for (ItemMetadata im : itemMetadata) {
            System.out.println("Class name = " + im.getClassName());
            System.out.println("Domain name = " + im.getDomainName());
            System.out.println("Logical name = " + im.getLogicalName() + "************Cachable = " + im.isCachable());
            IdMetadata idm = im.getId();
            System.out.println("Id=" + idm.getName());
            List<AttributeMetadata> am = im.getAttributeMetadata();
            for (AttributeMetadata attributeMetadata : am) {
                System.out.println("Property=" + attributeMetadata.getName());
                System.out.println("Class name=" + attributeMetadata.getClassName());
                System.out.println("Column name=" + attributeMetadata.getColumnName());
                System.out.println("Meta:" + attributeMetadata.getMeta());
            }
            List<ManyToOneMetadata> manyToOne = im.getManyToOne();
            for (ManyToOneMetadata manyToOneMetadata : manyToOne) {
                System.out.println("Many-to-one: for column:" + manyToOneMetadata.getColumnName() + ", name:" + manyToOneMetadata.getName());
            }
            List<ManyToManyMetadata> manyToMany = im.getManyToMany();
            for (ManyToManyMetadata manyToManyMetadata : manyToMany) {
                System.out.println("Many-to-many: for column:" + manyToManyMetadata.getColumnName() + ", name:" + manyToManyMetadata.getName() + ", lazy=" + manyToManyMetadata.isLazy() + ", collectionClazz=" + manyToManyMetadata.getCollectionClazz());
            }
            List<OneToManyMetadata> oneToMany = im.getOneToMany();
            for (OneToManyMetadata oneToManyMetadata : oneToMany) {
                System.out.println("One-to-many: for column:" + oneToManyMetadata.getColumnName() + ", name:" + oneToManyMetadata.getName() + ", lazy=" + oneToManyMetadata.isLazy() + ", collectionClazz=" + oneToManyMetadata.getCollectionClazz());
            }
        }
    }

    @Test
    public void testCheckOneToMany() {
        SimpleDBSession simpledbSession = (SimpleDBSession) context.getBean("simpledbSessionFactory");
        Party party = simpledbSession.find(Party.class, "15ecbd82-e397-40e9-a076-383c44cad6f8");
        Set<Account> accounts = party.getNfxAccounts();
        for (Account nfxAccount : accounts) {
            System.out.println("Found one-to-many account" + nfxAccount.getId());
        }
    }

    @Test
    public void testAddAccount() {
        SimpleDBSession simpledbSession = (SimpleDBSession) context.getBean("simpledbSessionFactory");
        Party party = new Party();
        party.setId("15ecbd82-e397-40e9-a076-383c44cad6f8");
        Account account = new Account();
        account.setChangeBy("Admin");
        account.setChangeDate(System.currentTimeMillis());
        account.setCreationDate(new Date());
        account.setEffectiveDate(System.currentTimeMillis());
        account.setExpirationDate(System.currentTimeMillis());
        account.setLicenseContent("-----BEGIN CERTIFICATE-----\n" + "MIICdzCCAeCgAwIBAgIGASHmHbwjMA0GCSqGSIb3DQEBBQUAMFcxCzAJBgNVBAYT\n" + "AlVTMRcwFQYDVQQKEw5OZXRmbGV4aXR5LmNvbTEMMAoGA1UECxMDTkZYMSEwHwYD\n" + "VQQDExhOZXRmbGV4aXR5IFNvbHV0aW9ucyBJbmMwHhcNMDkwNjE1MjI1MDA2WhcN\n" + "MDkwNjE1MjI1MTQ2WjBtMQswCQYDVQQGEwJVUzEXMBUGA1UEChMOTmV0ZmxleGl0\n" + "eS5jb20xFjAUBgNVBAsTDU5GWC1DdXN0b21lcnMxLTArBgNVBAMTJDE1ZWNiZDgy\n" + "LWUzOTctNDBlOS1hMDc2LTM4M2M0NGNhZDZmODCBnzANBgkqhkiG9w0BAQEFAAOB\n" + "jQAwgYkCgYEA4s0myci3tmB2EKnet6r8TcRYFKTbSv/lgoHsun2xE8aZVFnm5lae\n" + "RoYZ4UXLHAWWaT4O160xJEwQ+jieGUZtuWt+6L6BY3dO21QeC5uM5N2OKXJFEW+6\n" + "F4kYiG4OnxzqtWKd3JOM52vQziUz9Eu3UC76hohvEBbixAOGCZT9/QsCAwEAAaM4\n" + "MDYwDAYDVR0TAQH/BAIwADAOBgNVHQ8BAf8EBAMCBaAwFgYDVR0lAQH/BAwwCgYI\n" + "KwYBBQUHAwEwDQYJKoZIhvcNAQEFBQADgYEAAQEho2TeCxq5rnxmc2zbyJUqkKNr\n" + "Ir5HhvBpIqxEHmmifvxwT8mCVdqgXDad/NHQR31Agkxqqdsol0tIenaN/tvsMkNT\n" + "4FQcR2H8IG5/g45kcqu+BKqrU/ilNGwzZh2hYA+e7efjRDSkE60NX8bsEtfrnzGw\n" + "vVnTt0knNmGCzNY=\n" + "-----END CERTIFICATE-----");
        account.setUuid("MDc2LTM4M2M0NGNhZDZmODCBnzAN");
        account.setNfxParty(party);
        account.setStatus("A");
        simpledbSession.save(account);
        Assert.assertNotNull(account.getId());
        System.out.println("Account id=" + account.getId());
        Account foundAccount = simpledbSession.find(Account.class, account.getId());
        System.out.println(foundAccount.getLicenseContent());
        ItemMetadata metadata = simpledbSession.getMetadata(Account.class);
        Assert.assertEquals(RecordUtil.toStringWithoutIDs(metadata, account), RecordUtil.toStringWithoutIDs(metadata, foundAccount));
        Party loadedParty = foundAccount.getNfxParty();
        Assert.assertNotNull(loadedParty);
        System.out.println("Loaded party id=" + loadedParty.getId());
        ManyToOneMetadata m2o = metadata.getManyToOneMetadataByColumn("party_id");
        if (!m2o.isLazy()) {
            Assert.assertNotNull(loadedParty.getCompanyName());
            ItemMetadata partyMetadata = simpledbSession.getMetadata(Party.class);
            System.out.println(RecordUtil.toString(partyMetadata, loadedParty));
        }
    }

    @Test
    public void testAddParty() {
        SimpleDBSession simpledbSession = (SimpleDBSession) context.getBean("simpledbSessionFactory");
        Party party = new Party();
        party.setAddressLine1("2368 Dorchester Street");
        party.setBillingContactName("Max Fedorov");
        party.setBillingContactEmail("mfedorov@netflexity.com");
        party.setCity("Buckingham");
        party.setCompanyName("Netflexity 01");
        party.setPostalCode("18925");
        party.setCountryCode("US");
        simpledbSession.save(party);
        Assert.assertNotNull(party.getId());
        System.out.println("Inserted party with id=" + party.getId());
        Party foundParty = simpledbSession.find(Party.class, party.getId());
        Assert.assertEquals(party.getAddressLine1(), foundParty.getAddressLine1());
        Assert.assertEquals(party.getBillingContactName(), foundParty.getBillingContactName());
        Assert.assertEquals(party.getBillingContactEmail(), foundParty.getBillingContactEmail());
        Assert.assertEquals(party.getCity(), foundParty.getCity());
        Assert.assertEquals(party.getCompanyName(), foundParty.getCompanyName());
        Assert.assertEquals(party.getPostalCode(), foundParty.getPostalCode());
        Assert.assertEquals(party.getCountryCode(), foundParty.getCountryCode());
    }

    @Test
    public void testUpdateUserWithRoles() {
        SimpleDBSession simpledbSession = (SimpleDBSession) context.getBean("simpledbSessionFactory");
        Role role = new Role();
        role.setName("Administrator 3");
        Role role2 = new Role();
        role2.setName("Developer 3");
        Role role3 = new Role();
        role.setName("Data Steward");
        simpledbSession.save(role3);
        Account account = new Account();
        account.setId("3ca5b4b8-5d0b-4ed0-8908-bd6f046a3581");
        User user = simpledbSession.find(User.class, "e78cb0c6-0de9-4df5-9f07-1391f112ab5c");
        user.getNfxRoles().add(role3);
        simpledbSession.save(user);
        System.out.println("User id=" + user.getId());
    }

    @Test
    public void testAddUserWithRoles() {
        SimpleDBSession simpledbSession = (SimpleDBSession) context.getBean("simpledbSessionFactory");
        Role role = new Role();
        role.setName("Administrator 3");
        simpledbSession.save(role);
        Role role2 = new Role();
        role2.setName("Developer 3");
        Account account = new Account();
        account.setId("3ca5b4b8-5d0b-4ed0-8908-bd6f046a3581");
        User user = new User();
        user.setUsername("maxim");
        user.setPassword("password1");
        user.setStatus("A");
        user.setChangeBy("Jhopa");
        user.setChangeDate(System.currentTimeMillis());
        user.setNfxAccount(account);
        user.getNfxRoles().add(role);
        user.getNfxRoles().add(role2);
        simpledbSession.save(user);
        System.out.println("User id=" + user.getId());
    }

    @Test
    public void testCheckManyToMany() {
        SimpleDBSession simpledbSession = (SimpleDBSession) context.getBean("simpledbSessionFactory");
        User user = simpledbSession.find(User.class, "e78cb0c6-0de9-4df5-9f07-1391f112ab5c");
        Set<Role> roles = user.getNfxRoles();
        for (Role role : roles) {
            System.out.println("Found many-to-many role:" + role.getName());
        }
    }

    @Test
    public void testCheckManyToOne() {
        SimpleDBSession simpledbSession = (SimpleDBSession) context.getBean("simpledbSessionFactory");
        User user = simpledbSession.find(User.class, "e78cb0c6-0de9-4df5-9f07-1391f112ab5c");
        Account account = user.getNfxAccount();
        System.out.println("Lazy account with id:" + account.getId());
        Assert.assertNotNull(account.getLicenseContent());
        System.out.println(account.getLicenseContent());
    }

    @Test
    public void testCheckPopulate() {
        SimpleDBSession simpledbSession = (SimpleDBSession) context.getBean("simpledbSessionFactory");
        User user = simpledbSession.find(User.class, "e78cb0c6-0de9-4df5-9f07-1391f112ab5c");
        Set<Role> roles = user.getNfxRoles();
        for (Role role : roles) {
            System.out.println("(Before populate) - Found many-to-many role:" + role.getName());
        }
        Assert.assertEquals(false, roles.isEmpty());
        Account account = user.getNfxAccount();
        System.out.println("Lazy account with id:" + account.getId());
        Assert.assertNull(account.getLicenseContent());
        simpledbSession.populate(user);
        roles = user.getNfxRoles();
        for (Role role : roles) {
            System.out.println("(After populate) - Found many-to-many role:" + role.getName());
        }
        Assert.assertEquals(false, roles.isEmpty());
        account = user.getNfxAccount();
        System.out.println("Lazy account with id:" + account.getId());
        Assert.assertNotNull(account.getLicenseContent());
    }

    @Test
    public void testFindAllParty() {
        SimpleDBSession simpledbSession = (SimpleDBSession) context.getBean("simpledbSessionFactory");
        for (int i = 0; i < 3; i++) {
            Set<Party> parties = simpledbSession.findAll(Party.class, "companyName");
            for (Party nfxParty : parties) {
                System.out.println(RecordUtil.toString(simpledbSession.getMetadata(Party.class), nfxParty));
                Set<Account> accounts = nfxParty.getNfxAccounts();
                for (Account account : accounts) {
                    System.out.println("-->Account:" + account.getLicenseContent());
                    User user = new User();
                    user.setUsername("jopa" + i);
                    account.getNfxUsers().add(user);
                }
                nfxParty.getNfxAccounts().addAll(accounts);
            }
        }
        Set<Party> parties = simpledbSession.findAll(Party.class, "companyName");
        for (Party nfxParty : parties) {
            System.out.println(RecordUtil.toString(simpledbSession.getMetadata(Party.class), nfxParty));
            Set<Account> accounts = nfxParty.getNfxAccounts();
            for (Account account : accounts) {
                System.out.println("-->Account:" + account.getLicenseContent());
                Set<User> users = account.getNfxUsers();
                for (User user : users) {
                    System.out.println("-->User:" + user.getUsername());
                }
            }
        }
    }

    @Test
    public void testFindAllRoles() {
        SimpleDBSession simpledbSession = (SimpleDBSession) context.getBean("simpledbSessionFactory");
        for (int i = 0; i < 3; i++) {
            Set<Role> roles = simpledbSession.findAll(Role.class, new String[] { "name" });
            for (Role role : roles) {
                System.out.println(RecordUtil.toString(simpledbSession.getMetadata(Role.class), role));
            }
            try {
                Thread.sleep(11000l);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testFindAllRolesDao() {
        RoleDaoImpl roleDao = (RoleDaoImpl) context.getBean("roleDao");
        for (int i = 0; i < 3; i++) {
            Set<Role> roles = roleDao.findAll(new String[] { "name" });
            for (Role role : roles) {
                System.out.println(role.getName());
            }
        }
    }

    @Test
    public void testInClause() {
        SimpleDBSession simpledbSession = (SimpleDBSession) context.getBean("simpledbSessionFactory");
        String sqlToExpect = "itemName() in(\"Administrator 3\",\"Developer 3\")";
        Predicate<Role> predicate = Predicates.getInPredicate(Role.class, simpledbSession.getFactory().getMapping(), "name", new String[] { "Administrator 3", "Developer 3" });
        System.out.println(predicate.toSelect());
        Assert.assertEquals(sqlToExpect.toLowerCase(), predicate.toSelect().toLowerCase());
        List<Role> roles = simpledbSession.select(predicate, new String[] {});
        for (Role role : roles) {
            System.out.println(RecordUtil.toString(simpledbSession.getMetadata(Role.class), role));
        }
    }

    @Test
    public void testCachedEntity() {
        RoleDaoImpl roleDao = (RoleDaoImpl) context.getBean("roleDao");
        Role admin3 = roleDao.find("Administrator 3");
        Assert.assertNotNull(admin3);
        try {
            Thread.sleep(3000L);
        } catch (InterruptedException e) {
            System.out.println("Woke up to do a find");
        }
        Set<Role> roles = roleDao.findAll();
        for (Role role : roles) {
            System.out.println(role.getName());
        }
    }

    @Test
    public void testFindCountAllParty() {
        SimpleDBSession simpledbSession = (SimpleDBSession) context.getBean("simpledbSessionFactory");
        int parties = simpledbSession.count(new TypeFilter<Party>(Party.class, simpledbSession.getFactory().getMapping()));
        System.out.println(parties + " parties found!");
    }
}
