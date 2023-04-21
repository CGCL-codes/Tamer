package uk.org.ogsadai.client.toolkit.scenarios;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import uk.org.ogsadai.client.toolkit.DataIterator;
import uk.org.ogsadai.client.toolkit.DataRequestExecutionResource;
import uk.org.ogsadai.client.toolkit.PipelineWorkflow;
import uk.org.ogsadai.client.toolkit.RequestExecutionType;
import uk.org.ogsadai.client.toolkit.RequestResource;
import uk.org.ogsadai.client.toolkit.Resource;
import uk.org.ogsadai.client.toolkit.ServerProxy;
import uk.org.ogsadai.client.toolkit.activities.delivery.DeliverToFTP;
import uk.org.ogsadai.client.toolkit.activities.delivery.DeliverToRequestStatus;
import uk.org.ogsadai.client.toolkit.activities.delivery.DeliverToSMTP;
import uk.org.ogsadai.client.toolkit.activities.file.ListDirectory;
import uk.org.ogsadai.client.toolkit.activities.file.ReadFromFile;
import uk.org.ogsadai.client.toolkit.exception.RequestException;
import uk.org.ogsadai.resource.ResourceID;
import uk.org.ogsadai.resource.request.RequestStatus;

/**
 * Simple example of listing the files in a file resource using
 * ListDirectory, reading the first file from the file resource using 
 * ReadFile and then delivering it via both DeliverToFTP and
 * DeliverToSMTP. 
 * <p>
 * This is NOT an application or a fully-functional client. Rather it
 * is intended to serve as a workflow that developers can
 * cut-and-paste or customise for use indeveloping their own
 * applications. If there is a bug then please let us know. If you
 * want additional features then please add them yourself.
 * </p>
 *
 * @author The OGSA-DAI Project Team.
 */
public class ReadFileFTPSMTPScenario {

    /** Copyright statement. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2007-2008";

    /**
     * Run the scenario.
     *
     * @param args
     *     Unused.
     * @throws Exception
     *     If any problems arise.
     */
    public static void main(String[] args) throws Exception {
        String url = "http://localhost:9020/dai/services/";
        String drerID = "DataRequestExecutionResource";
        String id = "FileResource";
        String sql = "testFile.txt";
        ServerProxy server = new ServerProxy();
        server.setDefaultBaseServicesURL(new URL(url));
        DataRequestExecutionResource drer = server.getDataRequestExecutionResource(new ResourceID(drerID));
        ListDirectory listDirectory = new ListDirectory();
        listDirectory.setResourceID(id);
        listDirectory.addRecursive(true);
        DeliverToRequestStatus deliverToRequestStatus = new DeliverToRequestStatus();
        deliverToRequestStatus.connectInput(listDirectory.getDataOutput());
        PipelineWorkflow pipeline1 = new PipelineWorkflow();
        pipeline1.add(listDirectory);
        pipeline1.add(deliverToRequestStatus);
        RequestResource requestResource1 = drer.execute(pipeline1, RequestExecutionType.SYNCHRONOUS);
        System.out.println("Status: " + requestResource1.getRequestExecutionStatus());
        String firstFile = "";
        if (listDirectory.hasNextData()) {
            DataIterator dataList = listDirectory.nextData();
            while (dataList.hasNext()) {
                String file = (String) dataList.next();
                System.out.println(file);
                if (firstFile.equals("")) {
                    firstFile = file;
                }
            }
        }
        ReadFromFile readFromFile = new ReadFromFile();
        DeliverToFTP deliverToFTP = new DeliverToFTP();
        DeliverToSMTP deliverToSMTP = new DeliverToSMTP();
        readFromFile.setResourceID(id);
        readFromFile.addFile(firstFile);
        deliverToFTP.connectDataInput(readFromFile.getDataOutput());
        deliverToSMTP.connectDataInput(readFromFile.getDataOutput());
        deliverToFTP.addFilename(firstFile);
        deliverToFTP.addHost("user:password@ftp.server.url:21");
        deliverToFTP.addPassiveMode(true);
        deliverToSMTP.addFrom("user@e-mail.address");
        deliverToSMTP.addSubject("ReadFileFTPSMTPScenario");
        List to = Collections.singletonList("recipient@e-mail-address");
        deliverToSMTP.addTo(to.iterator());
        PipelineWorkflow pipeline2 = new PipelineWorkflow();
        pipeline2.add(readFromFile);
        pipeline2.add(deliverToFTP);
        pipeline2.add(deliverToSMTP);
        RequestResource requestResource2 = drer.execute(pipeline2, RequestExecutionType.SYNCHRONOUS);
        System.out.println("Status: " + requestResource2.getRequestExecutionStatus());
    }
}
