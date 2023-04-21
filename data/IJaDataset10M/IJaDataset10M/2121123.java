package nuts.exts.vfs.ndfs.dao;

import nuts.core.orm.dao.QueryParameter;
import nuts.core.orm.restriction.ComparableRestriction;

/**
 */
public interface NdfsDataExample extends QueryParameter {

    /**
	 * append AND
	 * @return this
	 */
    NdfsDataExample and();

    /**
	 * excludeALL
	 * @return this
	 */
    NdfsDataExample excludeALL();

    /**
	 * includeALL
	 * @return this
	 */
    NdfsDataExample includeALL();

    /**
	 * @return restriction of id
	 */
    ComparableRestriction<NdfsDataExample, Long> id();

    /**
	 * @return restriction of fileId
	 */
    ComparableRestriction<NdfsDataExample, Long> fileId();

    /**
	 * @return restriction of itemNo
	 */
    ComparableRestriction<NdfsDataExample, Integer> itemNo();
}
