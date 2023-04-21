package com.avatal.persistency.dao.course;

import java.util.Collection;
import com.avatal.persistency.dao.exception.DAOException;

/**
 * DAO's zum Finden von Kursobjekten
 * @author m0550
 * Created on 31.07.2003
 */
public interface CourseDAO {

    /**
     * Liefert eine Liste mit Id's der Kurse welche die Suchkriterien erf�llen.
     * @param keywords
     * @param state
     * @return
     * @throws DAOException
     */
    public Collection getCoursesByKeywordAndState(String keywords, Integer state) throws DAOException;
}
