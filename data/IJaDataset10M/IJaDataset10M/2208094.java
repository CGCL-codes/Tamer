package it.eng.bxmodeller.graphic;

import it.eng.bxmodeller.Activity;
import it.eng.bxmodeller.Application;
import it.eng.bxmodeller.Artifact;
import it.eng.bxmodeller.Assignment;
import it.eng.bxmodeller.ExtendedAttribute;
import it.eng.bxmodeller.Package;
import it.eng.bxmodeller.Participant;
import it.eng.bxmodeller.X_Object;
import it.eng.bxmodeller.dataFields.DataField;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jdom.Attribute;
import org.jdom.Element;

public class Lane {

    private String id = null, name = null;

    private X_Object object = null;

    private List nodeGraphicsInfos = null;

    private String parentPool = null;

    private String parentLane = null;

    private String prefix = "LNE_";

    public void generateClass(Element elementoAppoggio) {
        if (elementoAppoggio.getAttributeValue("Id") != null) {
            this.setId(elementoAppoggio.getAttributeValue("Id"));
        }
        if (elementoAppoggio.getAttributeValue("Name") != null) {
            this.setName(elementoAppoggio.getAttributeValue("Name"));
        }
        if (elementoAppoggio.getAttributeValue("ParentLane") != null) {
            this.setParentLane(elementoAppoggio.getAttributeValue("ParentLane"));
        }
        if (elementoAppoggio.getAttributeValue("ParentPool") != null) {
            this.setParentPool(elementoAppoggio.getAttributeValue("ParentPool"));
        }
        List elencoFigli = elementoAppoggio.getChildren();
        Iterator iteratore = elencoFigli.iterator();
        Element elementoAppoggioFigli = null;
        String nomeTagAppoggio = "";
        while (iteratore.hasNext()) {
            elementoAppoggioFigli = (Element) iteratore.next();
            nomeTagAppoggio = elementoAppoggioFigli.getName();
            if (nomeTagAppoggio.equals("Object")) {
                X_Object tmp = new X_Object();
                tmp.generateClass(elementoAppoggioFigli);
                this.setObject(tmp);
            } else if (nomeTagAppoggio.equals("NodeGraphicsInfos")) {
                List tmpList = new ArrayList();
                List elenco = elementoAppoggioFigli.getChildren();
                Iterator iteratoreTmp = elenco.iterator();
                Element elementoAppoggioTmp = null;
                String nomeTagTmp = "";
                while (iteratoreTmp.hasNext()) {
                    elementoAppoggioTmp = (Element) iteratoreTmp.next();
                    nomeTagTmp = elementoAppoggioTmp.getName();
                    NodeGraphicsInfo tmp = new NodeGraphicsInfo();
                    tmp.generateClass(elementoAppoggio, elementoAppoggioTmp);
                    tmpList.add(tmp);
                }
                this.setNodeGraphicsInfos(tmpList);
            }
        }
    }

    public void setHeight(double newHeight) {
        ((NodeGraphicsInfo) this.nodeGraphicsInfos.get(0)).setHeight(newHeight);
    }

    public String getParentLane() {
        return this.parentLane;
    }

    public void setParentLane(String parentLane) {
        this.parentLane = parentLane;
    }

    public String getParentPool() {
        return this.parentPool;
    }

    public void setParentPool(String parentPool) {
        this.parentPool = parentPool;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List getNodeGraphicsInfos() {
        return nodeGraphicsInfos;
    }

    public void setNodeGraphicsInfos(List nodeGraphicsInfos) {
        this.nodeGraphicsInfos = nodeGraphicsInfos;
    }

    public X_Object getObject() {
        return object;
    }

    public void setObject(X_Object object) {
        this.object = object;
    }

    public Element generateXPDL() {
        Element item = new Element("Lane");
        Attribute x_id = new Attribute("Id", this.id);
        item.setAttribute(x_id);
        if (this.name != null) {
            Attribute x_name = new Attribute("Name", this.name);
            item.setAttribute(x_name);
        }
        Attribute x_parentPool = new Attribute("ParentPool", this.parentPool);
        item.setAttribute(x_parentPool);
        if (this.parentLane != null) {
            Attribute x_parentLane = new Attribute("ParentLane", this.parentLane);
            item.setAttribute(x_parentLane);
        }
        if (this.object != null) item.addContent(object.generateXPDL());
        if (this.nodeGraphicsInfos != null) {
            if (this.nodeGraphicsInfos.size() > 0) {
                Element x_nodeGraphicsInfos = new Element("NodeGraphicsInfos");
                for (int i = 0; i < this.nodeGraphicsInfos.size(); i++) {
                    x_nodeGraphicsInfos.addContent(((NodeGraphicsInfo) this.nodeGraphicsInfos.get(i)).generateXPDL());
                }
                item.addContent(x_nodeGraphicsInfos);
            }
        }
        return item;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public double getX() {
        return ((NodeGraphicsInfo) this.nodeGraphicsInfos.get(0)).getCoordinates().getXCoordinate();
    }

    public double getY() {
        return ((NodeGraphicsInfo) this.nodeGraphicsInfos.get(0)).getCoordinates().getYCoordinate();
    }

    public void draw(Graphics2D g2d, Package mainPackage, String wfId, Pool parentPool, String imgPath) {
        int x = (int) ((NodeGraphicsInfo) this.nodeGraphicsInfos.get(0)).getCoordinates().getXCoordinate();
        int y = (int) ((NodeGraphicsInfo) this.nodeGraphicsInfos.get(0)).getCoordinates().getYCoordinate();
        int width = (int) ((NodeGraphicsInfo) this.nodeGraphicsInfos.get(0)).getWidth();
        int height = (int) ((NodeGraphicsInfo) this.nodeGraphicsInfos.get(0)).getHeight();
        x += (int) ((NodeGraphicsInfo) parentPool.nodeGraphicsInfos.get(0)).getCoordinates().getXCoordinate();
        y += (int) ((NodeGraphicsInfo) parentPool.nodeGraphicsInfos.get(0)).getCoordinates().getYCoordinate() + 1;
        g2d.setColor(Color.black);
        g2d.drawRect(x, y, width, height);
        Font font = new Font("Arial", Font.PLAIN, 9);
        g2d.setFont(font);
        FontMetrics fontMetrics = g2d.getFontMetrics();
        int charWidth = 0;
        int nameWidth = 0;
        if (this.name != null && !this.name.equals("")) {
            for (int i = 0; i < name.length(); i++) {
                charWidth = fontMetrics.charWidth(name.charAt(i));
                nameWidth += charWidth;
            }
            g2d.translate(x + fontMetrics.getHeight(), height / 2 + y + nameWidth / 2);
            g2d.rotate(-Math.PI / 2.0);
            g2d.drawString(this.name, 0, 0);
            g2d.rotate(Math.PI / 2.0);
            g2d.translate(-(x + fontMetrics.getHeight()), -(height / 2 + y + nameWidth / 2));
        }
        List laneArtifacts = mainPackage.getLaneArtifacts(this, this.parentPool, wfId);
        for (int i = 0; i < laneArtifacts.size(); i++) {
            ((Artifact) laneArtifacts.get(i)).draw(g2d, mainPackage, x, y, imgPath);
        }
        List laneActivities = mainPackage.getWorkflowProcessById(wfId).getLaneActivities(this, this.parentPool);
        for (int i = 0; i < laneActivities.size(); i++) ((Activity) laneActivities.get(i)).draw(g2d, mainPackage, x, y, imgPath);
    }
}
