package org.dant.ant.extension.tasks.internal;

import java.io.*;
import java.util.*;

public class RemoteResponse implements Serializable {

    public static final int TASK = 0;

    public static final int TESTCASE = 1;

    public static final int TESTSUITE = 2;

    private boolean success = true;

    private String result;

    private StringBuffer stdOutBuffer;

    private StringBuffer stdErrBuffer;

    private String fullurl;

    private Calendar startTime = Calendar.getInstance();

    private Calendar endTime = Calendar.getInstance();

    private String taskName;

    private String packageName;

    private String name;

    private int tagType;

    private String commandType;

    private String command;

    private int duplicate;

    private boolean multsited;

    public RemoteResponse() {
        stdOutBuffer = new StringBuffer();
        stdErrBuffer = new StringBuffer();
    }

    public RemoteResponse(String n, String p, int t, boolean s) {
        this();
        this.name = n;
        this.packageName = p;
        this.tagType = t;
        this.success = s;
    }

    public void setMultiSited(boolean m) {
        this.multsited = m;
    }

    public boolean getMultiSited() {
        return this.multsited;
    }

    public void setName(String n) {
        this.name = n;
    }

    public String getName() {
        return this.name;
    }

    public void setPackageName(String p) {
        this.packageName = p;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public void setTaskName(String t) {
        this.taskName = t;
    }

    public String getTaskName() {
        return this.taskName;
    }

    public int getTagType() {
        return this.tagType;
    }

    public void setTagType(int type) {
        this.tagType = type;
    }

    public void setStartTime(long s) {
        this.startTime.setTimeInMillis(s);
    }

    public void setEndTime(long e) {
        this.endTime.setTimeInMillis(e);
    }

    public long getStartTime() {
        return this.startTime.getTimeInMillis();
    }

    public long getEndTime() {
        return this.endTime.getTimeInMillis();
    }

    public long getTimes() {
        return endTime.getTimeInMillis() - startTime.getTimeInMillis();
    }

    public void setTimes(long times) {
    }

    public void setFullURL(String localhost) {
        this.fullurl = localhost;
    }

    public String getFullURL() {
        return this.fullurl;
    }

    public void setDuplicate(int d) {
        this.duplicate = d;
    }

    public int getDuplicate() {
        return this.duplicate;
    }

    public void setCommand(String c) {
        this.command = c;
    }

    public String getCommand() {
        return this.command;
    }

    public void setCommandType(String cmdType) {
        this.commandType = cmdType;
    }

    public String getCommandType() {
        return this.commandType;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean val) {
        this.success = val;
    }

    public void setResult(String val) {
        this.result = val;
    }

    public String getResult() {
        if (result == null) {
            return "N/A";
        }
        return result;
    }

    public void appendResult(Object val) {
        if (val == null) {
            return;
        }
        StringBuffer sb = new StringBuffer();
        if (result != null) {
            sb.append(result.toString());
        }
        sb.append("\n\n");
        sb.append(val.toString());
        setResult(sb.toString());
    }

    public void setStdOutBuffer(StringBuffer sb) {
        appendStdOutBuffer(sb.toString());
    }

    public void setStdErrBuffer(StringBuffer sb) {
        appendStdErrBuffer(sb.toString());
    }

    public void appendStdOutBuffer(String stdout) {
        this.stdOutBuffer.append(stdout);
        this.stdOutBuffer.append("\n\n");
    }

    public void appendStdErrBuffer(String stderr) {
        this.stdErrBuffer.append(stderr);
        this.stdErrBuffer.append("\n\n");
    }

    public StringBuffer getStdOutBuffer() {
        return this.stdOutBuffer;
    }

    public StringBuffer getStdErrBuffer() {
        return this.stdErrBuffer;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        if (fullurl != null) {
            buffer.append("\n-----------------Results---------------");
            buffer.append("\nFrom: ");
            buffer.append(fullurl);
        }
        buffer.append("\nStarts at:");
        buffer.append(this.startTime.getTime().toString());
        buffer.append("\nTask: ");
        buffer.append(this.taskName);
        buffer.append("\nCommand Type: ");
        buffer.append(commandType);
        if (command != null) {
            buffer.append("\nCommand: ");
            buffer.append(command);
        }
        buffer.append("\nsuccess : " + success);
        if (result != null) {
            buffer.append("\nresult : \n" + result.toString());
        }
        if (stdOutBuffer != null) {
            buffer.append("\nstdout : \n" + stdOutBuffer);
        }
        if (stdErrBuffer != null) {
            buffer.append("\nstderr : \n" + stdErrBuffer);
        }
        buffer.append("\nEnds at: ");
        buffer.append(this.endTime.getTime().toString());
        buffer.append("\n---------------------------------------\n");
        return buffer.toString();
    }
}
