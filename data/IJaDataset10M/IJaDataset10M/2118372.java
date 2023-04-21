package org.openremote.controller.protocol.lutron;

import org.jdom.Element;
import org.junit.Assert;
import org.junit.Test;
import org.openremote.controller.command.CommandBuilder;
import org.openremote.controller.exception.NoSuchCommandException;

/**
 * Basic unit tests for parsing XML elements in
 * {@link org.openremote.controller.protocol.lutron.LutronHomeWorksCommandBuilder} and building commands.
 * 
 * @author <a href="mailto:eric@openremote.org">Eric Bariaux</a>
 */
public class LutronHomeWorksCommandBuilderTest {

    @Test
    public void testDimmerCommands() {
        CommandBuilder cb = new LutronHomeWorksCommandBuilder();
        Assert.assertTrue(cb.build(getCommandElement("RAISE", "1:4:1:1:1", null, null, null)) instanceof DimmerCommand);
        Assert.assertTrue(cb.build(getCommandElement("LOWER", "1:4:1:1:1", null, null, null)) instanceof DimmerCommand);
        Assert.assertTrue(cb.build(getCommandElement("STOP", "1:4:1:1:1", null, null, null)) instanceof DimmerCommand);
        Assert.assertTrue(cb.build(getCommandElement("FADE", "1:4:1:1:1", null, null, "10")) instanceof DimmerCommand);
    }

    @Test(expected = NoSuchCommandException.class)
    public void testDimmerCommandInvalidAddress() {
        new LutronHomeWorksCommandBuilder().build(getCommandElement("RAISE", "1:8:1", null, null, null));
    }

    @Test(expected = NoSuchCommandException.class)
    public void testDimmerCommandMissingAddress() {
        new LutronHomeWorksCommandBuilder().build(getCommandElement("RAISE", null, null, null, null));
    }

    @Test(expected = NoSuchCommandException.class)
    public void testDimmerFadeCommandMissingLevel() {
        new LutronHomeWorksCommandBuilder().build(getCommandElement("FADE", "1:4:1:1:1", null, null, null));
    }

    @Test
    public void testKeypadCommands() {
        CommandBuilder cb = new LutronHomeWorksCommandBuilder();
        Assert.assertTrue(cb.build(getCommandElement("PRESS", "1:4:1", null, "2", null)) instanceof KeypadCommand);
        Assert.assertTrue(cb.build(getCommandElement("RELEASE", "1:4:1", null, "2", null)) instanceof KeypadCommand);
        Assert.assertTrue(cb.build(getCommandElement("HOLD", "1:4:1", null, "2", null)) instanceof KeypadCommand);
        Assert.assertTrue(cb.build(getCommandElement("DOUBLE_TAP", "1:4:1", null, "2", null)) instanceof KeypadCommand);
        Assert.assertTrue(cb.build(getCommandElement("STATUS_KEYPADLED", "1:4:1", null, "2", null)) instanceof KeypadCommand);
    }

    @Test(expected = NoSuchCommandException.class)
    public void testKeypadCommandMissingAddress() {
        new LutronHomeWorksCommandBuilder().build(getCommandElement("PRESS", null, null, "2", null));
    }

    @Test(expected = NoSuchCommandException.class)
    public void testKeypadCommandInvalidAddress() {
        new LutronHomeWorksCommandBuilder().build(getCommandElement("PRESS", "1:4:1:1", null, "2", null));
    }

    @Test(expected = NoSuchCommandException.class)
    public void testKeypadCommandMissingKey() {
        new LutronHomeWorksCommandBuilder().build(getCommandElement("PRESS", "1:4:1:1:1", null, null, null));
    }

    @Test(expected = NoSuchCommandException.class)
    public void testKeypadCommandInvalidKey() {
        new LutronHomeWorksCommandBuilder().build(getCommandElement("PRESS", "1:4:1:1:1", null, "hhj", null));
    }

