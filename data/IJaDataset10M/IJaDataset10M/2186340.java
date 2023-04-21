package org.commonlibrary.lcms.ws.learningobject.impl;

import com.sun.jersey.multipart.FormDataParam;
import net.sf.dozer.util.mapping.DozerBeanMapper;
import org.apache.commons.lang.StringUtils;
import org.commonlibrary.lcms.folder.service.FolderService;
import org.commonlibrary.lcms.learningobject.service.LearningObjectService;
import org.commonlibrary.lcms.model.*;
import org.commonlibrary.lcms.security.service.UserService;
import org.commonlibrary.lcms.standard.service.StandardHierarchyService;
import org.commonlibrary.lcms.support.exception.UniqueContentReferenceIdConstraintViolationException;
import org.commonlibrary.lcms.ws.learningobject.LearningObjectWebService;
import org.commonlibrary.lcms.ws.model.collection.AttachmentCollection;
import org.commonlibrary.lcms.ws.model.response.OperationResult;
import org.commonlibrary.lcms.ws.model.response.OperationStatus;
import org.commonlibrary.lcms.ws.support.response.OperationSuccessfulWSResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.PostConstruct;
import javax.ws.rs.FormParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.util.List;

/**
 * Default implementation of <code>LearningObjectWebService</code>
 * <p/>
 *
 * @author jcastillo
 *         Date: Nov 17, 2008
 *         Time: 10:09:36 AM
 *         <p/>
 */
@Service("learningObjectWebService")
@Transactional
public class LearningObjectWebServiceImpl implements LearningObjectWebService {

    @Autowired
    private DozerBeanMapper dozerBeanMapper;

    @Autowired
    private LearningObjectService learningObjectService;

    @Autowired
    private FolderService folderService;

    @Autowired
    private StandardHierarchyService standardHierarchyService;

    @Autowired
    private UserService userService;

    public void setLearningObjectService(LearningObjectService learningObjectService) {
        this.learningObjectService = learningObjectService;
    }

    public void setFolderService(FolderService folderService) {
        this.folderService = folderService;
    }

