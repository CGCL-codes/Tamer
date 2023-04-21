package org.t2framework.t2.action.internal;

import org.t2framework.commons.meta.MethodDesc;
import org.t2framework.commons.util.StringUtil;
import org.t2framework.t2.action.ActionContext;
import org.t2framework.t2.annotation.core.Ajax;
import org.t2framework.t2.contexts.Request;

/**
 * <#if locale="en">
 * <p>
 * ActionUtil handles T2-specific things.This utility class is absolutely
 * internal class.
 * </p>
 * <#else>
 * <p>
 * ActionUtilはT2独自の処理を行います.このユーティリティクラスは、T2の内部だけで利用されるクラスです.
 * </p>
 * </#if>
 * 
 * @author shot
 */
public class ActionUtil {

    /**
	 * <#if locale="en">
	 * <p>
	 * Get array position found by the given key.If there is no position for the
	 * key, throw runtime exception.
	 * </p>
	 * <#else>
	 * <p>
	 * 引数のキーが、配列の何番目に該当するかを返します.キーが見つからない場合ランタイム例外が発生します.
	 * </p>
	 * </#if>
	 * 
	 * @param src
	 *            <#if locale="en">
	 *            <p>
	 *            array formatted string like hoge[a][b]
	 *            </p>
	 *            <#else>
	 *            <p>
	 *            hoge[a][b]のような配列形式
	 *            </p>
	 *            </#if>
	 * @param key
	 *            <#if locale="en">
	 *            <p>
	 *            key of finding position
	 *            </p>
	 *            <#else>
	 *            <p>
	 *            ポジションを探すキー文字列
	 *            </p>
	 *            </#if>
	 * @return <#if locale="en">
	 *         <p>
	 *         position of key
	 *         </p>
	 *         <#else>
	 *         <p>
	 *         キーが見つかったポジション
	 *         </p>
	 *         </#if>
	 */
    public static int getArrayPostition(String src, String key) {
        if (StringUtil.isEmpty(src)) {
            throw new RuntimeException();
        }
        int pos = src.indexOf("[");
        if (pos < 0) {
            throw new RuntimeException();
        }
        src = src.substring(pos);
        final int index = src.indexOf(key);
        if (index < 0) {
            throw new RuntimeException();
        }
        String s = src.substring(0, index);
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '[') {
                count++;
            }
        }
        return count;
    }

    /**
	 * <#if locale="en">
	 * <p>
	 * Get array prefix key. e.g. hoge[a] -> hoge
	 * </p>
	 * <#else>
	 * <p>
	 * 配列のプレフィックスのキー部分を返します. 例) hoge[a] -> hoge
	 * </p>
	 * </#if>
	 * 
	 * @param src
	 * @return prefix for this array variable
	 */
    public static String getPrefixKey(String src) {
        int pos = src.indexOf("[");
        if (pos < 0) {
            return src;
        }
        return src.substring(0, pos);
    }

    /**
	 * <#if locale="en">
	 * <p>
	 * True if this is ajax request.
	 * </p>
	 * <#else>
	 * <p>
	 * Ajaxリクエストの場合、trueを返します.
	 * </p>
	 * </#if>
	 * 
	 * @param actionContext
	 * @param targetMethodDesc
	 * @return true it this request is ajax type, otherwise false
	 */
    public static boolean isAjaxRequest(final ActionContext actionContext, final MethodDesc targetMethodDesc) {
        if (targetMethodDesc.hasConfig(Ajax.class)) {
            return false;
        }
        final Request request = actionContext.getRequest();
        return request.isAjaxRequest();
    }
}
