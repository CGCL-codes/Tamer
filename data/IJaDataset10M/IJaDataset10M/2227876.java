package net.sf.jimo.modules.bot.impl.knowledge;

import net.sf.jimo.modules.bot.api.knowledge.KnowledgeBaseSkeleton;

public class KnowledgeBaseDBImpl extends KnowledgeBaseSkeleton {

    public void addAnswer(String question, String answer) {
        super.addAnswer(question, answer);
    }

    public String getAnswer(String question) {
        return super.getAnswer(question);
    }
}
