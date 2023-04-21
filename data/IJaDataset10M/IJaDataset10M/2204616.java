package org.zkoss.zul;

import java.util.Iterator;
import org.zkoss.lang.Objects;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.ext.render.MultiBranch;
import org.zkoss.zk.ui.ext.client.Openable;
import org.zkoss.zul.impl.XulElement;

/**
 * Groups a set of child elements to have a visual effect.
 * <p>Default {@link #getZclass}: "z-fieldset". If {@link #getMold()} is 3d,
 * "z-groupbox" is assumed.(since 3.5.0)
 *
 * @author tomyeh
 */
public class Groupbox extends XulElement implements org.zkoss.zul.api.Groupbox {

    private transient Caption _caption;

    /** The style used for the content block. */
    private String _cntStyle;

    /** The style class used for the content block. */
    private String _cntscls;

    private Boolean _legend;

    private boolean _open = true, _closable = true;

    /** Returns the caption of this groupbox.
	 */
    public Caption getCaption() {
        return _caption;
    }

    /** Returns the caption of this groupbox.
	 * @since 3.5.2
	 */
    public org.zkoss.zul.api.Caption getCaptionApi() {
        return getCaption();
    }

    /** Returns whether this groupbox is open.
	 *
	 * <p>Default: true.
	 */
    public boolean isOpen() {
        return _open;
    }

    /** Opens or closes this groupbox.
	 */
    public void setOpen(boolean open) {
        if (_open != open) {
            _open = open;
            smartUpdate("z.open", _open);
        }
    }

    /** Returns whether user can open or close the group box.
	 * In other words, if false, users are no longer allowed to
	 * change the open status (by clicking on the title).
	 *
	 * <p>Default: true.
	 */
    public boolean isClosable() {
        return _closable;
    }

    /** Sets whether user can open or close the group box.
	 */
    public void setClosable(boolean closable) {
        if (_closable != closable) {
            _closable = closable;
            smartUpdate("z.closable", closable);
        }
    }

    public String getZclass() {
        return _zclass == null ? isLegend() ? "z-fieldset" : "z-groupbox" : _zclass;
    }

    protected String getRealSclass() {
        final String cls = super.getRealSclass();
        final String added = isClosable() && !isOpen() ? getZclass() + "-colpsd" : "";
        return cls == null ? added : cls + " " + added;
    }

    /** Returns the CSS style for the content block of the groupbox.
	 * Used only if {@link #getMold} is not default.
	 */
    public String getContentStyle() {
        return _cntStyle;
    }

    /** Sets the CSS style for the content block of the groupbox.
	 * Used only if {@link #getMold} is not default.
	 *
	 * <p>Default: null.
	 */
    public void setContentStyle(String style) {
        if (!Objects.equals(_cntStyle, style)) {
            _cntStyle = style;
            smartUpdate("z.cntStyle", _cntStyle);
        }
    }

    /** Returns the style class used for the content block of the groupbox.
	 * Used only if {@link #getMold} is not default.
	 */
    public String getContentSclass() {
        return _cntscls;
    }

    /** Sets the style class used for the content block.
	 *
	 * @see #getContentSclass
	 * @since 3.0.0
	 */
    public void setContentSclass(String scls) {
        if (!Objects.equals(_cntscls, scls)) {
            _cntscls = scls;
            invalidate();
        }
    }

    /** Returns whether this groupbox is in the legend mold.
	 * By the legend mold we mean this group box is rendered with
	 * HTML FIELDSET tag.
	 *
	 * <p>Default: the legend mold is assumed if {@link #getMold}
	 * returns "default".
	 *
	 * <p>If it is not the case, you can call {@link #setLegend} to change
	 * it.
	 * @since 3.0.0
	 */
    public boolean isLegend() {
        return _legend != null ? _legend.booleanValue() : "default".equals(getMold());
    }

    /** Sets whether this groupbox is in the legend mold.
	 * @see #isLegend
	 * @since 3.0.0
	 */
    public void setLegend(boolean legend) {
        _legend = Boolean.valueOf(legend);
    }

    /** Returns the look of the caption.
	 * It is, in fact, a portion of the style class that are used
	 * to generate the style for the caption.
	 *
	 * <p>If the style class ({@link #getSclass}) of this groupbox is not
	 * defined and the mold is "default", then "groupbox" is returned.
	 *
	 * <p>If the mold is "default" or "3d", and the style class is defined,
	 * say "lite", then this method return "groupbox-lite".
	 *
	 * <p>If the mold is not "default", and the style class is not defined,
	 * this method returns:<br>
	 * <code>"groupbox-" + getMold()</code>
	 *
	 * <p>If the mold is not "default" and the style class is defined,
	 * this method returns<br/>
	 * <code>"groupbox-" + getMold() + "-" + getSclass()</code>
	 *
	 * <p>With this method, the caption generates the style class
	 * for the caption accordingly. For example, if the mold is "3d"
	 * and the style class not defined, then
	 * "groupbox-3d-tl" for the top-left corner of the caption,
	 * "groupbox-3d-tm" for the top-middle border, and so on.
	 *
	 * @since 3.0,0
	 * @deprecated As of release 3.5.0
	 */
    public String getCaptionLook() {
        return null;
    }

    public String getOuterAttrs() {
        final StringBuffer sb = new StringBuffer(64).append(super.getOuterAttrs());
        appendAsapAttr(sb, Events.ON_OPEN);
        final String clkattrs = getAllOnClickAttrs();
        if (clkattrs != null) sb.append(clkattrs);
        if (!_closable) sb.append(" z.closable=\"false\"");
        return sb.toString();
    }

    public void beforeChildAdded(Component child, Component refChild) {
        if (child instanceof Caption) {
            if (_caption != null && _caption != child) throw new UiException("Only one caption is allowed: " + this);
        } else if (refChild instanceof Caption) {
            throw new UiException("caption must be the first child");
        }
        super.beforeChildAdded(child, refChild);
    }

    public boolean insertBefore(Component child, Component refChild) {
        if (child instanceof Caption) {
            refChild = getFirstChild();
            if (super.insertBefore(child, refChild)) {
                _caption = (Caption) child;
                invalidate();
                return true;
            }
        } else {
            return super.insertBefore(child, refChild);
        }
        return false;
    }

    public void onChildRemoved(Component child) {
        if (child instanceof Caption) {
            _caption = null;
            invalidate();
        }
        super.onChildRemoved(child);
    }

    protected Object newExtraCtrl() {
        return new ExtraCtrl();
    }

    /** A utility class to implement {@link #getExtraCtrl}.
	 * It is used only by component developers.
	 */
    protected class ExtraCtrl extends XulElement.ExtraCtrl implements MultiBranch, Openable {

        public boolean inDifferentBranch(Component child) {
            return child instanceof Caption;
        }

        public void setOpenByClient(boolean open) {
            _open = open;
        }
    }

    public Object clone() {
        final Groupbox clone = (Groupbox) super.clone();
        if (_caption != null) clone.afterUnmarshal();
        return clone;
    }

    /** @param cnt # of children that need special handling (used for optimization).
	 * -1 means process all of them
	 */
    private void afterUnmarshal() {
        for (Iterator it = getChildren().iterator(); it.hasNext(); ) {
            final Object child = it.next();
            if (child instanceof Caption) {
                _caption = (Caption) child;
                break;
            }
        }
    }

    private synchronized void readObject(java.io.ObjectInputStream s) throws java.io.IOException, ClassNotFoundException {
        s.defaultReadObject();
        afterUnmarshal();
    }
}
