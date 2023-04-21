package org.soybeanMilk.core.bean;

import java.lang.reflect.Type;

/**
 * 转换器，它可以将某个类型的对象转换为另一个类型的对象，比如将字符串转换成整型值
 * @author earthAngry@gmail.com
 * @date 2010-10-5
 */
public interface Converter {

    /**
	 * 将源对象转换为目标类型的对象
	 * @param sourceObj 源对象
	 * @param targetType 目标类型，比如<code>Integer.class</code>、<code>Date.class</code>等，也可能是<code>null</code>
	 * @return 目标类型的对象
	 * @throws ConvertException
	 */
    Object convert(Object sourceObj, Type targetType) throws ConvertException;
}
