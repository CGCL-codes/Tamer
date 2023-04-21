package unit.test.teachin.domain.teachin.dao;

import org.hibernate.Transaction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import java.io.Serializable;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import teachin.domain.persistence.impl.GenericHibernateDaoImpl;
import teachin.domain.teachin.factories.teachin.*;
import teachin.domain.teachin.model.dao.teachin.*;
import teachin.domain.teachin.model.obj.teachin.*;

/** Hibernate tester
* author: auto-generated
*/
public class DAOTest {

    /** Transaction state */
    private static Transaction tx;

    /**
	 * Junit init
	 */
    @BeforeClass
    public static void initSpring() {
        new ClassPathXmlApplicationContext("applicationContext.xml");
    }

    /**
	 * Start Transaction
	 */
    @Before
    public void sessionOpen() {
        tx = GenericHibernateDaoImpl.getSession().beginTransaction();
    }

    /**
	 * End Transaction
	 */
    @After
    public void sessionClose() {
        if (tx.isActive()) {
            tx.commit();
        }
        GenericHibernateDaoImpl.closeCurrentSession();
    }

    /**
	 * Clears the database
	 */
    @Test
    public void testPreCleanDBFirstPass() {
        try {
            HibernateTeachinDaoFactory.getUserRolesDao().getQuery("delete from UserRoles").executeUpdate();
        } catch (Exception e) {
        }
        try {
            HibernateTeachinDaoFactory.getUserContactsDao().getQuery("delete from UserContacts").executeUpdate();
        } catch (Exception e) {
        }
        try {
            HibernateTeachinDaoFactory.getUserAddressesDao().getQuery("delete from UserAddresses").executeUpdate();
        } catch (Exception e) {
        }
        try {
            HibernateTeachinDaoFactory.getRolePermissionsDao().getQuery("delete from RolePermissions").executeUpdate();
        } catch (Exception e) {
        }
        try {
            HibernateTeachinDaoFactory.getZipcodesDao().getQuery("delete from Zipcodes").executeUpdate();
        } catch (Exception e) {
        }
        try {
            HibernateTeachinDaoFactory.getUsersDao().getQuery("delete from Users").executeUpdate();
        } catch (Exception e) {
        }
        try {
            HibernateTeachinDaoFactory.getTopicDao().getQuery("delete from Topic").executeUpdate();
        } catch (Exception e) {
        }
        try {
            HibernateTeachinDaoFactory.getSubjectDao().getQuery("delete from Subject").executeUpdate();
        } catch (Exception e) {
        }
        try {
            HibernateTeachinDaoFactory.getRolesDao().getQuery("delete from Roles").executeUpdate();
        } catch (Exception e) {
        }
        try {
            HibernateTeachinDaoFactory.getQuestionTypeDao().getQuery("delete from QuestionType").executeUpdate();
        } catch (Exception e) {
        }
        try {
            HibernateTeachinDaoFactory.getPermissionsDao().getQuery("delete from Permissions").executeUpdate();
        } catch (Exception e) {
        }
        try {
            HibernateTeachinDaoFactory.getGradingTypeDao().getQuery("delete from GradingType").executeUpdate();
        } catch (Exception e) {
        }
        try {
            HibernateTeachinDaoFactory.getExerciseDefinitionDao().getQuery("delete from ExerciseDefinition").executeUpdate();
        } catch (Exception e) {
        }
        try {
            HibernateTeachinDaoFactory.getExerciseContentDao().getQuery("delete from ExerciseContent").executeUpdate();
        } catch (Exception e) {
        }
        try {
            HibernateTeachinDaoFactory.getContactsDao().getQuery("delete from Contacts").executeUpdate();
        } catch (Exception e) {
        }
        try {
            HibernateTeachinDaoFactory.getAddressesDao().getQuery("delete from Addresses").executeUpdate();
        } catch (Exception e) {
        }
    }

