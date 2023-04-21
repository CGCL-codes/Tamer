package se.infact.manager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import se.infact.controller.presentation.ListController.SortType;
import se.infact.domain.Attachment;
import se.infact.domain.Bubble;
import se.infact.domain.FlashKnowledgeScene;
import se.infact.domain.Group;
import se.infact.domain.Language;
import se.infact.domain.Logo;
import se.infact.domain.MovieKnowledgeScene;
import se.infact.domain.Node;
import se.infact.domain.Participants;
import se.infact.domain.Presentation;
import se.infact.domain.Slide;
import se.infact.domain.SlideshowKnowledgeScene;
import se.infact.domain.TextImageKnowledgeScene;
import se.infact.domain.Textfield;
import se.infact.domain.TextlineKnowledgeScene;
import se.infact.domain.TimelineKnowledgeScene;
import se.infact.domain.User;
import se.infact.sort.PresentationsSorter;
import se.infact.util.CloneUtil;
import se.infact.util.DeleteNodeVisitor;
import se.infact.util.PossibleParentsVisitor;
import se.infact.util.RelationRemoverVisitor;
import se.infact.util.SaveAsVisitor;

@Service("infactManager")
public class InfactManager {

    private SessionFactory sessionFactory;

    private UserManager userManager;

    private long infactFamilyIndex;

    private boolean checkedInfactFamilyIndex = false;

    public Presentation getPresentationForParticipants(Participants participants) {
        String presentationQuery = "from presentations where particpants=" + participants.getId();
        Presentation presentation = (Presentation) sessionFactory.getCurrentSession().createQuery(presentationQuery).uniqueResult();
        return presentation;
    }

    public Presentation getPresentationForTextfield(Textfield textfield) {
        String presentationQuery = "from presentations " + "join nodes on presentations.id = nodes.presentation_id " + "join textfields on nodes.id = textfields.node_fk " + "where textfields.id = " + textfield.getId();
        Presentation presentation = (Presentation) sessionFactory.getCurrentSession().createQuery(presentationQuery).uniqueResult();
        return presentation;
    }

    public Presentation getPresentationForLogo(Logo logo) {
        String presentationQuery = "from presentations " + "join logos on presentations.id = logos.presentation_fk " + "where logos.id = " + logo.getId();
        Presentation presentation = (Presentation) sessionFactory.getCurrentSession().createQuery(presentationQuery).uniqueResult();
        return presentation;
    }

    public Presentation getPresentationForSlide(Slide slide) {
        String presentationQuery = "from presentations " + "join nodes on presentations.id = nodes.presentation_id " + "join nodes_slideshow_slides on nodes.id = nodes_slideshow_slides.nodes_slideshow_nodes_id " + "join nodes_textimage on nodes_textimage.nodes_id = nodes.id " + "join nodes_textline_slides on nodes.id = TextlineKnowledgeScene_nodes_id " + "join nodes_timeline_slides on nodes.id = TimelineKnowledgeScene_nodes_id " + "where nodes_slideshow_slides.slideList_id = " + slide.getId() + " or nodes_textline_slides.slideList_id = " + slide.getId() + " or nodes_timeline_slides.slideList_id = " + slide.getId() + " or nodes_textimage.slide_id = " + slide.getId();
        Presentation presentation = (Presentation) sessionFactory.getCurrentSession().createQuery(presentationQuery).uniqueResult();
        return presentation;
    }

    public long getNextInfactFamilyIndex() {
        if (checkedInfactFamilyIndex) {
            infactFamilyIndex++;
            return infactFamilyIndex;
        } else {
            checkedInfactFamilyIndex = true;
            infactFamilyIndex = infactFamilyIndexLookUp() + 1;
            return infactFamilyIndex;
        }
    }

    public boolean userHasRightsToPresentation(Presentation presentation) {
        if (presentation.isLocked()) {
            return false;
        }
        User currentUser = userManager.getAuthenticatedUser();
        if (currentUser.isSuperAdminAuthority()) {
            return true;
        }
        if (currentUser.getGroup().getId() == presentation.getGroup().getId()) {
            return true;
        }
        return false;
    }

