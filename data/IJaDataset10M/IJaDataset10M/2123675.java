package immf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class StringConverter {

    private static final Log log = LogFactory.getLog(StringConverter.class);

    private Map<String, String> replaceMap;

    public StringConverter() {
        this.replaceMap = new HashMap<String, String>();
    }

    public void load(File f) throws IOException {
        FileReader fr = null;
        BufferedReader br = null;
        try {
            fr = new FileReader(f);
            br = new BufferedReader(fr);
            String line = null;
            int lineCount = 0;
            int keys = 0;
            while ((line = br.readLine()) != null) {
                lineCount++;
                if (line.startsWith("#") || StringUtils.isBlank(line)) {
                    continue;
                }
                String[] vals = line.split("\\s", 2);
                if (vals.length != 2) {
                    log.warn("文字列変換表 " + f.getName() + "(" + lineCount + "行目)が不完全です。");
                    continue;
                }
                String from = vals[0];
                String to = vals[1].replaceAll("^\\s*(\\S+)\\s?.*$", "$1");
                if (from != null && !from.isEmpty() && to != null && !to.isEmpty()) {
                    keys++;
                    this.replaceMap.put(from, to);
                    log.info("StrConv From [" + from + "], to [" + to + "]");
                } else {
                    log.warn("文字列変換表 " + f.getName() + "(" + lineCount + "行目)が不完全です。");
                }
            }
            if (keys > replaceMap.size()) {
                log.warn("文字列変換表 " + f.getName() + " に" + (keys - replaceMap.size() + "個のキー重複があります。"));
            }
        } finally {
            Util.safeclose(br);
            Util.safeclose(fr);
        }
    }

    public String convert(String str) {
        if (this.replaceMap.isEmpty()) {
            return str;
        }
        String s = str;
        for (Object key : this.replaceMap.keySet().toArray()) {
            String from = (String) key;
            String to = this.replaceMap.get(from);
            Matcher m = Pattern.compile(from).matcher(s);
            if (m.find()) {
                log.info("文字列置換を行いました [" + m.group() + "]");
                s = m.replaceAll(to);
            }
        }
        return s;
    }
}