    /**
	 * Clears the database
	 */
    @Test
    public void testPreCleanDBFinalPass() {
        HibernateTeachinDaoFactory.getUserRolesDao().getQuery("delete from UserRoles").executeUpdate();
        HibernateTeachinDaoFactory.getUserContactsDao().getQuery("delete from UserContacts").executeUpdate();
        HibernateTeachinDaoFactory.getUserAddressesDao().getQuery("delete from UserAddresses").executeUpdate();
        HibernateTeachinDaoFactory.getRolePermissionsDao().getQuery("delete from RolePermissions").executeUpdate();
        HibernateTeachinDaoFactory.getZipcodesDao().getQuery("delete from Zipcodes").executeUpdate();
        HibernateTeachinDaoFactory.getUsersDao().getQuery("delete from Users").executeUpdate();
        HibernateTeachinDaoFactory.getTopicDao().getQuery("delete from Topic").executeUpdate();
        HibernateTeachinDaoFactory.getSubjectDao().getQuery("delete from Subject").executeUpdate();
        HibernateTeachinDaoFactory.getRolesDao().getQuery("delete from Roles").executeUpdate();
        HibernateTeachinDaoFactory.getQuestionTypeDao().getQuery("delete from QuestionType").executeUpdate();
        HibernateTeachinDaoFactory.getPermissionsDao().getQuery("delete from Permissions").executeUpdate();
        HibernateTeachinDaoFactory.getGradingTypeDao().getQuery("delete from GradingType").executeUpdate();
        HibernateTeachinDaoFactory.getExerciseDefinitionDao().getQuery("delete from ExerciseDefinition").executeUpdate();
        HibernateTeachinDaoFactory.getExerciseContentDao().getQuery("delete from ExerciseContent").executeUpdate();
        HibernateTeachinDaoFactory.getContactsDao().getQuery("delete from Contacts").executeUpdate();
        HibernateTeachinDaoFactory.getAddressesDao().getQuery("delete from Addresses").executeUpdate();
    }

    /**
	 * Hibernate test case for table: teachin.addresses
	 */
    @Test
    public void testAddresses() {
        try {
            AddressesDao addressesDao = HibernateTeachinDaoFactory.getAddressesDao();
            Addresses addresses = TeachinDataPoolFactory.getAddresses();
            addressesDao.save(addresses);
            GenericHibernateDaoImpl.getSession().flush();
            GenericHibernateDaoImpl.getSession().evict(addresses);
            Serializable addressesId = addresses.getId();
            Addresses copy = addresses.clone();
            addresses = addressesDao.get(addressesId);
            Assert.assertNotNull(copy);
            Assert.assertEquals(copy.getAddressComment(), addresses.getAddressComment());
            Assert.assertEquals(copy.getId(), addresses.getId());
            Assert.assertEquals(copy.getStreet(), addresses.getStreet());
            Assert.assertEquals(copy.getStreetnumber(), addresses.getStreetnumber());
            Assert.assertFalse(addresses.toString().equals(""));
            Assert.assertEquals(copy, copy.clone());
            Assert.assertEquals(copy, copy);
            Assert.assertNotSame(copy, 0L);
        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
            Assert.fail(e.getMessage());
        }
    }

    /**
	 * Hibernate test case for table: teachin.contacts
	 */
    @Test
    public void testContacts() {
        try {
            ContactsDao contactsDao = HibernateTeachinDaoFactory.getContactsDao();
            Contacts contacts = TeachinDataPoolFactory.getContacts();
            contactsDao.save(contacts);
            GenericHibernateDaoImpl.getSession().flush();
            GenericHibernateDaoImpl.getSession().evict(contacts);
            Serializable contactsId = contacts.getId();
            Contacts copy = contacts.clone();
            contacts = contactsDao.get(contactsId);
            Assert.assertNotNull(copy);
            Assert.assertEquals(copy.getContactmethod(), contacts.getContactmethod());
            Assert.assertEquals(copy.getContactvalue(), contacts.getContactvalue());
            Assert.assertEquals(copy.getId(), contacts.getId());
            Assert.assertFalse(contacts.toString().equals(""));
            Assert.assertEquals(copy, copy.clone());
            Assert.assertEquals(copy, copy);
            Assert.assertNotSame(copy, 0L);
        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
            Assert.fail(e.getMessage());
        }
    }

