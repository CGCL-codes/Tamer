package project.fantalk.api.sina;

import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import project.fantalk.api.ReturnCode;
import project.fantalk.api.Utils;
import project.fantalk.api.common.oauth.AbstractOAuth;
import project.fantalk.model.Member;

public class SinaServiceOAuth extends AbstractOAuth {

    private static final Logger logger = Logger.getLogger(SinaServiceOAuth.class.getName());

    public SinaServiceOAuth(String username, String password) {
        super(username, password);
    }

    public SinaServiceOAuth(Member member) {
        super(member.getSinaUsername(), member.getSinaPassword());
    }

    @Override
    public String getSNSName() {
        return "sina微博";
    }

    @Override
    public Member processMember(Member member) {
        member.setSinaUsername(getUsername());
        member.setSinaPassword(getPassword());
        member.put();
        return member;
    }

    public ReturnCode update(String text) {
        try {
            String params = "status=" + Utils.encode(text) + "&source=" + Utils.encode(getSource());
            String data = doPost(API.UPDATE_STATUS.url(), params);
            JSONObject o = new JSONObject(data);
            if (o.has("id")) {
                return ReturnCode.ERROR_OK;
            }
        } catch (JSONException e) {
            logger.warning(e.getMessage());
        } catch (RuntimeException e) {
            logger.warning(e.getMessage());
        }
        return ReturnCode.ERROR_FALSE;
    }

    public ReturnCode verify() {
        try {
            String params = "source=" + Utils.encode(getSource());
            String data = doGet(API.VERIFY_ACCOUNT.url(), params);
            JSONObject o = new JSONObject(data);
            if (o.has("id")) {
                return ReturnCode.ERROR_OK;
            }
        } catch (JSONException e) {
        }
        return ReturnCode.ERROR_FALSE;
    }

    public String getSource() {
        return SinaConstant.apiKey;
    }

    /**
	 * 枚举，腾讯微博API方法接口URL列表
	 * 
	 * @author mcxiaoke
	 */
    static enum API {

        UPDATE_STATUS("statuses/update"), VERIFY_ACCOUNT("account/verify_credentials");

        private String value;

        private static final String API_URL = "http://api.t.sina.com.cn/";

        private static final String JSON = ".json";

        API(String value) {
            this.value = value;
        }

        public String url() {
            return new StringBuilder().append(API_URL).append(value).append(JSON).toString();
        }
    }

    @Override
    public String getApiKey() {
        return SinaConstant.apiKey;
    }

    @Override
    public String getApiSecret() {
        return SinaConstant.secret;
    }
}
