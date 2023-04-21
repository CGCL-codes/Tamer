package net.sf.daotools.model.hql;

import java.util.List;

public class HqlStatement {

    public static final String SELECT = "select";

    public static final String INSERT = "insert";

    public static final String UPDATE = "update";

    public static final String DELETE = "delete";

    private String type;

    private List<Table> tables;
}