    public Node loadNode(long nodeId) {
        Node node = (Node) sessionFactory.getCurrentSession().load(Node.class, nodeId);
        if (userHasRightsToPresentation(node.getPresentation())) {
            return node;
        }
        throw new AccessDeniedException("You do not have permission to view this presentation");
    }

    public Bubble loadBubble(long bubbleId) {
        Bubble bubble = (Bubble) sessionFactory.getCurrentSession().load(Bubble.class, bubbleId);
        if (userHasRightsToPresentation(bubble.getPresentation())) {
            return bubble;
        }
        throw new AccessDeniedException("You do not have permission to view this presentation");
    }

    public TextImageKnowledgeScene loadTextImage(long textImageId) {
        TextImageKnowledgeScene ks = (TextImageKnowledgeScene) sessionFactory.getCurrentSession().load(TextImageKnowledgeScene.class, textImageId);
        if (userHasRightsToPresentation(ks.getPresentation())) {
            return ks;
        }
        throw new AccessDeniedException("You do not have permission to view this presentation");
    }

    public FlashKnowledgeScene loadFlash(long flashId) {
        FlashKnowledgeScene ks = (FlashKnowledgeScene) sessionFactory.getCurrentSession().load(FlashKnowledgeScene.class, flashId);
        if (userHasRightsToPresentation(ks.getPresentation())) {
            return ks;
        }
        throw new AccessDeniedException("You do not have permission to view this presentation");
    }

    public MovieKnowledgeScene loadMovie(long movieId) {
        MovieKnowledgeScene ks = (MovieKnowledgeScene) sessionFactory.getCurrentSession().load(MovieKnowledgeScene.class, movieId);
        if (userHasRightsToPresentation(ks.getPresentation())) {
            return ks;
        }
        throw new AccessDeniedException("You do not have permission to view this presentation");
    }

    public SlideshowKnowledgeScene loadSlideshow(long slideshowId) {
        SlideshowKnowledgeScene ks = (SlideshowKnowledgeScene) sessionFactory.getCurrentSession().load(SlideshowKnowledgeScene.class, slideshowId);
        if (userHasRightsToPresentation(ks.getPresentation())) {
            return ks;
        }
        throw new AccessDeniedException("You do not have permission to view this presentation");
    }

    public TimelineKnowledgeScene loadTimeline(long timelineId) {
        TimelineKnowledgeScene ks = (TimelineKnowledgeScene) sessionFactory.getCurrentSession().load(TimelineKnowledgeScene.class, timelineId);
        if (userHasRightsToPresentation(ks.getPresentation())) {
            return ks;
        }
        throw new AccessDeniedException("You do not have permission to view this presentation");
    }

    public TextlineKnowledgeScene loadTextline(long textlineId) {
        TextlineKnowledgeScene ks = (TextlineKnowledgeScene) sessionFactory.getCurrentSession().load(TextlineKnowledgeScene.class, textlineId);
        if (userHasRightsToPresentation(ks.getPresentation())) {
            return ks;
        }
        throw new AccessDeniedException("You do not have permission to view this presentation");
    }

    public Attachment loadAttachment(long attachmentId) {
        return (Attachment) sessionFactory.getCurrentSession().load(Attachment.class, attachmentId);
    }

    public Textfield loadTextfield(long textfieldId) {
        return (Textfield) sessionFactory.getCurrentSession().load(Textfield.class, textfieldId);
    }

    public Logo loadLogo(long logoId) {
        return (Logo) sessionFactory.getCurrentSession().load(Logo.class, logoId);
    }

    public Group loadGroup(long groupId) {
        return (Group) sessionFactory.getCurrentSession().load(Group.class, groupId);
    }

    public Group getDefaultGroup() {
        String defaultGroupQuery = "from Group where defaultGroup=true";
        Group defaultGroup = (Group) sessionFactory.getCurrentSession().createQuery(defaultGroupQuery).uniqueResult();
        if (defaultGroup == null) {
            Group sGroup = new Group("standard");
            sGroup.setDefaultGroup(true);
            saveGroup(sGroup);
        }
        return defaultGroup;
    }

    public Participants loadParticipants(long participantsId) {
        return (Participants) sessionFactory.getCurrentSession().load(Participants.class, participantsId);
    }

