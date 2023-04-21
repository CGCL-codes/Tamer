package ru.jkff.antro.ui;

import ru.jkff.antro.Call;
import ru.jkff.antro.OurLocation;
import ru.jkff.antro.Trace;

/**
 * Created on 14:18:05 22.03.2008
 *
 * @author jkff
 */
public class TracePredicates {

    public static Predicate<Trace> byLocation(final OurLocation location) {
        return new Predicate<Trace>() {

            public boolean fits(Trace trace) {
                return trace.getCall().location.equals(location);
            }
        };
    }

    public static Predicate<Trace> byFile(final String fileName) {
        return new Predicate<Trace>() {

            public boolean fits(Trace trace) {
                return trace.getCall().location.fileName.equals(fileName);
            }
        };
    }

    public static Predicate<Trace> byTask(final String taskName) {
        return new Predicate<Trace>() {

            public boolean fits(Trace trace) {
                return trace.getCall().kind == Call.Kind.TASK && trace.getCall().name.equals(taskName);
            }
        };
    }

    public static Predicate<Trace> byTarget(final String targetName) {
        return new Predicate<Trace>() {

            public boolean fits(Trace trace) {
                return trace.getCall().kind == Call.Kind.TARGET && trace.getCall().name.equals(targetName);
            }
        };
    }
}
