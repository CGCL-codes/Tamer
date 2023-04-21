package net.juniorbl.jtoyracing.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import com.jme.bounding.BoundingBox;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.TriMesh;
import com.jme.util.export.binary.BinaryImporter;
import com.jmex.model.XMLparser.Converters.ObjToJme;

/**
 * Utility class for models. Part of this code came from JMonkeyEngine documentation.
 *
 * @version 1.0 Sep 9, 2007
 * @author Fco. Carlos L. Barros Junior
 */
public final class ModelUtil {

    /**
	 * Prevents this class from being instantiated.
	 */
    private ModelUtil() {
    }

    /**
	 * Converts a OBJ model into JME model according to a given path.
	 * 
	 * @param path the path.
	 * @return the JME model converted.
	 */
    private static ByteArrayOutputStream convertModelObjToJME(String path) {
        ObjToJme converter = new ObjToJme();
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        try {
            URL objFile = ModelUtil.class.getClassLoader().getResource(path);
            converter.setProperty("mtllib", objFile);
            converter.setProperty("texdir", objFile);
            converter.convert(objFile.openStream(), byteArrayOS);
        } catch (IOException e) {
            throw new Error(e);
        }
        return byteArrayOS;
    }

    /**
	 * Converts a simple OBJ model into JME model according to a given path.
	 * 
	 * @param path the path.
	 * @return the JME model converted.
	 */
    public static TriMesh convertModelSimpleObjToJME(String path) {
        TriMesh model = null;
        try {
            ByteArrayOutputStream byteArrayOS = convertModelObjToJME(path);
            model = (TriMesh) BinaryImporter.getInstance().load(new ByteArrayInputStream(byteArrayOS.toByteArray()));
            model.setModelBound(new BoundingBox());
            model.updateModelBound();
            model.setDefaultColor(ColorRGBA.black);
        } catch (IOException e) {
            throw new Error(e);
        }
        return model;
    }

    /**
	 * Converts a multiple OBJ model into JME model according to a given path.
	 * 
	 * @param path the path.
	 * @return the JME model converted.
	 */
    public static Node convertMultipleModelToJME(String path) {
        Node modelo = null;
        try {
            ByteArrayOutputStream byteArrayOS = convertModelObjToJME(path);
            modelo = (Node) BinaryImporter.getInstance().load(new ByteArrayInputStream(byteArrayOS.toByteArray()));
            modelo.setModelBound(new BoundingBox());
            modelo.updateModelBound();
        } catch (IOException e) {
            throw new Error(e);
        }
        return modelo;
    }
}