    public boolean userDoneWithNode(User user, long nodeId) {
        Node node = loadNode(nodeId);
        return node.getFinishedUsers().contains(user);
    }

    public void markUserDoneForNode(User user, long nodeId) {
        loadNode(nodeId).addFinishedUser(user);
    }

    public void unmarkUserDoneForNode(User user, long nodeId) {
        loadNode(nodeId).removeFinishedUser(user);
    }

    public Slide loadSlide(long slideId) {
        return (Slide) sessionFactory.getCurrentSession().load(Slide.class, slideId);
    }

    public void approveNode(long nodeId, boolean approve) {
        loadNode(nodeId).setApproved(approve);
    }

    public void approvePresentation(long presentationId, boolean approve) {
        loadPresentation(presentationId).setApproved(approve);
    }

    public void saveNode(Node node) {
        if (!userHasRightsToPresentation(node.getPresentation())) {
            throw new AccessDeniedException("You do not have permission to view this presentation");
        }
        sessionFactory.getCurrentSession().saveOrUpdate(node);
    }

    public void saveAttachment(Attachment attachment) {
        sessionFactory.getCurrentSession().saveOrUpdate(attachment);
    }

    public void saveTextfield(Textfield textfield) {
        sessionFactory.getCurrentSession().saveOrUpdate(textfield);
    }

    public void saveLogo(Logo logo) {
        sessionFactory.getCurrentSession().saveOrUpdate(logo);
    }

    public void saveGroup(Group group) {
        sessionFactory.getCurrentSession().saveOrUpdate(group);
    }

    public void saveParticipants(Participants participants) {
        sessionFactory.getCurrentSession().saveOrUpdate(participants);
    }

    public Presentation loadLockedPresentation(long presentationId) {
        Presentation presentation = (Presentation) sessionFactory.getCurrentSession().load(Presentation.class, presentationId);
        User currentUser = userManager.getAuthenticatedUser();
        if (currentUser.isSuperAdminAuthority()) {
            return presentation;
        } else if (currentUser.getGroup().getId() == presentation.getGroup().getId()) {
            return presentation;
        } else {
            throw new AccessDeniedException("You do not have permission to view this presentation");
        }
    }

    public Presentation loadPresentation(long presentationId) {
        Presentation presentation = (Presentation) sessionFactory.getCurrentSession().load(Presentation.class, presentationId);
        if (userHasRightsToPresentation(presentation)) {
            return presentation;
        } else {
            throw new AccessDeniedException("You do not have permission to view this presentation");
        }
    }

    public Language loadLanguage(long languageId) {
        Language language = (Language) sessionFactory.getCurrentSession().load(Language.class, languageId);
        return language;
    }

    @SuppressWarnings("unchecked")
    public List<Presentation> listPresentations(SortType sortType, boolean filterLocked) {
        List<Presentation> presentations = sessionFactory.getCurrentSession().createCriteria(Presentation.class).addOrder(Order.asc("id")).list();
        PresentationsSorter fSorter = new PresentationsSorter(presentations, sortType);
        if (filterLocked) {
            return removeLockedPresentations(fSorter.getSortedPresentations());
        } else {
            return fSorter.getSortedPresentations();
        }
    }

    /**
	 * return a list of all presentations associated with group
	 * 
	 * @param group
	 * @return
	 */
    @SuppressWarnings("unchecked")
    public List<Presentation> listPresentationsInGroup(long groupId, SortType sortType, boolean filterLocked) {
        List<Presentation> presentations = sessionFactory.getCurrentSession().createCriteria(Presentation.class).addOrder(Order.asc("id")).list();
        presentations = filterPresentationOnGroup(presentations, groupId);
        PresentationsSorter pSorter = new PresentationsSorter(presentations, sortType);
        if (filterLocked) {
            return removeLockedPresentations(pSorter.getSortedPresentations());
        } else {
            return pSorter.getSortedPresentations();
        }
    }

    public List<Presentation> filterPresentationOnGroup(List<Presentation> allPresentations, long groupId) {
        List<Presentation> groupPresentations = new ArrayList<Presentation>();
        for (Presentation p : allPresentations) {
            if (p.getGroup() == null) {
                p.setGroup(getDefaultGroup());
            }
            if (p.getGroup().getId() == groupId) {
                groupPresentations.add(p);
            }
        }
        return groupPresentations;
    }

