package dendrarium.commandline;

import dendrarium.core.entities.Packet;
import dendrarium.core.entities.Task;
import dendrarium.core.entities.TaskBusinessProcess;
import dendrarium.muzg.MUZG;
import dendrarium.portal.admin.PacketXML;
import dendrarium.portal.admin.PacketXMLParser;
import dendrarium.trees.Forest;
import dendrarium.trees.xml.TreeXMLParser;
import dendrarium.utils.StreamUtils;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author pta
 */
public class CommandLineUpload {

    private Session session;

    private File directory;

    public CommandLineUpload(Session session) {
        this.session = session;
    }

    public void uploadForest(File forestXML, Packet packet, int sentenceNo) throws Exception {
        System.err.println("\t\tUploading forest: " + forestXML.getName());
        byte[] forest = StreamUtils.obtainByteDataFromStream(new FileInputStream(forestXML));
        Task newTask = new Task();
        newTask.setForestXML(new String(forest, "UTF-8"));
        Forest f = null;
        f = new TreeXMLParser().parse(new ByteArrayInputStream(forest));
        newTask.setSentence(f.getText());
        newTask.setSent_id(f.getSent_id());
        newTask.setGrammar_no(f.getGrammar_no());
        if (newTask.getGrammar_no() == 0) {
            System.err.println("\t\tWARNING: Zdanie: " + newTask.getSent_id() + " nie posiada identyfikatora wersji gramatyki");
            throw new Exception("Zdanie: " + newTask.getSent_id() + " nie posiada identyfikatora wersji gramatyki");
        }
        TaskBusinessProcess previousVersion = getPreviousVersion(newTask.getSent_id());
        if (previousVersion != null) {
            System.err.println("\t\tERROR: Zdanie " + newTask.getSent_id() + " jest już w systemie");
            throw new Exception("Zdanie " + newTask.getSent_id() + " jest już w systemie");
        }
        TaskBusinessProcess tbp = new TaskBusinessProcess(packet, newTask);
        tbp.setIndex(sentenceNo);
        packet.getParagraphText().add(newTask.getSentence());
        if (f.isEmpty()) {
            packet.setUrbanizacja(packet.getUrbanizacja() + 1);
        }
        tbp.setCurrent(true);
        tbp.updateState();
        session.persist(tbp);
    }

    public void uploadPacket(File packetXMLFile) throws Exception {
        System.err.println("\tUploading packet: " + packetXMLFile.getName());
        PacketXML packetXML = new PacketXMLParser().parse(new FileInputStream(packetXMLFile));
        Packet packet = new Packet();
        packet.setName(packetXML.getName());
        session.persist(packet);
        int sentenceNo = 0;
        for (String forest : packetXML.getForestFiles()) {
            uploadForest(new File(directory.getPath() + File.separator + forest), packet, sentenceNo);
            sentenceNo++;
        }
    }

    public void uploadDirectory(File directory) {
        uploadDirectory(directory, false);
    }

    private Boolean uploadDirectory(File directory, Boolean recur) {
        this.directory = directory;
        System.err.println("Uploading directory: " + directory.getName());
        File[] packetXMLs = directory.listFiles(new FilenameFilter() {

            public boolean accept(File arg0, String arg1) {
                return arg1.endsWith(".packet.xml");
            }
        });
        if (!recur) {
            session.beginTransaction();
        }
        for (File packetXML : packetXMLs) {
            try {
                uploadPacket(packetXML);
            } catch (Exception ex) {
                ex.printStackTrace();
                session.getTransaction().rollback();
                return false;
            }
        }
        for (File subdirectory : directory.listFiles()) {
            if (subdirectory.isDirectory()) {
                if (!uploadDirectory(subdirectory, true)) return false;
            }
        }
        if (!recur) {
            session.getTransaction().commit();
            session.flush();
            session.clear();
        }
        return true;
    }

