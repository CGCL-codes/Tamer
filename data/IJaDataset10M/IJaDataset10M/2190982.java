package com.handy.webwork.action;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.handy.webwork.config.HandyConfiguration;
import com.handy.webwork.view.MessageResource;

/**
 * 页面参数封装类。
 * 
 * @author rocken.zeng@gmail.com
 * 
 */
@SuppressWarnings("unchecked")
public class ActionForm {

    private static Log log = LogFactory.getLog(ActionForm.class);

    private ActionMessage actionMessage = new ActionMessage();

    private HashMap formMap = new HashMap();

    private String[] paramNames = null;

    public ActionForm() {
    }

    public ActionForm(HttpServletRequest request, ServletContext context) {
        parse(request, context);
        handle();
        paramNames();
    }

    /**
	 * 将参数从request中分析出来。
	 * 
	 * @param request
	 */
    private void parse(HttpServletRequest request, ServletContext context) {
        if (request == null) {
            log.error("request is null");
            return;
        }
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (isMultipart) {
            if (log.isDebugEnabled()) {
                log.debug("upload file...");
            }
            int maxMemorySize = 1024 * 1024;
            String _tempDirectory = context.getRealPath("/");
            File tempDirectory = new File(_tempDirectory);
            int maxRequestSize = 2 * 1024 * 1024;
            if (null != HandyConfiguration.properties(HandyConfiguration.PROP_UPLOADFILEMAXMEMORYSIZE)) {
                maxMemorySize = 1024 * Integer.parseInt(HandyConfiguration.properties(HandyConfiguration.PROP_UPLOADFILEMAXMEMORYSIZE));
            }
            if (null != HandyConfiguration.properties(HandyConfiguration.PROP_UPLOADFILETEMPDIRECTORY)) {
                tempDirectory = new File(HandyConfiguration.properties(HandyConfiguration.PROP_UPLOADFILETEMPDIRECTORY));
                if (!tempDirectory.exists()) tempDirectory.mkdirs();
            }
            if (null != HandyConfiguration.properties(HandyConfiguration.PROP_UPLOADFILEMAXREQUESTSIZE)) {
                maxRequestSize = 1024 * Integer.parseInt(HandyConfiguration.properties(HandyConfiguration.PROP_UPLOADFILEMAXREQUESTSIZE));
            }
            maxMemorySize = 0;
            FileItemFactory factory = new DiskFileItemFactory(maxMemorySize, tempDirectory);
            ServletFileUpload upload = new ServletFileUpload(factory);
            upload.setSizeMax(maxRequestSize);
            String fieldName = "";
            HashMap<String, LinkedList<String>> _formMap = new HashMap<String, LinkedList<String>>();
            HashMap<String, LinkedList<DiskFileItem>> _fileMap = new HashMap<String, LinkedList<DiskFileItem>>();
            try {
                List items = upload.parseRequest(request);
                Iterator iter = items.iterator();
                while (iter.hasNext()) {
                    DiskFileItem item = (DiskFileItem) iter.next();
                    fieldName = item.getFieldName();
                    if (log.isDebugEnabled()) {
                        log.info("fieldName=" + fieldName);
                    }
                    if (item.isFormField()) {
                        if (null == _formMap.get(fieldName)) {
                            _formMap.put(fieldName, new LinkedList<String>());
                        }
                        try {
                            _formMap.get(fieldName).add(item.getString(HandyConfiguration.properties(HandyConfiguration.PROP_ENCODING)));
                        } catch (UnsupportedEncodingException e) {
                            log.error(e.getMessage());
                        }
                    } else {
                        String fileName = item.getName();
                        if (null == fileName || "".equals(fileName)) continue;
                        if (null == _fileMap.get(fieldName)) {
                            _fileMap.put(fieldName, new LinkedList<DiskFileItem>());
                        }
                        _fileMap.get(fieldName).add(item);
                    }
                }
            } catch (FileUploadException e) {
                if (null != MessageResource.getInstance().getText(HandyConfiguration.ERROR_UPLOADFILE)) {
                    actionMessage.put(HandyConfiguration.ERROR_UPLOADFILE, MessageResource.getInstance().getText(HandyConfiguration.ERROR_UPLOADFILE));
                    log.error(MessageResource.getInstance().getText(HandyConfiguration.ERROR_UPLOADFILE));
                } else {
                    actionMessage.put(HandyConfiguration.ERROR_UPLOADFILE, e.getMessage());
                    log.error(e.getMessage());
                }
            }
            if (!_formMap.isEmpty()) {
                Set<Entry<String, LinkedList<String>>> _formParams = _formMap.entrySet();
                for (Map.Entry _formEntry : _formParams) {
                    LinkedList<String> _formValues = (LinkedList<String>) _formEntry.getValue();
                    if (null == formMap.get(_formEntry.getKey())) {
                        formMap.put(_formEntry.getKey(), _formValues.toArray(new String[_formValues.size()]));
                        if (log.isDebugEnabled()) {
                            log.debug("form param ==============");
                            for (String value : _formValues) {
                                log.debug(_formEntry.getKey() + " = " + value);
                            }
                            log.debug("form param end ==========");
                        }
                    }
                }
            }
            if (!_fileMap.isEmpty()) {
                Set<Entry<String, LinkedList<DiskFileItem>>> _fileParams = _fileMap.entrySet();
                for (Map.Entry _fileEntry : _fileParams) {
                    LinkedList<DiskFileItem> _fileValues = (LinkedList<DiskFileItem>) _fileEntry.getValue();
                    if (null == formMap.get(_fileEntry.getKey())) {
                        formMap.put(_fileEntry.getKey(), _fileValues.toArray(new DiskFileItem[_fileValues.size()]));
                    }
                }
            }
        } else {
            formMap.putAll(request.getParameterMap());
        }
    }

