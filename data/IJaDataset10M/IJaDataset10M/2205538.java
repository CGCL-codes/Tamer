package cn.com.believer.songyuanframework.openapi.storage.box.impl.simple.methods;

import java.io.IOException;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import cn.com.believer.songyuanframework.openapi.storage.box.constant.BoxConstant;
import cn.com.believer.songyuanframework.openapi.storage.box.factories.BoxResponseFactory;
import cn.com.believer.songyuanframework.openapi.storage.box.functions.GetUpdatesRequest;
import cn.com.believer.songyuanframework.openapi.storage.box.functions.GetUpdatesResponse;
import cn.com.believer.songyuanframework.openapi.storage.box.impl.simple.utils.ConverterUtils;
import cn.com.believer.songyuanframework.openapi.storage.box.objects.BoxException;

/**
 * @author jjia
 * 
 */
public class GetUpdatesMethod extends BaseBoxMethod {

    /**
     * @param getUpdatesRequest
     * @return
     */
    public GetUpdatesResponse getUpdates(GetUpdatesRequest getUpdatesRequest) throws IOException, BoxException {
        GetUpdatesResponse getUpdatesResponse = BoxResponseFactory.createGetUpdatesResponse();
        String apiKey = getUpdatesRequest.getApiKey();
        String authToken = getUpdatesRequest.getAuthToken();
        String beginTimestamp = getUpdatesRequest.getBeginTimestamp();
        String endTimestamp = getUpdatesRequest.getEndTimestamp();
        String[] params = getUpdatesRequest.getParams();
        if (BoxConstant.CONFIG_API_REQUEST_FORMAT_REST.equals(apiRequestFormat)) {
            StringBuffer urlBuff = super.getRestUrl(getUpdatesRequest.getActionName());
            appendUrlParam(urlBuff, BoxConstant.PARAM_NAME_API_KEY, apiKey);
            appendUrlParam(urlBuff, BoxConstant.PARAM_NAME_AUTH_TOKEN, authToken);
            appendUrlParam(urlBuff, BoxConstant.PARAM_NAME_BEGIN_TIMESTAMP, beginTimestamp);
            appendUrlParam(urlBuff, BoxConstant.PARAM_NAME_END_TIMESTAMP, endTimestamp);
            appendUrlParams(urlBuff, BoxConstant.PARAM_NAME_PARAMS, params);
            try {
                Document doc = httpManager.doGet(urlBuff.toString());
                Element responseElm = doc.getRootElement();
                Element statusElm = responseElm.element(BoxConstant.PARAM_NAME_STATUS);
                Element updatesElm = responseElm.element(BoxConstant.PARAM_NAME_UPDATES);
                String status = statusElm.getText();
                getUpdatesResponse.setStatus(status);
                if (updatesElm != null) {
                    List updates = ConverterUtils.toBoxUpdates(updatesElm);
                    getUpdatesResponse.setUpdates(updates);
                }
            } catch (DocumentException e) {
                BoxException be = new BoxException("failed to parse to a document.", e);
                be.setStatus(getUpdatesResponse.getStatus());
                throw be;
            }
        } else if (BoxConstant.CONFIG_API_REQUEST_FORMAT_XML.equals(apiRequestFormat)) {
            Document document = DocumentHelper.createDocument();
            Element requestElm = DocumentHelper.createElement(BoxConstant.PARAM_NAME_REQUEST);
            document.add(requestElm);
            Element actionElm = DocumentHelper.createElement(BoxConstant.PARAM_NAME_ACTION);
            Element apiKeyElm = DocumentHelper.createElement(BoxConstant.PARAM_NAME_API_KEY);
            Element authTokenElm = DocumentHelper.createElement(BoxConstant.PARAM_NAME_AUTH_TOKEN);
            Element beginTimestampElm = DocumentHelper.createElement(BoxConstant.PARAM_NAME_BEGIN_TIMESTAMP);
            Element endTimestampElm = DocumentHelper.createElement(BoxConstant.PARAM_NAME_END_TIMESTAMP);
            Element paramsElm = DocumentHelper.createElement(BoxConstant.PARAM_NAME_PARAMS);
            requestElm.add(actionElm);
            requestElm.add(apiKeyElm);
            requestElm.add(authTokenElm);
            requestElm.add(beginTimestampElm);
            requestElm.add(endTimestampElm);
            requestElm.add(paramsElm);
            actionElm.setText(getUpdatesRequest.getActionName());
            apiKeyElm.setText(apiKey);
            authTokenElm.setText(authToken);
            beginTimestampElm.setText(beginTimestamp);
            endTimestampElm.setText(endTimestamp);
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    String param = params[i];
                    Element itemElm = DocumentHelper.createElement(BoxConstant.PARAM_NAME_ITEM);
                    itemElm.setText(param);
                    paramsElm.add(itemElm);
                }
            }
            String result = httpManager.doPostXML(xmlApiUrl, document.asXML());
            try {
                Document doc = DocumentHelper.parseText(result);
                Element responseElm = doc.getRootElement();
                Element statusElm = responseElm.element(BoxConstant.PARAM_NAME_STATUS);
                Element updatesElm = responseElm.element(BoxConstant.PARAM_NAME_UPDATES);
                String status = statusElm.getText();
                getUpdatesResponse.setStatus(status);
                if (updatesElm != null) {
                    List updates = ConverterUtils.toBoxUpdates(updatesElm);
                    getUpdatesResponse.setUpdates(updates);
                }
            } catch (DocumentException e) {
                BoxException be = new BoxException("failed to parse to a document.", e);
                be.setStatus(getUpdatesResponse.getStatus());
                throw be;
            }
        } else if (BoxConstant.CONFIG_API_REQUEST_FORMAT_SOAP.equals(apiRequestFormat)) {
        } else {
        }
        return getUpdatesResponse;
    }
}
