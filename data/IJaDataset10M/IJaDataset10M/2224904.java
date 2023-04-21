package com.once.server.license;

public interface ILicenseManager {

    public String LICENSE_TYPE_COMMERCIAL = "Commercial license.";

    public String LICENSE_TYPE_DEVELOPMENT = "Internal development license.";

    public String LICENSE_TYPE_OPEN_SOURCE = "Copyright once:technologies 2004-2009. Released under the GPL V3 licence.";

    public String LICENSE_TYPE_NONE = "No license";

    public String LICENSE_PRODUCT = "once:radix";

    public String getLicenseType();

    public String getProductName();

    public int getMaxConcurrentUsers();

    public String getOwnerDetails();

    public String quoteBlockData(String data);

    public String dequoteBlockData(String data);
}
