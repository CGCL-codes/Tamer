package com.limespot.api;

public class GetBioTest extends LimeSpotTestCase {

    public void test1() {
        assertEquals(fake().getBio(), user().getBio());
    }
}
