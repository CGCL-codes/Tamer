package org.openbandy.example.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import org.openbandy.example.pingpong.PingPongProtocol;
import org.openbandy.io.Message;
import org.openbandy.io.stream.StreamSerializer;
import org.openbandy.service.LogService;
import org.openbandy.service.SerializerService;
import org.openbandy.test.Test;

/**
 * TODO describe purpose and usage
 *
 * <br><br>
 * (c) Copyright Philipp Bolliger 2007, ALL RIGHTS RESERVED.
 *
 * @author Philipp Bolliger (philipp@bolliger.name)
 * @version 0.1
 * 
 */
public class MessageSerializationTest extends Test {

    public MessageSerializationTest() {
        super("Message Serialization");
    }

    public void run() {
        testStarted();
        try {
            Message message = new Message();
            message.setSender("TestSender");
            message.setType(0);
            ByteArrayOutputStream baos = SerializerService.getXmlStream(message, StreamSerializer.COMPRESSION_NONE);
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            Message clonedMessage = (Message) SerializerService.getObjectFromXmlStream(bais);
            if (!clonedMessage.isEqual(message)) {
                LogService.warn(this, "Message serialization test failed (content==null, COMPRESSION_NONE)");
                testsScreen.setTestFailed(this);
                return;
            } else {
                testsScreen.setTestPassed(this);
            }
            baos = SerializerService.getXmlStream(message, StreamSerializer.COMPRESSED_TOKEN);
            bais = new ByteArrayInputStream(baos.toByteArray());
            clonedMessage = (Message) SerializerService.getObjectFromXmlStream(bais);
            if (!clonedMessage.isEqual(message)) {
                LogService.warn(this, "Message serialization test failed (content==null, COMPRESSED_TOKEN)");
                testsScreen.setTestFailed(this);
                return;
            } else {
                testsScreen.setTestPassed(this);
            }
            baos = SerializerService.getXmlStream(message, StreamSerializer.COMPRESSED_ZIP);
            bais = new ByteArrayInputStream(baos.toByteArray());
            clonedMessage = (Message) SerializerService.getObjectFromXmlStream(bais);
            if (!clonedMessage.isEqual(message)) {
                LogService.warn(this, "Message serialization test failed (content==null, COMPRESSED_ZIP)");
                testsScreen.setTestFailed(this);
                return;
            } else {
                testsScreen.setTestPassed(this);
            }
            baos = SerializerService.getXmlStream(message, StreamSerializer.COMPRESSED_BEST);
            bais = new ByteArrayInputStream(baos.toByteArray());
            clonedMessage = (Message) SerializerService.getObjectFromXmlStream(bais);
            if (!clonedMessage.isEqual(message)) {
                LogService.warn(this, "Message serialization test failed (content==null, COMPRESSED_BEST)");
                testsScreen.setTestFailed(this);
                return;
            } else {
                testsScreen.setTestPassed(this);
            }
            PingPongProtocol protocol = new PingPongProtocol();
            message = protocol.createMessage("TestSender", "Hello World", 0);
            baos = SerializerService.getXmlStream(message, StreamSerializer.COMPRESSION_NONE);
            bais = new ByteArrayInputStream(baos.toByteArray());
            clonedMessage = (Message) SerializerService.getObjectFromXmlStream(bais);
            if (!clonedMessage.isEqual(message)) {
                LogService.warn(this, "Message serialization test failed (content!=null, COMPRESSION_NONE)");
                testsScreen.setTestFailed(this);
                return;
            } else {
                testsScreen.setTestPassed(this);
            }
            baos = SerializerService.getXmlStream(message, StreamSerializer.COMPRESSED_TOKEN);
            bais = new ByteArrayInputStream(baos.toByteArray());
            clonedMessage = (Message) SerializerService.getObjectFromXmlStream(bais);
            if (!clonedMessage.isEqual(message)) {
                LogService.warn(this, "Message serialization test failed(content!=null, COMPRESSED_TOKEN)");
                testsScreen.setTestFailed(this);
                return;
            } else {
                testsScreen.setTestPassed(this);
            }
            baos = SerializerService.getXmlStream(message, StreamSerializer.COMPRESSED_ZIP);
            bais = new ByteArrayInputStream(baos.toByteArray());
            clonedMessage = (Message) SerializerService.getObjectFromXmlStream(bais);
            if (!clonedMessage.isEqual(message)) {
                LogService.warn(this, "Message serialization test failed(content!=null, COMPRESSED_ZIP)");
                testsScreen.setTestFailed(this);
                return;
            } else {
                testsScreen.setTestPassed(this);
            }
            baos = SerializerService.getXmlStream(message, StreamSerializer.COMPRESSED_BEST);
            bais = new ByteArrayInputStream(baos.toByteArray());
            clonedMessage = (Message) SerializerService.getObjectFromXmlStream(bais);
            if (!clonedMessage.isEqual(message)) {
                LogService.warn(this, "Message serialization test failed(content!=null, COMPRESSED_BEST)");
                testsScreen.setTestFailed(this);
                return;
            } else {
                testsScreen.setTestPassed(this);
            }
        } catch (Exception e) {
            LogService.error(this, e.getMessage(), e);
            testsScreen.setTestFailed(this);
        }
        testFinished();
    }
}
