package org.manaty.dde;

import java.io.IOException;
import java.io.StringReader;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.naming.InvalidNameException;
import org.manaty.dde.communication.CommunicationChannel;
import org.manaty.model.dde.DateRecordField;
import org.manaty.model.dde.NumberRecordField;
import org.manaty.model.dde.Record;
import org.manaty.model.dde.StringRecordField;
import org.manaty.model.dde.constraint.AllMandatoryConstraint;
import org.manaty.model.dde.constraint.OneMandatoryConstraint;
import junit.framework.TestCase;

public class CoreAnalyserTest extends TestCase {

    CommunicationChannel userChanel;

    public CoreAnalyserTest() {
        System.out.println("start test");
        userChanel = new CommunicationChannel(null, null);
    }

    public void testAnalyse() {
        CoreAnalyser analyser = new CoreAnalyser();
        analyser.setUserChanel(userChanel);
        analyser.setUserContext(null);
        StringReader st = new StringReader("sebastien,michea,16/11/1972,182\nmathilde,michea,10/05/1995,165\nalice,michea,05/11/2000,125\npaul,michea,17/05/2009,40");
        Record expectedRecordObject = new Record();
        try {
            expectedRecordObject.setClassame("Coords");
            expectedRecordObject.setName("ddsd");
            StringRecordField firstname = new StringRecordField();
            firstname.setName("firstname");
            firstname.setRegex("[a-zA-Z]+");
            expectedRecordObject.addField(firstname);
            StringRecordField lastname = new StringRecordField();
            lastname.setName("lastname");
            lastname.setRegex("[a-zA-Z]+");
            expectedRecordObject.addField(lastname);
            DateRecordField dob = new DateRecordField();
            dob.setName("dob");
            dob.setDefaultToCurrentDate(false);
            dob.setDateFormat(new SimpleDateFormat("ddMMyyyy"));
            expectedRecordObject.addField(dob);
            NumberRecordField size = new NumberRecordField();
            size.setName("size");
            size.setNumberFormat(NumberFormat.getInstance());
            expectedRecordObject.addField(size);
            expectedRecordObject.addConstraint(new AllMandatoryConstraint(lastname));
            expectedRecordObject.addConstraint(new OneMandatoryConstraint(firstname, dob));
            analyser.analyse(st, expectedRecordObject, System.currentTimeMillis() + 900000L);
        } catch (InvalidNameException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
