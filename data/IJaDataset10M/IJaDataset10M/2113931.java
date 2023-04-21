package learning;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.Test;
import au.id.jericho.lib.html.Attribute;
import au.id.jericho.lib.html.Attributes;
import au.id.jericho.lib.html.Element;
import au.id.jericho.lib.html.OutputDocument;
import au.id.jericho.lib.html.Source;
import au.id.jericho.lib.html.StartTag;
import au.id.jericho.lib.html.Tag;

public class LearningHttpConnection {

    @Test
    public void learningURL() throws Exception {
        URL url = new URL(new URL("http://d.hatena.ne.jp/kompiro/20071125/1195993886"), "/js/embed_movie_player.js");
        assertEquals("http://d.hatena.ne.jp/js/embed_movie_player.js", url.toString());
    }

    private String email = "*****";

    private String passwd = "*****";

    private String messageId = "*****";

    @Test
    public void learningGmailURL() throws Exception {
        CookieManager manager = new CookieManager();
        manager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(manager);
        URL url = new URL("https://www.google.com/accounts/ServiceLogin?service=mail&passive=true&rm=false&continue=http%3A%2F%2Fmail.google.com%2Fmail%2F%3Fui%3Dhtml%26zy%3Dl&ltmpl=default&ltmplcache=2");
        Source source = new Source(url);
        List forms = source.findAllElements(Tag.FORM);
        for (Iterator itr = forms.iterator(); itr.hasNext(); ) {
            Element form = (Element) itr.next();
            if ("gaia_loginform".equals(form.getAttributeValue("id"))) {
                authenticationToGmail(form);
            }
        }
        String script = makeCookies(manager);
        url = new URL(messageId);
        showBrowser(url, script);
    }

    private void authenticationToGmail(Element form) throws MalformedURLException, IOException, ProtocolException {
        URL action = new URL(form.getAttributeValue("action"));
        HttpURLConnection con = (HttpURLConnection) action.openConnection();
        con.setRequestMethod("POST");
        List tags = form.findAllElements(Tag.INPUT);
        StringBuilder builder = new StringBuilder("Email=" + email + "&Passwd=" + passwd);
        for (Iterator iitr = tags.iterator(); iitr.hasNext(); ) {
            Element tag = (Element) iitr.next();
            if ("hidden".equals(tag.getAttributeValue("type"))) {
                String name = tag.getAttributeValue("name");
                String value = tag.getAttributeValue("value");
                builder.append("&");
                builder.append(name);
                builder.append("=");
                builder.append(value);
            }
        }
        con.setDoOutput(true);
        DataOutputStream outStream = new DataOutputStream(con.getOutputStream());
        String params = builder.toString();
        byte[] bytes = params.getBytes();
        outStream.write(bytes, 0, bytes.length);
        con.connect();
        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String field = null;
        while ((field = br.readLine()) != null) {
        }
        con.disconnect();
    }

    private String makeCookies(CookieManager manager) {
        CookieStore store = manager.getCookieStore();
        List<HttpCookie> cookies = store.getCookies();
        StringBuilder scriptBuilder = new StringBuilder();
        for (int i = 0; i < cookies.size(); i++) {
            HttpCookie cookie = cookies.get(i);
            scriptBuilder.append(createCookieScript(cookie));
        }
        String script = scriptBuilder.toString();
        return script;
    }

    private String createCookieScript(HttpCookie cookie) {
        StringBuilder builder = new StringBuilder("document.cookie=\"");
        builder.append(cookie.getName());
        builder.append("=");
        builder.append(cookie.getValue());
        builder.append(";");
        String domain = cookie.getDomain();
        if (domain != null) {
            builder.append("domain=");
            builder.append(domain);
            builder.append(";");
        }
        builder.append("\"\n");
        return builder.toString();
    }

    @Test
    public void learnURL() throws Exception {
        String target = "document.write('<frameset onload=lj() rows=\"100%,*\" border=0><frame name=main src=\"?ui=1&amp;view=page&amp;name=loading&amp;ver=1h3rrn0ve8sso\" frameborder=0 noresize scrolling=no><frame name=js src=\"?ui=1&amp;view=page&amp;name=loading&amp;ver=1h3rrn0ve8sso\" frameborder=0 noresize></frameset>');";
        Pattern p = Pattern.compile("(.*src=\")(.*)(src=\")(.*)");
        Matcher m = p.matcher(target);
        assertTrue(m.matches());
        System.out.println(m.group(1));
        System.out.println(m.group(2));
        System.out.println(m.group(3));
        System.out.println(m.group(4));
    }

    @Test
    public void learnSetEncoding() throws Exception {
        URL sourceUrl = new URL("http://akiyah.bglb.jp/blog/");
        Source source = new Source(sourceUrl);
        OutputDocument document = new OutputDocument(source);
        replaceContentEncoding(source, document);
        List allTags = source.findAllStartTags();
        for (Iterator itr = allTags.iterator(); itr.hasNext(); ) {
            StartTag tag = (StartTag) itr.next();
            replaceExternalForm(document, tag, sourceUrl, "href");
            replaceExternalForm(document, tag, sourceUrl, "src");
        }
        StringWriter stringWriter = new StringWriter();
        document.writeTo(stringWriter);
        String html = stringWriter.toString();
        showBrowser(html);
    }

    private void replaceExternalForm(OutputDocument document, StartTag tag, URL sourceUrl, final String attributeName) throws MalformedURLException {
        String name = tag.getAttributeValue(attributeName);
        if (name != null) {
            final URL absoluteURL = new URL(sourceUrl, name);
            replace(document, tag, new ReplaceContext() {

                public void doReplace(Map<String, String> map, String name, String value) {
                    if (attributeName.equals(name)) {
                        map.put(name, absoluteURL.toExternalForm());
                    } else {
                        map.put(name, value);
                    }
                }
            });
        }
    }

    private void replaceContentEncoding(Source source, OutputDocument document) {
        StartTag contentTypeMetaTag = source.findNextStartTag(0, "http-equiv", "Content-Type", false);
        if (contentTypeMetaTag != null) {
            replace(document, contentTypeMetaTag, new ReplaceContext() {

                public void doReplace(Map<String, String> map, String name, String value) {
                    if ("content".equals(name)) {
                        map.put(name, "utf-8");
                    } else {
                        map.put(name, value);
                    }
                }
            });
        }
    }

    private void replace(OutputDocument document, StartTag tag, ReplaceContext context) {
        Attributes attributes = tag.getAttributes();
        Map<String, String> map = new HashMap<String, String>();
        for (Iterator iterator = attributes.iterator(); iterator.hasNext(); ) {
            Attribute atr = (Attribute) iterator.next();
            String name = atr.getName();
            String value = atr.getValue();
            context.doReplace(map, name, value);
        }
        document.replace(attributes, map);
    }

    private void showBrowser(String html) {
        showBrowser(null, html, null);
    }

    private void showBrowser(URL url, String script) {
        showBrowser(url, null, script);
    }

    private void showBrowser(URL url, String html, String script) {
        if (url == null && html == null) {
            throw new NullPointerException("url or html, which one is needed.");
        }
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setLayout(new FillLayout());
        Browser browser = new Browser(shell, SWT.NONE);
        if (script != null) {
            browser.execute(script);
        }
        if (url != null) {
            browser.setUrl(url.toString());
        }
        if (html != null) {
            browser.setText(html);
        }
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
        display.dispose();
    }

    private interface ReplaceContext {

        public void doReplace(Map<String, String> map, String name, String value);
    }
}
