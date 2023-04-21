package de.pallo.joti.ow;

import de.pallo.joti.sys.SimpleLog;
import de.pallo.joti.sys.JotiConfig;
import de.pallo.joti.model.JotiModel;
import de.pallo.joti.model.ImageModel;
import java.util.List;
import java.util.Set;
import java.util.Collection;
import java.io.File;

/**
 * -- Copyright (C) 2004 M. Pallo <markus@pallo.de>
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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307,
 *
 *
 * $Revision: 1.1.1.1 $
 * Author: M. Pallo
 * last changed by $Author: baskote $
 */
public class ObjectWarehouse {

    private static ObjectWarehouse warehouse = null;

    protected ObjectWarehouse() {
    }

    public Collection getRootCategories() {
        return null;
    }

    public void add(Object baseObject) {
    }

    public void remove(Object baseObject) {
    }

    public BaseObject search(Integer id, Class c) {
        return null;
    }

    public Collection search(BaseObject baseObjectIn) {
        return null;
    }

    public Collection getImageByCategory(List inCategoryList) {
        return null;
    }

    public Collection getImageByArchive(ArchiveObject archive) {
        return null;
    }

    public Collection getImageWithoutCategory() {
        return null;
    }

    public void update(Object baseObject) {
    }

    public static synchronized ObjectWarehouse getInstance() {
        if (warehouse == null) {
            warehouse = createInstance();
        }
        return warehouse;
    }

    private static ObjectWarehouse createInstance() {
        String className = JotiConfig.getInstance().getObjectWarehouseClass();
        try {
            Class cl = Class.forName(className);
            ObjectWarehouse w = (ObjectWarehouse) cl.newInstance();
            return w;
        } catch (Exception e) {
            e.printStackTrace();
            SimpleLog.logFatal("warehouse not found", e);
            System.exit(0);
        }
        return null;
    }
}
