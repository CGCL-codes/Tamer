package cn.com.believer.songyuanframework.openapi.storage.box.impl.simple.methods;

import java.io.IOException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import cn.com.believer.songyuanframework.openapi.storage.box.constant.BoxConstant;
import cn.com.believer.songyuanframework.openapi.storage.box.factories.BoxResponseFactory;
import cn.com.believer.songyuanframework.openapi.storage.box.functions.PublicUnshareRequest;
import cn.com.believer.songyuanframework.openapi.storage.box.functions.PublicUnshareResponse;
import cn.com.believer.songyuanframework.openapi.storage.box.objects.BoxException;

/**
 * @author Jimmy
 * 
 */
public class PublicUnshareMethod extends BaseBoxMethod {

    /**
     * This method unshares a shared file or folder. 'target' param shoud be
     * either 'file' or 'folder', 'target_id' is id of a file or folder to be
     * unshared.
     * 
     * On a successful result, the status will be 'unshare_ok'. If the result
     * wasn't successful, the status field can be: 'unshare_error',
     * 'wrong_node', 'not_logged_in', 'application_restricted'.
     * 
     * @param publicUnshareRequest
     *            request
     * @return response
     * @throws IOException
     *             io exception
     * @throws BoxException
     *             box exception
     */
    public PublicUnshareResponse publicUnshare(PublicUnshareRequest publicUnshareRequest) throws IOException, BoxException {
        PublicUnshareResponse baseBoxResponse = BoxResponseFactory.createPublicUnshareResponse();
        String apiKey = publicUnshareRequest.getApiKey();
        String authToken = publicUnshareRequest.getAuthToken();
        String target = publicUnshareRequest.getTarget();
        String targetId = publicUnshareRequest.getTargetId();
        if (BoxConstant.CONFIG_API_REQUEST_FORMAT_REST.equals(apiRequestFormat)) {
            StringBuffer urlBuff = super.getRestUrl(BoxConstant.ACTION_NAME_PUBLIC_UNSHARE);
            urlBuff.append(BoxConstant.AND_SIGN_STRING);
            urlBuff.append(BoxConstant.PARAM_NAME_API_KEY);
            urlBuff.append(BoxConstant.EQUALS_SIGN_STRING);
            urlBuff.append(apiKey);
            urlBuff.append(BoxConstant.AND_SIGN_STRING);
            urlBuff.append(BoxConstant.PARAM_NAME_AUTH_TOKEN);
            urlBuff.append(BoxConstant.EQUALS_SIGN_STRING);
            urlBuff.append(authToken);
            urlBuff.append(BoxConstant.AND_SIGN_STRING);
            urlBuff.append(BoxConstant.PARAM_NAME_TARGET);
            urlBuff.append(BoxConstant.EQUALS_SIGN_STRING);
            urlBuff.append(target);
            urlBuff.append(BoxConstant.AND_SIGN_STRING);
            urlBuff.append(BoxConstant.PARAM_NAME_TARGET_ID);
            urlBuff.append(BoxConstant.EQUALS_SIGN_STRING);
            urlBuff.append(targetId);
            try {
                Document doc = httpManager.doGet(urlBuff.toString());
                Element responseElm = doc.getRootElement();
                Element statusElm = responseElm.element(BoxConstant.PARAM_NAME_STATUS);
                String status = statusElm.getText();
                baseBoxResponse.setStatus(status);
            } catch (DocumentException e) {
                BoxException be = new BoxException("failed to parse to a document.", e);
                be.setStatus(baseBoxResponse.getStatus());
                throw be;
            }
        } else if (BoxConstant.CONFIG_API_REQUEST_FORMAT_XML.equals(apiRequestFormat)) {
            Document document = DocumentHelper.createDocument();
            Element requestElm = DocumentHelper.createElement(BoxConstant.PARAM_NAME_REQUEST);
            document.add(requestElm);
            Element actionElm = DocumentHelper.createElement(BoxConstant.PARAM_NAME_ACTION);
            Element apiKeyElm = DocumentHelper.createElement(BoxConstant.PARAM_NAME_API_KEY);
            Element authTokenElm = DocumentHelper.createElement(BoxConstant.PARAM_NAME_AUTH_TOKEN);
            Element targetElm = DocumentHelper.createElement(BoxConstant.PARAM_NAME_TARGET);
            Element targetIdElm = DocumentHelper.createElement(BoxConstant.PARAM_NAME_TARGET_ID);
            requestElm.add(actionElm);
            requestElm.add(apiKeyElm);
            requestElm.add(authTokenElm);
            requestElm.add(targetElm);
            requestElm.add(targetIdElm);
            actionElm.setText(BoxConstant.ACTION_NAME_PUBLIC_UNSHARE);
            apiKeyElm.setText(apiKey);
            authTokenElm.setText(authToken);
            targetElm.setText(target);
            targetIdElm.setText(targetId);
            String result = httpManager.doPostXML(xmlApiUrl, document.asXML());
            try {
                Document doc = DocumentHelper.parseText(result);
                Element responseElm = doc.getRootElement();
                Element statusElm = responseElm.element(BoxConstant.PARAM_NAME_STATUS);
                String status = statusElm.getText();
                baseBoxResponse.setStatus(status);
            } catch (DocumentException e) {
                BoxException be = new BoxException("failed to parse to a document.", e);
                be.setStatus(baseBoxResponse.getStatus());
                throw be;
            }
        } else if (BoxConstant.CONFIG_API_REQUEST_FORMAT_SOAP.equals(apiRequestFormat)) {
        } else {
        }
        return baseBoxResponse;
    }
}
