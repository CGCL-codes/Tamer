package edu.gsbme.geometrykernel.data.mesh;

import java.util.Enumeration;
import javax.swing.tree.DefaultMutableTreeNode;
import ncsa.hdf.object.Dataset;
import ncsa.hdf.object.Group;
import ncsa.hdf.object.HObject;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import edu.gsbme.MMLParser2.Error.NoObjectFoundInSearch;
import edu.gsbme.MMLParser2.Error.PathError;
import edu.gsbme.MMLParser2.FML.Search.FMLSearch.FMLSource;
import edu.gsbme.MMLParser2.FML.VirtualTree.VirtualFMLTree;
import edu.gsbme.MMLParser2.FML.VirtualTree.cTreeNodes;
import edu.gsbme.MMLParser2.Factory.FMLFactory;
import edu.gsbme.MMLParser2.Vocabulary.Attributes;
import edu.gsbme.MMLParser2.Vocabulary.FML;
import edu.gsbme.MMLParser2.XPath.XPathScanner;
import edu.gsbme.geometrykernel.kobject;
import edu.gsbme.geometrykernel.data.Idata;
import edu.gsbme.geometrykernel.data.STORAGE_TYPE;
import edu.gsbme.geometrykernel.data.dim0.ptIndex;
import edu.gsbme.hdf5.parser.DefaultDatasetNames;
import edu.gsbme.hdf5.parser.hdf5Parser;

/**
 * STORAGE METHODS
 *  *** DIRECT ***
 * The mesh data by topologicall dimensions are stored directly in this object, these includes
 * the Cell Element or Vertex Rferece (0 dim  objects) including parameter information 
 * domain information
 * orientation 
 *  
 *  *** Reference ***
 * The Mesh Object points to the relveant container that holds all the cells elements.
 * 
 * 
 * These following meta data information is always stored in this object
 * - points per element
 * - Parameter per element
 * - unique domain count
 * - domain identifier
 * 
 * Each Mesh Model should only have one type of cell for each topological dimension. (Different cell types for each dimensions are not supported yet)
 * 
 * TODO 
 * Insertions -> Create functions
 * 
 * FIXME
 * Performance unacceptable // Extracting coordinates are taking too long for small 3D files. Must use buffers etc
 * ?? Provide function that dumps the whole HDF5 Array into native Java array[][] etc, and let the writer sort it out (Cut out unecessary individual acccess)
 * 
 * 
 * @author David
 *
 */
public class MeshObject extends kobject {

    STORAGE_TYPE storage_type = STORAGE_TYPE.REFERNECE;

    String reference_path;

    FMLFactory factory;

    Dataset ReferenceDS_buf;

    Dataset ParameterDS_buf;

    Dataset DomainDS_buf;

    Dataset Orien_buf;

    int element_count_buf = -1;

    cTreeNodes node_buf;

    public int[] vtx_ref;

    public Idata[] elements;

    public int points_per_element;

    public int parameter_per_element;

    public int[] domain;

    public int unique_domainCount;

    public String[] domain_identifier;

    public int[][] orientation;

    String class_id;

    int topology_dim;

    public MeshObject(String id, int topo_dim) {
        super(id);
        this.topology_dim = topo_dim;
    }

    public int getTopologyDimension() {
        return topology_dim;
    }