    /**
	 * Hibernate test case for table: teachin.exercise_content
	 */
    @Test
    public void testExerciseContent() {
        try {
            ExerciseContentDao exerciseContentDao = HibernateTeachinDaoFactory.getExerciseContentDao();
            ExerciseContent exerciseContent = TeachinDataPoolFactory.getExerciseContent();
            exerciseContentDao.save(exerciseContent);
            GenericHibernateDaoImpl.getSession().flush();
            GenericHibernateDaoImpl.getSession().evict(exerciseContent);
            Serializable exerciseContentId = exerciseContent.getId();
            ExerciseContent copy = exerciseContent.clone();
            exerciseContent = exerciseContentDao.get(exerciseContentId);
            Assert.assertNotNull(copy);
            Assert.assertEquals(copy.getId(), exerciseContent.getId());
            Assert.assertEquals(copy.getQuestion(), exerciseContent.getQuestion());
            Assert.assertEquals(copy.getQuestionary(), exerciseContent.getQuestionary());
            Assert.assertEquals(copy.getQuestionFile(), exerciseContent.getQuestionFile());
            Assert.assertEquals(copy.getSolution(), exerciseContent.getSolution());
            Assert.assertEquals(copy.getSolutionDescription(), exerciseContent.getSolutionDescription());
            Assert.assertEquals(copy.getSolutionFile(), exerciseContent.getSolutionFile());
            Assert.assertFalse(exerciseContent.toString().equals(""));
            Assert.assertEquals(copy, copy.clone());
            Assert.assertEquals(copy, copy);
            Assert.assertNotSame(copy, 0L);
        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
            Assert.fail(e.getMessage());
        }
    }

    /**
	 * Hibernate test case for table: teachin.exercise_definition
	 */
    @Test
    public void testExerciseDefinition() {
        try {
            ExerciseDefinitionDao exerciseDefinitionDao = HibernateTeachinDaoFactory.getExerciseDefinitionDao();
            ExerciseDefinition exerciseDefinition = TeachinDataPoolFactory.getExerciseDefinition();
            exerciseDefinitionDao.save(exerciseDefinition);
            GenericHibernateDaoImpl.getSession().flush();
            GenericHibernateDaoImpl.getSession().evict(exerciseDefinition);
            Serializable exerciseDefinitionId = exerciseDefinition.getId();
            ExerciseDefinition copy = exerciseDefinition.clone();
            exerciseDefinition = exerciseDefinitionDao.get(exerciseDefinitionId);
            Assert.assertNotNull(copy);
            Assert.assertEquals(copy.getId(), exerciseDefinition.getId());
            Assert.assertEquals(copy.getTitle(), exerciseDefinition.getTitle());
            Assert.assertFalse(exerciseDefinition.toString().equals(""));
            Assert.assertEquals(copy, copy.clone());
            Assert.assertEquals(copy, copy);
            Assert.assertNotSame(copy, 0L);
        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
            Assert.fail(e.getMessage());
        }
    }

