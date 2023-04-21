package com.apelon.dts.samples.tutorial;

import com.apelon.apelonserver.client.ServerConnection;
import com.apelon.dts.client.DTSException;
import com.apelon.dts.client.concept.*;
import com.apelon.dts.client.namespace.Namespace;
import java.io.IOException;

/**
 * <p>Title: Chapter 7</p>
 * <p>Description: Using OntylogClassQuery to check hierarchy relationships.</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Apelon, Inc.</p>
 * @author     Apelon
 * @version    DTS 3.0.0
 */
public class Chapter7 {

    private ServerConnection srvrConn = null;

    int nameSpaceID = -1;

    private OntylogClassQuery classQry = null;

    private SearchQuery searchQry = null;

    public Chapter7(ServerConnection conn, int nameId) throws DTSException {
        srvrConn = conn;
        nameSpaceID = nameId;
        classQry = OntylogClassQuery.createInstance(srvrConn);
        searchQry = SearchQuery.createInstance(srvrConn);
    }

    /**
   * Call to close ServerConnection
   */
    public void close() throws Exception {
        srvrConn.close();
        srvrConn = null;
    }

    /**
   *
   * @throws DTSException
   * @throws IOException
   */
    public void searchAndCheckSubsumption() throws DTSException, IOException {
        DTSSearchOptions opts = new DTSSearchOptions();
        opts.setAttributeSetDescriptor(ConceptAttributeSetDescriptor.NO_ATTRIBUTES);
        opts.setLimit(5);
        opts.setNamespaceId(nameSpaceID);
        System.out.println("\nEnter two different concept name search strings...");
        String searchStr1 = PromptUtil.promptForString("Enter concept search string 1 >>");
        String searchStr2 = PromptUtil.promptForString("Enter concept search string 2 >>");
        DTSConcept[] cons1 = searchQry.findConceptsWithNameMatching(searchStr1, opts);
        DTSConcept[] cons2 = searchQry.findConceptsWithNameMatching(searchStr2, opts);
        if (cons1.length == 0) {
            System.out.println("No concepts returned for '" + searchStr1 + "', exiting...");
            return;
        }
        if (cons2.length == 0) {
            System.out.println("No concepts returned for '" + searchStr2 + "', exiting...");
            return;
        }
        for (int x = 0; x < cons1.length; x++) {
            for (int y = 0; y < cons2.length; y++) {
                OntylogConcept con1 = (OntylogConcept) cons1[x];
                OntylogConcept con2 = (OntylogConcept) cons2[y];
                boolean bIsSubConcept = classQry.isSubConcept(con2, con1);
                String msg = con2.getName();
                msg += (bIsSubConcept ? " IS " : " IS NOT ");
                msg += "a subconcept of " + con1.getName();
                System.out.println(msg);
                bIsSubConcept = classQry.isSubConcept(con1, con2);
                msg = con1.getName();
                msg += (bIsSubConcept ? " IS " : " IS NOT ");
                msg += "a subconcept of " + con2.getName();
                System.out.println(msg);
            }
        }
    }

    public static void printChapInfo() {
        System.out.println("Chapter Info : ");
        System.out.println("--------------");
        System.out.println("\tUses OntylogClassQuery to check hierarchy relationships.");
        System.out.println("");
    }

    public static void main(String[] args) {
        Chapter7.printChapInfo();
        Chapter1.initLog4j();
        Chapter1 chap1 = new Chapter1();
        try {
            ServerConnection conn = chap1.promptConnectionSocket();
            Chapter4 chap4 = new Chapter4(conn);
            Namespace namespace = chap4.promptForOntylogNamespace(conn);
            Chapter7 chap7 = new Chapter7(conn, namespace.getId());
            chap7.searchAndCheckSubsumption();
            chap7.close();
        } catch (Exception ex) {
            Chapter1.getDtsClientLog4jCategory().error("Caught Exception", ex);
        }
    }
}
