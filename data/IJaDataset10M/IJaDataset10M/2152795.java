package org.ncgr.cmtv.datamodel.impl;

import org.ncgr.isys.objectmodel.LinearObjectDistribution;
import org.ncgr.isys.objectmodel.LinearObjectPosition;
import java.util.*;

/**
 * This class simply adds a new type to LinearObjectDistribution to
 * distinguish between continuous and discontinuous distributions.
 */
public class ContinuousLinearObjectDistributionImpl extends LinearObjectDistributionImpl {

    /**
	 * The constructor of ContinuousLinearObjectDistributionImpl, simply calls
	 * the super class LinearObjectDistributionImpl. @see the super class for
	 * details.
	 * @param position a LinearObjectPosition object.
	 * @param distribution a <code>java.util.Map</code> object that contains the
			 underlying data.
	 * @param min a Number that defines the minimum coordinate of this distribution.
	 * @param max a Number that defined the maximum coordinate of this distribution.
	 * @param name a String representation of this distribution.
	 */
    ContinuousLinearObjectDistributionImpl(LinearObjectPosition position, Map distribution, Number min, Number max, String name, String type) {
        super(position, distribution, min, max, name, type);
    }
}
