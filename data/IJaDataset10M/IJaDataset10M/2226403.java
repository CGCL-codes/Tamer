package jcontrol.conect.gui.manufacturer;

import jcontrol.conect.gui.design.*;
import jcontrol.conect.data.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;

public class ProductSetsBox extends JInternalFrame {

    /** RsourceBundle for item. */
    protected static ResourceBundle resources = ManufacturerWorkSuite.resources;

    /** DesignBox for the InternalFrame. */
    public DesignBox box;

    /** Root ContainerItem. */
    public static ContainerItem manufacturerRootItem;

    /** Product ContainerItem. */
    protected ProductContainerItem productItem;

    /** Device LeafeItem. */
    protected CatalogEntryItem catalog;

    /**
     * Constructor.
     */
    public ProductSetsBox(ManufacturerToolDataManager datamanager) {
        super(resources.getString("box.prodSets.title"));
        this.manufacturerRootItem = new ContainerItem(Constants.MANUFACTURER_PRODUCT_SETS, true, false);
        manufacturerRootItem.setName(datamanager.data.getManufacturer().getManufacturerName());
        productItem = new ProductContainerItem(new DragDropItem(resources.getString("box.prod.container.prod"), resources.getString("box.prod.container.prod"), new String[] { resources.getString("box.prod.leaf.entry"), resources.getString("box.appl.container.appl"), resources.getString("box.appl.container.pei") }, Constants.deviceIcon), false, true);
        catalog = new CatalogEntryItem(new DragDropItem(resources.getString("box.prod.leaf.entry"), resources.getString("box.prod.leaf.entry"), new String[] {}, Constants.catalogEntryIcon));
        datamanager.setProductSetsBox(this);
        datamanager.setProductSetsRoot(manufacturerRootItem);
        DragDropItem[] items = new DragDropItem[] { manufacturerRootItem, productItem, catalog };
        ManufacturerTableHeaderDescriptor[] descriptors = new ManufacturerTableHeaderDescriptor[2];
        descriptors[0] = new ManufacturerTableHeaderDescriptor(Constants.PRODUCT_TABLE, productItem.getType());
        descriptors[1] = new ManufacturerTableHeaderDescriptor(Constants.CATALOG_ENTRY_TABLE, catalog.getType());
        box = new CommonManufacturerToolBox("product-sets-box", items, descriptors, datamanager, ManufacturerToolEvent.PRODUCT_SET);
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        getContentPane().add(box);
        pack();
    }

    public void updateLeafTable() {
        box.revalidateLeafTable();
    }

    public void initializeTreeFromRoot() {
        box.initializeTreeFromRoot();
    }

    public void setClosed(boolean b) {
        if (b) setVisible(false);
        fireInternalFrameEvent(InternalFrameEvent.INTERNAL_FRAME_CLOSED);
    }
}
