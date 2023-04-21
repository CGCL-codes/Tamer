package com.itbs.util;

import junit.framework.TestCase;

/**
 * @author Alex Rass
 * @since Apr 25, 2005
 */
public class UTestXMLNodeIterator extends TestCase {

    public static final String testXML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><Executions><Execution>\n" + " <TraderNo>4109</TraderNo>\n" + " <TraderName>SCOTT SILVER</TraderName>\n" + " <Symbol>VNTN</Symbol>\n" + " <Side>S</Side>\n" + " <Qty>200000</Qty>\n" + " <LvsQty>200000</LvsQty>\n" + " <Broker>STSS</Broker>\n" + " <Price>0.003</Price>\n" + " <OrderRefno>2334002823</OrderRefno>\n" + "</Execution>\n" + "<Execution>\n" + " <TraderNo>6009</TraderNo>\n" + " <TraderName>RAY CHICOLI</TraderName>\n" + " <Symbol>IPIXQ</Symbol>\n" + " <Side>B</Side>\n" + " <Qty>10000</Qty>\n" + " <LvsQty>10000</LvsQty>\n" + " <Broker>UBSS</Broker>\n" + " <Price>0.011</Price>\n" + " <OrderRefno>3014000989</OrderRefno>\n" + "</Execution>\n" + "<Execution>\n" + " <TraderNo>8109</TraderNo>\n" + " <TraderName>FRANK FERRANTE</TraderName>\n" + " <Symbol>UOVEY</Symbol>\n" + " <Side>B</Side>\n" + " <Qty>500</Qty>\n" + " <LvsQty>0</LvsQty>\n" + " <Broker>BCAP</Broker>\n" + " <Price>28.6</Price>\n" + " <OrderRefno>3013001334</OrderRefno>\n" + "</Execution>\n" + "</Executions>";

    public void testPlay() throws Exception {
    }
}
