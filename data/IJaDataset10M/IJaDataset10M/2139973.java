package edu.gsbme.wasabi.UI.tree;

import ncsa.hdf.object.HObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import edu.gsbme.MMLParser2.FML.FMLCore;
import edu.gsbme.MMLParser2.FML.FMLFrame;
import edu.gsbme.MMLParser2.FML.FMLMapping;
import edu.gsbme.MMLParser2.FML.Search.FMLSearch.FMLSource;
import edu.gsbme.MMLParser2.FML.VirtualTree.VirtualFMLTree;
import edu.gsbme.MMLParser2.FML.VirtualTree.cTreeNodes;
import edu.gsbme.MMLParser2.Factory.FMLFactory;
import edu.gsbme.MMLParser2.Vocabulary.Attributes;
import edu.gsbme.MMLParser2.Vocabulary.AttributesValues;
import edu.gsbme.MMLParser2.Vocabulary.FML;
import edu.gsbme.wasabi.DataModel.XMLModel;
import edu.gsbme.wasabi.UI.UItext;
import edu.gsbme.wasabi.UI.UItype;
import edu.gsbme.wasabi.UI.Dialog.Common.MMLHeaderDialog;
import edu.gsbme.wasabi.UI.Dialog.FML.BrepOverviewDialog;
import edu.gsbme.wasabi.UI.Dialog.FML.DimensionElementDialog;
import edu.gsbme.wasabi.UI.Dialog.FML.DimensionOverviewDialog;
import edu.gsbme.wasabi.UI.Dialog.FML.FrameDialog;
import edu.gsbme.wasabi.UI.Dialog.FML.MeshOverviewDialog;
import edu.gsbme.wasabi.UI.Dialog.FML.BREP.DomainAdjOverviewDialog;
import edu.gsbme.wasabi.UI.Dialog.FML.BREP.EdgeAdjOverviewDialog;
import edu.gsbme.wasabi.UI.Dialog.FML.BREP.FaceAdjOverviewDialog;
import edu.gsbme.wasabi.UI.Dialog.FML.BREP.pEdgeAdjOverviewDialog;
import edu.gsbme.wasabi.UI.Dialog.FML.BREP.pVertexAdjOverviewDialog;
import edu.gsbme.wasabi.UI.Dialog.FML.Cell.CellListOverviewDialog;
import edu.gsbme.wasabi.UI.Dialog.FML.Cell.DimTagOverviewDialog;
import edu.gsbme.wasabi.UI.Dialog.FML.Controller.CellDialogController;
import edu.gsbme.wasabi.UI.Dialog.FML.FieldAttributes.AttributeGroupOverviewDialog;
import edu.gsbme.wasabi.UI.Dialog.FML.FieldAttributes.FieldAttributeOverviewDialog;
import edu.gsbme.wasabi.UI.Dialog.FML.Mesh.MeshEdgeListDialog;
import edu.gsbme.wasabi.UI.Dialog.FML.Mesh.MeshElemListDialog;
import edu.gsbme.wasabi.UI.Dialog.FML.Mesh.MeshFaceListDialog;
import edu.gsbme.wasabi.UI.Dialog.FML.Mesh.MeshVertexListDialog;
import edu.gsbme.wasabi.UI.tree.menu.Basic_Edit_Delete_Menu;
import edu.gsbme.wasabi.UI.tree.menu.Basic_Edit_Menu;
import edu.gsbme.wasabi.UI.tree.menu.Basic_New_Menu;
import edu.gsbme.wasabi.UI.tree.menu.Basic_Overview_Edit_Menu;
import edu.gsbme.wasabi.UI.tree.menu.Basic_Overview_Menu;
import edu.gsbme.wasabi.UI.tree.menu.Metadata_Root_Menu;