    @SuppressWarnings("unchecked")
    public List<Group> listGroups() {
        return sessionFactory.getCurrentSession().createCriteria(Group.class).addOrder(Order.asc("name")).list();
    }

    @SuppressWarnings("unchecked")
    public List<Language> listLanguages() {
        List<Language> languages = sessionFactory.getCurrentSession().createCriteria(Language.class).addOrder(Order.asc("name")).list();
        Language swedish = languages.get(languages.size() - 1);
        languages.remove(languages.size() - 1);
        languages.add(0, swedish);
        return languages;
    }

    public void savePresentation(Presentation presentation) {
        if (sessionFactory.getCurrentSession().get(Presentation.class, presentation.getId()) != null) {
            if (!userHasRightsToPresentation(presentation)) {
                throw new AccessDeniedException("You do not have permission to view this presentation");
            }
        }
        sessionFactory.getCurrentSession().saveOrUpdate(presentation);
    }

    public void savePresentationAs(long oldPresentationId, String newPresentationSuffix) throws IOException {
        long newInfactFamilyIndex = getNextInfactFamilyIndex();
        List<Presentation> oldInfactFamilyList = getInfactFamily(oldPresentationId);
        for (Presentation presentation : oldInfactFamilyList) {
            Presentation newPresentation = copyPresentation(presentation.getId(), false);
            newPresentation.setName(presentation.getName() + " - " + newPresentationSuffix);
            newPresentation.setInfactFamily(newInfactFamilyIndex);
            this.savePresentation(newPresentation);
        }
    }

    public void saveLockedPresentation(long oldPresentationId, String newPresentationSuffix) throws IOException {
        Presentation newPresentation = copyPresentation(oldPresentationId, true);
        newPresentation.setLocked(false);
        newPresentation.setName(newPresentation.getName() + " - " + newPresentationSuffix);
        this.savePresentation(newPresentation);
    }

    public void createTranslation(long id, long languageId) throws IOException {
        Presentation newPresentation = copyPresentation(id, false);
        Language language = loadLanguage(languageId);
        newPresentation.setMainLanguage(false);
        newPresentation.setLanguage(language);
        newPresentation.setName(newPresentation.getName() + " - " + language.getName());
        this.savePresentation(newPresentation);
    }

    public Presentation copyPresentation(long oldPresentationId, boolean locked) throws IOException {
        Presentation newCopy = new Presentation();
        Presentation oldPresentation;
        if (locked) {
            oldPresentation = loadLockedPresentation(oldPresentationId);
        } else {
            oldPresentation = loadPresentation(oldPresentationId);
        }
        SaveAsVisitor visitor = new SaveAsVisitor(this, newCopy);
        newCopy.setPlanet(oldPresentation.isPlanet());
        newCopy.setApproved(oldPresentation.isApproved());
        newCopy.setFont(oldPresentation.getFont());
        newCopy.setInfactFamily(oldPresentation.getInfactFamily());
        newCopy.setMainLanguage(oldPresentation.isMainLanguage());
        newCopy.setName(oldPresentation.getName());
        newCopy.setBgImage(CloneUtil.cloneAttachment(oldPresentation.getBgImage()));
        newCopy.setCenterImage(CloneUtil.cloneAttachment(oldPresentation.getCenterImage()));
        newCopy.setLanguage(oldPresentation.getLanguage());
        newCopy.setGroup(oldPresentation.getGroup());
        if (oldPresentation.getParticipants() != null) {
            Participants oldParticipants = oldPresentation.getParticipants();
            newCopy.setParticipants(CloneUtil.cloneParticipants(oldParticipants));
        }
        oldPresentation.getStartnode().accept(visitor);
        return newCopy;
    }

    public void saveSlide(Slide slide) {
        sessionFactory.getCurrentSession().saveOrUpdate(slide);
    }

    public void saveLanguage(Language language) {
        sessionFactory.getCurrentSession().saveOrUpdate(language);
    }

