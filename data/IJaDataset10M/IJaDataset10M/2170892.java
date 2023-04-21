package de.fhg.igd.earth.model.input;

import java.awt.Color;
import java.awt.Frame;
import java.util.Hashtable;
import de.fhg.igd.earth.model.graph.BranchGroup;
import de.fhg.igd.earth.model.graph.Group;
import de.fhg.igd.earth.model.graph.HistoryGroup;
import de.fhg.igd.earth.model.graph.ModelGraph;
import de.fhg.igd.earth.model.graph.Node;
import de.fhg.igd.earth.model.graph.TransformGroup;

/**
 * This class is for loading an agent into the model.
 * You have to set a name, a position, a color and a transparency.
 *
 * Title        : Earth
 * Copyright    : Copyright (c) 2001
 * Organisation : IGD FhG
 * @author       : Werner Beutel
 * @version      : 1.0
 */
public class AgentLoader extends ModelLoader {

    /**
     * parent frame
     */
    private Frame parent_;

    /**
     * implicit name of this agent
     */
    private String implicitName_;

    /**
     * nickname of this agent
     */
    private String nickname_;

    /**
     * default agent icon
     */
    public static final String ICON_AGENTS = "icons/tree_agent_group.gif";

    /*************************************************************************
     * Creates an instance of this class.
     ************************************************************************/
    public AgentLoader() {
    }

    /*************************************************************************
     * Returns the identName for this class (AgentLoader).
     * @return "AgentLoader"
     ************************************************************************/
    public String getIdentName() {
        return "AgentLoader";
    }

    /*************************************************************************
     * Loads an agent into the given model (default pos, color, transparency).
     * @param model Model
     * @param name Name of the agent
     * @return <code>true</code> on success
     ************************************************************************/
    public HistoryGroup load(ModelGraph model, String name, String implicitName, Frame parent) {
        return load(model, name, implicitName, Color.red, 0.0f, 5.0, 50.0, 0.0, parent);
    }

    public boolean load(ModelGraph model, Frame parent) {
        return false;
    }

    /*************************************************************************
     * Loads an agent into the given model.
     * If the given name is null, a GUI is opened to request the name.
     * @param model Model
     * @param name Name of the agent
     * @param color Color
     * @param transparency Transparency
     * @param translationX,Y,Z Translation values
     * @return <code>true</code> on success
     ************************************************************************/
    public HistoryGroup load(ModelGraph model, String name, String implicitName, Color color, float transparency, double translationX, double translationY, double translationZ, Frame parent) {
        Hashtable infoTable;
        BranchGroup bg;
        Node node;
        parent_ = parent;
        nickname_ = name;
        implicitName_ = implicitName;
        if (nickname_ == null) {
            nickname_ = "";
            if (showDialog() == false) return null;
        }
        node = model.searchDown("World", "Agents");
        if (node == null) {
            bg = new BranchGroup();
            bg.setName("Agents");
            bg.setIcon(datapath_ + ICON_AGENTS);
            model.addChild(bg);
        } else {
            bg = (BranchGroup) node;
        }
        HistoryGroup hg = new HistoryGroup(20, 30, 5);
        hg.setDatapath(datapath_);
        hg.setName(nickname_);
        hg.setPickable(true);
        hg.setTrace(true);
        bg.addChild(hg);
        Group g = new Group();
        g.setName("NOW");
        g.setPickable(true);
        hg.addChild(g);
        TransformGroup tg = new TransformGroup();
        tg.setName("Position");
        g.addChild(tg);
        tg.translate(translationX, translationY, translationZ);
        de.fhg.igd.earth.model.graph.Polygon pol = new de.fhg.igd.earth.model.graph.Polygon();
        pol.setStyle(de.fhg.igd.earth.model.graph.Polygon.STYLE_WIRE_FRAME);
        pol.setColor(Color.white);
        pol.addPoint(0, 0, 0);
        pol.addPoint(0, 0, 1);
        pol.setName("Body");
        tg.addChild(pol);
        pol = new de.fhg.igd.earth.model.graph.Polygon();
        pol.setStyle(de.fhg.igd.earth.model.graph.Polygon.STYLE_SOLID);
        pol.setTransparency(transparency);
        pol.setColor(color);
        double headsize = 0.1;
        pol.addPoint(-headsize, -headsize, 1, 0, 0, 1);
        pol.addPoint(headsize, -headsize, 1, 0, 0, 1);
        pol.addPoint(0, headsize, 1, 0, 0, 1);
        pol.setName("Head");
        tg.addChild(pol);
        infoTable = hg.getInfoTable();
        infoTable.put("ImplicitName", implicitName_);
        infoTable.put("Distance", "0.0");
        hg.setInfoTable(infoTable);
        return hg;
    }

    /*************************************************************************
     * Shows the dialog box to input the "Nickname" and "Implicit Name" of
     * the agent.
     * @return true on OK, false on CANCEL
     ************************************************************************/
    private boolean showDialog() {
        AgentLoaderDialog ad;
        ad = new AgentLoaderDialog(parent_, "AgentLoader", true, nickname_, implicitName_);
        ad.show();
        if (ad.getResult() == false) return false;
        nickname_ = ad.getNickname();
        implicitName_ = ad.getImplicitName();
        return true;
    }
}
