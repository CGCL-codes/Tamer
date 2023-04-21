package com.sin.createcrcontext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.logging.Logger;
import org.apache.commons.lang3.text.WordUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.sin.domains.KeyWord;

public class CreateCrContextImpl implements CreateCrContext {

    private static final Logger log = Logger.getLogger(CreateCrContextImpl.class.getName());

    private String title;

    private ArrayList<KeyWord> keywordsArr;

    private JSONArray array;

    private JSONObject keyworsdsJSONObject;

    private JSONObject path0JSONObject;

    private JSONObject path1JSONObject;

    private JSONObject domainJSONObject;

    private String host;

    private String endHtml;

    private KeyWord weblinkClass;

    private String path0;

    private String path1;

    private JSONArray domainJSONArray;

    private String description_content;

    private StringBuffer sb;

    private KeyWord keyword0Class;

    private KeyWord keyword1Class;

    private KeyWord keyword2Class;

    private KeyWord keyword3Class;

    private KeyWord keyword4Class;

    @Override
    public String makeFrom(String domain, String pathinfo, String jsonStr) {
        log.info("Start CreateCrContextImpl domain " + domain + " pathinfo " + pathinfo);
        String result = jsonStr;
        sb = new StringBuffer();
        keywordsArr = new ArrayList<KeyWord>();
        String[] shot_domain_array = domain.split("\\.");
        host = shot_domain_array[0];
        String[] splitDomainArr = domain.split("\\.");
        if (splitDomainArr.length > 0) {
            title = splitDomainArr[1];
        }
        String[] pathSplit = pathinfo.split("\\/");
        if (pathSplit.length == 1) {
            path0 = pathSplit[0];
        }
        if (pathSplit.length == 2) {
            path0 = pathSplit[1];
            title = path0.split("\\.")[0];
        }
        if (pathSplit.length == 3) {
            path0 = pathSplit[1] + "/" + pathSplit[2];
            title = pathSplit[2].split("\\.")[0];
        }
        try {
            array = new JSONArray(result);
            for (int i = 0; i < array.length(); i++) {
                JSONObject jSONObject = (JSONObject) array.get(i);
                if (jSONObject.has("keywords")) {
                    keyworsdsJSONObject = jSONObject;
                } else if (jSONObject.has(path0)) {
                    path0JSONObject = jSONObject;
                } else if (jSONObject.has(host)) {
                    domainJSONObject = jSONObject;
                }
            }
        } catch (JSONException e) {
            log.severe("1 exception " + e.getMessage());
        }
        if (keyworsdsJSONObject != null) {
            try {
                JSONArray keywordsJSONArray = new JSONArray(keyworsdsJSONObject.getString("keywords").toString());
                Random rd = new Random();
                int rdweb = rd.nextInt(keywordsJSONArray.length());
                for (int k = 0; k < keywordsJSONArray.length(); k++) {
                    JSONObject record = (JSONObject) keywordsJSONArray.get(k);
                    String keyword = record.getString("keywords");
                    int rating = Integer.parseInt(record.getString("rating"));
                    String weblink = null;
                    if (record.has("weblink")) {
                        weblink = record.getString("weblink");
                        if (k == rdweb) {
                            weblinkClass = new KeyWord(keyword, rating, weblink);
                        }
                    }
                    KeyWord keyWord = new KeyWord(keyword, rating, weblink);
                    keywordsArr.add(keyWord);
                }
            } catch (JSONException e) {
                log.severe("2 exception " + e.getMessage());
            }
            Collections.shuffle(keywordsArr);
            keyword0Class = keywordsArr.get(0);
            keyword1Class = keywordsArr.get(1);
            keyword2Class = keywordsArr.get(2);
            keyword3Class = keywordsArr.get(3);
            keyword4Class = keywordsArr.get(4);
            Random rand = new Random();
            int randI2 = rand.nextInt(2);
            if (randI2 == 0) {
                endHtml = ".html";
            }
            if (randI2 == 1) {
                endHtml = ".jsp";
            }
        }
        if (domainJSONObject != null) {
            try {
                domainJSONArray = new JSONArray(domainJSONObject.getString(host).toString());
            } catch (JSONException e) {
                log.severe("3 exception " + e.getMessage());
            }
        }
        description_content = "";
        sb.append("<!doctype html>");
        sb.append("<html><head>");
        sb.append("<meta http-equiv='content-type' content='text/html; charset=UTF-8'>");
        sb.append("<title>" + WordUtils.capitalize(title) + "</title>");
        sb.append("<meta name='description' content='" + title.toUpperCase() + " " + description_content + "'/>");
        sb.append("</head>");
        sb.append("<body>");
        if (keyworsdsJSONObject != null) {
            sb.append("<p><a href=/" + keyword1Class.getKeyword() + endHtml + "><b><i>" + WordUtils.capitalize(keyword1Class.getKeyword()) + "</i></b></a></p>");
            sb.append("<p><a href=/" + keyword2Class.getKeyword() + "/" + keyword3Class.getKeyword() + endHtml + ">" + WordUtils.capitalize(keyword2Class.getKeyword() + " " + keyword3Class.getKeyword()) + "</a></p>");
        }
        if (path0JSONObject != null && path0 != null) {
            String h0_title;
            if (path0.split("\\.").length == 0) {
                h0_title = path0.replace("/", " ");
            } else {
                h0_title = path0.split("\\.")[0].replace("/", " ");
            }
            sb.append("<h1>" + WordUtils.capitalize(h0_title) + ".</h1>");
            JSONArray path0JSONArray = null;
            try {
                path0JSONArray = new JSONArray(path0JSONObject.getString(path0));
            } catch (JSONException e) {
                log.severe("4 exception " + e.getMessage());
            }
            try {
                for (int k = 0; k < path0JSONArray.length(); k++) {
                    sb.append(path0JSONArray.get(k).toString() + " ");
                }
            } catch (Exception e) {
                log.severe("5 exception " + e.getMessage());
            }
            if (weblinkClass != null) {
                sb.append("<p><a href=http://www." + weblinkClass.getWeblink() + "/>" + WordUtils.capitalize(weblinkClass.getKeyword()) + " " + keyword4Class.getKeyword() + "</a></p>");
            }
            sb.append("</body></html>");
            return sb.toString().replaceAll(".,", ". ");
        }
        if (domainJSONArray != null && host != null) {
            if (keyworsdsJSONObject != null) {
                if (host.equals("www")) {
                    sb.append("<h1>" + splitDomainArr[1].toUpperCase() + " " + WordUtils.capitalize(keyword0Class.getKeyword()) + ".</h1>");
                } else {
                    sb.append("<h1>" + splitDomainArr[1].toUpperCase() + " " + WordUtils.capitalize(host) + " " + WordUtils.capitalize(keyword0Class.getKeyword()) + ".</h1>");
                }
            }
            for (int k = 0; k < domainJSONArray.length(); k++) {
                try {
                    sb.append(domainJSONArray.get(k).toString() + " ");
                } catch (JSONException e) {
                    log.severe("4 exception " + e.getMessage());
                }
            }
            if (weblinkClass != null) {
                sb.append("<p><a href=http://www." + weblinkClass.getWeblink() + "/>" + WordUtils.capitalize(weblinkClass.getKeyword()) + " " + keyword4Class.getKeyword() + "</a></p>");
            }
            sb.append("</body></html>");
            return sb.toString().replaceAll(".,", ". ");
        } else {
            log.warning("Check domainJSONArray host");
        }
        return null;
    }
}
