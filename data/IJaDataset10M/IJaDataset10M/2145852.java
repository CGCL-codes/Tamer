package net.shopxx.service;

import java.util.List;
import net.shopxx.entity.MemberAttribute;

/**
 * Service接口 - 会员属性
 * ============================================================================
 * 版权所有 2008-2010 长沙鼎诚软件有限公司，并保留所有权利。
 * ----------------------------------------------------------------------------
 * 提示：在未取得SHOP++商业授权之前，您不能将本软件应用于商业用途，否则SHOP++将保留追究的权力。
 * ----------------------------------------------------------------------------
 * 官方网站：http://www.shopxx.net
 * ----------------------------------------------------------------------------
 * KEY: SHOPXX249F14A09332DCE8ED60265E3F523A3A
 * ============================================================================
 */
public interface MemberAttributeService extends BaseService<MemberAttribute, String> {

    /**
	 * 获取已启用的会员注册项.
	 * 
	 * @return 已启用的会员注册项集合.
	 */
    public List<MemberAttribute> getEnabledMemberAttributeList();
}
