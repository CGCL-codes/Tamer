package com.fainfy.jdoc2chm;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.fainfy.jdoc2chm.entity.Entry;

/**
 * 编译HTML Help index file。
 * 
 * @author Luffy
 * 
 */
public class HHKCompiler extends AbstractCompiler {

    private static final Pattern INDEXS_REGEX = Pattern.compile("(?is)<dl>(.+?)</dl>");

    private static final Pattern INDEX_LINE_REGEX = Pattern.compile("(?is)<dt><a href=\"(.*?)\".*?>(.*?)</a> - (.*?)<a href=\"(.*?)\".*?>(.*?)</a><dd>");

    private static final Pattern NAME_IS_JDK6_REGEX = Pattern.compile("(?is)<b>(.+?)</b>");

    protected Map<String, Entry> entrys = new LinkedHashMap<String, Entry>();

    @Override
    public void doCompile(File file) {
        doIndex(file.getParent());
        try {
            Collection<Entry> colls = new ArrayList<Entry>();
            for (Entry entry : entrys.values()) {
                if (entry.getChilds().size() <= 1) {
                    entry.setChilds(null);
                }
                colls.add(entry);
            }
            writeHHKFile(file, colls);
        } catch (IOException e) {
            throw new RuntimeException("write index failure", e);
        }
    }

    /**
	 * 
	 * @param dir
	 */
    protected void doIndex(String dir) {
        List<String> contents = null;
        try {
            contents = getIndexFileContent(dir);
        } catch (IOException e) {
            throw new RuntimeException("read index failure", e);
        }
        buildIndex(contents);
    }

    /**
	 * 
	 * @param contents
	 */
    protected void buildIndex(List<String> contents) {
        for (String content : contents) {
            Matcher matcher = getIndexLineRegex().matcher(content);
            while (matcher.find()) {
                String[] params = { matcher.group(1), matcher.group(2), matcher.group(4), matcher.group(3) + matcher.group(5) };
                buildIndexItem(params);
            }
        }
    }

    /**
	 * 
	 * @param params
	 */
    protected void buildIndexItem(String[] params) {
        String local = normalizeLocal(params[0]);
        String name = deleteTag(normalizeName(params[1]));
        String itemLocal = normalizeLocal(params[2]);
        String itemName = deleteTag(normalizeItemName(params[3]));
        Entry item = new Entry();
        item.addParam("Name", itemName);
        item.addParam("Local", itemLocal);
        Entry existIndex = entrys.get(name);
        if (existIndex == null) {
            Entry index = new Entry();
            index.addParam("Name", name);
            index.addParam("Local", local);
            index.addChild(item);
            entrys.put(name, index);
            return;
        }
        if (existIndex.getChilds().size() > 1) {
            existIndex.addChild(item);
            return;
        }
        Entry newIndex = new Entry();
        newIndex.addParam("Name", name);
        newIndex.addParam("See Also", name);
        newIndex.addChild(existIndex.getFirstChild());
        newIndex.addChild(item);
        entrys.put(name, newIndex);
    }

    /**
	 * 获取索引文件的内容。
	 * @param dir 被编译的HTML根目录。
	 * @return
	 * @throws IOException
	 */
    private List<String> getIndexFileContent(String dir) throws IOException {
        List<String> contents = new ArrayList<String>();
        File file = new File(dir, "index-all.html");
        if (file.exists()) {
            contents.add(getDLContent(file));
            return contents;
        }
        file = new File(dir, "index-files");
        File[] files = null;
        if (file.exists() && file.isDirectory()) {
            files = file.listFiles(new FilenameFilter() {

                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(".html");
                }
            });
        }
        if (file == null || file.length() <= 0) {
            return contents;
        }
        for (File file2 : files) {
            contents.add(getDLContent(file2));
        }
        return contents;
    }

    /**
	 * 获取索引文件内的所有内容。
	 * 
	 * @param file
	 *            索引文件
	 * @return 索引文件内容
	 * @throws IOException
	 */
    protected String getDLContent(File file) throws IOException {
        StringBuilder sb = new StringBuilder();
        Matcher matcher = getIndexsRegex().matcher(readFile(file));
        while (matcher.find()) {
            sb.append(matcher.group());
        }
        return sb.toString();
    }

    private void writeHHKFile(File file, Collection<Entry> entrys) throws IOException {
        Writer out = new OutputStreamWriter(new FileOutputStream(file), Charset.defaultCharset());
        BufferedWriter bufOut = new BufferedWriter(out);
        bufOut.write("<HTML>");
        bufOut.newLine();
        bufOut.write("<HEAD>");
        bufOut.newLine();
        bufOut.write("  <!-- HTML Help Index File -->");
        bufOut.newLine();
        bufOut.write("  <!-- Generated: " + new Date() + " with http://code.google.com/p/jdoc2chm/ -->");
        bufOut.newLine();
        bufOut.write("  <!-- www.fainfy.com -->");
        bufOut.newLine();
        bufOut.write("</HEAD>");
        bufOut.newLine();
        bufOut.write("<BODY>");
        bufOut.newLine();
        bufOut.write("<UL>");
        bufOut.newLine();
        for (Entry index : entrys) {
            bufOut.write(String.valueOf(index));
            bufOut.newLine();
        }
        bufOut.write("</UL>");
        bufOut.newLine();
        bufOut.write("</BODY>");
        bufOut.newLine();
        bufOut.write("</HTML>");
        bufOut.flush();
        bufOut.close();
    }

    /**
	 * 规范化索引名称。该方法会去除索引名称前后的空格，并采用{@link this#getIndexNameRegex()}
	 * 方法匹配名称返回匹配到的第一组信息。
	 * 
	 * @param name
	 *            索引名称
	 * @return 规范后的索引名称
	 * @see Matcher#group(int)
	 */
    protected String normalizeName(String name) {
        Matcher matcher = getIndexNameRegex().matcher(name.trim());
        if (matcher.find()) {
            return matcher.group(1);
        }
        return name;
    }

    /**
	 * 规范索引项名称。
	 * @param name 
	 * @return
	 */
    protected String normalizeItemName(String name) {
        StringBuilder sb = new StringBuilder(name.trim());
        if (sb.indexOf("Class ") == 0) {
            sb.delete(0, 6);
        } else if (sb.indexOf("Interface ") == 0) {
            sb.delete(0, 10);
        } else if (sb.indexOf("Enum ") == 0) {
            sb.delete(0, 5);
        } else if (sb.indexOf("Exception ") == 0) {
            sb.delete(0, 10);
        } else if (sb.indexOf("Constructor ") == 0) {
            sb.delete(0, 12);
        } else if (sb.indexOf("Method ") == 0) {
            sb.delete(0, 7);
        } else if (sb.indexOf("Static method ") == 0) {
            sb.delete(0, 14);
        } else if (sb.indexOf("Variable ") == 0) {
            sb.delete(0, 9);
        } else if (sb.indexOf("Static variable ") == 0) {
            sb.delete(0, 16);
        }
        int i_c = sb.indexOf(" class");
        int i_i = sb.indexOf(" interface");
        if (i_c >= 0) {
            sb.delete(i_c, i_c + 6);
        }
        if (i_i >= 0) {
            sb.delete(i_i, i_i + 10);
        }
        return sb.toString();
    }

    protected Pattern getIndexsRegex() {
        return INDEXS_REGEX;
    }

    protected Pattern getIndexLineRegex() {
        return INDEX_LINE_REGEX;
    }

    protected Pattern getIndexNameRegex() {
        return NAME_IS_JDK6_REGEX;
    }
}
