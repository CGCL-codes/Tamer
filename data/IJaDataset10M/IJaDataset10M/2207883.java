package org.t2framework.oneclick.service;

import org.t2framework.oneclick.model.User;

public interface UserService {

    /**
     * 新規にユーザを登録します。
     * <p>Userオブジェクトのハッシュ以外の値を埋めてこのメソッドに与えてください。
     * </p>
     * 
     * @param user Userオブジェクト。nullを指定してはいけません。
     * @return ユーザに対応するハッシュ。
     */
    String registerUser(User user);

    /**
     * ハッシュに対応するユーザをデータベースから検索して返します。
     * 
     * @param hash ハッシュ。nullを指定してはいけません。
     * @return Userオブジェクト。見つからなかった場合はnullを返します。
     */
    User getUser(String hash);

    /**
     * 既存のユーザの情報を変更します。
     * <p>Userオブジェクトのハッシュの値は埋められている必要があります。
     * </p>
     * 
     * @param user Userオブジェクト。nullを指定してはいけません。
     */
    void modifyUser(User user);
}
