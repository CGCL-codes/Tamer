package edu.nwpu.oboe.base.aop.helper;

/**
 * 
 * @copyright Oboe Project 2009 
 * @author William
 * @Revision
 * @date 2009-6-27
 * @param <T>
 */
public class EntityNamedQuerySupport<T> implements QueryNameProvider {

    @SuppressWarnings("unchecked")
    public String getQueryName(String methodName, Class type) {
        return type.getSimpleName() + "." + methodName;
    }
}
