package PowersetViewer;

import java.util.ArrayList;
import java.util.Collections;
import AccordionPowersetDrawer.PropertyData;
import AccordionPowersetDrawer.SetNode;

/**
 * Class used to calculate simple statistical properties of a SetNode
 * 
 * @author jordanel
 *
 */
public class NodeCalculation {

    public static final int MAX = 0;

    public static final int MIN = 1;

    public static final int MEAN = 2;

    public static final int MEDIAN = 3;

    public static final int SUM = 4;

    /**
	 * Max
	 * 
	 * @param node
	 * @param propName
	 * @param propData
	 * @return
	 *
	 * @author jordanel
	 */
    public static float max(SetNode node, String propName, PropertyData propData) {
        ArrayList propValues = getPropValues(node, propName, propData);
        return ((Float) propValues.get(propValues.size() - 1)).floatValue();
    }

    /**
	 * Min
	 * 
	 * @param node
	 * @param propName
	 * @param propData
	 * @return
	 *
	 * @author jordanel
	 */
    public static float min(SetNode node, String propName, PropertyData propData) {
        ArrayList propValues = getPropValues(node, propName, propData);
        return ((Float) propValues.get(0)).floatValue();
    }

    /**
	 * Mean
	 * 
	 * @param node
	 * @param propName
	 * @param propData
	 * @return
	 *
	 * @author jordanel
	 */
    public static float mean(SetNode node, String propName, PropertyData propData) {
        ArrayList propValues = getPropValues(node, propName, propData);
        float total = 0;
        for (int i = 0; i < propValues.size(); i++) {
            total += ((Float) propValues.get(i)).floatValue();
        }
        return total / propValues.size();
    }

    /**
	 * Median 
	 * 
	 * @param node
	 * @param propName
	 * @param propData
	 * @return
	 *
	 * @author jordanel
	 */
    public static float median(SetNode node, String propName, PropertyData propData) {
        ArrayList propValues = getPropValues(node, propName, propData);
        if ((propValues.size() % 2) == 0) {
            return (((Float) propValues.get((propValues.size() / 2) - 1)).floatValue() + ((Float) propValues.get((propValues.size() / 2))).floatValue()) / 2.0f;
        } else {
            return ((Float) propValues.get((propValues.size() - 1) / 2)).floatValue();
        }
    }

    /**
	 * Sum
	 * 
	 * @param node
	 * @param propName
	 * @param propData
	 * @return
	 *
	 * @author jordanel
	 */
    public static float sum(SetNode node, String propName, PropertyData propData) {
        ArrayList propValues = getPropValues(node, propName, propData);
        float total2 = 0;
        for (int i = 0; i < propValues.size(); i++) {
            total2 += ((Float) propValues.get(i)).floatValue();
        }
        return total2;
    }

    /**
	 * Returns the property values of all the property keys of a SetNode.
	 * 
	 * @param node
	 * @param propName
	 * @param propData
	 * @return
	 *
	 * @author jordanel
	 */
    private static ArrayList getPropValues(SetNode node, String propName, PropertyData propData) {
        ArrayList itemList = node.getSetArray();
        ArrayList propValues = new ArrayList();
        for (int i = 0; i < itemList.size(); i++) {
            propValues.add((new Float(propData.getValue(((Integer) itemList.get(i)).intValue(), propData.getAttributeKey(propName)))));
        }
        Collections.sort(propValues);
        return propValues;
    }
}
