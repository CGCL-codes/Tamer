package net.sf.magicmap.server.utils;

/**
 * author        schweige
 * date          03.12.2004
 * copyright     (C) 2004 Martin Schweigert, Tobias H�bner
 * 
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 */
public class Util {

    public static final String trimMac(String mac) {
        return mac.replace(':', '-').toUpperCase().trim();
    }
}
