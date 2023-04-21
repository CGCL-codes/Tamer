package org.dengues.warehouse.viewers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.dengues.commons.IDenguesSharedImage;
import org.dengues.commons.utils.ImageUtil;
import org.dengues.core.ExceptionOperation;
import org.dengues.core.dbs.DBSChecker;
import org.dengues.core.metadata.MetadataHelper;
import org.dengues.core.metadata.MetadataTable;
import org.dengues.core.resource.DenguesResourceManager;
import org.dengues.core.resource.WarehouseResourceFactory;
import org.dengues.core.warehouse.ENavNodeType;
import org.dengues.core.warehouse.ENodeCategoryName;
import org.dengues.core.warehouse.ENodeStatus;
import org.dengues.core.warehouse.IWarehouseNode;
import org.dengues.core.warehouse.IWarehouseObject;
import org.dengues.core.warehouse.IWarehouseView;
import org.dengues.core.warehouse.WarehouseLabelUtil;
import org.dengues.model.database.DBColumn;
import org.dengues.model.database.DBTable;
import org.dengues.model.database.DatabaseDiagram;
import org.dengues.model.database.EMFDataType;
import org.dengues.model.project.CDCConnType;
import org.dengues.model.project.ConnPropertySetType;
import org.dengues.model.warehouse.ColumnType;
import org.dengues.model.warehouse.MetadataType;
import org.dengues.model.warehouse.ProcessType;
import org.dengues.model.warehouse.Storage;
import org.dengues.ui.editors.GEFEditorUtils;
import org.dengues.warehouse.i18n.Messages;
import org.dengues.warehouse.models.BlockRefWarehouseNode;
import org.dengues.warehouse.models.CDCWarehouseNode;
import org.dengues.warehouse.models.FolderWarehouseNode;
import org.dengues.warehouse.models.WarehouseNode;
import org.dengues.warehouse.models.WarehouseObject;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DecorationOverlayIcon;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

/**
 * Qiang.Zhang.Adolf@gmail.com class global comment. Detailled comment <br/>
 * 
 * $Id: Dengues.epf 2007-12-10 qiang.zhang $
 * 
 */
public class WarehouseViewProvider extends LabelProvider implements IStructuredContentProvider, ITreeContentProvider, IFontProvider {

    private static Map<String, MetadataTable> warehouseTables = new HashMap<String, MetadataTable>();

    private static Map<String, DatabaseDiagram> warehouseQueries = new HashMap<String, DatabaseDiagram>();

    private final IWarehouseView view;

    private final WarehouseViewFactory viewFactory = new WarehouseViewFactory();

    private WarehouseNode root;

    private WarehouseNode binNode, dataSourceNode, reportsNode, scriptsNode, blockNode;

    private static WarehouseNode processNode, dbNode, flatFileNode, xmlFileNode, excelFileNode;

    /**
     * Qiang.Zhang.Adolf@gmail.com WarehouseViewProvider constructor comment.
     * 
     * @param viewer
     */
    public WarehouseViewProvider(IWarehouseView viewer) {
        super();
        this.view = viewer;
    }

    public void inputChanged(Viewer v, Object oldInput, Object newInput) {
    }

    public Object[] getElements(Object parent) {
        if (parent.equals(view.getViewSite())) {
            root = (WarehouseNode) view.getRoot();
            if (root.getChildren().isEmpty()) {
                initialize();
            }
            return root.getChildren().toArray(new WarehouseNode[0]);
        }
        return getChildren(parent);
    }

    public Object getParent(Object child) {
        if (child instanceof WarehouseNode) {
            return ((WarehouseNode) child).getParent();
        }
        return null;
    }

