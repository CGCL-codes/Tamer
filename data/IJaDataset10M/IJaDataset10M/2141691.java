package org.speech.asr.gui.dao.jcr.callback.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.speech.asr.gui.constant.JcrConstants.*;
import org.speech.asr.gui.vo.RepoInitStatus;
import org.springmodules.jcr.JcrCallback;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.io.IOException;

/**
 * Callback generujacy status repozytorium jcr.
 * <p/>
 * Creation date: Apr 20, 2009 <br/>
 *
 * @author Lukasz Olczak
 * @since 1.0.0
 */
public class InitRepoCallback implements JcrCallback {

    /**
   * slf4j Logger.
   */
    private static final Logger log = LoggerFactory.getLogger(InitRepoCallback.class.getName());

    public Object doInJcr(Session session) throws IOException, RepositoryException {
        Node rootNode = session.getRootNode();
        RepoInitStatus repoInitStatus = new RepoInitStatus();
        if (rootNode.hasNode(ASR_ROOT_NODE_NAME)) {
            repoInitStatus.setAsrRoot(true);
            Node asrRootNode = rootNode.getNode(ASR_ROOT_NODE_NAME);
            if (asrRootNode.hasNode(CORPORA_ROOT_NODE_NAME)) {
                repoInitStatus.setCorporaInit(true);
            }
            if (asrRootNode.hasNode(DICTIONARIES_ROOT_NODE_NAME)) {
                repoInitStatus.setDictionariesInit(true);
            }
            if (asrRootNode.hasNode(ACOUSTIC_MODELS_NODE_NAME)) {
                repoInitStatus.setAcousticModelsInit(true);
            }
            if (asrRootNode.hasNode(RECOGNIZERS_NODE_NAME)) {
                repoInitStatus.setRecognizersInit(true);
            }
        } else {
            repoInitStatus.setAsrRoot(false);
        }
        return repoInitStatus;
    }
}
