package org.ode4j.ode.internal.gimpact;

import static org.ode4j.ode.internal.gimpact.GimGeometry.*;
import org.cpp4j.java.ObjArray;
import org.ode4j.ode.internal.gimpact.GimTriCollision.GIM_TRIANGLE_DATA;

/**
 * Ported to Java by Tilmann Zaeschke
 * @author Francisco Le�n
*/
public class GimTrimeshCapsuleCollision {

    /** Capsule struct. */
    public static class GIM_CAPSULE_DATA {

        public float m_radius;

        public final vec3f m_point1 = new vec3f();

        public final vec3f m_point2 = new vec3f();
    }

    ;

    private static void CALC_CAPSULE_AABB(GIM_CAPSULE_DATA capsule, aabb3f aabb) {
        if (capsule.m_point1.f[0] < capsule.m_point2.f[0]) {
            aabb.minX = capsule.m_point1.f[0] - capsule.m_radius;
            aabb.maxX = capsule.m_point2.f[0] + capsule.m_radius;
        } else {
            aabb.minX = capsule.m_point2.f[0] - capsule.m_radius;
            aabb.maxX = capsule.m_point1.f[0] + capsule.m_radius;
        }
        if (capsule.m_point1.f[1] < capsule.m_point2.f[1]) {
            aabb.minY = capsule.m_point1.f[1] - capsule.m_radius;
            aabb.maxY = capsule.m_point2.f[1] + capsule.m_radius;
        } else {
            aabb.minY = capsule.m_point2.f[1] - capsule.m_radius;
            aabb.maxY = capsule.m_point1.f[1] + capsule.m_radius;
        }
        if (capsule.m_point1.f[2] < capsule.m_point2.f[2]) {
            aabb.minZ = capsule.m_point1.f[2] - capsule.m_radius;
            aabb.maxZ = capsule.m_point2.f[2] + capsule.m_radius;
        } else {
            aabb.minZ = capsule.m_point2.f[2] - capsule.m_radius;
            aabb.maxZ = capsule.m_point1.f[2] + capsule.m_radius;
        }
    }

    static void gim_closest_point_triangle_segment(GIM_TRIANGLE_DATA triangle, vec3f s1, vec3f s2, GimDynArray<GimContact> contacts) {
        vec3f[] segment_points = { new vec3f(), new vec3f(), new vec3f(), new vec3f() };
        vec3f[] closest_points = { new vec3f(), new vec3f() };
        int intersection_type, out_edge = 10;
        float dis, dis_temp, perpend;
        vec4f sdiff = new vec4f();
        dis = DISTANCE_PLANE_POINT(triangle.m_planes.m_planes[0], s1);
        dis_temp = DISTANCE_PLANE_POINT(triangle.m_planes.m_planes[0], s2);
        if (dis <= 0.0f && dis_temp <= 0.0f) return;
        VEC_DIFF(sdiff, s2, s1);
        perpend = VEC_DOT(sdiff, triangle.m_planes.m_planes[0]);
        if (!IS_ZERO(perpend)) {
            if (dis < dis_temp) {
                VEC_COPY(closest_points[0], s1);
            } else {
                dis = dis_temp;
                VEC_COPY(closest_points[0], s2);
            }
            if (dis >= 0.0f && dis_temp >= 0.0f) {
                out_edge = POINT_IN_HULL_TZ(closest_points[0], triangle.m_planes.m_planes, 1, 3);
                if (out_edge == 0) {
                    GimContact.GIM_PUSH_CONTACT(contacts, closest_points[0], triangle.m_planes.m_planes[0], dis, null, null, 0, 0);
                    return;
                }
            } else {
                PLANE_CLIP_SEGMENT(s1, s2, triangle.m_planes.m_planes[0], closest_points[1]);
                out_edge = POINT_IN_HULL_TZ(closest_points[1], triangle.m_planes.m_planes, 1, 3);
                if (out_edge == 0) {
                    GimContact.GIM_PUSH_CONTACT(contacts, closest_points[0], triangle.m_planes.m_planes[0], dis, null, null, 0, 0);
                    return;
                }
            }
        } else {
            intersection_type = PLANE_CLIP_SEGMENT_CLOSEST(s1, s2, triangle.m_planes.m_planes[1], segment_points[0], segment_points[1]);
            if (intersection_type == 0 || intersection_type == 1) {
                out_edge = 0;
                VEC_COPY(closest_points[0], segment_points[0]);
            } else {
                intersection_type = PLANE_CLIP_SEGMENT_CLOSEST(segment_points[0], segment_points[1], triangle.m_planes.m_planes[2], segment_points[2], segment_points[3]);
                if (intersection_type == 0 || intersection_type == 1) {
                    out_edge = 1;
                    VEC_COPY(closest_points[0], segment_points[3]);
                } else {
                    intersection_type = PLANE_CLIP_SEGMENT_CLOSEST(segment_points[2], segment_points[3], triangle.m_planes.m_planes[3], closest_points[0], closest_points[1]);
                    if (intersection_type == 0 || intersection_type == 1) {
                        out_edge = 2;
                    }
                }
            }
            if (out_edge > 2) {
                dis = VEC_DOT(closest_points[0], triangle.m_planes.m_planes[0]);
                GimContact.GIM_PUSH_CONTACT(contacts, closest_points[0], triangle.m_planes.m_planes[0], dis, null, null, 0, 0);
                GimContact.GIM_PUSH_CONTACT(contacts, closest_points[1], triangle.m_planes.m_planes[0], dis, null, null, 0, 0);
                return;
            }
        }
        out_edge = 10;
        dis = G_REAL_INFINITY;
        int i;
        for (i = 0; i < 3; i++) {
            SEGMENT_COLLISION(s1, s2, triangle.m_vertices[i], triangle.m_vertices[(i + 1) % 3], segment_points[0], segment_points[1]);
            VEC_DIFF(sdiff, segment_points[0], segment_points[1]);
            dis_temp = VEC_DOT(sdiff, sdiff);
            if (dis_temp < dis) {
                dis = dis_temp;
                out_edge = i;
                VEC_COPY(closest_points[0], segment_points[0]);
                VEC_COPY(closest_points[1], sdiff);
            }
        }
        if (out_edge > 2) return;
        if (IS_ZERO(dis)) {
            GimContact.GIM_PUSH_CONTACT(contacts, closest_points[0], triangle.m_planes.m_planes[0], 0.0f, null, null, 0, 0);
        } else {
            dis = GIM_SQRT(dis);
            VEC_SCALE(closest_points[1], (1.0f / dis), closest_points[1]);
            GimContact.GIM_PUSH_CONTACT(contacts, closest_points[0], closest_points[1], dis, null, null, 0, 0);
        }
    }

