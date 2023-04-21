package uk.ac.osswatch.simal.ssmm;

import java.util.LinkedHashMap;
import uk.ac.osswatch.simal.ssmm.model.QuestionSet;
import uk.ac.osswatch.simal.ssmm.model.SelectionQuestion;

public class KnowledgeQuestions extends QuestionSet {

    public KnowledgeQuestions() {
        LinkedHashMap<String, String> options = new LinkedHashMap<String, String>();
        options.put("User Docs", "User documentation section on website");
        options.put("Design Docs", "Design documentation");
        options.put("Roadmap", "Managed project roadmap");
        options.put("Metadata", "Machine readable meta-data");
        options.put("wiki", "Publicly writeable wiki");
        options.put("Revision control", "Version control system");
        options.put("Discussion", "Email lists or online forums");
        options.put("IM", "Instant messaging/IRC");
        options.put("Issue Tracker", "Issue tracker for bug and feature tracking");
        SelectionQuestion question = new SelectionQuestion("Communication Channels", "Which publicly available communication or dissemination mechanisms does the project use?", "Multiple documentation and communication components are indicative of at least the opportunity for project knowledge to exist. There are certainly cases where too many avenues of knowledge can hurt a project.", options);
        put(question.getLabel(), question);
    }
}
