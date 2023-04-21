package com.ibatis.sqlmap.implgen.example;

import com.ibatis.sqlmap.implgen.annotations.CacheModel;
import com.ibatis.sqlmap.implgen.annotations.CacheModels;
import com.ibatis.sqlmap.implgen.annotations.Delete;
import com.ibatis.sqlmap.implgen.annotations.HasSql;
import com.ibatis.sqlmap.implgen.annotations.Parameter;
import com.ibatis.sqlmap.implgen.annotations.ParameterMap;
import com.ibatis.sqlmap.implgen.annotations.ParameterMaps;
import com.ibatis.sqlmap.implgen.annotations.Procedure;
import com.ibatis.sqlmap.implgen.annotations.Result;
import com.ibatis.sqlmap.implgen.annotations.ResultMap;
import com.ibatis.sqlmap.implgen.annotations.ResultMaps;
import com.ibatis.sqlmap.implgen.annotations.Select;
import com.ibatis.sqlmap.implgen.annotations.Update;
import java.sql.SQLException;
import java.util.List;

@HasSql
public interface ExampleDaoOne {

    @ParameterMaps({ @ParameterMap(id = "param-map-1", parameters = { @Parameter(property = "mamber1", mode = "INOUT") }) })
    @ResultMaps({ @ResultMap(id = "person-result", results = { @Result(property = "personId", column = "id", javaType = "Long", jdbcType = "NUMERIC", nullValue = "0"), @Result(property = "fullName", column = "name", javaType = "string", jdbcType = "VARCHAR", nullValue = ""), @Result(property = "dateOfBirth", column = "dob", javaType = "date", jdbcType = "TIMESTAMP") }), @ResultMap(id = "person-result-obj", results = { @Result(property = "personId", column = "id", javaType = "Long", jdbcType = "NUMERIC", nullValue = "0"), @Result(property = "fullName", column = "name", javaType = "string", jdbcType = "VARCHAR", nullValue = ""), @Result(property = "dateOfBirth", column = "dob", javaType = "date", jdbcType = "TIMESTAMP") }) })
    @CacheModels({ @CacheModel(id = "oneDayCache", type = "LRU", flushIntervalHours = "24") })
    @Update
    public void updateName(int id, String name) throws SQLException;

    @Delete
    public void deleteName(int id) throws SQLException;

    @Select
    public List<String> getListOfNames() throws SQLException;

    @Select(cacheModel = "oneDayCache")
    public List<String> getListOfCoutries() throws SQLException;

    @Select(resultMap = "person-result")
    public List<Person> getListOfPeople() throws SQLException;

    /**
     * Check method overloading.
     */
    @Select(resultMap = "person-result")
    public List<Person> getListOfPeople(String name) throws SQLException;

    @Procedure
    public void myProc(String name, Long id) throws SQLException;

    class Person {
    }
}
