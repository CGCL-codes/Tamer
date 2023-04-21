package org.sucri.floxs.ext.data;

import org.sucri.floxs.ext.JsonConfig;
import org.sucri.floxs.ext.widget.ExtWidget;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Wen Yu
 * Date: Jul 19, 2007
 * Time: 9:13:38 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class DataReader extends ExtWidget implements Reader {

    protected List<Record> mRecords = new ArrayList<Record>();

    protected DataReader(String var) {
        super(var);
    }

    public void addRecord(Record s) {
        mRecords.add(s);
    }
}
