package com.asoft.common.sysframe.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;
import com.asoft.common.base.model.BaseObject;
import com.asoft.common.base.util.BaseObjectPriAscComparator;
import com.asoft.common.viewframe.define.ColumnType;
import com.asoft.common.viewframe.model.Area;
import com.asoft.common.viewframe.model.Column;
import com.asoft.common.viewframe.model.Home;

/**
 * <p>Title: 单元 </p>
 * <p>Description: 机构 Org </p>
 * <p>Copyright: Copyright (c) 2004-2006</p>
 * <p>Company: asoft</p>
 * @ $Author: amon.lei $
 * @ $Date: 2007-2-20 $
 * @ $Revision: 1.0 $
 * @ created in 2007-2-20
 *
 */
public abstract class Unit extends BaseObject implements Serializable {

    static Logger logger = Logger.getLogger(Unit.class);

    private Set homes;

    private Set areas;

    private Set columns;

    /**
         * @return 返回 areas。
         */
    public Set getAreas() {
        return areas;
    }

    /**
         * @param areas 要设置的 areas。
         */
    public void setAreas(Set areas) {
        this.areas = areas;
    }

    public void addArea(Area area) {
        if (this.areas == null) {
            this.areas = new LinkedHashSet();
        }
        this.areas.add(area);
    }

    /**
         * @return 返回 columns。
         */
    public Set getColumns() {
        return columns;
    }

    /**
         * @param columns 要设置的 columns。
         */
    public void setColumns(Set columns) {
        this.columns = columns;
    }

    public void addColumn(Column column) {
        if (this.columns == null) {
            this.columns = new LinkedHashSet();
        }
        this.columns.add(column);
    }

    public Set getColumn2s() {
        Set c2s = new LinkedHashSet();
        Object[] cs = this.columns.toArray();
        for (int i = 0; i < cs.length; i++) {
            Column c = (Column) cs[i];
            if (c.getType() == ColumnType.TWO_LEVEL) {
                c2s.add(c);
            }
        }
        return c2s;
    }

    public Set getColumn1s() {
        Set c1s = new LinkedHashSet();
        Object[] cs = this.columns.toArray();
        for (int i = 0; i < cs.length; i++) {
            Column c = (Column) cs[i];
            if (c.getType() == ColumnType.ONE_LEVEL) {
                c1s.add(c);
            }
        }
        return c1s;
    }

    /**
         * @return 返回 homes。
         */
    public Set getHomes() {
        return homes;
    }

    /**
         * @param homes 要设置的 homes。
         */
    public void setHomes(Set homes) {
        this.homes = homes;
    }

    public void addHome(Home home) {
        if (this.homes == null) {
            this.homes = new LinkedHashSet();
        }
        this.homes.add(home);
    }

    public abstract List getUnits2Root();

    public List getAuthorizedHomes(boolean isSort) {
        Set adHomes = new LinkedHashSet();
        List parentUnits = this.getUnits2Root();
        for (int i = 0; i < parentUnits.size(); i++) {
            Unit unit = (Unit) parentUnits.get(i);
            adHomes.addAll(unit.getHomes());
        }
        List ahs = new ArrayList(adHomes);
        if (isSort) {
            Collections.sort(ahs, new BaseObjectPriAscComparator());
        }
        return ahs;
    }

    public List getAuthorizedAreas(boolean isSort) {
        Set adAreas = new LinkedHashSet();
        List parentUnits = this.getUnits2Root();
        for (int i = 0; i < parentUnits.size(); i++) {
            Unit unit = (Unit) parentUnits.get(i);
            adAreas.addAll(unit.getAreas());
        }
        List aas = new ArrayList(adAreas);
        if (isSort) {
            Collections.sort(aas, new BaseObjectPriAscComparator());
        }
        return aas;
    }

    public List getAuthorizedColumns(boolean isSort) {
        Set adColumns = new LinkedHashSet();
        List parentUnits = this.getUnits2Root();
        logger.debug(this.getClass().getName() + " 到根的长度:" + parentUnits.size());
        for (int i = 0; i < parentUnits.size(); i++) {
            Unit unit = (Unit) parentUnits.get(i);
            logger.debug(this.getClass().getName() + " 被授权栏目共" + unit.getColumn1s().size());
            adColumns.addAll(unit.getColumns());
        }
        List acs = new ArrayList(adColumns);
        if (isSort) {
            Collections.sort(acs, new BaseObjectPriAscComparator());
        }
        return acs;
    }
}