    /**
	 * Hibernate test case for table: teachin.grading_type
	 */
    @Test
    public void testGradingType() {
        try {
            GradingTypeDao gradingTypeDao = HibernateTeachinDaoFactory.getGradingTypeDao();
            GradingType gradingType = TeachinDataPoolFactory.getGradingType();
            gradingTypeDao.save(gradingType);
            GenericHibernateDaoImpl.getSession().flush();
            GenericHibernateDaoImpl.getSession().evict(gradingType);
            Serializable gradingTypeId = gradingType.getId();
            GradingType copy = gradingType.clone();
            gradingType = gradingTypeDao.get(gradingTypeId);
            Assert.assertNotNull(copy);
            Assert.assertEquals(copy.getId(), gradingType.getId());
            Assert.assertEquals(copy.getName(), gradingType.getName());
            Assert.assertFalse(gradingType.toString().equals(""));
            Assert.assertEquals(copy, copy.clone());
            Assert.assertEquals(copy, copy);
            Assert.assertNotSame(copy, 0L);
        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
            Assert.fail(e.getMessage());
        }
    }

    /**
	 * Hibernate test case for table: teachin.permissions
	 */
    @Test
    public void testPermissions() {
        try {
            PermissionsDao permissionsDao = HibernateTeachinDaoFactory.getPermissionsDao();
            Permissions permissions = TeachinDataPoolFactory.getPermissions();
            permissionsDao.save(permissions);
            GenericHibernateDaoImpl.getSession().flush();
            GenericHibernateDaoImpl.getSession().evict(permissions);
            Serializable permissionsId = permissions.getId();
            Permissions copy = permissions.clone();
            permissions = permissionsDao.get(permissionsId);
            Assert.assertNotNull(copy);
            Assert.assertEquals(copy.getDescription(), permissions.getDescription());
            Assert.assertEquals(copy.getId(), permissions.getId());
            Assert.assertEquals(copy.getPermission(), permissions.getPermission());
            Assert.assertEquals(copy.getShortcut(), permissions.getShortcut());
            Assert.assertFalse(permissions.toString().equals(""));
            Assert.assertEquals(copy, copy.clone());
            Assert.assertEquals(copy, copy);
            Assert.assertNotSame(copy, 0L);
        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
            Assert.fail(e.getMessage());
        }
    }

    /**
	 * Hibernate test case for table: teachin.question_type
	 */
    @Test
    public void testQuestionType() {
        try {
            QuestionTypeDao questionTypeDao = HibernateTeachinDaoFactory.getQuestionTypeDao();
            QuestionType questionType = TeachinDataPoolFactory.getQuestionType();
            questionTypeDao.save(questionType);
            GenericHibernateDaoImpl.getSession().flush();
            GenericHibernateDaoImpl.getSession().evict(questionType);
            Serializable questionTypeId = questionType.getId();
            QuestionType copy = questionType.clone();
            questionType = questionTypeDao.get(questionTypeId);
            Assert.assertNotNull(copy);
            Assert.assertEquals(copy.getId(), questionType.getId());
            Assert.assertEquals(copy.getName(), questionType.getName());
            Assert.assertFalse(questionType.toString().equals(""));
            Assert.assertEquals(copy, copy.clone());
            Assert.assertEquals(copy, copy);
            Assert.assertNotSame(copy, 0L);
        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
            Assert.fail(e.getMessage());
        }
    }

    /**
	 * Hibernate test case for table: teachin.roles
	 */
    @Test
    public void testRoles() {
        try {
            RolesDao rolesDao = HibernateTeachinDaoFactory.getRolesDao();
            Roles roles = TeachinDataPoolFactory.getRoles();
            rolesDao.save(roles);
            GenericHibernateDaoImpl.getSession().flush();
            GenericHibernateDaoImpl.getSession().evict(roles);
            Serializable rolesId = roles.getId();
            Roles copy = roles.clone();
            roles = rolesDao.get(rolesId);
            Assert.assertNotNull(copy);
            Assert.assertEquals(copy.getDescription(), roles.getDescription());
            Assert.assertEquals(copy.getId(), roles.getId());
            Assert.assertEquals(copy.getRolename(), roles.getRolename());
            Assert.assertFalse(roles.toString().equals(""));
            Assert.assertEquals(copy, copy.clone());
            Assert.assertEquals(copy, copy);
            Assert.assertNotSame(copy, 0L);
        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
            Assert.fail(e.getMessage());
        }
    }

