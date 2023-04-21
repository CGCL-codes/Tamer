package org.jboke.framework.ejb;

import java.rmi.RemoteException;
import java.util.*;
import javax.ejb.*;
import java.rmi.*;
import javax.naming.*;
import javax.rmi.*;
import org.jboke.application.ejb.acting.*;

/**
 * Superclass of all Node-beans
 *
 * @author Kurt Huwig
 * @version $Id: NodeEntityBean.java,v 1.1 2001/05/04 13:29:02 kurti Exp $
 *
 * This file is part of the JBoKe-Project, see http://www.jboke.org/
 * (c) 2001 iKu Netzwerkl&ouml;sungen
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
public abstract class NodeEntityBean extends EntityAdapter {

    /**
   * ID (for primary key purpose only)
   */
    public Integer id;

    /**
   * Superordinated Node of this Node; 'null' means none
   */
    public NodeBusiness superNode;

    /**
   * Type of node
   */
    public String type;

    /**
   * Visible ID
   */
    public String visibleId;

    /**
   * Description
   */
    public String description;

    /**
   * Attribute; 'null' means none
   */
    public AttributeBusiness attribute;

    public NodeBusiness getSuperNode() {
        return superNode;
    }

    public String getType() {
        return type;
    }

    public String getVisibleId() {
        return visibleId;
    }

    public String getDescription() {
        return description;
    }

    public AttributeBusiness getAttribute() {
        return attribute;
    }

    public void setAttribute(AttributeBusiness attribute) {
        this.attribute = attribute;
    }

    /**
   * put fields into a Data-object
   */
    protected void storeFields(NodeData nd) {
        nd.superNode = superNode;
        nd.type = type;
        nd.visibleId = visibleId;
        nd.description = description;
    }

    public void ejbRemove() throws RemoveException {
        try {
            Collection col = ((NodeHome) ejbContext.getEJBHome()).findBySuperNode((NodeBusiness) ejbContext.getEJBObject());
            for (Iterator it = col.iterator(); it.hasNext(); ) {
                NodeBusiness nbChild = (NodeBusiness) it.next();
                AttributeBusiness attribute = nbChild.getAttribute();
                if (attribute != null) {
                    ((EJBObject) attribute).remove();
                } else {
                    ((EJBObject) nbChild).remove();
                }
            }
        } catch (Exception ne) {
            ne.printStackTrace();
            throw new RemoveException("Can't delete Child");
        }
    }
}
