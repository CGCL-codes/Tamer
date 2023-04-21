package org.ebadat.dao;

import org.ebadat.domain.Centro;
import java.util.List;

/**
 *
 * @author Sonia
 */
public interface CentrosDAO {

    List<Centro> getAll();

    int insert(Centro c1);

    int update(Centro c1);

    Centro get(long id);
}
