package easyJ.database.dao.command;

import easyJ.common.EasyJException;
import easyJ.database.dao.AggregateType;
import easyJ.database.dao.OrderRule;

public interface SelectCommand extends FilterableCommand {

    /**
     * 为一个查询命令增加查询字段
     * 
     * @param column
     *                String 增加查询的字段，这里用的是属性名例如studentName而不是字段名student_name
     * @throws EasyJException
     */
    public void addSelectItem(String column) throws EasyJException;

    /**
     * 设置查询命令执行后取得满足数据的范围
     * 
     * @param start
     *                long 从第start条取数据
     * @param end
     *                long 到第end条数据。
     * @throws EasyJException
     */
    public void setLimits(long start, long end) throws EasyJException;

    /**
     * 为一个查询命令增加查询字段
     * 
     * @param column
     *                String 增加查询的字段，这里用的是属性名例如studentName而不是字段名student_name
     * @param type
     *                AggregateType 聚集类型，例如SUM, AVG,等
     * @throws EasyJException
     */
    public void addSelectItem(String column, AggregateType type) throws EasyJException;

    /**
     * 为一个查询增加排序条件，一个查询可以有多个排序条件，按照增加的顺序决定优先级
     * 
     * @param orderRule
     *                OrderRule
     * @throws EasyJException
     */
    public void addOrderRule(OrderRule orderRule) throws EasyJException;

    /**
     * 为一个查询增加条件
     * 
     * @param condition
     *                String 是一个where条件，条件里面用的是字段名例如student_name like '%liu%'
     */
    public void setCondition(String condition);

    /**
     * 得到查询命令对应的视图
     * 
     * @return String
     */
    public String getViewName();

    /**
     * 查询命令对应的类，即查询命令是从哪个类对应的表或视图中进行查询
     * 
     * @return Class
     */
    public Class getSelectClass();
}
