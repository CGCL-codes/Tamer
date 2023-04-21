package com.bo;

import java.util.ArrayList;
import com.bean.ArticleBean;
import com.bean.BeanInterface;
import com.bean.ColumnBean;
import com.bean.UserinfoBean;

public interface BeanDao {

    /**
	 * 通过某字段获取对应的记录的实体
	 * @param filed 字段
	 * @param valuse 字段值
	 * @param bi 实体类
	 * @return 返回找到查询的结果
	 */
    public void getById(String field, String value, BeanInterface bi);

    /**
	 * 返回对以表的所有记录
	 * @param bi
	 * @return
	 */
    public ArrayList getAll(BeanInterface bi);

    /**
	 * 返回所有记录
	 * @param ab 实体类
	 * @return  返回所有实体类的ArrayList
	 */
    @SuppressWarnings("unchecked")
    public ArrayList getAll(String filed, String value, BeanInterface ab);

    /**
	 * 获得指定页数和大小的记录
	 * @param pageNow
	 * @param pageSize
	 * @param bi
	 * @return
	 */
    public ArrayList getAllByPage(int pageNow, int pageSize, BeanInterface bi);

    /**
	 * 根据当前页码和分页大小返回相应记录
	 * @param ab 实体类
	 * @return  返回所有实体类的ArrayList
	 */
    @SuppressWarnings("unchecked")
    public ArrayList getAllByPage(int pageNow, int pageSize, String filed, String value, BeanInterface ab);

    /**
	 * 获得页数(不带参数)
	 * @param table 数据表名
	 * @return 总页数
	 */
    public int getPageCount(int pageSize, String table);

    /**
	 * 获得总页数
	 * @param table 数据表名
	 * @return 总页数
	 */
    public int getPageCount(int pageSize, String filed, String value, String table);

    /**
	 * 通过id号删除对应的记录
	 * @param table 数据表名
	 * @param id id号
	 * @return 返回收影响的条数
	 */
    public int deleteById(String table, String filed, String id);

    /**
	 * 实现article的增加方法
	 * @param ab article对应实体类
	 * @return 返回收影响的条数
	 */
    public int addArticle(ArticleBean ab);

    /**
	 * 实现article的增加方法
	 * @param ab article对应实体类
	 * @return 返回收影响的条数
	 */
    public int updateArticle(ArticleBean ab);

    /**
	 * 通过用户名和密码返回用户实体类
	 * @param ub
	 * @return
	 */
    public UserinfoBean findByUnameAndUpss(String Uname, String Upass);

    /**
	 * 增加栏目
	 * @param cb 栏目实体类
	 * @return 返回收影响的条数
	 */
    public int addColumn(ColumnBean cb);

    /**
	 * 修改栏目
	 * @param cb
	 * @return
	 */
    public int modifyColumn(ColumnBean cb);

    /**
	 * 添加用户
	 * @param ub
	 * @return
	 */
    public int AddUser(UserinfoBean ub);

    /**
	 * 修改用户
	 * @param ub
	 * @return
	 */
    public int ModifyUser(UserinfoBean ub);
}