    /**
	 * Hibernate test case for table: teachin.role_permissions
	 */
    @Test
    public void testRolePermissions() {
        try {
            RolePermissionsDao rolePermissionsDao = HibernateTeachinDaoFactory.getRolePermissionsDao();
            RolePermissions rolePermissions = TeachinDataPoolFactory.getRolePermissions();
            HibernateTeachinDaoFactory.getPermissionsDao().saveOrUpdate(rolePermissions.getPermission());
            HibernateTeachinDaoFactory.getRolesDao().saveOrUpdate(rolePermissions.getRole());
            rolePermissionsDao.save(rolePermissions);
            GenericHibernateDaoImpl.getSession().flush();
            GenericHibernateDaoImpl.getSession().evict(rolePermissions);
            Serializable rolePermissionsId = rolePermissions.getId();
            RolePermissions copy = rolePermissions.clone();
            rolePermissions = rolePermissionsDao.get(rolePermissionsId);
            Assert.assertNotNull(copy);
            Assert.assertEquals(copy.getId(), rolePermissions.getId());
            Assert.assertEquals(copy.getPermission().getId(), rolePermissions.getPermission().getId());
            Assert.assertEquals(copy.getRole().getId(), rolePermissions.getRole().getId());
            Assert.assertFalse(rolePermissions.toString().equals(""));
            Assert.assertEquals(copy, copy.clone());
            Assert.assertEquals(copy, copy);
            Assert.assertNotSame(copy, 0L);
        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
            Assert.fail(e.getMessage());
        }
    }

    /**
	 * Hibernate test case for table: teachin.subject
	 */
    @Test
    public void testSubject() {
        try {
            SubjectDao subjectDao = HibernateTeachinDaoFactory.getSubjectDao();
            Subject subject = TeachinDataPoolFactory.getSubject();
            subjectDao.save(subject);
            GenericHibernateDaoImpl.getSession().flush();
            GenericHibernateDaoImpl.getSession().evict(subject);
            Serializable subjectId = subject.getId();
            Subject copy = subject.clone();
            subject = subjectDao.get(subjectId);
            Assert.assertNotNull(copy);
            Assert.assertEquals(copy.getId(), subject.getId());
            Assert.assertEquals(copy.getName(), subject.getName());
            Assert.assertFalse(subject.toString().equals(""));
            Assert.assertEquals(copy, copy.clone());
            Assert.assertEquals(copy, copy);
            Assert.assertNotSame(copy, 0L);
        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
            Assert.fail(e.getMessage());
        }
    }

    /**
	 * Hibernate test case for table: teachin.topic
	 */
    @Test
    public void testTopic() {
        try {
            TopicDao topicDao = HibernateTeachinDaoFactory.getTopicDao();
            Topic topic = TeachinDataPoolFactory.getTopic();
            topicDao.save(topic);
            GenericHibernateDaoImpl.getSession().flush();
            GenericHibernateDaoImpl.getSession().evict(topic);
            Serializable topicId = topic.getId();
            Topic copy = topic.clone();
            topic = topicDao.get(topicId);
            Assert.assertNotNull(copy);
            Assert.assertEquals(copy.getId(), topic.getId());
            Assert.assertEquals(copy.getName(), topic.getName());
            Assert.assertFalse(topic.toString().equals(""));
            Assert.assertEquals(copy, copy.clone());
            Assert.assertEquals(copy, copy);
            Assert.assertNotSame(copy, 0L);
        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
            Assert.fail(e.getMessage());
        }
    }

