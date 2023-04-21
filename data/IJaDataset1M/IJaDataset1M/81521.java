package com.sitescape.team.domain;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.hibernate.HibernateException;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.orm.hibernate3.support.ClobStringType;

public class AuthenticationMappingsUserType extends ClobStringType {

    public Class returnedClass() {
        return Map.class;
    }

    @Override
    protected Object nullSafeGetInternal(ResultSet resultSet, String[] names, Object owner, LobHandler lobHandler) throws SQLException {
        Map<String, String> result = new HashMap<String, String>();
        if (!resultSet.wasNull()) {
            String mappings = lobHandler.getClobAsString(resultSet, names[0]);
            try {
                Document doc = DocumentHelper.parseText(mappings);
                for (Object o : doc.selectNodes("//mapping")) {
                    Node node = (Node) o;
                    String attr = node.selectSingleNode("@from").getText();
                    String field = node.selectSingleNode("@to").getText();
                    result.put(attr, field);
                }
            } catch (Exception e) {
                logger.warn("Unable to parse attribute mapping: " + mappings);
            }
        }
        return result;
    }

    public void nullSafeSetInternal(PreparedStatement preparedStatement, int index, Object value, LobCreator lobCreator) throws SQLException {
        if (null == value) {
            preparedStatement.setNull(index, Types.CLOB);
        } else {
            Map<String, String> attributeMap = (Map<String, String>) value;
            StringBuffer map = new StringBuffer("<userMapping>");
            for (String attr : attributeMap.keySet()) {
                map.append("<mapping from=\"" + attr + "\" to=\"" + attributeMap.get(attr) + "\"/>");
            }
            map.append("</userMapping>");
            lobCreator.setClobAsString(preparedStatement, index, map.toString());
        }
    }

    public Object deepCopy(Object value) throws HibernateException {
        return new HashMap<String, String>((Map<String, String>) value);
    }

    public boolean isMutable() {
        return true;
    }

    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return deepCopy(cached);
    }

    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) deepCopy(value);
    }

    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return new HashMap<String, String>((Map<String, String>) original);
    }
}
