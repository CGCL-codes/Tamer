package ca.ucalgary.cpsc.ebe.fitClipseRunner.core.data.history;

import java.sql.Timestamp;

public interface IResult {

    public Timestamp getStartTime();

    public Timestamp getEndTime();

    public String toString();

    public String getID();

    public int passed();

    public int failed();

    public int neverPassed();

    public int exceptions();
}
