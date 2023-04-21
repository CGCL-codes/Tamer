package org.wdcode.shop.helper;

import org.wdcode.base.helper.BaseEntityCacheHelper;
import org.wdcode.shop.cache.EssaySortCache;
import org.wdcode.shop.po.EssaySort;

/**
 * 文章分类分类相关业务助手类 可操作缓存
 * @author WD
 * @since JDK6
 * @version 1.0 2009-11-23
 */
public final class EssaySortHelper extends BaseEntityCacheHelper<EssaySortCache, EssaySort> {

    private static EssaySortHelper helper;

    /**
	 * 实例化一个对象
	 */
    public EssaySort newInstance() {
        return new EssaySort();
    }

    /**
	 * 获得子类的Class
	 * @return 子类的Class
	 */
    public Class<EssaySort> getEntityClass() {
        return EssaySort.class;
    }

    /**
	 * 获得文章分类助手
	 * @return 文章分类助手
	 */
    public static EssaySortHelper getHelper() {
        return helper;
    }

    /**
	 * 设置文章分类助手
	 * @param helper 文章分类助手
	 */
    public void setHelper(EssaySortHelper helper) {
        EssaySortHelper.helper = helper;
    }
}
