package org.cobertura4j2me.runtime;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**
 * Encapsulates the coverage data of a class
 */
public class CoverageData implements HasBeenInstrumented, Persistent {

    class MethodEnumeration implements Enumeration {

        private Vector methods;

        private int index;

        public MethodEnumeration() {
            methods = new Vector(10);
            index = 0;
            Enumeration lineInfos = lines.elements();
            while (lineInfos.hasMoreElements()) {
                LineInformation lineInfo = (LineInformation) lineInfos.nextElement();
                String value = null;
                if (lineInfo.getMethodName() != null) {
                    value = lineInfo.getMethodName();
                }
                if (lineInfo.getMethodDescriptor() != null) {
                    value += lineInfo.getMethodDescriptor();
                }
                if (value != null && !methods.contains(value)) {
                    methods.addElement(value);
                }
            }
        }

        /**
         * {@inheritDoc}
         */
        public boolean hasMoreElements() {
            return index < methods.size();
        }

        /**
         * {@inheritDoc}
         */
        public Object nextElement() {
            return methods.elementAt(index++);
        }

        int getNumberOfMethods() {
            return methods.size();
        }
    }

    Hashtable lines;

    /**
     * Creates a new <tt>CoverageData</tt>.
     */
    public CoverageData() {
        lines = new Hashtable();
    }

    /**
     * Adds a new line to the list of instrumented lines.
     * 
     * @param lineNumber the line number
     * @param methodName the method name
     * @param methodDescriptor the method descriptor
     * 
     * @throws NullPointerException if <tt>methodName</tt> or
     *  <tt>methodDescriptor</tt> is null.
     * @throws IllegalArgumentException if <tt>lineNumber</tt> is smaller than
     *  one.
     */
    public void addLine(int lineNumber, String methodName, String methodDescriptor) {
        if (methodName == null || methodDescriptor == null) {
            throw new NullPointerException();
        }
        if (lineNumber < 1) {
            throw new IllegalArgumentException();
        }
        LineInformation lineInformation = getLineInformation(lineNumber);
        if (lineInformation == null) {
            lineInformation = new LineInformation(lineNumber, methodName, methodDescriptor);
            lines.put(new Integer(lineNumber), lineInformation);
        } else {
        }
    }

    /**
     * Answers the branch coverage rate for this class.
     * 
     * @return double.
     */
    public double getBranchCoverageRate() {
        int branches = getNumberOfValidBranches();
        if (branches == 0) {
            return 1d;
        }
        return (double) getNumberOfCoveredBranches() / branches;
    }

    /**
     * Answers the branch coverage rate for a particular method.
     * 
     * @return double the branch coverage rate
     * 
     * @throws NullPointerException if <tt>methodNameAndDescriptor</tt> is null.
     */
    public double getBranchCoverageRate(String methodNameAndDescriptor) {
        if (methodNameAndDescriptor == null) {
            throw new NullPointerException();
        }
        int total = 0;
        int hits = 0;
        Enumeration elements = lines.elements();
        while (elements.hasMoreElements()) {
            LineInformation next = (LineInformation) elements.nextElement();
            if (next.isConditional() && methodNameAndDescriptor.equals(next.getMethodName() + next.getMethodDescriptor())) {
                total++;
                if (next.getHits() > 0) {
                    hits++;
                }
            }
            return (double) hits / total;
        }
        return (double) hits / total;
    }

    /**
     * Answers the number of hits a particular line of code has
     * 
     * @param lineNumber the source code line number.
     * 
     * @return int.
     */
    public int getHitCount(int lineNumber) {
        Integer lineNum = new Integer(lineNumber);
        if (!lines.containsKey(lineNum)) {
            return 0;
        }
        return ((LineInformation) lines.get(lineNum)).getHits();
    }

    /**
     * @return The line coverage rate for the class
     */
    public double getLineCoverageRate() {
        if (lines.size() == 0) {
            return 1d;
        }
        return (double) getNumberOfCoveredLines() / lines.size();
    }

    /**
     * Answers the line coverage rate for the given method. 
     * 
     * @param methodNameAndDescriptor
     * 
     * @return The line coverage rate for a particular method
     * 
     * @throws NullPointerException if <tt>methodNameAndDescriptor</tt> is null.
     */
    public double getLineCoverageRate(String methodNameAndDescriptor) {
        if (methodNameAndDescriptor == null) {
            throw new NullPointerException();
        }
        int total = 0;
        int hits = 0;
        Enumeration elements = lines.elements();
        while (elements.hasMoreElements()) {
            LineInformation next = (LineInformation) elements.nextElement();
            if (methodNameAndDescriptor.equals(next.getMethodName() + next.getMethodDescriptor())) {
                total++;
                if (next.getHits() > 0) {
                    hits++;
                }
            }
        }
        return (double) hits / total;
    }

    /**
     * Answers the information for the given line
     * 
     * @param lineNumber the line number
     * @return the LineInformation associated with this line
     */
    private LineInformation getLineInformation(int lineNumber) {
        return (LineInformation) lines.get(new Integer(lineNumber));
    }

    /**
     * @return The method name and descriptor of each method found in the
     *         class represented by this instrumentation.
     */
    public Enumeration getMethodNamesAndDescriptors() {
        return new MethodEnumeration();
    }

    /**
     * @return The number of branches in this class covered by testing.
     */
    public int getNumberOfCoveredBranches() {
        int num = 0;
        Enumeration elements = lines.elements();
        while (elements.hasMoreElements()) {
            LineInformation next = (LineInformation) elements.nextElement();
            if (next.isConditional() && next.getHits() > 0) {
                num++;
            }
        }
        return num;
    }

