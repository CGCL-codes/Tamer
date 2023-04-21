package edu.ucdavis.genomics.metabolomics.util.status;

import edu.ucdavis.genomics.metabolomics.util.status.priority.Priority;

/**
 * some predifined typs of types and events
 * 
 * @author wohlgemuth
 * @version Apr 22, 2006
 */
public interface Reports {

    ReportType JOB = new ReportType("job", "a calculation job", Priority.DEBUG);

    ReportType EXPERIMENT = new ReportType("experiment", "a binbase experiment", Priority.INFO);

    ReportType UPDATE_DATABASE = new ReportType("update", "the binbase is being updated", Priority.INFO);

    ReportType CLASS = new ReportType("class", "a binbase class", Priority.INFO);

    ReportType SAMPLE = new ReportType("sample", "a binbase sample", Priority.DEBUG);

    ReportType SAMPLE_POSTMATCH_JOB = new ReportType("sample postmatching", "a binbase sample", Priority.DEBUG);

    ReportType FILE = new ReportType("file", "a file", Priority.DEBUG);

    ReportType SUB_JOB = new ReportType("sub job", "a sub job", Priority.DEBUG);

    ReportType CACHE = new ReportType("cache", "internal cache", Priority.DEBUG);

    ReportEvent STARTED = new ReportEvent("start", "something started", Priority.DEBUG);

    ReportEvent RUNNING = new ReportEvent("running", "something is running", Priority.DEBUG);

    ReportEvent STOP = new ReportEvent("stopp", "it stoped", Priority.DEBUG);

    ReportEvent ABORT = new ReportEvent("abort", "it was aborted", Priority.INFO);

    ReportEvent FAILED = new ReportEvent("failed", "something failed", Priority.INFO);

    ReportEvent DONE = new ReportEvent("done", "everything is done", Priority.DEBUG);

    ReportEvent SCHEDULE = new ReportEvent("schedule", "something is scheduled", Priority.INFO);

    ReportEvent WARNING = new ReportEvent("warning", "we issued a warning", Priority.WARNING);

    ReportType MESSAGE = new ReportType("message", "a message recieved", Priority.DEBUG);

    ReportEvent LOAD = new ReportEvent("load", "something is loaded", Priority.DEBUG);

    ReportEvent STORE = new ReportEvent("store", "something is stored", Priority.DEBUG);

    ReportEvent WAITING = new ReportEvent("waiting", "we are waiting for something", Priority.DEBUG);

    ReportEvent SLEEPING = new ReportEvent("sleeping", "something is sleeping", Priority.DEBUG);

    ReportType LOCK = new ReportType("lock", "the lock of a ressource", Priority.DEBUG);

    ReportEvent AQUIRED = new ReportEvent("aquired", "something was aquired", Priority.DEBUG);

    ReportEvent ATTEMPTED = new ReportEvent("attempted", "something was attempted", Priority.DEBUG);

    ReportEvent TRYING = new ReportEvent("trying", "something is trying", Priority.DEBUG);

    ReportEvent SUCCESSFUL = new ReportEvent("successful", "something was successful", Priority.DEBUG);

    ReportEvent RELEASED = new ReportEvent("released", "something was released", Priority.DEBUG);

    ReportType TRIGGER = new ReportType("trigger", "report was based on a trigger", Priority.DEBUG);

    ReportEvent SEND_EMAIL = new ReportEvent("send email", "we are sending an emai", Priority.INFO);

    ReportType KEY_VALIDATION = new ReportType("key validation", "an attempted key validation", Priority.WARNING);

    ReportType DSL = new ReportType("dsl calculation", "a dsl calculation", Priority.INFO);
}
