package org.reprap.scanning.GUI;

import org.reprap.scanning.FunctionLibraries.MatrixManipulations;
import org.reprap.scanning.Geometry.*;
import org.reprap.scanning.DataStructures.*;
import javax.swing.JFrame;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.*;
import com.sun.opengl.util.Animator;
import Jama.*;

public class Graphics3DFeedback {

    private static final double tau = Math.PI * 2;

    private static final int displayfudgefactorY = 24;

    private static final int displayfudgefactorX = 8;

    private final Point3d initialeyepoint;

    private final double maxdistance;

    private GLU glu = new GLU();

    private Animator animator;

    public JFrame frame;

    private boolean exituserinteraction;

    private final AxisAlignedBoundingBox volumeofinterest, boundingvolumeofobject;

    private final AxisAlignedBoundingBox[] surfacevoxels;

    private final Point2d[] calibrationcirclecenters;

    private final Ellipse calibrationellipse;

    private final Point3d[] cameracentre;

    private final Image[] image;

    private final Line3d[] principalaxis;

    private final Line3d[] directionandscale;

    private double[] tforimageplane;

    private final int pixelstepforimageplanedisplay;

    private Point3d eyepoint, up, lookat;

    private int calibrationsheet, cameras, volumeofinterestwireframe, objectboundingvolumewireframe, bordervoxels;

    private int[] imageplanes, cameralines;

    private static float red[] = { 1.0f, 0.0f, 0.0f, 1.0f };

    private static float green[] = { 0.0f, 1.0f, 0.0f, 1.0f };

    private static float blue[] = { 0.0f, 0.0f, 1.0f, 1.0f };

    private static float white[] = { 1.0f, 1.0f, 1.0f, 1.0f };

    private static float black[] = { 0.0f, 0.0f, 0.0f, 1.0f };

    private static float yellow[] = { 1.0f, 1.0f, 0.0f, 1.0f };

    private static float grey[] = { 0.5f, 0.5f, 0.5f, 1.0f };

    public Graphics3DFeedback(AxisAlignedBoundingBox volofinterest, AxisAlignedBoundingBox objectboundingvolume, Point2d[] calibrationcirclecentersinrealworldcoordinates, Ellipse standardcalibrationcircleonprintedsheet, Image[] images, AxisAlignedBoundingBox[] surfacevoxel, int pixelstepinimageplane) {
        int count = 0;
        for (int i = 0; i < images.length; i++) if (!images[i].skipprocessing) count++;
        cameracentre = new Point3d[count];
        image = new Image[count];
        tforimageplane = new double[count];
        principalaxis = new Line3d[count];
        directionandscale = new Line3d[count];
        imageplanes = new int[count];
        cameralines = new int[count];
        double initialz = 0;
        count = 0;
        for (int i = 0; i < images.length; i++) if (!images[i].skipprocessing) {
            image[count] = images[i].clone();
            Matrix P = image[count].getWorldtoImageTransformMatrix();
            cameracentre[count] = new Point3d(MatrixManipulations.GetRightNullSpace(P));
            tforimageplane[count] = 0.09;
            Matrix H = new Matrix(3, 3);
            H.set(0, 0, P.get(0, 0));
            H.set(0, 1, P.get(0, 1));
            H.set(0, 2, P.get(0, 3));
            H.set(1, 0, P.get(1, 0));
            H.set(1, 1, P.get(1, 1));
            H.set(1, 2, P.get(1, 3));
            H.set(2, 0, P.get(2, 0));
            H.set(2, 1, P.get(2, 1));
            H.set(2, 2, P.get(2, 3));
            Point2d principalaxispoint = new Point2d(MatrixManipulations.PseudoInverse(H).times(new Point2d(0, 0).ConvertPointTo3x1Matrix()));
            Point2d directionandscalepoint = new Point2d(MatrixManipulations.PseudoInverse(H).times(new Point2d(0, 1).ConvertPointTo3x1Matrix()));
            principalaxis[count] = new Line3d(cameracentre[count], new Point3d(principalaxispoint, 0));
            directionandscale[count] = new Line3d(cameracentre[count], new Point3d(directionandscalepoint, 0));
            if (cameracentre[count].z > initialz) initialz = cameracentre[count].z;
            count++;
        }
        pixelstepforimageplanedisplay = pixelstepinimageplane;
        calibrationcirclecenters = new Point2d[calibrationcirclecentersinrealworldcoordinates.length];
        for (int i = 0; i < calibrationcirclecenters.length; i++) calibrationcirclecenters[i] = calibrationcirclecentersinrealworldcoordinates[i].clone();
        calibrationellipse = standardcalibrationcircleonprintedsheet.clone();
        volumeofinterest = volofinterest.clone();
        boundingvolumeofobject = objectboundingvolume.clone();
        initialeyepoint = new Point3d(0, 0, initialz);
        maxdistance = initialz;
        eyepoint = initialeyepoint.clone();
        up = new Point3d(0, 1, 0);
        lookat = new Point3d(0, 0, 0);
        surfacevoxels = new AxisAlignedBoundingBox[surfacevoxel.length];
        for (int i = 0; i < surfacevoxels.length; i++) surfacevoxels[i] = surfacevoxel[i].clone();
    }

