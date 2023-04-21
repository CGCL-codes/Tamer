package net.sf.tacos.ajax.components;

import net.sf.tacos.ajax.AjaxUtils;
import net.sf.tacos.ajax.AjaxWebRequest;
import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRenderSupport;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.TapestryUtils;
import org.apache.tapestry.services.ExpressionEvaluator;

/**
 *  Implements a component that manages an HTML &lt;input type=radio&gt; form element.
 *  Such a component must be wrapped (possibly indirectly)
 *  inside a {@link RadioGroup} component.
 *
 *  [<a href="../../../../../ComponentReference/Radio.html">Component Reference</a>]
 *
 *
 *  <p>{@link Radio} and {@link RadioGroup} are generally not used (except
 *  for very special cases).  Instead, a {@link PropertySelection} component is used.
 *
 *
 *  @author Howard Lewis Ship
 *
 **/
public abstract class Radio extends AbstractComponent {

    /**
     * Injected ajax request
     * @param ajaxRequest
     */
    public abstract AjaxWebRequest getAjaxRequest();

    /**
     * Injected ognl parser
     * @param expressionEvaluator
     */
    public abstract ExpressionEvaluator getExpressionEvaluator();

    /**
     * Should be connected to a parameter named "id" (annotations would be helpful here!). For
     * components w/o such a parameter, this will simply return null.
     */
    public abstract String getIdParameter();

    /**
     *  Renders the form element, or responds when the form containing the element
     *  is submitted (by checking {@link Form#isRewinding()}.
     *
     *
     **/
    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) {
        RadioGroup group = RadioGroup.get(cycle);
        if (group == null) throw new ApplicationRuntimeException(Tapestry.getMessage("Radio.must-be-contained-by-group"), this, null, null);
        boolean rewinding = group.isRewinding();
        int option = group.getNextOptionId();
        if (rewinding) {
            if (!isDisabled() && !group.isDisabled() && group.isSelected(option)) group.updateSelection(getValue());
            return;
        }
        writer.beginEmpty("input");
        writer.attribute("type", "radio");
        writer.attribute("name", group.getName());
        String uid = getIdParameter();
        if (uid == null) {
            uid = group.getName() + option;
        }
        writer.attribute("id", uid);
        if (isParameterBound("eventListener")) {
            String evListener = (String) getBinding("eventListener").getObject();
            PageRenderSupport prs = TapestryUtils.getPageRenderSupport(cycle, this);
            AjaxUtils.linkFieldObservers(cycle, getExpressionEvaluator(), getAjaxRequest(), this, uid, prs, evListener);
        }
        if (group.isSelection(getValue())) writer.attribute("checked", "checked");
        if (isDisabled() || group.isDisabled()) writer.attribute("disabled", "disabled");
        writer.attribute("value", option);
        renderInformalParameters(writer, cycle);
    }

    /**
     * Whether or not disabled in form
     * @return
     */
    public abstract boolean isDisabled();

    /**
     * The value selected
     * @return
     */
    public abstract Object getValue();
}
