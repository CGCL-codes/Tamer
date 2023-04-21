package hla.rti1516e.time;

import hla.rti1516e.LogicalTime;
import hla.rti1516e.exceptions.IllegalTimeArithmetic;
import hla.rti1516e.exceptions.CouldNotEncode;

/**
 * Interface for the time part of the standardized time type HLAfloat64Time. 
 */
public interface HLAfloat64Time extends LogicalTime<HLAfloat64Time, HLAfloat64Interval> {

    boolean isInitial();

    boolean isFinal();

    HLAfloat64Time add(HLAfloat64Interval val) throws IllegalTimeArithmetic;

    HLAfloat64Time subtract(HLAfloat64Interval val) throws IllegalTimeArithmetic;

    HLAfloat64Interval distance(HLAfloat64Time val);

    int compareTo(HLAfloat64Time other);

    int encodedLength();

    void encode(byte[] buffer, int offset) throws CouldNotEncode;

    double getValue();
}
