package net.sourceforge.javautil.common.visitor;

/**
 * This represents the common simple single context visitor.
 *
 * @author elponderador
 * @author $Author: ponderator $
 * @version $Id: IVisitorSimple.java 2198 2010-05-31 19:24:17Z ponderator $
 */
public interface IVisitorSimple<C extends IVisitorContext> {

    /**
	 * @param ctx The context in which to execute this visit operation
	 */
    void visit(C ctx);
}
