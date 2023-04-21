package org.i0o.utilplus.populator.converters;

/**
 * Float 转换类
 * 
 * @author <a href="mailto:781131@gmail.com">HTF</a>
 * @version 2010-6-1
 */
public final class FloatConverter extends NumberConverter {

    @Override
    protected Class<?> getDefaultType() {
        return Float.class;
    }
}
