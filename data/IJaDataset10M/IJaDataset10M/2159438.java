package org.biff.openpgpbb;

import net.rim.blackberry.api.options.OptionsManager;

/**
 * Main class to start application.
 */
class OpenPGPBB {

    public static void main(String args[]) {
        System.out.print("Registering Sign menu item...");
        new PGPSignMenuItem(0).registerInstance();
        System.out.println("done!");
        System.out.print("Registering Encrypt menu item...");
        new PGPEncryptMenuItem(1).registerInstance();
        System.out.println("done!");
        System.out.print("Registering Sign + Encrypt menu item...");
        new PGPSignEncryptMenuItem(2).registerInstance();
        System.out.println("done!");
        System.out.print("Registering Options Provider...");
        OptionsManager.registerOptionsProvider(new OpenPGPBBOptionsProvider());
        System.out.println("done!");
    }
}
