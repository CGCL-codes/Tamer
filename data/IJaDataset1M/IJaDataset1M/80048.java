package net.sf.esims.dao;

import java.util.List;
import net.sf.esims.model.entity.School;
import net.sf.esims.model.entity.SchoolEssence;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.operation.DatabaseOperation;

public class SchoolSpringDAOTest extends EsimsTestBaseDAO {

    private IDatabaseConnection connection;

    private IDataSet schoolDataSet;

    private SchoolDAOIfc schoolDAO;

    public void testSave() throws Exception {
        DatabaseOperation.DELETE_ALL.execute(connection, schoolDataSet);
        SchoolEssence essence = this.createValidInstance();
        School school = essence.createSchool();
        this.schoolDAO.save(school);
        DatabaseOperation.DELETE_ALL.execute(connection, schoolDataSet);
    }

    public void testDelete() throws Exception {
        DatabaseOperation.DELETE_ALL.execute(connection, schoolDataSet);
        SchoolEssence essence = this.createValidInstance();
        School school = essence.createSchool();
        this.schoolDAO.save(school);
        this.schoolDAO.delete(school);
        try {
            this.schoolDAO.save(null);
            fail("Must throw out when null is passed");
        } catch (IllegalArgumentException ie) {
        }
        DatabaseOperation.DELETE_ALL.execute(connection, schoolDataSet);
    }

    public void testGets() throws Exception {
        DatabaseOperation.DELETE_ALL.execute(connection, schoolDataSet);
        SchoolEssence essence = this.createValidInstance();
        School school = essence.createSchool();
        this.schoolDAO.save(school);
        List<School> listOfSchools = this.schoolDAO.getAll();
        assertNotNull(listOfSchools);
        Long id = listOfSchools.get(0).getSchoolId();
        assertNotNull(this.schoolDAO.get(id));
        assertNotNull(this.schoolDAO.getSchoolsByBoardOfEducation(essence.getBoardOfEducation()));
        assertNotNull(this.schoolDAO.getSchoolsByPrincipalFirstName(essence.getPrincipalFirstName()));
        assertNotNull(this.schoolDAO.getSchoolsByName(essence.getSchoolName()));
        DatabaseOperation.DELETE_ALL.execute(connection, schoolDataSet);
    }

    @Override
    protected void onSetUp() throws Exception {
        this.connection = super.getConnectionForTests();
        schoolDataSet = getDataSet("schoolDataSet.xml");
    }

    private SchoolEssence createValidInstance() {
        SchoolEssence essence = new SchoolEssence();
        essence.setAddressLine1("ADD1");
        essence.setAddressLine2("ADD2");
        essence.setAddressLine3("ADD3");
        essence.setBoardOfEducation("CBSE");
        essence.setCountry("India");
        essence.setDistrict("Bengalooru");
        essence.setEmail("fictious_man@fictiousschool.com");
        essence.setFax("8023423450");
        essence.setIsActive(Boolean.TRUE);
        essence.setManagement("Test mgmt");
        essence.setPhone1("8023423450");
        essence.setPhone1("8023423450");
        essence.setPhone1("8023423450");
        essence.setPincode("654321");
        essence.setPrincipalFirstName("Muttal");
        essence.setPrincipalMiddleName("Kashapp");
        essence.setPrincipalLastName("Raman");
        essence.setSchoolName("Fictious school");
        essence.setSchoolCode("ABC");
        essence.setState("Karanataka");
        essence.setVicePrincipalFirstName("Dodda");
        essence.setVicePrincipalMiddleName("Belliappa");
        essence.setVicePrincipalLastName("Puttanna");
        return essence;
    }

    public void setSchoolDAO(SchoolDAOIfc schoolDAO) {
        this.schoolDAO = schoolDAO;
    }
}
