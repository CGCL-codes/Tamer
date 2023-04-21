package kr.or.javacafe.code.dao;

import java.util.List;
import kr.or.javacafe.code.domain.CodeCategory;

public interface CodeCategoryDAO {

    public List<CodeCategory> selectCodeCategory(CodeCategory objCodeCategory);
}
