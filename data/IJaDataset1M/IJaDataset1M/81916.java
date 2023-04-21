package com.infineon.dns.dao;

import java.util.List;
import com.infineon.dns.model.Attribute;

public interface AttributeDao {

    void insertAttribute(Attribute attribute);

    void updateAttribute(Attribute attribute);

    void deleteAttribute(int attributeId);

    Attribute getAttributeByAttributeId(int attributeId, boolean b);

    List<Attribute> getAttributeByAttributeName(String attributeName);

    List<Attribute> getAttributeByAttributeCode(String attributeCode);

    List<Attribute> getAttributes(String sort, String dir, String start, String limit);

    int getAttributeNumber();

    List<Attribute> getAllAttributes();

    List<Attribute> getAllAttributes(String orderBy, boolean isAsc);
}
