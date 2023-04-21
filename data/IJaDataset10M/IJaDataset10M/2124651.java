package com.potix.zul.html;

import com.potix.lang.Objects;
import com.potix.xml.HTMLs;
import com.potix.zk.ui.UiException;
import com.potix.zk.ui.WrongValueException;
import com.potix.zul.html.impl.LabelImageElement;

/**
 * A button.
 *
 * @author <a href="mailto:tomyeh@potix.com">tomyeh@potix.com</a>
 */
public class Button extends LabelImageElement {

    private String _orient = "horizontal", _dir = "normal";

    private String _href, _target;

    private boolean _disabled, _readonly;

    public Button() {
    }

    public Button(String label) {
        setLabel(label);
    }

    public Button(String label, String image) {
        setLabel(label);
        setImage(image);
    }

    /** Returns whether it is disabled.
	 * <p>Default: false.
	 */
    public boolean isDisabled() {
        return _disabled;
    }

    /** Sets whether it is disabled.
	 */
    public void setDisabled(boolean disabled) {
        if (_disabled != disabled) {
            _disabled = disabled;
            smartUpdate("disabled", _disabled);
        }
    }

    /** Returns whether it is readonly.
	 * <p>Default: false.
	 */
    public boolean isReadonly() {
        return _readonly;
    }

    /** Sets whether it is readonly.
	 */
    public void setReadonly(boolean readonly) {
        if (_readonly != readonly) {
            _readonly = readonly;
            smartUpdate("readOnly", _readonly);
        }
    }

    /** Returns the direction.
	 * <p>Default: "normal".
	 */
    public String getDir() {
        return _dir;
    }

    /** Sets the direction.
	 * @param dir either "normal" or "reverse".
	 */
    public void setDir(String dir) throws WrongValueException {
        if (!"normal".equals(dir) && !"reverse".equals(dir)) throw new WrongValueException(dir);
        if (!Objects.equals(_dir, dir)) {
            _dir = dir;
            invalidate(INNER);
        }
    }

    /** Returns the orient.
	 * <p>Default: "horizontal".
	 */
    public String getOrient() {
        return _orient;
    }

    /** Sets the orient.
	 * @param orient either "horizontal" or "vertical".
	 */
    public void setOrient(String orient) throws WrongValueException {
        if (!"horizontal".equals(orient) && !"vertical".equals(orient)) throw new WrongValueException(orient);
        if (!Objects.equals(_orient, orient)) {
            _orient = orient;
            invalidate(INNER);
        }
    }

    /** Returns the href.
	 * <p>Default: null. If null, the button has no function unless you
	 * specify the onClick event listener.
	 * <p>If it is not null, the onClick event won't be sent.
	 */
    public String getHref() {
        return _href;
    }

    /** Sets the href.
	 */
    public void setHref(String href) {
        if (href != null && href.length() == 0) href = null;
        if (!Objects.equals(_href, href)) {
            _href = href;
            smartUpdate("zk_href", getEncodedHref());
        }
    }

    /** Returns the target frame or window.
	 *
	 * <p>Note: it is useful only if href ({@link #setHref}) is specified
	 * (i.e., use the onClick listener).
	 *
	 * <p>Default: null.
	 */
    public String getTarget() {
        return _target;
    }

    /** Sets the target frame or window.
	 * @param target the name of the frame or window to hyperlink.
	 */
    public void setTarget(String target) {
        if (target != null && target.length() == 0) target = null;
        if (!Objects.equals(_target, target)) {
            _target = target;
            smartUpdate("zk_target", _target);
        }
    }

    private String getEncodedHref() {
        return _href != null ? getDesktop().getExecution().encodeURL(_href) : null;
    }

    public String getOuterAttrs() {
        final StringBuffer sb = new StringBuffer(64).append(super.getOuterAttrs());
        HTMLs.appendAttribute(sb, "zk_href", getEncodedHref());
        HTMLs.appendAttribute(sb, "zk_target", getTarget());
        if (isAsapRequired("onFocus")) HTMLs.appendAttribute(sb, "zk_onFocus", true);
        if (isAsapRequired("onBlur")) HTMLs.appendAttribute(sb, "zk_onBlur", true);
        if (isAsapRequired("onRightClick")) HTMLs.appendAttribute(sb, "zk_rtclk", true);
        if (isDisabled()) HTMLs.appendAttribute(sb, "disabled", "disabled");
        if (isReadonly()) HTMLs.appendAttribute(sb, "readonly", "readonly");
        return sb.toString();
    }
}