    /**
	 * Hibernate test case for table: teachin.users
	 */
    @Test
    public void testUsers() {
        try {
            UsersDao usersDao = HibernateTeachinDaoFactory.getUsersDao();
            Users users = TeachinDataPoolFactory.getUsers();
            usersDao.save(users);
            GenericHibernateDaoImpl.getSession().flush();
            GenericHibernateDaoImpl.getSession().evict(users);
            Serializable usersId = users.getId();
            Users copy = users.clone();
            users = usersDao.get(usersId);
            Assert.assertNotNull(copy);
            Assert.assertEquals(copy.getBirthname(), users.getBirthname());
            Assert.assertEquals(new java.text.SimpleDateFormat("dd/MM/yyyy").format(copy.getDateofbirth().getTime()), new java.text.SimpleDateFormat("dd/MM/yyyy").format(users.getDateofbirth().getTime()));
            Assert.assertEquals(copy.getFirstname(), users.getFirstname());
            Assert.assertEquals(copy.getGender(), users.getGender());
            Assert.assertEquals(copy.getId(), users.getId());
            Assert.assertEquals(copy.getLastname(), users.getLastname());
            Assert.assertEquals(copy.getPw(), users.getPw());
            Assert.assertEquals(copy.getUsername(), users.getUsername());
            Assert.assertFalse(users.toString().equals(""));
            Assert.assertEquals(copy, copy.clone());
            Assert.assertEquals(copy, copy);
            Assert.assertNotSame(copy, 0L);
        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
            Assert.fail(e.getMessage());
        }
    }

    /**
	 * Hibernate test case for table: teachin.user_addresses
	 */
    @Test
    public void testUserAddresses() {
        try {
            UserAddressesDao userAddressesDao = HibernateTeachinDaoFactory.getUserAddressesDao();
            UserAddresses userAddresses = TeachinDataPoolFactory.getUserAddresses();
            HibernateTeachinDaoFactory.getAddressesDao().saveOrUpdate(userAddresses.getAddress());
            HibernateTeachinDaoFactory.getUsersDao().saveOrUpdate(userAddresses.getUser());
            userAddressesDao.save(userAddresses);
            GenericHibernateDaoImpl.getSession().flush();
            GenericHibernateDaoImpl.getSession().evict(userAddresses);
            Serializable userAddressesId = userAddresses.getId();
            UserAddresses copy = userAddresses.clone();
            userAddresses = userAddressesDao.get(userAddressesId);
            Assert.assertNotNull(copy);
            Assert.assertEquals(copy.getAddress().getId(), userAddresses.getAddress().getId());
            Assert.assertEquals(copy.getId(), userAddresses.getId());
            Assert.assertEquals(copy.getUser().getId(), userAddresses.getUser().getId());
            Assert.assertFalse(userAddresses.toString().equals(""));
            Assert.assertEquals(copy, copy.clone());
            Assert.assertEquals(copy, copy);
            Assert.assertNotSame(copy, 0L);
        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
            Assert.fail(e.getMessage());
        }
    }

    /**
	 * Hibernate test case for table: teachin.user_contacts
	 */
    @Test
    public void testUserContacts() {
        try {
            UserContactsDao userContactsDao = HibernateTeachinDaoFactory.getUserContactsDao();
            UserContacts userContacts = TeachinDataPoolFactory.getUserContacts();
            HibernateTeachinDaoFactory.getContactsDao().saveOrUpdate(userContacts.getContact());
            HibernateTeachinDaoFactory.getUsersDao().saveOrUpdate(userContacts.getUser());
            userContactsDao.save(userContacts);
            GenericHibernateDaoImpl.getSession().flush();
            GenericHibernateDaoImpl.getSession().evict(userContacts);
            Serializable userContactsId = userContacts.getId();
            UserContacts copy = userContacts.clone();
            userContacts = userContactsDao.get(userContactsId);
            Assert.assertNotNull(copy);
            Assert.assertEquals(copy.getContact().getId(), userContacts.getContact().getId());
            Assert.assertEquals(copy.getId(), userContacts.getId());
            Assert.assertEquals(copy.getUser().getId(), userContacts.getUser().getId());
            Assert.assertFalse(userContacts.toString().equals(""));
            Assert.assertEquals(copy, copy.clone());
            Assert.assertEquals(copy, copy);
            Assert.assertNotSame(copy, 0L);
        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
            Assert.fail(e.getMessage());
        }
    }

