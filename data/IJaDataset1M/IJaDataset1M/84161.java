package com.sun.j2me.security;

import com.sun.midp.security.Permissions;

/**
 * Location and landmark store access permissions.
 */
public class LocationPermission extends Permission {

    public static LocationPermission LOCATION = new LocationPermission(Permissions.getName(Permissions.LOCATION), null);

    public static LocationPermission ORIENTATION = new LocationPermission(Permissions.getName(Permissions.ORIENTATION), null);

    public static LocationPermission LOCATION_PROXIMITY = new LocationPermission(Permissions.getName(Permissions.LOCATION_PROXIMITY), null);

    public static LocationPermission LANDMARK_STORE_READ = new LocationPermission(Permissions.getName(Permissions.LANDMARK_READ), null);

    public static LocationPermission LANDMARK_STORE_WRITE = new LocationPermission(Permissions.getName(Permissions.LANDMARK_WRITE), null);

    public static LocationPermission LANDMARK_STORE_CATEGORY = new LocationPermission(Permissions.getName(Permissions.LANDMARK_CATEGORY), null);

    public static LocationPermission LANDMARK_STORE_MANAGE = new LocationPermission(Permissions.getName(Permissions.LANDMARK_MANAGE), null);

    public LocationPermission(String name, String resource) {
        super(name, resource);
    }
}
