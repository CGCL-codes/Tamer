package org.openscience.jmol;

import java.awt.Dimension;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Matrix4d;
import javax.vecmath.AxisAngle4d;

public class TransformManager {

    DisplayControl control;

    TransformManager(DisplayControl control) {
        this.control = control;
    }

    public void homePosition() {
        matrixRotate.setIdentity();
        setSlabEnabled(false);
        slabToPercent(100);
        setZoomEnabled(true);
        zoomToPercent(100);
        scaleFitToScreen();
    }

    /****************************************************************
   ROTATIONS
  ****************************************************************/
    public final Matrix4d matrixRotate = new Matrix4d();

    public void rotateXYBy(int xDelta, int yDelta) {
        double rotateAccelerator = 1.1f;
        double ytheta = Math.PI * xDelta / minScreenDimension;
        rotateByY(ytheta * rotateAccelerator);
        double xtheta = Math.PI * yDelta / minScreenDimension;
        rotateByX(xtheta * rotateAccelerator);
    }

    public void rotateZBy(int zDelta) {
        double rotateAccelerator = 1.1f;
        double ztheta = Math.PI * zDelta / minScreenDimension;
        rotateByZ(ztheta * rotateAccelerator);
    }

    public void rotateFront() {
        matrixRotate.setIdentity();
    }

    public void rotateToX(double angleRadians) {
        matrixRotate.rotX(angleRadians);
    }

    public void rotateToY(double angleRadians) {
        matrixRotate.rotY(angleRadians);
    }

    public void rotateToZ(double angleRadians) {
        matrixRotate.rotZ(angleRadians);
    }

    public void rotateByX(double angleRadians) {
        matrixTemp.rotX(angleRadians);
        matrixRotate.mul(matrixTemp, matrixRotate);
    }

    public void rotateByY(double angleRadians) {
        matrixTemp.rotY(angleRadians);
        matrixRotate.mul(matrixTemp, matrixRotate);
    }

    public void rotateByZ(double angleRadians) {
        matrixTemp.rotZ(angleRadians);
        matrixRotate.mul(matrixTemp, matrixRotate);
    }

    public void rotate(AxisAngle4d axisAngle) {
        matrixTemp.setIdentity();
        matrixTemp.setRotation(axisAngle);
        matrixRotate.mul(matrixTemp, matrixRotate);
    }

    /****************************************************************
   TRANSLATIONS
  ****************************************************************/
    public int xTranslation;

    public int yTranslation;

    public void translateXYBy(int xDelta, int yDelta) {
        xTranslation += xDelta;
        yTranslation += yDelta;
    }

    public void translateToXPercent(int percent) {
        xTranslation = (dimCurrent.width / 2) + dimCurrent.width * percent / 100;
    }

    public void translateToYPercent(int percent) {
        yTranslation = (dimCurrent.height / 2) + dimCurrent.height * percent / 100;
    }

    public void translateToZPercent(int percent) {
    }

    public int getTranslationXPercent() {
        return (xTranslation - dimCurrent.width / 2) * 100 / dimCurrent.width;
    }

    public int getTranslationYPercent() {
        return (yTranslation - dimCurrent.height / 2) * 100 / dimCurrent.height;
    }

    public int getTranslationZPercent() {
        return 0;
    }

    /****************************************************************
   ZOOM
  ****************************************************************/
    public boolean zoomEnabled = true;

    public int zoomPercent = 100;

    public int zoomPercentSetting = 100;

    public void zoomBy(int pixels) {
        int percent = pixels * zoomPercentSetting / minScreenDimension;
        if (percent == 0) percent = (pixels < 0) ? -1 : 1;
        zoomByPercent(percent);
    }

    public int getZoomPercent() {
        return zoomPercent;
    }

    public int getZoomPercentSetting() {
        return zoomPercentSetting;
    }

    public void zoomToPercent(int percentZoom) {
        zoomPercentSetting = percentZoom;
        calcZoom();
    }

    public void zoomByPercent(int percentZoom) {
        int delta = percentZoom * zoomPercentSetting / 100;
        if (delta == 0) delta = (percentZoom < 0) ? -1 : 1;
        zoomPercentSetting += delta;
        calcZoom();
    }

