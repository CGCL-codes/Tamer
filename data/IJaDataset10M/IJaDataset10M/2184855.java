package br.com.marciotoshio.wave;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import org.json.JSONException;
import org.json.JSONObject;
import com.google.wave.api.*;

@SuppressWarnings("serial")
public class LanguageBot2Servlet extends AbstractRobotServlet {

    private String _welcomeMessage = "Hi i'm Liz and i'll translate everything you say. Create a blip and hit done! To see a list of languages available for translation go to http://translate.google.com. To define a destination language use /to=[language]";

    private String _sourceText = null;

    private String _fromLang;

    private String _toLang;

    private boolean _auto = true;

    private boolean _translate = false;

    private static Languages _langs = new Languages();

    @Override
    public void processEvents(RobotMessageBundle bundle) {
        Wavelet wavelet = bundle.getWavelet();
        try {
            if (bundle.wasSelfAdded()) {
                AppendBlip(wavelet, _welcomeMessage);
            }
            for (Event event : bundle.getEvents()) {
                if (event.getType() == EventType.BLIP_SUBMITTED) {
                    Blip sourceBlip = event.getBlip();
                    TextView sourceTextView = sourceBlip.getDocument();
                    _sourceText = sourceTextView.getText();
                    DefineRandomLanguage();
                    Pattern pattern = Pattern.compile("/([a-zA-z]+=[a-zA-Z\\-]+)");
                    Matcher matcher = pattern.matcher(_sourceText);
                    ProccesMatch(matcher);
                    if (_fromLang == null || _fromLang.isEmpty()) {
                        DetectLanguage();
                    }
                    if (_auto || _translate) {
                        String translatedText = TranslateText();
                        AppendBlip(wavelet, translatedText);
                        _translate = false;
                    }
                }
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    private void AppendBlip(Wavelet wavelet, String text) {
        Blip blip = wavelet.appendBlip();
        TextView textView = blip.getDocument();
        textView.append(text);
    }

    private void DefineRandomLanguage() {
        _toLang = _langs.GetRandomLanguage();
    }

    private void ProccesMatch(Matcher matcher) {
        while (matcher.find()) {
            for (int i = 0; i < matcher.groupCount(); i++) {
                String match = matcher.group(i);
                if (match.contains("/translate=")) {
                    _translate = Boolean.parseBoolean(GetParameterValue(matcher));
                }
                if (match.contains("/auto=")) {
                    _auto = Boolean.parseBoolean(GetParameterValue(matcher));
                }
                if (match.contains("/to=")) {
                    _toLang = GetParameterValue(matcher);
                }
                if (match.contains("/from=")) {
                    _fromLang = GetParameterValue(matcher);
                } else {
                    DetectLanguage();
                }
                _sourceText = matcher.replaceAll("").trim();
            }
        }
    }

    private String GetParameterValue(Matcher matcher) {
        String language = matcher.group(0);
        language = language.substring(language.indexOf("=") + 1);
        if (_langs.IsLanguageValid(language)) {
            return language;
        } else {
            return _langs.GetRandomLanguage();
        }
    }

    private void DetectLanguage() {
        URL url = GetUrl(RequestType.Detect);
        String response = GetResponse(url);
        _fromLang = GetDetectedText(response);
    }

    private String TranslateText() {
        URL url = GetUrl(RequestType.Translate);
        String response = GetResponse(url);
        String translatedText = GetTranslatedText(response);
        return translatedText;
    }

    private URL GetUrl(RequestType type) {
        String sourceTextEncoded = null;
        try {
            sourceTextEncoded = URLEncoder.encode(_sourceText.trim(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            System.out.println(e.getMessage());
        }
        String urlString = null;
        if (type == RequestType.Translate) {
            urlString = "http://ajax.googleapis.com/ajax/services/language/translate?v=1.0&q=" + sourceTextEncoded + "&langpair=" + _fromLang + "%7C" + _toLang;
        } else {
            urlString = "http://ajax.googleapis.com/ajax/services/language/detect?v=1.0&q=" + sourceTextEncoded;
        }
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    private String GetResponse(URL url) {
        String content = null;
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(false);
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line = br.readLine()) != null) content += line;
            } else {
            }
        } catch (MalformedURLException e) {
            e.getStackTrace();
        } catch (IOException e) {
            e.getStackTrace();
        }
        return content;
    }

    private String GetTranslatedText(String response) {
        response = response.substring(4);
        String translated = null;
        try {
            JSONObject jObj = new JSONObject(response);
            translated = jObj.getJSONObject("responseData").getString("translatedText");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return translated;
    }

    private String GetDetectedText(String response) {
        response = response.substring(4);
        String detected = null;
        try {
            JSONObject jObj = new JSONObject(response);
            detected = jObj.getJSONObject("responseData").getString("language");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return detected;
    }

    private enum RequestType {

        Translate, Detect
    }
}
