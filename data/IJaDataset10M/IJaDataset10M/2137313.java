package applet.command;

import java.io.File;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.NumberUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import applet.Command;
import applet.common.Constants;

public class Dir implements Command {

    private static final int RIGHT_PAD = 30;

    private static Log logger = LogFactory.getLog(Dir.class);

    DecimalFormat formatter = new DecimalFormat("#.#");

    public String execute(String commandArgs, Map<String, Object> context) {
        File[] list = new File((String) context.get(Constants.KEY_PWD)).listFiles();
        StringBuffer sbFull = new StringBuffer();
        List<String> files = new ArrayList<String>();
        for (File file : list) {
            StringBuffer sb = new StringBuffer();
            sb.append(file.isDirectory() ? "+" : " ");
            double byteSize = file.length();
            double kbSize = byteSize / 1024;
            String fileName = getFileName(file);
            sb.append(StringUtils.rightPad(fileName, RIGHT_PAD, ' ')).append(StringUtils.leftPad(formatter.format(kbSize) + "kb ", 10, ' ')).append(StringUtils.leftPad(new Timestamp(file.lastModified()).toGMTString(), 30, ' ')).append(Constants.NEW_LINE);
            files.add(sb.toString());
        }
        Collections.sort(files);
        for (String string : files) {
            sbFull.append(string);
        }
        sbFull.append("Total : ").append(list.length);
        System.out.println(sbFull.toString());
        return sbFull.toString();
    }

    private String getFileName(File file) {
        String name = file.getName();
        return name.length() > RIGHT_PAD ? name.substring(0, RIGHT_PAD - 4) + "..." : name;
    }

    public String getCommandNames() {
        return "dir,ls,list,show";
    }
}
