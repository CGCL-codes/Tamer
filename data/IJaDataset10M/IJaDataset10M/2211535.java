package cn.ekuma.impexp;

import cn.ekuma.impexp.model.AbstractImpExpTableDefinition;

/**
 *
 * @author Administrator
 */
public interface FieldGenerator<T> {

    T getNext();
}
