package sun.text.resources;

import java.util.ListResourceBundle;

public class FormatData_hu extends ListResourceBundle {

    /**
     * Overrides ListResourceBundle
     */
    protected final Object[][] getContents() {
        return new Object[][] { { "MonthNames", new String[] { "január", "február", "március", "április", "május", "június", "július", "augusztus", "szeptember", "október", "november", "december", "" } }, { "MonthAbbreviations", new String[] { "jan.", "febr.", "márc.", "ápr.", "máj.", "jún.", "júl.", "aug.", "szept.", "okt.", "nov.", "dec.", "" } }, { "DayNames", new String[] { "vasárnap", "hétfő", "kedd", "szerda", "csütörtök", "péntek", "szombat" } }, { "DayAbbreviations", new String[] { "V", "H", "K", "Sze", "Cs", "P", "Szo" } }, { "AmPmMarkers", new String[] { "DE", "DU" } }, { "Eras", new String[] { "i.e.", "i.u." } }, { "NumberElements", new String[] { ",", " ", ";", "%", "0", "#", "-", "E", "‰", "∞", "�" } }, { "DateTimePatterns", new String[] { "H:mm:ss z", "H:mm:ss z", "H:mm:ss", "H:mm", "yyyy. MMMM d.", "yyyy. MMMM d.", "yyyy.MM.dd.", "yyyy.MM.dd.", "{1} {0}" } }, { "DateTimePatternChars", "GanjkHmsSEDFwWxhKzZ" } };
    }
}
