package it.polimi.tagonto.mapper.noise;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import it.polimi.tagonto.TagontoException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import uk.ac.shef.wit.simmetrics.similaritymetrics.JaccardSimilarity;
import uk.ac.shef.wit.simmetrics.similaritymetrics.Levenshtein;

public class GoogleNoiseAnalyzer implements INoiseAnalizer {

    private static final String GOOGLE_URL = "http://www.google.com/search?q=";

    private String str = null;

    private boolean isAnalized = false;

    private float noisyness = 0;

    private String correctedStr = null;

    public void analize() throws TagontoException {
        if (this.isAnalized == true) return;
        HttpClient client = new HttpClient();
        client.getHttpConnectionManager().getParams().setConnectionTimeout(1000);
        String url = null;
        try {
            url = GoogleNoiseAnalyzer.GOOGLE_URL + URLEncoder.encode(this.str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            this.noisyness = 0;
            return;
        }
        HttpMethod getMethod = new GetMethod(url);
        getMethod.setFollowRedirects(true);
        String responseBody = null;
        try {
            client.executeMethod(getMethod);
            responseBody = getMethod.getResponseBodyAsString();
        } catch (Exception e) {
            e.printStackTrace();
            this.noisyness = 0;
            return;
        }
        int index1 = responseBody.indexOf("Did you mean: ");
        if (index1 == -1) {
            this.noisyness = 0;
            this.correctedStr = this.str;
        } else {
            int index2 = responseBody.indexOf("<i>", index1) + 3;
            int index3 = responseBody.indexOf("</i>", index2);
            this.correctedStr = responseBody.substring(index2, index3);
            JaccardSimilarity comparer1 = new JaccardSimilarity();
            Levenshtein comparer2 = new Levenshtein();
            this.noisyness = comparer1.getSimilarity(this.correctedStr, this.str) * 0.5f + comparer2.getSimilarity(this.correctedStr, this.str) * 0.5f;
            this.noisyness = 1 - this.noisyness;
        }
        this.isAnalized = true;
    }

    public String correctString() throws TagontoException {
        if (this.isAnalized == false) this.analize();
        return this.correctedStr;
    }

    public float getNoisyness() throws TagontoException {
        if (this.isAnalized == false) this.analize();
        return this.noisyness;
    }

    public void setString(String str) {
        this.str = str;
    }
}
