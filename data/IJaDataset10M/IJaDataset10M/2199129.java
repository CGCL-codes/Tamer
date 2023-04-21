package com.esl.web.util;

import java.text.SimpleDateFormat;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.esl.entity.practice.PracticeMedal;
import com.esl.model.*;
import com.esl.model.practice.PhoneticSymbols.Level;
import com.esl.web.model.practice.TopPracticeMedals;

public class LanguageUtil {

    private static Logger logger = LoggerFactory.getLogger("ESL");

    /**
	 * Set TopResult title, longTitle and Grade Title
	 * @param topResult
	 * @param locale
	 */
    public static TopResult formatTopResult(TopResult topResult, Locale locale) {
        if (topResult == null) return null;
        String bundleName = "messages.TopResult";
        ResourceBundle bundle = ResourceBundle.getBundle(bundleName, locale);
        String title = bundle.getString(topResult.getPracticeType() + "." + topResult.getOrderType() + ".title");
        String longTitle = bundle.getString(topResult.getPracticeType() + "." + topResult.getOrderType() + ".longTitle");
        String orderTypeTitle = bundle.getString(topResult.getOrderType().toString());
        String practiceTitle = bundle.getString(topResult.getPracticeType());
        String gradeTitle = "";
        if (topResult.getGrade() == null) {
            gradeTitle = bundle.getString("grade.all");
        } else {
            gradeTitle = bundle.getString("grade." + topResult.getGrade().getTitle());
        }
        topResult.setTitle(title);
        topResult.setLongTitle(longTitle);
        topResult.setOrderTypeTitle(orderTypeTitle);
        topResult.setPracticeTitle(practiceTitle);
        topResult.setGradeTitle(gradeTitle);
        topResult.setLevelTitle(getLevelTitle(topResult.getLevel(), locale));
        return topResult;
    }

    /**
	 * Set unavailable IPA for PhoneticQuestion
	 */
    public static PhoneticQuestion formatIPA(PhoneticQuestion question, Locale locale) {
        String bundleName = "messages.practice.SelfDictation";
        ResourceBundle bundle = ResourceBundle.getBundle(bundleName, locale);
        if (question.getIPA() == null || "".equals(question.getIPA())) {
            question.setIPA(bundle.getString("IPAUnavailable"));
        }
        return question;
    }

    /**
	 * Set grade Long Title
	 */
    public static Grade formatGradeDescription(Grade grade, Locale locale) {
        if (grade == null) return null;
        String bundleName = "messages.TopResult";
        ResourceBundle bundle = ResourceBundle.getBundle(bundleName, locale);
        grade.setDescription(bundle.getString("grade." + grade.getTitle()));
        return grade;
    }

    /**
	 * get Practice name
	 */
    public static String getPracticeType(String type, Locale locale) {
        String bundleName = "messages.TopResult";
        ResourceBundle bundle = ResourceBundle.getBundle(bundleName, locale);
        return bundle.getString(type);
    }

    /**
	 * get Level title
	 */
    public static String getLevelTitle(Level level, Locale locale) {
        final String logPrefix = "getLevelTitle: ";
        logger.debug("{}Input level [{}], locale [{}]", new Object[] { logPrefix, level, locale });
        if (level == null) return "";
        final String bundleName = "messages.practice.PhoneticSymbolPractice";
        ResourceBundle bundle = ResourceBundle.getBundle(bundleName, locale);
        return bundle.getString("level" + level.toString());
    }

    /**
	 * Return the UI model TopPracticeMedals
	 */
    public static TopPracticeMedals getTopPracticeMedals(List<PracticeMedal> medals, Locale locale) {
        final String logPrefix = "getTopPracticeMedals:";
        logger.debug("{} Input medals [{}]", new Object[] { logPrefix, medals });
        if (medals == null) return null;
        final String bundleName = "messages.TopResult";
        ResourceBundle bundle = ResourceBundle.getBundle(bundleName, locale);
        PracticeMedal m = medals.get(0);
        String practiceTitle = bundle.getString("ESLPracticeType." + m.getPracticeType().toString());
        String monthStr = new SimpleDateFormat("MMMM yyyy", locale).format(m.getAwardedDate());
        logger.debug("{} practiceTitle [{}], monthStr [{}]", new Object[] { logPrefix, practiceTitle, monthStr });
        TopPracticeMedals returnModel = new TopPracticeMedals(medals, practiceTitle, monthStr);
        return returnModel;
    }

    public static void main(String[] args) {
        SimpleDateFormat chi = new SimpleDateFormat("MMMM", Locale.TAIWAN);
        SimpleDateFormat eng = new SimpleDateFormat("MMMM", new Locale("en"));
        System.out.println(chi.format(new Date()));
        System.out.println(eng.format(new Date()));
    }
}
