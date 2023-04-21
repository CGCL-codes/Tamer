package com.crypticbit.ipa.licence;

import com.crypticbit.ipa.entity.status.Info;

public class UnlimitedLicenceValidator implements LicenceValidator {

    public static final LicenceValidator DEFAULT_INSTANCE = new UnlimitedLicenceValidator();

    @Override
    public void checkLicence(final Info info) throws NotLicencedException {
    }
}
