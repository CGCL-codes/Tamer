package http;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.cookie.CookieSpec;
import org.apache.commons.httpclient.params.DefaultHttpParams;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.RefreshHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;

public class YahooGroup {

    public static void main(String[] args) throws Exception {
        WebClient webClient = new WebClient(BrowserVersion.INTERNET_EXPLORER_6_0);
        DefaultHttpParams.getDefaultParams().setParameter("http.protocol.cookie-policy", CookiePolicy.BROWSER_COMPATIBILITY);
        webClient.setCookiesEnabled(true);
        webClient.setThrowExceptionOnScriptError(false);
        webClient.setRefreshHandler(new RefreshHandler() {

            public void handleRefresh(Page page, URL url, int arg) throws IOException {
                System.out.println("handleRefresh");
            }
        });
        HtmlPage page = (HtmlPage) webClient.getPage("https://login.yahoo.com/config/login?.done=http://groups.yahoo.com%2f&.src=ygrp&.intl=us");
        HtmlForm form = page.getFormByName("login_form");
        form.getInputByName("login").setValueAttribute("mrtestingtesting");
        form.getInputByName("passwd").setValueAttribute("sandeep");
        page = (HtmlPage) form.getInputByValue("Sign In").click();
        for (int i = 1; i <= 1; i++) {
            page = (HtmlPage) webClient.getPage("http://health.groups.yahoo.com/group/phqanda/message/" + i);
            System.out.println(page.asXml());
            List<HtmlDivision> divs = (List<HtmlDivision>) page.getDocumentHtmlElement().getHtmlElementsByTagName("div");
            for (HtmlDivision div : divs) {
                if (div.getAttributeValue("class").equals("doc-layout-body")) {
                    System.out.println(div.asText());
                }
            }
        }
    }
}
