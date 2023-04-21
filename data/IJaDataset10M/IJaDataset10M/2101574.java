package com.centraview.common.source;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

/** 
 * @author 
 */
public interface SourceLocalHome extends EJBLocalHome {

    public SourceLocal create(SourceVO svo, String ds) throws CreateException;

    public SourceLocal findByPrimaryKey(SourcePK primaryKey) throws FinderException;
}