    /**
	 * Hibernate test case for table: teachin.user_roles
	 */
    @Test
    public void testUserRoles() {
        try {
            UserRolesDao userRolesDao = HibernateTeachinDaoFactory.getUserRolesDao();
            UserRoles userRoles = TeachinDataPoolFactory.getUserRoles();
            HibernateTeachinDaoFactory.getRolesDao().saveOrUpdate(userRoles.getRole());
            HibernateTeachinDaoFactory.getUsersDao().saveOrUpdate(userRoles.getUser());
            userRolesDao.save(userRoles);
            GenericHibernateDaoImpl.getSession().flush();
            GenericHibernateDaoImpl.getSession().evict(userRoles);
            Serializable userRolesId = userRoles.getId();
            UserRoles copy = userRoles.clone();
            userRoles = userRolesDao.get(userRolesId);
            Assert.assertNotNull(copy);
            Assert.assertEquals(copy.getId(), userRoles.getId());
            Assert.assertEquals(copy.getRole().getId(), userRoles.getRole().getId());
            Assert.assertEquals(copy.getUser().getId(), userRoles.getUser().getId());
            Assert.assertFalse(userRoles.toString().equals(""));
            Assert.assertEquals(copy, copy.clone());
            Assert.assertEquals(copy, copy);
            Assert.assertNotSame(copy, 0L);
        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
            Assert.fail(e.getMessage());
        }
    }

    /**
	 * Hibernate test case for table: teachin.zipcodes
	 */
    @Test
    public void testZipcodes() {
        try {
            ZipcodesDao zipcodesDao = HibernateTeachinDaoFactory.getZipcodesDao();
            Zipcodes zipcodes = TeachinDataPoolFactory.getZipcodes();
            zipcodesDao.save(zipcodes);
            GenericHibernateDaoImpl.getSession().flush();
            GenericHibernateDaoImpl.getSession().evict(zipcodes);
            Serializable zipcodesId = zipcodes.getId();
            Zipcodes copy = zipcodes.clone();
            zipcodes = zipcodesDao.get(zipcodesId);
            Assert.assertNotNull(copy);
            Assert.assertEquals(copy.getCity(), zipcodes.getCity());
            Assert.assertEquals(copy.getCountry(), zipcodes.getCountry());
            Assert.assertEquals(copy.getId(), zipcodes.getId());
            Assert.assertEquals(copy.getZipcode(), zipcodes.getZipcode());
            Assert.assertFalse(zipcodes.toString().equals(""));
            Assert.assertEquals(copy, copy.clone());
            Assert.assertEquals(copy, copy);
            Assert.assertNotSame(copy, 0L);
        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
            Assert.fail(e.getMessage());
        }
    }

