package org.npsnet.v.views.opengl;

import gl4java.*;
import gl4java.utils.glut.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.*;

/**
 * Represents an X3D scene.
 *
 * @author Andrzej Kapolka
 */
public class X3DScene {

    /**
     * The X3D document element.
     */
    private static final String X3D = "X3D";

    /**
     * The X3D scene element.
     */
    private static final String SCENE = "Scene";

    /**
     * The group element.
     */
    private static final String GROUP = "Group";

    /**
     * The transform element.
     */
    private static final String TRANSFORM = "Transform";

    /**
     * The shape element.
     */
    private static final String SHAPE = "Shape";

    /**
     * The appearance element.
     */
    private static final String APPEARANCE = "Appearance";

    /**
     * The material element.
     */
    private static final String MATERIAL = "Material";

    /**
     * The box element.
     */
    private static final String BOX = "Box";

    /**
     * The cylinder element.
     */
    private static final String CYLINDER = "Cylinder";

    /**
     * The sphere element.
     */
    private static final String SPHERE = "Sphere";

    /**
     * The cone element.
     */
    private static final String CONE = "Cone";

    /**
     * The indexed face set element.
     */
    private static final String INDEXED_FACE_SET = "IndexedFaceSet";

    /**
     * The coordinate element.
     */
    private static final String COORDINATE = "Coordinate";

    /**
     * The normal element.
     */
    private static final String NORMAL = "Normal";

    /**
     * The DEF attribute.
     */
    private static final String DEF = "DEF";

    /**
     * The USE attribute.
     */
    private static final String USE = "USE";

    /**
     * The center attribute.
     */
    private static final String CENTER = "center";

    /**
     * The rotation attribute.
     */
    private static final String ROTATION = "rotation";

    /**
     * The scale attribute.
     */
    private static final String SCALE = "scale";

    /**
     * The scale orientation attribute.
     */
    private static final String SCALE_ORIENTATION = "scaleOrientation";

    /**
     * The translation attribute.
     */
    private static final String TRANSLATION = "translation";

    /**
     * The ambient intensity attribute.
     */
    private static final String AMBIENT_INTENSITY = "ambientIntensity";

    /**
     * The diffuse color attribute.
     */
    private static final String DIFFUSE_COLOR = "diffuseColor";

    /**
     * The emissive color attribute.
     */
    private static final String EMISSIVE_COLOR = "emissiveColor";

    /**
     * The shininess attribute.
     */
    private static final String SHININESS = "shininess";

    /**
     * The specular color attribute.
     */
    private static final String SPECULAR_COLOR = "specularColor";

    /**
     * The transparency attribute.
     */
    private static final String TRANSPARENCY = "transparency";

    /**
     * The size attribute.
     */
    private static final String SIZE = "size";

    /**
     * The height attribute.
     */
    private static final String HEIGHT = "height";

    /**
     * The radius attribute.
     */
    private static final String RADIUS = "radius";

    /**
     * The top attribute.
     */
    private static final String TOP = "top";

    /**
     * The bottom attribute.
     */
    private static final String BOTTOM = "bottom";

    /**
     * The side attribute.
     */
    private static final String SIDE = "side";

    /**
     * The bottom radius attribute.
     */
    private static final String BOTTOM_RADIUS = "bottomRadius";

    /**
     * The ccw attribute.
     */
    private static final String CCW = "ccw";

    /**
     * The color index attribute.
     */
    private static final String COLOR_INDEX = "colorIndex";

    /**
     * The color per vertex attribute.
     */
    private static final String COLOR_PER_VERTEX = "colorPerVertex";

    /**
     * The convex attribute.
     */
    private static final String CONVEX = "convex";

    /**
     * The coordinate index attribute.
     */
    private static final String COORD_INDEX = "coordIndex";

    /**
     * The crease angle attribute.
     */
    private static final String CREASE_ANGLE = "creaseAngle";

    /**
     * The normal index attribute.
     */
    private static final String NORMAL_INDEX = "normalIndex";

    /**
     * The normal per vertex attribute.
     */
    private static final String NORMAL_PER_VERTEX = "normalPerVertex";

    /**
     * The solid attribute.
     */
    private static final String SOLID = "solid";

    /**
     * The texture coordinate index attribute.
     */
    private static final String TEX_COORD_INDEX = "texCoordIndex";

