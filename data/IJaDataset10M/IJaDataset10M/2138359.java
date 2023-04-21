package it.tcon.xbeedriver.resmanager;

import it.tcon.xbeedriver.XBeeNode;
import it.tcon.xbeedriver.XBeeNodeList;
import org.apache.log4j.Logger;
import com.rapplogic.xbee.api.zigbee.ZNetRxIoSampleResponse;
import com.rapplogic.xbee.util.ByteUtils;

public class ZNetIoSampleManager {

    private XBeeNodeList list;

    private ZNetRxIoSampleResponse response;

    private Logger logger = Logger.getLogger(ZNetNodeIdentifierManager.class);

    public ZNetIoSampleManager(ZNetRxIoSampleResponse response, XBeeNodeList list) {
        this.response = response;
        this.list = list;
        String address = ByteUtils.toBase16(this.response.getRemoteAddress64().getAddress());
        logger.debug("ZNET IO sample received by:" + address);
        if (this.list.isPresent(address)) {
            XBeeNode node = this.list.getNode(address);
            if (node != null) {
                if (this.response.containsAnalog()) {
                    logger.debug("\nAnalog0: " + this.response.getAnalog0() + "\nAnalog1: " + this.response.getAnalog1() + "\nAnalog2: " + this.response.getAnalog2() + "\nAnalog3: " + this.response.getAnalog3());
                    int a0 = this.response.getAnalog0();
                    int a1 = this.response.getAnalog1();
                    int a2 = this.response.getAnalog2();
                    int a3 = this.response.getAnalog3();
                    node.setAnalog0(new int[] { a0 });
                    node.setAnalog1(new int[] { a1 });
                    node.setAnalog2(new int[] { a2 });
                    node.setAnalog3(new int[] { a3 });
                    this.list.updateNode(node);
                }
                if (this.response.containsDigital()) {
                    logger.debug("\nDigital0: " + this.response.isD0On() + "\nDigital0: " + this.response.isD1On() + "\nDigital2: " + this.response.isD2On() + "\nDigital3: " + this.response.isD3On() + "\nDigital4: " + this.response.isD4On() + "\nDigital5: " + this.response.isD5On() + "\nDigital6: " + this.response.isD6On() + "\nDigital7: " + this.response.isD7On() + "\nDigital10: " + this.response.isD10On() + "\nDigital11: " + this.response.isD11On() + "\nDigital12: " + this.response.isD12On());
                    if (this.response.isD0Enabled()) node.setDigital0(this.response.isD0On());
                    if (this.response.isD1Enabled()) node.setDigital1(this.response.isD1On());
                    if (this.response.isD2Enabled()) node.setDigital2(this.response.isD2On());
                    if (this.response.isD3Enabled()) node.setDigital3(this.response.isD3On());
                    if (this.response.isD4Enabled()) node.setDigital4(this.response.isD4On());
                    if (this.response.isD5Enabled()) node.setDigital5(this.response.isD5On());
                    if (this.response.isD6Enabled()) node.setDigital6(this.response.isD6On());
                    if (this.response.isD7Enabled()) node.setDigital7(this.response.isD7On());
                    if (this.response.isD10Enabled()) node.setDigital10(this.response.isD10On());
                    if (this.response.isD11Enabled()) node.setDigital11(this.response.isD11On());
                    if (this.response.isD12Enabled()) node.setDigital12(this.response.isD12On());
                    this.list.updateNode(node);
                }
            } else logger.debug("Node null");
        } else logger.debug("node not present on list");
    }
}
