package com.google.code.jholidays.io.csv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.google.code.jholidays.core.DescriptorCollection;
import com.google.code.jholidays.core.EventDescriptor;
import com.google.code.jholidays.events.DependentEvent;
import com.google.code.jholidays.events.FixedEvent;
import com.google.code.jholidays.events.FloatingEvent;
import com.google.code.jholidays.io.IDescriptorReader;

/**
 * Represents reader which can process CSV (comma-separated) file and build
 * {@link DescriptorCollection}. Each line in csv file must conform to special
 * format (<i>HH</i> below is place-holder for separator inside CSV file):
 * <code>
 * <p>Regex:(\\d+)HH(.+)HH(.+)HH(\\d*)HH(.+)$
 * <br>Verbal: id HH event name HH event description HH parentID HH expression
 * </code>
 * <p>
 * <b>Line example</b>: <br>
 * <code>1;first event;no description;;01.01</code>
 * <p>
 * If at least one line in input csv file doesn't match given format then the
 * whole file is considered to be corrupted and nothing is parsed. This
 * behaviour provides additional protection and enforces data integrity.
 * <p>
 * <b>CSV file example</b>: <code>
 * <br>1;first;none;;01.01
 * <br>2;second;none;1;-12
 * <br>3;third;none;;2%Sun%Apr
 * <br>4;fourth;none;3;+2
 * <br>5;fifth;none;4;+1
 * </code>
 * <p>
 * For separator <tt>;</tt> char is used. There are 5 event descriptors in this
 * example CSV file, so {@link #read()} will produce collection of 5 event
 * descriptors. First event is {@link FixedEvent} with id = <tt>1</tt>, name =
 * <tt>first</tt> and description = <tt>none</tt>. Second one is
 * {@link DependentEvent} with id = <tt>2</tt> and parentID = <tt>1</tt>. Third
 * event is {@link FloatingEvent}. It has 2 childs: event 4 and event 5.
 * <p>
 * Here are dates of all event dates for 2009 year in this example CSV file:
 * <code>
 * <br>Thu Jan 01 00:00:00 MSK 2009
 * <br>Sat Dec 20 00:00:00 MSK 2008
 * <br>Sun Apr 12 00:00:00 MSD 2009
 * <br>Tue Apr 14 00:00:00 MSD 2009
 * <br>Wed Apr 15 00:00:00 MSD 2009
 * </code>
 * 
 * 
 * @see IDescriptorReader
 * @see EventDescriptor
 * @author tillias
 * 
 */
public class CsvReader implements IDescriptorReader {

    public CsvReader(String fileName, String delimiter) {
        this.fileName = fileName;
        this.delimiter = delimiter;
    }

    /**
     * {@inheritDoc}
     * <p>
     * This implementation parses CSV (comma-separated) file. If there are
     * malformed string(s) inside CSV file then the whole file is considered to
     * be corrupted and returns method null
     */
    @Override
    public DescriptorCollection read() {
        DescriptorCollection result = null;
        File f = new File(fileName);
        if (f.exists()) {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(f));
                String str = null;
                Pattern regex = compileRegEx(delimiter);
                result = new DescriptorCollection();
                while ((str = reader.readLine()) != null) {
                    str = str.trim();
                    Matcher matcher = regex.matcher(str);
                    if (matcher.matches()) {
                        int eventID = Integer.valueOf(matcher.group(1));
                        String eventName = matcher.group(2);
                        String eventDescription = matcher.group(3);
                        int parentID = -1;
                        if (!matcher.group(4).equals("")) {
                            parentID = Integer.valueOf(matcher.group(4));
                        } else parentID = EventDescriptor.ROOT_ID;
                        String eventExpression = matcher.group(5);
                        EventDescriptor descriptor = new EventDescriptor(eventID, parentID);
                        descriptor.setName(eventName);
                        descriptor.setDescription(eventDescription);
                        descriptor.setExpression(eventExpression);
                        result.add(descriptor);
                    } else throw new Exception("Illegal input string");
                }
            } catch (Exception e) {
                result = null;
            } finally {
                if (reader != null) closeReader(reader);
            }
        }
        return result;
    }

    private void closeReader(Reader reader) {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
            }
        }
    }

    /**
     * Attempts to compile regex for parsing {@link EventDescriptor} using given
     * delimeter. <br>
     * Format: <code>
     * <p>
     * (\\d+)HH(.+)HH(.+)HH(\\d*)HH(.+)$
     * <p><tt>HH - delimiter</tt>
     * </code>
     * <p>
     * First group of numbers specifies event id. Second and thirs - its name
     * and description. Fourth group specifies parent id (might be empty). Final
     * group specifies expression
     * 
     * @param idelimiter
     *            Delimiter which will be used instead <tt>HH</tt>.
     * @return Compiled regex if succeded, null otherwise
     */
    private Pattern compileRegEx(String idelimiter) {
        Pattern result = null;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("^(\\d+)");
            sb.append(idelimiter);
            sb.append("(.+)");
            sb.append(idelimiter);
            sb.append("(.*)");
            sb.append(idelimiter);
            sb.append("(\\d*)");
            sb.append(idelimiter);
            sb.append("(.+)$");
            result = Pattern.compile(sb.toString());
        } catch (Exception e) {
            result = null;
        }
        return result;
    }

    String fileName;

    String delimiter;
}
