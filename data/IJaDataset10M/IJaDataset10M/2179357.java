package com.protomatter.syslog;

import java.io.PrintWriter;
import java.util.*;
import java.text.*;
import java.net.*;
import java.io.*;
import java.lang.reflect.*;
import org.jdom.*;
import com.protomatter.xml.*;
import com.protomatter.util.*;

/**
 *  A simple log entry formatter.  This class is used by several of
 *  the included <tt>Syslogger</tt> implementations to format their
 *  log entries.
 */
public class SimpleSyslogTextFormatter implements SyslogTextFormatter {

    private static char[] cr = System.getProperty("line.separator").toCharArray();

    private static char[] rb = " [".toCharArray();

    private static char[] lb = "] ".toCharArray();

    private static char rb_ns = '[';

    private static char[] sp = "  ".toCharArray();

    private static char sp_1 = ' ';

    private static char[] DEBUG = "DBUG".toCharArray();

    private static char[] INFO = "INFO".toCharArray();

    private static char[] WARNING = "WARN".toCharArray();

    private static char[] ERROR = "EROR".toCharArray();

    private static char[] FATAL = "FTAL".toCharArray();

    private static char[] UNKNOWN_LEVEL = "????".toCharArray();

    private static char[] CH_ALL_CHANNEL = "[ALL_CHANNEL    ] ".toCharArray();

    private static char[] CH_DEF_CHANNEL = "[DEFAULT_CHANNEL] ".toCharArray();

    private DateFormat dateFormat = null;

    private TimeZone dateFormatTimeZone = TimeZone.getDefault();

    private String dateFormatString = null;

    private long lastDate = -1;

    private char[] lastDateString = null;

    private int dateFormatCacheTime = 1000;

    private boolean showChannel = false;

    private boolean showThreadName = false;

    private boolean showHostName = false;

    /**
   *  Default constructor.
   */
    public SimpleSyslogTextFormatter() {
        super();
        setDateFormat("HH:mm:ss MM/dd");
    }

    /**
   *  Format the given log entry.
   */
    public void formatLogEntry(StringBuffer b, SyslogMessage message) {
        synchronized (b) {
            b.append(formatDate(message.time));
            b.append(rb);
            b.append(getStringForLevel(message.level));
            b.append(lb);
            if (showChannel) {
                if (message.channel.equals(Syslog.ALL_CHANNEL)) {
                    b.append(CH_ALL_CHANNEL);
                } else if (message.channel.equals(Syslog.DEFAULT_CHANNEL)) {
                    b.append(CH_DEF_CHANNEL);
                } else {
                    b.append(rb_ns);
                    StringUtil.pad(b, message.channel, 15);
                    b.append(lb);
                }
            }
            if (showHostName) {
                StringUtil.pad(b, getHostname(message.host), 10);
                b.append(sp_1);
            }
            if (showThreadName) {
                b.append(rb_ns);
                b.append(message.threadName);
                b.append(lb);
            }
            trimFromLastPeriod(b, message.loggerClassname, 20);
            if (message.msg != null) {
                b.append(sp);
                b.append(message.msg);
            }
            b.append(cr);
            if (message.detail != null) {
                formatMessageDetail(b, message);
            }
        }
    }

    public void formatMessageDetail(StringBuffer b, SyslogMessage message) {
        if (message.detail == null) return;
        if (message.detail instanceof Throwable) {
            ByteArrayOutputStream bs = new ByteArrayOutputStream();
            PrintWriter pw = new PrintWriter(bs);
            Throwable e = (Throwable) message.detail;
            Throwable e2 = e;
            Object[] junk = null;
            String methodCalled = null;
            while (e2 != null) {
                if (methodCalled != null) {
                    pw.print(methodCalled);
                    pw.print("(): ");
                }
                e2.printStackTrace(pw);
                junk = getNextException(e2);
                e2 = (Throwable) junk[1];
                methodCalled = (String) junk[0];
            }
            pw.flush();
            b.append(bs.toString());
            return;
        }
        b.append(message.detail.toString());
    }

