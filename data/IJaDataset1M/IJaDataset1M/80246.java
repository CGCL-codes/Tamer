package org.tolk.ipico.process.node.impl;

import org.apache.log4j.Logger;
import org.tolk.ApplicationContextFactory;
import org.tolk.ipico.util.IpicoUtil;
import org.tolk.ipico.util.TagVo;

/**
 * The FsLsIrspDataFormatDecorator will produce a string in the IRSP format for
 * first- and lastSeen events of a tag.
 * 
 * @author Werner van Rensburg
 *
 */
public class FsLsIrspDataFormatDecorator implements FirstSeenLastSeenDataFormatDecorator {

    private IpicoUtil ipicoUtil;

    private final Logger logger = Logger.getLogger(FsLsIrspDataFormatDecorator.class);

    /**
	 * 
	 * @param tagVo
	 */
    public String getFormattedString(TagVo tagVo) {
        return null;
    }

    /**
     * 
     * 
     * @param tagVo
     */
    public String getFormattedString_FirstSeen(TagVo tagVo) {
        return getIpicoUtil().generateIpxString(tagVo.getReaderId(), tagVo.getUid(), tagVo.getI_sum(), tagVo.getQ_sum(), tagVo.getLastTime());
    }

    /**
     * 
     * 
     * @param tagVo
     */
    public String getFormattedString_LastSeen(TagVo tagVo) {
        return getIpicoUtil().generateIpxString(tagVo.getReaderId(), tagVo.getUid(), tagVo.getI_sum(), tagVo.getQ_sum(), tagVo.getLastTime());
    }

    private IpicoUtil getIpicoUtil() {
        if (ipicoUtil == null) {
            ipicoUtil = (IpicoUtil) ApplicationContextFactory.getBean(IpicoUtil.BEAN_NAME);
        }
        return ipicoUtil;
    }
}
