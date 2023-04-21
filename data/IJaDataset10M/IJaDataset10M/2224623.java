package com.hk.svr;

import java.util.List;
import com.hk.bean.HkObjArticle;

public interface HkObjArticleService {

    void createHkObjArticle(HkObjArticle hkObjArticle);

    void updateHkObjArticle(HkObjArticle hkObjArticle);

    void deleteHkObjArticle(long articleId);

    List<HkObjArticle> getHkObjArticleListByUserId(long userId, int begin, int size);

    /**
	 * 获取的数据都是审核通过的
	 * 
	 * @param oid 足迹或宝贝的id
	 * @param begin
	 * @param size
	 * @return
	 */
    List<HkObjArticle> getHkObjArticleListByOid(long oid, int begin, int size);

    /**
	 * 根据审核状况过滤数据显示
	 * 
	 * @param checkflg
	 * @param begin
	 * @param size
	 * @return
	 */
    List<HkObjArticle> getHkObjArticleList(byte checkflg, int begin, int size);

    HkObjArticle getHkObjArticle(long articleId);

    void checkHkObjArticle(long articleId, byte checkflg);

    HkObjArticle getUserLastHkObjArticle(long userId);
}
