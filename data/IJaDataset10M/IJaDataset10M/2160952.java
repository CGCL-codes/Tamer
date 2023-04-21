package com.cell;

import java.io.ObjectStreamException;
import java.io.Serializable;
import com.cell.util.MarkedHashtable;

/**
 * @author WAZA
 * 提供了对序列化支持的对象
 */
public abstract class DObject implements Serializable {

    private static final long serialVersionUID = 1L;

    private transient MarkedHashtable data_group;

    protected DObject() {
        init_field();
        init_transient();
    }

    /**
	 * 初始化可序列化字段 <br>
	 * <font color="#ff0000"> 继承的子类不要忘记调用 super.init_field() </font>
	 */
    protected void init_field() {
    }

    /**
	 * 初始化不可序列化字段, 该方法将在构造函数和反序列化后调用 <br>
	 * <font color="#ff0000"> 继承的子类不要忘记调用 super.init_transient() </font>
	 */
    protected void init_transient() {
    }

    protected final Object writeReplace() throws ObjectStreamException {
        return this;
    }

    protected final Object readResolve() throws ObjectStreamException {
        init_transient();
        return this;
    }
}
