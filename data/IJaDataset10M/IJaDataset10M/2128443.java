package com.acciente.induction.controller;

import com.acciente.commons.htmlform.FileHandle;
import com.acciente.commons.htmlform.Parser;
import com.acciente.commons.htmlform.ParserException;
import com.acciente.induction.init.config.Config;
import org.apache.commons.fileupload.FileUploadException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Internal.
 * The implementation backing the the Form interface.
 *
 * @created Feb 18, 2008
 *
 * @author Adinath Raveendra Raj
 */
public class HTMLForm implements Form {

    private HttpServletRequest _oServletRequest;

    private Map _oFormParams;

    private Config.FileUpload _oFileUploadConfig;

    private boolean _bFormParsed;

    public HTMLForm(HttpServletRequest oHttpServletRequest, Config.FileUpload oFileUploadConfig) {
        _oServletRequest = oHttpServletRequest;
        _oFileUploadConfig = oFileUploadConfig;
        _bFormParsed = false;
    }

    public Object getObject(String sParamName) throws FormException {
        return getParamValue(sParamName);
    }

    public List getList(String sParamName) throws FormException {
        return (List) getParamValue(sParamName);
    }

    public Map getMap(String sParamName) throws FormException {
        return (Map) getParamValue(sParamName);
    }

    public String getString(String sParamName) throws FormException {
        return (String) getParamValue(sParamName);
    }

    public int getInteger(String sParamName) throws FormException {
        return ((Integer) getParamValue(sParamName)).intValue();
    }

    public float getFloat(String sParamName) throws FormException {
        return ((Float) getParamValue(sParamName)).floatValue();
    }

    public long getLong(String sParamName) throws FormException {
        return ((Long) getParamValue(sParamName)).longValue();
    }

    public boolean getBoolean(String sParamName) throws FormException {
        return ((Boolean) getParamValue(sParamName)).booleanValue();
    }

    public FileHandle getFile(String sParamName) throws FormException {
        return (FileHandle) getParamValue(sParamName);
    }

    public Set getParamNames() throws FormException {
        parseForm();
        return _oFormParams == null ? null : _oFormParams.keySet();
    }

    public boolean containsParam(String sParamName) throws FormException {
        parseForm();
        return _oFormParams != null && _oFormParams.containsKey(sParamName);
    }

    private Object getParamValue(String sParamName) throws FormException {
        parseForm();
        if (_oFormParams == null || !_oFormParams.containsKey(sParamName)) {
            throw (new FormException("Attempt to access undefined HTML form parameter: " + sParamName));
        }
        return _oFormParams.get(sParamName);
    }

    private void parseForm() throws FormException {
        if (!_bFormParsed) {
            try {
                _oFormParams = Parser.parseForm(_oServletRequest, _oFileUploadConfig.getStoreOnDiskThresholdInBytes(), _oFileUploadConfig.getUploadedFileStorageDir());
            } catch (IOException e) {
                throw new FormException("Error parsing form: ", e);
            } catch (FileUploadException e) {
                throw new FormException("Error parsing form: ", e);
            } catch (ParserException e) {
                throw new FormException("Error parsing form", e);
            }
            _bFormParsed = true;
        }
    }
}