    /**
   *  Get the "next" exception in this series.  This method
   *  will look for a no-arg method on the given object that
   *  returns a subclass of throwable.  It will skip the
   *  "fillInStackTrace" method.  The return type is
   *  a two-element object array.  The first element is
   *  the method name (or null) that was called to get
   *  the next exception, and the second element is the
   *  instance of the Throwable.
   */
    protected Object[] getNextException(Throwable t) {
        if (t == null) return null;
        Method methods[] = t.getClass().getMethods();
        Class pt[] = null;
        Class rt = null;
        boolean isFIST = false;
        String name = null;
        Object junk[] = new Object[2];
        for (int i = 0; i < methods.length; i++) {
            rt = methods[i].getReturnType();
            pt = methods[i].getParameterTypes();
            name = methods[i].getName();
            isFIST = name.equals("fillInStackTrace");
            if (!isFIST && pt.length == 0 && Throwable.class.isAssignableFrom(rt)) {
                try {
                    junk[0] = name;
                    junk[1] = (Throwable) methods[i].invoke(t, new Object[0]);
                    return junk;
                } catch (Throwable x) {
                    return junk;
                }
            }
        }
        return junk;
    }

    protected char[] getStringForLevel(int level) {
        switch(level) {
            case Syslog.DEBUG:
                return DEBUG;
            case Syslog.INFO:
                return INFO;
            case Syslog.WARNING:
                return WARNING;
            case Syslog.ERROR:
                return ERROR;
            case Syslog.FATAL:
                return FATAL;
            default:
                return UNKNOWN_LEVEL;
        }
    }

    protected String getHostname(InetAddress host) {
        String ip = host.getHostAddress();
        String name = host.getHostName();
        if (ip.equals(name)) return ip;
        int idx = name.indexOf(".");
        if (idx == -1) return name;
        return name.substring(0, idx);
    }

    /**
   *  Set the format for logging dates.
   */
    public void setDateFormat(String format) {
        this.dateFormatString = format;
        this.dateFormat = new SimpleDateFormat(format);
        setDateFormatTimezone(TimeZone.getDefault());
        resetDateFormat();
    }

    /**
   *  Get the format for logging dates.
   */
    public String getDateFormat() {
        return this.dateFormatString;
    }

    /**
   *  Set wether we should show the host name in the output.
   *  If this is set to <tt>true</tt> and the hostname
   *  has not been set on Syslog yet, the <tt>Syslog.setLocalHostName()</tt>
   *  method is called.
   */
    public void setShowHostName(boolean showHostName) {
        this.showHostName = showHostName;
        if (showHostName && (Syslog.getLocalHostName() == null)) {
            Syslog.setLocalHostName();
        }
    }

    /**
   *  Get wether we should show the host name in the output.
   */
    public boolean getShowHostName() {
        return this.showChannel;
    }

    /**
   *  Set wether we should show the thread name in the output.
   */
    public void setShowThreadName(boolean showThreadName) {
        this.showThreadName = showThreadName;
    }

    /**
   *  Get wether we should show the thread name in the output.
   */
    public boolean getShowThreadName() {
        return this.showThreadName;
    }

    /**
   *  Set wether we should show the channel name in the output.
   */
    public void setShowChannel(boolean showChannel) {
        this.showChannel = showChannel;
    }

    /**
   *  Get wether we should show the channel name in the output.
   */
    public boolean getShowChannel() {
        return this.showChannel;
    }

    /**
   *  Set the number of milliseconds a date format should
   *  be cached.  This is also the resolution of the timestamp
   *  in log entries.  Default is 1000ms (1 second).
   */
    public void setDateFormatCacheTime(int cacheTime) {
        this.dateFormatCacheTime = cacheTime;
    }

