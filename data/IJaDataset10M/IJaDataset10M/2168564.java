package persistencia.cv;

import java.util.Collection;

/**
 * 
 * @author Fernando
 */
public interface DAOInterface {

    public Object get(String businessId);

    public int insert(Object b);

    public void update(Object b);

    public void delete(Object b);

    public Collection getAll();
}
