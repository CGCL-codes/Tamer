package net.jfellow.tail.tailing;

import net.jfellow.tail.gui.JLabelTailStatusControl;
import net.jfellow.tail.util.TimestampFilter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractTailing extends BaseTail {

    protected static int debugCounter;

    protected Logger logger;

    protected int debugId;

    protected int status;

    protected boolean suspend = false;

    protected boolean printFileHeader = true;

    protected boolean isMixedOutput = false;

    protected boolean appendTimestamp = false;

    protected boolean tailFileCanBeRead;

    protected boolean retry;

    protected boolean useAppendingRegExpr;

    protected boolean useTimeRange;

    protected int linesToSearch = 1;

    protected int lines = 10;

    protected List regExpressions;

    protected List appendedRegExpressions;

    protected OutputControler caller;

    protected String file;

    protected String dynRegExpr;

    protected JLabelTailStatusControl control;

    protected SearchBuffer buf;

    protected TimestampFilter timestampFilter;

    protected int intervall = 500;

    public AbstractTailing(OutputControler caller, String file, TailCommander commander, boolean showStatus) {
        this.debugId = debugCounter++;
        this.logger = Logger.getLogger(this.getClass().getName());
        this.caller = caller;
        this.file = file;
        this.commander = commander;
        boolean nogui = caller.hasNoGUI();
        if (!nogui) {
            this.control = new JLabelTailStatusControl();
            this.control.setShowStatus(showStatus);
        }
        this.buf = new SearchBuffer();
        this.buf.setFile(file);
    }

    public void suspendRunning() {
        this.suspend = true;
    }

    public void resumeRunning() {
        this.suspend = false;
    }

    public boolean getSuspended() {
        return this.suspend;
    }

    public void stopRunning() {
        this.stop = true;
        this.clearAppendedRegExpressions();
        this.loadPatterns(new ArrayList());
    }

    public void setPrintFileHeader(boolean b) {
        this.printFileHeader = b;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getFile() {
        return this.file;
    }

    public void appendDynRegExpr(String s) {
        if ((s != null) && !"".equals(s)) {
        } else {
            return;
        }
        if (this.appendedRegExpressions == null) {
            this.appendedRegExpressions = new ArrayList();
        }
        if (this.containsDynRegExp(s)) {
            return;
        }
        if (this.dynRegExpr == null) {
            this.dynRegExpr = s;
        } else if ("".equals(this.dynRegExpr)) {
            this.dynRegExpr = s;
        } else {
            this.dynRegExpr = dynRegExpr + "|" + s;
        }
        this.buf.setAppendingRegExpression(this.dynRegExpr);
        this.appendedRegExpressions.add(s);
        this.addPattern(s);
        this.setTooltip();
        logger.finer(this.toString() + " : Appended single reg. Expression: " + s);
    }

    public void appendDynRegExpr(List l) {
        if (l == null) {
            return;
        }
        if (this.appendedRegExpressions == null) {
            this.appendedRegExpressions = new ArrayList();
        }
        Iterator it = l.iterator();
        String s = null;
        while (it.hasNext()) {
            s = (String) it.next();
            if (this.appendedRegExpressions.contains(s)) {
                continue;
            }
            if (this.dynRegExpr == null) {
                this.dynRegExpr = s;
            } else if ("".equals(this.dynRegExpr)) {
                this.dynRegExpr = s;
            } else {
                this.dynRegExpr = dynRegExpr + "|" + s;
            }
            this.buf.setAppendingRegExpression(this.dynRegExpr);
            this.appendedRegExpressions.add(s);
            this.addPattern(s);
            this.setTooltip();
            logger.finer(this.toString() + " : Appended reg. Expression(s) from a list: " + s);
        }
    }

    public String getDynRegExpr() {
        return this.dynRegExpr;
    }

    public void setAppendedRegExpressions(List l) {
        this.appendedRegExpressions = l;
    }

    public List getAppendedRegExpressions() {
        return this.appendedRegExpressions;
    }

    public void setRegExpressions(List regExpressions) {
        this.regExpressions = regExpressions;
        this.loadPatterns(regExpressions);
    }

    public List getRegExpressions() {
        return this.regExpressions;
    }

    public boolean getTailFileCanBeRead() {
        return this.tailFileCanBeRead;
    }

    public void setControl(JLabelTailStatusControl control) {
        this.control = control;
    }

    public JLabelTailStatusControl getControl() {
        return this.control;
    }

    public void setIntervall(int intervall) {
        this.intervall = intervall;
    }

    public void setLinesToSearch(int linesToSearch) {
        this.linesToSearch = linesToSearch;
    }

    public int getLinesToSearch() {
        return this.linesToSearch;
    }

    public void setAppendTimestamp(boolean b) {
        this.appendTimestamp = b;
        ;
    }

    public boolean getAppendTimestamp() {
        return this.appendTimestamp;
    }

    public void setIsMixedOutput(boolean b) {
        this.isMixedOutput = b;
        ;
    }

    public boolean getIsMixedOutput() {
        return this.isMixedOutput;
    }

    public void setRetry(boolean b) {
        this.retry = b;
        ;
    }

    public boolean getRetry() {
        return this.retry;
    }

    public void setLines(int lines) {
        this.lines = lines;
    }

    public int getLines() {
        return this.lines;
    }

    public int getStatus() {
        return this.status;
    }

    public void setUseAppendingRegExpr(boolean b) {
        this.useAppendingRegExpr = b;
    }

    public boolean getUseAppendingRegExpr() {
        return this.useAppendingRegExpr;
    }

    public int getDebugId() {
        return this.debugId;
    }

    public void clearAppendedRegExpressions() {
        if (this.appendedRegExpressions == null) {
            return;
        }
        this.appendedRegExpressions.clear();
        this.dynRegExpr = null;
        this.loadPatterns(new ArrayList());
    }

    public void setTimestampFilter(TimestampFilter filter) {
        this.timestampFilter = filter;
    }

    public TimestampFilter getTimestampFilter() {
        return this.timestampFilter;
    }

    public void setUseTimeRange(boolean b) {
        this.useTimeRange = b;
        ;
    }

    public boolean getUseTimeRange() {
        return this.useTimeRange;
    }

    protected void setStatus(int status) {
        if (this.control == null) {
            return;
        }
        this.status = status;
        this.control.setStatus(status);
    }

    protected boolean filterWithTimeRange(String s) {
        if (!this.useTimeRange) {
            return true;
        }
        if (this.timestampFilter == null) {
            return true;
        }
        boolean b = false;
        try {
            b = this.timestampFilter.filter(s);
        } catch (Exception e) {
            String msg = this.toString() + " : Timestamp Filter has return an error.";
            msg = msg + "\n\tThe logfile is: " + this.file + "\n\t" + e.getMessage();
            logger.warning(msg);
            logger.log(Level.FINE, this.toString() + ": Details:", e);
            return false;
        }
        return b;
    }

    protected void appendDependingOnSearchResult(String inLine, int currentLine, SearchBuffer buf) {
        int size = buf.getPatterns().size();
        if ((size < 1) && !this.useAppendingRegExpr && (this.excludeExpression == null)) {
            this.append(inLine);
        } else {
            String searchResult = buf.appendLineAndSearchStartingAtEnd(inLine, currentLine);
            if (searchResult != null) {
                this.append(searchResult);
            } else {
            }
        }
    }

    protected boolean containsDynRegExp(String s) {
        List tmpList = new ArrayList(this.appendedRegExpressions);
        Iterator it = tmpList.iterator();
        while (it.hasNext()) {
            String expr = (String) it.next();
            if (expr.equals(s)) {
                return true;
            }
        }
        return false;
    }

    protected void setTooltip() {
        if (this.control == null) {
            return;
        }
        StringBuffer buffer = new StringBuffer();
        buffer.append("<html>");
        buffer.append("<b>Regular Expressions</b>");
        boolean flag = false;
        if (this.regExpressions != null) {
            Iterator it = this.regExpressions.iterator();
            while (it.hasNext()) {
                if (flag) {
                    buffer.append("<br><b>AND</b>");
                }
                flag = true;
                String s = (String) it.next();
                buffer.append("<br>").append(s);
            }
        }
        if (this.excludeExpression != null) {
            if (!flag) {
                buffer.append("<br><b>Exclude Expression</b>");
                buffer.append("<br>").append(this.excludeExpression);
            } else {
                buffer.append("<br><b>AND NOT</b>");
                buffer.append("<br><b>Exclude Expression</b>");
                buffer.append("<br>").append(this.excludeExpression);
            }
            flag = true;
        }
        if (this.dynRegExpr != null) {
            if (!flag) {
                buffer.append("<br><b>Dynamically appended regular Expressions</b>");
                buffer.append("<br>").append(this.dynRegExpr);
            } else {
                buffer.append("<br><b>AND</b>");
                buffer.append("<br><b>Dynamically appended regular Expressions</b>");
                buffer.append("<br>").append(this.dynRegExpr);
            }
        }
        buffer.append("</html>");
        this.control.setToolTipText(buffer.toString());
    }

    protected abstract void append(String s);
}