    public Object[] getChildren(Object parent) {
        WarehouseNode pp = (WarehouseNode) parent;
        if (!pp.isInitialized()) {
            try {
                if (pp == processNode) {
                    convert(ENodeCategoryName.PROCESS, processNode);
                } else if (pp == dbNode) {
                } else if (pp == flatFileNode) {
                } else if (pp == xmlFileNode) {
                } else if (pp == excelFileNode) {
                } else if (pp == dataSourceNode) {
                    warehouseTables.clear();
                    warehouseQueries.clear();
                    convert(ENodeCategoryName.DATABASE, dbNode);
                    convert(ENodeCategoryName.EXCELFILE, excelFileNode);
                    convert(ENodeCategoryName.XMLFILE, xmlFileNode);
                    convert(ENodeCategoryName.CSVFILE, flatFileNode);
                } else if (pp == reportsNode) {
                    convert(ENodeCategoryName.REPORTS, reportsNode);
                } else if (pp == scriptsNode) {
                    convert(ENodeCategoryName.SCRIPTS, scriptsNode);
                } else if (pp == blockNode) {
                    convert(ENodeCategoryName.BLOCKS, blockNode);
                }
                pp.setInitialized(true);
            } catch (Exception e) {
                ExceptionOperation.operate(e);
            }
        }
        return pp.getChildren().toArray(new WarehouseNode[0]);
    }

    /**
     * Qiang.Zhang.Adolf@gmail.com Comment method "convert".
     * 
     * @param categoryName
     * @param parentNode
     */
    private void convert(ENodeCategoryName categoryName, WarehouseNode parentNode) {
        IPath warehousePath = parentNode.getWarehousePath();
        List<Object> objectsFromEMF = WarehouseResourceFactory.getObjectFromEMF(categoryName, warehousePath);
        for (Object object : objectsFromEMF) {
            if (object instanceof IFolder) {
                IFolder folder = ((IFolder) object);
                if (!folder.getName().equalsIgnoreCase(ENodeCategoryName.BIN.getName())) {
                    FolderWarehouseNode folderNode = new FolderWarehouseNode(parentNode, folder.getName());
                    folderNode.setCategoryName(categoryName);
                    parentNode.getChildren().add(folderNode);
                    convert(categoryName, folderNode);
                }
            }
        }
        addWarehouseNode(categoryName, warehousePath, parentNode);
    }

    /**
     * Qiang.Zhang.Adolf@gmail.com Comment method "addWarehouseNode".
     * 
     * @param categoryName
     * @param warehousePath
     * @param parentNode
     */
    private void addWarehouseNode(ENodeCategoryName categoryName, IPath warehousePath, WarehouseNode parentNode) {
        List<IWarehouseObject> objectsFromEMF = viewFactory.getObjectsFromEMF(categoryName, warehousePath);
        for (IWarehouseObject object : objectsFromEMF) {
            ENodeCategoryName categoryName2 = object.getCategoryName();
            switch(categoryName2) {
                case XMLFILE:
                case CSVFILE:
                case EXCELFILE:
                case DATABASE:
                    DatabaseDiagram db = (DatabaseDiagram) object.getData();
                    WarehouseNode child = null;
                    if (ENodeStatus.DELETED.getId().equals(db.getStatus())) {
                        child = new WarehouseNode(object, binNode, ENavNodeType.ELEMENT);
                        child.setBin(true);
                        int value = db.getEmfType().getValue();
                        switch(value) {
                            case EMFDataType.QUERY_VALUE:
                                child.setCategoryName(ENodeCategoryName.QUERY);
                                break;
                            default:
                                child.setCategoryName(categoryName2);
                                break;
                        }
                        child.setNodeType(ENavNodeType.ELEMENT);
                        child.setLabel(db.getName());
                        binNode.getChildren().add(child);
                    } else {
                        child = new WarehouseNode(object, parentNode, ENavNodeType.ELEMENT);
                        child.setLabel(db.getName());
                        int value = db.getEmfType().getValue();
                        ENodeCategoryName cName = categoryName2;
                        switch(value) {
                            case EMFDataType.QUERY_VALUE:
                                cName = ENodeCategoryName.QUERY;
                                warehouseQueries.put(WarehouseLabelUtil.getWarehouseNode(warehousePath, db.getName(), null), db);
                                break;
                        }
                        child.setCategoryName(cName);
                        if (GEFEditorUtils.isNodeActived(db.getName(), categoryName)) {
                            child.setNodeStatus(ENodeStatus.ACTIVED);
                        }
                        child.setNodeType(ENavNodeType.ELEMENT);
                        parentNode.getChildren().add(child);
                        ConnPropertySetType connSetType = MetadataHelper.getConnSetType(db);
                        CDCWarehouseNode cdcNode = null;
                        if (connSetType != null) {
                            CDCConnType cdcConn = connSetType.getCdcConn();
                            if (cdcConn != null) {
                                cdcNode = new CDCWarehouseNode(child, cdcConn.getName());
                                cdcNode.setCategoryName(categoryName);
                                child.getChildren().add(cdcNode);
                                addWarehouseCDCTables(cdcNode, cdcConn);
                            }
                        }
                        addWarehouseTables(warehousePath, db, child);
                    }
                    break;
                case BLOCKS:
                    ProcessType processType = (ProcessType) object.getData();
                    Map<String, Set<String>> blockRefs = WarehouseLabelUtil.getBlockRefs();
                    Set<String> list = blockRefs.get(processType.getUuid());
                    WarehouseNode addStorageNode = addStorageNode(categoryName, parentNode, object);
                    if (list != null) {
                        for (String string : list) {
                            BlockRefWarehouseNode node = new BlockRefWarehouseNode(addStorageNode, string);
                            node.setCategoryName(categoryName);
                            addStorageNode.getChildren().add(node);
                        }
                    }
                    break;
                default:
                    addStorageNode(categoryName, parentNode, object);
                    break;
            }
        }
    }

