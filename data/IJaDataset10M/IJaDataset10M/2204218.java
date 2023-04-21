package com.google.code.burija.normal.service;

import com.google.code.burija.common.exception.BusinessException;

/**
 * ユーザ操作用サービス。
 * @author imaitakafumi
 *
 */
public interface NormalUserService {

    /**
	 * ログイン可能なユーザIDかどうかを判定する。
	 * @param loginId
	 */
    public boolean existsId(String loginId);

    /**
	 * ログイン審査をする。
	 * @param loginId
	 * @param loginPw
	 * @throws BusinessException
	 */
    public long login(String loginId, String loginPw) throws BusinessException;

    /**
	 * 新規ユーザの登録をする。
	 * @param loginId
	 * @param loginPw
	 * @param sysUserName
	 * @param gender
	 */
    public void regist(String loginId, String loginPw, String sysUserName, int gender);

    /**
	 * 既存ユーザを編集する。
	 * @param sysUserId
	 * @param loginId
	 * @param loginPw
	 * @param sysUserName
	 * @param gender
	 */
    public void edit(long sysUserId, String loginId, String loginPw, String sysUserName, int gender);
}
