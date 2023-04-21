package game.gui.visj3d;

import game.gui.structure3d.j3d.GOB3DSphere;
import game.gui.structure3d.j3d.JKCanvasJ3D;
import game.gui.vis.Node;
import game.gui.vis.VNet3D;
import java.awt.*;
import game.gui.NetworkStructure3D;
import game.neurons.Neuron;

class NodeJ3D extends Node {

    private GOB3DSphere sphere;

    /**
     * @param onet
     * @param oid
     * @param otype
     * @param realRef
     */
    NodeJ3D(VNet3D onet, int oid, int otype, Neuron realRef) {
        super(onet, oid, otype, realRef);
    }

    /**
     * @param onet
     * @param oid
     * @param otype
     * @param othreshold
     * @param realRef
     */
    NodeJ3D(VNet3D onet, int oid, int otype, double othreshold, Neuron realRef) {
        super(onet, oid, otype, othreshold, realRef);
    }

    public void draw(Canvas ocan) {
        JKCanvasJ3D tcan = (JKCanvasJ3D) ocan;
        double r = net.NODE_RADIUS;
        if (type == VNet3D.IN) {
            Color c = NetworkStructure3D.getInputColor(id / 2);
            tcan.setColor(c);
            tcan.drawArrow(id, x - 5 * r, y, z, x - r, y, z, r / 3, r / 1.5, 2 * r);
        } else if (type == VNet3D.OUT) {
            tcan.setColor(net.NODE_COLOR_OUT);
            tcan.drawArrow(id, x + r, y, z, x + 5 * r, y, z, r / 3, r / 1.5, 2 * r);
        } else if (type == VNet3D.HIDDEN) {
            tcan.setColor(net.NODE_COLOR_HIDDEN);
            String typeN = NetworkStructure3D.getUnitType(id / 2);
            if (VNet3D.SHOW_NODE_MARKS) {
                if (typeN.equals("Sigmoid")) {
                    tcan.drawMark(id, x, y, z - 3 * r, JKCanvasJ3D.MARK_SIGMOID, r);
                } else if (typeN.equals("Linear")) {
                    tcan.drawMark(id, x, y, z - 3 * r, JKCanvasJ3D.MARK_LINEAR, r);
                } else if (typeN.equals("MultiGaussian")) {
                    tcan.drawMark(id, x, y, z - 3 * r, JKCanvasJ3D.MARK_MULTIGAUSSIAN, r);
                } else if (typeN.equals("Rational")) {
                    tcan.drawMark(id, x, y, z - 3 * r, JKCanvasJ3D.MARK_RATIONAL, r);
                } else if (typeN.equals("Polynomial Horner")) {
                    tcan.drawMark(id, x, y, z - 3 * r, JKCanvasJ3D.MARK_POLYHORNER, r);
                } else if (typeN.equals("Polynomial")) {
                    tcan.drawMark(id, x, y, z - 3 * r, JKCanvasJ3D.MARK_POLYNOMIAL, r);
                } else if (typeN.equals("Polynomial - NR")) {
                    tcan.drawMark(id, x, y, z - 3 * r, JKCanvasJ3D.MARK_POLYNR, r);
                } else if (typeN.equals("Sine")) {
                    tcan.drawMark(id, x, y, z - 3 * r, JKCanvasJ3D.MARK_SINE, r);
                } else if (typeN.equals("Polynomial - combi")) {
                    tcan.drawMark(id, x, y, z - 3 * r, JKCanvasJ3D.MARK_POLYCOMBI, r);
                } else if (typeN.equals("Exponential")) {
                    tcan.drawMark(id, x, y, z - 3 * r, JKCanvasJ3D.MARK_EXPONENTIAL, r);
                } else if (typeN.equals("Gaussian")) {
                    tcan.drawMark(id, x, y, z - 3 * r, JKCanvasJ3D.MARK_GAUSSIAN, r);
                } else if (typeN.equals("Gauss")) {
                    tcan.drawMark(id, x, y, z - 3 * r, JKCanvasJ3D.MARK_GAUSS, r);
                } else if (typeN.equals("Gaussian PDF")) {
                    tcan.drawMark(id, x, y, z - 3 * r, JKCanvasJ3D.MARK_GAUSSPDF, r);
                } else if (typeN.equals("Perceptron net")) {
                    tcan.drawMark(id, x, y, z - 3 * r, JKCanvasJ3D.MARK_PNET, r);
                } else if (typeN.equals("Perceptron net(NR)")) {
                    tcan.drawMark(id, x, y, z - 3 * r, JKCanvasJ3D.MARK_PNETNR, r);
                }
            }
        }
        sphere = tcan.drawSphere(id, x, y, z, net.NODE_RADIUS);
    }

    public void setColor(Color ocol) {
        sphere.setColor(ocol);
    }

    public void setDefaultColor() {
        if (type == VNet3D.IN) {
            Color c = NetworkStructure3D.getInputColor(id / 2);
            sphere.setColor(c);
        } else if (type == VNet3D.OUT) sphere.setColor(net.NODE_COLOR_OUT); else if (type == VNet3D.HIDDEN) sphere.setColor(net.NODE_COLOR_HIDDEN);
    }
}
