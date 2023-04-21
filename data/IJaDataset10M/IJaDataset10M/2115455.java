package org.dbwiki.data.time;

import org.dbwiki.data.provenance.Provenance;

/** Interface to Version information.
 * A Version is a struct containing a name, number, TimestampPrinter, Provenance, and time
 * The name is basically a date-time string stored in the database.
 * FIXME #time: Eliminate version names, recalculate from _time instead
 * @author jcheney
 *
 */
public class Version {

    private String _name;

    private int _number;

    private VersionIndex _index;

    private Provenance _provenance;

    private long _time;

    public Version(int number, String name, long time, Provenance provenance, VersionIndex index) {
        _name = name;
        _number = number;
        _provenance = provenance;
        _time = time;
        _index = index;
    }

    public String name() {
        return _name;
    }

    public int number() {
        return _number;
    }

    public VersionIndex index() {
        return _index;
    }

    public Provenance provenance() {
        return _provenance;
    }

    public long time() {
        return _time;
    }

    public String toString() {
        return "[" + _name + ", " + _number + "," + _time + "]";
    }
}
