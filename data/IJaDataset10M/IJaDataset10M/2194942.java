package com.appspot.ajnweb.swing;

import javax.swing.SwingUtilities;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;
import appengine.util.MakeSyncCallServletDelegate;
import com.google.apphosting.api.ApiProxy;

/**
 * @author shin1ogawa
 */
public class SwingApplication {

    /** アプリケーションのURL */
    public static final String SERVER = "http://2009-11-08a.latest.ajn-web.appspot.com/";

    /** 接続するサーブレットの名称 */
    public static final String SERVLET = "makesynccall";

    static MakeSyncCallServletDelegate delegate;

    /**
	 * main.
	 * @param args
	 * @throws NullPointerException 
	 * @throws URIException 
	 */
    public static void main(String[] args) throws URIException, NullPointerException {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                ApiProxy.setEnvironmentForCurrentThread(new Environment("ajn-web", "swingclient"));
                try {
                    delegate = new MakeSyncCallServletDelegate(new URI(SERVER + SERVLET, true));
                } catch (URIException e) {
                    e.printStackTrace();
                    System.exit(255);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    System.exit(255);
                }
                ApiProxy.setDelegate(delegate);
                new MainFrame().setVisible(true);
            }
        });
    }
}
