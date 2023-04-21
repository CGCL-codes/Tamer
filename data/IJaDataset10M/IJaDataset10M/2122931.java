package org.tripcom.security.evaluation;

import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.tripcom.integration.entry.QueryProcessingData;
import org.tripcom.integration.entry.RdOperationExternal;
import org.tripcom.integration.entry.ReadType;
import org.tripcom.integration.entry.SecurityAssertionsInfo;
import org.tripcom.integration.entry.SecurityInfo;
import org.tripcom.integration.entry.SpaceURI;
import org.tripcom.integration.entry.Template;
import org.tripcom.integration.entry.Timeout;
import org.tripcom.security.bus.JavaSpaceBus;
import org.tripcom.security.bus.LocalJavaSpace;
import org.tripcom.security.testbed.EnhancedTestbed;
import org.tripcom.security.testbed.Operation;
import org.tripcom.security.util.Certificates;
import org.tripcom.security.util.Resources;
import org.tripcom.security.util.SimpleCache;

public class NoCookieEvaluation {

    private static final String BASE = "org/tripcom/security/evaluation/";

    private static final String ROOT_SPACE = "tsc://test.tripcom.org:8080/root";

    private static final long TIMEOUT = 30000;

    private static final int COUNT = 1000;

    public static void main(String args[]) throws Exception {
        System.setProperty("org.tripcom.common.kernelAddress", "test.tripcom.org:8080");
        EnhancedTestbed testbed = new EnhancedTestbed(Resources.loadProperties(BASE + "sm.properties"), BASE + "hierarchy\\.ttl", BASE + "policy.*\\.ttl", new JavaSpaceBus(new LocalJavaSpace()), new SimpleCache<Object, Object>());
        testbed.start();
        System.out.println("Running test...");
        X509Certificate certificate = Certificates.load(BASE + "Karl-cert.pem");
        String assertion = Resources.loadString(BASE + "Karl-assertion.xml");
        SecurityInfo securityInfo = new SecurityAssertionsInfo(Certificates.encode(certificate), Collections.singleton(assertion));
        long begin = System.nanoTime();
        List<Operation> operations = new ArrayList<Operation>();
        for (int i = 1; i <= COUNT; ++i) {
            RdOperationExternal request = new RdOperationExternal();
            request.operationID = (long) i;
            request.kind = ReadType.READ;
            request.space = new SpaceURI(ROOT_SPACE);
            request.timeout = new Timeout(10000, System.currentTimeMillis());
            request.qpData = new QueryProcessingData();
            request.query = new Template("am I a query?");
            request.toFromReasoner = null;
            request.transactionID = null;
            request.securityInfo = securityInfo;
            Operation operation = testbed.process(request);
            operations.add(operation);
        }
        testbed.awaitCompletion(TIMEOUT);
        long end = System.nanoTime();
        for (Operation operation : operations) {
            System.out.println(operation);
        }
        int completed = 0;
        long smTime = 0L, netTime = 0L, totalTime = 0L;
        for (Operation operation : operations) {
            if (operation.isCompleted()) {
                ++completed;
                smTime += operation.getComponentProcessingTime();
                netTime += operation.getCommunicationTime();
                totalTime += operation.getTotalProcessingTime();
            }
        }
        System.out.println("\nCompleted operations: " + completed + "/" + COUNT + "\n");
        System.out.println("Security Manager statistics:\n" + testbed.getSecurityManagerStatistics().toString());
        System.out.println("Metadata Manager statistics:\n" + testbed.getMetadataManagerStatistics().toString());
        System.out.println("TS Adapter statistics:\n" + testbed.getTSAdapterStatistics().toString());
        System.out.println("Throughput:         " + (double) (COUNT * 1000000000L) / (double) (end - begin) + " requests/second");
        testbed.stop();
    }
}
