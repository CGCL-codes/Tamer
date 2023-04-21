package br.com.jnfe.connect.service;

import java.util.List;
import javax.annotation.Resource;
import org.helianto.core.Category;
import org.helianto.core.CategoryGroup;
import org.helianto.core.Unit;
import org.helianto.core.filter.CategoryFilterAdapter;
import org.helianto.core.filter.UnitFilterAdapter;
import org.helianto.core.service.CategoryMgr;
import org.helianto.core.service.UnitMgr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import br.com.jnfe.connect.adapter.UnitAdapter;

/**
 * Implemeta��o padr�o para <code>UnitConnectorMgr</code>.
 * 
 * @author mauriciofernandesdecastro
 */
@Component("unitConnectorMgr")
public class UnitConnectorMgrImpl implements UnitConnectorMgr {

    public Unit installOrUpdateUnit(UnitAdapter adapter) {
        Category category = new Category(adapter.getEntity(), CategoryGroup.UNIT, "UNIDADES");
        List<Category> categoryList = categoryMgr.findCategories(new CategoryFilterAdapter(category));
        if (categoryList.size() == 0) {
            category.setCategoryGroup(CategoryGroup.UNIT);
            category.setCategoryCode("UNIDADES");
        } else {
            category = categoryList.get(0);
            category.setCategoryGroup(CategoryGroup.UNIT);
            category.setCategoryCode("UNIDADES");
        }
        category.setCategoryName("UNIDADES");
        categoryMgr.storeCategory(category);
        Unit unit = new Unit(category.getEntity(), adapter.getCodigoUnidade());
        List<Unit> unitList = unitMgr.findUnits(new UnitFilterAdapter(unit));
        if (unitList.size() == 0) {
            unit.setEntity(category.getEntity());
            unit.setUnitCode(adapter.getCodigoUnidade());
        } else {
            unit = unitList.get(0);
            logger.debug("Encontrada a unidade: {}.", unit);
        }
        unit.setCategory(category);
        unit.setUnitName(adapter.getDescricaoUnidade());
        unitMgr.storeUnit(unit);
        return unit;
    }

    private CategoryMgr categoryMgr;

    private UnitMgr unitMgr;

    @Resource
    public void setCategoryMgr(CategoryMgr categoryMgr) {
        this.categoryMgr = categoryMgr;
    }

    @Resource
    public void setUnitMgr(UnitMgr unitMgr) {
        this.unitMgr = unitMgr;
    }

    private static final Logger logger = LoggerFactory.getLogger(UnitConnectorMgrImpl.class);
}
