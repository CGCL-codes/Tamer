package edu.mit.wi.omnigene.omnigraph;

import java.sql.*;

/**
 * Interface which represents a procedure in
 * a relational database
 */
public interface Procedure extends Entity {

    public String getProcedureName();

    public void setProcedureName();

    public boolean isCallable();

    public String getProcedure();
}
