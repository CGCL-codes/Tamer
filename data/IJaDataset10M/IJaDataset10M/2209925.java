package net.sourceforge.brightside.estiemo.selenium;

import com.thoughtworks.selenium.*;
import java.util.regex.Pattern;

public class RegistrationPageTest extends SeleneseTestCase {

    public void setUp() throws Exception {
        setUp("http://localhost:8080/estiemo/", "*chrome");
    }

    public void testCreateAnAccountFailedPasswordDoNotMatch() throws Exception {
        selenium.open("/estiemo/registration");
        selenium.type("firstNameF", "Aleksandra");
        selenium.type("lastNameF", "Mrkic");
        selenium.type("usernameF", "alexandra");
        selenium.type("passwordF", "jovanka");
        selenium.type("confirmPasswordF", "jovankaaa");
        selenium.type("birthdayF", "11/11/1986");
        selenium.type("moduleF", "Information Systems");
        selenium.type("mailF", "aleksandra.mrkic@gmail.com");
        selenium.type("languagesA", "Serbian, English");
        selenium.type("educationA", "2001-present FON");
        selenium.type("workingExpirienceA", "...");
        selenium.type("facebookURLF", "www.facebook.com/aleksandra.mrkic");
        selenium.type("twitterURLF", "www.twitter.com/aleksandra.mrkic");
        selenium.type("linkedinURLF", "www.linkedin.com/aleksandra.mrkic");
        selenium.click("submit");
        selenium.waitForPageToLoad("30000");
        verifyTrue(selenium.isTextPresent("Password do not match!"));
    }

    public void testCreateAnAccountFailedUsernameAlreadyExist() throws Exception {
        selenium.open("/estiemo/registration");
        selenium.type("firstNameF", "Aleksandra");
        selenium.type("lastNameF", "Mrkic");
        selenium.type("usernameF", "joka");
        selenium.type("passwordF", "joka");
        selenium.type("confirmPasswordF", "joka");
        selenium.type("birthdayF", "11/11/1986");
        selenium.type("moduleF", "Information Systems");
        selenium.type("mailF", "aleksandra.mrkic@gmail.com");
        selenium.type("languagesA", "Serbian, English");
        selenium.type("educationA", "2001-present FON");
        selenium.type("workingExpirienceA", "...");
        selenium.type("facebookURLF", "www.facebook.com/aleksandra.mrkic");
        selenium.type("twitterURLF", "www.twitter.com/aleksandra.mrkic");
        selenium.type("linkedinURLF", "www.linkedin.com/aleksandra.mrkic");
        selenium.click("submit");
        selenium.waitForPageToLoad("60000");
        verifyTrue(selenium.isTextPresent("Username joka already exist!"));
    }
}
