package deduced.viewer;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import deduced.PropertyCollection;
import deduced.xml.NameReferenceFinder;

/**
 * @author DDuff
 */
public class PropertyCollectionTransferable implements Transferable {

    private List _data = new ArrayList();

    public PropertyCollectionTransferable(List data) {
        _data.addAll(data);
    }

    /**
     * (non-Javadoc)
     * 
     * @see java.awt.datatransfer.Transferable#getTransferDataFlavors()
     */
    public DataFlavor[] getTransferDataFlavors() {
        DataFlavor[] retVal = new DataFlavor[2];
        retVal[1] = DeducedDataFlavor.DEDUCED_NAME_REFERENCE_DATA_FLAVOR;
        retVal[0] = DataFlavor.stringFlavor;
        return retVal;
    }

    /**
     * (non-Javadoc)
     * 
     * @see java.awt.datatransfer.Transferable#isDataFlavorSupported(java.awt.datatransfer.DataFlavor)
     */
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        if (flavor.equals(DataFlavor.stringFlavor)) {
            return true;
        }
        if (flavor.equals(DeducedDataFlavor.DEDUCED_NAME_REFERENCE_DATA_FLAVOR)) {
            return true;
        }
        return false;
    }

    /**
     * (non-Javadoc)
     * 
     * @see java.awt.datatransfer.Transferable#getTransferData(java.awt.datatransfer.DataFlavor)
     */
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        Object retVal = null;
        if (flavor.equals(DataFlavor.stringFlavor)) {
            retVal = getNameStringValue();
        } else if (flavor.equals(DeducedDataFlavor.DEDUCED_NAME_REFERENCE_DATA_FLAVOR)) {
            retVal = getNameStringValue();
        }
        return retVal;
    }

    /**
     * @return
     */
    private String getNameStringValue() {
        List stringList = new ArrayList(_data.size());
        Iterator i = _data.iterator();
        while (i.hasNext()) {
            PropertyCollection element = (PropertyCollection) i.next();
            stringList.add(NameReferenceFinder.getCollectionName(element));
        }
        return formatStringList(stringList);
    }

    public static String formatStringList(List stringList) {
        StringBuffer retVal = new StringBuffer();
        boolean isFirst = true;
        Iterator i = stringList.iterator();
        while (i.hasNext()) {
            String element = (String) i.next();
            if (isFirst) {
                isFirst = false;
            } else {
                retVal.append('\n');
            }
            retVal.append(element);
        }
        return retVal.toString();
    }

    public static List breakStringToList(String stringToBreak) {
        String[] split = stringToBreak.split("\n");
        return Arrays.asList(split);
    }

    public static List findCollectionsByName(List collectionNameList, PropertyCollection source) {
        List retVal = new ArrayList(collectionNameList.size());
        Iterator it = collectionNameList.iterator();
        while (it.hasNext()) {
            String name = (String) it.next();
            PropertyCollection collection = NameReferenceFinder.findCollection(source, name);
            retVal.add(collection);
        }
        return retVal;
    }

    public static List findFromTransferable(Transferable dataToCopy, PropertyCollection source) throws UnsupportedFlavorException, IOException {
        List retVal = null;
        String transferData = (String) dataToCopy.getTransferData(DeducedDataFlavor.DEDUCED_NAME_REFERENCE_DATA_FLAVOR);
        List collectionNameList = breakStringToList(transferData);
        retVal = findCollectionsByName(collectionNameList, source);
        return retVal;
    }
}
