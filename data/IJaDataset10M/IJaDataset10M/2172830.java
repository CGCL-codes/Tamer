package com.hk.svr;

import java.util.List;
import com.hk.bean.CmpHomePicAd;

/**
 * 企业网站首页焦点图，最多只能有4个
 * 
 * @author akwei
 */
public interface CmpHomePicAdService {

    void createCmpHomePicAd(CmpHomePicAd cmpHomePicAd);

    void updateCmpHomePicAd(CmpHomePicAd cmpHomePicAd);

    void deleteCmpHomePicAd(long adid);

    /**
	 * 只取4个
	 * 
	 * @param companyId
	 * @return
	 *         2010-6-13
	 */
    List<CmpHomePicAd> getCmpHomePicAdListByCompanyId(long companyId, int begin, int size);

    CmpHomePicAd getCmpHomePicAd(long adid);
}
