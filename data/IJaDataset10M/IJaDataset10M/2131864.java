package kosoft.dbgen.server;

import java.util.*;
import java.io.*;
import java.net.*;
import kosoft.proxy.client.*;
import kosoft.proxy.server.*;
import kosoft.database.client.*;
import kosoft.database.*;
import kosoft.dbgen.util.*;
import kosoft.dbgen.client.*;
import java.sql.*;
import java.util.Date;

public abstract class ProxyQueryBaseServer extends ProxyServerObjectBase implements ProxyQueryBaseInterface {

    QueryBase oQuery;

    public String GetObjectTypeName() {
        return PROXY_Name;
    }

    public ProxyQueryBaseServer() {
    }

    public ProxyResponse CallObjectMethod(ProxyRequest oRequest) {
        ProxyResponse oResponse = new ProxyResponse(oRequest);
        switch(oRequest.iMethodId) {
            case FUNCTION_AddWhere:
                {
                    String sWhere = (String) oRequest.pop();
                    oQuery.AddWhere(sWhere);
                    break;
                }
            case FUNCTION_AddWhereStringColumnEqual:
                {
                    int iColumn = ((Integer) oRequest.pop()).intValue();
                    String sValue = (String) oRequest.pop();
                    oQuery.AddWhere(iColumn, sValue);
                    break;
                }
            case FUNCTION_AddWhereStringColumnOperator:
                {
                    int iColumn = ((Integer) oRequest.pop()).intValue();
                    String sValue = (String) oRequest.pop();
                    String sOperator = (String) oRequest.pop();
                    oQuery.AddWhere(iColumn, sValue, sOperator);
                    break;
                }
            case FUNCTION_AddWhereStringColumnOperatorWithUnquotedValue:
                {
                    int iColumn = ((Integer) oRequest.pop()).intValue();
                    String sValue = (String) oRequest.pop();
                    String sOperator = (String) oRequest.pop();
                    oQuery.AddWhereWithUnquotedValue(iColumn, sValue, sOperator);
                    break;
                }
            case FUNCTION_AddWhereIntColumnEqual:
                {
                    int iColumn = ((Integer) oRequest.pop()).intValue();
                    int iValue = ((Integer) oRequest.pop()).intValue();
                    oQuery.AddWhere(iColumn, iValue);
                    break;
                }
            case FUNCTION_AddWhereIntColumnOperator:
                {
                    int iColumn = ((Integer) oRequest.pop()).intValue();
                    int iValue = ((Integer) oRequest.pop()).intValue();
                    String sOperator = (String) oRequest.pop();
                    oQuery.AddWhere(iColumn, iValue, sOperator);
                    break;
                }
            case FUNCTION_AddWhereBooleanColumnEqual:
                {
                    int iColumn = ((Integer) oRequest.pop()).intValue();
                    boolean bValue = ((Boolean) oRequest.pop()).booleanValue();
                    break;
                }
            case FUNCTION_AddWhereDateColumnEqual:
                {
                    int iColumn = ((Integer) oRequest.pop()).intValue();
                    Date oValue = (Date) oRequest.pop();
                    oQuery.AddWhere(iColumn, oValue);
                    break;
                }
            case FUNCTION_AddWhereDateColumnOperator:
                {
                    int iColumn = ((Integer) oRequest.pop()).intValue();
                    Date oValue = (Date) oRequest.pop();
                    String sOperator = (String) oRequest.pop();
                    oQuery.AddWhere(iColumn, oValue, sOperator);
                    break;
                }
            case FUNCTION_AddOrderBy:
                {
                    String sValue = (String) oRequest.pop();
                    oQuery.AddOrderBy(sValue);
                    break;
                }
            case FUNCTION_AddColumnOrderBy:
                {
                    int iColumn = ((Integer) oRequest.pop()).intValue();
                    oQuery.AddOrderBy(iColumn);
                    break;
                }
            case FUNCTION_AddColumnOrderByWithDirection:
                {
                    int iColumn = ((Integer) oRequest.pop()).intValue();
                    int iDirection = ((Integer) oRequest.pop()).intValue();
                    oQuery.AddOrderBy(iColumn, iDirection);
                    break;
                }
            case FUNCTION_FetchAll:
                {
                    Vector oLocalResults = oQuery.FetchAll();
                    Vector oReturnResults = new Vector(oLocalResults.size());
                    for (int i = 0; i < oResults.size(); i++) {
                        UpdateableRowBase oRow = (UpdateableRowBase) oResults.elementAt(i);
                        int iNewObjectNum = oClientConnection.AddObject(BuildNewRowObject(oRow));
                        Vector oRowItems = new Vector(2);
                        oRowItems.add(new Integer(iRowItems));
                        oRowItems.add(oRow.GetRowAsObjectArray());
                        oReturnResults.add(oRowItems);
                    }
                    oResponse.push(oReturnResults);
                }
                break;
        }
        return oResponse;
    }

    public abstract ProxyServerObjectBase BuildNewRowObject(UpdateableRowBase oRow);

    int CreateStatement() throws QueryException {
        SQLStatementInterface oStmt = oCon.CreateStatement();
        return oClientConnection.AddObject(new ProxySQLStatementServer(oStmt));
    }
}
