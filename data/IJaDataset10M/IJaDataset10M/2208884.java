package giangnh.download.http;

import giangnh.download.util.RegEx;
import java.util.List;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;

public class HttpGet extends AbsHttp {

    public HttpGet(String url) {
        this.url = url;
    }

    public List<String> getAll(String regex, int group) {
        String content = get();
        System.out.println(content);
        return new RegEx(content).getAll(regex, group);
    }

    @Override
    protected HttpMethod getMethod() {
        return new GetMethod(url);
    }
}
