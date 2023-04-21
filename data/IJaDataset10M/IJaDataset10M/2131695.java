package org.grassfield.common.view;

import java.util.List;
import org.grassfield.common.entity.Module;
import org.grassfield.common.service.IMenuListService;

/**
 * The Class MenuListAction.
 */
public class MenuListAction {

    private IMenuListService menuListService;

    private List<Module> moduleList;

    /**
	 * Gets the module list.
	 * 
	 * @return the module list
	 */
    public List<Module> getModuleList() {
        return moduleList;
    }

    /**
	 * Sets the module list.
	 * 
	 * @param moduleList the new module list
	 */
    public void setModuleList(List<Module> moduleList) {
        this.moduleList = moduleList;
    }

    /**
	 * Sets the menu list service.
	 * 
	 * @param menuListService the new menu list service
	 */
    public void setMenuListService(IMenuListService menuListService) {
        this.menuListService = menuListService;
    }

    /**
	 * Execute.
	 * 
	 * @return the string
	 */
    public String execute() {
        this.setModuleList(this.menuListService.getModuleList());
        return "SUCCESS";
    }
}