    /**
     * Qiang.Zhang.Adolf@gmail.com Comment method "addWarehouseCDCTables".
     * 
     * @param cdcNode
     * @param table
     * @param cdcConn
     */
    private void addWarehouseCDCTables(CDCWarehouseNode cdcNode, CDCConnType cdcConn) {
        String sbsTable = cdcConn.getSbsTable();
        EList<MetadataType> cdcTables = cdcConn.getCdcTables();
        for (MetadataType mtype : cdcTables) {
            if (sbsTable.equals(mtype.getName())) {
                addDbChildrenNode(ENodeCategoryName.CDC_TSYS, cdcNode, mtype);
            } else {
                addDbChildrenNode(ENodeCategoryName.TABLE, cdcNode, mtype);
            }
        }
    }

    /**
     * Qiang.Zhang.Adolf@gmail.com Comment method "addChildNode".
     * 
     * @param categoryName
     * @param parentNode
     * @param object
     */
    private WarehouseNode addStorageNode(ENodeCategoryName categoryName, WarehouseNode parentNode, IWarehouseObject object) {
        Storage processType = (Storage) object.getData();
        WarehouseNode child = null;
        if (ENodeStatus.DELETED.getId().equals(processType.getStatus())) {
            child = new WarehouseNode(object, binNode, ENavNodeType.ELEMENT);
            child.setBin(true);
            child.setCategoryName(categoryName);
            child.setNodeType(ENavNodeType.ELEMENT);
            child.setLabel(processType.getName());
            binNode.getChildren().add(child);
        } else {
            child = new WarehouseNode(object, parentNode, ENavNodeType.ELEMENT);
            child.setLabel(processType.getName());
            child.setCategoryName(categoryName);
            child.setNodeType(ENavNodeType.ELEMENT);
            if (GEFEditorUtils.isNodeActived(processType.getName(), categoryName)) {
                child.setNodeStatus(ENodeStatus.ACTIVED);
            }
            parentNode.getChildren().add(child);
        }
        return child;
    }

    /**
     * Qiang.Zhang.Adolf@gmail.com Comment method "addWarehouseTables".
     * 
     * @param warehousePath
     * 
     * @param db
     * @param dbNode
     */
    private void addWarehouseTables(IPath warehousePath, DatabaseDiagram db, WarehouseNode dbNode) {
        EList dbChildren = db.getDbChildren();
        for (Object object : dbChildren) {
            if (object instanceof DBTable) {
                MetadataTable metadataTable = MetadataHelper.convertMetadataTable((DBTable) object);
                warehouseTables.put(WarehouseLabelUtil.getWarehouseNode(warehousePath, db.getName(), metadataTable.getTableName()), metadataTable);
                addDbChildrenNode(ENodeCategoryName.TABLE, dbNode, object);
            }
        }
    }

