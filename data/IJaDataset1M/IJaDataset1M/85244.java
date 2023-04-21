package com.mchange.v2.c3p0.impl;

import java.sql.Connection;
import javax.sql.PooledConnection;
import com.mchange.v2.c3p0.stmt.GooGooStatementCache;
import com.mchange.v1.util.ClosableResource;

abstract class AbstractC3P0PooledConnection implements PooledConnection, ClosableResource {

    abstract Connection getPhysicalConnection();

    abstract void initStatementCache(GooGooStatementCache scache);
}
