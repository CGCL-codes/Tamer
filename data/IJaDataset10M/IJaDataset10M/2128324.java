package com.google.gwt.inject.client.nonpublic;

import com.google.gwt.inject.client.Ginjector;
import com.google.gwt.inject.client.nonpublic.secret.SecretMain;

public interface NonPublicGinjector extends Ginjector {

    SecretMain getMain();
}
