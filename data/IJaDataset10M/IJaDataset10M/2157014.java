package de.grogra.ray.light;

import de.grogra.ray.intersection.IntersectionDescription;
import de.grogra.ray.util.Ray;

public class NoShadows implements ShadowProcessor {

    private float m_scalar;

    public boolean shadowRay(Ray light, float length, Ray view, IntersectionDescription desc) {
        if (desc.getRTObject().isSolid()) {
            m_scalar = desc.getNormal().x * light.getDirection().x + desc.getNormal().y * light.getDirection().y + desc.getNormal().z * light.getDirection().z;
            if (m_scalar > 0.0f) {
                return true;
            }
        } else {
            m_scalar = view.getDirection().x * light.getDirection().x + view.getDirection().y * light.getDirection().y + view.getDirection().z * light.getDirection().z;
            if (m_scalar < 0.0f) {
                return true;
            }
        }
        return false;
    }
}
