package jframe.service;

import java.util.List;
import jframe.common.PageModel;
import jframe.entity.bean.Department;

/**
 * @描述:<p>部门，组织机构Service </p>
 *
 * @作者: 叶平平(yepp)
 *
 * @时间: 2012-2-25 上午12:35:46
 */
public interface DepartmentService {

    /**
	 * @描述:<p>保存部门，组织机构  </p>
	 *
	 * @作者:  叶平平(yepp)
	 *
	 * @时间:  2012-2-25 上午12:53:15
	 *
	 * @修改历史: <p>修改时间、修改人、修改原因/说明</p>
	 *
	 * @param department
	 * @return
	 */
    public Department saveDepartment(Department department);

    /**
	 * @描述:<p>查询部门，组织机构树</p>
	 *
	 * @作者:  叶平平(yepp)
	 *
	 * @时间:  2012-2-25 上午12:54:35
	 *
	 * @修改历史: <p>修改时间、修改人、修改原因/说明</p>
	 *
	 * @return
	 */
    @SuppressWarnings("unchecked")
    public List queryDepartmentByParentNo(String parentNo);

    /**
	 * @描述:<p>查询部门，组织机构列表  </p>
	 *
	 * @作者:  叶平平(yepp)
	 *
	 * @时间:  2012-2-25 下午02:59:53
	 *
	 * @修改历史: <p>修改时间、修改人、修改原因/说明</p>
	 *
	 * @param parentNo
	 * @return
	 */
    public PageModel queryDepartment(String parentNo);
}
