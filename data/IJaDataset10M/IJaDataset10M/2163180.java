package gov.nasa.jpf.report;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.Error;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.jvm.ChoiceGenerator;
import gov.nasa.jpf.jvm.ElementInfo;
import gov.nasa.jpf.jvm.JVM;
import gov.nasa.jpf.jvm.Path;
import gov.nasa.jpf.jvm.StackFrame;
import gov.nasa.jpf.jvm.Step;
import gov.nasa.jpf.jvm.ThreadInfo;
import gov.nasa.jpf.jvm.Transition;
import gov.nasa.jpf.util.RepositoryEntry;
import gov.nasa.jpf.util.Source;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class XMLPublisher extends Publisher {

    public XMLPublisher(Config conf, Reporter reporter) {
        super(conf, reporter);
    }

    public String getName() {
        return "xml";
    }

    protected void openChannel() {
        if (out == null) {
            String fname = getReportFileName("jpf.report.xml.file") + ".xml";
            try {
                out = new PrintWriter(fname);
            } catch (FileNotFoundException fnfx) {
            }
        }
    }

    protected void closeChannel() {
        if (out != null) {
            out.close();
            out = null;
        }
    }

    protected void publishProlog() {
        out.println("<?xml version=\"1.0\" ?>");
        out.println("<jpfreport>");
    }

    public void publishTopicStart(String topic) {
        out.println("  <" + topic + ">");
    }

    public void publishTopicEnd(String topic) {
        out.println("  </" + topic + ">");
    }

    protected void publishEpilog() {
        out.println("</jpfreport>");
    }

    protected void publishJPF() {
        out.println("  <jpf-version>" + JPF.VERSION + "</jpf-version>");
    }

    protected void publishJPFConfig() {
        TreeMap<Object, Object> map = conf.asOrderedMap();
        Set<Map.Entry<Object, Object>> eSet = map.entrySet();
        out.println("  <jpf-properties>");
        for (Object src : conf.getSources()) {
            out.println("    <source value=\"" + conf.getSourceName(src) + "\"/>");
        }
        for (Map.Entry<Object, Object> e : eSet) {
            out.println("    <entry key=\"" + e.getKey() + "\" value=\"" + e.getValue() + "\"/>");
        }
        out.println("  </jpf-properties>");
        String[] args = conf.getArgs();
        out.print("  <jpf-args>");
        for (String s : args) {
            out.print(s);
            out.print(' ');
        }
        out.println("</jpf-args>");
    }

    protected void publishPlatform() {
        out.println("  <platform>");
        out.println("    <hostname>" + reporter.getHostName() + "</hostname>");
        out.println("    <arch>" + reporter.getArch() + "</arch>");
        out.println("    <os>" + reporter.getOS() + "</os>");
        out.println("    <java>" + reporter.getJava() + "</java>");
        out.println("  </platform>");
    }

    protected void publishUser() {
        out.println("  <user>" + reporter.getUser() + "</user>");
    }

    protected void publishDTG() {
        out.println("  <started>" + reporter.getStartDate() + "</started>");
    }

    protected void publishSuT() {
        out.println("  <sut>");
        String mainCls = conf.getTargetArg();
        if (mainCls != null) {
            String mainPath = reporter.getSuT();
            if (mainPath != null) {
                out.println("    <source>" + mainPath + "</source>");
                RepositoryEntry rep = RepositoryEntry.getRepositoryEntry(mainPath);
                if (rep != null) {
                    out.println("    <repository>" + rep.getRepository() + "</repository>");
                    out.println("    <revision>" + rep.getRevision() + "</revision>");
                }
            } else {
                out.println("    <binary>" + mainCls + ".class" + "</binary>");
            }
        } else {
        }
        out.println("  </sut>");
    }

    protected void publishResult() {
        List<Error> errors = reporter.getErrors();
        out.print("  <result findings=\"");
        if (errors.isEmpty()) {
            out.println("none\"/>");
        } else {
            out.println("errors\">");
            int i = 0;
            for (Error e : errors) {
                out.print("    <error id=\"");
                out.print(i++);
                out.println("\">");
                out.print("      <property>");
                out.print(e.getProperty().getClass().getName());
                out.println("</property>");
                out.print("      <details>");
                out.print(e.getDetails());
                out.println("      </details>");
                out.println("    </error>");
            }
            out.println("  </result>");
        }
    }

    protected void publishTrace() {
        Path path = reporter.getPath();
        int i = 0;
        if (path.size() == 0) {
            return;
        }
        out.println("  <trace>");
        for (Transition t : path) {
            ChoiceGenerator<?> cg = t.getChoiceGenerator();
            out.println("    <transition id=\"" + i++ + "\" thread=\"" + t.getThreadIndex() + "\">");
            out.println("      <cg class=\"" + cg.getClass().getName() + "\" choice=\"" + cg.getProcessedNumberOfChoices() + "\"/>");
            for (Step s : t) {
                out.print("      <insn src=\"" + s.getLocationString() + "\">");
                String insn = s.getInstruction().toString();
                if (insn.indexOf('<') >= 0) {
                    insn = insn.replaceAll("<", "&lt;");
                    insn = insn.replaceAll(">", "&gt;");
                }
                out.print(insn);
                out.println("</insn>");
            }
            out.println("    </transition>");
        }
        out.println("  </trace>");
    }

    protected void publishOutput() {
        Path path = reporter.getPath();
        if (path.size() == 0) {
            return;
        }
        if (path.hasOutput()) {
            out.println("  <output>");
            for (Transition t : path) {
                String s = t.getOutput();
                if (s != null) {
                    out.print(s);
                }
            }
            out.println("  </output>");
        }
    }

    protected void publishSnapshot() {
        JVM vm = reporter.getVM();
        out.println("  <live-threads>");
        for (ThreadInfo ti : vm.getLiveThreads()) {
            out.println("    <thread id=\"" + ti.getIndex() + "\" name=\"" + ti.getName() + "\" status=\"" + ti.getStatusName() + "\">");
            for (ElementInfo e : ti.getLockedObjects()) {
                out.println("      <lock-owned object=\"" + e + "\"/>");
            }
            ElementInfo ei = ti.getLockObject();
            if (ei != null) {
                out.println("      <lock-request object=\"" + ei + "\"/>");
            }
            List<StackFrame> callStack = ti.getStack();
            Collections.reverse(callStack);
            for (StackFrame frame : callStack) {
                if (!frame.isDirectCallFrame()) {
                    out.println("      <frame>" + frame.getStackTraceInfo() + "</frame>");
                }
            }
            out.println("    </thread>");
        }
        out.println("  </live-threads>");
    }

    protected void publishStatistics() {
        Statistics stat = reporter.getStatistics();
        out.println("  <statistics>");
        out.println("    <elapsed-time>" + formatHMS(reporter.getElapsedTime()) + "</elapsed-time>");
        out.println("    <new-states>" + stat.newStates + "</new-states>");
        out.println("    <visited-states>" + stat.visitedStates + "</visited-states>");
        out.println("    <backtracked-states>" + stat.backtracked + "</backtracked-states>");
        out.println("    <end-states>" + stat.endStates + "</end-states>");
        out.println("    <max-memory unit=\"MB\">" + (stat.maxUsed >> 20) + "</max-memory>");
        out.println("  </statistics>");
    }
}