    public void updateForest(File forestXML) throws Exception {
        System.err.println("\t\tUploading forest: " + forestXML.getName());
        byte[] forest = StreamUtils.obtainByteDataFromStream(new FileInputStream(forestXML));
        Task newTask = new Task();
        newTask.setForestXML(new String(forest, "UTF-8"));
        Forest f = null;
        try {
            f = new TreeXMLParser().parse(new ByteArrayInputStream(forest));
        } catch (Exception ex) {
            System.out.println("Nie udało się dodać zadania: " + ex.getMessage());
            throw ex;
        }
        newTask.setSentence(f.getText());
        newTask.setSent_id(f.getSent_id());
        newTask.setGrammar_no(f.getGrammar_no());
        if (newTask.getGrammar_no() == 0) {
            System.out.println("Zdanie: " + newTask.getSent_id() + " nie posiada identyfikatora wersji gramatyki");
            throw new Exception("Zdanie: " + newTask.getSent_id() + " nie posiada identyfikatora wersji gramatyki");
        }
        TaskBusinessProcess previousVersion = getPreviousVersion(newTask.getSent_id());
        if (previousVersion == null) {
            System.out.println("Zdania " + newTask.getSent_id() + " nie znaleziono w systemie");
            throw new Exception("Zdania " + newTask.getSent_id() + " nie znaleziono w systemie");
        }
        if (newTask.getGrammar_no() <= previousVersion.getTask().getGrammar_no()) {
            System.out.println("Zdanie: " + newTask.getSent_id() + " jest w systemie w takiej samej lub nowszej wersji gramatyki");
            throw new Exception("Zdanie: " + newTask.getSent_id() + " jest w systemie w takiej samej lub nowszej wersji gramatyki");
        }
        Packet packet = previousVersion.getPacket();
        Forest oldF = null;
        try {
            oldF = new TreeXMLParser().parse(previousVersion.getTask().getForestXML());
        } catch (Exception ex) {
            System.out.println("Nie udało się dodać zadania: " + ex.getMessage());
            throw ex;
        }
        packet.setUrbanizacja(packet.getUrbanizacja() + (f.isEmpty() ? 1 : 0) - (oldF.isEmpty() ? 1 : 0));
        TaskBusinessProcess tbp = new TaskBusinessProcess(packet, newTask);
        tbp.setIndex(previousVersion.getIndex());
        tbp.setCurrent(true);
        session.persist(tbp);
        previousVersion.setCurrent(false);
        MUZG muzg = new MUZG();
        muzg.session = session;
        String muzgMsg = muzg.adapt(previousVersion, tbp);
        System.out.println("Uaktualniono zdanie " + tbp.getIndex() + "/" + tbp.getPacket().getName());
        if (!muzgMsg.equals("")) {
            System.out.println("Komunikat MUZGu: " + muzgMsg);
        }
    }

    /**
     * Dodaje zbior nowych wersji zdan
     */
    public void updateDirectory(File directory) {
        updateDirectory(directory, false);
    }

    private Boolean updateDirectory(File directory, Boolean recur) {
        this.directory = directory;
        System.err.println("Updating from directory: " + directory.getName());
        File[] forestXMLs = directory.listFiles(new FilenameFilter() {

            public boolean accept(File arg0, String arg1) {
                return arg1.endsWith(".xml") && !arg1.endsWith(".packet.xml");
            }
        });
        if (!recur) {
            session.beginTransaction();
        }
        for (File forestXML : forestXMLs) {
            try {
                updateForest(forestXML);
            } catch (Exception e) {
                System.err.println("Uaktualnienie nie powiodło się: " + e.getMessage());
                session.getTransaction().rollback();
                return false;
            }
        }
        for (File subdirectory : directory.listFiles()) {
            if (subdirectory.isDirectory()) {
                if (!updateDirectory(subdirectory, true)) return false;
            }
        }
        if (!recur) {
            session.getTransaction().commit();
            session.flush();
            session.clear();
        }
        return true;
    }

    private TaskBusinessProcess getPreviousVersion(String sent_id) {
        List<TaskBusinessProcess> prev = session.createQuery("from TaskBusinessProcess tbp where tbp.task.sent_id=:sid order by grammar_no desc").setString("sid", sent_id).setMaxResults(1).list();
        if (!prev.isEmpty()) {
            return prev.get(0);
        } else {
            return null;
        }
    }
}
