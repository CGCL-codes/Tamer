package com.googlecode.openmpis.persistence.ibatis.service.impl;

import com.googlecode.openmpis.dto.Person;
import com.googlecode.openmpis.persistence.ibatis.dao.PersonDAO;
import com.googlecode.openmpis.persistence.ibatis.service.PersonService;
import com.googlecode.openmpis.util.Pagination;
import java.sql.SQLException;
import java.util.List;

/**
 * The PersonServiceImpl interface provides the ability to add, edit, delete, update and
 * retrieve persons.
 * 
 * @author  <a href="mailto:rvbabilonia@gmail.com">Rey Vincent Babilonia</a>
 * @version 1.0
 */
public class PersonServiceImpl implements PersonService {

    /**
     * The person DAO
     */
    private PersonDAO personDAO = null;

    /**
     * Creates a new person service implementation.
     *
     * @param personDAO   the person DAO
     */
    public PersonServiceImpl(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    /**
     * Retrieves all persons.
     *
     * @param pagination    the pagination context
     * @return              the list of all persons
     * @throws java.sql.SQLException
     */
    @Override
    public List<Person> getAllPersons(Pagination pagination) throws SQLException {
        return personDAO.getAllPersons(pagination);
    }

    /**
     * Retrieves all missing persons.
     *
     * @param pagination    the pagination context
     * @return              the list of all missing persons
     * @throws java.sql.SQLException
     */
    @Override
    public List<Person> getAllMissing(Pagination pagination) throws SQLException {
        return personDAO.getAllMissing(pagination);
    }

    /**
     * Retrieves the 4 newly-added missing persons.
     *
     * @return              the list of all missing persons
     * @throws java.sql.SQLException
     */
    @Override
    public List<Person> listNewMissing() throws SQLException {
        return personDAO.listNewMissing();
    }

    /**
     * Retrieves endangered missing persons.
     *
     * @param pagination    the pagination context
     * @return              the list of endangered missing persons
     * @throws java.sql.SQLException
     */
    @Override
    public List<Person> getMissing(Pagination pagination) throws SQLException {
        return personDAO.getMissing(pagination);
    }

    /**
     * Retrieves family abductions.
     *
     * @param pagination    the pagination context
     * @return              the list of family abductions
     * @throws java.sql.SQLException
     */
    @Override
    public List<Person> getFamilyAbduction(Pagination pagination) throws SQLException {
        return personDAO.getFamilyAbduction(pagination);
    }

    /**
     * Retrieves non-family abductions.
     *
     * @param pagination    the pagination context
     * @return              the list of non-family abductions
     * @throws java.sql.SQLException
     */
    @Override
    public List<Person> getNonFamilyAbduction(Pagination pagination) throws SQLException {
        return personDAO.getNonFamilyAbduction(pagination);
    }

    /**
     * Retrieves endangered runaway persons.
     *
     * @param pagination    the pagination context
     * @return              the list of endangered runaway persons
     * @throws java.sql.SQLException
     */
    @Override
    public List<Person> getRunaway(Pagination pagination) throws SQLException {
        return personDAO.getRunaway(pagination);
    }

    /**
     * Retrieves cases of unknown classification.
     *
     * @param pagination    the pagination context
     * @return              the list of cases of unknown classification
     * @throws java.sql.SQLException
     */
    @Override
    public List<Person> getUnknown(Pagination pagination) throws SQLException {
        return personDAO.getUnknown(pagination);
    }

    /**
     * Retrieves all found persons.
     *
     * @param pagination    the pagination context
     * @return              the list of all found persons
     * @throws java.sql.SQLException
     */
    @Override
    public List<Person> getAllFound(Pagination pagination) throws SQLException {
        return personDAO.getAllFound(pagination);
    }

    /**
     * Retrieves found persons.
     *
     * @param pagination    the pagination context
     * @return              the list of found persons
     * @throws java.sql.SQLException
     */
    @Override
    public List<Person> getFound(Pagination pagination) throws SQLException {
        return personDAO.getFound(pagination);
    }

    /**
     * Retrieves abandoned persons.
     *
     * @param pagination    the pagination context
     * @return              the list of abandoned persons
     * @throws java.sql.SQLException
     */
    @Override
    public List<Person> getAbandoned(Pagination pagination) throws SQLException {
        return personDAO.getAbandoned(pagination);
    }

    /**
     * Retrieves throwaway persons.
     *
     * @param pagination    the pagination context
     * @return              the list of throwaway persons
     * @throws java.sql.SQLException
     */
    @Override
    public List<Person> getThrowaway(Pagination pagination) throws SQLException {
        return personDAO.getThrowaway(pagination);
    }

    /**
     * Retrieves unidentified persons.
     *
     * @param pagination    the pagination context
     * @return              the list of unidentified persons
     * @throws java.sql.SQLException
     */
    @Override
    public List<Person> getUnidentified(Pagination pagination) throws SQLException {
        return personDAO.getUnidentified(pagination);
    }

    /**
     * Retrieves ongoing cases.
     *
     * @param pagination    the pagination context
     * @return              the list of ongoing cases
     * @throws java.sql.SQLException
     */
    @Override
    public List<Person> getOngoing(Pagination pagination) throws SQLException {
        return personDAO.getOngoing(pagination);
    }

    /**
     * Retrieves ongoing cases.
     *
     * @return              the list of ongoing cases
     * @throws java.sql.SQLException
     */
    @Override
    public List<Person> listOngoing() throws SQLException {
        return personDAO.listOngoing();
    }

    /**
     * Retrieves an ongoing case given a person's ID.
     *
     * @param id            the person ID
     * @return              the person
     * @throws java.sql.SQLException
     */
    @Override
    public Person getOngoingCaseById(Integer id) throws SQLException {
        return personDAO.getOngoingCaseById(id);
    }

    /**
     * Retrieves solved cases.
     *
     * @param pagination    the pagination context
     * @return              the list of solved cases
     * @throws java.sql.SQLException
     */
    @Override
    public List<Person> getSolved(Pagination pagination) throws SQLException {
        return personDAO.getSolved(pagination);
    }

    /**
     * Retrieves solved cases.
     *
     * @return              the list of solved cases
     * @throws java.sql.SQLException
     */
    @Override
    public List<Person> listSolved() throws SQLException {
        return personDAO.listSolved();
    }

    /**
     * Retrieves unsolved cases.
     *
     * @param pagination    the pagination context
     * @return              the list of unsolved cases
     * @throws java.sql.SQLException
     */
    @Override
    public List<Person> getUnsolved(Pagination pagination) throws SQLException {
        return personDAO.getUnsolved(pagination);
    }

    /**
     * Retrieves unsolved cases.
     *
     * @return              the list of unsolved cases
     * @throws java.sql.SQLException
     */
    @Override
    public List<Person> listUnsolved() throws SQLException {
        return personDAO.listUnsolved();
    }

    /**
     * Retrieves all persons created by a given encoder.
     *
     * @param pagination    the pagination context
     * @param encoderId     the encoder ID
     * @return              the list of all persons created by a given encoder
     * @throws java.sql.SQLException
     */
    @Override
    public List<Person> getPersonsByEncoderId(Pagination pagination, Integer encoderId) throws SQLException {
        return personDAO.getPersonsByEncoderId(pagination, encoderId);
    }

    /**
     * Retrieves all persons handled by a given investigator.
     *
     * @param pagination    the pagination context
     * @param investigatorId the investigator ID
     * @return              the list of all persons handled by a given investigator
     * @throws java.sql.SQLException
     */
    @Override
    public List<Person> getPersonsByInvestigatorId(Pagination pagination, Integer investigatorId) throws SQLException {
        return personDAO.getPersonsByInvestigatorId(pagination, investigatorId);
    }

    /**
     * Retrieves all persons abducted by a given abductor.
     *
     * @param pagination    the pagination context
     * @param abductorId    the abductor ID
     * @return              the list of all persons abducted by a given abductor
     * @throws java.sql.SQLException
     */
    @Override
    public List<Person> getPersonsByAbductorId(Pagination pagination, Integer abductorId) throws SQLException {
        return personDAO.getPersonsByAbductorId(pagination, abductorId);
    }

    /**
     * Searches for persons.
     *
     * @param pagination    the pagination context
     * @param keyword       the search keyword
     * @return              the list of matching persons
     */
    @Override
    public List<Person> simpleSearch(Pagination pagination, String keyword) throws SQLException {
        return personDAO.simpleSearch(pagination, keyword);
    }

    /**
     * Retrieves a person given his ID.
     * 
     * @param id            the person ID
     * @return              the person
     * @throws java.sql.SQLException
     */
    @Override
    public Person getPersonById(Integer id) throws SQLException {
        return personDAO.getPersonById(id);
    }

    /**
     * Inserts a new person.
     * 
     * @param person        the new person
     * @return              the last index
     * @throws java.sql.SQLException
     */
    @Override
    public int insertPerson(Person person) throws SQLException {
        return personDAO.insertPerson(person);
    }

    /**
     * Updates an existing person.
     * 
     * @param person        the existing person
     * @return              <code>true</code> if the person was successfully updated; <code>false</code> otherwise
     * @throws java.sql.SQLException
     */
    @Override
    public boolean updatePerson(Person person) throws SQLException {
        return personDAO.updatePerson(person);
    }

    /**
     * Updates a person's abductor ID.
     * 
     * @param person        the existing person
     * @return              <code>true</code> if the person's abductor ID was successfully updated; <code>false</code> otherwise
     * @throws java.sql.SQLException
     */
    @Override
    public boolean updatePersonAbductor(Person person) throws SQLException {
        return personDAO.updatePersonAbductor(person);
    }

    /**
     * Updates a person's investigator ID.
     *
     * @param person        the existing person
     * @return              <code>true</code> if the person's investigator ID was successfully updated; <code>false</code> otherwise
     * @throws java.sql.SQLException
     */
    @Override
    public boolean updatePersonInvestigator(Person person) throws SQLException {
        return personDAO.updatePersonInvestigator(person);
    }

    /**
     * Updates a person's encoder ID.
     *
     * @param person        the existing person
     * @return              <code>true</code> if the person's encoder ID was successfully updated; <code>false</code> otherwise
     * @throws java.sql.SQLException
     */
    @Override
    public boolean updatePersonEncoder(Person person) throws SQLException {
        return personDAO.updatePersonEncoder(person);
    }

    /**
     * Updates a person's relative ID.
     *
     * @param person        the existing person
     * @return              <code>true</code> if the person's relative ID was successfully updated; <code>false</code> otherwise
     * @throws java.sql.SQLException
     */
    @Override
    public boolean updatePersonRelative(Person person) throws SQLException {
        return personDAO.updatePersonRelative(person);
    }

    /**
     * Deletes a person with the specified ID.
     * 
     * @param id            the ID of the person to be deleted
     * @return              <code>true</code> if the person was successfully deleted; <code>false</code> otherwise
     * @throws java.sql.SQLException
     */
    @Override
    public boolean deletePerson(Integer id) throws SQLException {
        return personDAO.deletePerson(id);
    }

    /**
     * Checks if a person is unique.
     * 
     * @param person        the existing person
     * @return              <code>true</code> if the person is unique; <code>false</code> otherwise
     * @throws java.sql.SQLException
     */
    @Override
    public boolean isUniquePerson(Person person) throws SQLException {
        return personDAO.isUniquePerson(person);
    }

    /**
     * Returns the total number of persons.
     *
     * @return              the total number of persons
     * @throws java.sql.SQLException
     */
    @Override
    public int countAllPersons() throws SQLException {
        return personDAO.countAllPersons();
    }

    /**
     * Returns the total number of ongoing cases.
     *
     * @return              the total number of ongoing cases
     * @throws java.sql.SQLException
     */
    @Override
    public int countOngoing() throws SQLException {
        return personDAO.countOngoing();
    }

    /**
     * Returns the total number of solved cases.
     *
     * @return              the total number of solved cases
     * @throws java.sql.SQLException
     */
    @Override
    public int countSolved() throws SQLException {
        return personDAO.countSolved();
    }

    /**
     * Returns the total number of unsolved cases.
     *
     * @return              the total number of unsolved cases
     * @throws java.sql.SQLException
     */
    @Override
    public int countUnsolved() throws SQLException {
        return personDAO.countUnsolved();
    }

    /**
     * Returns the total number of all ongoing and missing persons.
     *
     * @return              the total number of all ongoing and missing persons
     * @throws java.sql.SQLException
     */
    @Override
    public int countAllMissing() throws SQLException {
        return personDAO.countAllMissing();
    }

    /**
     * Returns the total number of all solved and missing persons.
     *
     * @return              the total number of all solved and missing persons
     * @throws java.sql.SQLException
     */
    @Override
    public int countAllSolvedMissing() throws SQLException {
        return personDAO.countAllSolvedMissing();
    }

    /**
     * Returns the total number of all unsolved and missing persons.
     *
     * @return              the total number of all unsolved and missing persons
     * @throws java.sql.SQLException
     */
    @Override
    public int countAllUnsolvedMissing() throws SQLException {
        return personDAO.countAllSolvedMissing();
    }

    /**
     * Returns the total number of endangered missing persons.
     *
     * @return              the total number of endangered missing persons
     * @throws java.sql.SQLException
     */
    @Override
    public int countMissing() throws SQLException {
        return personDAO.countMissing();
    }

    /**
     * Returns the total number of solved and missing persons.
     *
     * @return              the total number of solved and missing persons
     * @throws java.sql.SQLException
     */
    @Override
    public int countSolvedMissing() throws SQLException {
        return personDAO.countSolvedMissing();
    }

    /**
     * Returns the total number of unsolved and missing persons.
     *
     * @return              the total number of unsolved and missing persons
     * @throws java.sql.SQLException
     */
    @Override
    public int countUnsolvedMissing() throws SQLException {
        return personDAO.countUnsolvedMissing();
    }

    /**
     * Returns the total number of family abductions.
     *
     * @return              the total number of family abductions
     * @throws java.sql.SQLException
     */
    @Override
    public int countFamilyAbduction() throws SQLException {
        return personDAO.countFamilyAbduction();
    }

    /**
     * Returns the total number of solved family abductions.
     *
     * @return              the total number of solved family abductions
     * @throws java.sql.SQLException
     */
    @Override
    public int countSolvedFamilyAbduction() throws SQLException {
        return personDAO.countSolvedFamilyAbduction();
    }

    /**
     * Returns the total number of unsolved family abductions.
     *
     * @return              the total number of unsolved family abductions
     * @throws java.sql.SQLException
     */
    @Override
    public int countUnsolvedFamilyAbduction() throws SQLException {
        return personDAO.countUnsolvedFamilyAbduction();
    }

    /**
     * Returns the total number of non-family abductions.
     *
     * @return              the total number of non-family abductions
     * @throws java.sql.SQLException
     */
    @Override
    public int countNonFamilyAbduction() throws SQLException {
        return personDAO.countNonFamilyAbduction();
    }

    /**
     * Returns the total number of solved non-family abductions.
     *
     * @return              the total number of solved non-family abductions
     * @throws java.sql.SQLException
     */
    @Override
    public int countSolvedNonFamilyAbduction() throws SQLException {
        return personDAO.countSolvedNonFamilyAbduction();
    }

    /**
     * Returns the total number of unsolved non-family abductions.
     *
     * @return              the total number of unsolved non-family abductions
     * @throws java.sql.SQLException
     */
    @Override
    public int countUnsolvedNonFamilyAbduction() throws SQLException {
        return personDAO.countUnsolvedNonFamilyAbduction();
    }

    /**
     * Returns the total number of runaway persons.
     *
     * @return              the total number of runaway persons
     * @throws java.sql.SQLException
     */
    @Override
    public int countRunaway() throws SQLException {
        return personDAO.countRunaway();
    }

    /**
     * Returns the total number of solved and runaway persons.
     *
     * @return              the total number of solved and runaway persons
     * @throws java.sql.SQLException
     */
    @Override
    public int countSolvedRunaway() throws SQLException {
        return personDAO.countSolvedRunaway();
    }

    /**
     * Returns the total number of unsolved and runaway persons.
     *
     * @return              the total number of unsolved and runaway persons
     * @throws java.sql.SQLException
     */
    @Override
    public int countUnsolvedRunaway() throws SQLException {
        return personDAO.countUnsolvedRunaway();
    }

    /**
     * Returns the total number of unknown cases.
     *
     * @return              the total number of unknown cases
     * @throws java.sql.SQLException
     */
    @Override
    public int countUnknown() throws SQLException {
        return personDAO.countUnknown();
    }

    /**
     * Returns the total number of solved and unknown cases.
     *
     * @return              the total number of solved and unknown cases
     * @throws java.sql.SQLException
     */
    @Override
    public int countSolvedUnknown() throws SQLException {
        return personDAO.countSolvedUnknown();
    }

    /**
     * Returns the total number of unsolved and unknown cases.
     *
     * @return              the total number of unsolved and unknown cases
     * @throws java.sql.SQLException
     */
    @Override
    public int countUnsolvedUnknown() throws SQLException {
        return personDAO.countUnsolvedUnknown();
    }

    /**
     * Returns the total number of all ongoing and found persons.
     *
     * @return              the total number of all ongoing and found persons
     * @throws java.sql.SQLException
     */
    @Override
    public int countAllFound() throws SQLException {
        return personDAO.countAllFound();
    }

    /**
     * Returns the total number of all solved and found persons.
     *
     * @return              the total number of all solved and found persons
     * @throws java.sql.SQLException
     */
    @Override
    public int countAllSolvedFound() throws SQLException {
        return personDAO.countAllSolvedFound();
    }

    /**
     * Returns the total number of all unsolved and found persons.
     *
     * @return              the total number of all unsolved and found persons
     * @throws java.sql.SQLException
     */
    @Override
    public int countAllUnsolvedFound() throws SQLException {
        return personDAO.countAllUnsolvedFound();
    }

    /**
     * Returns the total number of found persons.
     *
     * @return              the total number of found persons
     * @throws java.sql.SQLException
     */
    @Override
    public int countFound() throws SQLException {
        return personDAO.countFound();
    }

    /**
     * Returns the total number of solved and found persons.
     *
     * @return              the total number of solved and found persons
     * @throws java.sql.SQLException
     */
    @Override
    public int countSolvedFound() throws SQLException {
        return personDAO.countSolvedFound();
    }

    /**
     * Returns the total number of unsolved and found persons.
     *
     * @return              the total number of unsolved and found persons
     * @throws java.sql.SQLException
     */
    @Override
    public int countUnsolvedFound() throws SQLException {
        return personDAO.countUnsolvedFound();
    }

    /**
     * Returns the total number of abandoned persons.
     *
     * @return              the total number of abandoned persons
     * @throws java.sql.SQLException
     */
    @Override
    public int countAbandoned() throws SQLException {
        return personDAO.countAbandoned();
    }

    /**
     * Returns the total number of solved and abandoned persons.
     *
     * @return              the total number of solved and abandoned persons
     * @throws java.sql.SQLException
     */
    @Override
    public int countSolvedAbandoned() throws SQLException {
        return personDAO.countSolvedAbandoned();
    }

    /**
     * Returns the total number of unsolved and abandoned persons.
     *
     * @return              the total number of unsolved and abandoned persons
     * @throws java.sql.SQLException
     */
    @Override
    public int countUnsolvedAbandoned() throws SQLException {
        return personDAO.countUnsolvedAbandoned();
    }

    /**
     * Returns the total number of throwaway persons.
     *
     * @return              the total number of throwaway persons
     * @throws java.sql.SQLException
     */
    @Override
    public int countThrowaway() throws SQLException {
        return personDAO.countThrowaway();
    }

    /**
     * Returns the total number of solved and throwaway persons.
     *
     * @return              the total number of solved and throwaway persons
     * @throws java.sql.SQLException
     */
    @Override
    public int countSolvedThrowaway() throws SQLException {
        return personDAO.countSolvedThrowaway();
    }

    /**
     * Returns the total number of unsolved and throwaway persons.
     *
     * @return              the total number of unsolved and throwaway persons
     * @throws java.sql.SQLException
     */
    @Override
    public int countUnsolvedThrowaway() throws SQLException {
        return personDAO.countUnsolvedThrowaway();
    }

    /**
     * Returns the total number of unidentified persons.
     *
     * @return              the total number of unidentified persons
     * @throws java.sql.SQLException
     */
    @Override
    public int countUnidentified() throws SQLException {
        return personDAO.countUnidentified();
    }

    /**
     * Returns the total number of solved and unidentified persons.
     *
     * @return              the total number of solved and unidentified persons
     * @throws java.sql.SQLException
     */
    @Override
    public int countSolvedUnidentified() throws SQLException {
        return personDAO.countSolvedUnidentified();
    }

    /**
     * Returns the total number of unsolved and unidentified persons.
     *
     * @return              the total number of unsolved and unidentified persons
     * @throws java.sql.SQLException
     */
    @Override
    public int countUnsolvedUnidentified() throws SQLException {
        return personDAO.countUnsolvedUnidentified();
    }

    /**
     * Returns the total number of persons created by a given encoder.
     *
     * @param encoderId     the encoder ID
     * @return              the total number of persons created by a given encoder
     * @throws java.sql.SQLException
     */
    @Override
    public int countPersonsByEncoderId(Integer encoderId) throws SQLException {
        return personDAO.countPersonsByEncoderId(encoderId);
    }

    /**
     * Returns the total number of persons handled by a given investigator.
     *
     * @param investigatorId the investigator ID
     * @return              the total number of persons handled by a given investigator
     * @throws java.sql.SQLException
     */
    @Override
    public int countPersonsByInvestigatorId(Integer investigatorId) throws SQLException {
        return personDAO.countPersonsByInvestigatorId(investigatorId);
    }

    /**
     * Returns the total number of persons abducted by a given abductor.
     *
     * @param abductorId    the abductor ID
     * @return              the total number of persons abducted by a given abductor
     * @throws java.sql.SQLException
     */
    @Override
    public int countPersonsByAbductorId(Integer abductorId) throws SQLException {
        return personDAO.countPersonsByAbductorId(abductorId);
    }

    /**
     * Returns the total number of persons related to a given relative.
     *
     * @param relativeId    the abductor ID
     * @return              the total number of persons related to a given relative
     * @throws java.sql.SQLException
     */
    @Override
    public int countPersonsByRelativeId(Integer relativeId) throws SQLException {
        return personDAO.countPersonsByRelativeId(relativeId);
    }
}
