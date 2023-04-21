package Absyn;

import java.io.Serializable;

/**
 * @author MaYunlei
 *
 */
public abstract class ConstValue extends Value implements Serializable {

    public abstract Object getValue();
}