    private void calcZoom() {
        if (zoomPercentSetting < 10) zoomPercentSetting = 10;
        if (zoomPercentSetting > 1000) zoomPercentSetting = 1000;
        zoomPercent = (zoomEnabled) ? zoomPercentSetting : 100;
        scalePixelsPerAngstrom = scaleDefaultPixelsPerAngstrom * zoomPercent / 100;
    }

    public void setZoomEnabled(boolean zoomEnabled) {
        if (this.zoomEnabled != zoomEnabled) {
            this.zoomEnabled = zoomEnabled;
            calcZoom();
        }
    }

    public boolean slabEnabled = false;

    public int modeSlab;

    public int slabValue;

    public int slabPercentSetting = 100;

    public boolean getSlabEnabled() {
        return slabEnabled;
    }

    public int getSlabPercentSetting() {
        return slabPercentSetting;
    }

    public void slabBy(int pixels) {
        int percent = pixels * slabPercentSetting / minScreenDimension;
        if (percent == 0) percent = (pixels < 0) ? -1 : 1;
        slabPercentSetting += percent;
        calcSlab();
    }

    public void slabToPercent(int percentSlab) {
        slabPercentSetting = percentSlab;
        calcSlab();
    }

    public void slabByPercent(int percentSlab) {
        int delta = percentSlab * slabPercentSetting / 100;
        if (delta == 0) delta = (percentSlab < 0) ? -1 : 1;
        slabPercentSetting += delta;
        calcSlab();
    }

    public void setSlabEnabled(boolean slabEnabled) {
        if (this.slabEnabled != slabEnabled) {
            this.slabEnabled = slabEnabled;
            calcSlab();
        }
    }

    public void setModeSlab(int modeSlab) {
        this.modeSlab = modeSlab;
    }

    public int getModeSlab() {
        return modeSlab;
    }

    private void calcSlab() {
        if (slabEnabled) {
            if (slabPercentSetting < 0) slabPercentSetting = 0; else if (slabPercentSetting > 100) slabPercentSetting = 100;
            int radius = (int) (control.getRotationRadius() * scalePixelsPerAngstrom);
            slabValue = (int) ((-100 + slabPercentSetting) * 2 * radius / 100);
        }
    }

    /****************************************************************
   PERSPECTIVE
  ****************************************************************/
    public boolean perspectiveDepth = true;

    public double cameraDepth = 3;

    public int cameraZ;

    public void setPerspectiveDepth(boolean perspectiveDepth) {
        this.perspectiveDepth = perspectiveDepth;
        scaleFitToScreen();
    }

    public boolean getPerspectiveDepth() {
        return perspectiveDepth;
    }

    public void setCameraDepth(double depth) {
        cameraDepth = depth;
    }

    public double getCameraDepth() {
        return cameraDepth;
    }

    public int getCameraZ() {
        return cameraZ;
    }

    /****************************************************************
   SCREEN SCALING
  ****************************************************************/
    public Dimension dimCurrent;

    public int minScreenDimension;

    public double scalePixelsPerAngstrom;

    public double scaleDefaultPixelsPerAngstrom;

    public void setScreenDimension(Dimension dimCurrent) {
        this.dimCurrent = dimCurrent;
    }

    public Dimension getScreenDimension() {
        return dimCurrent;
    }

    public void scaleFitToScreen() {
        if (dimCurrent == null || control.getFrame() == null) {
            return;
        }
        xTranslation = dimCurrent.width / 2;
        yTranslation = dimCurrent.height / 2;
        minScreenDimension = dimCurrent.width;
        if (dimCurrent.height < minScreenDimension) minScreenDimension = dimCurrent.height;
        if (minScreenDimension > 2) minScreenDimension -= 2;
        scalePixelsPerAngstrom = minScreenDimension / 2 / control.getRotationRadius();
        if (perspectiveDepth) {
            cameraZ = (int) (cameraDepth * minScreenDimension);
            double scaleFactor = (cameraZ + minScreenDimension / 2) / (double) cameraZ;
            scaleFactor += 0.02;
            scalePixelsPerAngstrom *= scaleFactor;
        }
        scaleDefaultPixelsPerAngstrom = scalePixelsPerAngstrom;
        zoomPercentSetting = zoomPercent = 100;
        zoomEnabled = true;
    }

