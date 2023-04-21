package net.sourceforge.javautil.groovy.builder.interceptor.objectfactory;

import net.sourceforge.javautil.groovy.builder.GroovyBuilder;

/**
 * This will allow the method of association from parent<->child to be handled independently.
 *
 * @author elponderador
 * @author $Author: ponderator $
 * @version $Id: INodeMapper.java 2321 2010-06-25 19:11:31Z ponderator $
 */
public interface INodeMapper {

    /**
	 * This method will be called from inside the {@link #handleInvokedMethod(GroovyBuilder, GroovyBuilderStack, MetaMethod, String, Object[])}
	 * method when {@link #isPassParentToConstructor()} returns false, in order for an alternative mechanism to
	 * associate the parent node to the child.
	 * 
	 * @param child The child node object.
	 * @param parent The parent node object (may be null if the child is a root node).
	 */
    void associate(GroovyBuilder builder, ObjectFactoryInterceptor ofi, Object child, Object parent);
}