    /**
     * The point attribute.
     */
    private static final String POINT = "point";

    /**
     * The vector attribute.
     */
    private static final String VECTOR = "vector";

    /**
     * The parsed scene element.
     */
    private Element sceneElement;

    /**
     * Maps DEF'd names to their corresponding elements.
     */
    private HashMap idElementMap;

    /**
     * The <code>GLFunc</code> reference.
     */
    private GLFunc gl;

    /**
     * The <code>GLUFunc</code> reference.
     */
    private GLUFunc glu;

    /**
     * The <code>GLUTFunc</code> reference.
     */
    private GLUTFunc glut;

    /**
     * The active vertex array.
     */
    private double[] vertexArray;

    /**
     * The active normal array.
     */
    private double[] normalArray;

    /**
     * Constructor.
     *
     * @param sceneUrl the location of the scene to load
     * @exception IOException if an error occurs in reading the scene document
     * @exception SAXException if an error occurs in parsing the scene document
     * @exception InvalidX3DException if the described scene is invalid
     */
    public X3DScene(URL sceneUrl) throws IOException, SAXException, InvalidX3DException {
        idElementMap = new HashMap();
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document d = db.parse(sceneUrl.toString());
            Element e = d.getDocumentElement();
            if (!e.getTagName().equals(X3D)) {
                throw new InvalidX3DException("Document is not an X3D scene");
            }
            NodeList nl = e.getChildNodes();
            for (int i = 0; i < nl.getLength() && sceneElement == null; i++) {
                if (nl.item(i) instanceof Element && nl.item(i).getNodeName().equals(SCENE)) {
                    sceneElement = (Element) nl.item(i);
                    return;
                }
            }
            throw new InvalidX3DException("Document does not contain a scene element");
        } catch (ParserConfigurationException pce) {
        }
    }

    /**
     * Draws this scene.
     *
     * @param pGL the <code>GLFunc</code> reference
     * @param pGLU the <code>GLUFunc</code> reference
     * @param pGLUT the <code>GLUTFunc</code> reference
     * @exception InvalidX3DException if the scene is invalid
     */
    public synchronized void draw(GLFunc pGL, GLUFunc pGLU, GLUTFunc pGLUT) throws InvalidX3DException {
        gl = pGL;
        glu = pGLU;
        glut = pGLUT;
        NodeList nl = sceneElement.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            if (nl.item(i) instanceof Element) {
                draw((Element) nl.item(i));
            }
        }
    }

    /**
     * Draws an element of this scene.
     *
     * @param element the element to draw
     * @exception InvalidX3DException if the element is invalid
     */
    private void draw(Element element) throws InvalidX3DException {
        if (element.hasAttribute(USE)) {
            Element referencedElement = (Element) idElementMap.get(element.getAttribute(USE));
            if (referencedElement == null) {
                throw new InvalidX3DException("Unknown identifier: " + element.getAttribute(USE));
            } else {
                draw(referencedElement);
            }
        } else {
            if (element.hasAttribute(DEF)) {
                idElementMap.put(element.getAttribute(DEF), element);
            }
            String name = element.getTagName();
            if (name.equals(GROUP)) {
                drawGroup(element);
            } else if (name.equals(TRANSFORM)) {
                drawTransform(element);
            } else if (name.equals(SHAPE)) {
                drawShape(element);
            } else if (name.equals(APPEARANCE)) {
                drawAppearance(element);
            } else if (name.equals(MATERIAL)) {
                drawMaterial(element);
            } else if (name.equals(BOX)) {
                drawBox(element);
            } else if (name.equals(CYLINDER)) {
                drawCylinder(element);
            } else if (name.equals(SPHERE)) {
                drawSphere(element);
            } else if (name.equals(CONE)) {
                drawCone(element);
            } else if (name.equals(INDEXED_FACE_SET)) {
                drawIndexedFaceSet(element);
            } else if (name.equals(COORDINATE)) {
                drawCoordinate(element);
            } else if (name.equals(NORMAL)) {
                drawNormal(element);
            }
        }
    }

    /**
     * Draws a group element.
     *
     * @param element the element to draw
     * @exception InvalidX3DException if the element is invalid
     */
    private void drawGroup(Element element) throws InvalidX3DException {
        NodeList nl = element.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            if (nl.item(i) instanceof Element) {
                draw((Element) nl.item(i));
            }
        }
    }

    /**
     * Draws a transform element.
     *
     * @param element the element to draw
     * @exception InvalidX3DException if the element is invalid
     */
    private void drawTransform(Element element) throws InvalidX3DException {
        double[] translation = new double[] { 0, 0, 0 }, center = new double[] { 0, 0, 0 }, rotation = new double[] { 0, 0, 1, 0 }, scaleOrientation = new double[] { 0, 0, 1, 0 }, scale = new double[] { 1, 1, 1 };
        if (element.hasAttribute(TRANSLATION)) {
            readInto(element.getAttribute(TRANSLATION), translation);
        }
        if (element.hasAttribute(CENTER)) {
            readInto(element.getAttribute(CENTER), center);
        }
        if (element.hasAttribute(ROTATION)) {
            readInto(element.getAttribute(ROTATION), rotation);
        }
        if (element.hasAttribute(SCALE_ORIENTATION)) {
            readInto(element.getAttribute(SCALE_ORIENTATION), scaleOrientation);
        }
        if (element.hasAttribute(SCALE)) {
            readInto(element.getAttribute(SCALE), scale);
        }
        gl.glPushMatrix();
        gl.glTranslated(translation[0], translation[1], translation[2]);
        gl.glTranslated(center[0], center[1], center[2]);
        gl.glRotated(Math.toDegrees(rotation[3]), rotation[0], rotation[1], rotation[2]);
        gl.glRotated(Math.toDegrees(scaleOrientation[3]), scaleOrientation[0], scaleOrientation[1], scaleOrientation[2]);
        gl.glScaled(scale[0], scale[1], scale[2]);
        gl.glRotated(-Math.toDegrees(scaleOrientation[3]), scaleOrientation[0], scaleOrientation[1], scaleOrientation[2]);
        gl.glTranslated(-center[0], -center[1], -center[2]);
        NodeList nl = element.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            if (nl.item(i) instanceof Element) {
                draw((Element) nl.item(i));
            }
        }
        gl.glPopMatrix();
    }

    /**
     * Reads a number of doubles from the specified string into the specified
     * array.
     *
     * @param string the string from which to read the doubles
     * @param values the array into which to write the doubles
     * @exception InvalidX3DException if one of the values was invalid
     */
    private void readInto(String string, double[] values) throws InvalidX3DException {
        StringTokenizer st = new StringTokenizer(string);
        int i;
        for (i = 0; i < values.length && st.hasMoreTokens(); i++) {
            try {
                values[i] = Double.parseDouble(st.nextToken());
            } catch (NumberFormatException nfe) {
                throw new InvalidX3DException(nfe.toString());
            }
        }
        if (st.hasMoreTokens() || i != values.length) {
            throw new InvalidX3DException("Wrong number of values");
        }
    }

    /**
     * Draws a shape element.
     *
     * @param element the element to draw
     * @exception InvalidX3DException if the element is invalid
     */
    private void drawShape(Element element) throws InvalidX3DException {
        NodeList nl = element.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            if (nl.item(i) instanceof Element && nl.item(i).getNodeName().equals(APPEARANCE)) {
                draw((Element) nl.item(i));
            }
        }
        for (int i = 0; i < nl.getLength(); i++) {
            if (nl.item(i) instanceof Element && !nl.item(i).getNodeName().equals(APPEARANCE)) {
                draw((Element) nl.item(i));
            }
        }
    }

    /**
     * Draws an appearance element.
     *
     * @param element the element to draw
     * @exception InvalidX3DException if the element is invalid
     */
    private void drawAppearance(Element element) throws InvalidX3DException {
        NodeList nl = element.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            if (nl.item(i) instanceof Element) {
                draw((Element) nl.item(i));
            }
        }
    }

    /**
     * Draws a material element.
     *
     * @param element the element to draw
     * @exception InvalidX3DException if the element is invalid
     */
    private void drawMaterial(Element element) throws InvalidX3DException {
        float ambientIntensity = 0.2f, shininess = 0.2f, transparency = 0.0f;
        float[] diffuseColor = new float[] { 0.8f, 0.8f, 0.8f }, emissiveColor = new float[] { 0.0f, 0.0f, 0.0f }, specularColor = new float[] { 0.0f, 0.0f, 0.0f };
        try {
            if (element.hasAttribute(AMBIENT_INTENSITY)) {
                ambientIntensity = Float.parseFloat(element.getAttribute(AMBIENT_INTENSITY));
            }
            if (element.hasAttribute(SHININESS)) {
                shininess = Float.parseFloat(element.getAttribute(SHININESS));
            }
            if (element.hasAttribute(TRANSPARENCY)) {
                transparency = Float.parseFloat(element.getAttribute(TRANSPARENCY));
            }
        } catch (NumberFormatException nfe) {
            throw new InvalidX3DException(nfe.toString());
        }
        if (element.hasAttribute(DIFFUSE_COLOR)) {
            readInto(element.getAttribute(DIFFUSE_COLOR), diffuseColor);
        }
        if (element.hasAttribute(EMISSIVE_COLOR)) {
            readInto(element.getAttribute(EMISSIVE_COLOR), emissiveColor);
        }
        if (element.hasAttribute(SPECULAR_COLOR)) {
            readInto(element.getAttribute(SPECULAR_COLOR), specularColor);
        }
        gl.glMaterialf(gl.GL_FRONT_AND_BACK, gl.GL_SHININESS, shininess * 128.0f);
        float alpha = 1.0f - transparency;
        gl.glMaterialfv(gl.GL_FRONT_AND_BACK, gl.GL_AMBIENT, new float[] { ambientIntensity, ambientIntensity, ambientIntensity, alpha });
        gl.glMaterialfv(gl.GL_FRONT_AND_BACK, gl.GL_DIFFUSE, new float[] { diffuseColor[0], diffuseColor[1], diffuseColor[2], alpha });
        gl.glMaterialfv(gl.GL_FRONT_AND_BACK, gl.GL_SPECULAR, new float[] { specularColor[0], specularColor[1], specularColor[2], alpha });
        gl.glMaterialfv(gl.GL_FRONT_AND_BACK, gl.GL_EMISSION, new float[] { emissiveColor[0], emissiveColor[1], emissiveColor[2], alpha });
    }

    /**
     * Reads a number of floats from the specified string into the specified
     * array.
     *
     * @param string the string from which to read the floats
     * @param values the array into which to write the floats
     * @exception InvalidX3DException if one of the values was invalid
     */
    private void readInto(String string, float[] values) throws InvalidX3DException {
        StringTokenizer st = new StringTokenizer(string);
        int i;
        for (i = 0; i < values.length && st.hasMoreTokens(); i++) {
            try {
                values[i] = Float.parseFloat(st.nextToken());
            } catch (NumberFormatException nfe) {
                throw new InvalidX3DException(nfe.toString());
            }
        }
        if (st.hasMoreTokens() || i != values.length) {
            throw new InvalidX3DException("Wrong number of values");
        }
    }

    /**
     * Draws a box element.
     *
     * @param element the element to draw
     * @exception InvalidX3DException if the element is invalid
     */
    private void drawBox(Element element) throws InvalidX3DException {
        float[] size = new float[] { 2.0f, 2.0f, 2.0f };
        if (element.hasAttribute(SIZE)) {
            readInto(element.getAttribute(SIZE), size);
        }
        gl.glPushMatrix();
        gl.glScaled(size[0], size[1], size[2]);
        glut.glutSolidCube(1.0);
        gl.glPopMatrix();
    }

    /**
     * Draws a cylinder element.
     *
     * @param element the element to draw
     * @exception InvalidX3DException if the element is invalid
     */
    private void drawCylinder(Element element) throws InvalidX3DException {
        boolean bottom = true, top = true, side = true;
        float height = 2.0f, radius = 1.0f;
        if (element.hasAttribute(TOP)) {
            top = Boolean.valueOf(element.getAttribute(TOP)).booleanValue();
        }
        if (element.hasAttribute(BOTTOM)) {
            bottom = Boolean.valueOf(element.getAttribute(BOTTOM)).booleanValue();
        }
        if (element.hasAttribute(SIDE)) {
            side = Boolean.valueOf(element.getAttribute(SIDE)).booleanValue();
        }
        try {
            if (element.hasAttribute(HEIGHT)) {
                height = Float.parseFloat(element.getAttribute(HEIGHT));
            }
            if (element.hasAttribute(RADIUS)) {
                radius = Float.parseFloat(element.getAttribute(RADIUS));
            }
        } catch (NumberFormatException nfe) {
            throw new InvalidX3DException(nfe.toString());
        }
        gl.glPushMatrix();
        gl.glTranslated(0, -height * 0.5, 0);
        gl.glRotated(-90.0, 1.0, 0.0, 0.0);
        if (side) {
            long gluq = glu.gluNewQuadric();
            glu.gluCylinder(gluq, radius, radius, height, 8, 8);
            glu.gluDeleteQuadric(gluq);
        }
        if (bottom) {
            long gluq = glu.gluNewQuadric();
            glu.gluQuadricOrientation(gluq, glu.GLU_INSIDE);
            glu.gluDisk(gluq, 0.0, radius, 8, 2);
            glu.gluDeleteQuadric(gluq);
        }
        if (top) {
            gl.glTranslated(0, 0, height);
            long gluq = glu.gluNewQuadric();
            glu.gluDisk(gluq, 0.0, radius, 8, 2);
            glu.gluDeleteQuadric(gluq);
        }
        gl.glPopMatrix();
    }

    /**
     * Draws a sphere element.
     *
     * @param element the element to draw
     * @exception InvalidX3DException if the element is invalid
     */
    private void drawSphere(Element element) throws InvalidX3DException {
        double radius = 1.0f;
        try {
            if (element.hasAttribute(RADIUS)) {
                radius = Float.parseFloat(element.getAttribute(RADIUS));
            }
        } catch (NumberFormatException nfe) {
            throw new InvalidX3DException(nfe.toString());
        }
        long gluq = glu.gluNewQuadric();
        glu.gluSphere(gluq, radius, 8, 8);
        glu.gluDeleteQuadric(gluq);
    }

    /**
     * Draws a cone element.
     *
     * @param element the element to draw
     * @exception InvalidX3DException if the element is invalid
     */
    private void drawCone(Element element) throws InvalidX3DException {
        boolean bottom = true, side = true;
        float height = 2.0f, bottomRadius = 1.0f;
        if (element.hasAttribute(BOTTOM)) {
            bottom = Boolean.valueOf(element.getAttribute(BOTTOM)).booleanValue();
        }
        if (element.hasAttribute(SIDE)) {
            side = Boolean.valueOf(element.getAttribute(SIDE)).booleanValue();
        }
        try {
            if (element.hasAttribute(HEIGHT)) {
                height = Float.parseFloat(element.getAttribute(HEIGHT));
            }
            if (element.hasAttribute(BOTTOM_RADIUS)) {
                bottomRadius = Float.parseFloat(element.getAttribute(BOTTOM_RADIUS));
            }
        } catch (NumberFormatException nfe) {
            throw new InvalidX3DException(nfe.toString());
        }
        gl.glPushMatrix();
        gl.glTranslated(0, -height * 0.5, 0);
        gl.glRotated(-90.0, 1.0, 0.0, 0.0);
        if (side) {
            long gluq = glu.gluNewQuadric();
            glu.gluCylinder(gluq, bottomRadius, 0, height, 8, 8);
            glu.gluDeleteQuadric(gluq);
        }
        if (bottom) {
            long gluq = glu.gluNewQuadric();
            glu.gluQuadricOrientation(gluq, glu.GLU_INSIDE);
            glu.gluDisk(gluq, 0.0, bottomRadius, 8, 2);
            glu.gluDeleteQuadric(gluq);
        }
        gl.glPopMatrix();
    }

    /**
     * Draws an indexed face set element.
     *
     * @param element the element to draw
     * @exception InvalidX3DException if the element is invalid
     */
    private void drawIndexedFaceSet(Element element) throws InvalidX3DException {
        boolean ccw = true, colorPerVertex = true, convex = true, normalPerVertex = true, solid = true;
        int[] colorIndex = new int[0], coordIndex = new int[0], normalIndex = new int[0], texCoordIndex = new int[0];
        float creaseAngle = 0.0f;
        if (element.hasAttribute(CCW)) {
            ccw = Boolean.valueOf(element.getAttribute(CCW)).booleanValue();
        }
        if (element.hasAttribute(COLOR_PER_VERTEX)) {
            colorPerVertex = Boolean.valueOf(element.getAttribute(COLOR_PER_VERTEX)).booleanValue();
        }
        if (element.hasAttribute(CONVEX)) {
            convex = Boolean.valueOf(element.getAttribute(CONVEX)).booleanValue();
        }
        if (element.hasAttribute(NORMAL_PER_VERTEX)) {
            normalPerVertex = Boolean.valueOf(element.getAttribute(NORMAL_PER_VERTEX)).booleanValue();
        }
        if (element.hasAttribute(SOLID)) {
            solid = Boolean.valueOf(element.getAttribute(SOLID)).booleanValue();
        }
        if (element.hasAttribute(COLOR_INDEX)) {
            colorIndex = readInts(element.getAttribute(COLOR_INDEX));
        }
        if (element.hasAttribute(COORD_INDEX)) {
            coordIndex = readInts(element.getAttribute(COORD_INDEX));
        }
        if (element.hasAttribute(NORMAL_INDEX)) {
            normalIndex = readInts(element.getAttribute(NORMAL_INDEX));
        }
        if (element.hasAttribute(TEX_COORD_INDEX)) {
            texCoordIndex = readInts(element.getAttribute(TEX_COORD_INDEX));
        }
        if (element.hasAttribute(CREASE_ANGLE)) {
            try {
                creaseAngle = Float.parseFloat(element.getAttribute(CREASE_ANGLE));
            } catch (NumberFormatException nfe) {
                throw new InvalidX3DException(nfe.toString());
            }
        }
        vertexArray = normalArray = null;
        NodeList nl = element.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            if (nl.item(i) instanceof Element) {
                draw((Element) nl.item(i));
            }
        }
        gl.glPushAttrib(gl.GL_ENABLE_BIT | gl.GL_POLYGON_BIT);
        if (solid) {
            gl.glEnable(gl.GL_CULL_FACE);
            if (ccw) {
                gl.glFrontFace(gl.GL_CCW);
            } else {
                gl.glFrontFace(gl.GL_CW);
            }
        } else {
            gl.glDisable(gl.GL_CULL_FACE);
        }
        gl.glEnable(gl.GL_NORMALIZE);
        double[] computedNormals = null;
        if (normalArray == null) {
            computedNormals = computeNormals(coordIndex, normalPerVertex, creaseAngle);
        }
        for (int i = 0, j = 0; i < coordIndex.length; i++, j++) {
            gl.glBegin(gl.GL_POLYGON);
            if (!normalPerVertex && normalArray != null) {
                gl.glNormal3d(normalArray[normalIndex[j] * 3], normalArray[normalIndex[j] * 3 + 1], normalArray[normalIndex[j] * 3 + 2]);
            }
            for (; i < coordIndex.length && coordIndex[i] != -1; i++) {
                if (normalPerVertex && normalArray != null) {
                    gl.glNormal3d(normalArray[normalIndex[i] * 3], normalArray[normalIndex[i] * 3 + 1], normalArray[normalIndex[i] * 3 + 2]);
                } else if (computedNormals != null) {
                    gl.glNormal3d(computedNormals[i * 3], computedNormals[i * 3 + 1], computedNormals[i * 3 + 2]);
                }
                gl.glVertex3d(vertexArray[coordIndex[i] * 3], vertexArray[coordIndex[i] * 3 + 1], vertexArray[coordIndex[i] * 3 + 2]);
            }
            gl.glEnd();
        }
        gl.glPopAttrib();
    }

    /**
     * Computes the normals for the current coordinate array.
     *
     * @param coordIndex the coordinate indices that define the polygons
     * @param normalPerVertex whether or not to smooth the normals
     * @param creaseAngle the crease angle
     * @return the array of computed normals
     */
    private double[] computeNormals(int[] coordIndex, boolean normalPerVertex, float creaseAngle) {
        double[] normals = new double[coordIndex.length * 3];
        for (int i = 0; i < coordIndex.length; i++) {
            double abx = vertexArray[coordIndex[i] * 3] - vertexArray[coordIndex[i + 1] * 3], aby = vertexArray[coordIndex[i] * 3 + 1] - vertexArray[coordIndex[i + 1] * 3 + 1], abz = vertexArray[coordIndex[i] * 3 + 2] - vertexArray[coordIndex[i + 1] * 3 + 2], cbx = vertexArray[coordIndex[i + 2] * 3] - vertexArray[coordIndex[i + 1] * 3], cby = vertexArray[coordIndex[i + 2] * 3 + 1] - vertexArray[coordIndex[i + 1] * 3 + 1], cbz = vertexArray[coordIndex[i + 2] * 3 + 2] - vertexArray[coordIndex[i + 1] * 3 + 2];
            double cpx = aby * cbz - abz * cby, cpy = abz * cbx - abx * cbz, cpz = abx * cby - aby * cbx, rlen = 1.0 / Math.sqrt(cpx * cpx + cpy * cpy + cpz * cpz);
            for (; i < coordIndex.length && coordIndex[i] != -1; i++) {
                normals[i * 3] = -cpx * rlen;
                normals[i * 3 + 1] = -cpy * rlen;
                normals[i * 3 + 2] = -cpz * rlen;
            }
        }
        if (normalPerVertex) {
            double[] smoothedNormals = new double[normals.length];
            for (int i = 0; i < coordIndex.length; i++) {
                if (coordIndex[i] != -1) {
                    double xa = normals[i * 3], ya = normals[i * 3 + 1], za = normals[i * 3 + 2];
                    for (int j = 0; j < coordIndex.length; j++) {
                        if (coordIndex[j] == coordIndex[i] && i != j) {
                            double dp = normals[i * 3] * normals[j * 3] + normals[i * 3 + 1] * normals[j * 3 + 1] + normals[i * 3 + 2] * normals[j * 3 + 2];
                            if (Math.acos(dp) < creaseAngle) {
                                xa += normals[j * 3];
                                ya += normals[j * 3 + 1];
                                za += normals[j * 3 + 2];
                            }
                        }
                    }
                    double rlen = 1.0 / Math.sqrt(xa * xa + ya * ya + za * za);
                    smoothedNormals[i * 3] = xa * rlen;
                    smoothedNormals[i * 3 + 1] = ya * rlen;
                    smoothedNormals[i * 3 + 2] = za * rlen;
                }
            }
            return smoothedNormals;
        } else {
            return normals;
        }
    }

    /**
     * Reads an number of integer values from the specified string and returns
     * them in an array.
     *
     * @param string the string containing the integer values
     * @return an array containing the values
     * @exception InvalidX3DException if one of the values is invalid
     */
    private int[] readInts(String string) throws InvalidX3DException {
        StringTokenizer st = new StringTokenizer(string, " \t\n\r\f,");
        int[] values = new int[st.countTokens()];
        for (int i = 0; i < values.length; i++) {
            try {
                values[i] = Integer.parseInt(st.nextToken());
            } catch (NumberFormatException nfe) {
                throw new InvalidX3DException(nfe.toString());
            }
        }
        return values;
    }

    /**
     * Draws a coordinate element.
     *
     * @param element the element to draw
     * @exception InvalidX3DException if the element is invalid
     */
    private void drawCoordinate(Element element) throws InvalidX3DException {
        if (element.hasAttribute(POINT)) {
            vertexArray = readDoubles(element.getAttribute(POINT));
        } else {
            vertexArray = new double[0];
        }
    }

    /**
     * Reads an number of double values from the specified string and returns
     * them in an array.
     *
     * @param string the string containing the double values
     * @return an array containing the values
     * @exception InvalidX3DException if one of the values is invalid
     */
    private double[] readDoubles(String string) throws InvalidX3DException {
        StringTokenizer st = new StringTokenizer(string, " \t\n\r\f,");
        double[] values = new double[st.countTokens()];
        for (int i = 0; i < values.length; i++) {
            try {
                values[i] = Double.parseDouble(st.nextToken());
            } catch (NumberFormatException nfe) {
                throw new InvalidX3DException(nfe.toString());
            }
        }
        return values;
    }

    /**
     * Draws a normal element.
     *
     * @param element the element to draw
     * @exception InvalidX3DException if the element is invalid
     */
    private void drawNormal(Element element) throws InvalidX3DException {
        if (element.hasAttribute(VECTOR)) {
            normalArray = readDoubles(element.getAttribute(VECTOR));
        } else {
            normalArray = new double[0];
        }
    }
}
