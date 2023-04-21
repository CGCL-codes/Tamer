package ru.curs.showcase.app.client.utils;

import java.util.Map.Entry;
import ru.curs.showcase.app.api.datapanel.*;
import ru.curs.showcase.app.api.event.Action;
import ru.curs.showcase.app.api.html.XFormContext;
import ru.curs.showcase.app.client.*;
import ru.curs.showcase.app.client.api.*;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.*;
import com.google.gwt.user.client.rpc.*;

/**
 * Класс для загрузки файлов на сервер прямо из XForm.
 * 
 * @author den
 * 
 */
public class InlineUploader {

    private static final String NAME_ATTR = "name";

    private static final String VALUE_ATTR = "value";

    private final String data;

    private final XFormPanel currentXFormPanel;

    private final Action ac;

    private static int counter = 0;

    private boolean isAtLeastOneFileSelected = false;

    /**
	 * Обработчик окончания загрузки файлов.
	 */
    private static CompleteHandler submitHandler = null;

    public InlineUploader(final String aData, final XFormPanel aCurrentXFormPanel, final Action aAc) {
        super();
        data = aData;
        currentXFormPanel = aCurrentXFormPanel;
        ac = aAc;
    }

    public void checkForUpload(final CompleteHandler uplSubmitEndHandler) {
        submitHandler = uplSubmitEndHandler;
        DataPanelElementInfo dpei = currentXFormPanel.getElementInfo();
        counter = 0;
        isAtLeastOneFileSelected = false;
        for (Entry<String, DataPanelElementProc> entry : dpei.getProcs().entrySet()) {
            if (entry.getValue().getType() == DataPanelElementProcType.UPLOAD) {
                JavaScriptObject form = getElementById(dpei.getUploaderId(entry.getKey()));
                if (form != null) {
                    submitInlineForm(dpei, form);
                }
            }
        }
        if (!isAtLeastOneFileSelected) {
            if (submitHandler != null) {
                submitHandler.onComplete(false);
                submitHandler = null;
            }
        }
    }

    public void singleFormUpload(final String linkId) {
        submitHandler = null;
        DataPanelElementInfo dpei = currentXFormPanel.getElementInfo();
        JavaScriptObject form = getElementById(dpei.getUploaderId(linkId));
        if (form != null) {
            submitInlineForm(dpei, form);
        }
    }

    private void submitInlineForm(final DataPanelElementInfo dpei, final JavaScriptObject element) {
        boolean isFilesSelected = false;
        try {
            FormElement form = (FormElement) FormElement.as(element);
            SerializationStreamFactory ssf = WebUtils.createStdGWTSerializer();
            XFormContext context = new XFormContext(currentXFormPanel.getContext(), data);
            for (int i = 0; i < form.getElements().getLength(); i++) {
                Element el = form.getElements().getItem(i);
                if (XFormContext.class.getName().equals(el.getAttribute(NAME_ATTR))) {
                    el.setAttribute(VALUE_ATTR, context.toParamForHttpPost(ssf));
                    continue;
                }
                if (DataPanelElementInfo.class.getName().equals(el.getAttribute(NAME_ATTR))) {
                    el.setAttribute(VALUE_ATTR, dpei.toParamForHttpPost(ssf));
                    continue;
                }
                if ("file".equals(el.getAttribute("type"))) {
                    isFilesSelected = isFilesSelected || isFilesSelected(el);
                }
            }
            if (isFilesSelected) {
                counter++;
                isAtLeastOneFileSelected = true;
                form.submit();
                clearForm(form);
            }
        } catch (SerializationException e) {
            MessageBox.showSimpleMessage(Constants.XFORMS_UPLOAD_ERROR, e.getMessage());
        }
    }

    private void clearForm(final FormElement form) {
        form.setInnerHTML(form.getInnerHTML());
    }

    private static native boolean isFilesSelected(final Element el);

    private static native JavaScriptObject getElementById(final String id);

    public static synchronized void onSubmitComplete(final String iframeName) {
        Boolean result = true;
        String err = getErrorByIFrame(iframeName);
        if (err != null) {
            result = false;
            MessageBox.showSimpleMessage(Constants.XFORMS_UPLOAD_ERROR, err);
        }
        if (submitHandler != null) {
            counter--;
            if (counter == 0) {
                submitHandler.onComplete(result);
                submitHandler = null;
            }
        }
    }

    private static native String getErrorByIFrame(final String iframeName);

    public static void onChooseFiles(final String inputName, final String filenamesMapping, final Boolean needClearFilenames) {
        insertFilenamesByXPath(inputName, filenamesMapping, needClearFilenames);
    }

    private static native void insertFilenamesByXPath(final String inputName, final String filenamesMapping, final Boolean needClearFilenames);
}
