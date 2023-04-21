package org.yafra.model;

import org.yafra.orm.YafraUser;

/**
 * Transform to/from database object to model object</br>
 * model object from database object from(database)
 * @see org.yafra.interfaces.IYafraMYafraUser
 * @version $Id: MYafraUserTransform.java,v 1.3 2009-12-12 18:49:20 mwn Exp $
 * @author <a href="mailto:martin.weber@yafra.org">Martin Weber</a>
 * @since 1.0
 */
public class MYafraUserTransform {

    /**
	 * copy from cayenne object to model object
	 * 
	 * @param YafraUser	a cayenne database object
	 * @return			a model object
	 * @since			1.0
	 */
    public MYafraUser from(YafraUser from) {
        MYafraUser to = new MYafraUser();
        to.setUserid(from.getUserid());
        to.setName(from.getName());
        to.setPicturelink(from.getPicturelink());
        return to;
    }

    /**
	 * copy from model object to cayenne object
	 * 
	 * @param MYafraRole	a model object
	 * @param YafraRole		a cayenne database object
	 * @since				1.0
	 */
    public void to(MYafraUser from, YafraUser to) {
        to.setUserid(from.getUserid());
        to.setName(from.getName());
        to.setPicturelink(from.getPicturelink());
    }
}
