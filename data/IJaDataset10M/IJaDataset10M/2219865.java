package com.mindquarry.persistence.jcr.cmds;

import com.mindquarry.persistence.jcr.JcrNode;
import com.mindquarry.persistence.jcr.JcrSession;
import com.mindquarry.persistence.jcr.Persistence;
import com.mindquarry.persistence.jcr.model.Model;
import com.mindquarry.persistence.jcr.trafo.TransformationManager;
import com.mindquarry.persistence.jcr.trafo.Transformer;

/**
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
class WriteCommand implements Command {

    private Object entity_;

    private Persistence persistence_;

    public void initialize(Persistence persistence, Object... objects) {
        entity_ = objects[0];
        persistence_ = persistence;
    }

    /**
     * @see com.mindquarry.persistence.jcr.mapping.Command#execute(javax.jcr.Session)
     */
    public Object execute(JcrSession session) {
        JcrNode folderNode = findEntityFolder(session);
        JcrNode entityNode = folderNode.getNode(entityId());
        entityTransformer().writeToJcr(entity_, entityNode);
        return entityNode;
    }

    private JcrNode findEntityFolder(JcrSession session) {
        JcrNode rootNode = session.getRootNode();
        return rootNode.getNode(parentFolderName());
    }

    private String entityId() {
        return getModel().findEntityType(entity_).getId(entity_);
    }

    private String parentFolderName() {
        return getModel().findEntityType(entity_).parentFolder();
    }

    private Model getModel() {
        return persistence_.getModel();
    }

    private Transformer entityTransformer() {
        return getTransformationManager().entityTransformer(entity_);
    }

    private TransformationManager getTransformationManager() {
        return persistence_.getTransformationManager();
    }
}
