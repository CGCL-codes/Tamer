package com.oktiva.mogno.teste;

import com.oktiva.mogno.*;
import com.oktiva.mogno.html.*;
import com.oktiva.mogno.additional.*;

/**
 * @version $Id: TestSimpleList.java,v 1.1.1.1 2005/01/05 16:50:29 ruoso Exp $
 */
public class TestSimpleList extends com.oktiva.mogno.html.PrototypePage {

    public void onPageShow(TestSimpleList sender) {
        StringBuffer l = new StringBuffer();
        SimpleList sl = (SimpleList) this.getChild("simpleList1");
        sl.list = "Item1\nItem 2\n Item 2.1\n  Item 2.1.1\n Item 2.2\nItem 3\n Item 3.1";
    }
}
