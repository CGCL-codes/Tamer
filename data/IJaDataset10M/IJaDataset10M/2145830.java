package se.infact.util;

import org.springframework.stereotype.Service;
import se.infact.domain.FlashKnowledgeScene;
import se.infact.domain.Node;
import se.infact.domain.TextImageKnowledgeScene;

@Service("knowledgeSceneFactory")
public class KnowledgeSceneFactory {

    public Node createKnowledgeScene(String type) {
        if (type.equals("text-image")) {
            return new TextImageKnowledgeScene();
        } else if (type.equals("flash")) {
            return new FlashKnowledgeScene();
        } else {
            return null;
        }
    }
}
