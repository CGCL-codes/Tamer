package dsrwebserver.pages.androidcomm;

import java.util.HashMap;
import dsr.comms.CommFuncs;
import dsrwebserver.pages.AbstractPage;
import dsrwebserver.pages.appletcomm.MiscCommsPage;
import dsrwebserver.tables.LoginsTable;

public abstract class AbstractAndroidPageWithHashmap extends AbstractPage {

    protected StringBuffer body = new StringBuffer();

    protected LoginsTable this_user;

    protected String login, pwd;

    public AbstractAndroidPageWithHashmap() {
        super();
    }

    @Override
    public void process() throws Exception {
        String post_raw = this.headers.getPostValueAsString("post");
        String post_b64_decoded = CommFuncs.Decode(post_raw);
        String post_a[] = post_b64_decoded.split(MiscCommsPage.SEP);
        if (post_a.length > 0) {
            HashMap<String, String> hashmap = MiscCommsPage.ExtractParams(post_a[0]);
            login = hashmap.get("android_login");
            pwd = hashmap.get("android_pwd");
            this_user = new LoginsTable(dbs);
            if (this_user.selectUser(login, pwd)) {
                subprocess(hashmap);
                this.content_length = body.length();
            } else {
                body.append("Invalid credentials: " + login + "/" + pwd);
            }
        }
    }

    public abstract void subprocess(HashMap<String, String> hashmap) throws Exception;

    @Override
    protected void writeContent() throws Exception {
        super.writeString(body.toString());
    }
}
