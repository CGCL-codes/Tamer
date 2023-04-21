package info.thinkbit.smil;

import java.io.*;
import org.apache.commons.lang.StringUtils;

/**
* <code>Generator</code>
* 
* @author	Wolfgang Nagele
* @version	0.3
*/
public class Generator {

    private String smil, body, meta, transition, layout, tabs;

    public Generator() {
        this.smil = "";
        this.body = "";
        this.meta = "";
        this.transition = "";
        this.layout = "";
        this.tabs = "";
    }

    public String getVersion() {
        return "%%%VERSION%%%";
    }

    public void generate() {
        this.push("<smil xmlns=\"http://www.w3.org/2001/SMIL20/Language\"");
        this.push("      xmlns:rn=\"http://features.real.com/2001/SMIL20/Extensions\">");
        this.addTabs(1);
        this.head();
        this.push("");
        this.body();
        this.removeTabs(1);
        this.push("</smil>");
    }

    private void head() {
        this.push("<head>");
        this.addTabs(1);
        this.rawPush(this.meta, false);
        this.push("");
        this.rawPush(this.transition, false);
        this.push("");
        this.push("<layout>");
        this.addTabs(1);
        this.rawPush(this.layout, false);
        this.removeTabs(1);
        this.push("</layout>");
        this.removeTabs(1);
        this.push("</head>");
    }

    private void body() {
        this.push("<body>");
        this.addTabs(1);
        this.rawPush(this.body, false);
        this.removeTabs(1);
        this.push("</body>");
    }

    protected void push(String append) {
        this.doPush(append, "smil", false, true);
    }

    protected void push(String append, boolean newline) {
        this.doPush(append, "smil", false, newline);
    }

    protected void push(String append, String target) {
        this.doPush(append, target, false, true);
    }

    protected void push(String append, String target, boolean newline) {
        this.doPush(append, target, false, newline);
    }

    protected void rawPush(String append) {
        this.doPush(append, "smil", true, true);
    }

    protected void rawPush(String append, boolean newline) {
        this.doPush(append, "smil", true, newline);
    }

    protected void rawPush(String append, String target) {
        this.doPush(append, target, true, true);
    }

    protected void rawPush(String append, String target, boolean newline) {
        this.doPush(append, target, true, newline);
    }

    private void doPush(String append, String target, boolean raw, boolean newline) {
        String output = "";
        if (!raw) {
            output += this.tabs;
        }
        output += append;
        if (newline) {
            output += System.getProperty("line.separator");
        }
        if (target == "smil") {
            this.smil += output;
        } else if (target == "body") {
            this.body += output;
        } else if (target == "meta") {
            this.meta += output;
        } else if (target == "transition") {
            this.transition += output;
        } else if (target == "layout") {
            this.layout += output;
        }
    }

    private void flush() {
        this.smil = "";
    }

    public String show() {
        this.flush();
        this.generate();
        return this.smil;
    }

    public boolean write(String file) {
        return this.doWrite(new File(file));
    }

    public boolean write(File file) {
        return this.doWrite(file);
    }

    private boolean doWrite(File file) {
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(this.show());
            fileWriter.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public void addTabs(int number) {
        for (int i = 0; i < number; i++) {
            this.tabs += "\t";
        }
    }

    public void removeTabs(int number) {
        for (int i = 0; i < number; i++) {
            this.tabs = StringUtils.chop(this.tabs);
        }
    }
}
