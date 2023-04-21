package sente;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;

/******************************************************************************
* This is creates the space for the robot 
* area without determining how it is displayed
* 
* @Version	$Id: RobotArenaUI.java,v 1.2 2001/07/31 03:27:36 thefirelane Exp $
*
* @Author	Lane Westlund
*
* Revisions	see http://www.sourceforge.net/projects/sente/
*
******************************************************************************/
public abstract class RobotArenaUI extends Component implements Runnable, MouseInputListener {

    /*****************
* the method used to add a UI element to the Arena
*
* @param e      The element to add to the ArenaUI
*
*****************/
    public abstract void addElement(GameElementUI e);

    /*****************
* 
* run method to run the UI
*
*****************/
    public void run() {
        while (true) {
            try {
                Thread.currentThread().sleep(30);
                repaint();
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }

    /*****************
* method used to get the X location from a fraction
*
* @param x      fraction of location along X axis of robot Arena
*
*****************/
    public int getArenaX(float x) {
        return ((int) (x * (getSize().width)));
    }

    /*****************
* method used to get the Y location from a fraction
*
* @param y      fraction of location along X axis of robot Arena
*
*****************/
    public int getArenaY(float y) {
        return ((int) (y * (getSize().height)));
    }

    /*****************
* method used to get the Z location from a fraction
*
* @param z      fraction of location along X axis of robot Arena
*
*****************/
    public int getArenaZ(float z) {
        return ((int) (z * 15));
    }
}
