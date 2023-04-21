package com.companyname.common.basedata.valid;

import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import com.companyname.common.basedata.define.TypeParams;
import com.companyname.common.basedata.manager.TypeManager;
import com.companyname.common.basedata.model.Type;
import com.companyname.common.util.mvc.valid.UserValidator;
import com.companyname.common.util.mvc.valid.ValidatedResult;

/**
 * <p>Title: 用户自定义的修改校验器</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: companyname</p>
 * @ $Author: author $
 * @ $Date: 2005/02/16 01:04:14 $
 * @ $Revision: 1.7 $
 * @ created in 2005-11-14
 *
 */
public class UpdateTypeValidator extends UserValidator {

    static Logger logger = Logger.getLogger(UpdateTypeValidator.class);

    private TypeManager typeManager;

    /**
         * @param typeManager 要设置的 typeManager
         */
    public void setTypeManager(TypeManager typeManager) {
        this.typeManager = typeManager;
    }

    /**
         * 修改前的校验
         *
         * @ param: HttpServletRequest request
         * @ param: ValidatedResult vr 校验结果存放器
         * @ param: String[] params 配置参数
         */
    public void validing(HttpServletRequest request, ValidatedResult vr, String[] params) {
        Type type = (Type) this.validBaseObjectById(request, vr, typeManager, "id");
        Type superType = (Type) this.typeManager.get(request.getParameter("parentId"));
        if (superType == null) {
            superType = typeManager.getByCode(TypeParams.ROOT);
        }
        String code = this.validString(request, vr, "code", 1, 200);
        Type tempType = this.typeManager.getByCode(request.getParameter("code"));
        if (tempType != null && !tempType.getId().equals(type.getId())) {
            vr.setErrMess("code", "已经被使用");
        }
        String name = this.validString(request, vr, "name", 1, 50);
        String remark = this.validString(request, vr, "remark", 0, 1000);
        if (vr.isAllValidated()) {
            type.setCode(code);
            type.setName(name);
            type.setSuperType(superType);
            type.setRemark(remark);
            request.setAttribute(Type.class.getName(), type);
        }
    }
}
