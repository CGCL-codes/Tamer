package net.sf.borg.model;

import java.io.BufferedWriter;
import java.util.Date;

/**
 * Interface CalWriter
 * 
 * This interface defines a single method to write a representation
 * of the calendar from a startDate to an endDate.  The particular
 * representation is defined by the implementing class
 * 
 * Created in UF Software Engineering - Summer 2009
 * @author Matthew Carroll
 */
public interface CalWriter {

    void writeCal(BufferedWriter bw, Date startDate, Date endDate);
}
