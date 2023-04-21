package net.cordova.justus.profiler;

import java.io.*;
import java.util.*;
import net.cordova.justus.progressparser.*;

public class ExecutionProgram implements CommandListener, Comparator<Object> {

    private ArrayList<ProfileInfo> items;

    private String sourceCode = new String();

    private String programName;

    private int sourceId;

    public ExecutionProgram(int sourceId, String programName) {
        this.sourceId = sourceId;
        this.programName = programName;
        this.items = new ArrayList<ProfileInfo>();
    }

    protected ExecutionProgram(int sourceId, String programName, String sourceCode) {
        this(sourceId, programName);
        this.parseSource(sourceCode);
    }

    protected void parseSource(String sourceCode) {
        this.sourceCode = sourceCode;
        ByteArrayInputStream input = new ByteArrayInputStream(sourceCode.getBytes());
        ProgressParser parser = new ProgressParser(input);
        parser.addCommandListener(this);
        parser.analiseCode();
    }

    public String getProgramName() {
        return this.programName;
    }

    public String getSourceCode() {
        return this.sourceCode;
    }

    public Collection<ProfileInfo> getSourceInfo() {
        return this.items;
    }

    /***
	 * Searches for all statements on line <i>lineNumber</i>
	 * @param lineNumber Line number on the source code. 
	 * @return All the statements found on this line.
	 */
    public ArrayList<ProfileInfo> findLine(int lineNumber) {
        ArrayList<ProfileInfo> elementsBefore = new ArrayList<ProfileInfo>();
        ArrayList<ProfileInfo> elementsAfter = new ArrayList<ProfileInfo>();
        ArrayList<ProfileInfo> result = new ArrayList<ProfileInfo>();
        int pos = Collections.binarySearch(this.items, lineNumber, this);
        if (pos < 0) return result;
        for (int item = pos - 1; item >= 0; item--) {
            ProfileInfo info = this.items.get(item);
            if (info.getCommandLine() == lineNumber) elementsBefore.add(0, info); else break;
        }
        for (int item = pos + 1; item < this.items.size(); item++) {
            ProfileInfo info = this.items.get(item);
            if (info.getCommandLine() == lineNumber) elementsAfter.add(info); else break;
        }
        result.addAll(elementsBefore);
        result.add(this.items.get(pos));
        result.addAll(elementsAfter);
        return result;
    }

    public void commandRead(GenericProgressStatement command) {
        ProfileInfo info = new ProfileInfo(command);
        this.items.add(info);
    }

    public int getSourceId() {
        return sourceId;
    }

    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }

    public float getCoverage() {
        int statementsCounter = 0;
        int coveredStatements = 0;
        for (ProfileInfo stmt : items) {
            if (stmt.isValidForCoverage()) {
                statementsCounter++;
                if (stmt.getUsage() > 0) coveredStatements++;
            }
        }
        return coveredStatements / (float) statementsCounter;
    }

    public int compare(Object item1, Object item2) {
        int line1, line2;
        if (item1 instanceof Integer) line1 = ((Integer) item1).intValue(); else if (item1 instanceof ProfileInfo) line1 = ((ProfileInfo) item1).getCommandLine(); else return -1;
        if (item2 instanceof Integer) line2 = ((Integer) item2).intValue(); else if (item1 instanceof ProfileInfo) line2 = ((ProfileInfo) item2).getCommandLine(); else return -1;
        return line1 - line2;
    }
}
