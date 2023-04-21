package gov.lanl.locator;

import gov.lanl.identifier.sru.SRUDC;
import gov.lanl.identifier.sru.SRUException;
import gov.lanl.identifier.sru.SRUSearchRetrieveResponse;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.StringTokenizer;

public class IdLocatorProxy {

    private URL baseurl;

    public IdLocatorProxy(URL baseurl) {
        if (baseurl == null) throw new NullPointerException("empty baseurl");
        this.baseurl = baseurl;
    }

    public ArrayList<IdLocation> get(String identifier) throws IdLocatorException {
        return doGet(identifier);
    }

    private ArrayList<IdLocation> doGet(String identifier) throws IdLocatorException {
        String openurl = baseurl.toString() + "?url_ver=Z39.88-2004&rft_id=" + identifier;
        URL url;
        SRUSearchRetrieveResponse sru;
        try {
            url = new URL(openurl);
            HttpURLConnection huc = (HttpURLConnection) (url.openConnection());
            int code = huc.getResponseCode();
            if (code == 200) {
                sru = SRUSearchRetrieveResponse.read(huc.getInputStream());
            } else throw new IdLocatorException("cannot get " + url.toString());
        } catch (MalformedURLException e) {
            throw new IdLocatorException("A MalformedURLException occurred for " + openurl);
        } catch (IOException e) {
            throw new IdLocatorException("An IOException occurred attempting to connect to " + openurl);
        } catch (SRUException e) {
            throw new IdLocatorException("An SRUException occurred attempting to parse the response");
        }
        ArrayList<IdLocation> ids = new ArrayList<IdLocation>();
        for (SRUDC dc : sru.getRecords()) {
            IdLocation id = new IdLocation();
            id.setId(dc.getKeys(SRUDC.Key.IDENTIFIER).firstElement());
            id.setRepo(dc.getKeys(SRUDC.Key.SOURCE).firstElement());
            id.setDate(dc.getKeys(SRUDC.Key.DATE).firstElement());
            ids.add(id);
        }
        Collections.sort(ids);
        return ids;
    }

    public static void main(String[] args) {
        try {
            IdLocatorProxy proxy = new IdLocatorProxy(new URL(args[0]));
            ArrayList<IdLocation> ids = new ArrayList<IdLocation>();
            StringTokenizer st = new StringTokenizer(args[1], ",");
            while (st.hasMoreTokens()) ids.addAll(proxy.get(st.nextToken()));
            for (IdLocation loc : ids) {
                System.out.println(loc.getId() + "," + loc.getRepo() + "," + loc.getDate());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
