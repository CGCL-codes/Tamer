package org.vikamine.kernel.persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import de.d3web.kernel.domainModel.Answer;
import de.d3web.kernel.domainModel.IDObject;
import de.d3web.kernel.domainModel.KnowledgeBase;
import de.d3web.kernel.domainModel.NamedObject;
import de.d3web.kernel.domainModel.answers.AnswerChoice;
import de.d3web.kernel.domainModel.qasets.Question;
import de.d3web.kernel.domainModel.qasets.QuestionChoice;
import de.d3web.kernel.supportknowledge.Property;

/**
 * @author atzmueller
 */
public class ShortDescriptionInfoLoader {

    private static final String CONST_REPLACE = "REPLACE-";

    public ShortDescriptionInfoLoader() {
        super();
    }

    /**
     * @param kb
     * @param url
     * @return
     */
    public KnowledgeBase loadShortDescriptions(KnowledgeBase kb, URL url) {
        try {
            FileInputStream stream = new FileInputStream(new File(url.getFile()));
            try {
                ResourceBundle bundle = new PropertyResourceBundle(stream);
                List keys = Collections.list(bundle.getKeys());
                for (Iterator iter = keys.iterator(); iter.hasNext(); ) {
                    String key = (String) iter.next();
                    if (!key.startsWith(CONST_REPLACE)) {
                        IDObject idObject = kb.search(key);
                        if ((idObject != null) && (idObject instanceof NamedObject)) {
                            NamedObject no = (NamedObject) idObject;
                            no.getProperties().setProperty(Property.SHORT_DESCRIPTION, bundle.getString(key).trim());
                        }
                    } else {
                        String objectString = key.substring(key.indexOf(CONST_REPLACE) + CONST_REPLACE.length());
                        List questions = kb.getQuestions();
                        for (Iterator iterator = questions.iterator(); iterator.hasNext(); ) {
                            Question q = (Question) iterator.next();
                            if (q.getText().indexOf(objectString) != -1) {
                                String shortDescription = q.getText().replaceAll(objectString, bundle.getString(key).trim());
                                q.getProperties().setProperty(Property.SHORT_DESCRIPTION, shortDescription);
                            }
                            if (q instanceof QuestionChoice) {
                                List answers = ((QuestionChoice) q).getAllAlternatives();
                                for (Iterator answerIter = answers.iterator(); answerIter.hasNext(); ) {
                                    Answer ans = (Answer) answerIter.next();
                                    if (ans instanceof AnswerChoice) {
                                        AnswerChoice a = (AnswerChoice) ans;
                                        if (a.getText().indexOf(objectString) != -1) {
                                            String shortDescription = a.getText().replaceAll(objectString, bundle.getString(key).trim());
                                            a.getProperties().setProperty(Property.SHORT_DESCRIPTION, shortDescription);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (IOException e) {
                Logger.getLogger(getClass().getName()).throwing(getClass().getName(), "loadShortDescriptions", e);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(getClass().getName()).throwing(getClass().getName(), "loadShortDescriptions", ex);
        }
        return kb;
    }
}
