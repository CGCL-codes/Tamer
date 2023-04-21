package com.ss4o.core.component;

/**
 * チェック用部品
 * 
 * @author kakusuke
 * 
 * @param <ValueObject>
 *            ValueObjectクラス
 */
public interface CheckComponent<ValueObject> {

    /**
	 * チェック処理を実行します。
	 * 
	 * @param values
	 *            チェック対象ValueObject
	 */
    void doCheck(ValueObject... values);
}
