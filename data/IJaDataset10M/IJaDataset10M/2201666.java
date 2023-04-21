package org.jude.client.graph;

import java.util.Hashtable;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.net.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import org.jude.client.*;
import org.jude.client.db.*;
import org.jude.client.db.kb.*;
import org.jude.client.content.*;
import org.jude.client.editor.*;
import org.jude.client.editor.swing.*;
import org.jude.simplelogic.*;
import org.jude.client.editor.swing.logical.BasicFileViewer;

/**
 *  <p>
 *
 *  A Editor that permit to view files containing graphs/charts. <p>
 *
 *  This Editor use the package GRAPH Copyright (C) 1995, 1996 Leigh Brookshaw,
 *  under GNU License. <p>
 *
 *  !! reduce marging in order to improve the spave for the graph. <p>
 *
 *  !! find a more modern and useful graph viewer
 *
 *@author     Massimo Zaniboni
 *@created    October 12, 2001
 *@version    $version$
 */
public class GraphViewer extends BasicFileViewer {

    /**
     *  Description of the Field
     */
    public static String editorDescription = "Display a Graph.";

    /**
     *  Description of the Field
     */
    public static JudeObject editorCategory = Editor.SWING_EDITOR;

    /**
     *  Description of the Field
     */
    public static Class javaContentClass = JudeObject.class;

    /**
     *  Description of the Field
     */
    public static JudeObject editorType = VIEWER;

    /**
     *  Description of the Field
     */
    public static JudeObject contentLogicalType = DB.id("xy_chart");

    /**
     *  Description of the Field
     */
    protected JPanel mainPanel = new JPanel();

    /**
     *  Description of the Field
     */
    protected LoadData dynamic;

    /**
     *  Description of the Field
     */
    protected G2Dint graph;

    /**
     *  Description of the Field
     */
    protected Label title;

    /**
     *  Description of the Field
     */
    protected DataSet data1;

    /**
     *  Description of the Field
     */
    protected Axis xaxis;

    /**
     *  Description of the Field
     */
    protected Axis yaxis;

    /**
     *  Description of the Field
     */
    protected SwingEditorWrapper wrapper = null;

    /**
     *  Description of the Field
     */
    protected Collection mainMenu = null;

    /**
     *  Constructor for the GraphEditor object
     */
    public GraphViewer() {
        wrapper = new SwingEditorWrapper(this, mainPanel);
    }

    /**
     *  Gets the component attribute of the GraphEditor object
     *
     *@return    The component value
     */
    public SwingEditorWrapper getComponent() {
        return wrapper;
    }

    /**
     *  Description of the Method
     */
    protected void updateView() {
        try {
            int i;
            int j;
            String st = "!! Title !!";
            String data = "!! DATA NAME !!";
            graph = new G2Dint();
            dynamic = new LoadData();
            graph.borderTop = 30;
            graph.borderBottom = 10;
            graph.borderRight = 40;
            graph.setDataBackground(new Color(50, 50, 200));
            graph.setGraphBackground(new Color(0, 200, 255));
            try {
                data1 = dynamic.loadDataSet(loadFileFromServer().toURL(), graph);
            } catch (Exception e) {
                DB.getLogger().addErrorMessage("Failed to load data file:" + e.getMessage());
            }
            data1.linecolor = new Color(255, 255, 0);
            DB.getLogger().addDebugMessage("1");
            xaxis = graph.createXAxis();
            xaxis.attachDataSet(data1);
            DB.getLogger().addDebugMessage("2");
            yaxis = graph.createYAxis();
            yaxis.attachDataSet(data1);
            yaxis.setTitleColor(Color.magenta);
            mainPanel.removeAll();
            mainPanel.setLayout(new BorderLayout());
            mainPanel.add(BorderLayout.CENTER, graph);
        } catch (Exception ex) {
            DB.getLogger().addErrorMessage(ex);
        }
    }
}
