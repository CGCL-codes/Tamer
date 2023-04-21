package pl.kwiecienm.cvms.model.dao;

import javax.persistence.Query;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Name;
import pl.kwiecienm.cvms.model.File;

/**
 * File dao class.
 * 
 * @author kwiecienm
 */
@Name("fileDao")
@AutoCreate
public class FileDao extends AbstractDao<File> {

    private static String Q_FIND_BY_ID = "select f from File f where f.id = :id";

    /**
	 * @param tagId
	 * @return
	 */
    public File findById(Integer tagId) {
        File file = null;
        Query query = getEntityManager().createQuery(Q_FIND_BY_ID);
        query.setParameter("id", tagId);
        file = (File) query.getSingleResult();
        return file;
    }
}
