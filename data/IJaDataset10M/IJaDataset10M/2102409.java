package cz.zcu.kiv.jet.jpa.data.generator.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import cz.zcu.kiv.jet.jpa.data.generator.Generator;
import cz.zcu.kiv.jet.jpa.data.generator.StatisticGenerator;

/**
 * Executable class for filling database with large data.
 * @author pona
 *
 */
public class Main {

    /**
	 * Specifies how many users will be stored in database.
	 */
    private static final int USERS_IN_APPLICATION = 2000;

    /**
	 * Specifies how many groups will be stored in database.
	 */
    private static final int GROUPS_IN_APPLICATION = USERS_IN_APPLICATION;

    /**
	 * Specifies how many messages will be present in the database.
	 */
    private static final int MESSAGES_IN_APPLICATION = USERS_IN_APPLICATION * 2;

    /**
	 * Starting date of creation of the messages. 
	 */
    private static final String DATE_FROM = "1/1/2009";

    /**
	 * Ending date of the creation of the messages.
	 */
    private static final String DATE_TO = "1/1/2010";

    /**
	 * Median of blips count in one message.
	 */
    private static final float BLIPS_IN_MESSAGES_MEDIAN = 2.3f;

    /**
	 * Minimum of blips count in one message.
	 */
    private static final int BLIPS_IN_MESSAGES_MINIMUM = 1;

    /**
	 * Minimum of blips count in one message.
	 */
    private static final int BLIPS_IN_MESSAGES_MAXIMUM = 15;

    /**
	 * Median of blips content's text length meassured in words. 
	 */
    private static final float TEXT_LENGTH_MEDIAN = 25f;

    /**
	 * Minimum of blips content's text length meassured in words. 
	 */
    private static final int TEXT_LENGTH_MINIMUM = 8;

    /**
	 * Maximum of blips content's text length meassured in words. 
	 */
    private static final int TEXT_LENGTH_MAXIMUM = 400;

    /**
	 * Median of users in one group count. 
	 */
    private static final float USERS_IN_GROUP_MEDIAN = 6f;

    /**
	 * Minimum of users in one group count. 
	 */
    private static final int USERS_IN_GROUP_MINIMUM = 1;

    /**
	 * Maximum of users in one group count. 
	 */
    private static final int USERS_IN_GROUP_MAXIMUM = 20;

    /**
	 * Format in which we'll parse the dates.
	 */
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    /**
	 * Generator for blips in messages count.
	 */
    private static final StatisticGenerator BLIPS_IN_MESSAGES_GENERATOR = new StatisticGeneratorImpl(BLIPS_IN_MESSAGES_MEDIAN, BLIPS_IN_MESSAGES_MINIMUM, BLIPS_IN_MESSAGES_MAXIMUM);

    /**
	 * Generator for blips content text length.
	 */
    private static final StatisticGenerator TEXT_LENGTH_GENERATOR = new StatisticGeneratorImpl(TEXT_LENGTH_MEDIAN, TEXT_LENGTH_MINIMUM, TEXT_LENGTH_MAXIMUM);

    /**
	 * Generator for users in one group count.
	 */
    private static final StatisticGenerator USERS_IN_GROUP_GENERATOR = new StatisticGeneratorImpl(USERS_IN_GROUP_MEDIAN, USERS_IN_GROUP_MINIMUM, USERS_IN_GROUP_MAXIMUM);

    public static void main(String[] args) {
        long time = System.currentTimeMillis();
        Logger log = Logger.getLogger(Main.class);
        log.debug("Start generovani zahajen: " + time);
        ApplicationContext appContext = new ClassPathXmlApplicationContext("/appctx-main.xml", "/appctx-orm-perf.xml", "/appctx-generator.xml");
        Generator generator = (Generator) appContext.getBean("generator2");
        Long[] users = generator.createUsers(USERS_IN_APPLICATION);
        log.debug("Users created");
        Long[] groups = generator.createGroups(GROUPS_IN_APPLICATION);
        log.debug("Groups created");
        generator.addUsersToGroups(users, groups, USERS_IN_GROUP_GENERATOR);
        log.debug("Users added to groups");
        try {
            generator.createMessagesWithBlips(users, groups, MESSAGES_IN_APPLICATION, DATE_FORMAT.parse(DATE_FROM), DATE_FORMAT.parse(DATE_TO), BLIPS_IN_MESSAGES_GENERATOR, TEXT_LENGTH_GENERATOR);
        } catch (ParseException e) {
            log.error("Chyba pri parsovani data");
        }
        log.debug("Celkova doba trvani generovani databaze v milisekundach: " + (System.currentTimeMillis() - time));
    }
}
