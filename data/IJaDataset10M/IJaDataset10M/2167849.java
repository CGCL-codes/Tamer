package com.liferay.portal.upload;

import com.liferay.portal.kernel.upload.UploadServletRequest;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.util.PropsUtil;
import com.liferay.util.SystemProperties;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <a href="UploadServletRequestImpl.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 * @author Zongliang Li
 * @author Harry Mark
 *
 */
public class UploadServletRequestImpl extends HttpServletRequestWrapper implements UploadServletRequest {

    public static final int DEFAULT_SIZE_MAX = GetterUtil.getInteger(PropsUtil.get(UploadServletRequestImpl.class.getName() + ".max.size"));

    public static final File DEFAULT_TEMP_DIR = new File(GetterUtil.getString(PropsUtil.get(UploadServletRequestImpl.class.getName() + ".temp.dir"), SystemProperties.get(SystemProperties.TMP_DIR)));

    public UploadServletRequestImpl(HttpServletRequest request) {
        super(request);
        _params = new LinkedHashMap<String, LiferayFileItem[]>();
        try {
            ServletFileUpload servletFileUpload = new LiferayFileUpload(new LiferayFileItemFactory(DEFAULT_TEMP_DIR), request);
            servletFileUpload.setSizeMax(DEFAULT_SIZE_MAX);
            _lsr = new LiferayServletRequest(request);
            List<LiferayFileItem> list = servletFileUpload.parseRequest(_lsr);
            for (LiferayFileItem fileItem : list) {
                if (fileItem.isFormField()) {
                    fileItem.setString(request.getCharacterEncoding());
                }
                LiferayFileItem[] fileItems = _params.get(fileItem.getFieldName());
                if (fileItems == null) {
                    fileItems = new LiferayFileItem[] { fileItem };
                } else {
                    LiferayFileItem[] newFileItems = new LiferayFileItem[fileItems.length + 1];
                    System.arraycopy(fileItems, 0, newFileItems, 0, fileItems.length);
                    newFileItems[newFileItems.length - 1] = fileItem;
                    fileItems = newFileItems;
                }
                _params.put(fileItem.getFieldName(), fileItems);
            }
        } catch (FileUploadException fue) {
            _log.error(fue.getMessage());
        }
    }

    public void cleanUp() {
        if ((_params != null) && !_params.isEmpty()) {
            for (LiferayFileItem[] fileItems : _params.values()) {
                for (int i = 0; i < fileItems.length; i++) {
                    fileItems[i].delete();
                }
            }
        }
    }

    public String getContentType(String name) {
        LiferayFileItem[] fileItems = _params.get(name);
        if ((fileItems != null) && (fileItems.length > 0)) {
            return fileItems[0].getContentType();
        } else {
            return null;
        }
    }

    public File getFile(String name) {
        if (getFileName(name) == null) {
            return null;
        }
        LiferayFileItem[] fileItems = _params.get(name);
        if ((fileItems != null) && (fileItems.length > 0)) {
            return fileItems[0].getStoreLocation();
        } else {
            return null;
        }
    }

    public String getFileName(String name) {
        LiferayFileItem[] fileItems = _params.get(name);
        if ((fileItems != null) && (fileItems.length > 0)) {
            return fileItems[0].getFileName();
        } else {
            return null;
        }
    }

    public String getFullFileName(String name) {
        LiferayFileItem[] fileItems = _params.get(name);
        if ((fileItems != null) && (fileItems.length > 0)) {
            return fileItems[0].getFullFileName();
        } else {
            return null;
        }
    }

    public ServletInputStream getInputStream() throws IOException {
        return _lsr.getInputStream();
    }

    public Map<String, LiferayFileItem[]> getMultipartParameterMap() {
        return _params;
    }

    public String getParameter(String name) {
        LiferayFileItem[] fileItems = _params.get(name);
        if ((fileItems != null) && (fileItems.length > 0)) {
            return fileItems[0].getString();
        } else {
            return super.getParameter(name);
        }
    }

    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> map = new HashMap<String, String[]>();
        Enumeration<String> enu = getParameterNames();
        while (enu.hasMoreElements()) {
            String name = enu.nextElement();
            map.put(name, getParameterValues(name));
        }
        return map;
    }

    public Enumeration<String> getParameterNames() {
        List<String> parameterNames = new ArrayList<String>();
        Enumeration<String> enu = super.getParameterNames();
        while (enu.hasMoreElements()) {
            String name = enu.nextElement();
            if (!_params.containsKey(name)) {
                parameterNames.add(name);
            }
        }
        for (String name : _params.keySet()) {
            parameterNames.add(name);
        }
        return Collections.enumeration(parameterNames);
    }

    public String[] getParameterValues(String name) {
        String[] parentValues = super.getParameterValues(name);
        LiferayFileItem[] fileItems = _params.get(name);
        if ((fileItems == null) || (fileItems.length == 0)) {
            return parentValues;
        } else if ((parentValues == null) || (parentValues.length == 0)) {
            String[] values = new String[fileItems.length];
            for (int i = 0; i < values.length; i++) {
                values[i] = fileItems[i].getString();
            }
            return values;
        } else {
            String[] values = new String[parentValues.length + fileItems.length];
            System.arraycopy(parentValues, 0, values, 0, parentValues.length);
            for (int i = parentValues.length; i < values.length; i++) {
                values[i] = fileItems[i - parentValues.length].getString();
            }
            return values;
        }
    }

    public Boolean isFormField(String name) {
        LiferayFileItem[] fileItems = _params.get(name);
        if ((fileItems != null) && (fileItems.length > 0)) {
            return new Boolean(fileItems[0].isFormField());
        } else {
            return null;
        }
    }

    private static Log _log = LogFactory.getLog(UploadServletRequestImpl.class);

    private LiferayServletRequest _lsr;

    private Map<String, LiferayFileItem[]> _params;
}
