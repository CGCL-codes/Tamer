package cn.ourpk.bbs.robot.core.internal;

import java.io.IOException;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import cn.ourpk.bbs.robot.core.internal.SiteDescriptor.PageDescriptor;

public class PostPageImpl extends PageImpl {

    public PostPageImpl(SiteImpl site, PageDescriptor desc) {
        super(site, desc);
    }

    @Override
    public boolean execute(String... arguments) throws HttpException, IOException {
        boolean rt = super.execute(arguments);
        if (rt) {
            String[] properties = pageDesc.getProperties();
            NameValuePair[] params = new NameValuePair[properties.length];
            for (int i = 0; i < params.length; i++) {
                params[i] = new NameValuePair(properties[i], arguments[i]);
            }
            HttpMethod method = site.getPostMethod(pageDesc.getUrl(), params);
            site.executeMethod(method);
        }
        return rt;
    }
}
