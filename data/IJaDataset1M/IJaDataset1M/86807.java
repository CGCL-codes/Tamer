package com.nullfish.lib.vfs.impl.smb;

import com.nullfish.lib.vfs.Permission;
import com.nullfish.lib.vfs.permission.ClassFileAccess;
import com.nullfish.lib.vfs.permission.FileAccess;
import com.nullfish.lib.vfs.permission.PermissionType;

/**
 * 
 * @author Shunji Yamaura
 */
public class SMBPermission implements Permission {

    private static final PermissionType[] keys = { PermissionType.READABLE, PermissionType.WRITABLE };

    /**
	 * アクセス種別
	 */
    private static final FileAccess[] access = { ClassFileAccess.ALL };

    /**
	 * 読み込み可能
	 */
    private boolean readable;

    /**
	 * 書き込み可能
	 */
    private boolean writable;

    /**
	 * パーミッション属性名を取得する。
	 * @see com.nullfish.lib.vfs.Permission#getKeys()
	 */
    public PermissionType[] getTypes() {
        return keys;
    }

    /**
	 * 属性値を取得する。
	 * @see com.nullfish.lib.vfs.Permission#getValue()
	 */
    public boolean hasPermission(PermissionType name, FileAccess access) {
        if (PermissionType.READABLE.equals(name)) {
            return readable;
        } else if (PermissionType.WRITABLE.equals(name)) {
            return writable;
        }
        return false;
    }

    /**
	 * 属性値をセットする。
	 * @see com.nullfish.lib.vfs.Permission#setValue(java.lang.String, java.lang.Object)
	 */
    public void setPermission(PermissionType name, FileAccess access, boolean value) {
        if (PermissionType.READABLE.equals(name)) {
            readable = value;
        } else if (PermissionType.WRITABLE.equals(name)) {
            writable = value;
        }
    }

    public String getOwner() {
        return null;
    }

    public void setOwner(String owner) {
    }

    /**
	 * グループを取得する
	 * @return
	 */
    public String getGroup() {
        return null;
    }

    /**
	 * グループをセットする。
	 * @param owner
	 */
    public void setGroup(String group) {
    }

    /**
	 * アクセス種別を取得する。
	 * @see com.nullfish.lib.vfs.Permission#getAccess()
	 */
    public FileAccess[] getAccess() {
        return access;
    }

    public void importPermission(Permission permission) {
        this.readable = permission.hasPermission(PermissionType.READABLE, ClassFileAccess.ALL);
        this.writable = permission.hasPermission(PermissionType.WRITABLE, ClassFileAccess.ALL);
    }

    /**
	 * パーミッションの文字列表現を取得する。
	 * @return
	 */
    public String getPermissionString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(readable ? "r" : "-");
        buffer.append(writable ? "w" : "-");
        return buffer.toString();
    }

    /**
	 * パーミッション文字列から初期化する。
	 *
	 */
    public void initFromString(String pemissionStr) {
    }

    /**
	 * パーミッションの編集可否を返す
	 * @param name
	 * @param access
	 * @return
	 */
    public boolean isEditable(PermissionType name, FileAccess access) {
        return true;
    }
}
