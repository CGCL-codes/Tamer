package org.dozer.vo.enumtest;

/**
 * Bean for enum mapping test
 * @author cchou.hung
 *
 */
public class MyBeanPrime {

    private DestTypeWithOverride destTypeWithOverride;

    private DestType destType;

    public void setDestTypeWithOverride(DestTypeWithOverride destTypeWithOverride) {
        this.destTypeWithOverride = destTypeWithOverride;
    }

    public DestTypeWithOverride getDestTypeWithOverride() {
        return destTypeWithOverride;
    }

    public void setDestType(DestType destType) {
        this.destType = destType;
    }

    public DestType getDestType() {
        return destType;
    }
}
