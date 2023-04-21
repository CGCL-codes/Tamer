package com.tirsen.hanoi.engine;

import com.tirsen.hanoi.beans.DefaultDynamicProperties;
import com.tirsen.hanoi.beans.DynamicPropertyDescriptor;
import java.beans.*;

/**
 *
 *
 * <!-- $Id: DefaultDatasheet.java,v 1.5 2002/09/03 07:55:24 tirsen Exp $ -->
 * <!-- $Author: tirsen $ -->
 *
 * @author Jon Tirs&eacute;n (tirsen@users.sourceforge.net)
 * @version $Revision: 1.5 $
 */
public class DefaultDatasheet extends DefaultDynamicProperties implements Datasheet {

    static {
        try {
            BeanInfo info = Introspector.getBeanInfo(DefaultDatasheet.class);
            PropertyDescriptor[] descriptors = info.getPropertyDescriptors();
            for (int i = 0; i < descriptors.length; i++) {
                PropertyDescriptor descriptor = descriptors[i];
                if ("currentStepID".equals(descriptor.getName())) {
                    descriptor.setDisplayName("Current Step ID");
                    descriptor.setHidden(true);
                }
                if ("instanceID".equals(descriptor.getName())) {
                    descriptor.setDisplayName("Instance ID");
                    descriptor.setHidden(true);
                }
                if ("definitionID".equals(descriptor.getName())) {
                    descriptor.setDisplayName("Definition ID");
                    descriptor.setHidden(true);
                }
                if ("state".equals(descriptor.getName())) {
                    descriptor.setDisplayName("State");
                    descriptor.setHidden(true);
                }
                if ("entries".equals(descriptor.getName())) {
                    descriptor.setHidden(true);
                }
                if ("dynamicPropertyDescriptors".equals(descriptor.getName())) {
                    descriptor.setHidden(true);
                }
                if ("empty".equals(descriptor.getName())) {
                    descriptor.setHidden(true);
                }
            }
        } catch (IntrospectionException e) {
            assert false : e.getMessage();
        }
    }

    public static final String CURRENT_STEP_ID_PROPERTY = "_currentStepID";

    public static final String INSTANCE_ID_PROPERTY = "_instanceID";

    public static final String DEFINITION_ID_PROPERTY = "_definitionID";

    public static final String STATE_PROPERTY = "_state";

    public DefaultDatasheet() {
        try {
            DynamicPropertyDescriptor descriptor = new DynamicPropertyDescriptor(CURRENT_STEP_ID_PROPERTY, String.class);
            descriptor.setShortDescription("Stores the ID of the current step.");
            descriptor.setHidden(true);
            declareProperty(descriptor);
            descriptor = new DynamicPropertyDescriptor(INSTANCE_ID_PROPERTY, String.class);
            descriptor.setShortDescription("Stores the ID of the instance.");
            descriptor.setHidden(true);
            declareProperty(descriptor);
            descriptor = new DynamicPropertyDescriptor(DEFINITION_ID_PROPERTY, String.class);
            descriptor.setShortDescription("Stores the ID of the definition.");
            descriptor.setHidden(true);
            declareProperty(descriptor);
            descriptor = new DynamicPropertyDescriptor(STATE_PROPERTY, String.class);
            descriptor.setShortDescription("Stores the code of the current state.");
            descriptor.setHidden(true);
            descriptor.setPropertyType(Integer.class);
            declareProperty(descriptor);
        } catch (IntrospectionException e) {
            assert false : e.getMessage();
        }
    }

    public String getCurrentStepID() {
        return (String) get(CURRENT_STEP_ID_PROPERTY);
    }

    public void setCurrentStepID(String id) {
        set(CURRENT_STEP_ID_PROPERTY, id);
    }

    public String getInstanceID() {
        return (String) get(INSTANCE_ID_PROPERTY);
    }

    public void setInstanceID(String id) {
        set(INSTANCE_ID_PROPERTY, id);
    }

    public String getDefinitionID() {
        return (String) get(DEFINITION_ID_PROPERTY);
    }

    public void setDefinitionID(String id) {
        set(DEFINITION_ID_PROPERTY, id);
    }

    public int getState() {
        Integer integer = (Integer) get(STATE_PROPERTY);
        return integer == null ? 0 : integer.intValue();
    }

    public void setState(int state) {
        set(STATE_PROPERTY, new Integer(state));
    }
}
