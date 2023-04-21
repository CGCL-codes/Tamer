package com.lise.markowitz.client.localization;

import com.google.gwt.core.client.GWT;

public class Localize {

    private static LocalizeConstant localizeConstant;

    public static LocalizeConstant getInstance() {
        if (localizeConstant == null) localizeConstant = GWT.create(LocalizeConstant.class);
        return localizeConstant;
    }
}
