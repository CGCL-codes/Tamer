package net.shopxx.dao;

import java.util.List;
import net.shopxx.entity.FriendLink;

/**
 * Dao接口 - 友情链接
 * ============================================================================
 * 版权所有 2008-2010 长沙鼎诚软件有限公司，并保留所有权利。
 * ----------------------------------------------------------------------------
 * 提示：在未取得SHOP++商业授权之前，您不能将本软件应用于商业用途，否则SHOP++将保留追究的权力。
 * ----------------------------------------------------------------------------
 * 官方网站：http://www.shopxx.net
 * ----------------------------------------------------------------------------
 * KEY: SHOPXXC3D6CEBD9415FBD14A8F32042E622780
 * ============================================================================
 */
public interface FriendLinkDao extends BaseDao<FriendLink, String> {

    /**
	 * 获取所有图片友情链接集合;
	 * 
	 * @return 图片友情链接集合
	 * 
	 */
    public List<FriendLink> getPictureFriendLinkList();

    /**
	 * 获取所有文字友情链接集合;
	 * 
	 * @return 图片友情链接集合
	 * 
	 */
    public List<FriendLink> getTextFriendLinkList();
}
