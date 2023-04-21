package weka.gui.beans;

import java.beans.*;

/**
 * BeanInfo class for the cross validation fold maker bean
 *
 * @author <a href="mailto:mhall@cs.waikato.ac.nz">Mark Hall</a>
 * @version $Revision: 1.2 $
 */
public class CrossValidationFoldMakerBeanInfo extends AbstractTrainAndTestSetProducerBeanInfo {

    /**
   * Return the property descriptors for this bean
   *
   * @return a <code>PropertyDescriptor[]</code> value
   */
    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            PropertyDescriptor p1;
            PropertyDescriptor p2;
            p1 = new PropertyDescriptor("folds", CrossValidationFoldMaker.class);
            p2 = new PropertyDescriptor("seed", CrossValidationFoldMaker.class);
            PropertyDescriptor[] pds = { p1, p2 };
            return pds;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
   * Return the bean descriptor for this bean
   *
   * @return a <code>BeanDescriptor</code> value
   */
    public BeanDescriptor getBeanDescriptor() {
        return new BeanDescriptor(weka.gui.beans.CrossValidationFoldMaker.class, CrossValidationFoldMakerCustomizer.class);
    }
}
