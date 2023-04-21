package flexmud.menu;

import flexmud.cfg.Preferences;
import flexmud.engine.context.ContextCommandAlias;
import flexmud.log.LoggingUtil;
import flexmud.util.Util;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.UUID;

public class TestContextMenuAliasDecorator {

    private static Logger LOGGER = Logger.getLogger(TestContextMenuAliasDecorator.class);

    private static String leftAcceleratorBracket;

    private static String rightAcceleratorBracket;

    private static String bulletSeparator;

    private String randomUUID;

    private String randomSingleCharString;

    private String randomMultiCharString;

    private ContextCommandAlias acceleratorAlias;

    static {
        LoggingUtil.resetConfiguration();
        LoggingUtil.configureLogging(Preferences.getPreference(Preferences.LOG4J_TEST_CONFIG_FILE));
        leftAcceleratorBracket = Preferences.getPreference(Preferences.ACCELERATOR_LEFT_BRACKET);
        rightAcceleratorBracket = Preferences.getPreference(Preferences.ACCELERATOR_RIGHT_BRACKET);
        bulletSeparator = Preferences.getPreference(Preferences.BULLET_SEPERATOR);
    }

    @Before
    public void setup() {
        randomUUID = UUID.randomUUID().toString();
        randomSingleCharString = Util.getRandomSubstring(randomUUID, 1);
        randomMultiCharString = Util.getRandomSubstring(randomUUID, Util.getRandomBounded(2, randomUUID.length()));
        acceleratorAlias = new ContextCommandAlias();
    }

    @Test
    public void testSingleCharAccleratorAlias() {
        String decoratedString;
        acceleratorAlias.setAccelerator(true);
        acceleratorAlias.setAlias(randomSingleCharString);
        decoratedString = ContextMenuAliasDecorator.decorate(randomUUID, acceleratorAlias);
        LOGGER.info("Random string: " + randomUUID + ", alias: " + randomSingleCharString + ", decorated string: " + decoratedString);
        Assert.assertEquals("Left accelerator bracket was not found at the position of the first occurance of the alias string; ", decoratedString.indexOf(leftAcceleratorBracket), randomUUID.indexOf(acceleratorAlias.getAlias()));
        Assert.assertEquals("Right accelerator bracket was not found two positions to the right of the first occurance of the alias string", decoratedString.indexOf(rightAcceleratorBracket), randomUUID.indexOf(acceleratorAlias.getAlias()) + 2);
    }

    @Test
    public void testMultiCharAccleratorAlias() {
        String decoratedString;
        acceleratorAlias.setAccelerator(true);
        acceleratorAlias.setAlias(randomMultiCharString);
        decoratedString = ContextMenuAliasDecorator.decorate(randomUUID, acceleratorAlias);
        LOGGER.info("Random string: " + randomUUID + ", alias: " + randomMultiCharString + ", decorated string: " + decoratedString);
        Assert.assertEquals("Left accelerator bracket was not found at the position of the first occurance of the alias string; ", decoratedString.indexOf(leftAcceleratorBracket), randomUUID.indexOf(acceleratorAlias.getAlias()));
        Assert.assertEquals("Right accelerator bracket was not found two positions to the right of the first occurance of the alias string", decoratedString.indexOf(rightAcceleratorBracket), randomUUID.indexOf(acceleratorAlias.getAlias()) + acceleratorAlias.getAlias().length() + 1);
    }

    @Test
    public void testSingleCharBulletAlias() {
        String decoratedString;
        acceleratorAlias.setBullet(true);
        acceleratorAlias.setAlias(randomSingleCharString);
        decoratedString = ContextMenuAliasDecorator.decorate(randomUUID, acceleratorAlias);
        LOGGER.info("Random string: " + randomUUID + ", alias: " + randomSingleCharString + ", decorated string: " + decoratedString);
        Assert.assertEquals("Bullet was not found at the position of the first occurance of the alias string; ", decoratedString.indexOf(randomSingleCharString), 0);
        Assert.assertEquals("Bullet seperator and space were not found immediately after the bullet; ", decoratedString.indexOf(bulletSeparator + " "), randomSingleCharString.length());
    }

    @Test
    public void testMultiCharBulletAlias() {
        String decoratedString;
        acceleratorAlias.setBullet(true);
        acceleratorAlias.setAlias(randomMultiCharString);
        decoratedString = ContextMenuAliasDecorator.decorate(randomUUID, acceleratorAlias);
        LOGGER.info("Random string: " + randomUUID + ", alias: " + randomMultiCharString + ", decorated string: " + decoratedString);
        Assert.assertEquals("Bullet was not found at the position of the first occurance of the alias string; ", decoratedString.indexOf(randomMultiCharString), 0);
        Assert.assertEquals("Bullet seperator and space were not found immediately after the bullet; ", decoratedString.indexOf(bulletSeparator + " "), randomMultiCharString.length());
    }

    @Test
    public void testSingleCharAcceleratorBulletAlias() {
        String decoratedString;
        acceleratorAlias.setBullet(true);
        acceleratorAlias.setAccelerator(true);
        acceleratorAlias.setAlias(randomSingleCharString);
        decoratedString = ContextMenuAliasDecorator.decorate(randomUUID, acceleratorAlias);
        LOGGER.info("Random string: " + randomUUID + ", alias: " + randomSingleCharString + ", decorated string: " + decoratedString);
        Assert.assertEquals("Left accelerator bracket was not found at the position of the first occurance of the alias string; ", decoratedString.indexOf(leftAcceleratorBracket, 1), randomUUID.indexOf(acceleratorAlias.getAlias()) + 3);
        Assert.assertEquals("Right accelerator bracket was not found two positions to the right of the first occurance of the alias string", decoratedString.indexOf(rightAcceleratorBracket, 1), randomUUID.indexOf(acceleratorAlias.getAlias()) + 5);
        Assert.assertEquals("Bullet was not found at the position of the first occurance of the alias string; ", decoratedString.indexOf(randomSingleCharString), 0);
        Assert.assertEquals("Bullet seperator and space were not found immediately after the bullet; ", decoratedString.indexOf(bulletSeparator + " "), randomSingleCharString.length());
    }

    @Test
    public void testMultiCharAcceleratorBulletAlias() {
        String decoratedString;
        acceleratorAlias.setBullet(true);
        acceleratorAlias.setAccelerator(true);
        acceleratorAlias.setAlias(randomMultiCharString);
        decoratedString = ContextMenuAliasDecorator.decorate(randomUUID, acceleratorAlias);
        LOGGER.info("Random string: " + randomUUID + ", alias: " + randomMultiCharString + ", decorated string: " + decoratedString);
        Assert.assertEquals("Left accelerator bracket was not found at the position of the first occurance of the alias string; ", decoratedString.indexOf(leftAcceleratorBracket), decoratedString.indexOf(randomMultiCharString, 1) - 1);
        Assert.assertEquals("Right accelerator bracket was not found two positions to the right of the first occurance of the alias string", decoratedString.indexOf(rightAcceleratorBracket), decoratedString.indexOf(randomMultiCharString, 1) + randomMultiCharString.length());
        Assert.assertEquals("Bullet was not before the second occurance of the alias string; ", decoratedString.indexOf(randomMultiCharString), 0);
        Assert.assertEquals("Bullet seperator and space were not found immediately after the bullet; ", decoratedString.indexOf(bulletSeparator + " "), randomMultiCharString.length());
    }
}
