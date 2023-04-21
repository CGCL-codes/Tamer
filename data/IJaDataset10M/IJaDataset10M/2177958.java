package org.objectstyle.cayenne.modeler.dialog.validator;

import javax.swing.JFrame;
import org.objectstyle.cayenne.access.DataDomain;
import org.objectstyle.cayenne.map.Attribute;
import org.objectstyle.cayenne.map.DataMap;
import org.objectstyle.cayenne.map.DbEntity;
import org.objectstyle.cayenne.map.Entity;
import org.objectstyle.cayenne.map.ObjEntity;
import org.objectstyle.cayenne.modeler.ProjectController;
import org.objectstyle.cayenne.modeler.event.AttributeDisplayEvent;
import org.objectstyle.cayenne.project.validator.ValidationInfo;

/**
 * Attribute validation message.
 * 
 * @author Misha Shengaout
 * @author Andrei Adamchik
 */
public class AttributeErrorMsg extends ValidationDisplayHandler {

    protected DataMap map;

    protected Entity entity;

    protected Attribute attribute;

    /**
     * Constructor for AttributeErrorMsg.
     * 
     * @param result
     */
    public AttributeErrorMsg(ValidationInfo result) {
        super(result);
        Object[] path = result.getPath().getPath();
        int len = path.length;
        if (len >= 1) {
            attribute = (Attribute) path[len - 1];
        }
        if (len >= 2) {
            entity = (Entity) path[len - 2];
        }
        if (len >= 3) {
            map = (DataMap) path[len - 3];
        }
        if (len >= 4) {
            domain = (DataDomain) path[len - 4];
        }
    }

    public void displayField(ProjectController mediator, JFrame frame) {
        AttributeDisplayEvent event = new AttributeDisplayEvent(frame, attribute, entity, map, domain);
        if (entity instanceof ObjEntity) {
            mediator.fireObjEntityDisplayEvent(event);
            mediator.fireObjAttributeDisplayEvent(event);
        } else if (entity instanceof DbEntity) {
            mediator.fireDbEntityDisplayEvent(event);
            mediator.fireDbAttributeDisplayEvent(event);
        }
    }
}
