package net.jomper.fetchcenter.wc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import net.jomper.fetchcenter.model.TargetPage;

public class MatchesFecther {

    public void handler(Map<String, String> result, TargetPage target) {
        try {
            URL url = new URL(target.getUrl());
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                for (Map.Entry<String, String> entry : result.entrySet()) {
                    if (line.indexOf(target.getInclude()) != -1) {
                        int fromIndex = line.indexOf(target.getFromStr());
                        String r = line.substring(fromIndex + target.getFromStr().length(), line.indexOf(target.getToStr(), fromIndex));
                        entry.setValue(r);
                        line = line.substring(line.indexOf(target.getToStr()) + target.getToStr().length());
                    }
                }
            }
            reader.close();
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }
    }
}
