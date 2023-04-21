package au.edu.diasb.annotation.danno.admin;

/**
 * The API used by benchmarks to report results.
 * 
 * @author scrawley
 */
public interface Reporter {

    void report(String message);

    void report(String prop, String value);

    void report(long start, long count, String desc);
}
