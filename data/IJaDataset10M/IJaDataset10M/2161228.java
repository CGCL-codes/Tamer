package org.plenaquest.server.tests;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import org.plenadis.sdk.server.Connectable;
import org.plenaquest.common.question.Questionnaire;
import org.plenaquest.common.question.QuestionnaireModel;
import org.plenaquest.common.test.QuestionTest;
import org.plenaquest.server.persistence.PersistenceService;
import org.plenaquest.server.persistence.TableCreation;

public class TestEquals {

    public static void main(String[] args) {
        try {
            Connection connexion = null;
            final BDConnection c = new BDConnection();
            TableCreation creation = new TableCreation(c);
            creation.createTables();
            PersistenceService service = PersistenceService.getInstance(new Connectable() {

                public Connection getConnection() throws SQLException {
                    Connection res = null;
                    res = c.getConnection();
                    return res;
                }
            });
            QuestionnaireModel toSave = QuestionTest.getQuestionnaireModel();
            toSave.setTypeSurvey();
            int id = service.saveQuestionnaireModel(toSave);
            QuestionnaireModel loaded = service.loadQuestionnaireModel(id);
            System.out.println(toSave.equals(loaded));
            Questionnaire sauve = new Questionnaire(-1, "oho", "aha", loaded);
            sauve.setAnonymat(false);
            sauve.setCloseDate(new Date());
            sauve.setOpeningDate(new Date());
            sauve.setResultDate(new Date());
            sauve.setModeTiming(Questionnaire.MODETIMING_HOURSFIXED);
            sauve.setResponseStored(true);
            int id2 = service.saveQuestionnaire(sauve);
            Thread.sleep(100000);
            Questionnaire charge = service.loadQuestionnaire(id2);
            System.out.println(sauve.equals(charge));
            System.out.println("--------------------------------");
            System.out.println(charge.getCloseDate().getTime());
            System.out.println(sauve.getCloseDate().getTime());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }
}
