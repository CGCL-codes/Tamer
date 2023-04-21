package net.community.chest.rrd4j.common.jmx;

import net.community.chest.jmx.dom.MBeanFeatureDescriptor;
import net.community.chest.rrd4j.common.core.DsDefExt;
import net.community.chest.util.datetime.TimeUnits;
import org.rrd4j.DsType;
import org.w3c.dom.Element;

/**
 * <P>Copyright 2008 as per GPLv2</P>
 *
 * @author Lyor G.
 * @since Jan 10, 2008 3:23:36 PM
 */
public class MBeanDsDef extends DsDefExt {

    public MBeanDsDef(String dsName, DsType dsType, long heartbeat, double minValue, double maxValue) {
        super(dsName, dsType, heartbeat, minValue, maxValue);
    }

    public MBeanDsDef(String dsName, DsType dsType, TimeUnits htUnits, long htValue, double minValue, double maxValue) {
        super(dsName, dsType, htUnits, htValue, minValue, maxValue);
    }

    public MBeanDsDef(String dsName, DsType dsType, long heartbeat) {
        super(dsName, dsType, heartbeat);
    }

    public MBeanDsDef(String dsName, DsType dsType, TimeUnits htUnits, long htValue) {
        super(dsName, dsType, htUnits, htValue);
    }

    private String _attrName;

    public String getMBeanAttributeName() {
        return _attrName;
    }

    public void setMBeanAttributeName(String name) {
        _attrName = name;
    }

    public String setMBeanAttributeName(Element elem) throws Exception {
        final String val = elem.getAttribute(MBeanFeatureDescriptor.NAME_ATTR);
        if ((val != null) && (val.length() > 0)) setMBeanAttributeName(val);
        return val;
    }

    public MBeanDsDef(Element elem) throws Exception {
        super(elem);
        setMBeanAttributeName(elem);
    }
}