    public int screenAtomDiameter(int z, Atom atom, int percentVdw) {
        double vdwRadius = atom.getVanderwaalsRadius();
        if (z > 0) System.out.println("--?QUE? no way that z > 0--");
        if (vdwRadius <= 0) System.out.println("--?QUE? vdwRadius=" + vdwRadius);
        int d = (int) (2 * vdwRadius * scalePixelsPerAngstrom * percentVdw / 100);
        if (perspectiveDepth) d = (d * cameraZ) / (cameraZ - z);
        return d;
    }

    public int screenBondWidth(int z, int percentAngstrom) {
        int w = (int) (scalePixelsPerAngstrom * percentAngstrom / 100);
        if (perspectiveDepth) w = (w * cameraZ) / (cameraZ - z);
        return w;
    }

    public double scaleToScreen(int z, double sizeAngstroms) {
        double pixelSize = sizeAngstroms * scalePixelsPerAngstrom;
        if (perspectiveDepth) pixelSize = (pixelSize * cameraZ) / (cameraZ - z);
        return pixelSize;
    }

    public int scaleToScreen(int z, int milliAngstroms) {
        int pixelSize = (int) (milliAngstroms * scalePixelsPerAngstrom / 1000);
        if (perspectiveDepth) pixelSize = (pixelSize * cameraZ) / (cameraZ - z);
        return pixelSize;
    }

    /****************************************************************
   TRANSFORMATIONS
  ****************************************************************/
    public final Matrix4d matrixTransform = new Matrix4d();

    private final Point3d point3dScreenTemp = new Point3d();

    private final Matrix4d matrixTemp = new Matrix4d();

    private final Vector3d vectorTemp = new Vector3d();

    public void calcViewTransformMatrix() {
        matrixTransform.setIdentity();
        vectorTemp.set(control.getRotationCenter());
        matrixTemp.setZero();
        matrixTemp.setTranslation(vectorTemp);
        matrixTransform.sub(matrixTemp);
        matrixTransform.mul(matrixRotate, matrixTransform);
        vectorTemp.x = 0;
        vectorTemp.y = 0;
        vectorTemp.z = control.getRotationRadius();
        matrixTemp.setTranslation(vectorTemp);
        matrixTransform.sub(matrixTemp);
        matrixTemp.set(scalePixelsPerAngstrom);
        matrixTemp.m11 = -scalePixelsPerAngstrom;
        matrixTransform.mul(matrixTemp, matrixTransform);
    }

    public void transformPoint(Point3d pointAngstroms, Point3d pointScreen) {
        pointScreen.set(transformPoint(pointAngstroms));
    }

    public Point3d transformPoint(Point3d pointAngstroms) {
        matrixTransform.transform(pointAngstroms, point3dScreenTemp);
        if (perspectiveDepth) {
            int depth = cameraZ - (int) point3dScreenTemp.z;
            point3dScreenTemp.x = (((int) point3dScreenTemp.x * cameraZ) / depth) + xTranslation;
            point3dScreenTemp.y = (((int) point3dScreenTemp.y * cameraZ) / depth) + yTranslation;
        } else {
            point3dScreenTemp.x += xTranslation;
            point3dScreenTemp.y += yTranslation;
        }
        return point3dScreenTemp;
    }

    /****************************************************************
   exports for POV rendering
  ****************************************************************/
    public Matrix4d getPovRotateMatrix() {
        return new Matrix4d(matrixRotate);
    }

    public Matrix4d getPovTranslateMatrix() {
        Matrix4d matrixPovTranslate = new Matrix4d();
        matrixPovTranslate.setIdentity();
        matrixPovTranslate.get(vectorTemp);
        vectorTemp.x = (xTranslation - dimCurrent.width / 2) / scalePixelsPerAngstrom;
        vectorTemp.y = -(yTranslation - dimCurrent.height / 2) / scalePixelsPerAngstrom;
        vectorTemp.z = 0;
        matrixPovTranslate.set(vectorTemp);
        return matrixPovTranslate;
    }
}
