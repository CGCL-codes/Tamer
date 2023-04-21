package com.wizzer.m3g.viewer.ui.property;

import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import com.wizzer.m3g.Object3D;
import com.wizzer.m3g.AnimationTrack;

/**
 * This class is a Property Source for a M3G Object3D.
 * 
 * @author Mark Millard
 */
public class Object3DPropertySource implements IPropertySource {

    private static final String PROPERTY_USER_IDENTIFIER = "com.wizzer.m3g.viewer.ui.object3d.useridentifier";

    private static final String PROPERTY_USER_PARAMETER = "com.wizzer.m3g.viewer.ui.object3d.userparameter";

    private static final String PROPERTY_ANIMATION_TRACK = "com.wizzer.m3g.viewer.ui.object3d.animationtrack";

    private Object3D m_object;

    private IPropertyDescriptor[] m_descriptors = null;

    private UserParameterPropertySource[] m_userParameterPropertySources = null;

    private AnimationTrackPropertySource[] m_animationTrackPropertySources = null;

    private Object3DPropertySource() {
    }

    /**
	 * Create a new property source.
	 * 
	 * @param object The associated M3G Object3D for this property.
	 */
    public Object3DPropertySource(Object3D object) {
        m_object = object;
        Hashtable userParams = (Hashtable) m_object.getUserObject();
        if (userParams.size() > 0) {
            int i = 0;
            m_userParameterPropertySources = new UserParameterPropertySource[userParams.size()];
            for (Enumeration e = userParams.keys(); e.hasMoreElements(); i++) {
                Integer id = (Integer) e.nextElement();
                byte[] value = (byte[]) userParams.get(id);
                m_userParameterPropertySources[i] = new UserParameterPropertySource(id.longValue(), value);
            }
        }
        int numTracks = m_object.getAnimationTrackCount();
        if (numTracks > 0) {
            m_animationTrackPropertySources = new AnimationTrackPropertySource[numTracks];
            for (int i = 0; i < numTracks; i++) {
                AnimationTrack track = m_object.getAnimationTrack(i);
                m_animationTrackPropertySources[i] = new AnimationTrackPropertySource(track);
            }
        }
    }

    /**
	 * Get the associated Object3D data.
	 * 
	 * @return A <code>Object3D</code> is returned.
	 */
    public Object3D getHeader() {
        return m_object;
    }

    public Object getEditableValue() {
        return this;
    }

    public IPropertyDescriptor[] getPropertyDescriptors() {
        if (m_descriptors == null) {
            Vector<PropertyDescriptor> descriptors = new Vector<PropertyDescriptor>();
            PropertyDescriptor userIdDesc = new PropertyDescriptor(PROPERTY_USER_IDENTIFIER, "User Identifier");
            descriptors.add(userIdDesc);
            Hashtable userParameters = (Hashtable) m_object.getUserObject();
            int count = userParameters.size();
            for (int i = 0; i < count; i++) {
                String id = new String(PROPERTY_USER_PARAMETER + ":" + i);
                PropertyDescriptor userParamDescr = new UserParameterPropertyDescriptor(id, "User Parameter");
                descriptors.add(userParamDescr);
            }
            count = m_object.getAnimationTrackCount();
            for (int i = 0; i < count; i++) {
                String id = new String(PROPERTY_ANIMATION_TRACK + ":" + i);
                PropertyDescriptor animationTrackDescr = new AnimationTrackPropertyDescriptor(id, "AnimationTrack Properties");
                descriptors.add(animationTrackDescr);
            }
            Object[] objs = descriptors.toArray();
            m_descriptors = new IPropertyDescriptor[objs.length];
            for (int i = 0; i < m_descriptors.length; i++) {
                m_descriptors[i] = (IPropertyDescriptor) objs[i];
            }
        }
        return m_descriptors;
    }

    public Object getPropertyValue(Object id) {
        if (id.equals(PROPERTY_USER_IDENTIFIER)) {
            return m_object.getUserID();
        } else {
            String str = (String) id;
            int index = str.indexOf(":");
            if (index != -1) {
                String key = str.substring(index + 1);
                Integer keyValue = Integer.valueOf(key);
                String property = str.substring(0, index);
                if (property.equals(PROPERTY_USER_PARAMETER)) return m_userParameterPropertySources[keyValue.intValue()]; else if (property.equals(PROPERTY_ANIMATION_TRACK)) return m_animationTrackPropertySources[keyValue.intValue()];
            }
        }
        return null;
    }

    public boolean isPropertySet(Object id) {
        if (PROPERTY_USER_IDENTIFIER.equals(id)) {
            return true;
        } else {
            String str = (String) id;
            int index = str.indexOf(":");
            if (index != -1) {
                return true;
            }
        }
        return false;
    }

    public void resetPropertyValue(Object id) {
    }

    public void setPropertyValue(Object id, Object value) {
    }
}
