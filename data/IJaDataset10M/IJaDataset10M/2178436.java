package de.simpleworks.jmeter.protocol.rstatd.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

/**
 * convert RstatdStatsTime to XML.
 * 
 * @author Marcin Brzoza
 * @since Mar 12, 2008
 */
public final class ConvertRstatdStatsTimeToXml {

    private static final Logger logger = LoggingManager.getLoggerForClass();

    private static void addElement(final Element _root, final String _tag, final String _value) {
        final Element element = new Element(_tag);
        element.setText(_value);
        _root.addContent(element);
    }

    private static void addElement(final Element _root, final String _tag, final int _value) {
        final Element element = new Element(_tag);
        element.setText(Integer.toString(_value));
        _root.addContent(element);
    }

    private static void addElement(final Element _root, final String _tag, final long _value) {
        final Element element = new Element(_tag);
        element.setText(Long.toString(_value));
        _root.addContent(element);
    }

    private static void addElement(final Element _root, final String _tag, final RstatdTimeVal _value) {
        final Element element = new Element(_tag);
        element.setAttribute(new Attribute(RstatdStatsTimeXmlTag.ATTRIBUTE_TIME_TVSEC, Integer.toString(_value.tv_sec)));
        element.setAttribute(new Attribute(RstatdStatsTimeXmlTag.ATTRIBUTE_TIME_TVUSEC, Integer.toString(_value.tv_usec)));
        _root.addContent(element);
    }

    public static final Element toElement(final RstatdStatsTime _times) {
        final Element result;
        if (_times != null) {
            result = new Element(RstatdStatsTimeXmlTag.NODE);
            Element element;
            addElement(result, RstatdStatsTimeXmlTag.HOSTNAME, _times.hostname);
            addElement(result, RstatdStatsTimeXmlTag.TIMESTAMP, _times.timestamp);
            element = new Element(RstatdStatsTimeXmlTag.CPU);
            element.setAttribute(new Attribute(RstatdStatsTimeXmlTag.ATTRIBUTE_CPU_USER, Integer.toString(_times.cp_time[RstatdConsts.RSTAT_CPU_USER])));
            element.setAttribute(new Attribute(RstatdStatsTimeXmlTag.ATTRIBUTE_CPU_NICE, Integer.toString(_times.cp_time[RstatdConsts.RSTAT_CPU_NICE])));
            element.setAttribute(new Attribute(RstatdStatsTimeXmlTag.ATTRIBUTE_CPU_SYSTEM, Integer.toString(_times.cp_time[RstatdConsts.RSTAT_CPU_SYS])));
            element.setAttribute(new Attribute(RstatdStatsTimeXmlTag.ATTRIBUTE_CPU_IDLE, Integer.toString(_times.cp_time[RstatdConsts.RSTAT_CPU_IDLE])));
            result.addContent(element);
            for (int i = 0; i < RstatdConsts.RSTAT_DK_NDRIVE; i++) {
                element = new Element(RstatdStatsTimeXmlTag.DISK);
                element.setAttribute(new Attribute(RstatdStatsTimeXmlTag.ATTRIBUTE_DISK_NUMBER, Integer.toString(i)));
                element.setAttribute(new Attribute(RstatdStatsTimeXmlTag.ATTRIBUTE_DISK_XFER, Integer.toString(_times.dk_xfer[i])));
                result.addContent(element);
            }
            addElement(result, RstatdStatsTimeXmlTag.V_PGPGIN, _times.v_pgpgin);
            addElement(result, RstatdStatsTimeXmlTag.V_PGPGOUT, _times.v_pgpgout);
            addElement(result, RstatdStatsTimeXmlTag.V_PSWPIN, _times.v_pswpin);
            addElement(result, RstatdStatsTimeXmlTag.V_PSWPOUT, _times.v_pswpout);
            addElement(result, RstatdStatsTimeXmlTag.V_INTR, _times.v_intr);
            addElement(result, RstatdStatsTimeXmlTag.V_SWTCH, _times.v_swtch);
            addElement(result, RstatdStatsTimeXmlTag.IF_IPACKETS, _times.if_ipackets);
            addElement(result, RstatdStatsTimeXmlTag.IF_IERRORS, _times.if_ierrors);
            addElement(result, RstatdStatsTimeXmlTag.IF_OERRORS, _times.if_oerrors);
            addElement(result, RstatdStatsTimeXmlTag.IF_COLLISIONS, _times.if_collisions);
            element = new Element(RstatdStatsTimeXmlTag.AVENRUN);
            element.setAttribute(new Attribute(RstatdStatsTimeXmlTag.ATTRIBUTE_AVENRUN_1, Integer.toString(_times.avenrun[RstatdConsts.RSTAT_AVEN_MIN_1])));
            element.setAttribute(new Attribute(RstatdStatsTimeXmlTag.ATTRIBUTE_AVENRUN_5, Integer.toString(_times.avenrun[RstatdConsts.RSTAT_AVEN_MIN_5])));
            element.setAttribute(new Attribute(RstatdStatsTimeXmlTag.ATTRIBUTE_AVENRUN_15, Integer.toString(_times.avenrun[RstatdConsts.RSTAT_AVEN_MIN_15])));
            result.addContent(element);
            addElement(result, RstatdStatsTimeXmlTag.TIME_BOOT, _times.boottime);
            addElement(result, RstatdStatsTimeXmlTag.TIME_CUR, _times.curtime);
        } else {
            result = null;
        }
        return result;
    }

    public static final String getXML(final Element _element) {
        String result = null;
        if (_element != null) {
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            final XMLOutputter outputter = new XMLOutputter();
            try {
                outputter.output(_element, out);
                result = new String(out.toByteArray());
            } catch (final IOException ex) {
                logger.error("can't write XML to ByteOutputStream.", ex);
                UtilIO.close(out);
                outputter.clone();
            }
        }
        return result;
    }

    public static final String getXML(final RstatdStatsTime _times) {
        final Element element = toElement(_times);
        return element == null ? null : getXML(element);
    }
}
