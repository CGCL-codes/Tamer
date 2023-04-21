package ist.ac.simulador.application;

import ist.ac.simulador.gef.Module;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.InputEvent;
import java.io.IOException;
import javax.swing.JComponent;
import javax.swing.TransferHandler;
import java.awt.datatransfer.Transferable;
import ist.ac.simulador.gef.SimulatorGraph;
import ist.ac.simulador.gef.GModule;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;

/**
 *
 * @author Nuno Afonso <nafonso@gmail.com>
 */
public class ModuleTransferHandler extends TransferHandler {

    private static String MYME_TYPE = DataFlavor.javaJVMLocalObjectMimeType + ";class=ist.ac.simulador.application.NodeElement";

    private DataFlavor _dataFlavor = null;

    public ModuleTransferHandler() {
        super();
        System.out.println("ModuleTransferHandler Constructor");
        try {
            _dataFlavor = new DataFlavor(MYME_TYPE);
        } catch (ClassNotFoundException e) {
            System.err.println("Error with DataFlavor class. Shouldn't get here, probably a bad jar?");
        }
    }

    public boolean importData(JComponent c, Transferable t) {
        System.out.println("ModuleTransferHandler.importData()");
        System.out.println(c.getClass().getName());
        if (!(c instanceof SimulatorGraph)) return false;
        SimulatorGraph simGraph = (SimulatorGraph) c;
        try {
            Module module = new Module();
            module.initialize((NodeElement) t.getTransferData(_dataFlavor));
            simGraph.add(module);
        } catch (UnsupportedFlavorException e) {
            System.err.println("1");
        } catch (IOException e) {
            System.err.println("2");
        }
        return true;
    }

    public boolean canImport(JComponent c, DataFlavor[] flavors) {
        System.out.println("ModuleTransferHandler.canImport()");
        if (c instanceof SimulatorGraph) {
            System.out.println("� um simgraph");
            for (int i = 0; i != flavors.length; ++i) if (flavors[i].equals(_dataFlavor)) return true;
        } else System.out.println("n�o � um sim graph");
        System.out.println(c.getClass().getName());
        return false;
    }

    protected Transferable createTransferable(JComponent c) {
        System.out.println("ModuleTransferHandler.createTransferable()");
        XTree moduleTree = (XTree) c;
        return new ModuleTransferable(moduleTree.getSelectedNode());
    }

    public int getSourceActions(JComponent c) {
        System.out.println("ModuleTransferHandler.getSourceActions()");
        return COPY_OR_MOVE;
    }

    public class ModuleTransferable implements Transferable {

        private NodeElement _moduleInfo = null;

        public ModuleTransferable(NodeElement moduleInfo) {
            System.out.println("ModuleTransferable Constructor");
            _moduleInfo = moduleInfo;
        }

        public DataFlavor[] getTransferDataFlavors() {
            System.out.println("ModuleTransferable.getTransferDataFlavors()");
            DataFlavor dataFlavor = null;
            try {
                dataFlavor = new DataFlavor(MYME_TYPE);
            } catch (ClassNotFoundException e) {
                System.err.println("Error with DataFlavor class. Shouldn't get here, probably a bad jar?");
            }
            DataFlavor dataFlavors[] = { dataFlavor };
            return dataFlavors;
        }

        public boolean isDataFlavorSupported(DataFlavor flavor) {
            System.out.println("ModuleTransferable.isDataFlavorSupported()");
            boolean flavorSupported = false;
            try {
                flavorSupported = flavor.equals(new DataFlavor(MYME_TYPE));
            } catch (ClassNotFoundException e) {
                System.err.println("Error with DataFlavor class. Shouldn't get here, probably a bad jar?");
            }
            return flavorSupported;
        }

        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
            System.out.println("ModuleTransferable.getTransferData()");
            return _moduleInfo;
        }
    }
}