    /**
     * @return The number of lines in this class covered by testing.
     */
    public int getNumberOfCoveredLines() {
        int num = 0;
        Enumeration elements = lines.elements();
        while (elements.hasMoreElements()) {
            if (((LineInformation) elements.nextElement()).getHits() > 0) {
                num++;
            }
        }
        return num;
    }

    /**
     * Answers the number of covered methods in the
     * class represented by this coverage data
     * 
     * @return
     */
    public int getNumberOfCoveredMethods() {
        int num = 0;
        Enumeration enumeration = getMethodNamesAndDescriptors();
        while (enumeration.hasMoreElements()) {
            String method = (String) enumeration.nextElement();
            if (getLineCoverageRate(method) != 0) {
                num++;
            }
        }
        return num;
    }

    /**
     * @return The number of branches in this class.
     */
    public int getNumberOfValidBranches() {
        int num = 0;
        Enumeration elements = lines.elements();
        while (elements.hasMoreElements()) {
            LineInformation next = (LineInformation) elements.nextElement();
            if (next.isConditional()) {
                num++;
            }
        }
        return num;
    }

    /**
     * @return The number of lines in this class.
     */
    public int getNumberOfValidLines() {
        return lines.size();
    }

    /**
     * Answers the number of valid methods in the
     * class represented by this coverage data
     * 
     * @return int
     */
    public int getNumberOfValidMethods() {
        return new MethodEnumeration().getNumberOfMethods();
    }

    /**
     * @return The set of valid source line numbers
     */
    public Enumeration getValidLineNumbers() {
        return lines.keys();
    }

    /**
     * Determines if a given line number is a valid line of code.
     *
     * @return True if the line contains executable code, false if the line is
     *  empty, or a comment, etc.
     */
    public boolean isValidSourceLineNumber(int lineNumber) {
        return lines.containsKey(new Integer(lineNumber));
    }

    /**
     * Marks the given line as conditional, i.e. the start of a new branch
     * 
     * @param lineNumber the line number
     */
    public void markLineAsConditional(int lineNumber) {
        LineInformation lineInformation = getLineInformation(lineNumber);
        if (lineInformation != null) {
            lineInformation.markAsConditional();
        }
    }

    /**
     * Merges some existing instrumentation with this instrumentation.
     *
     * @param coverageData Some existing coverage data.
     */
    public void merge(CoverageData coverageData) {
        merge(coverageData.lines, lines);
    }

    /**
     * Copy data from <code>src</code> to <code>dest</code>
     * 
     * @param src the Hashtable to copy from
     * @param dest the Hashtable to copy to
     */
    private void merge(Hashtable src, Hashtable dest) {
        Enumeration enumeration = src.keys();
        while (enumeration.hasMoreElements()) {
            Integer lineNumber = (Integer) enumeration.nextElement();
            Object lineInfo = dest.get(lineNumber);
            if (lineInfo != null) {
                ((LineInformation) lineInfo).merge((LineInformation) src.get(lineNumber));
            } else {
                dest.put(lineNumber, src.get(lineNumber));
            }
        }
    }

    /**
     * Removes the given line number from this coverage data
     * 
     * @param lineNumber the line number to remove
     */
    public void removeLine(int lineNumber) {
        lines.remove(new Integer(lineNumber));
    }

    /**
     * Increments the number of hits for a particular line of code.
     *
     * @param lineNumber the line of code to increment the number of hits.
     */
    public void touch(int lineNumber) {
        LineInformation lineInformation = getLineInformation(lineNumber);
        if (lineInformation == null) {
            lineInformation = new LineInformation(lineNumber);
            lines.put(new Integer(lineNumber), lineInformation);
        }
        lineInformation.touch();
    }

    /**
     * {@inheritDoc}
     */
    public void persist(DataOutputStream dos) throws IOException {
        if (dos == null) {
            throw new NullPointerException();
        }
        Factory.trace(Factory.TRACE_COVERAGE_DATA, toString());
        dos.writeInt(lines.size());
        Enumeration keys = lines.keys();
        while (keys.hasMoreElements()) {
            Integer key = (Integer) keys.nextElement();
            LineInformation element = (LineInformation) lines.get(key);
            dos.writeInt(key.intValue());
            element.persist(dos);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void resurrect(DataInputStream dis) throws IOException {
        if (dis == null) {
            throw new NullPointerException();
        }
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            int lineNumber = dis.readInt();
            LineInformation lineInformation = new LineInformation(lineNumber);
            lineInformation.resurrect(dis);
            lines.put(new Integer(lineNumber), lineInformation);
        }
        Factory.trace(Factory.TRACE_COVERAGE_DATA, toString());
    }

    /**
     * Answers whether we have all the data necessary. If not this means that
     * this represents the runtime coverage data and that it hasn't been
     * merged with the instrumentation one.
     * 
     * @return true if complete, false otherwise
     */
    public boolean isDataComplete() {
        Enumeration enumeration = lines.keys();
        while (enumeration.hasMoreElements()) {
            Integer lineNumber = (Integer) enumeration.nextElement();
            if (!((LineInformation) lines.get(lineNumber)).isDataComplete()) {
                return false;
            }
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public String toString() {
        return "[CoverageData" + " | complete: " + isDataComplete() + " | LCR: " + getLineCoverageRate() + " | BCR: " + getBranchCoverageRate() + " | valid lines: " + getNumberOfValidLines() + "]";
    }
}
