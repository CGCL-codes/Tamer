package Communication;

import Common.InfoLogger;
import Response.*;
import Request.Request;
import Request.*;
import Request.RequestType;
import java.io.*;
import java.net.*;
import java.util.*;
import AccManager.UserProfile;

/**
 *
 * @author lcy
 */
public class NetDataProcessor {

    String _str_data;

    Request _request;

    InfoLogger _debugger;

    ProcessorType _p_type;

    public NetDataProcessor() {
        InitCommComponents();
    }

    public NetDataProcessor(Request new_request) {
        _request = new_request;
        _p_type = ProcessorType.OBJECT;
    }

    private void InitCommComponents() {
        _debugger = new InfoLogger();
    }

    public void FormResponse(NetDataResponse result) {
        switch(_p_type) {
            case OBJECT:
                GetResponseOBJ(result);
                break;
        }
    }

    public boolean IsDataFormatCorrect(Request request) {
        RequestType type = request.GetRequestType();
        switch(type) {
            case LOGIN:
            case REGISTRATION:
                UserProfile profile = (UserProfile) request.GetRequestContent();
                String name = profile.GetUserID();
                String passwd = profile.GetPasswdRaw();
                if (name.equals("") || passwd.equals("")) {
                    return false;
                }
                break;
            case USERLIST:
                name = (String) request.GetRequestContent();
                if (name.equals("")) {
                    return false;
                }
                break;
            default:
                break;
        }
        return true;
    }

    private void GetResponseOBJ(NetDataResponse result) {
        switch(_request.GetRequestType()) {
            case LOGIN:
            case REGISTRATION:
                FormLoginRegResponseOBJ((NetDataLRResp) result);
                break;
            case USERLIST:
                FormUserListResponseOBJ((NetDataUserListResp) result);
                break;
            default:
                break;
        }
    }

    private void FormLoginRegResponseOBJ(NetDataLRResp result) {
        boolean request_validity = IsDataFormatCorrect(_request);
        if (request_validity == true) {
            result.SetRequest(_request);
            String host_name = _request.GetLocalInetAddress().getHostName();
            int port = _request.GetLocalPort();
            result.SetInetSockAddr(new InetSocketAddress(host_name, port));
            result.SetDataFormatValid(request_validity);
        } else {
            result.SetResultMsg(ResponseMsgList.msg_list[ResponseMsgList.INVALID_IDX]);
            return;
        }
    }

    private void FormUserListResponseOBJ(NetDataUserListResp result) {
        boolean request_validity = IsDataFormatCorrect(_request);
        if (request_validity == true) {
            result.SetRequest(_request);
            String host_name = _request.GetLocalInetAddress().getHostName();
            int port = _request.GetLocalPort();
            result.SetInetSockAddr(new InetSocketAddress(host_name, port));
            result.SetDataFormatValid(request_validity);
        } else {
            _debugger.LogInfo("Data Processor: data NOT valid");
            result.SetResultMsg(ResponseMsgList.msg_list[ResponseMsgList.INVALID_IDX]);
            return;
        }
    }
}