    public void setStandardHierarchyService(StandardHierarchyService standardHierarchyService) {
        this.standardHierarchyService = standardHierarchyService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @PostConstruct
    public void init() {
        String msg = "%s property must be set";
        if (this.learningObjectService == null) {
            throw new IllegalStateException(String.format(msg, "learningObjectService"));
        }
        if (this.folderService == null) {
            throw new IllegalStateException(String.format(msg, "folderService"));
        }
        if (this.standardHierarchyService == null) {
            throw new IllegalStateException(String.format(msg, "standardHierarchyService"));
        }
        if (this.userService == null) {
            throw new IllegalStateException(String.format(msg, "userService"));
        }
    }

    /**
     * @see org.commonlibrary.lcms.ws.learningobject.LearningObjectWebService#create(org.commonlibrary.lcms.model.LearningObject)
     */
    public String create(LearningObject learningObject) {
        try {
            return learningObjectService.create(learningObject);
        } catch (Throwable e) {
            throw new WebApplicationException(e);
        }
    }

    /**
     * @see org.commonlibrary.lcms.ws.learningobject.LearningObjectWebService#create(org.commonlibrary.lcms.model.LearningObject, String)
     */
    public String create(LearningObject learningObject, @PathParam("parentId") String parentId) {
        try {
            Folder parent = folderService.findById(parentId);
            return learningObjectService.create(learningObject, parent);
        } catch (Throwable e) {
            throw new WebApplicationException(e);
        }
    }

    /**
     * @see org.commonlibrary.lcms.ws.learningobject.LearningObjectWebService#findById(String)
     */
    public LearningObject findById(@PathParam("learningObjectId") String learningObjectId) {
        try {
            LearningObject learningObject = learningObjectService.findById(learningObjectId);
            return (LearningObject) dozerBeanMapper.map(learningObject, LearningObject.class);
        } catch (Throwable e) {
            throw new WebApplicationException(e);
        }
    }

    /**
     * @see org.commonlibrary.lcms.ws.learningobject.LearningObjectWebService#update(org.commonlibrary.lcms.model.LearningObject)
     */
    public Response update(LearningObject learningObject) {
        try {
            if (StringUtils.isBlank(learningObject.getId())) {
                throw new IllegalArgumentException("The given learningObject should be a backend retrieved instance. It should have at least a valid id assigned");
            }
            LearningObject retrievedLearningObject = learningObjectService.findById(learningObject.getId());
            retrievedLearningObject.setContentReferenceId(learningObject.getContentReferenceId());
            retrievedLearningObject.setContentSize(learningObject.getContentSize());
            retrievedLearningObject.setDraftReferenceId(learningObject.getDraftReferenceId());
            retrievedLearningObject.setDraftSize(learningObject.getDraftSize());
            retrievedLearningObject.setName(learningObject.getName());
            retrievedLearningObject.setTotalRating(learningObject.getTotalRating());
            retrievedLearningObject.setViewCount(learningObject.getViewCount());
            learningObjectService.update(retrievedLearningObject);
            return OperationSuccessfulWSResponse.get();
        } catch (Throwable e) {
            throw new WebApplicationException(e);
        }
    }

    /**
     * @see org.commonlibrary.lcms.ws.learningobject.LearningObjectWebService#remove(String)
     */
    public Response remove(@PathParam("learningObjectId") String learningObjectId) {
        try {
            LearningObject learningObject = learningObjectService.findById(learningObjectId);
            learningObjectService.remove(learningObject);
            return OperationSuccessfulWSResponse.get();
        } catch (Throwable e) {
            throw new WebApplicationException(e);
        }
    }

    /**
     * @see org.commonlibrary.lcms.ws.learningobject.LearningObjectWebService#addContent(String, InputStream)
     */
    public OperationResult addContent(@PathParam("learningObjectId") String learningObjectId, @FormDataParam("content") InputStream content) throws UniqueContentReferenceIdConstraintViolationException {
        try {
            LearningObject learningObject = learningObjectService.findById(learningObjectId);
            learningObjectService.addContent(learningObject, content);
            return new OperationResult(OperationStatus.OK);
        } catch (Throwable e) {
            throw new WebApplicationException(e);
        }
    }

    /**
     * @see org.commonlibrary.lcms.ws.learningobject.LearningObjectWebService#getContentByLearningObjectId(String)
     */
    public Content getContentByLearningObjectId(@PathParam("learningObjectId") String learningObjectId) {
        try {
            Content content = learningObjectService.getContentByLearningObjectId(learningObjectId);
            return (Content) dozerBeanMapper.map(content, Content.class);
        } catch (Throwable e) {
            throw new WebApplicationException(e);
        }
    }

    /**
     * @see org.commonlibrary.lcms.ws.learningobject.LearningObjectWebService#getURLFromURLLearningObject(String)
     */
    public String getURLFromURLLearningObject(@PathParam("learningObjectId") String learningObjectId) {
        try {
            return learningObjectService.getURLFromURLLearningObject(learningObjectId);
        } catch (Throwable e) {
            throw new WebApplicationException(e);
        }
    }

    /**
     * @see org.commonlibrary.lcms.ws.learningobject.LearningObjectWebService#updateContent(String, java.io.InputStream)
     */
    public Response updateContent(@PathParam("learningObjectId") String learningObjectId, @FormParam("content") InputStream content) {
        try {
            LearningObject learningObject = learningObjectService.findById(learningObjectId);
            learningObjectService.updateContent(learningObject, content);
            return OperationSuccessfulWSResponse.get();
        } catch (Throwable e) {
            throw new WebApplicationException(e);
        }
    }

    /**
     * @see org.commonlibrary.lcms.ws.learningobject.LearningObjectWebService#removeContent(String)
     */
    public Response removeContent(@PathParam("learningObjectId") String learningObjectId) {
        try {
            LearningObject learningObject = learningObjectService.findById(learningObjectId);
            learningObjectService.removeContent(learningObject);
            return OperationSuccessfulWSResponse.get();
        } catch (Throwable e) {
            throw new WebApplicationException(e);
        }
    }

    /**
     * @see org.commonlibrary.lcms.ws.learningobject.LearningObjectWebService#addMetadata(String, org.commonlibrary.lcms.model.Metadata)
     */
    public Response addMetadata(@PathParam("learningObjectId") String learningObjectId, Metadata metadata) {
        try {
            LearningObject learningObject = learningObjectService.findById(learningObjectId);
            learningObjectService.addMetadata(learningObject, metadata);
            return OperationSuccessfulWSResponse.get();
        } catch (Throwable e) {
            throw new WebApplicationException(e);
        }
    }

    /**
     * @see org.commonlibrary.lcms.ws.learningobject.LearningObjectWebService#updateMetadata(String, org.commonlibrary.lcms.model.Metadata)
     */
    public OperationResult updateMetadata(@PathParam("learningObjectId") String learningObjectId, Metadata metadata) {
        try {
            LearningObject learningObject = learningObjectService.findById(learningObjectId);
            Metadata retrievedMetadata = learningObject.getMetadata();
            retrievedMetadata.setAgeFrom(metadata.getAgeFrom());
            retrievedMetadata.setAgeTo(metadata.getAgeTo());
            retrievedMetadata.setContext(metadata.getContext());
            retrievedMetadata.setCoverage(metadata.getCoverage());
            retrievedMetadata.setDegreeOfInteractivity(metadata.getDegreeOfInteractivity());
            retrievedMetadata.setDescription(metadata.getDescription());
            retrievedMetadata.setDifficulty(metadata.getDifficulty());
            retrievedMetadata.setEndUser(metadata.getEndUser());
            retrievedMetadata.setFormat(metadata.getFormat());
            retrievedMetadata.setInteractivityType(metadata.getInteractivityType());
            retrievedMetadata.setKeywords(metadata.getKeywords());
            retrievedMetadata.setLanguage(metadata.getLanguage());
            retrievedMetadata.setLearningTime(metadata.getLearningTime());
            retrievedMetadata.setResourceType(metadata.getResourceType());
            retrievedMetadata.setStatus(metadata.getStatus());
            retrievedMetadata.setSubject(metadata.getSubject());
            retrievedMetadata.setTitle(metadata.getTitle());
            learningObjectService.updateMetadata(learningObject, retrievedMetadata);
            return new OperationResult(OperationStatus.OK);
        } catch (Throwable e) {
            throw new WebApplicationException(e);
        }
    }

    /**
     * @see org.commonlibrary.lcms.ws.learningobject.LearningObjectWebService#increaseViewCount(String)
     */
    public Response increaseViewCount(@PathParam("learningObjectId") String learningObjectId) {
        try {
            LearningObject learningObject = learningObjectService.findById(learningObjectId);
            learningObjectService.increaseViewCount(learningObject);
            return OperationSuccessfulWSResponse.get();
        } catch (Throwable e) {
            throw new WebApplicationException(e);
        }
    }

    /**
     * @see org.commonlibrary.lcms.ws.learningobject.LearningObjectWebService#align(String, String)
     */
    public Response align(@PathParam("learningObjectId") String learningObjectId, @PathParam("standardTopicId") String standardTopicId) {
        try {
            LearningObject learningObject = learningObjectService.findById(learningObjectId);
            StandardTopic standardTopic = (StandardTopic) standardHierarchyService.findById(standardTopicId);
            learningObjectService.align(learningObject, standardTopic);
            return OperationSuccessfulWSResponse.get();
        } catch (Throwable e) {
            throw new WebApplicationException(e);
        }
    }

    /**
     * @see org.commonlibrary.lcms.ws.learningobject.LearningObjectWebService#removeAlignment(String, String)
     */
    public Response removeAlignment(@PathParam("learningObjectId") String learningObjectId, @PathParam("standardTopicId") String standardTopicId) {
        try {
            LearningObject learningObject = learningObjectService.findById(learningObjectId);
            StandardTopic standardTopic = (StandardTopic) standardHierarchyService.findById(standardTopicId);
            learningObjectService.removeAlignment(learningObject, standardTopic);
            return OperationSuccessfulWSResponse.get();
        } catch (Throwable e) {
            throw new WebApplicationException(e);
        }
    }

    public Response addDraft(@PathParam("learningObjectId") String learningObjectId, @FormParam("content") InputStream content) throws UniqueContentReferenceIdConstraintViolationException {
        try {
            LearningObject learningObject = learningObjectService.findById(learningObjectId);
            learningObjectService.addDraft(learningObject, content);
            return OperationSuccessfulWSResponse.get();
        } catch (Throwable e) {
            throw new WebApplicationException(e);
        }
    }

    public Content getDraftByLearningObjectId(@PathParam("learningObjectId") String learningObjectId) {
        try {
            Content content = learningObjectService.getDraftByLearningObjectId(learningObjectId);
            return (Content) dozerBeanMapper.map(content, Content.class);
        } catch (Throwable e) {
            throw new WebApplicationException(e);
        }
    }

    public Response updateDraft(@PathParam("learningObjectId") String learningObjectId, @FormParam("content") InputStream content) {
        try {
            LearningObject learningObject = learningObjectService.findById(learningObjectId);
            learningObjectService.updateContent(learningObject, content);
            return OperationSuccessfulWSResponse.get();
        } catch (Throwable e) {
            throw new WebApplicationException(e);
        }
    }

    public Response removeDraft(@PathParam("learningObjectId") String learningObjectId) {
        try {
            LearningObject learningObject = learningObjectService.findById(learningObjectId);
            learningObjectService.removeDraft(learningObject);
            return OperationSuccessfulWSResponse.get();
        } catch (Throwable e) {
            throw new WebApplicationException(e);
        }
    }

    /**
     * @see org.commonlibrary.lcms.ws.learningobject.LearningObjectWebService#publish(String)
     */
    public Response publish(@PathParam("learningObjectId") String learningObjectId) {
        try {
            LearningObject learningObject = learningObjectService.findById(learningObjectId);
            learningObjectService.publish(learningObject);
            return OperationSuccessfulWSResponse.get();
        } catch (Throwable e) {
            throw new WebApplicationException(e);
        }
    }

    /**
     * @see org.commonlibrary.lcms.ws.learningobject.LearningObjectWebService#createDraftFromCurrentContent(String)
     */
    public Response createDraftFromCurrentContent(@PathParam("learningObjectId") String learningObjectId) {
        try {
            LearningObject learningObject = learningObjectService.findById(learningObjectId);
            learningObjectService.createDraftFromCurrentContent(learningObject);
            return OperationSuccessfulWSResponse.get();
        } catch (Throwable e) {
            throw new WebApplicationException(e);
        }
    }

    /**
     * @see org.commonlibrary.lcms.ws.learningobject.LearningObjectWebService#addAttachment(String, java.io.InputStream, String, String)
     */
    public Response addAttachment(@PathParam("learningObjectId") String learningObjectId, @FormParam("content") InputStream content, @PathParam("filename") String filename, @FormParam("note") String note) {
        try {
            LearningObject learningObject = learningObjectService.findById(learningObjectId);
            learningObjectService.addAttachment(learningObject, content, filename, note);
            return OperationSuccessfulWSResponse.get();
        } catch (Throwable e) {
            throw new WebApplicationException(e);
        }
    }

    public Response removeAttachment(@PathParam("learningObjectId") String learningObjectId, @PathParam("attachmentId") String attachmentId) {
        try {
            LearningObject learningObject = learningObjectService.findById(learningObjectId);
            List<Attachment> attachments = learningObject.getAttachments();
            for (Attachment attachment : attachments) {
                if (attachmentId.equals(attachment.getId())) {
                    learningObjectService.removeAttachment(learningObject, attachment);
                    break;
                }
            }
            return OperationSuccessfulWSResponse.get();
        } catch (Throwable e) {
            throw new WebApplicationException(e);
        }
    }

    public AttachmentCollection getAttachments(@PathParam("learningObjectId") String learningObjectId) {
        try {
            LearningObject learningObject = learningObjectService.findById(learningObjectId);
            List<Attachment> attachmentList = learningObject.getAttachments();
            return new AttachmentCollection(attachmentList);
        } catch (Throwable e) {
            throw new WebApplicationException(e);
        }
    }

    public Content getAttachmentContent(@PathParam("learningObjectId") String learningObjectId, @PathParam("attachmentId") String attachmentId) {
        try {
            LearningObject learningObject = learningObjectService.findById(learningObjectId);
            List<Attachment> attachments = learningObject.getAttachments();
            for (Attachment attachment : attachments) {
                if (attachmentId.equals(attachment.getId())) {
                    Content content = learningObjectService.getAttachmentContent(attachment);
                    return (Content) dozerBeanMapper.map(content, Content.class);
                }
            }
            return null;
        } catch (Throwable e) {
            throw new WebApplicationException(e);
        }
    }

    public Response logicalRemove(@PathParam("learningObjectId") String learningObjectId) {
        try {
            LearningObject learningObject = learningObjectService.findById(learningObjectId);
            learningObjectService.logicalRemove(learningObject);
            return OperationSuccessfulWSResponse.get();
        } catch (Throwable e) {
            throw new WebApplicationException(e);
        }
    }
}
