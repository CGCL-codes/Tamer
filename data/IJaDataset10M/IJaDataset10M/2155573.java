package meconsea.webcoll.base.dao;

import java.io.Serializable;
import java.util.List;
import com.wideplay.warp.persist.Transactional;

/**
 * <p>�ò��ִ���Դ��warp-persist/example</p>
 * @author meconsea
 *         MSN:  meconsea@hotmail.com
 *         Mail: wangzhanhai@139.com
 *  
 * @param <T>
 * @param <PK>
 */
public interface IDAO<T, PK extends Serializable> {

    /**
	 * Generic method to get an object based on class and identifier.
	 * 
	 * @param id
	 *            the identifier (primary key) of the object to get.
	 * @return a populated object.
	 */
    public T find(PK id);

    /**
	 * Generic method used to get all objects of a particular type. This is the
	 * same as lookup up all rows in a table.
	 * 
	 * @return the list of populated objects.
	 */
    public List<T> findAll();

    /**
	 * Generic method to save an object - handles both update and insert.
	 * 
	 * @param object
	 *            the object to save.
	 * @return the persisted object.
	 */
    @Transactional
    public T save(T object);

    /**
	 * Generic method to delete an object based on class and id.
	 * 
	 * @param id
	 *            the identifier (primary key) of the object to remove.
	 */
    @Transactional
    public void remove(PK id);
}
