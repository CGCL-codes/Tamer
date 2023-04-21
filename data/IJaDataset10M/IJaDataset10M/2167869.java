package whf.framework.lucene.index;

import java.util.List;
import junit.framework.TestCase;
import whf.framework.meta.entity.ObjectProperty;
import whf.framework.meta.service.ObjectPropertyServiceImp;
import whf.framework.meta.service.ObjectServiceImp;

/**
 * @author king
 * @create Jan 28, 2008 2:22:20 PM
 * 
 */
public class IndexTest extends TestCase {

    private IndexManager indexBuilder = new IndexManager();

    /**
	 * @author king
	 * @create Jan 28, 2008 2:26:14 PM
	 * @throws Exception
	 */
    public void testCreateObjects() throws Exception {
        List<whf.framework.meta.entity.Object> objects = ObjectServiceImp.getObjectService().findAll();
        indexBuilder.indexEntities(objects);
    }

    public void tes1tUpdateObjects() throws Exception {
        List<whf.framework.meta.entity.Object> objects = ObjectServiceImp.getObjectService().findAll();
        indexBuilder.updateEntities(objects);
    }

    /**
	 * @author king
	 * @create Jan 29, 2008 2:40:21 PM
	 * @throws Exception
	 */
    public void tes1tIndexObjectProperties() throws Exception {
        List<ObjectProperty> properties = ObjectPropertyServiceImp.getObjectPropertyService().findAll();
        indexBuilder.indexEntieies(properties);
    }
}
