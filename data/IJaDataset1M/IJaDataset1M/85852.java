package com.mangobop.math;

import com.mangobop.functions.FunctionEvent;
import com.mangobop.functions.connection.ConnectionException;
import com.mangobop.functions.connection.ConnectionList;

/**
 * @author Stefan Meyer
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class SumImpl extends SumAbstractState {

    private ConnectionList list_con;

    /**
	 * 
	 */
    public SumImpl() {
        super();
    }

    private Integer value = new Integer(0);

    public Integer getOut() {
        return value;
    }

    public void beforeFiringEvents(FunctionEvent e) {
        if (list_con == null) return;
        java.util.List int_list = (java.util.List) list_con.getValue();
        int result = 0;
        for (int i = 0; i < int_list.size(); i++) {
            Integer next = (Integer) int_list.get(i);
            result += next.intValue();
        }
        value = new Integer(result);
    }

    public void connectList(ConnectionList list) {
        list_con = list;
        try {
            list.create(this, getId().getType().getConnectorByName("list"));
        } catch (ConnectionException e) {
            e.printStackTrace();
        }
    }

    public java.util.Collection getUpdatables() {
        if (list_con == null) return null; else return list_con.getEventSources();
    }
}
