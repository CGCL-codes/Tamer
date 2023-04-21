package hermes.browser.model;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.swing.DefaultComboBoxModel;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Category;

/**
 * @author colincrist@hermesjms.com
 * @version $Id: BeanComboBoxModel.java,v 1.4 2004/09/16 20:30:51 colincrist Exp $
 */
public class BeanComboBoxModel extends DefaultComboBoxModel {

    /**
	 * 
	 */
    private static final long serialVersionUID = 511257801205550807L;

    private static Set ignore = new HashSet();

    private static final Category cat = Category.getInstance(BeanTableModel.class);

    static {
        ignore.add("class");
    }

    /**
     *  
     */
    public BeanComboBoxModel(Object bean) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        super();
        Map properties = BeanUtils.describe(bean);
        for (Iterator iter = properties.keySet().iterator(); iter.hasNext(); ) {
            addElement(iter.next());
        }
    }

    public BeanComboBoxModel(Object bean, String selection) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        this(bean);
        setSelectedItem(selection);
    }
}
