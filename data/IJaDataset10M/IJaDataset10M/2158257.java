package com.clanwts.nbdf.kernel;

import java.util.Date;

public interface Service {

    public String getFriendlyName();

    public Version getVersion();

    public Date getReleaseDate();

    public String getDescription();
}
