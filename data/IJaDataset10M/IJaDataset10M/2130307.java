package com.hp.hpl.guess.layout;

import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import java.util.*;
import com.hp.hpl.guess.*;

/**
 * seperate layout thread
 *
 * @author Eytan Adar
 * @author Joshua Tyler
 * Copyright (c) 2003, Hewlett Packard Labs
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with
 * or without modification, are permitted provided that the following
 * conditions are met:
 *
 *   Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 *
 *   Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 *   Neither the name of the Hewlett Packard nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE TRUSTEES OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */
public class Rotate {

    private int xrotate = 0;

    private int yrotate = 0;

    private int pad = 20;

    public Rotate(Set nodes, Hashtable args) throws Exception {
        rotatePositions(nodes, pad);
    }

    /**
     * Rotates the x and y coordinates of each node so that the network
     * will maximally fill the display. Will result in some distortion.
     * Called internally if rescale is set to true, will rescale
     * smoothly if animateTransitions is true.
     *
     * @param nodes the nodes to rescale.
     */
    public static void rotatePositions(Set nodes, int pad) {
        int nNodes = nodes.size();
        double[] xPos = new double[nNodes];
        double[] yPos = new double[nNodes];
        Node[] nlist = new Node[nNodes];
        double xMax = Double.MIN_VALUE;
        double yMax = Double.MIN_VALUE;
        double xMin = Double.MAX_VALUE;
        double yMin = Double.MAX_VALUE;
        Iterator it = nodes.iterator();
        int i = 0;
        while (it.hasNext()) {
            nlist[i] = (Node) it.next();
            xPos[i] = (double) nlist[i].getX();
            yPos[i] = (double) nlist[i].getY();
            xMax = Math.max(xMax, xPos[i]);
            yMax = Math.max(yMax, yPos[i]);
            xMin = Math.min(xMin, xPos[i]);
            yMin = Math.min(yMin, yPos[i]);
            i++;
        }
        double width = xMax - xMin;
        double height = yMax - yMin;
        double newx = xMin;
        double newy = yMin;
        double oxmx = xMax;
        double oymx = yMax;
        double oymi = yMin;
        double oxmi = xMin;
        xMax = oymx;
        xMin = oymi;
        yMax = oxmx;
        yMin = oxmi;
        double xTrans = newx;
        double yTrans = newy;
        for (i = 0; i < nNodes; i++) {
            double xP = xPos[i];
            double yP = yPos[i];
            xPos[i] = yP;
            yPos[i] = xP;
            xPos[i] = ((xPos[i] - xMin) / (xMax - xMin)) * (width);
            yPos[i] = ((yPos[i] - yMin) / (yMax - yMin)) * (height);
            xPos[i] = xPos[i] + xTrans;
            yPos[i] = yPos[i] + yTrans;
            nlist[i].setLocation(xPos[i], yPos[i]);
        }
    }
}
