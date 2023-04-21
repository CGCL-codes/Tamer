package my_package;

import net.codemate.DbAssert;
import net.codemate.DbSource;
import net.codemate.Fixture;
import org.junit.Test;

public class PackageExternalTest {

    @Test(expected = RuntimeException.class)
    public void testIssue2() {
        DbAssert dbassert = DbAssert.init("net/codemate/databases.yml");
        dbassert.table("events").where("id", 1);
        dbassert.assert_column("name", "dglinenko");
    }

    @Test
    public void testThatPases() {
        DbAssert dbAssert = DbAssert.init("net/codemate/databases.yml");
        DbSource testSource = dbAssert.source("testSource");
        testSource.clean_table("events");
        final Fixture eventsFixture = testSource.fixture("net/codemate/fixtures/events.yml");
        final Fixture event_one = (Fixture) eventsFixture.get("event_one");
        dbAssert.table("events").where("id", String.valueOf(event_one.get("id")));
        dbAssert.assert_column("name", event_one.get("name"));
        dbAssert.assert_column("login_name", event_one.get("login_name"));
        testSource.close();
    }
}
