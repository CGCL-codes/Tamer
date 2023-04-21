package com.ibatis.common.sqlmap;

import java.io.*;
import java.util.*;
import org.apache.log4j.Logger;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

/**
 * @author     clinton_begin
 * @created    December 4, 2002
 */
public class XmlSqlMapBuilder {

    private static final String PARAMETER_TOKEN = "#";

    private static final String DOT = ".";

    private static final String SQL_MAP_ELEMENT = "sql-map";

    private static final String RESULT_MAP_ELEMENT = "result-map";

    private static final String PARAMETER_MAP_ELEMENT = "parameter-map";

    private static final String MAPPED_STATEMENT_ELEMENT = "mapped-statement";

    private static final String PROPERTY_ELEMENT = "property";

    private static final Logger log = Logger.getLogger(XmlSqlMapBuilder.class);

    /**
     *  Builds a SQL map from a file
     *
     * @param  file  The file
     * @return
     */
    public static SqlMap buildSqlMap(File file) {
        SqlMap sqlMap = new SqlMap();
        try {
            if (file != null) {
                if (file.isFile()) {
                    includeSqlMap(sqlMap, new FileInputStream(file));
                } else if (file.isDirectory()) {
                    File[] files = file.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        if (files[i].getName().endsWith(".xml")) {
                            includeSqlMap(sqlMap, new FileInputStream(files[i]));
                        }
                    }
                } else {
                    throw new SqlMapException("Unkown error with input file/directory passed to XmlSqlMapBuilder.buildSqlMap().");
                }
            } else {
                throw new SqlMapException("Input file/directory passed to XmlSqlMapBuilder.buildSqlMap() was NULL.");
            }
        } catch (IOException e) {
            throw new SqlMapException("Error in XmlSqlMapBuilder.buildSqlMap(). \n\nCause: \n\n" + e);
        }
        return sqlMap;
    }

    private static void includeSqlMap(SqlMap sqlMap, InputStream in) {
        try {
            SAXBuilder builder = new SAXBuilder();
            Document doc = builder.build(in);
            Element root = doc.getRootElement();
            String rootname = root.getName();
            if (SQL_MAP_ELEMENT.equals(rootname)) {
            } else {
                throw new IOException("The root tag of the SqlMap XML document was not '" + SQL_MAP_ELEMENT + "'.");
            }
            String sqlMapName = null;
            Attribute nameAttrib = root.getAttribute("name");
            if (nameAttrib != null && nameAttrib.getValue() != null) {
                sqlMapName = nameAttrib.getValue();
            } else {
                throw new SqlMapException("A sql map element requires a name attribute.");
            }
            List children = root.getChildren();
            for (int i = 0; i < children.size(); i++) {
                Element child = (Element) children.get(i);
                if (PARAMETER_MAP_ELEMENT.equals(child.getName())) {
                    ParameterMap parameterMap = buildParameterMap(child);
                    parameterMap.setName(sqlMapName + DOT + parameterMap.getName());
                    sqlMap.addParameterMap(parameterMap);
                } else if (RESULT_MAP_ELEMENT.equals(child.getName())) {
                    ResultMap resultMap = buildResultMap(child);
                    resultMap.setName(sqlMapName + DOT + resultMap.getName());
                    sqlMap.addResultMap(resultMap);
                } else if (MAPPED_STATEMENT_ELEMENT.equals(child.getName())) {
                    MappedStatement mappedStatement = buildMappedStatement(child);
                    mappedStatement.setSqlMap(sqlMap);
                    if ("inline".equals(mappedStatement.getParameterMapName())) {
                        mappedStatement.setParameterMapName(sqlMapName + DOT + mappedStatement.getName() + "-inline");
                        processInlineParameterMap(mappedStatement);
                    }
                    String parameterMapName = mappedStatement.getParameterMapName();
                    if (parameterMapName != null && parameterMapName.indexOf(DOT) < 0) {
                        mappedStatement.setParameterMapName(sqlMapName + DOT + mappedStatement.getParameterMapName());
                    }
                    String resultMapName = mappedStatement.getResultMapName();
                    if (resultMapName != null && resultMapName.indexOf(DOT) < 0) {
                        mappedStatement.setResultMapName(sqlMapName + DOT + mappedStatement.getResultMapName());
                    }
                    sqlMap.addMappedStatement(mappedStatement);
                }
            }
        } catch (SqlMapException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error", e);
            throw new SqlMapException("Error while building SqlMap.  \n\nCause: \n\n" + e);
        }
    }

    private static ParameterMap buildParameterMap(Element element) {
        ParameterMap parameterMap = new ParameterMap();
        Attribute nameAttrib = element.getAttribute("name");
        if (nameAttrib != null && nameAttrib.getValue() != null) {
            parameterMap.setName(nameAttrib.getValue());
        } else {
            throw new SqlMapException("A result map element requires a name attribute.");
        }
        List children = element.getChildren();
        for (int i = 0; i < children.size(); i++) {
            Element child = (Element) children.get(i);
            if (PROPERTY_ELEMENT.equals(child.getName())) {
                Attribute propNameAttrib = child.getAttribute("name");
                Attribute propClassAttrib = child.getAttribute("class");
                if (propNameAttrib != null && propNameAttrib.getValue() != null && propClassAttrib != null && propClassAttrib.getValue() != null) {
                    parameterMap.addPropertyMapping(propNameAttrib.getValue(), propClassAttrib.getValue());
                } else {
                    throw new SqlMapException("A property element requires name and class attributes.");
                }
            }
        }
        return parameterMap;
    }

    private static ResultMap buildResultMap(Element element) {
        ResultMap resultMap = new ResultMap();
        Attribute nameAttrib = element.getAttribute("name");
        if (nameAttrib != null && nameAttrib.getValue() != null) {
            resultMap.setName(nameAttrib.getValue());
        } else {
            throw new SqlMapException("A result map element requires a name attribute.");
        }
        Attribute classAttrib = element.getAttribute("class");
        if (nameAttrib != null && nameAttrib.getValue() != null) {
            resultMap.setClassName(classAttrib.getValue());
        } else {
            throw new SqlMapException("A result map element requires a class attribute.");
        }
        Attribute keyAttrib = element.getAttribute("key");
        if (keyAttrib != null && keyAttrib.getValue() != null) {
            resultMap.setKey(keyAttrib.getValue());
        } else {
            throw new SqlMapException("A result map element requires a key attribute.");
        }
        List children = element.getChildren();
        for (int i = 0; i < children.size(); i++) {
            Element child = (Element) children.get(i);
            if (PROPERTY_ELEMENT.equals(child.getName())) {
                Attribute propNameAttrib = child.getAttribute("name");
                Attribute columnAttrib = child.getAttribute("column");
                Attribute statementAttrib = child.getAttribute("mapped-statement");
                Attribute resultAttrib = child.getAttribute("result-map");
                Attribute crosstabNameAttrib = child.getAttribute("name-column");
                Attribute crosstabValueAttrib = child.getAttribute("value-column");
                if (propNameAttrib != null && propNameAttrib.getValue() != null && columnAttrib != null && columnAttrib.getValue() != null) {
                    resultMap.addPropertyMapping(propNameAttrib.getValue(), columnAttrib.getValue());
                } else if (statementAttrib != null && statementAttrib.getValue() != null) {
                    resultMap.addStatementMapping(propNameAttrib.getValue(), statementAttrib.getValue());
                } else if (resultAttrib != null && resultAttrib.getValue() != null) {
                    resultMap.addResultMapping(propNameAttrib.getValue(), resultAttrib.getValue());
                    resultMap.addPropertyMapping(propNameAttrib.getValue(), null);
                } else if (crosstabNameAttrib != null && crosstabNameAttrib.getValue() != null && crosstabValueAttrib != null && crosstabValueAttrib.getValue() != null) {
                    resultMap.addPropertyMapping(propNameAttrib.getValue(), null);
                    resultMap.addCrosstabMapping(propNameAttrib.getValue(), crosstabNameAttrib.getValue(), crosstabValueAttrib.getValue());
                } else {
                    throw new SqlMapException("A property element requires name and column attributes.");
                }
            }
        }
        return resultMap;
    }

    private static MappedStatement buildMappedStatement(Element element) throws SqlMapException {
        MappedStatement mappedStatement = new MappedStatement();
        Attribute nameAttrib = element.getAttribute("name");
        if (nameAttrib != null && nameAttrib.getValue() != null) {
            mappedStatement.setName(nameAttrib.getValue());
        } else {
            throw new SqlMapException("A mapped statement element requires a name attribute.");
        }
        String content = element.getText();
        if (content != null && content.length() > 0) {
            mappedStatement.setSql(content.trim());
        } else {
            throw new SqlMapException("A mapped statement element requires body content.");
        }
        Attribute parameterAttrib = element.getAttribute("parameter-map");
        if (parameterAttrib != null && parameterAttrib.getValue() != null) {
            mappedStatement.setParameterMapName(parameterAttrib.getValue());
        }
        Attribute resultAttrib = element.getAttribute("result-map");
        if (resultAttrib != null && resultAttrib.getValue() != null) {
            mappedStatement.setResultMapName(resultAttrib.getValue());
        }
        return mappedStatement;
    }

    private static void processInlineParameterMap(MappedStatement mappedStatement) {
        ParameterMap parameterMap = new ParameterMap();
        parameterMap.setName(mappedStatement.getParameterMapName());
        StringTokenizer parser = new StringTokenizer(mappedStatement.getSql(), PARAMETER_TOKEN, true);
        StringBuffer newSql = new StringBuffer();
        String token = null;
        String lastToken = null;
        while (parser.hasMoreTokens()) {
            token = parser.nextToken();
            if (PARAMETER_TOKEN.equals(lastToken)) {
                if (PARAMETER_TOKEN.equals(token)) {
                    newSql.append(PARAMETER_TOKEN);
                    token = null;
                } else {
                    parameterMap.addPropertyMapping(token, "java.util.String");
                    newSql.append("?");
                    token = parser.nextToken();
                    if (!PARAMETER_TOKEN.equals(token)) {
                        throw new SqlMapException("Unterminated inline parameter in mapped statement (" + mappedStatement.getName() + ").");
                    }
                    token = null;
                }
            } else {
                if (!PARAMETER_TOKEN.equals(token)) {
                    newSql.append(token);
                }
            }
            lastToken = token;
        }
        mappedStatement.setSql(newSql.toString());
        mappedStatement.getSqlMap().addParameterMap(parameterMap);
    }
}