    @Test
    public void testSceneCommands() {
        CommandBuilder cb = new LutronHomeWorksCommandBuilder();
        Assert.assertTrue(cb.build(getCommandElement("SCENE", "1:4:1", "1", null, null)) instanceof GrafikEyeCommand);
        Assert.assertTrue(cb.build(getCommandElement("STATUS_SCENE", "1:4:1", "1", null, null)) instanceof GrafikEyeCommand);
    }

    @Test(expected = NoSuchCommandException.class)
    public void testSceneCommandMissingAddress() {
        new LutronHomeWorksCommandBuilder().build(getCommandElement("SCENE", null, "3", null, null));
    }

    @Test(expected = NoSuchCommandException.class)
    public void testSceneCommandInvalidAddress() {
        new LutronHomeWorksCommandBuilder().build(getCommandElement("SCENE", "1:4:1:1", "3", null, null));
    }

    @Test(expected = NoSuchCommandException.class)
    public void testSceneCommandMissingScene() {
        new LutronHomeWorksCommandBuilder().build(getCommandElement("SCENE", "1:4:1", null, null, null));
    }

    @Test(expected = NoSuchCommandException.class)
    public void testSceneCommandInvalidScene() {
        new LutronHomeWorksCommandBuilder().build(getCommandElement("SCENE", "1:4:1", "hjhjj", null, null));
    }

    @Test(expected = NoSuchCommandException.class)
    public void testUnknwonCommand() {
        new LutronHomeWorksCommandBuilder().build(getCommandElement("DIMMER", null, null, null, null));
    }

    private Element getCommandElement(String cmd, String address, String scene, String key, String level) {
        Element ele = new Element("command");
        ele.setAttribute("id", "test");
        ele.setAttribute(CommandBuilder.PROTOCOL_ATTRIBUTE_NAME, "lutron_homeworks");
        if (cmd != null) {
            Element propAddr = new Element(CommandBuilder.XML_ELEMENT_PROPERTY);
            propAddr.setAttribute(CommandBuilder.XML_ATTRIBUTENAME_NAME, LutronHomeWorksCommandBuilder.LUTRON_XMLPROPERTY_COMMAND);
            propAddr.setAttribute(CommandBuilder.XML_ATTRIBUTENAME_VALUE, cmd);
            ele.addContent(propAddr);
        }
        if (address != null) {
            Element propAddr = new Element(CommandBuilder.XML_ELEMENT_PROPERTY);
            propAddr.setAttribute(CommandBuilder.XML_ATTRIBUTENAME_NAME, LutronHomeWorksCommandBuilder.LUTRON_XMLPROPERTY_ADDRESS);
            propAddr.setAttribute(CommandBuilder.XML_ATTRIBUTENAME_VALUE, address);
            ele.addContent(propAddr);
        }
        if (scene != null) {
            Element propAddr = new Element(CommandBuilder.XML_ELEMENT_PROPERTY);
            propAddr.setAttribute(CommandBuilder.XML_ATTRIBUTENAME_NAME, LutronHomeWorksCommandBuilder.LUTRON_XMLPROPERTY_SCENE);
            propAddr.setAttribute(CommandBuilder.XML_ATTRIBUTENAME_VALUE, scene);
            ele.addContent(propAddr);
        }
        if (key != null) {
            Element propAddr = new Element(CommandBuilder.XML_ELEMENT_PROPERTY);
            propAddr.setAttribute(CommandBuilder.XML_ATTRIBUTENAME_NAME, LutronHomeWorksCommandBuilder.LUTRON_XMLPROPERTY_KEY);
            propAddr.setAttribute(CommandBuilder.XML_ATTRIBUTENAME_VALUE, key);
            ele.addContent(propAddr);
        }
        if (level != null) {
            Element propAddr = new Element(CommandBuilder.XML_ELEMENT_PROPERTY);
            propAddr.setAttribute(CommandBuilder.XML_ATTRIBUTENAME_NAME, LutronHomeWorksCommandBuilder.LUTRON_XMLPROPERTY_LEVEL);
            propAddr.setAttribute(CommandBuilder.XML_ATTRIBUTENAME_VALUE, level);
            ele.addContent(propAddr);
        }
        return ele;
    }
}
