package org.osee.indexer.abstractMining;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TitleMining {

    private String title;

    public String getTitle() {
        return title;
    }

    public TitleMining(String content) {
        title = "�ޱ���";
        String patternString = "<[tT][iI][tT][lL][eE]>";
        Pattern pattern = Pattern.compile(patternString);
        for (int i = 0; i + 7 <= content.length(); i++) {
            Matcher matcher = pattern.matcher(content.substring(i, i + 7));
            if (matcher.find()) {
                int a = i + 7;
                int b = content.indexOf("</", a);
                if (b > a) {
                    title = content.substring(a, b);
                }
                break;
            }
        }
    }
}
