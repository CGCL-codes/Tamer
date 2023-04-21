package br.ufmg.saotome.arangi.controller.logic;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import br.ufmg.saotome.arangi.controller.bean.CRUDLogicBean;
import br.ufmg.saotome.arangi.dto.PermissionDeniedBean;

@ManagedBean
@CustomScoped(value = "#{conversation}")
public class PermissionDenied extends CRUDLogicBean<PermissionDeniedBean> {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public PermissionDenied() {
        setDtoBeanName("permissionDeniedBean");
        setDtoClassName("br.ufmg.saotome.arangi.dto.PermissionDeniedBean");
        setControllerBeanName("permissionDeniedController");
        setDefaultAction(ACTION_NEW);
    }
}
