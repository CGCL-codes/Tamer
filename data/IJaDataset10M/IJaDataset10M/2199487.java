package org.jugile.util;

/**
 * "And God said, Let us make man in our image, after our likeness: and let them 
 *  have dominion over the fish of the sea, and over the fowl of the air, 
 *  and over the cattle, and over all the earth, and over every creeping thing 
 *  that creepeth upon the earth. So God created man in his own image, 
 *  in the image of God created he him; male and female created he them." (Gen 1:26-27)
 * 
 * ==========
 * 
 * @author jukka.rahkonen@iki.fi
 */
public interface ISaxListener {

    public void startTag(String tag, int linenum);

    public void endTag(String tag);

    public void content(String cont);
}
