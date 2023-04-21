package uk.ac.ebi.intact.util.imex;

import uk.ac.ebi.imexcentral.wsclient.ImexCentralWebserviceClient;
import uk.ac.ebi.imexcentral.wsclient.generated.ImexCentralWebserviceException_Exception;
import uk.ac.ebi.imexcentral.wsclient.generated.PublicationUtil;
import uk.ac.ebi.intact.util.imex.exception.ImexFacadeException;
import uk.ac.ebi.intact.util.imex.model.ImexPublication;
import uk.ac.ebi.intact.util.imex.status.ImexPublicationStatus;
import uk.ac.ebi.intact.util.imex.status.ImexStatusStateDiagram;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: CatherineLeroy
 * Date: 09-Feb-2007
 * Time: 12:16:34
 * To change this template use File | Settings | File Templates.
 */
public class ImexWsClientFacade {

    private ImexCentralWebserviceClient imexClient;

    public ImexWsClientFacade() {
        this.imexClient = new ImexCentralWebserviceClient();
    }

    public ImexWsClientFacade(String webServiceUrl) {
        if (webServiceUrl == null) {
            throw new NullPointerException("You must give a non null Web Service URL.");
        }
        this.imexClient = new ImexCentralWebserviceClient(webServiceUrl);
    }

    /**
     * Given an ImexPublicationStatus (postpublication, prepublication...) it returns all the Publication in ImexCentral
     * having this publicationStatus.
     *
     * @param status one of the ImexPublicationStatus listed in the ImexStatusStatedDiagram (pre, postpublication,
     *               reserved..., ex : ImexStatusStateDiagram.getReserved())
     *
     * @return
     *
     * @throws ImexFacadeException
     */
    public List<PublicationUtil> getPublicationByStatus(ImexPublicationStatus status) throws ImexFacadeException {
        if (status == null) {
            throw new IllegalArgumentException("The status is null");
        }
        if (!ImexStatusStateDiagram.getStatusCol().contains(status)) {
            throw new IllegalArgumentException("The given status : " + status.getStatusName() + "is not a valid status");
        }
        List<PublicationUtil> publications = new ArrayList<PublicationUtil>();
        try {
            publications = imexClient.getPublicationsByStatus(status.getStatusName());
        } catch (RemoteException e) {
            throw new ImexFacadeException(e);
        } catch (ImexCentralWebserviceException_Exception e) {
            throw new ImexFacadeException(e);
        }
        return publications;
    }

    /**
     * Create in ImexCentral, a publication with the caracteristic given in argument.
     *
     * @param type,                    describe the source of the publication. Can be PubMed, DOI... see in ImexPublication
     * @param pubmedId,                id of the publication
     * @param status,                  status of the publication. See in ImexStatusStateDiagram (ex : ImexStatusStateDiagram.getReserved())
     * @param title,                   title of the publication
     * @param author,                  first author of the publication as it appears in the publication
     * @param releaseDate,             date at which the publication is released
     * @param expectedPublicationDate, if the publication status is postPublication one can specify the expected
     *                                 publication date.
     * @param publicationDate,         the date at which the publication has been publicated
     * @param loginId,                 imex login id of the curator by which this publication is created
     *
     * @return ImexPublication
     *
     * @throws ImexFacadeException
     */
    public ImexPublication createPublication(String type, String pubmedId, ImexPublicationStatus status, String title, String author, Date releaseDate, Date expectedPublicationDate, Date publicationDate, String loginId) throws ImexFacadeException {
        uk.ac.ebi.imexcentral.wsclient.generated.Publication publication = null;
        try {
            publication = imexClient.createPublication(type, pubmedId, status.getStatusName(), title, author, dateConvertor(releaseDate), dateConvertor(expectedPublicationDate), dateConvertor(publicationDate), loginId);
        } catch (ImexCentralWebserviceException_Exception e) {
            throw new ImexFacadeException(e);
        } catch (DatatypeConfigurationException e) {
            throw new ImexFacadeException(e);
        }
        return new ImexPublication(publication);
    }

    /**
     * @param pubmedId, id of the publication
     *
     * @return a list of PublicationUtil having as id, the pubmedId given in argument.
     *
     * @throws ImexFacadeException
     */
    public List<PublicationUtil> getPublicationById(String pubmedId) throws ImexFacadeException {
        return getPublicationById(pubmedId, "PUBMED");
    }

    /**
     * @param pubmedId, id of the publication
     * @param type,     describe the source of the publication. Can be PubMed, DOI... see in ImexPublication
     *                  (ex : ImexPublication.PUBMED_TYPE)
     *
     * @return
     *
     * @throws ImexFacadeException
     */
    public List<PublicationUtil> getPublicationById(String pubmedId, String type) throws ImexFacadeException {
        List<PublicationUtil> publicationUtils = null;
        try {
            publicationUtils = imexClient.getPublicationsById(pubmedId, type);
        } catch (ImexCentralWebserviceException_Exception e) {
            throw new ImexFacadeException(e);
        }
        return publicationUtils;
    }

    /**
     * Get all the publication in ImexCentral havent as author, the String given in argument and return them as a List.
     *
     * @param author, first author of the publication as it appears in the publication
     *
     * @return a list of PublicationUtil having as author the author String given as a parameter
     *
     * @throws ImexFacadeException
     */
    public List<PublicationUtil> getPublicationByAuthor(String author) throws ImexFacadeException {
        List<PublicationUtil> publicationUtils = null;
        try {
            publicationUtils = imexClient.getPublicationsByAuthor(author);
        } catch (ImexCentralWebserviceException_Exception e) {
            throw new ImexFacadeException(e);
        }
        return publicationUtils;
    }

    /**
     * Update the publication status, to the status given in argument if loginId is a valid login in Imex and of the
     * same organisation then the one of the curator who created the publication in ImexCentral.
     *
     * @param pubmedId, id of the publication
     * @param status,   status of the publication. See in ImexStatusStateDiagram (ex : ImexStatusStateDiagram.getReserved())
     * @param loginId,  imex login id of the curator by which this publication is created
     *
     * @throws ImexCentralWebserviceException_Exception
     *
     */
    public void updatePublicationStatus(String pubmedId, ImexPublicationStatus status, String loginId) throws ImexCentralWebserviceException_Exception {
        imexClient.updatePublicationStatus(pubmedId, status.getStatusName(), loginId);
    }

    /**
     * Given a Date it an XmlGregorianCalendar with it's date set to date. If date is null, returns null.
     *
     * @param date
     *
     * @return an XMLGregorianCalendar
     *
     * @throws DatatypeConfigurationException
     */
    private XMLGregorianCalendar dateConvertor(Date date) throws DatatypeConfigurationException {
        if (date == null) {
            return null;
        }
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(date);
        DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
        XMLGregorianCalendar xmlGregorianCalendar = datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
        return xmlGregorianCalendar;
    }
}
