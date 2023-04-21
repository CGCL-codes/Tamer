package net.sf.wwusmart.algorithms;

import net.sf.wwusmart.algorithms.framework.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author thilo
 */
public class AlgorithmExample01 implements JavaAlgorithmImplementation {

    private Parameter<TripleImageIcon> icon3 = new Parameter<TripleImageIcon>(TripleImageIcon.class, "3 sketches", "for testing of the sketch parameter interface");

    public void initialize() {
    }

    public String getName() {
        return "Vertex Count Algorithm";
    }

    public String getDescription() {
        return "A dummy Algorithm determining the similarity of two shapes by " + "comparing the numbers of vertices used to build the shapes. " + "If two shapes have the same number of vertices, they are considered " + "completely equal (match result 1.0), if a shape has e.g. twice as " + "much vertices as the other one, match result will be 1/2 == 0.5, if it has " + "3 times as much vertices as the other one, match result will be 1/3 == 0.333... ." + "Algorithms descriptors include apart from the number of vertices a pixmap of a part of the smart logo, " + "the off-data of the shape and sample text, hex and CSV data to demonstrate " + "descriptor visualisation capabilities of the smart shape inspection panel.";
    }

    public String getVersion() {
        return "1.2";
    }

    public String getAuthors() {
        return "SMART Team";
    }

    public List<Parameter> getParameters() {
        List<Parameter> l = new LinkedList<Parameter>();
        l.add(icon3);
        return l;
    }

    public byte[] computeDescriptor(byte[] shape) {
        String s = new String(shape, 0, Math.min(shape.length, 32));
        if (!s.startsWith("OFF")) {
            System.err.println("Shape is no valid off file.");
            return null;
        }
        AlgorithmExample01Descriptor d = new AlgorithmExample01Descriptor();
        d.data = Integer.parseInt(s.split("\\s")[1]);
        try {
            return getBytes(d);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public double match(byte[] d1, byte[] d2) {
        Integer i1;
        Integer i2;
        try {
            i1 = ((AlgorithmExample01Descriptor) getObject(d1)).data;
            i2 = ((AlgorithmExample01Descriptor) getObject(d2)).data;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        Float f1 = i1.floatValue();
        Float f2 = i2.floatValue();
        return Math.min(f1 / f2, f2 / f1);
    }

    public Set<ShapeType> getApplicableTypes() {
        return EnumSet.of(ShapeType.OFF_3D);
    }

    public boolean isQueryShapeMandatory() {
        return false;
    }

    public void processNewParameters() throws InvalidParameterValuesException {
        return;
    }

    public void renderDescriptor(byte[] shapeData, byte[] descData, DescriptorRenderer renderer) {
        try {
            AlgorithmExample01Descriptor d = (AlgorithmExample01Descriptor) getObject(descData);
            renderer.render("Number of vertices", Integer.toString(d.data), DescriptorRenderer.Mode.TEXT_STRING);
            renderer.render("Shape", shapeData, DescriptorRenderer.Mode.OFF_FILE);
            renderer.render("Some text", d.text, DescriptorRenderer.Mode.TEXT_STRING);
            renderer.render("a hex number", d.hex, DescriptorRenderer.Mode.HEX);
            renderer.render("and a table", d.csv, DescriptorRenderer.Mode.CSV_STRING);
        } catch (IOException ex) {
            Logger.getLogger(AlgorithmExample01.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AlgorithmExample01.class.getName()).log(Level.SEVERE, null, ex);
        }
        return;
    }

    public static byte[] getBytes(Object obj) throws java.io.IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(obj);
        oos.flush();
        oos.close();
        bos.close();
        byte[] data = bos.toByteArray();
        return data;
    }

    public static Object getObject(byte[] data) throws java.io.IOException, ClassNotFoundException {
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        ObjectInputStream ois = new ObjectInputStream(bis);
        Object result = ois.readObject();
        ois.close();
        bis.close();
        return result;
    }
}
