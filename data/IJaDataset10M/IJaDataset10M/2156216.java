package com.selcukcihan.xfacej.xface;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;
import com.selcukcihan.xfacej.xengine.DeformableGeometry;
import com.selcukcihan.xfacej.xengine.Drawable;
import com.selcukcihan.xfacej.xengine.Entity;
import com.selcukcihan.xfacej.xengine.MeshManager;
import com.selcukcihan.xfacej.xengine.MorphChannel;
import com.selcukcihan.xfacej.xengine.Vector3Buffer;
import com.selcukcihan.xfacej.xmath.Vector3;

public class EyeChannel extends MorphChannel {

    public EyeChannel(final String name) {
        super(name);
    }

    protected boolean updateResult(Entity result, final Entity rest) {
        if ((result.getDrawableCount() == 0) || (m_result.getDrawableCount() == 0)) return false;
        MeshManager pMM = MeshManager.getInstance();
        final LinkedList<Drawable> restDr = rest.getDrawables();
        LinkedList<Drawable> resDr = result.getDrawables();
        final LinkedList<Drawable> eyeDr = m_result.getDrawables();
        Iterator<Drawable> it_rest = restDr.iterator();
        Iterator<Drawable> it_res = resDr.iterator();
        Iterator<Drawable> it_eye = eyeDr.iterator();
        while (it_res.hasNext()) {
            Drawable it_rest_object = it_rest.next();
            if ((!m_name.equals("BlinkEyes")) && (!it_rest_object.getBinding().equals("LeftEye")) && (!it_rest_object.getBinding().equals("RightEye"))) {
                it_res.next();
                it_eye.next();
                continue;
            }
            DeformableGeometry pRes = pMM.getMesh(it_rest_object.getMeshName());
            final DeformableGeometry pEye = pMM.getMesh(it_eye.next().getMeshName());
            final DeformableGeometry pRest = pMM.getMesh(it_rest.next().getMeshName());
            Vector3Buffer vRest = pRest.getVertices();
            Vector3Buffer vRes = pRes.getDeformedVerticesVector();
            Vector3Buffer vResNorm = pRes.getNormals();
            final Vector3Buffer vEye = pEye.getDeformedVerticesVector();
            final Vector3Buffer vEyeNorm = pEye.getNormals();
            for (int i = 0; i < pRes.getVertexCount(); i++) {
                Vector3 test = vRest.get(i).opSubtract(vEye.get(i));
                if (Math.abs(test.x) + Math.abs(test.y) + Math.abs(test.z) > 0.1f) {
                    vRes.put(i, new Vector3(vEye.get(i)));
                    vResNorm.put(i, new Vector3(vEyeNorm.get(i)));
                }
            }
        }
        return true;
    }
}
