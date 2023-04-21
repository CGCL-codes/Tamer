package org.wd.extractor.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class TrainListService {

    Pattern pattern = Pattern.compile("NAME=\"lccp_trnname\"[\\s]+VALUE=\"([\\d]+)\"");

    public List<String> process() {
        List<String> list = new ArrayList<String>();
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet("http://www.indianrail.gov.in/mail_express_trn_list.html");
            HttpResponse response = httpclient.execute(httpget);
            InputStream is = response.getEntity().getContent();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while ((line = br.readLine()) != null) {
                parse(line, list);
                if (list.size() == 10) break;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private void parse(String line, List<String> list) {
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            list.add(matcher.group(1));
        }
    }
}
