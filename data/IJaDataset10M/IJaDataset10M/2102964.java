package org.frameworkset.spi.remote.jms;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import org.frameworkset.mq.JMSConnectionFactory;
import org.frameworkset.mq.JMSReceiveTemplate;
import org.frameworkset.mq.JMSTemplate;
import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.DefaultApplicationContext;
import org.junit.Before;
import org.junit.Test;

/**
 * <p>Title: JMSTemplateTest.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2009-11-24 ����02:57:10
 * @author biaoping.yin
 * @version 1.0
 */
public class JMSTemplateTest {

    private BaseApplicationContext context;

    @Before
    public void init() {
        context = DefaultApplicationContext.getApplicationContext("org/frameworkset/spi/remote/jms/manager-jmstemplate-test.xml");
    }

    @Test
    public void test() {
        JMSTemplate template = context.getTBeanObject("test.jmstemplate", JMSTemplate.class);
        try {
            template.send("atest", "ahello");
        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
            template.stop();
        }
    }

    @Test
    public void testgwcs() {
        JMSTemplate template = context.getTBeanObject("test.jmstemplate", JMSTemplate.class);
        try {
            template.send("atest", "ahello");
        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
            template.stop();
        }
    }

    @Test
    public void testPooledFactoryConnection() {
        JMSConnectionFactory factory = context.getTBeanObject("test.amq.PooledConnectionFactory", JMSConnectionFactory.class);
        try {
            Connection connection = factory.getConnection();
            connection.start();
            connection.close();
            connection = factory.getConnection();
            connection.start();
            connection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFactoryConnection() {
        JMSConnectionFactory factory = context.getTBeanObject("test.amq.ConnectionFactory", JMSConnectionFactory.class);
        try {
            Connection connection = factory.getConnection();
            connection.start();
            connection.close();
            connection = factory.getConnection();
            connection.start();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPersistentMessage() {
        JMSTemplate template = context.getTBeanObject("test.jmstemplate", JMSTemplate.class);
        try {
            template.send("atest", "phello", true);
        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
            template.stop();
        }
    }

    @Test
    public void testAllparamsMessage() {
        JMSTemplate template = context.getTBeanObject("test.jmstemplate", JMSTemplate.class);
        try {
            template.send("atest", "allhello", true, 4, 10000);
        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
            template.stop();
        }
    }

    @Test
    public void testReceiveMessage() {
        JMSTemplate template = context.getTBeanObject("test.jmstemplate", JMSTemplate.class);
        try {
            Message msg = template.receive("atest");
            System.out.println("testReceiveMessage:" + msg);
        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
            template.stop();
        }
    }

    @Test
    public void testReceiveGWCSMessage() {
        JMSTemplate template = context.getTBeanObject("test.jmstemplate", JMSTemplate.class);
        try {
            for (int i = 0; i < 10; i++) {
                Message msg = template.receive("GWCS.Receive.Cache");
                System.out.println("testReceiveMessage:" + msg);
            }
        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
            template.stop();
        }
    }

    @Test
    public void testMessageListener() {
        JMSReceiveTemplate template = context.getTBeanObject("test.jms.receive.template", JMSReceiveTemplate.class);
        try {
            template.setMessageListener("atest", new MessageListener() {

                public void onMessage(Message arg0) {
                    System.out.println("msg comming:" + arg0);
                }
            });
        } catch (Exception e) {
            template.stop();
        } finally {
        }
    }

    @Test
    public void testTopicSend() {
        JMSTemplate template = context.getTBeanObject("test.jmstemplate", JMSTemplate.class);
        try {
            template.send("topic://getsubtest", "getsubtest");
        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
            template.stop();
        }
    }

    @Test
    public void testGetSubscriber() {
        JMSReceiveTemplate template = context.getTBeanObject("test.topic.receive.jmstemplate", JMSReceiveTemplate.class);
        try {
            template.getTopicSubscriber("getsubtest", "subscribename").setMessageListener(new MessageListener() {

                public void onMessage(Message arg0) {
                    System.out.println("topic msg comming:" + arg0);
                }
            });
        } catch (Exception e) {
            template.stop();
        } finally {
        }
        System.out.println();
    }

    @Test
    public void testSubscriber() {
        JMSReceiveTemplate template = context.getTBeanObject("test.topic1.receive.jmstemplate", JMSReceiveTemplate.class);
        try {
            template.subscribeTopic("subtest", "subscribename", new MessageListener() {

                public void onMessage(Message arg0) {
                    System.out.println("topic msg comming:" + arg0);
                }
            });
        } catch (Exception e) {
            template.stop();
        } finally {
        }
    }

    @Test
    public void testunSubscriber() {
        JMSReceiveTemplate template = context.getTBeanObject("test.topic1.receive.jmstemplate", JMSReceiveTemplate.class);
        try {
            template.unsubscribe("subscribename");
        } catch (Exception e) {
            template.stop();
        } finally {
        }
    }

    public static void main(String[] args) {
    }
}
