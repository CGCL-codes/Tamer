package com.ingenta.clownbike;

import com.ingenta.clownbike.odb.Attribute;
import com.ingenta.clownbike.odb.FreeContent;
import com.ingenta.clownbike.odb.FreeContentManager;
import com.ingenta.odb.OdbException;
import java.util.List;

public class EditFreeContentTask extends EditTask {

    public void defineAttributes(DatabaseTransaction transaction) throws DatabaseException, TaskException {
        try {
            Attribute attribute;
            attribute = new TextAttribute("path", "/html/");
            transaction.create(attribute);
            addAttribute(transaction, attribute);
            attribute = new TextAttribute("title", "");
            transaction.create(attribute);
            addAttribute(transaction, attribute);
            attribute = new TextAttribute("trail", "");
            transaction.create(attribute);
            addAttribute(transaction, attribute);
            attribute = new TextAttribute("content", "<p>\n</p>");
            transaction.create(attribute);
            addAttribute(transaction, attribute);
        } catch (OdbException e) {
            throw new DatabaseException("unable to add attribute", e);
        }
    }

    public void updateAttributes(DatabaseTransaction transaction, Object object) throws DatabaseException, TaskException {
        FreeContent freeContent = (FreeContent) object;
        TextAttribute path = (TextAttribute) getAttribute("path");
        path.setValue(freeContent.getPath());
        transaction.update(path);
        TextAttribute title = (TextAttribute) getAttribute("title");
        title.setValue(freeContent.getTitle());
        transaction.update(title);
        TextAttribute trail = (TextAttribute) getAttribute("trail");
        trail.setValue(freeContent.getTrail());
        transaction.update(trail);
        TextAttribute content = (TextAttribute) getAttribute("content");
        content.setValue(freeContent.getContent());
        transaction.update(content);
    }

    public void updateObject(DatabaseTransaction transaction, Object object) throws DatabaseException, TaskException {
        FreeContent freeContent = (FreeContent) object;
        TextAttribute path = (TextAttribute) getAttribute("path");
        freeContent.setPath(path.getValue());
        TextAttribute title = (TextAttribute) getAttribute("title");
        freeContent.setTitle(title.getValue());
        TextAttribute trail = (TextAttribute) getAttribute("trail");
        freeContent.setTrail(trail.getValue());
        TextAttribute content = (TextAttribute) getAttribute("content");
        freeContent.setContent(content.getValue());
        freeContent.setUpdatedOn(transaction.getNow());
        transaction.update(freeContent, true);
    }

    public List findAll(DatabaseTransaction transaction) throws DatabaseException, TaskException {
        try {
            FreeContentManager manager = (FreeContentManager) transaction.getManager(FreeContent.class);
            List items = manager.findAll(transaction);
            return items;
        } catch (OdbException e) {
            throw new DatabaseException("unable to find items", e);
        }
    }

    public Object findById(DatabaseTransaction transaction, Integer id) throws DatabaseException, TaskException {
        try {
            FreeContentManager manager = (FreeContentManager) transaction.getManager(FreeContent.class);
            Object item = manager.load(transaction, id);
            return item;
        } catch (OdbException e) {
            throw new DatabaseException("unable to find item", new Object[] { id }, e);
        }
    }
}