    static int gim_triangle_capsule_collision(GIM_TRIANGLE_DATA triangle, GIM_CAPSULE_DATA capsule, GimDynArray<GimContact> contacts) {
        int old_contact_size = contacts.size();
        gim_closest_point_triangle_segment(triangle, capsule.m_point1, capsule.m_point2, contacts);
        if (contacts.size() == old_contact_size) {
            return 0;
        }
        ObjArray<GimContact> pcontacts = contacts.GIM_DYNARRAY_POINTER_V();
        pcontacts.inc(old_contact_size);
        if (pcontacts.at0().m_depth > capsule.m_radius) {
            contacts.m_size = old_contact_size;
            return 0;
        }
        vec3f vec = new vec3f();
        while (old_contact_size < contacts.size()) {
            GimContact pcontact = pcontacts.at0();
            VEC_SCALE(pcontact.m_normal, -1.0f, pcontact.m_normal);
            VEC_SCALE(vec, capsule.m_radius, pcontact.m_normal);
            VEC_SUM(pcontact.m_point, vec, pcontact.m_point);
            pcontact.m_depth = capsule.m_radius - pcontact.m_depth;
            pcontacts.inc();
            old_contact_size++;
        }
        return 1;
    }

    /**
	 * Trimesh Capsule collision.
	 * Find the closest primitive collided by the ray.
	 * @param trimesh
	 * @param capsule
	 * @param contact
	 * @param contacts A GIM_CONTACT array. Must be initialized
	 */
    static void gim_trimesh_capsule_collision(GimTrimesh trimesh, GIM_CAPSULE_DATA capsule, GimDynArray<GimContact> contacts) {
        contacts.m_size = 0;
        aabb3f test_aabb = new aabb3f();
        CALC_CAPSULE_AABB(capsule, test_aabb);
        GimDynArrayInt collision_result = GimDynArrayInt.GIM_CREATE_BOXQUERY_LIST();
        trimesh.m_aabbset.gim_aabbset_box_collision(test_aabb, collision_result);
        if (collision_result.size() == 0) {
            collision_result.GIM_DYNARRAY_DESTROY();
        }
        trimesh.gim_trimesh_locks_work_data();
        GimDynArray<GimContact> dummycontacts = GimContact.GIM_CREATE_CONTACT_LIST();
        int cresult;
        int i;
        int[] boxesresult = collision_result.GIM_DYNARRAY_POINTER();
        GIM_TRIANGLE_DATA tri_data = new GIM_TRIANGLE_DATA();
        int old_contact_size;
        ObjArray<GimContact> pcontacts;
        for (i = 0; i < collision_result.size(); i++) {
            old_contact_size = dummycontacts.size();
            trimesh.gim_trimesh_get_triangle_data(boxesresult[i], tri_data);
            cresult = gim_triangle_capsule_collision(tri_data, capsule, dummycontacts);
            if (cresult != 0) {
                pcontacts = dummycontacts.GIM_DYNARRAY_POINTER_V();
                pcontacts.inc(old_contact_size);
                while (old_contact_size < dummycontacts.size()) {
                    GimContact pcontact = pcontacts.at0();
                    pcontact.m_handle1 = trimesh;
                    pcontact.m_handle2 = capsule;
                    pcontact.m_feature1 = boxesresult[i];
                    pcontact.m_feature2 = 0;
                    pcontacts.inc();
                    old_contact_size++;
                }
            }
        }
        trimesh.gim_trimesh_unlocks_work_data();
        collision_result.GIM_DYNARRAY_DESTROY();
        GimContact.gim_merge_contacts(dummycontacts, contacts);
        dummycontacts.GIM_DYNARRAY_DESTROY();
    }
}
