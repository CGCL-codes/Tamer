package kr.ac.ssu.imc.whitehole.report.designer.items;

import org.w3c.dom.*;
import org.xml.sax.*;
import java.util.Vector;
import java.awt.*;
import kr.ac.ssu.imc.whitehole.report.designer.*;
import kr.ac.ssu.imc.whitehole.report.designer.rdxml.RDXml;

public class RDRegion extends RDVirtualObject {

    Point rPoint;

    Dimension rSize;

    private Vector rObjects;

    private static int Count = 0;

    private int rIdent;

    private String OwnQuery;

    private String MasterQuery;

    private String RelationQuery1;

    private String RelationQuery2;

    private String Name;

    private boolean bCreate = true;

    public RDRegion() {
        super(0);
    }

    public RDRegion(int nId, Point tStartLoc, Dimension tSize, ReportDesigner tGlobalData, Vector Objects) {
        super(nId);
        rPoint = tStartLoc;
        rSize = tSize;
        rIdent = ++Count;
        RelationQuery1 = tGlobalData.GetOwnField();
        RelationQuery2 = tGlobalData.GetMasterField();
        OwnQuery = tGlobalData.GetOwnQuery();
        MasterQuery = tGlobalData.GetMasterQuery();
        Name = tGlobalData.GetRegionName();
        rObjects = new Vector();
        for (int i = 0; i < Objects.size(); i++) {
            RDObject tList = ((RDObject) Objects.get(i));
            Point pcal = tList.getObjectLocation();
            Dimension dcal = tList.getObjectSize();
            if (rPoint.getX() <= pcal.getX() && rPoint.getY() <= pcal.getY()) {
                if (rPoint.getX() + rSize.getWidth() >= pcal.getX() + dcal.getWidth() && rPoint.getY() + rSize.getHeight() >= pcal.getY() + dcal.getHeight()) {
                    if (tList.RegionId == 0) rObjects.add(tList);
                }
            }
        }
        if (rObjects.size() == 0) bCreate = false;
        for (int i = 0; i < rObjects.size(); i++) {
            Vector a = ((RDObject) rObjects.get(i)).getQuerys();
            if (a.size() > 1) bCreate = false;
            if (a.size() == 1 && !((String) a.get(0)).equals(OwnQuery)) bCreate = false;
        }
        if (bCreate) {
            for (int i = 0; i < rObjects.size(); i++) {
                ((RDObject) rObjects.get(i)).RegionId = rIdent;
                ((RDObject) rObjects.get(i)).oSelectView.setStyle();
            }
        } else {
            Count--;
        }
    }

    public boolean isCreate() {
        return bCreate;
    }

    public void Free() {
        for (int i = 0; i < rObjects.size(); i++) {
            ((RDObject) rObjects.get(i)).RegionId = 0;
            ((RDObject) rObjects.get(i)).oSelectView.setStyle();
        }
    }

    public void View(Report tGDDoc) {
        tGDDoc.getCurrentPage().unselectObjects();
        for (int i = 0; i < rObjects.size(); i++) {
            tGDDoc.getCurrentPage().selectObject((RDObject) rObjects.get(i), false);
        }
    }

    public String getName() {
        return Name;
    }

    public void setData(Vector Datas) {
        OwnQuery = (String) Datas.get(0);
        MasterQuery = (String) Datas.get(1);
        RelationQuery1 = (String) Datas.get(2);
        RelationQuery2 = (String) Datas.get(3);
    }

    public Vector getData() {
        Vector Datas = new Vector(4);
        Datas.add(OwnQuery);
        Datas.add(MasterQuery);
        Datas.add(RelationQuery1);
        Datas.add(RelationQuery2);
        return Datas;
    }

    public Element createElementNode(Document tDocument) {
        org.w3c.dom.Element tElement = tDocument.createElement("rdRegion");
        tElement.setAttribute("OwnQuery", OwnQuery);
        tElement.setAttribute("MasterQuery", MasterQuery);
        tElement.setAttribute("RelationQuery1", RelationQuery1);
        tElement.setAttribute("RelationQuery2", RelationQuery2);
        tElement.setAttribute("Name", Name);
        tElement.setAttribute("RegionId", Integer.toString(rIdent));
        return tElement;
    }

    public static RDRegion createRDRegion(org.w3c.dom.Element tElement) {
        RDRegion tRegion = new RDRegion();
        org.w3c.dom.NodeList oRegionList = ((org.w3c.dom.Node) tElement).getChildNodes();
        org.w3c.dom.Node oRegionNode = null;
        for (int i = 0; i < oRegionList.getLength(); i++) {
            if (oRegionList.item(i).getNodeName().equals("rdRegion")) {
                oRegionNode = oRegionList.item(i);
                tRegion.OwnQuery = ((org.w3c.dom.Element) oRegionNode).getAttribute("OwnQuery");
                tRegion.MasterQuery = ((org.w3c.dom.Element) oRegionNode).getAttribute("MasterQuery");
                tRegion.RelationQuery1 = ((org.w3c.dom.Element) oRegionNode).getAttribute("RelationQuery1");
                tRegion.RelationQuery2 = ((org.w3c.dom.Element) oRegionNode).getAttribute("RelationQuery2");
                tRegion.Name = ((org.w3c.dom.Element) oRegionNode).getAttribute("Name");
                tRegion.rIdent = Integer.parseInt(((org.w3c.dom.Element) oRegionNode).getAttribute("RegionId"));
            }
        }
        return tRegion;
    }

    public void SelectObject(Vector Objects) {
        rObjects = new Vector();
        for (int i = 0; i < Objects.size(); i++) {
            if (((RDObject) Objects.get(i)).RegionId == rIdent) {
                rObjects.add(Objects.get(i));
                ((RDObject) Objects.get(i)).oSelectView.setStyle();
            }
        }
    }

    /** �Ķ���ͷ� �Ѿ�� ���� ��ü�� ������ΰ� �ƴѰ� �����. */
    public boolean usedQueryObject(String queryObject) {
        if (this.OwnQuery.equals(queryObject)) return true; else return false;
    }
}
