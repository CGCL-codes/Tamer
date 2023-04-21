package net.sf.cantina.system;

import org.jmock.MockObjectTestCase;
import org.jmock.cglib.Mock;
import net.sf.cantina.Realm;
import net.sf.cantina.User;
import net.sf.cantina.DataSource;
import java.util.Locale;

/**
 * @author Stephane JAIS
 */
public class ChangeRealmGroupPropertyTransactionTest extends MockObjectTestCase {

    public void testExecute() throws Exception {
        Mock realm = new Mock(Realm.class);
        Mock user = new Mock(User.class);
        user.expects(atLeastOnce()).method("hasAdministratorPrivileges").will(returnValue(true));
        Locale locale = new Locale("en");
        realm.expects(atLeastOnce()).method("setGroupDefaultLocale").with(same(locale));
        realm.expects(atLeastOnce()).method("getGroupDefaultLocale").withNoArguments().will(returnValue(null));
        Mock datasource = new Mock(DataSource.class);
        datasource.expects(atLeastOnce()).method("saveRealm").with(same(realm.proxy()));
        DataSource.instance = (DataSource) datasource.proxy();
        Transaction t = new ChangeRealmGroupPropertyTransaction((Realm) realm.proxy(), "defaultLocale", locale, (User) user.proxy());
        t.execute();
        realm.verify();
        user.verify();
        datasource.verify();
        t = new ChangeRealmGroupPropertyTransaction((Realm) realm.proxy(), "defaultLocale", "a string, not a locale", (User) user.proxy());
        try {
            t.execute();
        } catch (IllegalArgumentException e) {
            return;
        }
        fail("expected IllegalArgumentException");
    }
}
