package net.narusas.daumaccess;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchBuilder {

    String sec;

    String kw;

    String ks;

    String sss;

    int uco = 0;

    String uda2;

    String URL = "http://log.search.daum.net/cgi-bin/";

    private String u;

    private final String searchURL;

    private final String siteURL;

    private final DaumPageFetcher fetcher;

    private final boolean useAccount;

    private final int startAccount;

    private final int endAccount;

    String openWin(String u1, String u2) throws IOException {
        String url = u1 + "&uco=" + (++uco) + u2;
        uda2 = null;
        return url;
    }

    void init(String s, String keyw, String keys, String ss) {
        sec = s;
        int index = keyw.indexOf("%2F");
        if (index > 0) {
            try {
                kw = URLDecoder.decode(keyw, "euc-kr");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            kw = keyw;
        }
        ks = keys;
        sss = ss;
    }

    public String cU1(String a, String scope, String r, String p, String ext) {
        return URL + "s=" + sec + "&a=" + a + scope + "&q=" + kw + "&k=" + ks + "&r=" + r + "&p=" + p + "&ext=" + ext;
    }

    public String cU2(String o, String u1, String u2, String u3) {
        String u = "NONE";
        if (o != null) u = o;
        if (uda2 != null) u2 = uda2;
        return "&usr1=" + u1 + "&usr2=" + u2 + "&usr3=" + u3 + "&t=D&u=" + u;
    }

    public String cU3(String u, String u1, String u2, String u3) {
        return "&usr1=" + u1 + "&usr2=" + u2 + "&usr3=" + u3 + "&t=D&u=" + u;
    }

    public String gLink(String o, String a, String r, String p) throws IOException {
        String scope = "&ss=" + sss + "&as=";
        String u1 = cU1(a, scope, r, p, "");
        String u2 = cU2(o, "", "", "");
        return openWin(u1, u2);
    }

    public String gExtraLink(String o, String a, String ext) throws IOException {
        String scope = "&ss=" + sss + "&as=";
        if (o != null) u = o;
        String u1 = cU1(a, scope, "", "", ext);
        String u2 = cU2(o, "", "", "");
        return openWin(u1, u2);
    }

    public String gCRLink(String u, String a, String r, String p) throws IOException {
        String scope = "&ss=" + sss + "&as=";
        String u1 = cU1(a, scope, r, p, "");
        String u2 = cU3(u, "", "", "");
        return openWin(u1, u2);
    }

    public String gCRExtraLink(String u, String a, String ext) throws IOException {
        String scope = "&ss=" + sss + "&as=";
        String u1 = cU1(a, scope, "", "", ext);
        String u2 = cU3(u, "", "", "");
        return openWin(u1, u2);
    }

    public String gUSR1Link(String o, String a, String r, String p, String usr1) throws IOException {
        String scope = "&ss=" + sss + "&as=";
        String u1 = cU1(a, scope, r, p, "");
        String u2 = cU2(o, usr1, "", "");
        return openWin(u1, u2);
    }

    public String gUSR2Link(String o, String a, String r, String p, String usr2) throws IOException {
        String scope = "&ss=" + sss + "&as=";
        String u1 = cU1(a, scope, r, p, "");
        String u2 = cU2(o, "", usr2, "");
        return openWin(u1, u2);
    }

    public String gUSR12Link(String o, String a, String r, String p, String usr1, String usr2) throws IOException {
        String scope = "&ss=" + sss + "&as=";
        String u1 = cU1(a, scope, r, p, "");
        String u2 = cU2(o, usr1, usr2, "");
        return openWin(u1, u2);
    }

    public String gUSR123Link(String o, String a, String r, String p, String usr1, String usr2, String usr3) throws IOException {
        String scope = "&ss=" + sss + "&as=";
        String u1 = cU1(a, scope, r, p, "");
        String u2 = cU2(o, usr1, usr2, usr3);
        return openWin(u1, u2);
    }

    public String gAsLink(String o, String a, String r, String p, String as) throws IOException {
        String scope = "&ss=" + sss + "&as=" + as;
        String u1 = cU1(a, scope, r, p, "");
        String u2 = cU2(o, "", "", "");
        return openWin(u1, u2);
    }

    public String gAsUSR1Link(String o, String a, String r, String p, String as, String usr1) throws IOException {
        System.out.println("#####");
        String scope = "&ss=" + sss + "&as=" + as;
        String u1 = cU1(a, scope, r, p, "");
        String u2 = cU2(o, usr1, "", "");
        return openWin(u1, u2);
    }

    public String gAsUSR12Link(String o, String a, String r, String p, String as, String usr1, String usr2) throws IOException {
        String scope = "&ss=" + sss + "&as=" + as;
        String u1 = cU1(a, scope, r, p, "");
        String u2 = cU2(o, usr1, usr2, "");
        return openWin(u1, u2);
    }

    public String gAsUSR123Link(String o, String a, String r, String p, String as, String usr1, String usr2, String usr3) throws IOException {
        String scope = "&ss=" + sss + "&as=" + as;
        String u1 = cU1(a, scope, r, p, "");
        String u2 = cU2(o, usr1, usr2, usr3);
        return openWin(u1, u2);
    }

    public String gAsUSR123Link(String o, String a, String r, String p, String as, String usr1, String usr2) throws IOException {
        String scope = "&ss=" + sss + "&as=" + as;
        String u1 = cU1(a, scope, r, p, "");
        String u2 = cU2(o, usr1, usr2, "");
        return openWin(u1, u2);
    }

    public SearchBuilder(String searchURL, String siteURL, DaumPageFetcher fetcher, boolean useAccount, int startAccount, int endAccount) {
        this.searchURL = searchURL;
        this.siteURL = siteURL;
        this.fetcher = fetcher;
        this.useAccount = useAccount;
        this.startAccount = startAccount;
        this.endAccount = endAccount;
    }

    public Search build() throws IOException {
        String src = fetcher.fetch(searchURL);
        Pattern p1 = Pattern.compile("init\\(\"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\"\\);");
        Matcher m = p1.matcher(src);
        m.find();
        init(m.group(1), m.group(2), m.group(3), m.group(4));
        int pos = src.indexOf(siteURL);
        Pattern sp = Pattern.compile("onclick='return ([^\\(]+[^;]+)", Pattern.CASE_INSENSITIVE);
        int end = m.end();
        m = sp.matcher(src);
        m.find(pos);
        src = m.group(1);
        System.out.println(src);
        Pattern p = Pattern.compile("([^\\(]+)", Pattern.CASE_INSENSITIVE);
        m = p.matcher(src);
        m.find();
        String method = m.group(1);
        Pattern p2 = Pattern.compile("\"([^\"]*)\"");
        m = p2.matcher(src);
        LinkedList<String> params = new LinkedList<String>();
        while (m.find()) {
            params.add(m.group(1));
            end = m.end();
        }
        String logURL = null;
        System.out.println(method + ":" + params.size());
        Method[] ms = getClass().getMethods();
        for (Method method2 : ms) {
            System.out.println("#" + method2.getName());
            if (method.equals(method2.getName())) {
                Object[] param = new Object[params.size()];
                for (int i = 0; i < params.size(); i++) {
                    param[i] = params.get(i);
                }
                try {
                    logURL = (String) method2.invoke(this, param);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return new Search(searchURL, siteURL, useAccount, logURL, startAccount, endAccount);
    }

    private String parsegAsUSR1Link(Matcher m) throws IOException {
        String res;
        String method = m.group(1);
        String o = siteURL;
        String a = m.group(2);
        String r = m.group(3);
        String p = m.group(4);
        String as = m.group(5);
        String usr1 = m.group(6);
        res = gAsUSR1Link(o, a, r, p, as, usr1);
        return res;
    }
}
