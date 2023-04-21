package ispyb.server.webservice.WebService;

import ispyb.common.util.PropertyLoader;
import ispyb.server.data.interfaces.BlsampleFacadeLocal;
import ispyb.server.data.interfaces.BlsampleFacadeUtil;
import ispyb.server.data.interfaces.BlsampleValue;
import ispyb.server.data.interfaces.ContainerFacadeLocal;
import ispyb.server.data.interfaces.ContainerFacadeUtil;
import ispyb.server.data.interfaces.CrystalFacadeLocal;
import ispyb.server.data.interfaces.CrystalFacadeUtil;
import ispyb.server.data.interfaces.CrystalLightValue;
import ispyb.server.data.interfaces.CrystalValue;
import ispyb.server.data.interfaces.DataCollectionFacadeLocal;
import ispyb.server.data.interfaces.DataCollectionFacadeUtil;
import ispyb.server.data.interfaces.DataCollectionLightValue;
import ispyb.server.data.interfaces.DataCollectionValue;
import ispyb.server.data.interfaces.ImageFacadeLocal;
import ispyb.server.data.interfaces.ImageFacadeUtil;
import ispyb.server.data.interfaces.ImageLightValue;
import ispyb.server.data.interfaces.LaboratoryLightValue;
import ispyb.server.data.interfaces.PersonFacadeLocal;
import ispyb.server.data.interfaces.PersonFacadeUtil;
import ispyb.server.data.interfaces.PersonValue;
import ispyb.server.data.interfaces.ProposalFacadeLocal;
import ispyb.server.data.interfaces.ProposalFacadeUtil;
import ispyb.server.data.interfaces.ProposalLightValue;
import ispyb.server.data.interfaces.ProposalValue;
import ispyb.server.data.interfaces.ProteinFacadeLocal;
import ispyb.server.data.interfaces.ProteinFacadeUtil;
import ispyb.server.data.interfaces.ProteinLightValue;
import ispyb.server.data.interfaces.SessionFacadeLocal;
import ispyb.server.data.interfaces.SessionFacadeUtil;
import ispyb.server.data.interfaces.SessionLightValue;
import ispyb.server.data.interfaces.SessionValue;
import ispyb.server.util.PathUtils;
import ispyb.server.webservice.DBAcess.DB2Xml;
import ispyb.server.webservice.DBAcess.DBTools_EJB;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import uk.ac.ehtpx.model.BeamlineExportedInformation;
import uk.ac.ehtpx.model.DataCollectionInformation;
import uk.ac.ehtpx.model.ObjectFactory;
import uk.ac.ehtpx.model.SessionsList;
import com.ice.tar.TarArchive;
import com.ice.tar.TarEntry;

public class SessionExporter implements Runnable {

    Properties mProp = PropertyLoader.loadProperties("ispyb/server/webservice/WebService/conf/ISPyB");

    private Integer mSessionId = null;

    private String mFinalTarFilePath = new String();

    private DBTools_EJB mDBTools;

    public SessionExporter(Integer sessionId, String finalTarFilePath) {
        this.mSessionId = sessionId;
        this.mFinalTarFilePath = finalTarFilePath;
    }

