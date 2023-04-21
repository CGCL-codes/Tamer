package edu.kit.cm.kitcampusguide.ws.poi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import edu.kit.cm.kitcampusguide.dao.PoiDao;
import edu.kit.cm.kitcampusguide.dao.exception.PoiDaoException;
import edu.kit.cm.kitcampusguide.model.POI;
import edu.kit.cm.kitcampusguide.service.security.SecurityService;
import edu.kit.cm.kitcampusguide.ws.poi.util.PoiConverter;
import edu.kit.tm.cm.kitcampusguide.poiservice.CreateRequestComplexType;
import edu.kit.tm.cm.kitcampusguide.poiservice.CreateResponseComplexType;
import edu.kit.tm.cm.kitcampusguide.poiservice.DeleteRequestComplexType;
import edu.kit.tm.cm.kitcampusguide.poiservice.DeleteResponseComplexType;
import edu.kit.tm.cm.kitcampusguide.poiservice.ExecuteFault;
import edu.kit.tm.cm.kitcampusguide.poiservice.ExecuteRequestComplexType;
import edu.kit.tm.cm.kitcampusguide.poiservice.ExecuteResponseComplexType;
import edu.kit.tm.cm.kitcampusguide.poiservice.Ids;
import edu.kit.tm.cm.kitcampusguide.poiservice.Names;
import edu.kit.tm.cm.kitcampusguide.poiservice.PoiService;
import edu.kit.tm.cm.kitcampusguide.poiservice.PoiWithId;
import edu.kit.tm.cm.kitcampusguide.poiservice.ReadRequestComplexType;
import edu.kit.tm.cm.kitcampusguide.poiservice.ReadResponseComplexType;
import edu.kit.tm.cm.kitcampusguide.poiservice.SelectRequestComplexType;
import edu.kit.tm.cm.kitcampusguide.poiservice.SelectResponseComplexType;
import edu.kit.tm.cm.kitcampusguide.poiservice.Strings;
import edu.kit.tm.cm.kitcampusguide.poiservice.UpdateRequestComplexType;
import edu.kit.tm.cm.kitcampusguide.poiservice.UpdateResponseComplexType;

/**
 * Facade for cruds access to database through service request.
 * 
 * @author Roland Steinegger, Karlsruhe Institute of Technology
 */
@Repository
@Transactional
public class PoiFacadeImpl implements PoiService, PoiFacade {

    private static final Logger log = Logger.getLogger(PoiFacadeImpl.class);

    @Autowired
    private SecurityService securityService;

    @Autowired
    private PoiDao dao;

    public PoiFacadeImpl() {
    }

    public PoiFacadeImpl(PoiDao dao) {
        this.dao = dao;
    }

    public ExecuteResponseComplexType execute(ExecuteRequestComplexType parameters) throws ExecuteFault {
        log.debug("Handling execute request.");
        if (securityService.getSecurityContext().getAuthentication() != null) {
            Authentication authentication = securityService.getSecurityContext().getAuthentication();
            log.debug("auth is set: " + authentication.getName());
            if (authentication.getPrincipal() != null) {
                log.debug("principal is set: " + authentication.getPrincipal());
            }
        }
        final List<Object> responses = new ArrayList<Object>();
        log.debug("Found " + parameters.getCreateRequestsOrReadRequestsOrUpdateRequests().size() + " inner requests for processing.");
        for (Object request : parameters.getCreateRequestsOrReadRequestsOrUpdateRequests()) {
            responses.add(handleRequestAndGetResponse(request));
        }
        final ExecuteResponseComplexType executeResponse = new ExecuteResponseComplexType();
        executeResponse.getCreateResponsesOrReadResponsesOrUpdateResponses().addAll(responses);
        return executeResponse;
    }

    private Object handleRequestAndGetResponse(Object request) throws ExecuteFault {
        Object response;
        if (request instanceof CreateRequestComplexType) {
            response = create((CreateRequestComplexType) request);
        } else if (request instanceof ReadRequestComplexType) {
            response = read((ReadRequestComplexType) request);
        } else if (request instanceof UpdateRequestComplexType) {
            response = update((UpdateRequestComplexType) request);
        } else if (request instanceof DeleteRequestComplexType) {
            response = delete((DeleteRequestComplexType) request);
        } else if (request instanceof SelectRequestComplexType) {
            response = select((SelectRequestComplexType) request);
        } else {
            log.error("An inner request could not be matched to valid types: " + request);
            throw new ExecuteFault();
        }
        return response;
    }

    @PreAuthorize("hasRole('RIGHT_CREATE_POI')")
    public CreateResponseComplexType create(CreateRequestComplexType createRequest) throws ExecuteFault {
        log.debug("Processing create request.");
        POI poiFromRequest = PoiConverter.convertToPojo(createRequest.getPoi());
        try {
            this.dao.save(poiFromRequest);
        } catch (PoiDaoException e) {
            throw new ExecuteFault(e.getMessage(), e);
        }
        CreateResponseComplexType response = new CreateResponseComplexType();
        response.setPoi(PoiConverter.createPoiWithId(poiFromRequest));
        response.setSuccessMessage("The point of interest was successfully added.");
        return response;
    }

