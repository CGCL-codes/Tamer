package org.echarts.servlet.sip;

/** Interface used to mark an ECharts Machine as one for use in originating region.
 *  If a machine that implements this interface receives an initial request
 *  in a region other than originating, that request will be proxied on, without
 *  invoking the machine.
 */
public interface OriginatingFeature {

    static final String rcsid = "$Name:  $ $Id: OriginatingFeature.java 2115 2012-02-15 17:11:46Z yotommy $";
}
