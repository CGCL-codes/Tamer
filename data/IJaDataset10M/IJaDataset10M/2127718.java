package org.xith3d.utility.properties;

/**
 * :Id: PropertyWriter.java,v 1.5 2003/02/24 00:13:53 wurp Exp $
 * 
 * :Log: PropertyWriter.java,v $
 * Revision 1.5  2003/02/24 00:13:53  wurp
 * Formatted all java code for cvs (strictSunConvention.xml)
 * 
 * Revision 1.4  2001/06/20 04:05:42  wurp
 * added log4j.
 * 
 * Revision 1.3  2001/01/28 07:52:20  wurp
 * Removed <dollar> from Id and Log in log comments.
 * Added several new commands to AdminApp
 * Unfortunately, several other changes that I have lost track of.  Try diffing this
 * version with the previous one.
 * 
 * Revision 1.2  2000/12/16 22:07:33  wurp
 * Added Id and Log to almost all of the files that didn't have it.  It's
 * possible that the script screwed something up.  I did a commit and an update
 * right before I ran the script, so if a file is screwed up you should be able
 * to fix it by just going to the version before this one.
 * 
 * @author David Yazel
 */
public interface PropertyWriter {

    void write(String name, int value);

    void write(String name, float value);

    void write(String name, double value);

    void write(String name, String value);

    void write(Property value);
}
