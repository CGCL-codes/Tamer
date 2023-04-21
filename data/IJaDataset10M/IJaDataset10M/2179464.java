package org.fantasy.cpp.core.pojo;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;
import org.fantasy.cpp.core.annotation.Field;

/**
 * 查询类型
 * 
 * @author 王文成
 * @version 1.0
 * @since 2011-7-5
 */
@Table("QUERY_TYPE")
public class QueryType {

    @Id
    @Column("query_type_id")
    @Field(desc = "查询类型id", nullable = false)
    private Long queryTypeId;

    @Column("query_type_name")
    @Field(desc = "查询类型名称", nullable = false, maxlenth = 64)
    private String queryTypeName;

    @Column("query_type_desc")
    @Field(desc = "查询类型描述", nullable = false, maxlenth = 1024)
    private String queryTypeDesc;

    @Column("state")
    @Field(desc = "状态", nullable = false, maxlenth = 3)
    private String state;

    /**
	 * SQL格式
	 */
    public static final long SQL_TYPE = 1;

    /**
	 * 扩展SQL格式
	 */
    public static final long XSQL_TYPE = 4;

    /**
	 * JSON数据格式
	 */
    public static final long JSON_TYPE = 2;

    /**
	 * Java拼装SQL格式
	 */
    public static final long JAVA_TYPE = 3;

    /**
	 * SQL类型
	 * 
	 * @return
	 */
    public static boolean isSqlType(long type) {
        return type == QueryType.SQL_TYPE;
    }

    /**
	 * Java类型
	 * 
	 * @return
	 */
    public static boolean isJavaType(long type) {
        return type == QueryType.JAVA_TYPE;
    }

    /**
	 * 扩展SQL类型
	 * 
	 * @return
	 */
    public static boolean isXSqlType(long type) {
        return type == QueryType.XSQL_TYPE;
    }

    /**
	 * JSON类型
	 * 
	 * @return
	 */
    public static boolean isJSONType(long type) {
        return type == QueryType.JSON_TYPE;
    }

    public Long getQueryTypeId() {
        return queryTypeId;
    }

    public void setQueryTypeId(Long queryTypeId) {
        this.queryTypeId = queryTypeId;
    }

    public String getQueryTypeName() {
        return queryTypeName;
    }

    public void setQueryTypeName(String queryTypeName) {
        this.queryTypeName = queryTypeName;
    }

    public String getQueryTypeDesc() {
        return queryTypeDesc;
    }

    public void setQueryTypeDesc(String queryTypeDesc) {
        this.queryTypeDesc = queryTypeDesc;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
