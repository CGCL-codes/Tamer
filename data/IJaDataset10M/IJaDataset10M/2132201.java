package com.hisham.powerpark.util;

import java.util.Collection;
import com.hisham.util.collections.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author Ali Hisham Malik
 * @version 1.0
 */
public class VehicleInfoList extends DynamicList<VehicleInfo> {

    /**
	 * 
	 */
    private static final long serialVersionUID = -7354860494837635054L;

    public VehicleInfoList() {
    }

    public VehicleInfoList(VehicleInfo[] p0) {
        super(p0);
    }

    public VehicleInfoList(Collection<VehicleInfo> p0) {
        super(p0);
    }

    public VehicleInfoList(int p0) {
        super(p0);
    }

    public VehicleInfo getNewElement() {
        return new VehicleInfo();
    }
}