    public void addNewNode(Node node, long parentId) {
        Bubble parentNode = loadBubble(parentId);
        node.setParent(parentNode);
        node.setPresentation(parentNode.getPresentation());
        node.setSortIndex(parentNode.getSubnodes().size());
        parentNode.addSubnode(node);
        sessionFactory.getCurrentSession().saveOrUpdate(node);
        sessionFactory.getCurrentSession().saveOrUpdate(parentNode);
    }

    public void addNewParticipants(Participants participants, long presentationId) {
        Presentation presentation = loadPresentation(presentationId);
        presentation.setParticipants(participants);
        sessionFactory.getCurrentSession().saveOrUpdate(presentation);
        sessionFactory.getCurrentSession().saveOrUpdate(participants);
    }

    public List<Node> getParentTree(long nodeId) {
        List<Node> parentList = new ArrayList<Node>();
        Node node = (Node) sessionFactory.getCurrentSession().load(Node.class, nodeId);
        while (node.getParent() != null) {
            node = node.getParent();
            parentList.add(node);
        }
        Collections.reverse(parentList);
        return parentList;
    }

    public List<Bubble> getPossibleParents(long nodeId) throws IOException {
        Node node = (Node) sessionFactory.getCurrentSession().load(Node.class, nodeId);
        PossibleParentsVisitor visitor = new PossibleParentsVisitor(node);
        node.getPresentation().getStartnode().accept(visitor);
        return visitor.getPossibleParents();
    }

    public void moveUp(long parentId, long childId) {
        swap(parentId, childId, -1);
    }

    public void moveDown(long parentId, long childId) {
        swap(parentId, childId, 1);
    }

    public void slideMoveUp(long ksId, long slideId) {
        if (loadNode(ksId).getTypeString().equals("timeline")) {
            TimelineKnowledgeScene timeline = loadTimeline(ksId);
            if (!(timeline.getFirstSlideId() == slideId)) {
                swapSlide(ksId, slideId, -1, "timeline");
            }
        } else if (loadNode(ksId).getTypeString().equals("textline")) {
            TextlineKnowledgeScene textline = loadTextline(ksId);
            if (!(textline.getFirstSlideId() == slideId)) {
                swapSlide(ksId, slideId, -1, "textline");
            }
        } else {
            SlideshowKnowledgeScene slideshow = loadSlideshow(ksId);
            if (!(slideshow.getFirstSlideId() == slideId)) {
                swapSlide(ksId, slideId, -1, "slideshow");
            }
        }
    }

    public void slideMoveDown(long ksId, long slideId) {
        if (loadNode(ksId).getTypeString().equals("timeline")) {
            TimelineKnowledgeScene timeline = loadTimeline(ksId);
            if (!(timeline.getLastSlideId() == slideId)) {
                swapSlide(ksId, slideId, 1, "timeline");
            }
        } else if (loadNode(ksId).getTypeString().equals("textline")) {
            TextlineKnowledgeScene textline = loadTextline(ksId);
            if (!(textline.getLastSlideId() == slideId)) {
                swapSlide(ksId, slideId, 1, "textline");
            }
        } else {
            SlideshowKnowledgeScene slideshow = loadSlideshow(ksId);
            if (!(slideshow.getLastSlideId() == slideId)) {
                swapSlide(ksId, slideId, 1, "slideshow");
            }
        }
    }

    public void moveNode(long sourceId, long destinationId) {
        Node source = loadNode(sourceId);
        long parentId = source.getParent().getId();
        Bubble destination = loadBubble(destinationId);
        boolean changeIndex = false;
        String parentSubnodeQuery = "from Node where parent_id=" + parentId + "order by sortIndex asc";
        @SuppressWarnings("unchecked") List<Node> queryList = sessionFactory.getCurrentSession().createQuery(parentSubnodeQuery).list();
        source.setParent(destination);
        destination.addSubnode(source);
        source.setSortIndex(destination.getSubnodes().size() - 1);
        sessionFactory.getCurrentSession().flush();
        for (Node node : queryList) {
            if (sourceId == node.getId()) {
                loadBubble(parentId).removeSubnode(source);
                changeIndex = true;
            } else if (changeIndex) {
                node.setSortIndex(node.getSortIndex() - 1);
            }
        }
        sessionFactory.getCurrentSession().flush();
    }

