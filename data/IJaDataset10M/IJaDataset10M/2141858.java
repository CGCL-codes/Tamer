package com.android.email.service;

/**
 * {@link EasAuthenticatorService} used with the alternative label.
 *
 * <p>Functionality wise, it's a 100% clone of {@link EasAuthenticatorService}, but in order to
 * independently disable/enable each service we need to give it a different class name.
 */
public class EasAuthenticatorServiceAlternate extends EasAuthenticatorService {
}
