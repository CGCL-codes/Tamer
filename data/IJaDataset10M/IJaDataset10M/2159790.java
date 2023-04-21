package com.liferay.portlet.expando.action;

import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.User;
import com.liferay.portal.security.auth.PrincipalException;
import com.liferay.portal.struts.PortletAction;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.expando.ColumnNameException;
import com.liferay.portlet.expando.ColumnTypeException;
import com.liferay.portlet.expando.DuplicateColumnNameException;
import com.liferay.portlet.expando.NoSuchColumnException;
import com.liferay.portlet.expando.ValueDataException;
import com.liferay.portlet.expando.model.ExpandoBridge;
import com.liferay.portlet.expando.model.ExpandoBridgeImpl;
import com.liferay.portlet.expando.model.ExpandoColumnConstants;
import com.liferay.portlet.expando.service.ExpandoColumnServiceUtil;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.List;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * <a href="EditExpandoAction.java.html"><b><i>View Source</i></b></a>
 *
 * @author Raymond Augé
 *
 */
public class EditExpandoAction extends PortletAction {

    public static Object getValue(ActionRequest actionRequest, String name, int type) throws PortalException, SystemException {
        Object value = null;
        if (type == ExpandoColumnConstants.BOOLEAN) {
            value = ParamUtil.getBoolean(actionRequest, name);
        } else if (type == ExpandoColumnConstants.BOOLEAN_ARRAY) {
        } else if (type == ExpandoColumnConstants.DATE) {
            User user = PortalUtil.getUser(actionRequest);
            int valueDateMonth = ParamUtil.getInteger(actionRequest, name + "Month");
            int valueDateDay = ParamUtil.getInteger(actionRequest, name + "Day");
            int valueDateYear = ParamUtil.getInteger(actionRequest, name + "Year");
            int valueDateHour = ParamUtil.getInteger(actionRequest, name + "Hour");
            int valueDateMinute = ParamUtil.getInteger(actionRequest, name + "Minute");
            int valueDateAmPm = ParamUtil.getInteger(actionRequest, name + "AmPm");
            if (valueDateAmPm == Calendar.PM) {
                valueDateHour += 12;
            }
            value = PortalUtil.getDate(valueDateMonth, valueDateDay, valueDateYear, valueDateHour, valueDateMinute, user.getTimeZone(), new ValueDataException());
        } else if (type == ExpandoColumnConstants.DATE_ARRAY) {
        } else if (type == ExpandoColumnConstants.DOUBLE) {
            value = ParamUtil.getDouble(actionRequest, name);
        } else if (type == ExpandoColumnConstants.DOUBLE_ARRAY) {
            String[] values = StringUtil.split(ParamUtil.getString(actionRequest, name), StringPool.NEW_LINE);
            value = GetterUtil.getDoubleValues(values);
        } else if (type == ExpandoColumnConstants.FLOAT) {
            value = ParamUtil.getFloat(actionRequest, name);
        } else if (type == ExpandoColumnConstants.FLOAT_ARRAY) {
            String[] values = StringUtil.split(ParamUtil.getString(actionRequest, name), StringPool.NEW_LINE);
            value = GetterUtil.getFloatValues(values);
        } else if (type == ExpandoColumnConstants.INTEGER) {
            value = ParamUtil.getInteger(actionRequest, name);
        } else if (type == ExpandoColumnConstants.INTEGER_ARRAY) {
            String[] values = StringUtil.split(ParamUtil.getString(actionRequest, name), StringPool.NEW_LINE);
            value = GetterUtil.getIntegerValues(values);
        } else if (type == ExpandoColumnConstants.LONG) {
            value = ParamUtil.getLong(actionRequest, name);
        } else if (type == ExpandoColumnConstants.LONG_ARRAY) {
            String[] values = StringUtil.split(ParamUtil.getString(actionRequest, name), StringPool.NEW_LINE);
            value = GetterUtil.getLongValues(values);
        } else if (type == ExpandoColumnConstants.SHORT) {
            value = ParamUtil.getShort(actionRequest, name);
        } else if (type == ExpandoColumnConstants.SHORT_ARRAY) {
            String[] values = StringUtil.split(ParamUtil.getString(actionRequest, name), StringPool.NEW_LINE);
            value = GetterUtil.getShortValues(values);
        } else if (type == ExpandoColumnConstants.STRING_ARRAY) {
            String[] values = StringUtil.split(ParamUtil.getString(actionRequest, name), StringPool.NEW_LINE);
            value = StringUtil.merge(values);
        } else {
            value = ParamUtil.getString(actionRequest, name);
        }
        return value;
    }

