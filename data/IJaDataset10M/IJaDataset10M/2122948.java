package org.commonlibrary.lcms.sample.dao;

import org.commonlibrary.lcms.model.Sample;
import org.commonlibrary.lcms.support.dao.CrudDao;

/**
 * User: diegomunguia
 * Date: Jun 11, 2008
 * Time: 3:45:11 PM
 */
public interface SampleDao extends CrudDao<Sample, String> {

    Sample findByName(String name);
}