    @PreAuthorize("hasRole('RIGHT_DELETE_POI')")
    public DeleteResponseComplexType delete(DeleteRequestComplexType deleteRequest) throws ExecuteFault {
        log.debug("Processing delete request.");
        try {
            this.dao.remove(this.dao.findByUid(deleteRequest.getId()));
        } catch (PoiDaoException e) {
            throw new ExecuteFault(e.getMessage(), e);
        }
        DeleteResponseComplexType response = new DeleteResponseComplexType();
        response.setSuccessMessage("The point of interest was successfully removed.");
        return response;
    }

    @PreAuthorize("hasRole('RIGHT_UPDATE_POI')")
    public UpdateResponseComplexType update(UpdateRequestComplexType updateRequest) throws ExecuteFault {
        log.debug("Processing update request.");
        POI poiFromRequest = PoiConverter.convertToPojo(updateRequest.getPoi());
        try {
            this.dao.merge(poiFromRequest);
        } catch (PoiDaoException e) {
            throw new ExecuteFault(e.getMessage(), e);
        }
        UpdateResponseComplexType response = new UpdateResponseComplexType();
        response.setSuccessMessage("The changes were successfully saved.");
        return response;
    }

    @PreAuthorize("hasRole('RIGHT_READ_POI')")
    public ReadResponseComplexType read(ReadRequestComplexType readRequest) throws ExecuteFault {
        log.debug("Processing read request.");
        int idToRead = readRequest.getId();
        POI foundPoi;
        try {
            foundPoi = (POI) this.dao.findByUid(idToRead);
        } catch (PoiDaoException e) {
            throw new ExecuteFault(e.getMessage(), e);
        }
        ReadResponseComplexType response = new ReadResponseComplexType();
        if (foundPoi != null) {
            PoiWithId pwi = new PoiWithId();
            pwi.setUid(foundPoi.getId());
            pwi.setName(foundPoi.getName());
            pwi.setLongitude(foundPoi.getLongitude());
            pwi.setLatitude(foundPoi.getLatitude());
            pwi.setDescription(foundPoi.getDescription());
            pwi.setCategoryName(foundPoi.getCategoryName());
            response.setPoi(pwi);
            response.setSuccessMessage("Successfully retrieved the point of interest.");
        } else {
            throw new ExecuteFault("Could not find poi with uid " + idToRead + ".");
        }
        return response;
    }

    @PreAuthorize("hasRole('RIGHT_READ_POI')")
    public SelectResponseComplexType select(SelectRequestComplexType selectRequest) throws ExecuteFault {
        log.debug("Processing select request.");
        List<POI> foundPois = new ArrayList<POI>();
        foundPois.addAll(findPoisByIds(selectRequest.getFindByIds()));
        foundPois.addAll(findPoisByNamePrefixes(selectRequest.getFindByNamePrefixes()));
        foundPois.addAll(findPoisByNames(selectRequest.getFindByNames()));
        foundPois.addAll(findPoisByNamesLike(selectRequest.getFindByNamesLike()));
        foundPois.addAll(findPoisByNameSuffixes(selectRequest.getFindByNameSuffixes()));
        SelectResponseComplexType response = new SelectResponseComplexType();
        response.getPoi().addAll(PoiConverter.convertPoisForResponse(foundPois));
        response.setSuccessMessage("Successfully retrieved points of interest.");
        return response;
    }

    private Collection<? extends POI> findPoisByNameSuffixes(Names suffixes) {
        List<POI> pois = new ArrayList<POI>();
        if (suffixes != null) {
            for (String suffix : suffixes.getName()) {
                pois.addAll(this.dao.findByNameWithSuffix(suffix));
            }
        }
        return pois;
    }

    private Collection<? extends POI> findPoisByNamesLike(Names likePatterns) {
        List<POI> pois = new ArrayList<POI>();
        if (likePatterns != null) {
            for (String likePattern : likePatterns.getName()) {
                pois.addAll(this.dao.findByNameLike(likePattern));
            }
        }
        return pois;
    }

    private Collection<? extends POI> findPoisByNames(Names names) {
        List<POI> pois = new ArrayList<POI>();
        if (names != null) {
            for (String name : names.getName()) {
                pois.addAll(this.dao.findByNameLike(name));
            }
        }
        return pois;
    }

    private Collection<? extends POI> findPoisByNamePrefixes(Names prefixes) {
        List<POI> pois = new ArrayList<POI>();
        if (prefixes != null) {
            for (String prefix : prefixes.getName()) {
                pois.addAll(this.dao.findByNameWithPrefix(prefix));
            }
        }
        return pois;
    }

    private Collection<? extends POI> findPoisByIds(Ids ids) throws ExecuteFault {
        List<POI> pois = new ArrayList<POI>();
        if (ids != null) {
            for (int uid : ids.getId()) {
                try {
                    pois.add((POI) this.dao.findByUid(uid));
                } catch (PoiDaoException e) {
                    throw new ExecuteFault(e.getMessage(), e);
                }
            }
        }
        return pois;
    }
}
