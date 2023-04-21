package de.flingelli.scrum.datastructure.language;

import java.util.Locale;
import junit.framework.Assert;
import org.junit.Test;
import de.flingelli.scrum.datastructure.appointment.EAppointmentType;

public class TestCaseEAppointmentType {

    @Test
    public void testEnglish() {
        Locale.setDefault(Locale.ENGLISH);
        Assert.assertEquals("Business trip", TranslateEnums.getAppointmentType(EAppointmentType.BUSINESS_TRIP));
        Assert.assertEquals("Holiday", TranslateEnums.getAppointmentType(EAppointmentType.HOLIDAY));
        Assert.assertEquals("Other", TranslateEnums.getAppointmentType(EAppointmentType.OTHER));
        Assert.assertEquals("Private", TranslateEnums.getAppointmentType(EAppointmentType.PRIVATE));
        Assert.assertEquals("Qualification", TranslateEnums.getAppointmentType(EAppointmentType.QUALIFICATION));
        Assert.assertEquals("Sprint planning 1", TranslateEnums.getAppointmentType(EAppointmentType.SPRINT_PLANNING_1));
        Assert.assertEquals("Sprint planning 2", TranslateEnums.getAppointmentType(EAppointmentType.SPRINT_PLANNING_2));
        Assert.assertEquals("Sprint review", TranslateEnums.getAppointmentType(EAppointmentType.SPRINT_REVIEW));
        Assert.assertEquals("Sprint retrospective", TranslateEnums.getAppointmentType(EAppointmentType.SPRINT_RETROSPEKTIVE));
        Assert.assertEquals("Daily Scrum", TranslateEnums.getAppointmentType(EAppointmentType.DAILY_SCRUM));
    }

    @Test
    public void testGerman() {
        Locale.setDefault(Locale.GERMAN);
        Assert.assertEquals("Urlaub", TranslateEnums.getAppointmentType(EAppointmentType.HOLIDAY));
        Assert.assertEquals("Geschäftsreise", TranslateEnums.getAppointmentType(EAppointmentType.BUSINESS_TRIP));
        Assert.assertEquals("Sonstiges", TranslateEnums.getAppointmentType(EAppointmentType.OTHER));
        Assert.assertEquals("Privat", TranslateEnums.getAppointmentType(EAppointmentType.PRIVATE));
        Assert.assertEquals("Weiterbildung", TranslateEnums.getAppointmentType(EAppointmentType.QUALIFICATION));
        Assert.assertEquals("Sprint Planung 1", TranslateEnums.getAppointmentType(EAppointmentType.SPRINT_PLANNING_1));
        Assert.assertEquals("Sprint Planung 2", TranslateEnums.getAppointmentType(EAppointmentType.SPRINT_PLANNING_2));
        Assert.assertEquals("Sprint Review", TranslateEnums.getAppointmentType(EAppointmentType.SPRINT_REVIEW));
        Assert.assertEquals("Sprint Retrospektive", TranslateEnums.getAppointmentType(EAppointmentType.SPRINT_RETROSPEKTIVE));
        Assert.assertEquals("Daily Scrum", TranslateEnums.getAppointmentType(EAppointmentType.DAILY_SCRUM));
    }
}
