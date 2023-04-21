package ru.wdcb.esse.ogsadai.client.toolkit.activity;

import java.io.*;
import java.net.URL;
import uk.org.ogsadai.client.toolkit.*;
import uk.org.ogsadai.client.toolkit.activities.delivery.DeliverToRequestStatus;
import uk.org.ogsadai.client.toolkit.exception.RequestExecutionException;
import uk.org.ogsadai.data.StringData;
import uk.org.ogsadai.resource.ResourceID;
import uk.org.ogsadai.client.toolkit.exception.ClientToolkitException;
import uk.org.ogsadai.client.toolkit.exception.RequestException;
import uk.org.ogsadai.client.toolkit.exception.ClientException;
import uk.org.ogsadai.client.toolkit.exception.ResourceUnknownException;
import uk.org.ogsadai.client.toolkit.exception.ServerException;
import uk.org.ogsadai.client.toolkit.exception.ServerCommsException;
import uk.org.ogsadai.client.toolkit.exception.DataSourceUsageException;
import uk.org.ogsadai.client.toolkit.exception.UnexpectedDataValueException;
import uk.org.ogsadai.client.toolkit.exception.DataStreamErrorException;

/**
 * Application for the HelloWorld activity tutorial.
 *
 * @author The OGSA-DAI Project Team
 */
public class FuzzyRun {

    public void go() {
        try {
            String path1 = "/home/dimm/jbproject/dai3_testers/request.xml";
            String path2 = "/home/dimm/jbproject/dai3_testers/requestFuzzy.xml";
            File file = new File(path1);
            if (!file.exists()) {
                System.err.println("File " + path1 + " not found.");
                return;
            }
            BufferedReader bufRead = new BufferedReader(new FileReader(file));
            StringBuffer performStrData = new StringBuffer();
            while (bufRead.ready()) {
                performStrData.append(bufRead.readLine());
            }
            bufRead.close();
            file = new File(path2);
            if (!file.exists()) {
                System.err.println("File " + path2 + " not found.");
                return;
            }
            bufRead = new BufferedReader(new FileReader(file));
            StringBuffer performStrFuzzy = new StringBuffer();
            while (bufRead.ready()) {
                performStrFuzzy.append(bufRead.readLine());
            }
            bufRead.close();
            URL serverBaseUrl = new URL("http://dimm.wdcb.ru:8080/dai/services");
            ResourceID drerId = new ResourceID("DataRequestExecutionResource");
            ServerProxy serverProxy = new ServerProxy();
            serverProxy.setDefaultBaseServicesURL(serverBaseUrl);
            DataRequestExecutionResource drer = serverProxy.getDataRequestExecutionResource(drerId);
            GetData getData = new GetData();
            getData.setResourceID("Ncep25FullResource");
            getData.getInput().add(new StringData(performStrData.toString()));
            Fuzzy fuzzy = new Fuzzy();
            fuzzy.setResourceID("Ncep25FullResource");
            fuzzy.getInputs()[0].add(new StringData(performStrFuzzy.toString()));
            fuzzy.getInputs()[1].connect(getData.getOutput());
            DeliverToRequestStatus deliverToRequestStatus = new DeliverToRequestStatus();
            deliverToRequestStatus.connectInput(fuzzy.getOutput());
            PipelineWorkflow pipeline = new PipelineWorkflow();
            pipeline.add(fuzzy);
            pipeline.add(getData);
            pipeline.add(deliverToRequestStatus);
            uk.org.ogsadai.client.toolkit.RequestResource rs = drer.execute(pipeline, RequestExecutionType.SYNCHRONOUS);
            System.out.println(deliverToRequestStatus.getDataValueIterator().next().toString());
        } catch (RequestExecutionException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClientToolkitException ex) {
            ex.printStackTrace();
        } catch (RequestException ex) {
            ex.printStackTrace();
        } catch (ClientException ex) {
            ex.printStackTrace();
        } catch (ResourceUnknownException ex) {
            ex.printStackTrace();
        } catch (ServerException ex) {
            ex.printStackTrace();
        } catch (ServerCommsException ex) {
            ex.printStackTrace();
        } catch (DataSourceUsageException ex) {
            ex.printStackTrace();
        } catch (UnexpectedDataValueException ex) {
            ex.printStackTrace();
        } catch (DataStreamErrorException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Main method.
     *
     * @param args command line arguments.
     *
     * @throws Exception if an unexpected error occurs
     */
    public static void main(String[] args) {
        FuzzyRun run = new FuzzyRun();
        run.go();
    }
}