    /**
	 * Clears the database
	 */
    @Test
    public void testPostCleanDBFirstPass() {
        try {
            HibernateTeachinDaoFactory.getUserRolesDao().getQuery("delete from UserRoles").executeUpdate();
        } catch (Exception e) {
        }
        try {
            HibernateTeachinDaoFactory.getUserContactsDao().getQuery("delete from UserContacts").executeUpdate();
        } catch (Exception e) {
        }
        try {
            HibernateTeachinDaoFactory.getUserAddressesDao().getQuery("delete from UserAddresses").executeUpdate();
        } catch (Exception e) {
        }
        try {
            HibernateTeachinDaoFactory.getRolePermissionsDao().getQuery("delete from RolePermissions").executeUpdate();
        } catch (Exception e) {
        }
        try {
            HibernateTeachinDaoFactory.getZipcodesDao().getQuery("delete from Zipcodes").executeUpdate();
        } catch (Exception e) {
        }
        try {
            HibernateTeachinDaoFactory.getUsersDao().getQuery("delete from Users").executeUpdate();
        } catch (Exception e) {
        }
        try {
            HibernateTeachinDaoFactory.getTopicDao().getQuery("delete from Topic").executeUpdate();
        } catch (Exception e) {
        }
        try {
            HibernateTeachinDaoFactory.getSubjectDao().getQuery("delete from Subject").executeUpdate();
        } catch (Exception e) {
        }
        try {
            HibernateTeachinDaoFactory.getRolesDao().getQuery("delete from Roles").executeUpdate();
        } catch (Exception e) {
        }
        try {
            HibernateTeachinDaoFactory.getQuestionTypeDao().getQuery("delete from QuestionType").executeUpdate();
        } catch (Exception e) {
        }
        try {
            HibernateTeachinDaoFactory.getPermissionsDao().getQuery("delete from Permissions").executeUpdate();
        } catch (Exception e) {
        }
        try {
            HibernateTeachinDaoFactory.getGradingTypeDao().getQuery("delete from GradingType").executeUpdate();
        } catch (Exception e) {
        }
        try {
            HibernateTeachinDaoFactory.getExerciseDefinitionDao().getQuery("delete from ExerciseDefinition").executeUpdate();
        } catch (Exception e) {
        }
        try {
            HibernateTeachinDaoFactory.getExerciseContentDao().getQuery("delete from ExerciseContent").executeUpdate();
        } catch (Exception e) {
        }
        try {
            HibernateTeachinDaoFactory.getContactsDao().getQuery("delete from Contacts").executeUpdate();
        } catch (Exception e) {
        }
        try {
            HibernateTeachinDaoFactory.getAddressesDao().getQuery("delete from Addresses").executeUpdate();
        } catch (Exception e) {
        }
    }

    /**
	 * Clears the database
	 */
    @Test
    public void postCleanDBFinalPass() {
        HibernateTeachinDaoFactory.getUserRolesDao().getQuery("delete from UserRoles").executeUpdate();
        HibernateTeachinDaoFactory.getUserContactsDao().getQuery("delete from UserContacts").executeUpdate();
        HibernateTeachinDaoFactory.getUserAddressesDao().getQuery("delete from UserAddresses").executeUpdate();
        HibernateTeachinDaoFactory.getRolePermissionsDao().getQuery("delete from RolePermissions").executeUpdate();
        HibernateTeachinDaoFactory.getZipcodesDao().getQuery("delete from Zipcodes").executeUpdate();
        HibernateTeachinDaoFactory.getUsersDao().getQuery("delete from Users").executeUpdate();
        HibernateTeachinDaoFactory.getTopicDao().getQuery("delete from Topic").executeUpdate();
        HibernateTeachinDaoFactory.getSubjectDao().getQuery("delete from Subject").executeUpdate();
        HibernateTeachinDaoFactory.getRolesDao().getQuery("delete from Roles").executeUpdate();
        HibernateTeachinDaoFactory.getQuestionTypeDao().getQuery("delete from QuestionType").executeUpdate();
        HibernateTeachinDaoFactory.getPermissionsDao().getQuery("delete from Permissions").executeUpdate();
        HibernateTeachinDaoFactory.getGradingTypeDao().getQuery("delete from GradingType").executeUpdate();
        HibernateTeachinDaoFactory.getExerciseDefinitionDao().getQuery("delete from ExerciseDefinition").executeUpdate();
        HibernateTeachinDaoFactory.getExerciseContentDao().getQuery("delete from ExerciseContent").executeUpdate();
        HibernateTeachinDaoFactory.getContactsDao().getQuery("delete from Contacts").executeUpdate();
        HibernateTeachinDaoFactory.getAddressesDao().getQuery("delete from Addresses").executeUpdate();
    }
}
