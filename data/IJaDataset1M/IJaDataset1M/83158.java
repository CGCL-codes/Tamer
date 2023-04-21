package edu.gsbme.geometrykernel.data.dim3;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.vecmath.Point3d;
import ncsa.hdf.object.Dataset;
import ncsa.hdf.object.HObject;
import org.w3c.dom.Element;
import edu.gsbme.MMLParser2.Error.NoObjectFoundInSearch;
import edu.gsbme.MMLParser2.Error.PathError;
import edu.gsbme.MMLParser2.FML.Search.FMLSearch.FMLSource;
import edu.gsbme.MMLParser2.FML.VirtualTree.VirtualFMLTree;
import edu.gsbme.MMLParser2.FML.VirtualTree.cTreeNodes;
import edu.gsbme.MMLParser2.Factory.FMLFactory;
import edu.gsbme.geometrykernel.data.Idata;
import edu.gsbme.geometrykernel.data.STORAGE_TYPE;
import edu.gsbme.geometrykernel.data.dim0.Dim0Object;
import edu.gsbme.hdf5.parser.handlers.CellHandler;

/**
 * Generic 3 dimensional abstract geometric class
 * @author David
 *
 */
public abstract class Dim3Object extends Idata {

    public Dim3Object(String id) {
        super(id);
    }

    public abstract Point3d evaluate(double s, double t, double u);

    /**
	 * Default getCoordinate function returns double[][] control point array
	 * This may be overriden if implementing class has a different method of extraction or topology
	 */
    public Object getCoordinates() {
        if (getStorageTye() == STORAGE_TYPE.DIRECT) {
            Dim0Object[] array = getVertexArray();
            double[][] result = new double[array.length][3];
            for (int i = 0; i < array.length; i++) {
                result[i] = (double[]) array[i].getCoordinates();
            }
            return result;
        } else {
            try {
                FMLFactory factory = this.getReferenceFactory();
                VirtualFMLTree tree = factory.getVirtualTree();
                cTreeNodes node = tree.searchCellTree(getReferencePath());
                if (node.getSource() == FMLSource.XML) {
                    Element cell = (Element) node.getReferneceObject();
                    return factory.getDataAPIWorker().returnXMLGenericCellCoordinate(cell);
                } else {
                    HObject h5object = (HObject) node.getReferneceObject();
                    if (h5object instanceof Dataset) {
                        return getCoord(factory, node, (Dataset) h5object);
                    } else {
                        DefaultMutableTreeNode grp_node = node.h5parser.getTreeNode(h5object);
                        Dataset primary = (Dataset) CellHandler.getPrimaryDataset(grp_node).getUserObject();
                        return getCoord(factory, node, (Dataset) primary);
                    }
                }
            } catch (PathError e) {
                e.printStackTrace();
            } catch (NoObjectFoundInSearch e) {
                e.printStackTrace();
            }
        }
        return new double[0][0];
    }

    private double[][] getCoord(FMLFactory factory, cTreeNodes node, Dataset ds) {
        if (node.hdf5_index != -1) {
            int[] ref = node.h5parser.getMeshHandler().returnHDF5RefenreceDSEntry(node.hdf5_index, ds);
            double[][] result = new double[ref.length][3];
            for (int i = 0; i < ref.length; i++) {
                result[i] = factory.getDataAPIWorker().returnGlobalPointCoordinate(ref[i]);
            }
            return result;
        }
        if (node.index != -1) {
            int[] ref = node.h5parser.getMeshHandler().returnHDF5RefenreceDSEntry(node.index, ds);
            double[][] result = new double[ref.length][3];
            for (int i = 0; i < ref.length; i++) {
                result[i] = factory.getDataAPIWorker().returnGlobalPointCoordinate(ref[i]);
            }
            return result;
        } else {
            return node.h5parser.getCellHandler().convertDStoDoubleDim2Array(ds);
        }
    }
}
