package com.w20e.socrates.process;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.w20e.socrates.data.Node;
import com.w20e.socrates.data.XSBoolean;
import com.w20e.socrates.data.XSInteger;
import com.w20e.socrates.data.XSString;
import com.w20e.socrates.expression.XBoolean;
import com.w20e.socrates.model.InstanceImpl;
import com.w20e.socrates.model.ItemProperties;
import com.w20e.socrates.model.ItemPropertiesImpl;
import com.w20e.socrates.model.ModelImpl;
import com.w20e.socrates.model.NodeImpl;
import com.w20e.socrates.rendering.ControlImpl;
import com.w20e.socrates.rendering.Input;
import com.w20e.socrates.rendering.RenderStateImpl;
import com.w20e.socrates.rendering.Renderable;
import com.w20e.socrates.rendering.Select;
import junit.framework.TestCase;

public class TestDataHandler extends TestCase {

    private InstanceImpl instance = new InstanceImpl();

    private ModelImpl model = new ModelImpl();

    private RenderStateImpl state;

    private HashMap<String, Object> data;

    private ItemProperties props;

    protected void setUp() throws Exception {
        super.setUp();
        Node node0 = new NodeImpl("/a");
        this.instance.addNode(node0);
        this.props = new ItemPropertiesImpl("/a");
        this.model.addItemProperties(this.props);
        ControlImpl r0 = new Input("r0");
        r0.setBind("/a");
        ControlImpl r1 = new Select("r1");
        r1.setBind("/a");
        List<Renderable> controls = new ArrayList<Renderable>();
        controls.add(r0);
        controls.add(r1);
        this.state = new RenderStateImpl(controls);
        this.data = new HashMap<String, Object>();
    }

    public void testXSInteger() {
        this.props.setType(XSInteger.class);
        this.data.clear();
        try {
            DataHandler.setData(this.data, this.model, this.instance, this.state);
        } catch (ValidationException e) {
            fail("Shouldn't have failed here");
        }
        this.data.put("r0", "YOO");
        try {
            DataHandler.setData(this.data, this.model, this.instance, this.state);
            fail("Should have failed on invalid type for XSInteger");
        } catch (ValidationException e) {
        }
        this.data.put("r0", "12");
        try {
            DataHandler.setData(this.data, this.model, this.instance, this.state);
        } catch (ValidationException e) {
            fail("Should not have failed on valid type for XSInteger");
        }
        this.data.put("r0", "");
        try {
            DataHandler.setData(this.data, this.model, this.instance, this.state);
        } catch (ValidationException e) {
            fail("Should not have failed on valid type for XSInteger");
        }
        this.props.setRequired(new XBoolean(true));
        try {
            DataHandler.setData(this.data, this.model, this.instance, this.state);
            fail("Should have failed on empty value for XSInteger");
        } catch (ValidationException e) {
        }
    }

    public void testXSBoolean() {
        this.props.setType(XSBoolean.class);
        this.data.clear();
        try {
            DataHandler.setData(this.data, this.model, this.instance, this.state);
        } catch (ValidationException e) {
            fail("Shouldn't have failed here");
        }
        this.data.put("r0", "12");
        try {
            DataHandler.setData(this.data, this.model, this.instance, this.state);
        } catch (ValidationException e) {
            fail("Should not have failed on valid type for XSBoolean");
        }
        this.data.put("r0", "");
        try {
            DataHandler.setData(this.data, this.model, this.instance, this.state);
        } catch (ValidationException e) {
            fail("Shouldn't have failed here");
        }
        this.props.setRequired(new XBoolean(true));
        try {
            DataHandler.setData(this.data, this.model, this.instance, this.state);
        } catch (ValidationException e) {
            fail("Shouldn't have failed on empty value for XSBoolean");
        }
    }

    public void testXSString() {
        this.props.setType(XSString.class);
        this.data.clear();
        try {
            DataHandler.setData(this.data, this.model, this.instance, this.state);
        } catch (ValidationException e) {
            fail("Shouldn't have failed here");
        }
        this.data.put("r0", "12");
        try {
            DataHandler.setData(this.data, this.model, this.instance, this.state);
        } catch (ValidationException e) {
            fail("Should not have failed on valid type for XSString");
        }
        this.data.put("r0", "");
        try {
            DataHandler.setData(this.data, this.model, this.instance, this.state);
        } catch (ValidationException e) {
            fail("Shouldn't have failed here");
        }
        this.props.setRequired(new XBoolean(true));
        try {
            DataHandler.setData(this.data, this.model, this.instance, this.state);
            fail("Should have failed on empty value for XSString");
        } catch (ValidationException e) {
        }
        this.data.clear();
        try {
            DataHandler.setData(this.data, this.model, this.instance, this.state);
            fail("Should have failed on undefined value for XSString");
        } catch (ValidationException e) {
        }
    }
}
