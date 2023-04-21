package com.google.gwt.storage.client;

/**
 * Tests Session {@link StorageMap}.
 * 
 * Because HtmlUnit does not support Storage, you will need to run these tests
 * manually by adding this line to your VM args: -Dgwt.args="-runStyle Manual:1"
 * If you are using Eclipse and GPE, go to "run configurations" or
 * "debug configurations", select the test you would like to run, and put this
 * line in the VM args under the arguments tab: -Dgwt.args="-runStyle Manual:1"
 */
public class SessionStorageMapTest extends StorageMapTest {

    @Override
    Storage getStorage() {
        return Storage.getLocalStorageIfSupported();
    }
}
