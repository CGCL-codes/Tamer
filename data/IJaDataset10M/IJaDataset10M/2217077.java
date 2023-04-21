package com.xtreme.cis.ui;

import java.util.ArrayList;
import java.util.List;
import javax.faces.FacesException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.aigsi.sbs.core.ContextHelper;
import com.xtreme.cis.constant.Xtreme;
import com.xtreme.cis.dao.ItemDAO;
import com.xtreme.cis.model.Item;
import com.xtreme.cis.util.ServiceFinder;

public class UomBean {

    protected final Log logger = LogFactory.getLog(this.getClass());

    private List items;

    private String itemId;

    private String itemCode;

    private String description;

    private boolean editable;

    private boolean newUpdate;

    private int column;

    public UomBean() {
        items = new ArrayList();
    }

    public List getItems() {
        try {
            ItemDAO dao = (ItemDAO) ContextHelper.getInstance().getApplicationContext().getBean(Xtreme.ITEM_DAO);
            items = dao.listItem();
        } catch (Exception e) {
            String msg = "Could not initialize ItemBean";
            this.logger.error(msg, e);
            throw new FacesException(msg, e);
        }
        return items;
    }

    public String newItem() throws Exception {
        setItemId("");
        setItemCode("");
        setDescription("");
        setEditable(true);
        setNewUpdate(true);
        return "INVENTORY_ITEM";
    }

    public String list() throws Exception {
        return "INVENTORY_ITEM_LIST";
    }

    public String add() throws Exception {
        String status = "INVENTORY_ITEM";
        setEditable(false);
        setNewUpdate(true);
        ItemDAO dao = (ItemDAO) ContextHelper.getInstance().getApplicationContext().getBean(Xtreme.ITEM_DAO);
        if (dao.checkItem(getItemCode()) != null) {
        } else {
            Item item = new Item();
            item.setItemId(getItemId());
            item.setItemCode(getItemCode());
            item.setDescription(getDescription());
            try {
                dao.addItem(item);
            } catch (Exception e) {
                setEditable(true);
            }
        }
        return status;
    }

    public String save() throws Exception {
        String status = "INVENTORY_ITEM";
        setEditable(false);
        setNewUpdate(false);
        ItemDAO dao = (ItemDAO) ContextHelper.getInstance().getApplicationContext().getBean(Xtreme.ITEM_DAO);
        Item item = dao.findByItemId(getItemId());
        item.setItemCode(getItemCode());
        item.setDescription(getDescription());
        try {
            dao.saveItem(item);
        } catch (Exception e) {
            setEditable(true);
        }
        return status;
    }

    public String edit() throws Exception {
        String status = "INVENTORY_ITEM";
        setEditable(true);
        setNewUpdate(false);
        try {
            ItemDAO dao = (ItemDAO) ContextHelper.getInstance().getApplicationContext().getBean(Xtreme.ITEM_DAO);
            Item item = dao.findByItemId(getItemId());
            if (item != null) {
                this.setItemCode(item.getItemCode());
                this.setDescription(item.getDescription());
            }
        } catch (Exception e) {
            String msg = "Could not initialize ItemBean";
            this.logger.error(msg, e);
            throw new FacesException(msg, e);
        }
        return status;
    }

    public String view() throws Exception {
        String status = "INVENTORY_ITEM";
        setEditable(false);
        setNewUpdate(false);
        try {
            ItemDAO dao = (ItemDAO) ContextHelper.getInstance().getApplicationContext().getBean(Xtreme.ITEM_DAO);
            Item item = dao.findByItemId(getItemId());
            if (item != null) {
                this.setItemCode(item.getItemCode());
                this.setDescription(item.getDescription());
            }
        } catch (Exception e) {
            String msg = "Could not initialize ItemBean";
            this.logger.error(msg, e);
            throw new FacesException(msg, e);
        }
        return status;
    }

    public String delete() throws Exception {
        String status = "INVENTORY_ITEM_LIST";
        try {
            ItemDAO dao = (ItemDAO) ContextHelper.getInstance().getApplicationContext().getBean(Xtreme.ITEM_DAO);
            Item item = dao.findByItemId(getItemId());
            dao.deleteItem(item);
        } catch (Exception e) {
            String msg = "Could not initialize ItemBean";
            this.logger.error(msg, e);
            throw new FacesException(msg, e);
        }
        return status;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        if (editable) setColumn(3); else setColumn(2);
        this.editable = editable;
    }

    public boolean getNewUpdate() {
        return newUpdate;
    }

    public void setNewUpdate(boolean newUpdate) {
        this.newUpdate = newUpdate;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }
}
