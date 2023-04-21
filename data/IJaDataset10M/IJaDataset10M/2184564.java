package org.atlantal.api.cms.control;

import java.util.Map;
import org.atlantal.api.app.rundata.AtlantalRequest;
import org.atlantal.api.cms.app.content.ContentContext;
import org.atlantal.api.cms.data.MapContentData;
import org.atlantal.api.cms.display.DisplayMedia;
import org.atlantal.api.cms.display.DisplayItemType;
import org.atlantal.api.cms.util.DisplayParams;
import org.atlantal.api.cms.util.FormDisplayParams;
import org.atlantal.utils.ActionResults;

/**
 * <p>Titre : Atlantal Framework</p>
 * <p>Description : </p>
 * <p>Copyright : Copyright (c) 2001-2002</p>
 * <p>Soci�t� : Mably Multim�dia</p>
 * @author Fran�ois MASUREL
 * @version 1.0
 */
public interface ControlType extends DisplayItemType {

    /** EDIT MODE */
    int MODE_EDIT = 0;

    /** SEARCH MODE */
    int MODE_SEARCH = 1;

    /**
     * {@inheritDoc}
     */
    void xml(ContentContext ctx, StringBuilder xml, Control ctl, MapContentData values);

    /**
     * @param ctx ctx
     * @param media html
     * @param ctl ctl
     * @param values values
     * @param params params
     */
    void text(ContentContext ctx, DisplayMedia media, Control ctl, MapContentData values, DisplayParams params);

    /**
     * @param ctl ctl
     * @param request rundata
     * @param ctx cm
     * @param element element
     * @param values values
     * @param result result
     */
    void getFormValue(Control ctl, AtlantalRequest request, ContentContext ctx, String element, MapContentData values, ActionResults result);

    /**
     * @param ctl ctl
     * @param ctx cm
     * @param request rundata
     * @param element element
     * @param values values
     * @param result result
     */
    void getSearchValue(Control ctl, AtlantalRequest request, ContentContext ctx, String element, MapContentData values, ActionResults result);

    /**
     * @param ctl ctl
     * @param values values
     * @param results results
     */
    void validate(Control ctl, MapContentData values, ActionResults results);

    /**
     * @return boolean
     */
    boolean scriptInitOnly();

    /**
     * @param ctx cm
     * @param ctl ctl
     * @param params params
     * @return string
     */
    String beforeInitScript(ContentContext ctx, Control ctl, Map params);

    /**
     * @param ctx cm
     * @param ctl ctl
     * @param params params
     * @return string
     */
    String initScript(ContentContext ctx, Control ctl, Map params);

    /**
     * @param ctx cm
     * @param script script
     * @param ctl ctl
     * @param values values
     * @param params params
     */
    void initValuesScript(ContentContext ctx, DisplayMedia script, Control ctl, MapContentData values, Map params);

    /**
     * @param ctx cm
     * @param ctl ctl
     * @param params params
     * @return string
     */
    String initSearchScript(ContentContext ctx, Control ctl, Map params);

    /**
     * @param ctx cm
     * @param script script
     * @param ctl ctl
     * @param values values
     * @param params params
     */
    void initSearchValuesScript(ContentContext ctx, DisplayMedia script, Control ctl, MapContentData values, Map params);

    /**
     * @param ctl ctl
     * @return string
     */
    String razScript(Control ctl);

    /**
     * @param ctl ctl
     * @return string
     */
    String searchRAZScript(Control ctl);

    /**
     * @param ctl control
     * @return has submit script
     */
    boolean hasOnSubmit(Control ctl);

    /**
     * @param script script
     * @param ctl ctl
     * @param search search
     */
    void onSubmit(StringBuilder script, Control ctl, boolean search);

    /**
     * @param script script
     * @param ctl ctl
     * @param search search
     */
    void onCancel(StringBuilder script, Control ctl, boolean search);

    /**
     * @param script script
     * @param ctl ctl
     * @param search search
     */
    void onReset(StringBuilder script, Control ctl, boolean search);

    /**
     * @param script script
     * @param ctl ctl
     * @param params prefix
     */
    void onChange(StringBuilder script, Control ctl, FormDisplayParams params);
}
