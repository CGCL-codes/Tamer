package de.iteratec.turm.functionalTests.password;

import static de.iteratec.turm.functionalTests.user.Constants.BUTTON_ADD_ROLE;
import static de.iteratec.turm.functionalTests.user.Constants.BUTTON_REMOVE_ROLE;
import static de.iteratec.turm.functionalTests.user.Constants.BUTTON_USER_CREATEUPDATE;
import java.io.IOException;
import java.net.MalformedURLException;
import de.iteratec.turm.functionalTests.TurmTestBase;

public class PasswordTest extends TurmTestBase {

    @Override
    public void setUp() throws MalformedURLException, IOException {
        login();
        session.clickElementById("button_edit_user_4");
        session.clickElementById("option_user_availableRole_5");
        session.clickButtonById(BUTTON_ADD_ROLE);
        session.clickButtonById(BUTTON_USER_CREATEUPDATE);
        logout();
    }

    public void tearDown() throws MalformedURLException, IOException {
        login();
        session.clickElementById("button_edit_user_4");
        session.clickElementById("option_user_addedRole_3");
        session.clickButtonById(BUTTON_REMOVE_ROLE);
        session.clickButtonById(BUTTON_USER_CREATEUPDATE);
        logout();
    }

    public void testPasswordChange() throws MalformedURLException, IOException {
        session.goToPasswordPage();
        session.assertText("Bitte geben Sie Ihren Login, Ihr altes Passwort und ein neues Passwort ein.", false);
        session.setTextFieldById("text_password", "pass");
        session.setTextFieldById("text_passwordRepeated", "pass");
        session.clickButtonById("button_submit");
        session.assertText("Das Passwort ist zu kurz", false);
        session.setTextFieldById("text_password", "password");
        session.setTextFieldById("text_passwordRepeated", "password");
        session.clickButtonById("button_submit");
        session.assertText("Das Passwort ist nicht sicher", false);
        session.setTextFieldById("text_password", "Passw0rt");
        session.setTextFieldById("text_passwordRepeated", "Passw1rt");
        session.clickButtonById("button_submit");
        session.assertText("Die eingegebenen Passw�rter stimmen nicht �berein.", false);
        session.setTextFieldById("text_login_name", "mmu1");
        session.setTextFieldById("text_oldPassword", "Reader123");
        session.setTextFieldById("text_password", "Reader1234");
        session.setTextFieldById("text_passwordRepeated", "Reader1234");
        session.clickButtonById("button_submit");
        session.assertText("Der angegebene Loginname oder das Passwort ist falsch.", false);
        session.assertTextFieldContentsById("text_login_name", "mmu1");
        session.setTextFieldById("text_login_name", "mmu");
        session.setTextFieldById("text_oldPassword", "Reader1234");
        session.setTextFieldById("text_password", "Reader123");
        session.setTextFieldById("text_passwordRepeated", "Reader123");
        session.clickButtonById("button_submit");
        session.assertText("Der angegebene Loginname oder das Passwort ist falsch.", false);
        session.setTextFieldById("text_login_name", "mmu");
        session.setTextFieldById("text_oldPassword", "Reader123");
        session.setTextFieldById("text_password", "Reader123");
        session.setTextFieldById("text_passwordRepeated", "Reader123");
        session.clickButtonById("button_submit");
        session.assertText("Das neue Passwort ist das gleiche wie das alte Passwort. Bitte geben Sie ein neues Passwort ein.", false);
        session.setTextFieldById("text_login_name", "mmu");
        session.setTextFieldById("text_oldPassword", "Reader123");
        session.setTextFieldById("text_password", "Reader1234");
        session.setTextFieldById("text_passwordRepeated", "Reader1234");
        session.clickButtonById("button_submit");
        session.assertNoError();
        session.login("mmu", "Reader123", false);
        session.assertText("Der angegebene Loginname oder das Passwort ist falsch.", false);
        session.login("mmu", "Reader1234");
        logout();
        session.goToPasswordPage();
        session.setTextFieldById("text_login_name", "mmu");
        session.setTextFieldById("text_oldPassword", "Reader1234");
        session.setTextFieldById("text_password", "Reader123");
        session.setTextFieldById("text_passwordRepeated", "Reader123");
        session.clickButtonById("button_submit");
        session.assertNoError();
    }
}