    public void deleteSlide(long slideshowId, long slideId) {
        SlideshowKnowledgeScene slideshow = loadSlideshow(slideshowId);
        Slide slideToRemove = loadSlide(slideId);
        if (!slideshow.getSlideList().contains(slideToRemove)) {
            throw new HibernateException("No slide with the given id found.");
        }
        List<Slide> slideList = slideshow.getSlideList();
        slideList.remove(slideToRemove);
        renumberSlides(slideList);
        sessionFactory.getCurrentSession().saveOrUpdate(slideshow);
        sessionFactory.getCurrentSession().delete(slideToRemove);
    }

    public void deleteAttachementFromPresentation(long presentationId, long attachmentId) {
        Presentation presentation = loadPresentation(presentationId);
        if (presentation.getBgImage() != null && presentation.getBgImage().getId() == attachmentId) {
            presentation.setBgImage(null);
        }
        if (presentation.getCenterImage() != null && presentation.getCenterImage().getId() == attachmentId) {
            presentation.setCenterImage(null);
        }
        sessionFactory.getCurrentSession().saveOrUpdate(presentation);
        deleteAttachment(attachmentId);
    }

    public void deleteAttachmentFromParticipants(long participantsId, long attachmentId) {
        Participants participants = loadParticipants(participantsId);
        participants.setImage(null);
        sessionFactory.getCurrentSession().saveOrUpdate(participants);
        deleteAttachment(attachmentId);
    }

    public void deleteAttachementFromKnowledgeScene(long sceneId, long attachmentId) {
        Node knowledgeScene = loadNode(sceneId);
        knowledgeScene.setStoryboardFile(null);
        sessionFactory.getCurrentSession().saveOrUpdate(knowledgeScene);
        deleteAttachment(attachmentId);
    }

    public void deleteAttachment(long attachmentId) {
        Attachment attachment = loadAttachment(attachmentId);
        sessionFactory.getCurrentSession().delete(attachment);
    }

    public void deleteTextfield(long textfieldId) {
        Textfield textfield = loadTextfield(textfieldId);
        sessionFactory.getCurrentSession().delete(textfield);
    }

    public void deleteLogo(long logoId) {
        Logo logo = loadLogo(logoId);
        sessionFactory.getCurrentSession().delete(logo);
    }

    public void deleteGroup(Group group) {
        if (group.isDefaultGroup()) {
            return;
        }
        List<User> users = userManager.list();
        for (User user : users) {
            if (user.getGroup() != null) {
                if (user.getGroup().getId() == group.getId()) {
                    user.setGroup(getDefaultGroup());
                }
            } else {
                user.setGroup(getDefaultGroup());
            }
        }
        List<Presentation> presentations = listPresentations(SortType.DATE_ASC, false);
        for (Presentation presentation : presentations) {
            if (presentation.getGroup() != null) {
                if (presentation.getGroup().getId() == group.getId()) {
                    presentation.setGroup(getDefaultGroup());
                }
            } else {
                presentation.setGroup(getDefaultGroup());
            }
        }
        sessionFactory.getCurrentSession().delete(group);
    }

    public void deleteSlideWithYear(long timelineId, long slideId) {
        TimelineKnowledgeScene timeline = loadTimeline(timelineId);
        Slide slideToRemove = loadSlide(slideId);
        if (!timeline.getSlideList().contains(slideToRemove)) {
            throw new HibernateException("No slide with the given id found.");
        }
        List<Slide> slideList = timeline.getSlideList();
        slideList.remove(slideToRemove);
        renumberSlides(slideList);
        sessionFactory.getCurrentSession().saveOrUpdate(timeline);
        sessionFactory.getCurrentSession().delete(slideToRemove);
    }

    public void deleteTextSlide(long textlineId, long slideId) {
        TextlineKnowledgeScene textline = loadTextline(textlineId);
        Slide slideToRemove = loadSlide(slideId);
        if (!textline.getSlideList().contains(slideToRemove)) {
            throw new HibernateException("No slide with the given id found.");
        }
        List<Slide> slideList = textline.getSlideList();
        slideList.remove(slideToRemove);
        renumberSlides(slideList);
        sessionFactory.getCurrentSession().saveOrUpdate(textline);
        sessionFactory.getCurrentSession().delete(slideToRemove);
    }

