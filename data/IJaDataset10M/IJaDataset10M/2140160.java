package oxygen.util;

/**
 * This file is like this:
 * admin.txt (properties file containing filename, head version, extension)
 * description-n.txt
 * version-n.$extension
 * 
 * Currently, this class is not being used. Managing a zip archive is kinda heavyweight - so we just 
 * use the flat file. Also, the truezip does things with tmp files and a Shutdown Handler which 
 * I'm not a fan of.
 *
 * Making this abstract for now, and commenting everything out. Apr 16, 2007. Ugorji.
 */
public abstract class OxygenVersioningArchiveFullZipStoreImpl implements OxygenVersioningArchive {
}