    public int getElementCount() {
        if (storage_type == STORAGE_TYPE.DIRECT) {
            if (elements != null) return elements.length; else return vtx_ref.length;
        } else {
            try {
                if (node_buf == null) {
                    FMLFactory factory = this.getReferenceFactory();
                    VirtualFMLTree tree = factory.getVirtualTree();
                    node_buf = tree.searchCellTree(getReferencePath());
                }
                if (node_buf.getSource() == FMLSource.XML) {
                    Element tag = (Element) node_buf.getReferneceObject();
                    NodeList children = XPathScanner.getScanner().searchNodeListXPath(tag, "*");
                    return children.getLength();
                } else {
                    if (element_count_buf == -1) {
                        Group grp_node = (Group) node_buf.getReferneceObject();
                        hdf5Parser h5parser = node_buf.h5parser;
                        DefaultMutableTreeNode s = h5parser.getTreeNode(grp_node);
                        Enumeration<DefaultMutableTreeNode> children = s.children();
                        DefaultMutableTreeNode child = children.nextElement();
                        Dataset ds = (Dataset) h5parser.getChildObject((HObject) child.getUserObject(), DefaultDatasetNames.ReferenceDS.toString());
                        try {
                            h5parser.master.open();
                            ds.init();
                            long[] dim = ds.getDims();
                            int result = (int) dim[0];
                            h5parser.master.close();
                            element_count_buf = result;
                            return result;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        return element_count_buf;
                    }
                    return -1;
                }
            } catch (PathError e) {
                e.printStackTrace();
            } catch (NoObjectFoundInSearch e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    public int[][] getElementCoordinateIndexArray() {
        if (storage_type == STORAGE_TYPE.DIRECT) {
            if (elements != null) {
                int[][] index_array = new int[elements.length][elements[0].getVertexArray().length];
                for (int h = 0; h < elements.length; h++) {
                    for (int i = 0; i < index_array.length; i++) {
                        index_array[h][i] = ((ptIndex) elements[i].getVertex(i)).getIndex();
                    }
                }
                return index_array;
            } else {
                return new int[][] { vtx_ref };
            }
        } else {
            try {
                if (node_buf == null) {
                    FMLFactory factory = this.getReferenceFactory();
                    VirtualFMLTree tree = factory.getVirtualTree();
                    node_buf = tree.searchCellTree(getReferencePath());
                }
                if (node_buf.getSource() == FMLSource.XML) {
                    Element tag = (Element) node_buf.getReferneceObject();
                    NodeList children = XPathScanner.getScanner().searchNodeListXPath(tag, "*");
                    int[][] result = new int[children.getLength()][];
                    for (int i = 0; i < children.getLength(); i++) {
                        Element cell = (Element) children.item(i);
                        result[i] = factory.getDataAPIWorker().returnXMLGenericCellCoordinateIndex(cell);
                    }
                    return result;
                } else {
                    if (ReferenceDS_buf == null) {
                        Group grp_node = (Group) node_buf.getReferneceObject();
                        DefaultMutableTreeNode s = node_buf.h5parser.getTreeNode(grp_node);
                        Enumeration<DefaultMutableTreeNode> children = s.children();
                        DefaultMutableTreeNode child = children.nextElement();
                        Dataset ds = (Dataset) node_buf.h5parser.getChildObject((HObject) child.getUserObject(), DefaultDatasetNames.ReferenceDS.toString());
                        ReferenceDS_buf = ds;
                        return node_buf.h5parser.getCellHandler().convertDStoIntegerDim2Array(ds);
                    } else {
                        return node_buf.h5parser.getCellHandler().convertDStoIntegerDim2Array(ReferenceDS_buf);
                    }
                }
            } catch (PathError e) {
                e.printStackTrace();
            } catch (NoObjectFoundInSearch e) {
                e.printStackTrace();
            }
        }
        return new int[0][0];
    }

    public int[] getElementCoordinateIndex(int pos) {
        if (storage_type == STORAGE_TYPE.DIRECT) {
            if (elements != null) {
                int[] index_array = new int[elements[pos].getVertexArray().length];
                for (int i = 0; i < index_array.length; i++) {
                    index_array[i] = ((ptIndex) elements[pos].getVertex(i)).getIndex();
                }
                return index_array;
            } else {
                return new int[] { vtx_ref[pos] };
            }
        } else {
            try {
                if (node_buf == null) {
                    FMLFactory factory = this.getReferenceFactory();
                    VirtualFMLTree tree = factory.getVirtualTree();
                    node_buf = tree.searchCellTree(getReferencePath());
                }
                if (node_buf.getSource() == FMLSource.XML) {
                    Element tag = (Element) node_buf.getReferneceObject();
                    NodeList children = XPathScanner.getScanner().searchNodeListXPath(tag, "*");
                    Element cell = (Element) children.item(pos);
                    return factory.getDataAPIWorker().returnXMLGenericCellCoordinateIndex(cell);
                } else {
                    if (ReferenceDS_buf == null) {
                        Group grp_node = (Group) node_buf.getReferneceObject();
                        hdf5Parser h5parser = node_buf.h5parser;
                        DefaultMutableTreeNode s = h5parser.getTreeNode(grp_node);
                        Enumeration<DefaultMutableTreeNode> children = s.children();
                        DefaultMutableTreeNode child = children.nextElement();
                        Dataset ds = (Dataset) h5parser.getChildObject((HObject) child.getUserObject(), DefaultDatasetNames.ReferenceDS.toString());
                        ReferenceDS_buf = ds;
                        int[] ref = h5parser.getMeshHandler().returnHDF5RefenreceDSEntry(pos, ds);
                        return ref;
                    } else {
                        return node_buf.h5parser.getMeshHandler().returnHDF5RefenreceDSEntry(pos, ReferenceDS_buf);
                    }
                }
            } catch (PathError e) {
                e.printStackTrace();
            } catch (NoObjectFoundInSearch e) {
                e.printStackTrace();
            }
        }
        return new int[0];
    }

    public double[][] getElementCoordinate(int pos) {
        if (storage_type == STORAGE_TYPE.DIRECT) {
            if (elements != null) return (double[][]) elements[pos].getCoordinates(); else {
                int pt = vtx_ref[pos];
                return new double[][] { factory.getDataAPIWorker().returnGlobalPointCoordinate(pt) };
            }
        } else {
            try {
                if (node_buf == null) {
                    FMLFactory factory = this.getReferenceFactory();
                    VirtualFMLTree tree = factory.getVirtualTree();
                    node_buf = tree.searchCellTree(getReferencePath());
                }
                if (node_buf.getSource() == FMLSource.XML) {
                    Element tag = (Element) node_buf.getReferneceObject();
                    NodeList children = XPathScanner.getScanner().searchNodeListXPath(tag, "*");
                    Element cell = (Element) children.item(pos);
                    return factory.getDataAPIWorker().returnXMLGenericCellCoordinate(cell);
                } else {
                    if (ReferenceDS_buf == null) {
                        Group grp_node = (Group) node_buf.getReferneceObject();
                        hdf5Parser h5parser = node_buf.h5parser;
                        DefaultMutableTreeNode s = h5parser.getTreeNode(grp_node);
                        Enumeration<DefaultMutableTreeNode> children = s.children();
                        DefaultMutableTreeNode child = children.nextElement();
                        Dataset ds = (Dataset) h5parser.getChildObject((HObject) child.getUserObject(), DefaultDatasetNames.ReferenceDS.toString());
                        ReferenceDS_buf = ds;
                        int[] ref = h5parser.getMeshHandler().returnHDF5RefenreceDSEntry(pos, ds);
                        double[][] result = new double[ref.length][3];
                        for (int i = 0; i < ref.length; i++) {
                            result[i] = factory.getDataAPIWorker().returnGlobalPointCoordinate(ref[i]);
                        }
                        return result;
                    } else {
                        int[] ref = node_buf.h5parser.getMeshHandler().returnHDF5RefenreceDSEntry(pos, ReferenceDS_buf);
                        double[][] result = new double[ref.length][3];
                        for (int i = 0; i < ref.length; i++) {
                            result[i] = factory.getDataAPIWorker().returnGlobalPointCoordinate(ref[i]);
                        }
                        return result;
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

    public double[][] getElementParameterArray() {
        if (storage_type == STORAGE_TYPE.DIRECT) {
            if (elements[0].parameter != null) {
                double[][] result = new double[elements.length][];
                for (int i = 0; i < result.length; i++) {
                    result[i] = elements[i].parameter;
                }
                return result;
            }
            return new double[0][0];
        } else {
            try {
                if (node_buf == null) {
                    FMLFactory factory = this.getReferenceFactory();
                    VirtualFMLTree tree = factory.getVirtualTree();
                    node_buf = tree.searchCellTree(getReferencePath());
                }
                if (node_buf.getSource() == FMLSource.XML) {
                    Element tag = (Element) node_buf.getReferneceObject();
                    NodeList children = XPathScanner.getScanner().searchNodeListXPath(tag, "*");
                    double[][] result = new double[children.getLength()][];
                    for (int h = 0; h < children.getLength(); h++) {
                        Element temp = (Element) children.item(h);
                        NodeList paralist = XPathScanner.getScanner().searchNodeListXPath(temp, FML.parameter.toString());
                        if (paralist.getLength() == 0) {
                            result[h] = new double[0];
                            continue;
                        }
                        for (int i = 0; i < paralist.getLength(); i++) {
                            Element paratemp = (Element) paralist.item(i);
                            result[h][i] = Double.valueOf(paratemp.getAttribute(Attributes.value.toString()));
                        }
                    }
                    return result;
                } else {
                    if (ParameterDS_buf == null) {
                        Group grp_node = (Group) node_buf.getReferneceObject();
                        hdf5Parser h5parser = node_buf.h5parser;
                        DefaultMutableTreeNode s = h5parser.getTreeNode(grp_node);
                        Enumeration<DefaultMutableTreeNode> children = s.children();
                        DefaultMutableTreeNode child = children.nextElement();
                        Dataset ds = (Dataset) h5parser.getChildObject((HObject) child.getUserObject(), DefaultDatasetNames.ParameterDS.toString());
                        ParameterDS_buf = ds;
                        if (ds != null) {
                            return h5parser.getMeshHandler().convertDStoDoubleDim2Array(ds);
                        } else {
                            return new double[0][0];
                        }
                    } else {
                        return node_buf.h5parser.getMeshHandler().convertDStoDoubleDim2Array(ParameterDS_buf);
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

    public double[] getElementParameter(int pos) {
        if (storage_type == STORAGE_TYPE.DIRECT) {
            if (elements[pos].parameter != null) {
                return elements[pos].parameter;
            }
            return new double[0];
        } else {
            try {
                if (node_buf == null) {
                    FMLFactory factory = this.getReferenceFactory();
                    VirtualFMLTree tree = factory.getVirtualTree();
                    node_buf = tree.searchCellTree(getReferencePath());
                }
                if (node_buf.getSource() == FMLSource.XML) {
                    Element tag = (Element) node_buf.getReferneceObject();
                    NodeList children = XPathScanner.getScanner().searchNodeListXPath(tag, "*");
                    Element temp = (Element) children.item(pos);
                    NodeList paralist = XPathScanner.getScanner().searchNodeListXPath(temp, FML.parameter.toString());
                    double[] result = new double[paralist.getLength()];
                    for (int i = 0; i < paralist.getLength(); i++) {
                        Element paratemp = (Element) paralist.item(i);
                        result[i] = Double.valueOf(paratemp.getAttribute(Attributes.value.toString()));
                    }
                    return result;
                } else {
                    if (ParameterDS_buf == null) {
                        Group grp_node = (Group) node_buf.getReferneceObject();
                        hdf5Parser h5parser = node_buf.h5parser;
                        DefaultMutableTreeNode s = h5parser.getTreeNode(grp_node);
                        Enumeration<DefaultMutableTreeNode> children = s.children();
                        DefaultMutableTreeNode child = children.nextElement();
                        Dataset ds = (Dataset) h5parser.getChildObject((HObject) child.getUserObject(), DefaultDatasetNames.ParameterDS.toString());
                        ParameterDS_buf = ds;
                        if (ds != null) {
                            double[] ref = h5parser.getMeshHandler().returnHDF5ParameterDSEntry(pos, ds);
                            return ref;
                        } else {
                            return new double[0];
                        }
                    } else {
                        return node_buf.h5parser.getMeshHandler().returnHDF5ParameterDSEntry(pos, ParameterDS_buf);
                    }
                }
            } catch (PathError e) {
                e.printStackTrace();
            } catch (NoObjectFoundInSearch e) {
                e.printStackTrace();
            }
        }
        return new double[0];
    }

    public int[] getElementDomainArray() {
        if (storage_type == STORAGE_TYPE.DIRECT) {
            return domain;
        } else {
            try {
                if (node_buf == null) {
                    FMLFactory factory = this.getReferenceFactory();
                    VirtualFMLTree tree = factory.getVirtualTree();
                    node_buf = tree.searchCellTree(getReferencePath());
                }
                if (node_buf.getSource() == FMLSource.XML) {
                    Element tag = (Element) node_buf.getReferneceObject();
                    NodeList children = XPathScanner.getScanner().searchNodeListXPath(tag, "*");
                    int[] result = new int[children.getLength()];
                    for (int i = 0; i < children.getLength(); i++) {
                        Element temp = (Element) children.item(i);
                        result[i] = Integer.parseInt(temp.getAttribute(Attributes.domain.toString()));
                    }
                    return result;
                } else {
                    if (DomainDS_buf == null) {
                        Group grp_node = (Group) node_buf.getReferneceObject();
                        hdf5Parser h5parser = node_buf.h5parser;
                        DefaultMutableTreeNode s = h5parser.getTreeNode(grp_node);
                        Enumeration<DefaultMutableTreeNode> children = s.children();
                        DefaultMutableTreeNode child = children.nextElement();
                        Dataset ds = (Dataset) h5parser.getChildObject((HObject) child.getUserObject(), DefaultDatasetNames.DomainDS.toString());
                        DomainDS_buf = ds;
                        return h5parser.getMeshHandler().convertDStoIntegerDim1Array(ds);
                    } else {
                        return node_buf.h5parser.getMeshHandler().convertDStoIntegerDim1Array(DomainDS_buf);
                    }
                }
            } catch (PathError e) {
                e.printStackTrace();
            } catch (NoObjectFoundInSearch e) {
                e.printStackTrace();
            }
        }
        return new int[0];
    }

    public int getElementDomain(int pos) {
        if (storage_type == STORAGE_TYPE.DIRECT) {
            return domain[pos];
        } else {
            try {
                if (node_buf == null) {
                    FMLFactory factory = this.getReferenceFactory();
                    VirtualFMLTree tree = factory.getVirtualTree();
                    node_buf = tree.searchCellTree(getReferencePath());
                }
                if (node_buf.getSource() == FMLSource.XML) {
                    Element tag = (Element) node_buf.getReferneceObject();
                    NodeList children = XPathScanner.getScanner().searchNodeListXPath(tag, "*");
                    Element temp = (Element) children.item(pos);
                    int domina = Integer.parseInt(temp.getAttribute(Attributes.domain.toString()));
                    return domina;
                } else {
                    if (DomainDS_buf == null) {
                        Group grp_node = (Group) node_buf.getReferneceObject();
                        hdf5Parser h5parser = node_buf.h5parser;
                        DefaultMutableTreeNode s = h5parser.getTreeNode(grp_node);
                        Enumeration<DefaultMutableTreeNode> children = s.children();
                        DefaultMutableTreeNode child = children.nextElement();
                        Dataset ds = (Dataset) h5parser.getChildObject((HObject) child.getUserObject(), DefaultDatasetNames.DomainDS.toString());
                        DomainDS_buf = ds;
                        int[] ref = h5parser.getMeshHandler().returnHDF5DomainDSEntry(pos, ds);
                        return ref[0];
                    } else {
                        return node_buf.h5parser.getMeshHandler().returnHDF5DomainDSEntry(pos, DomainDS_buf)[0];
                    }
                }
            } catch (PathError e) {
                e.printStackTrace();
            } catch (NoObjectFoundInSearch e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    public int[][] getElementOrientationArray() {
        if (storage_type == STORAGE_TYPE.DIRECT) {
            return orientation;
        } else {
            try {
                if (node_buf == null) {
                    FMLFactory factory = this.getReferenceFactory();
                    VirtualFMLTree tree = factory.getVirtualTree();
                    node_buf = tree.searchCellTree(getReferencePath());
                }
                if (node_buf.getSource() == FMLSource.XML) {
                    Element tag = (Element) node_buf.getReferneceObject();
                    NodeList children = XPathScanner.getScanner().searchNodeListXPath(tag, "*");
                    int[][] result = new int[children.getLength()][];
                    for (int i = 0; i < children.getLength(); i++) {
                        Element temp = (Element) children.item(i);
                        if (temp.hasAttribute(Attributes.up.toString()) && temp.hasAttribute(Attributes.down.toString())) {
                            int up = Integer.parseInt(temp.getAttribute(Attributes.up.toString()));
                            int down = Integer.parseInt(temp.getAttribute(Attributes.down.toString()));
                            result[i] = new int[] { up, down };
                        } else {
                            result[i] = new int[0];
                        }
                    }
                    return result;
                } else {
                    if (Orien_buf == null) {
                        Group grp_node = (Group) node_buf.getReferneceObject();
                        hdf5Parser h5parser = node_buf.h5parser;
                        DefaultMutableTreeNode s = h5parser.getTreeNode(grp_node);
                        Enumeration<DefaultMutableTreeNode> children = s.children();
                        DefaultMutableTreeNode child = children.nextElement();
                        Dataset ds = (Dataset) h5parser.getChildObject((HObject) child.getUserObject(), DefaultDatasetNames.TopologyDS.toString());
                        Orien_buf = ds;
                        return h5parser.getMeshHandler().convertDStoIntegerDim2Array(ds);
                    } else {
                        return node_buf.h5parser.getMeshHandler().convertDStoIntegerDim2Array(Orien_buf);
                    }
                }
            } catch (PathError e) {
                e.printStackTrace();
            } catch (NoObjectFoundInSearch e) {
                e.printStackTrace();
            }
        }
        return new int[0][0];
    }

    public int[] getElementOrientation(int pos) {
        if (storage_type == STORAGE_TYPE.DIRECT) {
            return orientation[pos];
        } else {
            try {
                if (node_buf == null) {
                    FMLFactory factory = this.getReferenceFactory();
                    VirtualFMLTree tree = factory.getVirtualTree();
                    node_buf = tree.searchCellTree(getReferencePath());
                }
                if (node_buf.getSource() == FMLSource.XML) {
                    Element tag = (Element) node_buf.getReferneceObject();
                    NodeList children = XPathScanner.getScanner().searchNodeListXPath(tag, "*");
                    Element temp = (Element) children.item(pos);
                    int up = Integer.parseInt(temp.getAttribute(Attributes.up.toString()));
                    int down = Integer.parseInt(temp.getAttribute(Attributes.down.toString()));
                    return new int[] { up, down };
                } else {
                    if (Orien_buf == null) {
                        Group grp_node = (Group) node_buf.getReferneceObject();
                        hdf5Parser h5parser = node_buf.h5parser;
                        DefaultMutableTreeNode s = h5parser.getTreeNode(grp_node);
                        Enumeration<DefaultMutableTreeNode> children = s.children();
                        DefaultMutableTreeNode child = children.nextElement();
                        Dataset ds = (Dataset) h5parser.getChildObject((HObject) child.getUserObject(), DefaultDatasetNames.TopologyDS.toString());
                        Orien_buf = ds;
                        int[] ref = h5parser.getMeshHandler().returnHDF5TopologyDSEntry(pos, ds);
                        return ref;
                    } else {
                        return node_buf.h5parser.getMeshHandler().returnHDF5TopologyDSEntry(pos, Orien_buf);
                    }
                }
            } catch (PathError e) {
                e.printStackTrace();
            } catch (NoObjectFoundInSearch e) {
                e.printStackTrace();
            }
        }
        return new int[0];
    }

    public int getUniqueDomainCount() {
        return unique_domainCount;
    }

    public int getPointsPerElement() {
        return points_per_element;
    }

    public int getParameterPerElement() {
        return parameter_per_element;
    }

    public String getDomainIdentifier(int domain) {
        return domain_identifier[domain];
    }

    public void setDomainIdentifier(String[] id) {
        this.domain_identifier = id;
    }

    public void createElementArray(int count) {
        elements = new Idata[count];
    }

    public void createDomainArray(int count) {
        domain = new int[count];
    }

    public void createOrientationArray(int count) {
        orientation = new int[count][2];
    }

    public void setStorageType(STORAGE_TYPE type) {
        storage_type = type;
    }

    public STORAGE_TYPE getStorageType() {
        return storage_type;
    }

    public void setReferencePath(String path) {
        this.reference_path = path;
    }

    public String getReferencePath() {
        return reference_path;
    }

    public FMLFactory getReferenceFactory() {
        return factory;
    }

    public void setReferenceFactory(FMLFactory factory) {
        this.factory = factory;
    }

    public String getClassID() {
        return class_id;
    }

    public void setClassID(String class_id) {
        this.class_id = class_id;
    }

    public void dispose() {
        ReferenceDS_buf = null;
        ParameterDS_buf = null;
        DomainDS_buf = null;
        Orien_buf = null;
        node_buf = null;
    }
}
