package com.apelon.dts.samples.tutorial;

import com.apelon.apelonserver.client.ServerConnection;
import com.apelon.dts.client.DTSException;
import com.apelon.dts.client.concept.*;
import com.apelon.dts.client.namespace.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * <p>Title: Chapter 4</p>
 * <p>Description: Using NavQuery to navigate concepts in an Ontylog hierarchy.</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Apelon, Inc.</p>
 * @author     Apelon, Inc.
 * @version    DTS 3.0.0
 */
public class Chapter4 {

    private ServerConnection srvrConn = null;

    Chapter2 chap2 = null;

    private NavQuery navQry = null;

    private ConceptAttributeSetDescriptor asdToUse = ConceptAttributeSetDescriptor.NO_ATTRIBUTES;

    private int MAX_LEVELS_TO_NAVIGATE = 3;

    private String[] levelSpaces = null;

    public Chapter4(ServerConnection conn) throws DTSException {
        srvrConn = conn;
        navQry = NavQuery.createInstance(srvrConn);
        levelSpaces = new String[MAX_LEVELS_TO_NAVIGATE];
        for (int x = 0; x < MAX_LEVELS_TO_NAVIGATE; x++) {
            levelSpaces[x] = new String();
            for (int y = 0; y < x; y++) levelSpaces[x] += "  ";
        }
    }

    /**
   * Call to close ServerConnection
   */
    public void close() throws Exception {
        srvrConn.close();
        srvrConn = null;
    }

    private void walkConceptTree(int namespaceId) throws DTSException {
        ConceptChild[] roots = navQry.getConceptChildRoots(asdToUse, namespaceId);
        navigateConcepts(roots, 0);
    }

    private void navigateConcepts(ConceptChild[] consToNav, int level) throws DTSException {
        for (int i = 0; i < consToNav.length; i++) {
            OntylogConcept con = consToNav[i];
            System.out.println(levelSpaces[level] + con.getName());
            if (level + 1 < MAX_LEVELS_TO_NAVIGATE) {
                NavChildContext childCtx = navQry.getNavChildContext(con, asdToUse);
                ConceptChild[] children = childCtx.getChildren();
                navigateConcepts(children, level + 1);
            }
        }
    }

    /**
   * Prompt for a namespace from a list of all Ontylog namespaces.
   *
   * @param conn server connection
   * @return the selected Namespace
   * @throws DTSException
   */
    public Namespace promptForOntylogNamespace(ServerConnection conn) throws DTSException {
        Namespace retVal = null;
        NamespaceQuery nameQry = NamespaceQuery.createInstance(conn);
        Namespace[] nameSpaces = nameQry.getNamespaces();
        while (retVal == null) {
            System.out.println("\nEnter the number of the Ontylog namespace to use:");
            for (int x = 0; x < nameSpaces.length; x++) {
                if (nameSpaces[x].getNamespaceType() == NamespaceType.ONTYLOG) System.out.println("[" + (x + 1) + "] " + nameSpaces[x].getName() + " (" + nameSpaces[x].getNamespaceType() + ")");
            }
            System.out.print(">>");
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                String option = in.readLine();
                int value = new Integer(option).intValue();
                if ((value > 0) && (value <= nameSpaces.length)) {
                    retVal = nameSpaces[value - 1];
                } else {
                    System.out.println("Please enter a number between 1 and " + nameSpaces.length);
                }
            } catch (Exception ex) {
                System.out.println("Please enter a number between 1 and " + nameSpaces.length);
            }
        }
        return retVal;
    }

    public static void printChapInfo() {
        System.out.println("Chapter Info : ");
        System.out.println("--------------");
        System.out.println("\tUses NavQuery to navigate concepts in an Ontylog hierarchy.");
        System.out.println("");
    }

    public static void main(String[] args) {
        Chapter4.printChapInfo();
        Chapter1.initLog4j();
        Chapter1 chap1 = new Chapter1();
        ServerConnection conn = null;
        try {
            conn = chap1.promptConnectionSocket();
            Chapter4 chap4 = new Chapter4(conn);
            Namespace namespace = chap4.promptForOntylogNamespace(conn);
            if (namespace.getNamespaceType() != NamespaceType.ONTYLOG) System.out.println("You must select an Ontylog Namespace for this chapter"); else {
                chap4.walkConceptTree(namespace.getId());
                chap4.close();
            }
        } catch (Exception ex) {
            Chapter1.getDtsClientLog4jCategory().error("Caught Exception", ex);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception ex) {
                Chapter1.getDtsClientLog4jCategory().error("Caught Exception (while closing ServerConnection", ex);
            }
        }
    }
}
