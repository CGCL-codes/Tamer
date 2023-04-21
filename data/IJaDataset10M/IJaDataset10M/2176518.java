package com.quikj.ace.web.client;

import java.util.HashMap;
import java.util.List;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.quikj.ace.messages.vo.app.Message;
import com.quikj.ace.messages.vo.talk.CannedMessageSummaryElement;
import com.quikj.ace.messages.vo.talk.SendMailRequestMessage;

/**
 * @author amit
 * 
 */
public interface AceOperatorServiceAsync {

    public RequestBuilder exchangeMessages(Message incoming, AsyncCallback<List<Message>> callback);

    public RequestBuilder exchangeMessages(List<Message> incoming, AsyncCallback<List<Message>> callback);

    public RequestBuilder connect(AsyncCallback<String> callback);

    public RequestBuilder disconnect(String sessionId, AsyncCallback<Void> callback);

    public RequestBuilder listCannedMessages(String[] groups, AsyncCallback<CannedMessageSummaryElement[]> callback);

    public RequestBuilder getProfile(String profileName, AsyncCallback<HashMap<String, String>> callback);

    public RequestBuilder allOperatorBusy(String group, AsyncCallback<Boolean> callback);

    public RequestBuilder sendMail(SendMailRequestMessage mail, String captcha, AsyncCallback<String> callback);
}