    public void processAction(ActionMapping mapping, ActionForm form, PortletConfig portletConfig, ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {
        String cmd = ParamUtil.getString(actionRequest, Constants.CMD);
        try {
            if (cmd.equals(Constants.ADD)) {
                addExpando(actionRequest);
            } else if (cmd.equals(Constants.DELETE)) {
                deleteExpando(actionRequest);
            } else if (cmd.equals(Constants.UPDATE)) {
                updateExpando(actionRequest);
            }
            sendRedirect(actionRequest, actionResponse);
        } catch (Exception e) {
            if (e instanceof NoSuchColumnException || e instanceof PrincipalException) {
                SessionErrors.add(actionRequest, e.getClass().getName());
                setForward(actionRequest, "portlet.expando.error");
            } else if (e instanceof ColumnNameException || e instanceof ColumnTypeException || e instanceof DuplicateColumnNameException || e instanceof ValueDataException) {
                SessionErrors.add(actionRequest, e.getClass().getName());
            } else {
                throw e;
            }
        }
    }

    public ActionForward render(ActionMapping mapping, ActionForm form, PortletConfig portletConfig, RenderRequest renderRequest, RenderResponse renderResponse) throws Exception {
        try {
            ActionUtil.getColumn(renderRequest);
        } catch (Exception e) {
            if (e instanceof NoSuchColumnException || e instanceof PrincipalException) {
                SessionErrors.add(renderRequest, e.getClass().getName());
                return mapping.findForward("portlet.expando.error");
            } else {
                throw e;
            }
        }
        return mapping.findForward(getForward(renderRequest, "portlet.expando.edit_expando"));
    }

    protected void addExpando(ActionRequest actionRequest) throws Exception {
        String modelResource = ParamUtil.getString(actionRequest, "modelResource");
        long resourcePrimKey = ParamUtil.getLong(actionRequest, "resourcePrimKey");
        String name = ParamUtil.getString(actionRequest, "name");
        int type = ParamUtil.getInteger(actionRequest, "type");
        ExpandoBridge expandoBridge = new ExpandoBridgeImpl(modelResource, resourcePrimKey);
        expandoBridge.addAttribute(name, type);
        updateProperties(actionRequest, expandoBridge, name);
    }

    protected void deleteExpando(ActionRequest actionRequest) throws Exception {
        long columnId = ParamUtil.getLong(actionRequest, "columnId");
        ExpandoColumnServiceUtil.deleteColumn(columnId);
    }

    protected void updateExpando(ActionRequest actionRequest) throws Exception {
        String modelResource = ParamUtil.getString(actionRequest, "modelResource");
        long resourcePrimKey = ParamUtil.getLong(actionRequest, "resourcePrimKey");
        String name = ParamUtil.getString(actionRequest, "name");
        int type = ParamUtil.getInteger(actionRequest, "type");
        Object defaultValue = getValue(actionRequest, "defaultValue", type);
        ExpandoBridge expandoBridge = new ExpandoBridgeImpl(modelResource, resourcePrimKey);
        if (Validator.isNotNull(defaultValue)) {
            expandoBridge.setAttributeDefault(name, defaultValue);
        }
        updateProperties(actionRequest, expandoBridge, name);
    }

    protected void updateProperties(ActionRequest actionRequest, ExpandoBridge expandoBridge, String name) throws Exception {
        Enumeration<String> enu = actionRequest.getParameterNames();
        UnicodeProperties properties = expandoBridge.getAttributeProperties(name);
        List<String> propertyNames = new ArrayList<String>();
        while (enu.hasMoreElements()) {
            String param = enu.nextElement();
            if (param.indexOf("PropertyName(") != -1) {
                String propertyName = ParamUtil.getString(actionRequest, param);
                propertyNames.add(propertyName);
            }
        }
        for (String propertyName : propertyNames) {
            String value = ParamUtil.getString(actionRequest, "Property(" + propertyName + ")");
            properties.setProperty(propertyName, value);
        }
        expandoBridge.setAttributeProperties(name, properties);
    }
}
