package com.mychoize.app.cricscore;

import java.util.List;

public class XMLGenerator {

    public static int INT_INVALID_MATCH = -1;

    public static int INT_NO_UPDATE = -2;

    public String getMatchesXML(List<Match> matches) {
        StringBuilder builder = new StringBuilder("<?xml version=\"1.0\"?>\n<li>");
        for (Match match : matches) {
            builder.append("<m><t1>" + match.getTeamOne() + "</t1><t2>" + match.getTeamTwo() + "</t2><id>" + match.getMatchId() + "</id></m>");
        }
        builder.append("</li>");
        String str = builder.toString();
        str = str.replace("&", "&amp;");
        return str;
    }

    public String getScoreXML(SimpleScore score) {
        StringBuilder builder = new StringBuilder("<?xml version=\"1.0\"?>\n<info>");
        if (score.getVersion() == INT_NO_UPDATE) {
            builder.append("<nu/>");
        } else if (score.getVersion() == INT_INVALID_MATCH) {
            builder.append("<im/>");
        } else {
            builder.append("<s>" + score.getSimple() + "</s>");
            builder.append("<d>" + score.getDetail() + "</d>");
            builder.append("<v>" + score.getVersion() + "</v>");
        }
        builder.append("</info>");
        String str = builder.toString();
        str = str.replace("&", "&amp;");
        return str;
    }
}
