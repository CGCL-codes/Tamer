package org.atlantal.impl.cms.control;

import java.util.Map;
import org.atlantal.api.cms.app.content.ContentContext;
import org.atlantal.api.cms.control.Control;
import org.atlantal.api.cms.control.ControlType;
import org.atlantal.api.cms.data.MapContentData;
import org.atlantal.api.cms.display.DisplayMedia;
import org.atlantal.api.cms.util.DisplayParams;
import org.atlantal.api.app.rundata.AtlantalRequest;
import org.atlantal.utils.ActionResults;

/**
 * <p>Titre : Ewaloo</p>
 * <p>Description : Moteur de recherche structur�</p>
 * <p>Copyright : Copyright (c) 2002</p>
 * <p>Soci�t� : Mably Multim�dia</p>
 * @author Fran�ois MASUREL
 * @version 1.0
 */
public class ControlLink extends ControlTypeInstance {

    private static ControlType singleton = new ControlLink();

    /**
     * Constructor
     */
    protected ControlLink() {
    }

    /**
     * @return itemtype
     */
    public static ControlType getInstance() {
        return singleton;
    }

    /**
     * {@inheritDoc}
     */
    public void text(ContentContext ctx, DisplayMedia media, Control ctl, MapContentData ctvalues, DisplayParams cntparams) {
    }

    /**
     * {@inheritDoc}
     */
    public void xml(ContentContext ctx, StringBuilder xml, Control ctl, MapContentData values) {
    }

    /**
     * {@inheritDoc}
     */
    public void getFormValue(Control ctl, AtlantalRequest request, ContentContext ctx, String element, MapContentData values, ActionResults result) {
        String label = request.getParameter(element + "_label");
        String href = request.getParameter(element + "_href");
        String valueAlias = ctl.getItemObject().getFieldValueAlias();
        values.put(valueAlias + "_label", label);
        values.put(valueAlias + "_href", href);
    }

    /**
     * {@inheritDoc}
     */
    public void getSearchValue(Control ctl, AtlantalRequest request, ContentContext ctx, String element, MapContentData values, ActionResults result) {
        getFormValue(ctl, request, ctx, element, values, result);
    }

    /**
     * {@inheritDoc}
     */
    public String initScript(ContentContext ctx, Control ctl, Map params) {
        String elt = ctl.getId().toString();
        String javascript = "controlLinkInit(formparams, '" + elt + "', values);";
        return javascript;
    }

    /**
     * {@inheritDoc}
     */
    public void initValuesScript(ContentContext ctx, DisplayMedia script, Control ctl, MapContentData values, Map params) {
        String elt = ctl.getId().toString();
        String valueAlias = ctl.getItemObject().getFieldValueAlias();
        String label = (String) values.get(valueAlias + "_label");
        if (label != null) {
            label = label.replaceAll("'", "\\\\'");
            label = label.replaceAll("\r", "");
            label = label.replaceAll("\n", "\\\\n");
            script.append("values['V").append(elt).append("_label'] = '").append(label).append("';");
        }
        String href = (String) values.get(valueAlias + "_href");
        if (href != null) {
            href = href.replaceAll("'", "\\\\'");
            href = href.replaceAll("\r", "");
            href = href.replaceAll("\n", "\\\\n");
            script.append("values['V").append(elt).append("_href'] = '").append(href).append("';");
        }
    }

    /**
     * {@inheritDoc}
     */
    public String initSearchScript(ContentContext ctx, Control ctl, Map params) {
        return initScript(ctx, ctl, params);
    }

    /**
     * {@inheritDoc}
     */
    public void initSearchValuesScript(ContentContext ctx, DisplayMedia script, Control ctl, MapContentData values, Map params) {
        initValuesScript(ctx, script, ctl, values, params);
    }

    /**
     * {@inheritDoc}
     */
    public String razScript(Control ctl) {
        String javascript = "controlLinkRaz(formparams, '" + ctl.getId() + "');";
        return javascript;
    }

    /**
     * {@inheritDoc}
     */
    public String searchRAZScript(Control ctl) {
        return razScript(ctl);
    }
}
