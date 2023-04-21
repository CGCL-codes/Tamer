package it.webscience.kpeople.ega.core.errorService.errorServiceInterface;

/**
 * @author XPMUser
 */
public interface ErrorService {

    /**
     * @param e
     *            eccezione.
     */
    void sendErrorNotification(Exception e);
}
