package com.sitechasia.webx.example.category.service;

import java.util.List;
import com.sitechasia.webx.core.service.IValueObjectBaseService;
import com.sitechasia.webx.core.support.Page;
import com.sitechasia.webx.example.category.vo.CategoryVo;

/**
 * @author Administrator
 *
 */
public interface CategoryIService extends IValueObjectBaseService<CategoryVo> {

    public Page searchCategorys(int pageNo, int pageSize, String sortStr, Object... params);
}
