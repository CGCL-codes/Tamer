package antsnest.datatransfer;

import java.awt.datatransfer.*;
import antsnest.antinfo.*;

/**
 * Defines how an Ant element definition is transfered
 * @author Chris Clohosy
 */
public class AntElementDefinitionTransferable extends AntsNestTransferable {

    /**
	 * The Ant element definition to transfer
	 */
    private Definition definition;

    /**
	 * Constructs the new transferable
	 * @param definition the definition to transfer
	 */
    public AntElementDefinitionTransferable(Definition definition) {
        super();
        try {
            this.definition = definition;
            flavor = new DataFlavor("application/x-java-serialized-object; class=antsnest.antinfo.Definition");
            flavors = new DataFlavor[1];
            flavors[0] = flavor;
            flavorList.add(flavor);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
	 * Gets an object to be used in the transfer
	 * @return an Object, the object to transfer
	 * @param usedFlavor the flavor being used
	 * @throws UnsupportedFlavorException if an unsupported flavor is used
	 */
    public synchronized Object getTransferData(DataFlavor usedFlavor) throws UnsupportedFlavorException {
        if (isDataFlavorSupported(usedFlavor)) {
            return definition;
        } else {
            throw new UnsupportedFlavorException(usedFlavor);
        }
    }

    /**
	 * Gets the data flavor used in this transferable
	 * @return a DataFlavor the data flavor used
	 */
    public static DataFlavor getFlavor() {
        try {
            return new DataFlavor("application/x-java-serialized-object; class=antsnest.antinfo.Definition");
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
}