    /**
     * Qiang.Zhang.Adolf@gmail.com Comment method "addDbChildrenNode".
     * 
     * @param table
     * @param dbNode2
     * @param object
     */
    private void addDbChildrenNode(ENodeCategoryName table, WarehouseNode dbNode2, Object object) {
        WarehouseObject object2 = new WarehouseObject(object);
        WarehouseNode child = new WarehouseNode(object2, dbNode2, ENavNodeType.ELEMENT);
        child.setCategoryName(table);
        child.setNodeType(ENavNodeType.ELEMENT);
        String name = null;
        if (object instanceof DBTable || object instanceof MetadataType) {
            if (object instanceof DBTable) {
                DBTable db = (DBTable) object;
                name = db.getName();
                EList<DBColumn> columns = db.getColumns();
                for (DBColumn column : columns) {
                    addDbChildrenNode(ENodeCategoryName.COLUMN, child, column);
                }
            } else if (object instanceof MetadataType) {
                MetadataType db = (MetadataType) object;
                name = db.getName();
                EList<ColumnType> column = db.getColumn();
                for (ColumnType columnType : column) {
                    addDbChildrenNode(ENodeCategoryName.COLUMN, child, columnType);
                }
            }
        } else if (object instanceof DBColumn || object instanceof ColumnType) {
            if (object instanceof DBColumn) {
                name = ((DBColumn) object).getName();
            } else if (object instanceof ColumnType) {
                name = ((ColumnType) object).getName();
            }
        }
        child.setLabel(name);
        dbNode2.getChildren().add(child);
    }

