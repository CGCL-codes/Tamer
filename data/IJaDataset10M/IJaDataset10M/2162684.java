package net.googlecode.demenkov.dao;

import net.googlecode.demenkov.domains.Faculty;
import net.googlecode.demenkov.domains.University;
import java.util.List;

/**
 * DAO for actions with Faculties
 *
 * @author Demenkov Yura
 */
public interface FacultyDAO extends GenericDAO<Faculty, Integer> {

    /**
     * Finds faculty by facultyname and universityname in database
     *
     * @param facultyName
     * @param university
     * @return faculty if there is faculty with such facultyname and universityname in database or null if not
     */
    public Faculty findFacultyByFacultyNameAndUniversity(String facultyName, University university);

    /**
     * Finds all faculties by universityname in database
     *
     * @param universityName
     * @return list of faculties
     */
    public List<Faculty> findAllFacultiesByUniversityName(String universityName);
}
