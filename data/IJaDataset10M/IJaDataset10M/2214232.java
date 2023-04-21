package kr.or.javacafe.code.bo;

import kr.or.javacafe.code.domain.*;
import java.util.*;

public interface CodeCategoryBO {

    public List<ComboType> listRoleGrpForCombo();

    public List<CodeCategory> listCodeCategory(String strCodeId);

    public List<ComboType> listCodeCategoryForCombo(String strCodeId);
}
