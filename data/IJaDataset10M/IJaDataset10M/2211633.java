package edu.uncw.grid.gui.lib;

import ptolemy.data.JxplToken;
import ptolemy.data.Token;
import ptolemy.data.StringToken;
import ptolemy.data.type.BaseType;
import ptolemy.data.expr.Parameter;
import ptolemy.kernel.CompositeEntity;
import ptolemy.kernel.util.*;
import ptolemy.actor.lib.Transformer;
import ptolemy.actor.TypedIOPort;
import ptolemy.data.BooleanToken;
import org.jxpl.bindings.*;
import java.util.Vector;

public class GDSClient extends Transformer {

    TypedIOPort serviceHandle, arguments;

    Parameter secure, destroy;

    /**
   * Creates a new instance of the GDSClient, sets the input port
   * @param container CompositeEntity
   * @param name String
   * @throws IllegalActionException
   * @throws NameDuplicationException
   */
    public GDSClient(CompositeEntity container, String name) throws IllegalActionException, NameDuplicationException {
        super(container, name);
        input.setTypeEquals(BaseType.JXPL);
        input.setMultiport(false);
        input.setName("factoryUrl");
        Attribute inputShow = new Attribute(input, "_showName");
        serviceHandle = new TypedIOPort(this, "serviceHandle", true, false);
        serviceHandle.setTypeEquals(BaseType.JXPL);
        serviceHandle.setMultiport(false);
        Attribute serviceHandleShow = new Attribute(serviceHandle, "_showName");
        arguments = new TypedIOPort(this, "input", true, false);
        arguments.setTypeEquals(BaseType.JXPL);
        arguments.setMultiport(true);
        Attribute argumentsShow = new Attribute(arguments, "_showName");
        output.setTypeEquals(BaseType.JXPL);
        secure = new Parameter(this, "Secure", new BooleanToken(true));
        secure.setTypeEquals(BaseType.BOOLEAN);
        destroy = new Parameter(this, "Destroy", new BooleanToken(true));
        destroy.setTypeEquals(BaseType.BOOLEAN);
        _attachText("_iconDescription", "<svg>\n" + "<rect x=\"0\" y=\"0\" " + "width=\"60\" height=\"40\" " + "style=\"fill:white\"/>\n" + "<image x=\"0\" y=\"0\" " + "width=\"60\" height=\"40\" " + "xlink:href=\"file:configs/images/grillsmall.jpg\"/>" + "</svg>\n");
    }

    /**
   * Fires the GDSClient.
   * @throws IllegalActionException
   */
    public void fire() throws IllegalActionException {
        int width;
        JxplToken token;
        JxplPrimitive primitive;
        if (arguments.getWidth() > 0) {
            JxplList list = new JxplList();
            Vector listElements = list.getElements();
            primitive = new JxplPrimitive();
            primitive.setName(".ogsadai.GDSClient");
            width = input.getWidth();
            for (int i = 0; i < width; i++) {
                token = (JxplToken) input.get(i);
                JxplElement El = (JxplElement) token.getValue();
                if (El != null) {
                    JxplList factList = new JxplList();
                    Vector factListElements = factList.getElements();
                    JxplPrimitive factPrimitive = new JxplPrimitive();
                    factPrimitive.setName("Property");
                    factListElements.addElement(factPrimitive);
                    factListElements.addElement(new JxplString("factoryUrl"));
                    factListElements.addElement(El);
                    primitive.getProperties().addElement(factList);
                }
            }
            width = serviceHandle.getWidth();
            for (int i = 0; i < width; i++) {
                token = (JxplToken) serviceHandle.get(i);
                JxplElement El = (JxplElement) token.getValue();
                if (El != null) {
                    JxplList servList = new JxplList();
                    Vector servListElements = servList.getElements();
                    JxplPrimitive servPrimitive = new JxplPrimitive();
                    servPrimitive.setName("Property");
                    servListElements.addElement(servPrimitive);
                    servListElements.addElement(new JxplString("serviceHandle"));
                    servListElements.addElement(El);
                    primitive.getProperties().addElement(servList);
                }
            }
            if (!((BooleanToken) secure.getToken()).booleanValue()) {
                JxplProperty prop = new JxplProperty("secure", "false");
                primitive.getProperties().addElement(prop);
            }
            if (!((BooleanToken) destroy.getToken()).booleanValue()) {
                JxplProperty prop = new JxplProperty("destroy", "false");
                primitive.getProperties().addElement(prop);
            }
            listElements.addElement(primitive);
            width = arguments.getWidth();
            for (int i = 0; i < width; i++) {
                token = (JxplToken) arguments.get(i);
                listElements.addElement(token.getValue());
            }
            output.send(0, new JxplToken(list));
        }
    }
}
