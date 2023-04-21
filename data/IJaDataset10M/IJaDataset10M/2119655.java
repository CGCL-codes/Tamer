package org.zkoss.zk.ui.metainfo;

import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.metainfo.impl.ComponentDefinitionImpl;
import org.zkoss.zk.xel.ExValue;
import org.zkoss.zk.xel.impl.Utils;

/**
 * Represents the zk element in a ZUML page.
 *
 * @author tomyeh
 * @since 3.5.0
 */
public class ZkInfo extends ComponentInfo {

    static final ComponentDefinition ZK = new ComponentDefinitionImpl(null, null, "zk", Component.class);

    ;

    /** The swith condition. */
    private ExValue _switch;

    /** The case codition. */
    private ExValue[] _case;

    /** Constructs a ZK info.
	 */
    public ZkInfo(NodeInfo parent) {
        super(parent, ZK);
    }

    /** Returns whether the switch condition is defined.
	 */
    public boolean withSwitch() {
        return _switch != null;
    }

    /** Sets the swith condition.
	 * @param switchc the switch condition; EL allowed.
	 * Ignored if null.
	 * Note: if it is an empty string, the switch condition
	 * (returned by {@link #resolveSwitch}) is an empty string.
	 */
    public void setSwitch(String switchc) {
        if (_case != null && switchc != null) throw new IllegalStateException("case and switch/choose cannot coexist");
        _switch = switchc != null ? new ExValue(switchc, Object.class) : null;
    }

    /** Returns the switch condition's value; evaluate EL expression if any.
	 * <p>Note: unlike {@link #resolveCase}, it might return null
	 * even if {@link #withSwitch} is true.
	 */
    public Object resolveSwitch(Page page, Component comp) {
        return _switch == null ? null : comp != null ? _switch.getValue(getEvaluator(), comp) : _switch.getValue(getEvaluator(), page);
    }

    /** Returns whether the case condition is defined.
	 */
    public boolean withCase() {
        return _case != null;
    }

    /** Sets the case condition.
	 */
    public void setCase(String casec) {
        if (_switch != null && casec != null) throw new IllegalStateException("case and switch/choose cannot coexist");
        _case = Utils.parseList(casec, Object.class, false);
    }

    /** Returns the case condition's value, or null if no case is
	 * specified (i.e., {@link #withCase} is fasle.
	 * It evaluates EL expression if any.
	 */
    public Object[] resolveCase(Page page, Component comp) {
        if (_case == null) return null;
        Object[] ary = new Object[_case.length];
        for (int j = 0; j < _case.length; ++j) ary[j] = comp != null ? _case[j].getValue(getEvaluator(), comp) : _case[j].getValue(getEvaluator(), page);
        return ary;
    }
}
