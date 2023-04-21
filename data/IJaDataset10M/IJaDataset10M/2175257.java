package org.soa4all.lpml.bpel;

import java.io.Serializable;
import org.soa4all.lpml.impl.ProcessElementImpl;

/**
 * @author Adrian Mos
 *
 */
public class ExclusiveGatewayImpl extends ProcessElementImpl implements ExclusiveGateway, Serializable {

    @Override
    public boolean isSplit() {
        return false;
    }

    @Override
    public float getX() {
        return 0;
    }

    @Override
    public float getY() {
        return 0;
    }

    @Override
    public float getZ() {
        return 0;
    }

    @Override
    public void setX(float position) {
    }

    @Override
    public void setY(float position) {
    }

    @Override
    public void setZ(float position) {
    }
}