    public void Display() {
        exituserinteraction = false;
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        GLCapabilities capabilities = new GLCapabilities();
        GLCanvas drawable = new GLCanvas(capabilities);
        drawable.addGLEventListener(new OpenGLRenderer());
        frame.add(drawable);
        int framesizex, framesizey;
        int screenwidth, screenheight;
        screenwidth = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().getBounds().width;
        screenheight = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().getBounds().height;
        framesizex = (screenwidth / 2) + displayfudgefactorX;
        framesizey = (screenheight / 2) + displayfudgefactorY;
        frame.setSize(framesizex, framesizey);
        frame.setLocation((screenwidth - framesizex) / 2, (screenheight - framesizey) / 2);
        animator = new Animator(drawable);
        frame.setVisible(true);
        drawable.requestFocus();
        animator.start();
        drawable.repaint();
        System.out.println("Press Q to quit.");
        while (!exituserinteraction) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        }
    }

    /*********************************************************
	 * 
	 * JOGL OpenGL Listener class and methods
	 *
	 *********************************************************/
    class OpenGLRenderer implements GLEventListener, MouseListener, MouseMotionListener {

        private int prevMouseX, prevMouseY;

        private boolean showwireframe, showsurfacevoxels;

        private boolean[] showsiloutte;

        private final double stepforimageplane = 0.05;

        private boolean mouseRButtonDown = false;

        private boolean mouseLButtonDown = false;

        private int selectedimage = -1;

        private int oldselectedimage = -1;

        private int steps = 360;

        private GL gl;

        public void init(GLAutoDrawable drawable) {
            int[][] pointpair = AxisAlignedBoundingBox.GetPointPairIndicesFor3DWireFrameLines();
            System.out.println("Set what is visible by default");
            showwireframe = false;
            showsurfacevoxels = false;
            showsiloutte = new boolean[image.length];
            for (int i = 0; i < image.length; i++) showsiloutte[i] = false;
            gl = drawable.getGL();
            gl.glClearColor(0, 0, 0, 0);
            gl.glPixelStorei(GL.GL_UNPACK_ALIGNMENT, 1);
            gl.glClear(GL.GL_COLOR_BUFFER_BIT);
            System.out.println("Set up the camera points");
            cameras = gl.glGenLists(1);
            gl.glNewList(cameras, GL.GL_COMPILE);
            gl.glPointSize(10);
            gl.glBegin(GL.GL_POINTS);
            gl.glColor3f(red[0], red[1], red[2]);
            for (int i = 0; i < cameracentre.length; i++) gl.glVertex3d(cameracentre[i].x, cameracentre[i].y, cameracentre[i].z);
            gl.glEnd();
            gl.glEndList();
            System.out.println("Set up the wireframe for the bounding volume of the object");
            Point3d[] boundingvolvertices = boundingvolumeofobject.GetCornersof3DBoundingBox();
            objectboundingvolumewireframe = gl.glGenLists(1);
            gl.glNewList(objectboundingvolumewireframe, GL.GL_COMPILE);
            gl.glLineWidth(3);
            gl.glBegin(GL.GL_LINES);
            gl.glColor3f(green[0], green[1], green[2]);
            for (int i = 0; i < 12; i++) {
                gl.glVertex3d(boundingvolvertices[pointpair[i][0]].x, boundingvolvertices[pointpair[i][0]].y, boundingvolvertices[pointpair[i][0]].z);
                gl.glVertex3d(boundingvolvertices[pointpair[i][1]].x, boundingvolvertices[pointpair[i][1]].y, boundingvolvertices[pointpair[i][1]].z);
            }
            gl.glEnd();
            gl.glEndList();
            System.out.println("Set up the wireframe for the volume of interest");
            Point3d[] vertices = volumeofinterest.GetCornersof3DBoundingBox();
            volumeofinterestwireframe = gl.glGenLists(1);
            gl.glNewList(volumeofinterestwireframe, GL.GL_COMPILE);
            gl.glLineWidth(3);
            gl.glBegin(GL.GL_LINES);
            gl.glColor3f(yellow[0], yellow[1], yellow[2]);
            for (int i = 0; i < 12; i++) {
                gl.glVertex3d(vertices[pointpair[i][0]].x, vertices[pointpair[i][0]].y, vertices[pointpair[i][0]].z);
                gl.glVertex3d(vertices[pointpair[i][1]].x, vertices[pointpair[i][1]].y, vertices[pointpair[i][1]].z);
            }
            gl.glEnd();
            gl.glEndList();
            System.out.println("Set up the calibration sheet");
            vertices = volumeofinterest.GetCornersof3DBoundingBox();
            calibrationsheet = gl.glGenLists(1);
            gl.glNewList(calibrationsheet, GL.GL_COMPILE);
            gl.glNormal3f(0.0f, 0.0f, 1.0f);
            gl.glBegin(GL.GL_QUADS);
            gl.glColor3f(white[0], white[1], white[2]);
            for (int i = 0; i < 4; i++) gl.glVertex3d(vertices[i].x, vertices[i].y, vertices[i].z);
            gl.glEnd();
            for (int i = 0; i < calibrationcirclecenters.length; i++) {
                gl.glBegin(GL.GL_TRIANGLE_FAN);
                gl.glColor3f(black[0], black[1], black[2]);
                gl.glVertex3d(calibrationcirclecenters[i].x, calibrationcirclecenters[i].y, 0);
                Ellipse temp = calibrationellipse.clone();
                temp.ResetCenter(calibrationcirclecenters[i]);
                for (int step = 0; step < steps; step++) {
                    double angle = (tau / steps) * step;
                    Point2d circumferencepoint = temp.GetEllipseEdgePointPolar(angle);
                    gl.glVertex3d(circumferencepoint.x, circumferencepoint.y, 0);
                }
                Point2d circumferencepoint = temp.GetEllipseEdgePointPolar((double) 0);
                gl.glVertex3d(circumferencepoint.x, circumferencepoint.y, 0);
                gl.glEnd();
            }
            gl.glEndList();
            System.out.println("Set up cubes for the surface voxels");
            bordervoxels = gl.glGenLists(1);
            gl.glNewList(bordervoxels, GL.GL_COMPILE);
            gl.glBegin(GL.GL_QUADS);
            gl.glColor3f(grey[0], grey[1], grey[2]);
            int[][] faces = AxisAlignedBoundingBox.GetPointQuadIndicesForBuildingFaces();
            for (int k = 0; k < surfacevoxels.length; k++) {
                Point3d[] v = surfacevoxels[k].GetCornersof3DBoundingBox();
                for (int i = 0; i < 6; i++) for (int j = 0; j < 4; j++) gl.glVertex3d(v[faces[i][j]].x, v[faces[i][j]].y, v[faces[i][j]].z);
            }
            gl.glEnd();
            gl.glEndList();
            System.out.println("Set up image-planes");
            for (int i = 0; i < image.length; i++) Imageplane(i);
            drawable.addKeyListener(new KeyAdapter() {

                public void keyPressed(KeyEvent e) {
                    dispatchKey(e.getKeyCode(), e.getKeyChar());
                }
            });
            drawable.addMouseListener(this);
            drawable.addMouseMotionListener(this);
        }

        public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
            gl = drawable.getGL();
            gl.glViewport(0, 0, width, height);
        }

        private void dispatchKey(int keyCode, char k) {
            switch(keyCode) {
                case KeyEvent.VK_ESCAPE:
                    eyepoint = initialeyepoint.clone();
                    lookat = new Point3d(0, 0, 0);
                    up = new Point3d(0, 1, 0);
                    break;
                case KeyEvent.VK_LEFT:
                case KeyEvent.VK_KP_LEFT:
                    if (!mouseLButtonDown) {
                        Point3d axisofrotation = up.clone();
                        axisofrotation = axisofrotation.times(0.017453293 / Math.sqrt(axisofrotation.lengthSquared()));
                        Matrix R = MatrixManipulations.getRotationMatrixFromRodriguesRotationVector(axisofrotation.ConvertPointTo3x1Matrix());
                        lookat = eyepoint.minusEquals(new Point3d(R.times(eyepoint.minusEquals(lookat).ConvertPointTo3x1Matrix())));
                    } else {
                        Point3d axisofrotation = eyepoint.minusEquals(lookat);
                        axisofrotation = axisofrotation.times(-0.017453293 / Math.sqrt(axisofrotation.lengthSquared()));
                        Matrix R = MatrixManipulations.getRotationMatrixFromRodriguesRotationVector(axisofrotation.ConvertPointTo3x1Matrix());
                        up = up.times(1 / Math.sqrt(up.lengthSquared()));
                        up = new Point3d(R.times(up.ConvertPointTo3x1Matrix()));
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                case KeyEvent.VK_KP_RIGHT:
                    if (!mouseLButtonDown) {
                        Point3d axisofrotation = up.clone();
                        axisofrotation = axisofrotation.times(-0.017453293 / Math.sqrt(axisofrotation.lengthSquared()));
                        Matrix R = MatrixManipulations.getRotationMatrixFromRodriguesRotationVector(axisofrotation.ConvertPointTo3x1Matrix());
                        lookat = eyepoint.minusEquals(new Point3d(R.times(eyepoint.minusEquals(lookat).ConvertPointTo3x1Matrix())));
                    } else {
                        Point3d axisofrotation = eyepoint.minusEquals(lookat);
                        axisofrotation = axisofrotation.times(0.017453293 / Math.sqrt(axisofrotation.lengthSquared()));
                        Matrix R = MatrixManipulations.getRotationMatrixFromRodriguesRotationVector(axisofrotation.ConvertPointTo3x1Matrix());
                        up = up.times(1 / Math.sqrt(up.lengthSquared()));
                        up = new Point3d(R.times(up.ConvertPointTo3x1Matrix()));
                    }
                    break;
                case KeyEvent.VK_UP:
                case KeyEvent.VK_KP_UP:
                    if ((mouseRButtonDown) || (mouseLButtonDown)) {
                        Point3d oldVector = eyepoint.minusEquals(lookat);
                        Point3d newVector = oldVector.times(0.9);
                        if (newVector.lengthSquared() < 100) newVector = oldVector.clone();
                        eyepoint = lookat.plusEquals(newVector);
                        if (!mouseRButtonDown) lookat = eyepoint.minusEquals(oldVector);
                    } else {
                        up = up.times(1 / Math.sqrt(up.lengthSquared()));
                        Point3d axisofrotation = up.crossProduct(eyepoint.minusEquals(lookat));
                        axisofrotation = axisofrotation.times(0.017453293 / Math.sqrt(axisofrotation.lengthSquared()));
                        Matrix R = MatrixManipulations.getRotationMatrixFromRodriguesRotationVector(axisofrotation.ConvertPointTo3x1Matrix());
                        lookat = eyepoint.minusEquals(new Point3d(R.times(eyepoint.minusEquals(lookat).ConvertPointTo3x1Matrix())));
                        up = new Point3d(R.times(up.ConvertPointTo3x1Matrix()));
                    }
                    break;
                case KeyEvent.VK_DOWN:
                case KeyEvent.VK_KP_DOWN:
                    if ((mouseRButtonDown) || (mouseLButtonDown)) {
                        Point3d oldVector = eyepoint.minusEquals(lookat);
                        Point3d newVector = oldVector.times(1.1);
                        eyepoint = lookat.plusEquals(newVector);
                        if (!mouseRButtonDown) lookat = eyepoint.minusEquals(oldVector);
                    } else {
                        up = up.times(1 / Math.sqrt(up.lengthSquared()));
                        Point3d axisofrotation = up.crossProduct(eyepoint.minusEquals(lookat));
                        axisofrotation = axisofrotation.times(-0.017453293 / Math.sqrt(axisofrotation.lengthSquared()));
                        Matrix R = MatrixManipulations.getRotationMatrixFromRodriguesRotationVector(axisofrotation.ConvertPointTo3x1Matrix());
                        lookat = eyepoint.minusEquals(new Point3d(R.times(eyepoint.minusEquals(lookat).ConvertPointTo3x1Matrix())));
                        up = new Point3d(R.times(up.ConvertPointTo3x1Matrix()));
                    }
                    break;
            }
            switch(k) {
                case 'w':
                case 'W':
                    showwireframe = !showwireframe;
                    break;
                case 'v':
                case 'V':
                    showsurfacevoxels = !showsurfacevoxels;
                    break;
                case 'q':
                case 'Q':
                    exit();
                    break;
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    int newselectedimage = Character.getNumericValue(k);
                    oldselectedimage = selectedimage;
                    if (newselectedimage < image.length) selectedimage = newselectedimage; else selectedimage = -1;
                    break;
                case '*':
                    oldselectedimage = selectedimage;
                    selectedimage = -1;
                    break;
                case '+':
                    if (selectedimage != -1) {
                        if (tforimageplane[selectedimage] < (1 - stepforimageplane)) tforimageplane[selectedimage] = tforimageplane[selectedimage] + stepforimageplane;
                    }
                    break;
                case '-':
                    if (selectedimage != -1) {
                        if (tforimageplane[selectedimage] > (stepforimageplane)) tforimageplane[selectedimage] = tforimageplane[selectedimage] - stepforimageplane;
                    }
                    break;
                case 's':
                case 'S':
                    if (selectedimage != -1) {
                        showsiloutte[selectedimage] = !showsiloutte[selectedimage];
                    }
                    break;
            }
        }

        public void display(GLAutoDrawable drawable) {
            gl = drawable.getGL();
            gl.glClear(GL.GL_COLOR_BUFFER_BIT);
            gl.glMatrixMode(GL.GL_PROJECTION);
            gl.glLoadIdentity();
            float widthHeightRatio = (float) drawable.getWidth() / (float) drawable.getHeight();
            glu.gluPerspective(45, widthHeightRatio, 1, maxdistance * 2);
            gl.glMatrixMode(GL.GL_MODELVIEW);
            gl.glLoadIdentity();
            glu.gluLookAt(eyepoint.x, eyepoint.y, eyepoint.z, lookat.x, lookat.y, lookat.z, up.x, up.y, up.z);
            gl.glCallList(calibrationsheet);
            gl.glCallList(cameras);
            gl.glCallList(volumeofinterestwireframe);
            if (showwireframe) gl.glCallList(objectboundingvolumewireframe);
            if (showsurfacevoxels) gl.glCallList(bordervoxels);
            for (int i = 0; i < imageplanes.length; i++) {
                if (oldselectedimage == i) {
                    Imageplane(i);
                    oldselectedimage = -1;
                } else if (selectedimage == i) Imageplane(i);
                gl.glCallList(imageplanes[i]);
                gl.glCallList(cameralines[i]);
            }
            gl.glFlush();
        }

        public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

        public void mousePressed(MouseEvent e) {
            prevMouseX = e.getX();
            prevMouseY = e.getY();
            if ((e.getModifiers() & MouseEvent.BUTTON3_MASK) != 0) {
                mouseRButtonDown = true;
            } else mouseLButtonDown = true;
        }

        public void mouseReleased(MouseEvent e) {
            if ((e.getModifiers() & MouseEvent.BUTTON3_MASK) != 0) {
                mouseRButtonDown = false;
            } else mouseLButtonDown = false;
        }

        public void mouseClicked(MouseEvent e) {
        }

        public void mouseDragged(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            double dx = (prevMouseX - x);
            double dy = (y - prevMouseY);
            Plane plane = new Plane(eyepoint, eyepoint.minusEquals(lookat));
            Point3d neweyepoint = plane.GetPointOnPlaneFromParametricCoordinates(up, new Point2d(dx, dy));
            if (!mouseRButtonDown) {
                lookat = lookat.plusEquals(neweyepoint.minusEquals(eyepoint));
            }
            eyepoint = neweyepoint.clone();
            prevMouseX = x;
            prevMouseY = y;
        }

        public void mouseMoved(MouseEvent e) {
        }

        public void exit() {
            exituserinteraction = true;
            animator.stop();
            frame.dispose();
        }

        private void Imageplane(int i) {
            Point3d imageplaneorigin = principalaxis[i].GetPointonLine(tforimageplane[i]);
            Plane imageplane = new Plane(imageplaneorigin, principalaxis[i].V);
            Point3d imageplane0x1y = imageplane.IntersectionPoint(directionandscale[i]);
            Point3d unitupvector = imageplane0x1y.minusEquals(imageplaneorigin);
            Point3d[] corner = new Point3d[4];
            corner[0] = imageplane.GetPointOnPlaneFromParametricCoordinates(unitupvector, new Point2d(-image[i].width / 2, -image[i].height / 2));
            corner[1] = imageplane.GetPointOnPlaneFromParametricCoordinates(unitupvector, new Point2d(-image[i].width / 2, image[i].height / 2));
            corner[2] = imageplane.GetPointOnPlaneFromParametricCoordinates(unitupvector, new Point2d(image[i].width / 2, image[i].height / 2));
            corner[3] = imageplane.GetPointOnPlaneFromParametricCoordinates(unitupvector, new Point2d(image[i].width / 2, -image[i].height / 2));
            cameralines[i] = gl.glGenLists(1);
            gl.glNewList(cameralines[i], GL.GL_COMPILE);
            gl.glLineWidth(2);
            gl.glBegin(GL.GL_LINES);
            gl.glColor3f(red[0], red[1], red[2]);
            for (int j = 0; j < 4; j++) {
                gl.glVertex3d(cameracentre[i].x, cameracentre[i].y, cameracentre[i].z);
                gl.glVertex3d(corner[j].x, corner[j].y, corner[j].z);
            }
            gl.glEnd();
            gl.glEndList();
            imageplanes[i] = gl.glGenLists(1);
            gl.glNewList(imageplanes[i], GL.GL_COMPILE);
            gl.glBegin(GL.GL_QUADS);
            gl.glNormal3f((float) -principalaxis[i].V.x, (float) -principalaxis[i].V.y, (float) -principalaxis[i].V.z);
            if (i != selectedimage) {
                for (int y = -image[i].height / 2; y < image[i].height / 2; y = y + pixelstepforimageplanedisplay) {
                    System.out.print(".");
                    for (int x = -image[i].width / 2; x < image[i].width / 2; x = x + pixelstepforimageplanedisplay) {
                        if (showsiloutte[i]) {
                            if (image[i].PointIsUnprocessed(imageplane.GetPointOnPlaneFromParametricCoordinates(unitupvector, new Point2d(x, y)))) gl.glColor3f(black[0], black[1], black[2]); else gl.glColor3f(grey[0], grey[1], grey[2]);
                        } else {
                            PixelColour colour = image[i].InterpolatePixelColour(image[i].getWorldtoImageTransform(imageplane.GetPointOnPlaneFromParametricCoordinates(unitupvector, new Point2d(x, y)).ConvertPointTo4x1Matrix()));
                            gl.glColor3f(((float) (int) (colour.getRed() & 0xff)) / (float) 255, ((float) (int) (colour.getGreen() & 0xff)) / (float) 255, ((float) (int) (colour.getBlue() & 0xff)) / (float) 255);
                        }
                        corner = new Point3d[4];
                        corner[0] = imageplane.GetPointOnPlaneFromParametricCoordinates(unitupvector, new Point2d(x - pixelstepforimageplanedisplay, y + pixelstepforimageplanedisplay));
                        corner[1] = imageplane.GetPointOnPlaneFromParametricCoordinates(unitupvector, new Point2d(x - pixelstepforimageplanedisplay, y - pixelstepforimageplanedisplay));
                        corner[2] = imageplane.GetPointOnPlaneFromParametricCoordinates(unitupvector, new Point2d(x + pixelstepforimageplanedisplay, y - pixelstepforimageplanedisplay));
                        corner[3] = imageplane.GetPointOnPlaneFromParametricCoordinates(unitupvector, new Point2d(x + pixelstepforimageplanedisplay, y + pixelstepforimageplanedisplay));
                        for (int j = 0; j < 4; j++) gl.glVertex3d(corner[j].x, corner[j].y, corner[j].z);
                    }
                }
                System.out.println();
            } else {
                gl.glColor3f(blue[0], blue[1], blue[2]);
                for (int j = 0; j < 4; j++) gl.glVertex3d(corner[j].x, corner[j].y, corner[j].z);
            }
            gl.glEnd();
            gl.glEndList();
        }
    }
}