    public void run() {
        try {
            this.ExportSession();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
 * ExportSessionAsXML
 * @return
 * @throws Exception
 */
    public String ExportSessionAsXML() throws Exception {
        String response = new String("No Information or no Session : " + mSessionId);
        boolean isWindows = (System.getProperty("os.name").indexOf("Win") != -1) ? true : false;
        String dumpDirectory = ((isWindows) ? mProp.getProperty("mysqldump.outputFilePath.windows") : mProp.getProperty("mysqldump.outputFilePath"));
        String dumpPath = dumpDirectory + File.separator + this.mSessionId + File.separator;
        String xmlFilePath = dumpPath + this.mSessionId + ".xml";
        File xmlFile = new File(xmlFilePath);
        new File(dumpPath).mkdirs();
        FileOutputStream outStream = new FileOutputStream(xmlFile);
        PrintWriter fout = new PrintWriter(outStream);
        StringWriter sout = new StringWriter();
        ObjectFactory factory = new ObjectFactory();
        try {
            SessionFacadeLocal _session = SessionFacadeUtil.getLocalHome().create();
            PersonFacadeLocal _person = PersonFacadeUtil.getLocalHome().create();
            BlsampleFacadeLocal _sample = BlsampleFacadeUtil.getLocalHome().create();
            CrystalFacadeLocal _crystal = CrystalFacadeUtil.getLocalHome().create();
            ImageFacadeLocal _image = ImageFacadeUtil.getLocalHome().create();
            DB2Xml mDB2Xml = new DB2Xml();
            BeamlineExportedInformation ispybExportedInformation = factory.createBeamlineExportedInformationElement();
            JAXBContext jc = JAXBContext.newInstance("uk.ac.ehtpx.model");
            Unmarshaller u = jc.createUnmarshaller();
            StringBuffer xmlStr;
            SessionValue session = _session.findByPrimaryKey(this.mSessionId);
            if (session != null) {
                uk.ac.ehtpx.model.Session jaxbSession = mDB2Xml.toSession((SessionLightValue) session);
                ispybExportedInformation.setSession(jaxbSession);
            }
            ProposalLightValue proposal = session.getProposal();
            uk.ac.ehtpx.model.Proposal jaxbProposal = mDB2Xml.toProposal(proposal);
            ispybExportedInformation.setProposal(jaxbProposal);
            PersonValue person = _person.findByPrimaryKey(proposal.getProposalId());
            uk.ac.ehtpx.model.Person jaxbPerson = mDB2Xml.toPerson(person);
            LaboratoryLightValue laboratory = person.getLaboratory();
            uk.ac.ehtpx.model.Laboratory jaxbLaboratory = mDB2Xml.toLaboratory(laboratory);
            jaxbLaboratory.getPerson().add(jaxbPerson);
            ispybExportedInformation.setLaboratory(jaxbLaboratory);
            DataCollectionLightValue dataCollections[] = session.getDataCollections();
            DataCollectionLightValue _ejbDataCollection = null;
            ImageLightValue _ejbImage = null;
            for (int d = 0; d < dataCollections.length; d++) {
                DataCollectionInformation jaxbDataCollectionInformation = factory.createDataCollectionInformation();
                _ejbDataCollection = (DataCollectionLightValue) dataCollections[d];
                uk.ac.ehtpx.model.DataCollection jaxbDataCollection = mDB2Xml.toDataCollection(_ejbDataCollection);
                jaxbDataCollectionInformation.setDataCollection(jaxbDataCollection);
                BlsampleValue sample = null;
                if (_ejbDataCollection.getBlSampleId() != null) sample = _sample.findByPrimaryKey(_ejbDataCollection.getBlSampleId());
                if (sample != null) {
                    uk.ac.ehtpx.model.BLSample jaxbBLSample = mDB2Xml.toBLSample(sample);
                    jaxbDataCollectionInformation.setBlSample(jaxbBLSample);
                    CrystalLightValue _ejbCrystal = sample.getCrystal();
                    CrystalValue ejbCrystal = _crystal.findByPrimaryKey(_ejbCrystal.getCrystalId());
                    uk.ac.ehtpx.model.Crystal jaxbCrystal = null;
                    jaxbCrystal = mDB2Xml.toCrystal(_ejbCrystal);
                    ProteinLightValue _ejbProteinProtein = ejbCrystal.getProtein();
                    uk.ac.ehtpx.model.Protein jaxbProtein = mDB2Xml.toProtein(_ejbProteinProtein);
                    jaxbCrystal.setProtein(jaxbProtein);
                    jaxbDataCollectionInformation.setBlSample(jaxbBLSample);
                    jaxbDataCollectionInformation.setCrystal(jaxbCrystal);
                }
                List images = (List) _image.findByDataCollectionId(_ejbDataCollection.getDataCollectionId());
                for (int i = 0; i < images.size(); i++) {
                    _ejbImage = (ImageLightValue) images.get(i);
                    uk.ac.ehtpx.model.Image jaxbImage = mDB2Xml.toImage(_ejbImage);
                    jaxbDataCollectionInformation.getImage().add(jaxbImage);
                }
                ispybExportedInformation.getDataCollectionInformation().add(jaxbDataCollectionInformation);
            }
            Marshaller m = jc.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            m.marshal(ispybExportedInformation, fout);
            fout.close();
            m.marshal(ispybExportedInformation, sout);
            sout.close();
            response = sout.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public String GetSessionsList(String proposalCodeAndNumber) throws Exception {
        StringWriter response = new StringWriter();
        response.write("No Sessions for Proposal : " + mSessionId);
        uk.ac.ehtpx.model.ObjectFactory factory = new ObjectFactory();
        this.mDBTools = new DBTools_EJB();
        DB2Xml db2xml = new DB2Xml();
        ProposalFacadeLocal _proposal = ProposalFacadeUtil.getLocalHome().create();
        ProposalLightValue _ejbPorposal = this.mDBTools.GetProposal(proposalCodeAndNumber);
        ProposalValue ejbProposal = null;
        if (_ejbPorposal != null) {
            ejbProposal = _proposal.findByPrimaryKey(_ejbPorposal.getProposalId());
            SessionLightValue ejbSessions[] = ejbProposal.getSessions();
            SessionsList sessionsList = factory.createSessionsListElement();
            for (int s = 0; s < ejbSessions.length; s++) {
                uk.ac.ehtpx.model.Session se = db2xml.toSession((SessionLightValue) ejbSessions[s]);
                sessionsList.getSession().add(se);
            }
            JAXBContext jc = JAXBContext.newInstance("uk.ac.ehtpx.model");
            Marshaller m = jc.createMarshaller();
            response = new StringWriter();
            m.marshal(sessionsList, response);
        }
        return response.toString();
    }

    public void ExportSession() throws Exception {
        String OSname = System.getProperty("os.name");
        boolean isWindows = (OSname.indexOf("Win") != -1) ? true : false;
        String dumpDirectory = ((isWindows) ? mProp.getProperty("mysqldump.outputFilePath.windows") : mProp.getProperty("mysqldump.outputFilePath"));
        String dumpPath = dumpDirectory + File.separator + this.mSessionId + File.separator;
        String mysqldumpFile = dumpPath + this.mSessionId + ".sql";
        String tarFile = dumpDirectory + File.separator + this.mSessionId;
        boolean success = deleteDirectory((new File(dumpPath)));
        success = (new File(dumpPath)).mkdirs();
        success = (new File(mysqldumpFile)).delete();
        FileOutputStream outStream = new FileOutputStream(new File(mysqldumpFile));
        PrintWriter fout = new PrintWriter(outStream);
        this.ExportSessionAsXML();
        String cmdOut = null;
        SessionFacadeLocal sessionF = SessionFacadeUtil.getLocalHome().create();
        SessionValue session = sessionF.findByPrimaryKey(this.mSessionId);
        ProposalFacadeLocal proposalF = ProposalFacadeUtil.getLocalHome().create();
        ProposalValue proposal = proposalF.findByPrimaryKey(session.getProposalId());
        DataCollectionFacadeLocal dataCollectionF = DataCollectionFacadeUtil.getLocalHome().create();
        BlsampleFacadeLocal sampleF = BlsampleFacadeUtil.getLocalHome().create();
        CrystalFacadeLocal crystalF = CrystalFacadeUtil.getLocalHome().create();
        ProteinFacadeLocal proteinF = ProteinFacadeUtil.getLocalHome().create();
        ContainerFacadeLocal containerF = ContainerFacadeUtil.getLocalHome().create();
        int nbDataCol = session.getDataCollections().length;
        String tarFileFullPath = tarFile + ".tar";
        String zipFileFullPath = tarFile + ".zip";
        FileOutputStream fosTar = new FileOutputStream(tarFileFullPath);
        TarArchive tarArchive = new TarArchive(fosTar);
        nbDataCol = session.getDataCollections().length;
        ArrayList syncedDirectoires = new ArrayList(nbDataCol);
        for (int d = 0; d < nbDataCol; d++) {
            DataCollectionLightValue dataCollectionLight = session.getDataCollections()[d];
            DataCollectionValue dataCollection = dataCollectionF.findByPrimaryKey(dataCollectionLight.getDataCollectionId());
            String imageDirectory = dataCollection.getImageDirectory();
            String fullDNAPath = PathUtils.GetFullDNAPath(dataCollection.getDataCollectionId());
            if (!syncedDirectoires.contains(fullDNAPath)) {
                syncedDirectoires.add(fullDNAPath);
                File dnaFilePath = new File(fullDNAPath);
                if (dnaFilePath.exists()) {
                    File dnaContent = dnaFilePath;
                    File[] dnaFiles = dnaContent.listFiles();
                    if (dnaFiles != null) for (int f = 0; f < dnaFiles.length; f++) {
                        try {
                            File dnaFile = dnaFiles[f];
                            if (dnaFile.isFile()) {
                                tarArchive.writeEntry(new TarEntry(dnaFile), false);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            if (!syncedDirectoires.contains(imageDirectory)) {
                syncedDirectoires.add(imageDirectory);
                File jpegContent = new File(((isWindows) ? "Y:" : "") + imageDirectory);
                File[] jpegImages = jpegContent.listFiles();
                if (jpegImages != null) for (int f = 0; f < jpegImages.length; f++) {
                    try {
                        File jpegImage = jpegImages[f];
                        String fileName = jpegImage.getName();
                        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
                        if (jpegImage.isFile() && (fileExtension.equals("jpeg") || fileExtension.equals("xml"))) {
                            tarArchive.writeEntry(new TarEntry(jpegImage), false);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        tarArchive.writeEntry(new TarEntry(new File(dumpPath)), true);
        tarArchive.closeArchive();
        File newTarFile = new File(tarFileFullPath);
        String finalTarFileName = mFinalTarFilePath + File.separator + newTarFile.getName();
        File finalTarFile = new File(finalTarFileName);
        if (newTarFile.exists()) {
            if (finalTarFile.exists()) finalTarFile.delete();
            copyFile(newTarFile, finalTarFile);
        }
        success = deleteDirectory((new File(dumpPath)));
        success = newTarFile.delete();
    }

    public static void copyFile(File source, File dest) throws IOException {
        FileChannel in = null, out = null;
        try {
            in = new FileInputStream(source).getChannel();
            out = new FileOutputStream(dest).getChannel();
            long size = in.size();
            MappedByteBuffer buf = in.map(FileChannel.MapMode.READ_ONLY, 0, size);
            out.write(buf);
        } finally {
            if (in != null) in.close();
            if (out != null) out.close();
        }
    }

    private String GetCmd(String tableName_and_Options, int tmpFileNumber) {
        String cmd;
        String OSname = System.getProperty("os.name");
        boolean isWindows = (OSname.indexOf("Win") != -1) ? true : false;
        String mysqldump = (isWindows) ? mProp.getProperty("mysqldump.path.windows") : mProp.getProperty("mysqldump.path");
        String dumpDirectory = ((isWindows) ? mProp.getProperty("mysqldump.outputFilePath.windows") : mProp.getProperty("mysqldump.outputFilePath"));
        String dumpPath = dumpDirectory + File.separator + this.mSessionId + File.separator;
        String mysqldumpArguments = " --result-file=" + dumpPath + tmpFileNumber + " " + mProp.getProperty("mysqldump.arguments");
        cmd = mysqldump + " " + mysqldumpArguments + tableName_and_Options;
        return cmd;
    }

    public String CmdExec(String cmdline, boolean captureOutput) {
        String output = new String();
        try {
            String line;
            Process p = Runtime.getRuntime().exec(cmdline);
            if (captureOutput) {
                BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
                while ((line = input.readLine()) != null) {
                    output += line;
                }
                input.close();
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
        return output;
    }

    private void buildSqlOutput(PrintWriter fout, String dumpPath, int tmpFileNumber) throws Exception {
        String line;
        String fileName = dumpPath + File.separator + tmpFileNumber;
        BufferedReader rdr = new BufferedReader(new FileReader(fileName));
        while ((line = rdr.readLine()) != null) {
            fout.println(line);
        }
        rdr.close();
        new File(fileName).delete();
    }

    public static boolean deleteDirectory(File path) {
        if (path.exists()) {
            File[] files = path.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
        return (path.delete());
    }
}
