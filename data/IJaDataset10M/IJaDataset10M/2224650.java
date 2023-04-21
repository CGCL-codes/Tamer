package oast.geoip;

import java.net.InetAddress;

public interface GeoIPService {

    public Country getCountryByIP(InetAddress ipAddress);

    public Country getCountryByIP(String ipAddress);
}
