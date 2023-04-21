package com.breachwalls.mogen.model;

/**
 * モデルのプロパティ.
 * @author todoken
 * @version $Revision: 31 $
 */
public interface ModelProperty {

    /**
     * プロパティ名を取得する.
     * @return プロパティ名
     */
    String getName();

    /**
     * 説明文を取得する.
     * @return 説明文
     */
    String getDescription();

    /**
     * プロパティの型を取得する.
     * @return プロパティの型
     */
    String getType();

    /**
     * 値を取得する.
     * @return 値
     */
    String getValue();

    /**
     * スペックを取得する.
     * @return スペック
     */
    String[] getSpecs();

    /**
     * モデルを取得する.
     * @return モデル
     */
    Model getModel();

    /**
     * モデルを設定する.
     * @param model モデル
     */
    void setModel(Model model);
}