    /**
   *  Get the number of milliseconds a date format should
   *  be cached.  This is also the resolution of the timestamp
   *  in log entries.  Default is 1000ms (1 second).
   */
    public int getDateFormatCacheTime() {
        return this.dateFormatCacheTime;
    }

    /**
   *  Set the timezone of the date format.  Default
   *  is <tt>TimeZone.getDefault()</tt>.
   */
    public void setDateFormatTimezone(TimeZone zone) {
        this.dateFormatTimeZone = zone;
        this.dateFormat.setTimeZone(zone);
    }

    /**
   *  Get the timezone of the date format.
   */
    public TimeZone getDateFormatTimezone() {
        return this.dateFormatTimeZone;
    }

    /**
   *  Format the given date with the dateformat that's been set.
   *  This will cache the date until it's been long enough
   *  for the date format cache time to have expired.
   *
   *  @see #setDateFormatCacheTime
   */
    protected char[] formatDate(long theDate) {
        if (lastDate == -1 || theDate > lastDate + dateFormatCacheTime) {
            synchronized (dateFormat) {
                if (lastDate == -1 || theDate > lastDate + dateFormatCacheTime) {
                    lastDateString = dateFormat.format(new Date(theDate)).toCharArray();
                    lastDate = theDate;
                }
            }
        }
        return lastDateString;
    }

    /**
   *  Given something like "foo.bar.Baz" this will return "Baz".
   */
    protected void trimFromLastPeriod(StringBuffer b, String s, int width) {
        char data[] = s.toCharArray();
        int i = data.length;
        for (; --i >= 0 && data[i] != '.'; ) ;
        i++;
        int len = data.length - i;
        b.append(data, i, len);
        len = width - len;
        for (; --len >= 0; ) b.append(sp_1);
    }

    protected String trimFromLastPeriod(String s) {
        int pos = s.lastIndexOf('.');
        if (pos >= 0) return s.substring(pos + 1); else return s;
    }

    /**
   *  Reset the <tt>formatDate(...)</tt> method so that it's
   *  guaranteed to not return a cached date string the
   *  next time it's called.
   */
    public void resetDateFormat() {
        this.lastDate = -1;
    }

    /**
   *  Get the log header.  This simply returns an empty string.
   */
    public String getLogHeader() {
        return "";
    }

    /**
   *  Get the log footer.  This simply returns an empty string.
   */
    public String getLogFooter() {
        return "";
    }

