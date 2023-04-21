package ch.superj.jjtree.test;

import ch.superj.core.IBeans;
import ch.superj.jjtree.compiler.SJCompiler;

/**
 * @author superj
 *
 * tag: 
 */
public class IBeansTest implements IBeans {

    private SJCompiler _compiler;

    public void setCompiler(SJCompiler compiler) {
        _compiler = compiler;
    }

    public Object getBean(String beanName) {
        if (beanName.equals("compiler")) return _compiler;
        return null;
    }
}
