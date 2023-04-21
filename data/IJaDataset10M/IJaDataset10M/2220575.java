package org.ode4j.ode.internal;

import org.cpp4j.java.ObjArray;
import org.ode4j.ode.DColliderFn;
import org.ode4j.ode.DContactGeom;
import org.ode4j.ode.DContactGeomBuffer;
import org.ode4j.ode.DGeom;
import org.ode4j.ode.internal.gimpact.GimContact;
import org.ode4j.ode.internal.gimpact.GimDynArray;

public class CollideTrimeshTrimesh implements DColliderFn {

    @Override
    public int dColliderFn(DGeom o1, DGeom o2, int flags, DContactGeomBuffer contacts) {
        return dCollideTTL((DxGimpact) o1, (DxGimpact) o2, flags, contacts, 1);
    }

    int dCollideTTL(DxGimpact g1, DxGimpact g2, int Flags, DContactGeomBuffer Contacts, int Stride) {
        Common.dIASSERT(Stride == 1);
        Common.dIASSERT((Flags & DxGeom.NUMC_MASK) >= 1);
        DxGimpact TriMesh1 = g1;
        DxGimpact TriMesh2 = g2;
        GimDynArray<GimContact> trimeshcontacts;
        trimeshcontacts = GimContact.GIM_CREATE_CONTACT_LIST();
        g1.recomputeAABB();
        g2.recomputeAABB();
        TriMesh1.m_collision_trimesh.gim_trimesh_trimesh_collision(TriMesh2.m_collision_trimesh, trimeshcontacts);
        if (trimeshcontacts.size() == 0) {
            trimeshcontacts.GIM_DYNARRAY_DESTROY();
            return 0;
        }
        ObjArray<GimContact> ptrimeshcontacts = trimeshcontacts.GIM_DYNARRAY_POINTER_V();
        int contactcount = trimeshcontacts.size();
        int maxcontacts = (int) (Flags & DxGeom.NUMC_MASK);
        if (contactcount > maxcontacts) {
            contactcount = maxcontacts;
        }
        DContactGeom pcontact;
        GimContact ptrimeshcontact;
        for (int i = 0; i < contactcount; i++) {
            pcontact = Contacts.getSafe(Flags, i);
            ptrimeshcontact = ptrimeshcontacts.at(i);
            pcontact.pos.set(ptrimeshcontact.getPoint().f);
            pcontact.normal.set(ptrimeshcontact.getNormal().f);
            pcontact.depth = ptrimeshcontact.getDepth();
            pcontact.g1 = g1;
            pcontact.g2 = g2;
            pcontact.side1 = ptrimeshcontact.getFeature1();
            pcontact.side2 = ptrimeshcontact.getFeature2();
        }
        trimeshcontacts.GIM_DYNARRAY_DESTROY();
        return contactcount;
    }
}