    /**
   *  Configure this text formatter given the XML element.
   *  The <tt>&lt;Format&gt;</tt> element should look like this:<P>
   *
   *  <TABLE BORDER=1 CELLPADDING=4 CELLSPACING=0 WIDTH="90%">
   *  <TR><TD>
   *  <PRE><B>
   *
   *  &lt;Format class="com.protomatter.syslog.SimpleSyslogTextFormatter" &gt;
   *
   *    &lt;showChannel&gt;<i>true|false</i>&lt;/showChannel&gt;
   *    &lt;showThreadName&gt;<i>true|false</i>&lt;/showThreadName&gt;
   *    &lt;showHostName&gt;<i>true|false</i>&lt;/showHostName&gt;
   *    &lt;dateFormat&gt;<i>DateFormat</i>&lt;/dateFormat&gt;
   *    &lt;dateFormatTimeZone&gt;<i>TimeZoneName</i>&lt;/dateFormatTimeZone&gt;
   *    &lt;dateFormatCacheTime&gt;<i>CacheTimeout</i>&lt;/dateFormatCacheTime&gt;
   *
   *  &lt;/Format&gt;
   *  </B></PRE>
   *  </TD></TR></TABLE><P>
   *
   *  <TABLE BORDER=1 CELLPADDING=2 CELLSPACING=0 WIDTH="90%">
   *  <TR CLASS="TableHeadingColor">
   *  <TD COLSPAN=3><B>Element</B></TD>
   *  </TR>
   *  <TR CLASS="TableHeadingColor">
   *  <TD><B>name</B></TD>
   *  <TD><B>value</B></TD>
   *  <TD><B>required</B></TD>
   *  </TR>
   *
   *  <TR CLASS="TableRowColor">
   *  <TD VALIGN=TOP><TT>showChannel</TT></TD>
   *  <TD><tt>true</tt> or <tt>false</tt> -- decide if the channel
   *  name should appear in the log.
   *  </TD>
   *  <TD VALIGN=TOP>no (default is <tt>false</tt>)</TD>
   *  </TR>
   *
   *  <TR CLASS="TableRowColor">
   *  <TD VALIGN=TOP><TT>showThreadName</TT></TD>
   *  <TD><tt>true</tt> or <tt>false</tt> -- decide if the thread
   *  name should appear in the log.
   *  </TD>
   *  <TD VALIGN=TOP>no (default is <tt>false</tt>)</TD>
   *  </TR>
   *
   *  <TR CLASS="TableRowColor">
   *  <TD VALIGN=TOP><TT>showHostName</TT></TD>
   *  <TD><tt>true</tt> or <tt>false</tt> -- decide if the host name
   *  name should appear in the log.
   *  </TD>
   *  <TD VALIGN=TOP>no (default is <tt>false</tt>)</TD>
   *  </TR>
   *
   *  <TR CLASS="TableRowColor">
   *  <TD VALIGN=TOP><TT>dateFormat</TT></TD>
   *  <TD>A date format that can be understood by the
   *  <tt>java.text.SimpleDateFormat</tt> format class.
   *  </TD>
   *  <TD VALIGN=TOP>no (default is "<tt>HH:mm:ss MM/dd</tt>")</TD>
   *  </TR>
   *
   *  <TR CLASS="TableRowColor">
   *  <TD VALIGN=TOP><TT>dateFormatTimeZone</TT></TD>
   *  <TD>The name of the timezone to use for formatting timestamps.
   *  </TD>
   *  <TD VALIGN=TOP>no (default is the current time zone)</TD>
   *  </TR>
   *
   *  <TR CLASS="TableRowColor">
   *  <TD VALIGN=TOP><TT>dateFormatCacheTime</TT></TD>
   *  <TD>How long (in milliseconds) to cache a created timestamp formatting object
   *  </TD>
   *  <TD VALIGN=TOP>no (default is <tt>1000</tt>)</TD>
   *  </TR>
   *
   *  </TABLE><P>
   */
    public void configure(Element e) {
        if (e == null) return;
        String tmp = e.getChildTextTrim("showChannel", e.getNamespace());
        setShowChannel("true".equalsIgnoreCase(tmp));
        tmp = e.getChildTextTrim("showThreadName", e.getNamespace());
        setShowThreadName("true".equalsIgnoreCase(tmp));
        tmp = e.getChildTextTrim("showHostName", e.getNamespace());
        setShowHostName("true".equalsIgnoreCase(tmp));
        tmp = e.getChildTextTrim("dateFormat", e.getNamespace());
        if (tmp != null) setDateFormat(tmp);
        tmp = e.getChildTextTrim("dateFormatTimeZone", e.getNamespace());
        if (tmp != null) setDateFormatTimezone(TimeZone.getTimeZone(tmp));
        tmp = e.getChildTextTrim("dateFormatCacheTime", e.getNamespace());
        if (tmp != null) setDateFormatCacheTime(Integer.parseInt(tmp));
    }

    /**
   *
   */
    public Element getConfiguration(Element element) {
        element.addChild((new Element("showChannel")).setText(String.valueOf(showChannel)));
        element.addChild((new Element("showThreadName")).setText(String.valueOf(showThreadName)));
        element.addChild((new Element("showHostName")).setText(String.valueOf(showHostName)));
        element.addChild((new Element("dateFormat")).setText(dateFormatString));
        element.addChild((new Element("dateFormatCacheTime")).setText(String.valueOf(dateFormatCacheTime)));
        element.addChild((new Element("dateFormatTimeZone")).setText(dateFormatTimeZone.getID()));
        return element;
    }
}