    public void deleteSingleNode(Node node) throws IOException {
        if (!userHasRightsToPresentation(node.getPresentation())) {
            throw new AccessDeniedException("You do not have permission to view this presentation");
        }
        removeNodeFromRelations(node);
        if (node.getParent() != null) {
            removeFromParent(node);
        }
        sessionFactory.getCurrentSession().delete(node);
    }

    public void deleteRelationTextImage(long textImageId, long relationId) {
        TextImageKnowledgeScene textImage = loadTextImage(textImageId);
        List<Node> newRelatedNodes = new ArrayList<Node>();
        for (Node node : textImage.getRelatedNodes()) {
            if (!node.getId().equals(relationId)) {
                newRelatedNodes.add(node);
            }
        }
        textImage.setRelatedNodes(newRelatedNodes);
        sessionFactory.getCurrentSession().saveOrUpdate(textImage);
    }

    public void deleteRelationMovie(long movieId, long relationId) {
        MovieKnowledgeScene movie = loadMovie(movieId);
        List<Node> newRelatedNodes = new ArrayList<Node>();
        for (Node node : movie.getRelatedNodes()) {
            if (!node.getId().equals(relationId)) {
                newRelatedNodes.add(node);
            }
        }
        movie.setRelatedNodes(newRelatedNodes);
        sessionFactory.getCurrentSession().saveOrUpdate(movie);
    }

    public void deleteRelationFlash(long flashId, long relationId) {
        FlashKnowledgeScene flash = loadFlash(flashId);
        List<Node> newRelatedNodes = new ArrayList<Node>();
        for (Node node : flash.getRelatedNodes()) {
            if (!node.getId().equals(relationId)) {
                newRelatedNodes.add(node);
            }
        }
        flash.setRelatedNodes(newRelatedNodes);
        sessionFactory.getCurrentSession().saveOrUpdate(flash);
    }

    public void deleteRelationTimeline(long timelineId, long relationId) {
        TimelineKnowledgeScene timeline = loadTimeline(timelineId);
        List<Node> newRelatedNodes = new ArrayList<Node>();
        for (Node node : timeline.getRelatedNodes()) {
            if (!node.getId().equals(relationId)) {
                newRelatedNodes.add(node);
            }
        }
        timeline.setRelatedNodes(newRelatedNodes);
        sessionFactory.getCurrentSession().saveOrUpdate(timeline);
    }

    public void deleteRelationTextline(long textlineId, long relationId) {
        TextlineKnowledgeScene textline = loadTextline(textlineId);
        List<Node> newRelatedNodes = new ArrayList<Node>();
        for (Node node : textline.getRelatedNodes()) {
            if (!node.getId().equals(relationId)) {
                newRelatedNodes.add(node);
            }
        }
        textline.setRelatedNodes(newRelatedNodes);
        sessionFactory.getCurrentSession().saveOrUpdate(textline);
    }

    public void deleteRelationSlideshow(long slideshowId, long relationId) {
        SlideshowKnowledgeScene slideshow = loadSlideshow(slideshowId);
        List<Node> newRelatedNodes = new ArrayList<Node>();
        for (Node node : slideshow.getRelatedNodes()) {
            if (!node.getId().equals(relationId)) {
                newRelatedNodes.add(node);
            }
        }
        slideshow.setRelatedNodes(newRelatedNodes);
        sessionFactory.getCurrentSession().saveOrUpdate(slideshow);
    }

    public void deleteNodeTree(Node node) throws IOException {
        node.accept(new DeleteNodeVisitor(this));
    }

    public void deletePresentation(Presentation presentation) throws IOException {
        if (!userHasRightsToPresentation(presentation)) {
            throw new AccessDeniedException("You do not have permission to view this presentation");
        }
        Node startNode = presentation.getStartnode();
        deleteNodeTree(startNode);
        sessionFactory.getCurrentSession().delete(presentation);
    }

