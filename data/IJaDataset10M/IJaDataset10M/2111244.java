package com.manydesigns.portofino.methods.scrud;

import bsh.TargetError;
import com.manydesigns.portofino.base.MDClass;
import com.manydesigns.portofino.base.MDConfig;
import com.manydesigns.portofino.base.MDConfigException;
import com.manydesigns.portofino.base.MDObject;
import com.manydesigns.portofino.base.workflow.MDWfTransition;
import com.manydesigns.portofino.methods.BadRequestException;
import com.manydesigns.portofino.methods.BasicServlet;
import com.manydesigns.portofino.util.Defs;
import com.manydesigns.portofino.util.Util;
import com.manydesigns.xmlbuffer.XhtmlBuffer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Locale;

/**
 * @author predo
 */
public class Update extends BasicServlet {

    public static final String copyright = "Copyright (c) 2005-2009, ManyDesigns srl";

    private final Log log = LogFactory.getLog(Update.class);

    @Override
    public void doMethod(HttpServletRequest req, HttpServletResponse res, MDConfig config, XhtmlBuffer xb) throws Exception {
        String successReturnUrl = req.getParameter(Defs.SUCCESS_RETURN_URL_PARAMETER);
        String cancelReturnUrl = req.getParameter(Defs.CANCEL_RETURN_URL_PARAMETER);
        Locale locale = config.getLocale();
        String clsName = req.getParameter("class");
        if (clsName == null) {
            throw new Exception(Util.getLocalizedString(Defs.MDLIBI18N, locale, "Class_not_specified"));
        }
        MDClass cls = config.getMDClassByName(clsName);
        if (req.getParameter("cancel") != null) {
            if (cancelReturnUrl == null) {
                cancelReturnUrl = cls.getSearchLink();
            }
            res.sendRedirect(cancelReturnUrl);
            return;
        }
        String idString = req.getParameter("id");
        int id;
        try {
            id = Integer.parseInt(idString);
        } catch (Exception e) {
            String idStr = "id";
            throw new BadRequestException(MessageFormat.format(Util.getLocalizedString(Defs.MDLIBI18N, locale, "Invalid_value_for_parameter"), idString, idStr));
        }
        MDObject obj = cls.getMDObject(id);
        if (!obj.isVisible()) throw new Exception(Util.getLocalizedString(Defs.MDLIBI18N, locale, "Visibility_denied"));
        CreateUpdateForm form = new CreateUpdateForm(req, obj, successReturnUrl, cancelReturnUrl);
        if (!form.getErrors().isEmpty()) {
            form.render(xb);
        } else if (form.isActionPerformed()) {
            applyUpdates(successReturnUrl, xb, form, res, config, obj);
        } else {
            form.render(xb);
        }
    }

    private void applyUpdates(String successReturnUrl, final XhtmlBuffer xb, final CreateUpdateForm form, final HttpServletResponse res, final MDConfig config, final MDObject obj) throws Exception {
        Collection<String> errors = form.getErrors();
        try {
            if (config.isUpstairs()) {
                config.writeStartTransactionDDL();
            }
            form.consolidate();
            MDWfTransition selectedWft = form.getSelectedWft();
            if (selectedWft != null) {
                obj.applyWfTransition(selectedWft, errors);
            }
            if (errors.isEmpty()) {
                config.getConfigContainer().commitTransaction();
                if (config.isUpstairs()) {
                    config.writeCommitDDL();
                }
                if (successReturnUrl == null) {
                    successReturnUrl = obj.getReadLink();
                }
                res.sendRedirect(successReturnUrl);
            } else {
                form.render(xb);
            }
        } catch (TargetError e) {
            log.error(e.getMessage(), e);
            Throwable target = e.getTarget();
            String errorMsg = target.getMessage();
            errors.add(errorMsg);
            form.render(xb);
        } catch (MDConfigException e) {
            log.error(e.getMessage(), e);
            errors.addAll(e.getErrors());
            form.render(xb);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            String errorMsg = e.getMessage();
            errors.add(errorMsg);
            form.render(xb);
        }
    }
}
