package test.dicom4j.jlds.core;

import org.dicom4j.jlds.core.beans.Instance;
import org.dicom4j.jlds.core.beans.Serie;
import org.dicom4j.jlds.core.beans.Study;

/**
 * class use to provides test data
 *
 * @since 0.1
 * @author <a href="mailto:straahd@users.sourceforge.net">Laurent Lecomte 
 *
 */
public class CoreTestData {

    public static Serie newSerie() {
        Serie lbean = new Serie();
        lbean.setDescription("Description");
        return lbean;
    }

    public static Study newStudy() {
        Study lbean = new Study();
        lbean.setDescription("Description");
        return lbean;
    }

    public static Instance newInstance() {
        Instance lBean = new Instance();
        lBean.setInstanceUID("ABCDEF");
        return lBean;
    }
}