    public List<Language> getInfactFamilyLanguages(long presentationId) {
        List<Language> languageList = new ArrayList<Language>();
        List<Presentation> presentationList = getInfactFamily(presentationId);
        for (Presentation presentation : presentationList) {
            languageList.add(presentation.getLanguage());
        }
        return languageList;
    }

    @SuppressWarnings("unchecked")
    public List<Presentation> getInfactFamily(long presentationId) {
        long presentationFamily = loadPresentation(presentationId).getInfactFamily();
        String familyLanguageQuery = "from Presentation where infactFamily=" + presentationFamily;
        List<Presentation> queryList = sessionFactory.getCurrentSession().createQuery(familyLanguageQuery).list();
        return queryList;
    }

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Autowired
    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    private void removeNodeFromRelations(Node node) throws IOException {
        node.getPresentation().getStartnode().accept(new RelationRemoverVisitor(node));
    }

    private void renumberSlides(List<Slide> slideList) {
        int i = 0;
        for (Slide slide : slideList) {
            slide.setSortIndex(i++);
        }
    }

    private void removeFromParent(Node child) {
        if (!userHasRightsToPresentation(child.getPresentation())) {
            throw new AccessDeniedException("You do not have permission to view this presentation");
        }
        Bubble parent = child.getParent();
        parent.removeSubnode(child);
        sessionFactory.getCurrentSession().flush();
        for (int i = 0; i < parent.getSubnodes().size(); i++) {
            parent.getSubnodes().get(i).setSortIndex(i);
        }
    }

    private void swap(long parentId, long childId, int sortIndexOffset) {
        Bubble parent = loadBubble(parentId);
        Node child = loadNode(childId);
        int childSortIndex = child.getSortIndex();
        Node swapChild = getNodeWithSortIndex(parent.getSubnodes(), childSortIndex + sortIndexOffset);
        int tempSortIndex = child.getSortIndex();
        child.setSortIndex(swapChild.getSortIndex());
        swapChild.setSortIndex(tempSortIndex);
        saveNode(child);
        saveNode(swapChild);
    }

    private void swapSlide(long ksId, long slideId, int sortIndexOffset, String ksType) {
        List<Slide> slideList;
        if (ksType.equals("timeline")) {
            TimelineKnowledgeScene timeline = loadTimeline(ksId);
            slideList = timeline.getSlideList();
        } else if (ksType.equals("textline")) {
            TextlineKnowledgeScene textline = loadTextline(ksId);
            slideList = textline.getSlideList();
        } else {
            SlideshowKnowledgeScene slideshow = loadSlideshow(ksId);
            slideList = slideshow.getSlideList();
        }
        Slide slide = loadSlide(slideId);
        int slideSortIndex = slide.getSortIndex();
        Slide swapSlide = getSlideWithSortIndex(slideList, slideSortIndex + sortIndexOffset);
        int tempSortIndex = slide.getSortIndex();
        slide.setSortIndex(swapSlide.getSortIndex());
        swapSlide.setSortIndex(tempSortIndex);
        saveSlide(slide);
        saveSlide(swapSlide);
    }

    private Node getNodeWithSortIndex(List<Node> nodes, int sortIndex) {
        for (Node node : nodes) {
            if (node.getSortIndex() == sortIndex) {
                return node;
            }
        }
        return null;
    }

    private Slide getSlideWithSortIndex(List<Slide> slides, int sortIndex) {
        for (Slide slide : slides) {
            if (slide.getSortIndex() == sortIndex) {
                return slide;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private long infactFamilyIndexLookUp() {
        String infactFamilyIndexQuery = "select max(infactFamily) from Presentation";
        List<Long> queryList = sessionFactory.getCurrentSession().createQuery(infactFamilyIndexQuery).list();
        if (queryList.get(0) != null) {
            infactFamilyIndex = (Long) queryList.get(0);
        } else {
            infactFamilyIndex = 0L;
        }
        return infactFamilyIndex;
    }

    private List<Presentation> removeLockedPresentations(List<Presentation> presentations) {
        List<Presentation> filteredPresentations = new ArrayList<Presentation>();
        for (Presentation p : presentations) {
            if (!p.isLocked()) {
                filteredPresentations.add(p);
            }
        }
        return filteredPresentations;
    }
}
