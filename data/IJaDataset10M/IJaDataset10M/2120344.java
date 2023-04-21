package viewerdaemon;

import java.util.*;
import java.nio.*;
import java.nio.channels.*;
import java.sql.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import viewerprotocol.*;
import java.io.*;
import java.net.*;
import java.io.*;
import java.net.*;
import viewerdaemon.MeasureValue;

public class Measure {

    public enum MeasureQuality {

        MQ_BAD_GPS_SYNC, MQ_DATA_INVALID, MQ_NOT_CALIBRATED, MQ_EXCESSIVE_DELAY, MQ_TRIGGER_MASK, MQ_MASK
    }

    int measurandId;

    Timestamp timestamp;

    int delay;

    MeasureValue measure;

    public Measure() {
    }

    public void init(Measure m, Measurand md, Timestamp t) {
        m.measurandId = md.getMeasurandId();
        m.measure.type = md.getDataType();
        m.timestamp = t;
        m.delay = 0;
        m.measure.init();
    }

    public boolean isUnknown(Measure m) {
        return m.measure.type == MeasureValue.DataType.DT_UNKNOWN;
    }

    public void setTimeStamp(Measure m, Timestamp t) {
        m.timestamp = t;
    }

    public void setPhasor(MeasureValue.Phasor val) {
        if (measure instanceof MeasureValue.Phasor) {
            ((MeasureValue.Phasor) measure).init();
        }
    }

    public void setScalar(MeasureValue.Scalar val) {
        if (measure instanceof MeasureValue.Phasor) {
            ((MeasureValue.Scalar) measure).init();
        }
    }

    public void setTriState(MeasureValue.TriState val) {
        if (measure instanceof MeasureValue.TriState) {
            ((MeasureValue.TriState) measure).init();
        }
    }
}
