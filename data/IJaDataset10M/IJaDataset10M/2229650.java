package br.ufal.tci.nexos.arcolive.service.fileshare;

import java.io.IOException;
import br.ufal.tci.nexos.arcolive.ArCoLIVEServerProperties;
import br.ufal.tci.nexos.arcolive.ArCoLIVEServiceInstance;
import br.ufal.tci.nexos.arcolive.exception.ArCoLIVECannotAddParticipantException;
import br.ufal.tci.nexos.arcolive.exception.ArCoLIVECannotStartServiceException;
import br.ufal.tci.nexos.arcolive.exception.ArCoLIVECannotStopServiceException;
import br.ufal.tci.nexos.arcolive.participant.ArCoLIVEParticipant;

/**
 * FileShare.java
 * 
 * This class represents a instance of the fileshare service. It implements the
 * methods of control of a basic service and adds some methods to exchange messages
 * @see ArCoLIVEServiceInstance for more details about service instances classes
 * 
 * @see 30/07/2007
 * 
 * @author <a href="mailto:txithihausen@gmail.com">Ivo Augusto Andrade R Calado</a>.
 * @author <a href="mailto:thiagobrunoms@gmail.com">Thiago Bruno Melo de Sales</a>.
 * @since 0.1
 * @version 0.1
 * 
 * <p>
 * <b>Revisions:</b>
 * 
 * <p>
 * <b>yyyymmdd USERNAME:</b>
 * <ul>
 * <li> VERSION
 * </ul>
 */
public class FileShare extends ArCoLIVEServiceInstance {

    private FileShareImpl fileShareImpl;

    @Override
    public void addParticipant(ArCoLIVEParticipant participant) throws ArCoLIVECannotAddParticipantException {
        super.participantGroup.addParticipant(participant);
    }

    @Override
    public void removeParticipant(ArCoLIVEParticipant participant) {
        super.participantGroup.removeParticipant(participant);
    }

    @Override
    public void startService() throws ArCoLIVECannotStartServiceException {
        long timeOutUpload = Long.valueOf(ArCoLIVEServerProperties.getInstance().getStringProperty("fileShare.timeOut.upload"));
        long timeOutDownload = Long.valueOf(ArCoLIVEServerProperties.getInstance().getStringProperty("fileShare.timeOut.download"));
        int minServerPort = ArCoLIVEServerProperties.getInstance().getIntProperty("fileShare.minServerPort");
        int maxServerPort = ArCoLIVEServerProperties.getInstance().getIntProperty("fileShare.maxServerPort");
        long maxFileLenght = Long.valueOf(ArCoLIVEServerProperties.getInstance().getStringProperty("fileShare.maxFileLenght"));
        String announce = "400#commandId=402#instanceId=" + this.getServiceInstanceDescriptor().getServiceInstanceId();
        fileShareImpl = new FileShareImpl(timeOutUpload, timeOutDownload, minServerPort, maxServerPort, maxFileLenght, this.participantGroup, announce);
        this.setServiceStatus(1);
    }

    @Override
    public void stopService() throws ArCoLIVECannotStopServiceException {
        this.setServiceStatus(0);
        try {
            fileShareImpl.unregistryAndCloseAllServers();
        } catch (IOException e) {
            throw new ArCoLIVECannotStopServiceException("File Share Servers couldn't be stopped", e.getCause());
        }
    }
}