    private void handle() {
        if (formMap.isEmpty()) return;
        boolean _autoHandleForm = false;
        String autoHandleForm = HandyConfiguration.properties(HandyConfiguration.PROP_AUTODEALWITHFORM);
        if (null != autoHandleForm && autoHandleForm.equalsIgnoreCase("true")) {
            _autoHandleForm = true;
        } else {
            _autoHandleForm = false;
        }
        if (!_autoHandleForm) return;
        HashMap _formMap = new HashMap();
        Set<Entry> params = formMap.entrySet();
        for (Map.Entry entry : params) {
            Object[] value = (Object[]) entry.getValue();
            if (value == null || value.length > 1) {
                _formMap.put(entry.getKey(), entry.getValue());
            } else {
                _formMap.put(entry.getKey(), value[0]);
            }
        }
        getFormMap().clear();
        getFormMap().putAll(_formMap);
        if (log.isDebugEnabled()) {
            log.debug("finished handle ActionForm");
        }
    }

    /**
	 * 所有参数名。
	 * 
	 * @return
	 */
    private void paramNames() {
        if (formMap.isEmpty()) {
            setParamNames(null);
        } else {
            String[] names = new String[formMap.size()];
            Set<Entry> params = formMap.entrySet();
            int i = 0;
            for (Map.Entry entry : params) {
                names[i] = (String) entry.getKey();
                i++;
            }
            setParamNames(names);
        }
    }

    /**
	 * 自动验证表单(主要异常:上传文件时是否有错误)。
	 */
    public ActionMessage validate() {
        return actionMessage;
    }

    /**
	 * 返回字符串。
	 * 
	 * @param param
	 * @return
	 */
    public String getString(Object param) {
        return getValue(param) == null ? null : (String) (getValue(param)[0]);
    }

    /**
	 * 返回字符串数组。
	 * 
	 * @param param
	 * @return
	 */
    public String[] getStringArray(Object param) {
        return getValue(param) == null ? null : (String[]) (getValue(param));
    }

    public DiskFileItem getDiskFileItem(Object param) {
        return getValue(param) == null ? null : (DiskFileItem) (getValue(param)[0]);
    }

    public DiskFileItem[] getDiskFileItemArray(Object param) {
        return getValue(param) == null ? null : (DiskFileItem[]) (getValue(param));
    }

    public Integer getInteger(Object param) {
        return getValue(param) == null ? null : Integer.valueOf(((String) (getValue(param)[0])).trim());
    }

    public Integer[] getIntegerArray(Object param) {
        String[] _values = (String[]) getValue(param);
        Integer[] values = null;
        if (_values != null) {
            values = new Integer[_values.length];
            for (int i = 0; i < _values.length; i++) {
                values[i] = Integer.valueOf(_values[i]);
            }
        }
        return values;
    }

    public Object getObject(Object param) {
        return getValue(param) == null ? null : getValue(param)[0];
    }

    public Object[] getObjectArray(Object param) {
        return getValue(param) == null ? null : getValue(param);
    }

    /**
	 * 取对应参数的所有值。
	 * 
	 * @param param
	 * @return
	 */
    private Object[] getValue(Object param) {
        Object[] values = formMap.get(param) == null ? null : (Object[]) formMap.get(param);
        return values;
    }

    public HashMap getFormMap() {
        return formMap;
    }

    public void setFormMap(HashMap formMap) {
        this.formMap = formMap;
    }

    public ActionMessage getActionMessage() {
        return actionMessage;
    }

    public void setActionMessage(ActionMessage actionMessage) {
        this.actionMessage = actionMessage;
    }

    public String[] getParamNames() {
        return paramNames;
    }

    public void setParamNames(String[] paramNames) {
        this.paramNames = paramNames;
    }
}
