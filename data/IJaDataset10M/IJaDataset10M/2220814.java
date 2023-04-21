package com.nullfish.lib.vfs.manipulation;

import com.nullfish.lib.vfs.Manipulation;
import com.nullfish.lib.vfs.permission.FileAccess;
import com.nullfish.lib.vfs.permission.PermissionType;

/**
 * パーミッションセット操作クラス
 * @author shunji
 */
public interface SetPermissionManipulation extends Manipulation {

    /**
	 * 操作名
	 */
    public static final String NAME = "set_permission";

    /**
	 * ファイル属性をセットする。
	 * @param type パーミッション種別
	 * @param access ユーザーアクセス種別
	 * @param value パーミッション値
	 */
    public abstract void addPermission(PermissionType type, FileAccess access, boolean value);
}