    /**
     * Qiang.Zhang.Adolf@gmail.com Comment method "initialize".
     */
    private void initialize() {
        DenguesResourceManager.resetResourceSet();
        List<IWarehouseNode> nodes = root.getChildren();
        binNode = new WarehouseNode(null, root, ENavNodeType.SYSTEM_FOLDER);
        binNode.setCategoryName(ENodeCategoryName.BIN);
        binNode.setImage(ImageUtil.getImage(IDenguesSharedImage.VIEW_TRASH));
        binNode.setDescription(Messages.getString("WarehouseViewProvider.nodeDesc", ENodeCategoryName.BIN.getDisplayName()));
        binNode.setBin(true);
        nodes.add(binNode);
        processNode = new WarehouseNode(null, root, ENavNodeType.SYSTEM_FOLDER);
        processNode.setCategoryName(ENodeCategoryName.PROCESS);
        processNode.setImage(ImageUtil.getImage(IDenguesSharedImage.VIEW_PROCESS));
        processNode.setDescription(Messages.getString("WarehouseViewProvider.nodeDesc", ENodeCategoryName.PROCESS.getDisplayName()));
        nodes.add(processNode);
        blockNode = new WarehouseNode(null, root, ENavNodeType.SYSTEM_FOLDER);
        blockNode.setCategoryName(ENodeCategoryName.BLOCKS);
        blockNode.setImage(ImageUtil.getImage(IDenguesSharedImage.VIEW_PROCESS));
        blockNode.setDescription(Messages.getString("WarehouseViewProvider.nodeDesc", ENodeCategoryName.BLOCKS.getDisplayName()));
        nodes.add(blockNode);
        dataSourceNode = new WarehouseNode(null, root, ENavNodeType.SYSTEM_FOLDER);
        dataSourceNode.setCategoryName(ENodeCategoryName.DATASOURCE);
        dataSourceNode.setDescription(Messages.getString("WarehouseViewProvider.nodeDesc", ENodeCategoryName.DATASOURCE.getDisplayName()));
        nodes.add(dataSourceNode);
        dbNode = new WarehouseNode(null, dataSourceNode, ENavNodeType.SYSTEM_FOLDER);
        dbNode.setCategoryName(ENodeCategoryName.DATABASE);
        dbNode.setImage(ImageUtil.getImage(IDenguesSharedImage.VIEW_DATA));
        dbNode.setDescription(Messages.getString("WarehouseViewProvider.nodeDesc", ENodeCategoryName.DATABASE.getDisplayName()));
        dataSourceNode.getChildren().add(dbNode);
        flatFileNode = new WarehouseNode(null, dataSourceNode, ENavNodeType.SYSTEM_FOLDER);
        flatFileNode.setCategoryName(ENodeCategoryName.CSVFILE);
        flatFileNode.setImage(ImageUtil.getImage(IDenguesSharedImage.QUERSTION));
        flatFileNode.setDescription(Messages.getString("WarehouseViewProvider.nodeDesc", ENodeCategoryName.CSVFILE.getDisplayName()));
        dataSourceNode.getChildren().add(flatFileNode);
        xmlFileNode = new WarehouseNode(null, dataSourceNode, ENavNodeType.SYSTEM_FOLDER);
        xmlFileNode.setCategoryName(ENodeCategoryName.XMLFILE);
        xmlFileNode.setImage(ImageUtil.getImage(IDenguesSharedImage.QUERSTION));
        xmlFileNode.setDescription(Messages.getString("WarehouseViewProvider.nodeDesc", ENodeCategoryName.XMLFILE.getDisplayName()));
        dataSourceNode.getChildren().add(xmlFileNode);
        excelFileNode = new WarehouseNode(null, dataSourceNode, ENavNodeType.SYSTEM_FOLDER);
        excelFileNode.setCategoryName(ENodeCategoryName.EXCELFILE);
        excelFileNode.setImage(ImageUtil.getImage(IDenguesSharedImage.QUERSTION));
        excelFileNode.setDescription(Messages.getString("WarehouseViewProvider.nodeDesc", ENodeCategoryName.EXCELFILE.getDisplayName()));
        dataSourceNode.getChildren().add(excelFileNode);
        if (DBSChecker.loadedDBSReport()) {
            reportsNode = new WarehouseNode(null, root, ENavNodeType.SYSTEM_FOLDER);
            reportsNode.setCategoryName(ENodeCategoryName.REPORTS);
            reportsNode.setImage(ImageUtil.getImage(IDenguesSharedImage.REPORTS_REPORTS));
            reportsNode.setDescription(Messages.getString("WarehouseViewProvider.nodeDesc", ENodeCategoryName.REPORTS.getDisplayName()));
            nodes.add(reportsNode);
        }
        scriptsNode = new WarehouseNode(null, root, ENavNodeType.SYSTEM_FOLDER);
        scriptsNode.setCategoryName(ENodeCategoryName.SCRIPTS);
        scriptsNode.setImage(ImageUtil.getImage(IDenguesSharedImage.PALETTE_CODE));
        scriptsNode.setDescription(Messages.getString("WarehouseViewProvider.nodeDesc", ENodeCategoryName.SCRIPTS.getDisplayName()));
        nodes.add(scriptsNode);
    }

    public boolean hasChildren(Object parent) {
        return getChildren(parent).length > 0;
    }

    @Override
    public String getText(Object obj) {
        if (obj instanceof WarehouseNode) {
            return ((WarehouseNode) obj).getLabel();
        }
        return obj.toString();
    }

    @Override
    public Image getImage(Object obj) {
        Image image = null;
        if (obj instanceof WarehouseNode) {
            WarehouseNode warehouseNode = ((WarehouseNode) obj);
            if (warehouseNode.getNodeType().equals(ENavNodeType.SIMPLE_FOLDER)) {
                if (view.isExpanded(warehouseNode)) {
                    return ImageUtil.getImage(IDenguesSharedImage.WAREHOUSE_FOLDER_OPEN);
                }
                return ImageUtil.getImage(IDenguesSharedImage.WAREHOUSE_FOLDER_CLOSE);
            } else if (warehouseNode.getNodeType().equals(ENavNodeType.CDC)) {
                image = ImageUtil.getImage(IDenguesSharedImage.WAREHOUSE_CDC_CONN);
                DecorationOverlayIcon icon = new DecorationOverlayIcon(image, ImageUtil.getDescriptor(IDenguesSharedImage.WAREHOUSE_SHORTCUT), IDecoration.BOTTOM_LEFT);
                image = icon.createImage();
                return image;
            } else if (warehouseNode.getNodeType().equals(ENavNodeType.BLOCK_REF)) {
                image = ImageUtil.getImage(IDenguesSharedImage.VIEW_PROCESS);
                DecorationOverlayIcon icon = new DecorationOverlayIcon(image, ImageUtil.getDescriptor(IDenguesSharedImage.WAREHOUSE_SHORTCUT), IDecoration.BOTTOM_LEFT);
                image = icon.createImage();
                return image;
            }
            image = WarehouseLabelUtil.getNodeIcon(obj);
            if (image != null) {
                return getOverlayIcon(warehouseNode, image);
            }
        }
        return null;
    }

