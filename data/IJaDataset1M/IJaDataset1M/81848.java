package com.tensegrity;

import org.palo.api.Connection;
import org.palo.api.ConnectionFactory;
import org.palo.api.Database;
import org.palo.api.Dimension;
import org.palo.api.Element;
import org.palo.api.ElementNode;
import org.palo.api.Subset;
import org.palo.api.ext.subsets.SubsetHandler;
import org.palo.api.ext.subsets.SubsetHandlerRegistry;

public class SubsetTest2 {

    public static void main(String[] args) {
        Connection connection = ConnectionFactory.getInstance().newConnection("localhost", "7777", "user", "pass");
        Database db = connection.getDatabaseByName("Demo");
        Dimension month = db.getDimensionByName("Months");
        Subset[] ss = month.getSubsets();
        Subset j = ss[3];
        SubsetHandlerRegistry handlerReg = SubsetHandlerRegistry.getInstance();
        SubsetHandler handler = handlerReg.getHandler(j);
        ElementNode[] r = handler.getVisibleRootNodes();
        Element[] elements = handler.getVisibleElements();
        System.out.println("root count : " + r.length);
        for (int i = 0; i < r.length; i++) {
            ElementNode node = r[i];
            System.out.println("element " + node.getElement().getName());
        }
        System.out.println("visible elements count : " + elements.length);
        for (int i = 0; i < elements.length; i++) {
            Element element = elements[i];
            System.out.println("element : " + element.getName());
        }
        connection.disconnect();
    }
}
