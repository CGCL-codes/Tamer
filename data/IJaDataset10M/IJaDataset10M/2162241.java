package kr.or.javacafe.code.bo;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import kr.or.javacafe.code.dao.CodeCategoryDAO;
import kr.or.javacafe.code.domain.*;
import kr.or.javacafe.member.dao.*;
import kr.or.javacafe.member.domain.RoleGrp;

@Service
public class CodeCategoryBOImpl implements CodeCategoryBO {

    @Autowired
    private MemberDAO memberDAO;

    @Autowired
    private CodeCategoryDAO codeCategoryDAO;

    @Override
    public List<ComboType> listRoleGrpForCombo() {
        List<ComboType> objList = new ArrayList<ComboType>();
        List<RoleGrp> objTempList = memberDAO.selectRoleGrpNmList();
        for (RoleGrp objTemp : objTempList) {
            ComboType objCombo = new ComboType();
            objCombo.setCode(objTemp.getRoleGrpId());
            objCombo.setName(objTemp.getRoleGrpId() + " (" + objTemp.getRoleGrpNm() + ")");
            objList.add(objCombo);
        }
        return objList;
    }

    @Override
    public List<CodeCategory> listCodeCategory(String strCodeId) {
        List<CodeCategory> objGlobalList = new ArrayList();
        CodeCategory objCode = new CodeCategory();
        objCode.setCdCatId(strCodeId);
        List<CodeCategory> objTempList = codeCategoryDAO.selectCodeCategory(objCode);
        objGlobalList = objTempList;
        return objGlobalList;
    }

    @Override
    public List<ComboType> listCodeCategoryForCombo(String strCodeId) {
        List<CodeCategory> objGlobalList = new ArrayList();
        CodeCategory objCode = new CodeCategory();
        objCode.setCdCatId(strCodeId);
        List<CodeCategory> objTempList = codeCategoryDAO.selectCodeCategory(objCode);
        objGlobalList = objTempList;
        List<ComboType> objList = new ArrayList<ComboType>();
        for (CodeCategory objTemp : objGlobalList) {
            ComboType objCombo = new ComboType();
            objCombo.setCode(objTemp.getCdId());
            objCombo.setName(objTemp.getCdNm());
            objList.add(objCombo);
        }
        return objList;
    }
}
