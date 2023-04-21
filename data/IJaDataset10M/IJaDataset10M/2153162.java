package org.eyewitness.nids.main;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.eyewitness.util.HTMLLogger;

public class SnortSignatureConverter {

    static String globalSignaturePattern = "(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(->|<>|<-)\\s+(\\S+)\\s+(\\S+)(.*)$";

    private List<String> rawRules = new ArrayList<String>();

    private List<String> includeFiles = new ArrayList<String>();

    private Map<String, String> varsList = new HashMap<String, String>();

    private List<SnortRule> rules;

    public SnortSignatureConverter() {
        try {
            log("Initialized", 3);
            initializeConfigVariables();
            init();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void initializeConfigVariables() {
        try {
            String[] lines = removeCommentedLines(readLines("eyewitness.conf"));
            Matcher varMatcher = null;
            for (String line : lines) {
                varMatcher = Pattern.compile("var(.*)\\s+(.*)").matcher(line);
                if (varMatcher.find()) {
                    String varName = varMatcher.group(1).trim();
                    String varValue = varMatcher.group(2).trim();
                    if (varValue.contains("$")) {
                        Matcher m = Pattern.compile("\\$(.+)").matcher(varValue);
                        if (m.find()) {
                            String v = m.group(1).trim();
                            varValue = varsList.get(v);
                        } else varValue = "";
                    }
                    if (!varValue.isEmpty()) varsList.put(varName, varValue);
                }
                varMatcher = Pattern.compile("include_rule_file\\s+(.*)").matcher(line);
                if (varMatcher.find()) {
                    includeFiles.add(varMatcher.group(1).trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            log(e.getMessage(), 0);
        }
    }

    private String setVars(String text) {
        Matcher m = null;
        for (String varName : varsList.keySet()) {
            m = Pattern.compile("(\\$" + varName + ")").matcher(text);
            text = m.replaceAll(varsList.get(varName));
        }
        return text;
    }

    private void init() {
        Map<String, String> refUrls = new HashMap<String, String>();
        refUrls.put("bugtraq", "http://www.securityfocus.com/bid/");
        refUrls.put("cve", "http://cve.mitre.org/cgi-bin/cvename.cgi?name=");
        refUrls.put("arachnids", "http://www.whitehats.com/info/IDS");
        refUrls.put("mcafee", "http://vil.nai.com/vil/dispVirus.asp?virus_k=");
        refUrls.put("url", "http://");
        Pattern pattern = Pattern.compile(globalSignaturePattern);
        setRules(new ArrayList<SnortRule>());
        String buffer = "";
        for (String includeFile : includeFiles) {
            try {
                buffer += setVars(readFile(includeFile));
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        String[] lines = removeCommentedLines(buffer.split("\n"));
        rawRules.addAll(Arrays.asList(lines));
        for (String rule : rawRules) {
            Matcher matcher = pattern.matcher(rule);
            if (matcher.find()) {
                String attack = matcher.group(1);
                String proto = matcher.group(2);
                String src = matcher.group(3);
                String src_port = matcher.group(4);
                String direction = matcher.group(5);
                String dst = matcher.group(6);
                String dst_port = matcher.group(7);
                String rest = matcher.group(8);
                String msg = "";
                String flags = "";
                String dsize = "";
                String classtype = "";
                String depth = "";
                Matcher tempMatcher = Pattern.compile("msg\\s*:\\s*\"([^\"]+)", Pattern.CASE_INSENSITIVE).matcher(rest);
                if (tempMatcher.find()) msg = tempMatcher.group(1);
                tempMatcher = Pattern.compile("flags\\s*:\\s*([^;]+)", Pattern.CASE_INSENSITIVE).matcher(rest);
                if (tempMatcher.find()) flags = tempMatcher.group(1);
                tempMatcher = Pattern.compile("dsize\\s*:\\s*([^;]+)", Pattern.CASE_INSENSITIVE).matcher(rest);
                if (tempMatcher.find()) dsize = tempMatcher.group(1);
                tempMatcher = Pattern.compile("classtype\\s*:\\s*([^;]+)", Pattern.CASE_INSENSITIVE).matcher(rest);
                if (tempMatcher.find()) classtype = tempMatcher.group(1);
                tempMatcher = Pattern.compile("depth\\s*:\\s*([^;]+)", Pattern.CASE_INSENSITIVE).matcher(rest);
                if (tempMatcher.find()) depth = tempMatcher.group(1);
                rest = rest.replaceAll("nocase;", "");
                String contents = String.copyValueOf(rest.toCharArray());
                List<String> contentsStorage = new ArrayList<String>();
                tempMatcher = Pattern.compile("content\\s*:\\s*[!]?\"[^\"]+").matcher(contents);
                if (tempMatcher.find()) {
                    while (contents.contains("content")) {
                        Matcher tempMatcher1 = Pattern.compile("content:\"([^\"]+)\";.*offset:(\\d+);(.*)", Pattern.CASE_INSENSITIVE).matcher(contents);
                        if (tempMatcher1.find()) {
                            contents = tempMatcher1.replaceFirst("$3");
                            String tmp = tempMatcher1.group(1);
                            int offset = Integer.valueOf(tempMatcher1.group(2));
                            String pad = "";
                            for (int i = 0; i < offset; i++) pad += "A";
                            String t = parseContent(pad + tmp);
                            contentsStorage.add(t);
                        } else {
                            tempMatcher1 = Pattern.compile("content\\s*:\\s*[!]?\"([^\"]+)(.*)").matcher(contents);
                            String tempContent = "";
                            if (tempMatcher1.find()) {
                                tempContent = tempMatcher1.group(1);
                                String newStr = tempMatcher1.replaceFirst("$2");
                                if (!newStr.equals(contents)) {
                                    contents = newStr;
                                    contentsStorage.add(parseContent(tempContent));
                                }
                            }
                        }
                    }
                }
                StringBuffer payload = new StringBuffer();
                for (String x : contentsStorage) {
                    payload.append(x);
                }
                List<String> references = new ArrayList<String>();
                String ref = contents;
                Matcher m = Pattern.compile("(.*)reference:([^;]+)(.*)$").matcher(ref);
                while (m.find()) {
                    String tmp = m.group(2);
                    Matcher m1 = Pattern.compile("(\\w+),(.*)", Pattern.CASE_INSENSITIVE).matcher(tmp);
                    if (m1.find()) {
                        tmp = refUrls.get(m1.group(1)) + m1.group(2);
                    }
                    references.add(tmp);
                    ref = m.replaceFirst("$1 $3");
                    m = Pattern.compile("(.*)reference:([^;]+)(.*)$").matcher(ref);
                }
                StringBuffer reference = new StringBuffer();
                for (String x : references) {
                    reference.append(" " + x);
                }
                if (!dsize.isEmpty()) {
                    String dsizeCopy = String.copyValueOf(dsize.toCharArray());
                    m = Pattern.compile("dsize: ").matcher(ref);
                    m.replaceFirst("");
                    m = Pattern.compile("([\\D]{0,1})(\\d+)$").matcher(ref);
                }
                SnortRule newRule = new SnortRule();
                newRule.action = attack;
                newRule.protocol = proto;
                newRule.destIP = dst;
                newRule.destPort = dst_port;
                newRule.sourceIP = src;
                newRule.sourcePort = src_port;
                newRule.flow = direction;
                newRule.msg = msg;
                newRule.flags = flags;
                newRule.depth = depth;
                newRule.dsize = dsize;
                newRule.classtype = classtype;
                newRule.content = payload.toString();
                newRule.reference = reference.toString();
                getRules().add(newRule);
            }
        }
    }

    private String parseContent(String contentArg) {
        String content = String.copyValueOf(contentArg.toCharArray());
        String hex2a = "";
        String end = "";
        Matcher tempMatcher1 = Pattern.compile("([^|]*)(.*)").matcher(content);
        if (tempMatcher1.find()) {
            String pre = tempMatcher1.group(1);
            content = tempMatcher1.replaceFirst("$2");
            if (content.contains("|")) {
                Matcher tempMatcher2 = Pattern.compile("([^|]+)\\|(.*)").matcher(content);
                if (tempMatcher2.find()) {
                    String hex = tempMatcher2.group(1);
                    String post = tempMatcher2.group(2);
                    if (hex == null) hex = "";
                    Matcher tempMatcher3 = Pattern.compile("(\\w{2})(.*)").matcher(hex);
                    while (tempMatcher3.find()) {
                        String tmpVar = tempMatcher3.group(1);
                        hex = tempMatcher3.replaceFirst("$2");
                        int i = Integer.parseInt(tmpVar, 16);
                        char ch = (char) i;
                        hex2a += ch;
                        tempMatcher3 = Pattern.compile("(\\w{2})(.*)").matcher(hex);
                    }
                    if (!post.isEmpty()) {
                        end = parseContent(post);
                    }
                }
            }
            return pre + hex2a + end;
        }
        return "";
    }

    private String[] removeCommentedLines(String[] lines) {
        List<String> result = new ArrayList<String>();
        for (String line : lines) {
            if (!line.matches("^\\s*#(.*)") && !line.isEmpty()) result.add(line);
        }
        return result.toArray(new String[0]);
    }

    private String[] readLines(String filename) throws IOException {
        FileReader reader = null;
        File f = new File(filename);
        reader = new FileReader(f);
        StringBuffer buf = new StringBuffer();
        int oneChar = 0;
        while ((oneChar = reader.read()) > 0) buf.append((char) oneChar);
        return buf.toString().split("\n");
    }

    private String readFile(String filename) throws IOException {
        FileReader reader = null;
        File f = new File(filename);
        reader = new FileReader(f);
        StringBuffer buf = new StringBuffer();
        int oneChar = 0;
        while ((oneChar = reader.read()) > 0) buf.append((char) oneChar);
        return buf.toString();
    }

    /**
	 * @param rules
	 *            the rules to set
	 */
    public void setRules(List<SnortRule> rules) {
        this.rules = rules;
    }

    /**
	 * @return the rules
	 */
    public List<SnortRule> getRules() {
        return rules;
    }

    private void log(String msg, int priority) {
        Logger.getLogger("org.eyewitness.nids").log(Level.INFO, msg);
    }
}