/**
 * Construct 
 * -Import
 * -Declaration
 * -Frame list
 *  -Spatial
 *   Vertex, edge, face list
 *  -fields
 * -Frame Hierarcy
 * 
 * 
 * NOTE
 * Tree Cell Branch Generation must take into consideration whether the parent or ancestors are HDF5 or XML elements
 * HDF5 nodes can not be edited or have new information attached to it. This is not supported in this release.
 * 
 * Only XML element may have their information edited or added.
 * 
 * 
 * @author David
 *
 */
public class FMLTree extends ITree {

    public FMLTree(XMLModel dom) {
        super(dom);
    }

    @Override
    public void genereateTree() {
        populateTree();
    }

    private void populateTree() {
        final Element fml = FMLCore.getRootElement(dom.returnDoc());
        if (fml == null) {
            root = new ITreeNode(null, FML.fml.toString(), "FML empty", null);
            root.setImageCode(ITreeNodeImageCode.fml_empty);
            return;
        } else {
            root = new ITreeNode(null, FML.fml.toString(), "FML : " + fml.getAttribute(Attributes.name.toString()), fml);
            root.setImageCode(ITreeNodeImageCode.fml_default);
        }
        Menu menu = new Menu(getShell(), SWT.POP_UP);
        MenuItem Edit = new MenuItem(menu, SWT.PUSH);
        Edit.setText(UItext.edit);
        Edit.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                new MMLHeaderDialog(getShell(), (Element) root.getReferenceObject(), UItype.OVERVIEW).OpenDialog();
            }
        });
        Element[] frame = FMLFrame.returnFrameList(fml);
        if (frame.length == 0) {
            MenuItem menuitem = new MenuItem(menu, SWT.PUSH);
            menuitem.setText("Insert Frame");
            menuitem.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent e) {
                    new FrameDialog(getShell(), fml, UItype.NEW).OpenDialog();
                }
            });
        }
        MenuItem separator = new MenuItem(menu, SWT.SEPARATOR);
        MenuItem metadata = new MenuItem(menu, SWT.CASCADE);
        metadata.setText("&Metadata");
        metadata.setMenu(Metadata_Root_Menu.getMenu(getShell(), root));
        root.setMenu(menu);
        ImportTree iTree = new ImportTree(dom);
        iTree.genereateTree();
        root.insertChild(iTree.root);
        DeclTree dTree = new DeclTree(dom);
        dTree.genereateTree();
        root.insertChild(dTree.root);
        populateMapping(root, fml);
        populateFrameList(root, fml);
    }

    private void populateFrameList(ITreeNode parent, Element fml) {
        Element[] framelist = FMLFrame.returnFrameList(fml);
        for (int i = 0; i < framelist.length; i++) {
            populateFrame(parent, framelist[i]);
        }
    }

    private void populateMapping(ITreeNode parent, Element fml) {
        Element mapping = FMLFrame.returnMappingTag(fml);
        if (mapping != null) {
            ITreeNode mappingVtx = new ITreeNode(parent, FML.mapping.toString(), "Mapping", mapping);
            mappingVtx.setImageCode(ITreeNodeImageCode.fml_default);
            Element mapListTag = FMLMapping.returnMapListTag(mapping);
            ITreeNode mapList = new ITreeNode(mappingVtx, FML.mapping.toString(), "Mapping", mapListTag);
            mapList.setImageCode(ITreeNodeImageCode.fml_default);
            Element[] applyList = FMLMapping.returnLeadingApplyTags(mapping);
            for (int i = 0; i < applyList.length; i++) {
                ITreeNode temp = new ITreeNode(mappingVtx, FML.apply.toString(), "Apply : " + applyList[i].getAttribute(Attributes.id.toString()), applyList[i]);
                temp.setImageCode(ITreeNodeImageCode.fml_default);
            }
        }
    }

    private void populateFrame(ITreeNode parent, Element frame) {
        final ITreeNode frameVtx = new ITreeNode(parent, FML.frame.toString(), "Frame : " + frame.getAttribute(Attributes.name.toString()), frame);
        frameVtx.setImageCode(ITreeNodeImageCode.fml_default);
        Menu menu = Basic_Overview_Menu.getMenu(getShell(), frameVtx);
        menu.getItem(Basic_Overview_Edit_Menu.Overview_Menu).addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                new FrameDialog(getShell(), (Element) frameVtx.getReferenceObject(), UItype.OVERVIEW).OpenDialog();
            }
        });
        frameVtx.setMenu(menu);
        Element dim = FMLFrame.returnDimensionTag(frame);
        populateDimList(frameVtx, dim, frame);
        FMLFactory factory = (FMLFactory) returnXMLModel().getFactory();
        VirtualFMLTree vTree = factory.getVirtualTree();
        populateVirtualTree_CellStructure(frameVtx, vTree.getCellTree());
        Element brep = FMLFrame.returnBRepTag(frame);
        populateBREP(frameVtx, brep, frame);
        populateVirtualTree_Mesh(frameVtx, vTree.getMeshTree());
        Element fields = FMLFrame.returnFieldsTag(frame);
        populateFieldsAttributes(frameVtx, fields, frame);
    }

    private void populateDimList(ITreeNode parent, Element dimension, Element eparent) {
        if (dimension != null) {
            final ITreeNode clistVTX = new ITreeNode(parent, FML.dimension.toString(), "Dimensions", dimension);
            clistVTX.setImageCode(ITreeNodeImageCode.fml_default);
            Menu menu = Basic_Overview_Menu.getMenu(getShell(), clistVTX);
            menu.getItem(Basic_Overview_Edit_Menu.Overview_Menu).addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent e) {
                    new DimensionOverviewDialog(getShell(), (Element) clistVTX.getReferenceObject(), UItype.OVERVIEW).OpenDialog();
                }
            });
            clistVTX.setMenu(menu);
            NodeList list = FMLFrame.returnDimensionElementList(dimension);
            for (int i = 0; i < list.getLength(); i++) {
                final Element temp = (Element) list.item(i);
                ITreeNode node = new ITreeNode(clistVTX, FML.dimension_element.toString(), temp.getAttribute(Attributes.name.toString()), temp);
                node.setImageCode(ITreeNodeImageCode.fml_default);
                node.setMenu(Basic_Edit_Delete_Menu.getMenu(getShell(), node));
                node.returnMenu().getItem(Basic_Edit_Delete_Menu.Edit_menu).addSelectionListener(new SelectionAdapter() {

                    public void widgetSelected(SelectionEvent e) {
                        DimensionElementDialog dialog = new DimensionElementDialog(getShell(), temp, UItype.EDIT);
                        dialog.OpenDialog();
                    }
                });
            }
        }
    }

    private void populateVirtualTree_CellStructure(ITreeNode parent, final cTreeNodes cell_node) {
        if (cell_node != null) {
            cTreeNodes[] children = cell_node.getChildren();
            final ITreeNode node = new ITreeNode(parent, FML.cell_list.toString(), "Cell List", cell_node.getReferneceObject());
            node.setImageCode(ITreeNodeImageCode.fml_default);
            Menu menu1 = Basic_Overview_Menu.getMenu(getShell(), node);
            menu1.getItem(Basic_Overview_Edit_Menu.Overview_Menu).addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent e) {
                    if (cell_node.getInterchangeReferenceObject() != null) {
                        new CellListOverviewDialog(getShell(), (Element) cell_node.getInterchangeReferenceObject(), UItype.OVERVIEW).OpenDialog();
                    } else new CellListOverviewDialog(getShell(), (Element) node.getReferenceObject(), UItype.OVERVIEW).OpenDialog();
                }
            });
            node.setMenu(menu1);
            boolean dim0_parsed = false;
            boolean dim1_parsed = false;
            boolean dim2_parsed = false;
            boolean dim3_parsed = false;
            for (int i = 0; i < children.length; i++) {
                if (children[i].getFML_ID().equals(FML.dim_0.toString())) {
                    dim0_parsed = true;
                    populateVirtualTree_DimStructure(node, children[i]);
                } else if (children[i].getFML_ID().equals(FML.dim_1.toString())) {
                    dim1_parsed = true;
                    populateVirtualTree_DimStructure(node, children[i]);
                } else if (children[i].getFML_ID().equals(FML.dim_2.toString())) {
                    dim2_parsed = true;
                    populateVirtualTree_DimStructure(node, children[i]);
                } else if (children[i].getFML_ID().equals(FML.dim_3.toString())) {
                    dim3_parsed = true;
                    populateVirtualTree_DimStructure(node, children[i]);
                }
            }
            if (!dim0_parsed) {
                final ITreeNode vtx = new ITreeNode(node, FML.dim_0.toString(), FML.dim_0.toString() + " (Empty)", null);
                vtx.setImageCode(ITreeNodeImageCode.fml_default);
                Menu menu = Basic_New_Menu.getMenu(getShell(), vtx);
                menu.getItem(Basic_New_Menu.New_menu).addSelectionListener(new SelectionAdapter() {

                    public void widgetSelected(SelectionEvent e) {
                        new DimTagOverviewDialog(getShell(), null, 0, UItype.NEW).OpenDialog();
                    }
                });
                vtx.setMenu(menu);
            }
            if (!dim1_parsed) {
                final ITreeNode vtx = new ITreeNode(node, FML.dim_1.toString(), FML.dim_1.toString() + " (Empty)", null);
                vtx.setImageCode(ITreeNodeImageCode.fml_default);
                Menu menu = Basic_New_Menu.getMenu(getShell(), vtx);
                menu.getItem(Basic_New_Menu.New_menu).addSelectionListener(new SelectionAdapter() {

                    public void widgetSelected(SelectionEvent e) {
                        new DimTagOverviewDialog(getShell(), null, 1, UItype.NEW).OpenDialog();
                    }
                });
                vtx.setMenu(menu);
            }
            if (!dim2_parsed) {
                final ITreeNode vtx = new ITreeNode(node, FML.dim_2.toString(), FML.dim_2.toString() + " (Empty)", null);
                vtx.setImageCode(ITreeNodeImageCode.fml_default);
                Menu menu = Basic_New_Menu.getMenu(getShell(), vtx);
                menu.getItem(Basic_New_Menu.New_menu).addSelectionListener(new SelectionAdapter() {

                    public void widgetSelected(SelectionEvent e) {
                        new DimTagOverviewDialog(getShell(), null, 2, UItype.NEW).OpenDialog();
                    }
                });
                vtx.setMenu(menu);
            }
            if (!dim3_parsed) {
                final ITreeNode vtx = new ITreeNode(node, FML.dim_3.toString(), FML.dim_3.toString() + " (Empty)", null);
                vtx.setImageCode(ITreeNodeImageCode.fml_default);
                Menu menu = Basic_New_Menu.getMenu(getShell(), vtx);
                menu.getItem(Basic_New_Menu.New_menu).addSelectionListener(new SelectionAdapter() {

                    public void widgetSelected(SelectionEvent e) {
                        new DimTagOverviewDialog(getShell(), null, 3, UItype.NEW).OpenDialog();
                    }
                });
                vtx.setMenu(menu);
            }
        } else {
            final ITreeNode clistVTX = new ITreeNode(parent, FML.cell_list.toString(), "Cell List", null);
            clistVTX.setImageCode(ITreeNodeImageCode.fml_empty);
            Menu menu = Basic_Overview_Menu.getMenu(getShell(), clistVTX);
            menu.getItem(Basic_Overview_Edit_Menu.Overview_Menu).addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent e) {
                    new CellListOverviewDialog(getShell(), null, UItype.NEW).OpenDialog();
                }
            });
            clistVTX.setMenu(menu);
        }
    }

    private void populateVirtualTree_DimStructure(ITreeNode parent, final cTreeNodes dim_node) {
        if (dim_node != null) {
            cTreeNodes[] children = dim_node.getChildren();
            final ITreeNode node = new ITreeNode(parent, dim_node.getFML_ID(), dim_node.getFML_ID(), dim_node.getReferneceObject());
            node.setImageCode(ITreeNodeImageCode.fml_default);
            Menu menu = Basic_Overview_Menu.getMenu(getShell(), root);
            menu.getItem(Basic_Overview_Menu.Overview_Menu).addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent e) {
                    if (dim_node.getInterchangeReferenceObject() != null) {
                        CellDialogController.getDialog(dim_node.getFML_ID(), null, (Element) dim_node.getInterchangeReferenceObject(), UItype.OVERVIEW, getShell());
                    } else {
                        if (dim_node.getReferneceObject() instanceof Element) {
                            CellDialogController.getDialog(dim_node.getFML_ID(), null, (Element) node.getReferenceObject(), UItype.OVERVIEW, getShell());
                        } else if (dim_node.getReferneceObject() instanceof HObject) {
                            CellDialogController.getDialog(dim_node.getFML_ID(), dim_node.h5parser, node.getReferenceObject(), UItype.OVERVIEW, getShell());
                        }
                    }
                }
            });
            node.setMenu(menu);
            if (dim_node.getFML_ID().equals(FML.dim_0.toString())) {
                for (int i = 0; i < children.length; i++) {
                    populateVirtualTree_ClassStructure(node, children[i], true);
                }
            } else {
                for (int i = 0; i < children.length; i++) {
                    populateVirtualTree_ClassStructure(node, children[i], false);
                }
            }
        } else {
            System.out.println("This should never be reached : @ populateVirtualTree_xxxx@FMLTree.java");
        }
    }

    private void populateVirtualTree_ClassStructure(ITreeNode parent, final cTreeNodes class_node, boolean summarize) {
        if (class_node != null) {
            cTreeNodes[] children = class_node.getChildren();
            String summary = "";
            if (summarize) summary += " (" + children.length + ")";
            final ITreeNode node = new ITreeNode(parent, class_node.getFML_ID(), class_node.getFML_ID() + summary, class_node.getReferneceObject());
            node.setImageCode(ITreeNodeImageCode.fml_default);
            Menu menu = Basic_Overview_Menu.getMenu(getShell(), root);
            menu.getItem(Basic_Overview_Menu.Overview_Menu).addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent e) {
                    if (class_node.getInterchangeReferenceObject() != null) {
                        CellDialogController.getDialog(class_node.getFML_ID(), null, (Element) class_node.getInterchangeReferenceObject(), UItype.OVERVIEW, getShell());
                    } else {
                        if (class_node.getReferneceObject() instanceof Element) {
                            CellDialogController.getDialog(class_node.getFML_ID(), null, (Element) node.getReferenceObject(), UItype.OVERVIEW, getShell());
                        } else if (class_node.getReferneceObject() instanceof HObject) {
                            CellDialogController.getDialog(class_node.getFML_ID(), class_node.h5parser, node.getReferenceObject(), UItype.OVERVIEW, getShell());
                        }
                    }
                }
            });
            node.setMenu(menu);
            if (!summarize) {
                for (int i = 0; i < children.length; i++) {
                    final String fml_id = children[i].getFML_ID();
                    final Object ref = children[i].getReferneceObject();
                    ITreeNode child = new ITreeNode(node, children[i].getFML_ID(), children[i].getName(), children[i].getReferneceObject());
                    child.setImageCode(ITreeNodeImageCode.fml_default);
                    Menu cmenu = Basic_Edit_Menu.getMenu(getShell(), root);
                    cmenu.getItem(Basic_Edit_Menu.Edit_menu).addSelectionListener(new SelectionAdapter() {

                        public void widgetSelected(SelectionEvent e) {
                            CellDialogController.getDialog(fml_id, null, ref, UItype.EDIT, getShell());
                        }
                    });
                }
            }
        } else {
            System.out.println("This should never be reached : @ populateVirtualTree_xxxx@FMLTree.java");
        }
    }

    private void populateBREP(ITreeNode parent, Element brep, final Element eparent) {
        if (brep != null) {
            final ITreeNode vtx = new ITreeNode(parent, FML.b_rep.toString(), FML.b_rep.toString(), brep);
            vtx.setImageCode(ITreeNodeImageCode.fml_default);
            Menu menu = Basic_Overview_Menu.getMenu(getShell(), vtx);
            menu.getItem(Basic_Overview_Edit_Menu.Overview_Menu).addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent e) {
                    new BrepOverviewDialog(getShell(), (Element) vtx.getReferenceObject(), UItype.OVERVIEW).OpenDialog();
                }
            });
            vtx.setMenu(menu);
            populateAdjacencyList(vtx, brep);
        } else {
            final ITreeNode vtx = new ITreeNode(parent, FML.b_rep.toString(), FML.b_rep.toString() + " (Empty)", eparent);
            vtx.setImageCode(ITreeNodeImageCode.fml_empty);
            Menu menu = Basic_New_Menu.getMenu(getShell(), vtx);
            menu.getItem(Basic_New_Menu.New_menu).addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent e) {
                    new BrepOverviewDialog(getShell(), eparent, UItype.NEW).OpenDialog();
                }
            });
            vtx.setMenu(menu);
        }
    }

    private void populateAdjacencyList(ITreeNode parent, Element brep) {
        Element pVtx = FMLFrame.returnAdjacencyTag(brep, AttributesValues.pvertex.toString());
        if (pVtx != null) {
            final ITreeNode adjpV = new ITreeNode(parent, "pVertex", "Parameteric Vertex Adjacency", brep);
            adjpV.setImageCode(ITreeNodeImageCode.fml_default);
            Menu menuF = Basic_Overview_Menu.getMenu(getShell(), adjpV);
            menuF.getItem(Basic_Overview_Edit_Menu.Overview_Menu).addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent e) {
                    new pVertexAdjOverviewDialog(getShell(), (Element) adjpV.getReferenceObject(), UItype.OVERVIEW).OpenDialog();
                }
            });
            adjpV.setMenu(menuF);
        }
        Element edg = FMLFrame.returnAdjacencyTag(brep, AttributesValues.edge.toString());
        if (edg != null) {
            final ITreeNode adjE = new ITreeNode(parent, "EdgeAdj", "Edge Adjacency", brep);
            adjE.setImageCode(ITreeNodeImageCode.fml_default);
            Menu menuE = Basic_Overview_Menu.getMenu(getShell(), adjE);
            menuE.getItem(Basic_Overview_Edit_Menu.Overview_Menu).addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent e) {
                    new EdgeAdjOverviewDialog(getShell(), (Element) adjE.getReferenceObject(), UItype.OVERVIEW).OpenDialog();
                }
            });
            adjE.setMenu(menuE);
        }
        Element pEdge = FMLFrame.returnAdjacencyTag(brep, AttributesValues.pedge.toString());
        if (pEdge != null) {
            final ITreeNode adjPE = new ITreeNode(parent, "pEdge", "Parameteric Edge Adjacency", brep);
            adjPE.setImageCode(ITreeNodeImageCode.fml_default);
            Menu menuF = Basic_Overview_Menu.getMenu(getShell(), adjPE);
            menuF.getItem(Basic_Overview_Edit_Menu.Overview_Menu).addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent e) {
                    new pEdgeAdjOverviewDialog(getShell(), (Element) adjPE.getReferenceObject(), UItype.OVERVIEW).OpenDialog();
                }
            });
            adjPE.setMenu(menuF);
        }
        Element face = FMLFrame.returnAdjacencyTag(brep, AttributesValues.face.toString());
        if (face != null) {
            final ITreeNode adjF = new ITreeNode(parent, "FaceAdj", "Face Adjacency", face);
            adjF.setImageCode(ITreeNodeImageCode.fml_default);
            Menu menuF = Basic_Overview_Menu.getMenu(getShell(), adjF);
            menuF.getItem(Basic_Overview_Edit_Menu.Overview_Menu).addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent e) {
                    new FaceAdjOverviewDialog(getShell(), (Element) adjF.getReferenceObject(), UItype.OVERVIEW).OpenDialog();
                }
            });
            adjF.setMenu(menuF);
        }
        Element sub = FMLFrame.returnAdjacencyTag(brep, AttributesValues.subdomain.toString());
        if (sub != null) {
            final ITreeNode adjSub = new ITreeNode(parent, "DomainAdj", "Subdomain", sub);
            adjSub.setImageCode(ITreeNodeImageCode.fml_default);
            Menu menu = Basic_Overview_Menu.getMenu(getShell(), adjSub);
            menu.getItem(Basic_Overview_Edit_Menu.Overview_Menu).addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent e) {
                    new DomainAdjOverviewDialog(getShell(), (Element) adjSub.getReferenceObject(), UItype.OVERVIEW).OpenDialog();
                }
            });
            adjSub.setMenu(menu);
        }
    }

    private void populateVirtualTree_Mesh(ITreeNode parent, final cTreeNodes mesh_node) {
        if (mesh_node != null) {
            if (mesh_node.getSource() != FMLSource.ERROR) {
                final ITreeNode node = new ITreeNode(parent, FML.mesh.toString(), mesh_node.getName(), mesh_node.getReferneceObject());
                node.setImageCode(ITreeNodeImageCode.fml_default);
                Menu Mmenu = Basic_Overview_Menu.getMenu(getShell(), node);
                Mmenu.getItem(Basic_Overview_Menu.Overview_Menu).addSelectionListener(new SelectionAdapter() {

                    public void widgetSelected(SelectionEvent e) {
                        if (mesh_node.isInterchangeNode()) {
                            new MeshOverviewDialog(getShell(), (Element) mesh_node.getInterchangeReferenceObject(), UItype.OVERVIEW).OpenDialog();
                        } else {
                            new MeshOverviewDialog(getShell(), (Element) node.getReferenceObject(), UItype.OVERVIEW).OpenDialog();
                        }
                    }
                });
                node.setMenu(Mmenu);
                cTreeNodes[] children = mesh_node.getChildren();
                for (int i = 0; i < children.length; i++) {
                    populateVirtualTree_MeshTopologyClass(node, children[i]);
                }
            } else {
                final ITreeNode node = new ITreeNode(parent, FML.mesh.toString(), mesh_node.getName(), mesh_node.getReferneceObject());
                node.setImageCode(ITreeNodeImageCode.fml_default);
            }
        } else {
            final ITreeNode vtx = new ITreeNode(parent, FML.mesh.toString(), FML.mesh.toString() + " (Empty)", null);
            vtx.setImageCode(ITreeNodeImageCode.fml_empty);
            Menu menu = Basic_Overview_Menu.getMenu(getShell(), vtx);
            menu.getItem(Basic_Overview_Menu.Overview_Menu).addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent e) {
                    new MeshOverviewDialog(getShell(), null, UItype.NEW).OpenDialog();
                }
            });
            vtx.setMenu(menu);
        }
    }

    private void populateVirtualTree_MeshTopologyClass(ITreeNode parent, final cTreeNodes topo_node) {
        final ITreeNode node = new ITreeNode(parent, topo_node.getFML_ID(), topo_node.getFML_ID(), topo_node.getReferneceObject());
        node.setImageCode(ITreeNodeImageCode.fml_default);
        Menu Mmenu = Basic_Overview_Menu.getMenu(getShell(), node);
        Mmenu.getItem(Basic_Overview_Menu.Overview_Menu).addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                if (topo_node.isInterchangeNode()) {
                    if (topo_node.getFML_ID().equalsIgnoreCase(FML.vertex_list.toString())) {
                        new MeshVertexListDialog(getShell(), (Element) topo_node.getInterchangeReferenceObject(), UItype.OVERVIEW).OpenDialog();
                    } else if (topo_node.getFML_ID().equalsIgnoreCase(FML.edge_list.toString())) {
                        new MeshEdgeListDialog(getShell(), (Element) topo_node.getInterchangeReferenceObject(), UItype.OVERVIEW).OpenDialog();
                    } else if (topo_node.getFML_ID().equalsIgnoreCase(FML.face_list.toString())) {
                        new MeshFaceListDialog(getShell(), (Element) topo_node.getInterchangeReferenceObject(), UItype.OVERVIEW).OpenDialog();
                    } else if (topo_node.getFML_ID().equalsIgnoreCase(FML.element_list.toString())) {
                        new MeshElemListDialog(getShell(), (Element) topo_node.getInterchangeReferenceObject(), UItype.OVERVIEW).OpenDialog();
                    }
                } else {
                    if (topo_node.getFML_ID().equalsIgnoreCase(FML.vertex_list.toString())) {
                        new MeshVertexListDialog(getShell(), topo_node, UItype.OVERVIEW).OpenDialog();
                    } else if (topo_node.getFML_ID().equalsIgnoreCase(FML.edge_list.toString())) {
                        new MeshEdgeListDialog(getShell(), topo_node, UItype.OVERVIEW).OpenDialog();
                    } else if (topo_node.getFML_ID().equalsIgnoreCase(FML.face_list.toString())) {
                        new MeshFaceListDialog(getShell(), topo_node, UItype.OVERVIEW).OpenDialog();
                    } else if (topo_node.getFML_ID().equalsIgnoreCase(FML.element_list.toString())) {
                        new MeshElemListDialog(getShell(), topo_node, UItype.OVERVIEW).OpenDialog();
                    }
                }
            }
        });
        node.setMenu(Mmenu);
    }

    private void populateFieldsAttributes(ITreeNode parent, final Element fields, Element eparent) {
        if (fields != null) {
            ITreeNode vtx = new ITreeNode(parent, FML.fields.toString(), FML.fields.toString(), fields);
            vtx.setImageCode(ITreeNodeImageCode.fml_default);
            Menu menu = Basic_Overview_Menu.getMenu(getShell(), vtx);
            menu.getItem(Basic_Overview_Menu.Overview_Menu).addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent e) {
                    new FieldAttributeOverviewDialog(getShell(), fields, UItype.OVERVIEW).OpenDialog();
                }
            });
            vtx.setMenu(menu);
            NodeList grp_list = FMLFrame.returnAttrGroupList(fields);
            for (int i = 0; i < grp_list.getLength(); i++) {
                Element temp = (Element) grp_list.item(i);
                populateFieldAttributeGroup(vtx, temp);
            }
        } else {
            ITreeNode vtx = new ITreeNode(parent, FML.fields.toString(), FML.fields.toString() + " (Empty)", eparent);
            vtx.setImageCode(ITreeNodeImageCode.fml_empty);
            Menu menu = Basic_Overview_Menu.getMenu(getShell(), vtx);
            menu.getItem(Basic_Overview_Menu.Overview_Menu).addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent e) {
                    new FieldAttributeOverviewDialog(getShell(), fields, UItype.NEW).OpenDialog();
                }
            });
            vtx.setMenu(menu);
        }
    }

    private void populateFieldAttributeGroup(ITreeNode parent, Element group) {
        final ITreeNode vtx = new ITreeNode(parent, FML.attr_group.toString(), group.getAttribute(Attributes.name.toString()), group);
        vtx.setImageCode(ITreeNodeImageCode.fml_default);
        Menu menu = Basic_Edit_Delete_Menu.getMenu(getShell(), root);
        menu.getItem(Basic_Edit_Delete_Menu.Edit_menu).addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                new AttributeGroupOverviewDialog(getShell(), (Element) vtx.getReferenceObject(), UItype.EDIT).OpenDialog();
            }
        });
        vtx.setMenu(menu);
    }
}