    private final ImageDescriptor overlayImage = ImageUtil.getDescriptor(IDenguesSharedImage.VIEW_LOCKED);

    /**
     * Qiang.Zhang.Adolf@gmail.com Comment method "getOverlayIcon".
     * 
     * @param object
     * @param baseImage
     * @return
     */
    private Image getOverlayIcon(WarehouseNode node, Image baseImage) {
        Image image = baseImage;
        if (node != null) {
            ENodeStatus data2 = node.getNodeStatus();
            String status = data2.getId();
            if (status.equals(ENodeStatus.ACTIVED.getId())) {
                DecorationOverlayIcon icon = new DecorationOverlayIcon(baseImage, overlayImage, IDecoration.TOP_RIGHT);
                image = icon.createImage();
            }
            IWarehouseObject object = node.getObject();
            if (object != null) {
                Object data = object.getData();
                if (data instanceof DBTable) {
                    boolean sycdb = ((DBTable) data).isSycdb();
                    if (sycdb) {
                        DecorationOverlayIcon icon = new DecorationOverlayIcon(image, ImageUtil.getDescriptor(IDenguesSharedImage.TABLE_SYNC), IDecoration.TOP_LEFT);
                        image = icon.createImage();
                    }
                    boolean iscdc = ((DBTable) data).isActivateCdc();
                    if (iscdc) {
                        DecorationOverlayIcon icon = new DecorationOverlayIcon(image, ImageUtil.getDescriptor(IDenguesSharedImage.TABLE_ACTIVECDC), IDecoration.BOTTOM_LEFT);
                        image = icon.createImage();
                    }
                }
            }
        }
        return image;
    }

    /**
     * Qiang.Zhang.Adolf@gmail.com Comment method "getWarehouseTables".
     * 
     * @return
     */
    public static Map<String, MetadataTable> getWarehouseTables() {
        return warehouseTables;
    }

    /**
     * Qiang.Zhang.Adolf@gmail.com Comment method "getWarehouseQueries".
     * 
     * @return
     */
    public static Map<String, DatabaseDiagram> getWarehouseQueries() {
        return warehouseQueries;
    }

    public Font getFont(Object element) {
        if (element instanceof WarehouseNode) {
            WarehouseNode node = (WarehouseNode) element;
            if (node.getNodeType().equals(ENavNodeType.SYSTEM_FOLDER)) {
                Font systemFont = Display.getDefault().getSystemFont();
                Font font = new Font(null, systemFont.getFontData()[0].getName(), 9, SWT.BOLD);
                return font;
            }
        }
        return null;
    }

    /**
     * Getter for processNode.
     * 
     * @return the processNode
     */
    public static WarehouseNode getProcessNode() {
        return processNode;
    }

    /**
     * Getter for dbNode.
     * 
     * @return the dbNode
     */
    public static WarehouseNode getDbNode() {
        return dbNode;
    }

    /**
     * Getter for xmlFileNode.
     * 
     * @return the xmlFileNode
     */
    public static WarehouseNode getXmlFileNode() {
        return xmlFileNode;
    }

    /**
     * Getter for flatFileNode.
     * 
     * @return the flatFileNode
     */
    public static WarehouseNode getFlatFileNode() {
        return flatFileNode;
    }

    /**
     * Getter for excelFileNode.
     * 
     * @return the excelFileNode
     */
    public static WarehouseNode getExcelFileNode() {
        return excelFileNode;
    }
}
