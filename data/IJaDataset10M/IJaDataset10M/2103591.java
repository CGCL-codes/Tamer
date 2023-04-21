package org.seasar.webhelpers.validator.reader.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.SerializationUtils;
import org.seasar.framework.util.AssertionUtil;
import org.seasar.webhelpers.validator.bean.ValidatorDef;
import org.seasar.webhelpers.validator.reader.ValidatorDefResolver;

/**
 * JSFのバリデータを生成するクラスです。
 * 
 * @author takanori
 * 
 */
public class JsfValidatorDefResolver implements ValidatorDefResolver {

    /** バリデータを保持するマップ */
    private Map validatorMap = new HashMap();

    /**
     * インスタンスを生成します。
     */
    public JsfValidatorDefResolver() {
    }

    /**
     * {@inheritDoc}
     * 
     * このメソッドでは、呼び出される度に新しいインスタンスを生成します。
     */
    public ValidatorDef resolve(String validatorId) {
        ValidatorDef validatorDef = (ValidatorDef) this.validatorMap.get(validatorId);
        ValidatorDef clone = (ValidatorDef) SerializationUtils.clone(validatorDef);
        return clone;
    }

    /**
     * バリデータのクラスをこのオブジェクトに追加します。
     * 
     * @param name バリデータの識別子
     * @param clazz バリデータクラス 
     */
    public void addValidator(final String name, final Class clazz) {
        addValidator(name, clazz, null);
    }

    /**
     * バリデータのクラスをこのオブジェクトに追加します。
     * 
     * @param name バリデータの識別子
     * @param clazz バリデータクラス 
     * @param propertyNames プロパティ名のリスト
     */
    public void addValidator(final String name, final Class clazz, final List propertyNameList) {
        AssertionUtil.assertNotEmpty("validator name", name);
        AssertionUtil.assertNotNull("validator class", clazz);
        String[] propertyNames;
        if (propertyNameList != null) {
            propertyNames = (String[]) propertyNameList.toArray(new String[propertyNameList.size()]);
        } else {
            propertyNames = new String[0];
        }
        ValidatorDef validatorDef = new ValidatorDef(name, clazz, null, null, propertyNames);
        this.validatorMap.put(name, validatorDef);
    }
}
