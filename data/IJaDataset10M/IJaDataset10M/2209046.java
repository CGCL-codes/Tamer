package org.apache.ibatis.abator.internal.java.dao;

/**
 * @author Jeff Butler
 */
public class SpringJava2DAOGenerator extends BaseDAOGenerator {

    public SpringJava2DAOGenerator() {
        super(new SpringDAOTemplate(), false);
    }
}
