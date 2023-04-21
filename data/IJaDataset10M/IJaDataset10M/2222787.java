package net.sourceforge.openconferencer.client.edit;

import java.io.File;
import java.io.FileFilter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import net.sourceforge.openconferencer.client.ApplicationProperties;
import net.sourceforge.openconferencer.client.Constants;
import net.sourceforge.openconferencer.client.edit.Conference.Participant;
import net.sourceforge.openconferencer.client.util.LogHelper;

/**
 * @author aleksandar
 * 
 */
public class ConferenceManager implements IConferenceManager {

    private static final String DEFAULT_STORE_NAME = ".openconference.settings";

    private static final String EXTENSION = ".openconf";

    private static final FileFilter conferenceFileFilter = new FileFilter() {

        @Override
        public boolean accept(File pathname) {
            return pathname.getName().toLowerCase().endsWith(EXTENSION);
        }
    };

    private File archive;

    private JAXBContext jaxbContext;

    /**
	 * 
	 */
    public ConferenceManager() {
        initialize();
    }

    /**
	 * 
	 */
    protected void initialize() {
        ApplicationProperties props = ApplicationProperties.getInstance();
        String archiveName = props.getProperty(Constants.PROPERTY_STORE_ARCHIVE, DEFAULT_STORE_NAME);
        String homeDir = System.getProperty("user.home");
        File archiveFile = new File(homeDir, archiveName);
        if (!archiveFile.exists()) {
            archiveFile.mkdirs();
        }
        archive = archiveFile;
        try {
            jaxbContext = JAXBContext.newInstance(Conference.class, Participant.class);
        } catch (JAXBException e) {
            LogHelper.error("Failed to setup JAXB context for parsing conference files!!", e);
        }
    }

    public boolean exists(String name) {
        return new File(archive, name + EXTENSION).exists();
    }

    public String[] listAll() {
        File files[] = archive.listFiles(conferenceFileFilter);
        String names[] = new String[files.length];
        for (int i = 0; i < names.length; i++) {
            String fileName = files[i].getName();
            names[i] = fileName.substring(0, fileName.length() - EXTENSION.length());
        }
        return names;
    }

    public Conference load(String name) {
        File file = new File(archive, name + EXTENSION);
        if (!file.exists()) return null;
        Conference conf;
        try {
            conf = (Conference) jaxbContext.createUnmarshaller().unmarshal(file);
            conf.loaded = true;
            return conf;
        } catch (JAXBException e) {
            LogHelper.error("Failed to load conference: " + name, e);
        }
        return null;
    }

    public boolean save(Conference conference) {
        File file = new File(archive, conference.getName() + EXTENSION);
        try {
            if (!file.exists()) file.createNewFile();
            jaxbContext.createMarshaller().marshal(conference, file);
            return true;
        } catch (Exception ex) {
            LogHelper.error("Failed to save conference " + conference.getName(), ex);
        }
        return false;
    }

    public boolean delete(String name) {
        File file = new File(archive, name + EXTENSION);
        if (file.exists()) return file.delete();
        return false;
    }
}
