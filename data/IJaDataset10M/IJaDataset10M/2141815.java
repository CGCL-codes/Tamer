package org.sourceforge.vlibrary.user.dao;

import java.util.ArrayList;
import org.sourceforge.vlibrary.exceptions.LibraryException;
import org.sourceforge.vlibrary.user.domain.Subject;

/**
 * @version $Revision$ $Date$
 */
public interface SubjectDAO {

    /******************************************************************************
     * Inserts *this* into the database. Has the side effect of
     *  setting this.id to the sequentially generated id of the new record.
     *  First checks to see if a subject with this.description exists.
     *  If so, no insert is performed, but this.id is updated to the id of
     *  the previously existing record.  Delegates to insert(Connection).
     *
     * @throws LibraryException
     *****************************************************************************/
    public Subject insert(Subject subject) throws LibraryException;

    /******************************************************************************
     * Walks input ArrayList pf {@link Subject} instances, looking up and
     * setting ID properties for each Subject in the list.  Lookup is based on
     * the description property. If a Subject is not found, a new entry is
     * inserted into the database and the ID of the newly created record is
     * assigned to the instance.
     *
     * @param subjects = ArrayList of Subjects
     * @exception LibraryException if the list contains objects that are not
     * Subject instances or a data access error occurs
     *****************************************************************************/
    public void setSubjectIds(ArrayList subjects) throws LibraryException;

    /******************************************************************************
     * Retrieves all subjects from the database
     *
     * @return ArrayList of Subject object, if none found returns empty ArrayList
     * @throws LibraryException
     *****************************************************************************/
    public ArrayList getSubjects() throws LibraryException;

    public Long getSubjectIdByDescription(Subject subject) throws LibraryException;
}
