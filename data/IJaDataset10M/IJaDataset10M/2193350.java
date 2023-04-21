package org.bug4j.server.processor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Determines the title for a bug based on the stack trace.
 * The title format will be something like "exception-name at class-file:line-number"
 * The idea is that we get the deepest method call that matches the application's packages.
 * Even if the application packages are not specified, we still consider that java.*, javax.* and sun.* are not part of the application.
 */
public class StackAnalyzer {

    private static final Pattern STACK_PATTERN = Pattern.compile("\tat ([^()]*)\\((.*)\\)");

    private static final String[] STD_PACKAGES = { "java.", "javax.", "sun." };

    private List<String> _applicationPackages;

    public StackAnalyzer() {
    }

    public void setApplicationPackages(List<String> applicationPackages) {
        _applicationPackages = applicationPackages;
    }

    /**
     * Analyzes the stack trace and determines a good title.
     *
     * @param stackLines
     * @return
     */
    public String getTitle(List<String> stackLines) {
        final Iterator<String> iterator = stackLines.iterator();
        if (iterator.hasNext()) {
            final String messageLine = iterator.next();
            final String exceptionClass = getExceptionClass(messageLine);
            final String ret = analyze(exceptionClass, iterator);
            return ret;
        }
        return null;
    }

    /**
     * Extracts the causes of a stack
     *
     * @param stackLines the stack trace
     * @return a list of exception class names
     */
    public List<String> getCauses(List<String> stackLines) {
        final List<String> ret = new ArrayList<String>();
        final Iterator<String> iterator = stackLines.iterator();
        if (iterator.hasNext()) {
            final String messageLine = iterator.next();
            final String firstExceptionClass = getExceptionClass(messageLine);
            ret.add(firstExceptionClass);
            while (iterator.hasNext()) {
                final String stackLine = iterator.next();
                if (stackLine.startsWith("Caused by: ")) {
                    final String substring = stackLine.substring("Caused by: ".length());
                    final String exceptionClass = getExceptionClass(substring);
                    ret.add(exceptionClass);
                }
            }
            return ret;
        }
        return null;
    }

    protected String analyze(String exceptionClass, Iterator<String> iterator) {
        String ret = null;
        boolean findBetter = true;
        while (iterator.hasNext()) {
            final String stackLine = iterator.next();
            if (stackLine.startsWith("Caused by: ")) {
                final String substring = stackLine.substring("Caused by: ".length());
                exceptionClass = getExceptionClass(substring);
                findBetter = true;
            } else {
                if (findBetter) {
                    final Matcher matcher = STACK_PATTERN.matcher(stackLine);
                    if (matcher.matches()) {
                        final String methodCall = matcher.group(1);
                        if (isInApplicationPackages(methodCall)) {
                            final String location = matcher.group(2);
                            final int pos = location.indexOf(':');
                            if (pos > 0) {
                                final String shortExceptionClassName;
                                if (exceptionClass != null) {
                                    shortExceptionClassName = getShortExceptionClassName(exceptionClass);
                                } else {
                                    shortExceptionClassName = "Exception";
                                }
                                ret = shortExceptionClassName + " at " + location;
                                findBetter = false;
                            }
                        }
                    }
                }
            }
        }
        return ret;
    }

    static String getShortExceptionClassName(String exceptionClass) {
        final int pos = exceptionClass.lastIndexOf('.');
        return exceptionClass.substring(pos + 1);
    }

    private boolean isInApplicationPackages(String methodCall) {
        if (_applicationPackages != null && !_applicationPackages.isEmpty()) {
            for (String applicationPackage : _applicationPackages) {
                if (methodCall.startsWith(applicationPackage)) {
                    return true;
                }
            }
            return false;
        } else {
            for (String stdPackage : STD_PACKAGES) {
                if (methodCall.startsWith(stdPackage)) {
                    return false;
                }
            }
            return true;
        }
    }

    static String getExceptionClass(String messageLine) {
        final String ret;
        final int pos = messageLine.indexOf(':');
        if (pos < 0) {
            ret = messageLine;
        } else {
            ret = messageLine.substring(0, pos);
        }
        return ret;
    }
}
