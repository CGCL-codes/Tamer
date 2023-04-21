package monitor.gui;

import java.util.HashMap;
import java.util.Map;
import monitor.edu.berkeley.guir.prefuse.graph.DefaultEdge;
import monitor.edu.berkeley.guir.prefuse.graph.Node;

/**
 * �l szerkezet�nek b�v�t�se, hogy t�mogassa az IPv6 multicast forgalmak
 * adatainak t�rol�s�t r�tegek szerint rendezve 
 * @author gyuf
 *
 */
public class L3Edge extends DefaultEdge {

    private Map<Integer, Map> m_attlayer;

    public L3Edge(Node n1, Node n2) {
        super(n1, n2);
        m_attlayer = new HashMap();
    }

    public void addLayer(int layerID, String layername) {
        if ((layerID > -1) && (layerID < MainFrame.numberOfLayers)) {
            m_attlayer.put(layerID, new HashMap());
        }
    }

    public boolean hasLayer(int layerID) {
        if (m_attlayer.get(layerID) != null) {
            return true;
        }
        return false;
    }

    public void setLayerAttribute(int layerID, String name, String value) {
        Object o = m_attlayer.get(layerID);
        if (null != o) {
            HashMap map = (HashMap) o;
            map.put(name, value);
        }
    }

    public String getLayerAttribute(int layerID, String name) {
        Object o = m_attlayer.get(layerID);
        if (null != o) {
            HashMap map = (HashMap) o;
            return (String) map.get(name);
        } else return null;
    }
}
