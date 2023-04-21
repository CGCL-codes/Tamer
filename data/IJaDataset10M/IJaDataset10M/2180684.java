package edu.ucsb.ccs.jcontractor.extras.doclet;

import com.sun.tools.doclets.standard.*;
import com.sun.tools.doclets.*;
import com.sun.javadoc.*;
import java.io.*;
import java.util.*;

/**
 * Decorates the PackageDoc implementation in the "standard" doclet mostly to
 * permit supression of contract classes.
 *
 * @author David P. White
 * @version %I%, %G%
 *
 */
class jContractorPackageDoc implements PackageDoc {

    private PackageDoc original = null;

    /**
     * @param original A PackageDoc created by Javadoc.
     */
    jContractorPackageDoc(PackageDoc original) {
        this.original = original;
    }

    /**
     * @return An array of jContractorClassDoc objects, one each for each of the
     * results provided by the "standard" PackageDoc implementation less any
     * contract classes.
     */
    public ClassDoc[] allClasses(boolean filter) {
        ClassDoc[] originalResults = jContractorDocletUtil.removeContractClasses(original.allClasses(filter));
        jContractorClassDoc[] results = new jContractorClassDoc[originalResults.length];
        for (int i = 0; i < results.length; i++) {
            results[i] = new jContractorClassDoc(originalResults[i]);
        }
        return results;
    }

    /**
     * @return An array of jContractorClassDoc objects, one each for each of the
     * results provided by the "standard" PackageDoc implementation less any
     * contract classes.
     */
    public ClassDoc[] allClasses() {
        ClassDoc[] originalResults = jContractorDocletUtil.removeContractClasses(original.allClasses());
        jContractorClassDoc[] results = new jContractorClassDoc[originalResults.length];
        for (int i = 0; i < results.length; i++) {
            results[i] = new jContractorClassDoc(originalResults[i]);
        }
        return results;
    }

    /**
     * @return An array of jContractorClassDoc objects, one each for each of the
     * results provided by the "standard" PackageDoc implementation less any
     * contract classes.
     */
    public ClassDoc[] ordinaryClasses() {
        ClassDoc[] originalResults = jContractorDocletUtil.removeContractClasses(original.ordinaryClasses());
        jContractorClassDoc[] results = new jContractorClassDoc[originalResults.length];
        for (int i = 0; i < results.length; i++) {
            results[i] = new jContractorClassDoc(originalResults[i]);
        }
        return results;
    }

    /**
     * @return An array of jContractorClassDoc objects, one each for each of the
     * results provided by the "standard" PackageDoc implementation less any
     * contract classes.
     */
    public ClassDoc[] exceptions() {
        ClassDoc[] originalResults = jContractorDocletUtil.removeContractClasses(original.exceptions());
        jContractorClassDoc[] results = new jContractorClassDoc[originalResults.length];
        for (int i = 0; i < results.length; i++) {
            results[i] = new jContractorClassDoc(originalResults[i]);
        }
        return results;
    }

    /**
     * @return An array of jContractorClassDoc objects, one each for each of the
     * results provided by the "standard" PackageDoc implementation less any
     * contract classes.
     */
    public ClassDoc[] errors() {
        ClassDoc[] originalResults = jContractorDocletUtil.removeContractClasses(original.errors());
        jContractorClassDoc[] results = new jContractorClassDoc[originalResults.length];
        for (int i = 0; i < results.length; i++) {
            results[i] = new jContractorClassDoc(originalResults[i]);
        }
        return results;
    }

    /**
     * @return An array of jContractorClassDoc objects, one each for each of the
     * results provided by the "standard" PackageDoc implementation less any
     * contract classes.
     */
    public ClassDoc[] interfaces() {
        ClassDoc[] originalResults = jContractorDocletUtil.removeContractClasses(original.interfaces());
        jContractorClassDoc[] results = new jContractorClassDoc[originalResults.length];
        for (int i = 0; i < results.length; i++) {
            results[i] = new jContractorClassDoc(originalResults[i]);
        }
        return results;
    }

    /**
     * @return a jContractorClassDoc for the specified class name or null if the class
     * is unknown or is a contract class.
     */
    public ClassDoc findClass(String className) {
        ClassDoc result = original.findClass(className);
        if (null != result) {
            if (jContractorDocletUtil.isContractClass(result)) {
                result = null;
            }
        }
        return new jContractorClassDoc(result);
    }

    public int compareTo(Object o) {
        if (o.getClass() == this.getClass()) {
            return original.compareTo(((jContractorPackageDoc) o).original);
        }
        return original.compareTo((PackageDoc) o);
    }

    public String commentText() {
        return original.commentText();
    }

    public Tag[] tags() {
        return original.tags();
    }

    public Tag[] tags(String tagname) {
        return original.tags(tagname);
    }

    public SeeTag[] seeTags() {
        return original.seeTags();
    }

    public Tag[] inlineTags() {
        return original.inlineTags();
    }

    public Tag[] firstSentenceTags() {
        return original.firstSentenceTags();
    }

    public String getRawCommentText() {
        return original.getRawCommentText();
    }

    public void setRawCommentText(String rawDocumentation) {
        original.setRawCommentText(rawDocumentation);
    }

    public String name() {
        return original.name();
    }

    public boolean isField() {
        return original.isField();
    }

    public boolean isMethod() {
        return original.isMethod();
    }

    public boolean isConstructor() {
        return original.isConstructor();
    }

    public boolean isInterface() {
        return original.isInterface();
    }

    public boolean isException() {
        return original.isException();
    }

    public boolean isError() {
        return original.isError();
    }

    public boolean isOrdinaryClass() {
        return original.isOrdinaryClass();
    }

    public boolean isClass() {
        return original.isClass();
    }

    public boolean isIncluded() {
        return original.isIncluded();
    }

    public SourcePosition position() {
        return original.position();
    }

    public int hashCode() {
        return original.hashCode();
    }

    public boolean equals(Object o) {
        if (o.getClass() == this.getClass()) {
            if (original.equals(((jContractorPackageDoc) o).original)) {
                return true;
            }
        }
        return false;
    }
}
