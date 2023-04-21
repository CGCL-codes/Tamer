package de.fmf.db.berkeley;

import static com.sleepycat.persist.model.Relationship.MANY_TO_ONE;
import java.util.ArrayList;
import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;
import com.sleepycat.persist.model.SecondaryKey;

@Entity
public class SimpleEntityClass {

    @PrimaryKey
    private String pKey;

    @SecondaryKey(relate = MANY_TO_ONE)
    private String sKey;

    @SecondaryKey(relate = MANY_TO_ONE)
    private String line;

    public void setPKey(String data) {
        pKey = data;
    }

    public void setSKey(String data) {
        sKey = data;
    }

    public String getPKey() {
        return pKey;
    }

    public String getSKey() {
        return sKey;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }
}
