package dndbeans;

import java.beans.*;

/**
 * 
 * 
 * 
 * $Date: 2003/12/25 07:45:45 $<br>
 * @author tedberg
 * @author $Author: tedberg $
 * @version $Revision: 1.3 $
 * @since Oct 4, 2003
 */
public class ExperienceBeanBeanInfo extends SimpleBeanInfo {

    Class beanClass = ExperienceBean.class;

    String iconColor16x16Filename;

    String iconColor32x32Filename;

    String iconMono16x16Filename;

    String iconMono32x32Filename;

    public ExperienceBeanBeanInfo() {
    }

    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            PropertyDescriptor _cohort = new PropertyDescriptor("cohort", beanClass, "isCohort", "setCohort");
            _cohort.setDisplayName("Cohort");
            _cohort.setShortDescription("Cohort");
            _cohort.setBound(true);
            PropertyDescriptor _experience = new PropertyDescriptor("experience", beanClass, "getExperience", "setExperience");
            _experience.setDisplayName("XP");
            _experience.setShortDescription("XP");
            _experience.setBound(true);
            PropertyDescriptor _included = new PropertyDescriptor("included", beanClass, "isIncluded", "setIncluded");
            _included.setDisplayName("Included");
            _included.setShortDescription("Included");
            _included.setBound(true);
            PropertyDescriptor _level = new PropertyDescriptor("level", beanClass, "getLevel", "setLevel");
            _level.setDisplayName("Level");
            _level.setShortDescription("Level");
            PropertyDescriptor _levelAdjustment = new PropertyDescriptor("levelAdjustment", beanClass, "getLevelAdjustment", "setLevelAdjustment");
            _levelAdjustment.setDisplayName("Level Adjustment");
            _levelAdjustment.setShortDescription("Level adj");
            _levelAdjustment.setBound(true);
            PropertyDescriptor _name = new PropertyDescriptor("name", beanClass, "getName", "setName");
            _name.setDisplayName("Name");
            _name.setShortDescription("Name");
            _name.setBound(true);
            PropertyDescriptor _ecl = new PropertyDescriptor("ecl", beanClass, "getEcl", null);
            _ecl.setDisplayName("ECL");
            _ecl.setShortDescription("ECL");
            PropertyDescriptor[] pds = new PropertyDescriptor[] { _cohort, _experience, _included, _level, _levelAdjustment, _name, _ecl };
            return pds;
        } catch (IntrospectionException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public java.awt.Image getIcon(int iconKind) {
        switch(iconKind) {
            case BeanInfo.ICON_COLOR_16x16:
                return iconColor16x16Filename != null ? loadImage(iconColor16x16Filename) : null;
            case BeanInfo.ICON_COLOR_32x32:
                return iconColor32x32Filename != null ? loadImage(iconColor32x32Filename) : null;
            case BeanInfo.ICON_MONO_16x16:
                return iconMono16x16Filename != null ? loadImage(iconMono16x16Filename) : null;
            case BeanInfo.ICON_MONO_32x32:
                return iconMono32x32Filename != null ? loadImage(iconMono32x32Filename) : null;
        }
        return null;
    }
}
