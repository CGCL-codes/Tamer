package com.hy.enterprise.framework.lang.pair;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * <ul>
 * <li>设计作者：刘川</li>
 * <li>设计日期：2009-8-4</li>
 * <li>设计时间：下午06:03:37</li>
 * <li>设计目的：以字符串为键值类型的键值对的抽象实现类</li>
 * </ul>
 * <ul>
 * <b>修订历史</b>
 * <li>1、</li>
 * </ul>
 */
public abstract class AbstractStringValuePair<Value> implements KeyValuePairable<String, Value> {

    private static final long serialVersionUID = 1637521549996057283L;

    /**
	 * 键值对的键
	 */
    private String key;

    /**
	 * 键值对的值
	 */
    private Value value;

    /**
	 * 构造函数
	 */
    public AbstractStringValuePair() {
        super();
    }

    /**
	 * 构造函数
	 * 
	 * @param key
	 * @param value
	 */
    public AbstractStringValuePair(String key, Value value) {
        super();
        this.key = key;
        this.value = value;
    }

    /**
	 * @see com.vsoft.libra.enterprise.framework.lang.pair.KeyValuePairable#getKey()
	 */
    @Override
    public String getKey() {
        return this.key;
    }

    /**
	 * @see com.vsoft.libra.enterprise.framework.lang.pair.KeyValuePairable#getValue()
	 */
    @Override
    public Value getValue() {
        return this.value;
    }

    /**
	 * @see com.vsoft.libra.enterprise.framework.lang.pair.KeyValuePairable#setKey(com.vsoft.libra.enterprise.framework.lang.identifier.Identifierable)
	 */
    @Override
    public void setKey(String key) {
        this.key = key;
    }

    /**
	 * @see com.vsoft.libra.enterprise.framework.lang.pair.KeyValuePairable#setValue(java.lang.Object)
	 */
    @Override
    public void setValue(Value value) {
        this.value = value;
    }

    /**
	 * @see java.lang.Object#toString()
	 */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
