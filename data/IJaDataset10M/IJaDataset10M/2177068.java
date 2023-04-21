package org.bejug.javacareers.jobs.service;

import org.bejug.javacareers.common.search.SearchCriteria;
import org.bejug.javacareers.common.search.SearchCriteriaService;
import org.bejug.javacareers.jobs.dao.OfferDao;
import org.bejug.javacareers.jobs.model.AcademicEducationOffer;
import org.bejug.javacareers.jobs.model.CommercialEducationOffer;
import org.bejug.javacareers.jobs.model.EducationOffer;
import java.util.List;

/**
 * Concrete implementation of the educationservice.
 *
 * @author Bart Meyers (Last modified by $Author: stephan_janssen $)
 * @version $Revision: 1.2 $ - $Date: 2005/10/11 09:09:14 $
 */
public class EducationServiceImpl implements EducationService {

    /**
     * The SearchcriteriaService.
     */
    private SearchCriteriaService searchCriteriaService;

    /**
     * the academiceducationofferdao.
     */
    private OfferDao academicEducationOfferDao = null;

    /**
     * the commercialeducationofferdao.
     */
    private OfferDao commercialEducationOfferDao = null;

    /**
     * Constructor.
     * @param academicEducationOfferDao the needed academicEducationOfferDao.
     * @param commercialEducationOfferDao the needed commercialEducationOfferDao.
     * @param searchCriteriaService the searchCriteriaService.
     */
    public EducationServiceImpl(OfferDao academicEducationOfferDao, OfferDao commercialEducationOfferDao, SearchCriteriaService searchCriteriaService) {
        this.searchCriteriaService = searchCriteriaService;
        this.academicEducationOfferDao = academicEducationOfferDao;
        this.commercialEducationOfferDao = commercialEducationOfferDao;
    }

    /**
     * set the academicEducationOfferDao.
     * @param academicEducationOfferDao the dao to set.
     */
    public void setAcademicEducationOfferDao(OfferDao academicEducationOfferDao) {
        this.academicEducationOfferDao = academicEducationOfferDao;
    }

    /**
     * set the commercialEducationOfferDao.
     * @param commercialEducationOfferDao the dao to set
     */
    public void setCommercialEducationOfferDao(OfferDao commercialEducationOfferDao) {
        this.commercialEducationOfferDao = commercialEducationOfferDao;
    }

    /**
     * {@inheritDoc}
     */
    public void storeEducationOffer(EducationOffer offer) {
        commercialEducationOfferDao.store(offer);
    }

    /**
     * {@inheritDoc}
     */
    public void deleteEducationOffer(EducationOffer offer) {
        commercialEducationOfferDao.deleteOffer(offer);
    }

    /**
     * {@inheritDoc}
     */
    public List getAcademicEducationOffers() {
        return academicEducationOfferDao.getOffers();
    }

    /**
     * {@inheritDoc}
     */
    public List getCommercialEducationOffers() {
        return commercialEducationOfferDao.getOffers();
    }

    /**
     * {@inheritDoc}
     */
    public AcademicEducationOffer getAcademicEducationOffer(Integer id) {
        return (AcademicEducationOffer) academicEducationOfferDao.getOffer(id);
    }

    /**
     * {@inheritDoc}
     */
    public CommercialEducationOffer getCommercialEducationOffer(Integer id) {
        return (CommercialEducationOffer) commercialEducationOfferDao.getOffer(id);
    }

    /**
     * {@inheritDoc}
     */
    public int getCommercialEducationsCount() {
        return commercialEducationOfferDao.getOfferCount();
    }

    /**
     * {@inheritDoc}
     */
    public int getAcademicEducationsCount() {
        return academicEducationOfferDao.getOfferCount();
    }

    /**
     * {@inheritDoc}
     */
    public int getEducationsCount() {
        return commercialEducationOfferDao.getOfferCount() + academicEducationOfferDao.getOfferCount();
    }

    /**
     * {@inheritDoc}
     */
    public void setSearchCriteriaService(SearchCriteriaService searchCriteriaService) {
        this.searchCriteriaService = searchCriteriaService;
    }

    /**
     * {@inheritDoc}
     */
    public List getCommercialEducationOffers(SearchCriteria searchCriteria) throws IllegalArgumentException {
        if (searchCriteria.getClazz().equals(CommercialEducationOffer.class)) {
            return searchCriteriaService.executeQuery(searchCriteria);
        }
        throw new IllegalArgumentException("invalid class type");
    }

    /**
     * {@inheritDoc}
     */
    public List getAcademicEducationOffers(SearchCriteria searchCriteria) throws IllegalArgumentException {
        if (searchCriteria.getClazz().equals(AcademicEducationOffer.class)) {
            return searchCriteriaService.executeQuery(searchCriteria);
        }
        throw new IllegalArgumentException("invalid class type");
    }
}
