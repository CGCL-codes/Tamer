package com.plus.fcentre.jobfilter.dao;

import java.util.Collection;
import com.plus.fcentre.jobfilter.domain.CV;
import com.plus.fcentre.jobfilter.domain.CVContentType;
import com.plus.fcentre.jobfilter.domain.VacancyApplication;

/**
 * Data Access Object interface providing {@link CV} persistence operations.
 * 
 * @author Steve Jefferson
 */
public interface CVDAO {

    /**
	 * Find CV with given id.
	 * 
	 * @param id CV id.
	 * @return CV with given id or null if no such CV.
	 */
    public CV findCVById(long id);

    /**
	 * Find CV with given name
	 * 
	 * @param cvName CV name.
	 * @return CV with given name or null if no such CV.
	 */
    public CV findCVByName(String cvName);

    /**
	 * Find all CV's ordered by creation date.
	 * 
	 * @return all CVs.
	 */
    public Collection<CV> findAllCVs();

    /**
	 * Find all vacancy applications made using the provided CV.
	 * 
	 * @param cv CV used for vacancy applications.
	 * @return list of vacancy applications made with CV.
	 */
    public Collection<VacancyApplication> findAllVacancyApplications(CV cv);

    /**
	 * Find CVContentType with given id.
	 * 
	 * @param id CVContentType id.
	 * @return CVContentType with id or null if no such CVContentType.
	 */
    public CVContentType findCVContentTypeById(long id);

    /**
	 * Find CVContentType with given name.
	 * 
	 * @param name CVContentType name.
	 * @return CVContentType with name or null if no such CVContentType.
	 */
    public CVContentType findCVContentTypeByName(String name);

    /**
	 * Find all CV content types.
	 * 
	 * @return list of CV content types.
	 */
    public Collection<CVContentType> findAllCVContentTypes();

    /**
	 * Insert a new CV content type into persistent store.
	 * 
	 * @param contentType CV content type to insert.
	 * @return inserted CV content type.
	 */
    public CVContentType insertCVContentType(CVContentType contentType);

    /**
	 * Insert a new CV in persistent store.
	 * 
	 * @param cv CV to insert.
	 * @return inserted CV.
	 */
    public CV insertCV(CV cv);

    /**
	 * Remove provided CV from persistent store.
	 * 
	 * @param cv CV to remove.
	 */
    public void removeCV(CV cv);

    /**
	 * Attach the provided (detached) CV to the current transaction.
	 * 
	 * @param cv CV to attach.
	 * @return attached CV.
	 */
    public CV attachCV(CV cv);
}
