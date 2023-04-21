package fitlibrary.specify;

import fitlibrary.DoFixture;

public class WithinFlow extends DoFixture {

    public DoFixtureSetUp withSetUp() {
        return new DoFixtureSetUp();
    }

    public DoFixtureTearDown withTearDown() {
        return new DoFixtureTearDown();
    }

    public DoFixtureSetUpWithException withSetUpException() {
        return new DoFixtureSetUpWithException();
    }
}
