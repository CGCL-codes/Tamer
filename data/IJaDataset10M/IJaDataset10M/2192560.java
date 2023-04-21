package net.paoding.rose.web.impl.validation;

import net.paoding.rose.web.Invocation;
import org.springframework.util.Assert;
import org.springframework.validation.AbstractBindingResult;

/**
 * 控制器action方法普通参数绑定信息类，
 * 
 * @author 王志亮 [qieqie.wang@gmail.com]
 * 
 */
public class ParameterBindingResult extends AbstractBindingResult {

    public static final String OBJECT_NAME = "parameterBindingResult";

    private static final long serialVersionUID = -592629554361073051L;

    private transient Invocation inv;

    /**
     * 
     * @param inv
     */
    public ParameterBindingResult(Invocation inv) {
        super(OBJECT_NAME);
        Assert.notNull(inv, "Target Invocation must not be null");
        this.inv = inv;
    }

    @Override
    public Object getTarget() {
        return this.inv;
    }

    /**
     * 
     * @throws IllegalStateException 在反序列化后调用
     */
    @Override
    protected Object getActualFieldValue(String field) {
        if (inv == null) {
            throw new IllegalStateException();
        }
        return inv.getParameter(field);
    }
}
