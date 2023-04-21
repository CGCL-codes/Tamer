package com.jpacker.encoders;

/**
 * Normal (base62) encoder: [0-Z]
 * 
 * @author Pablo Santiago <pablo.santiago @ gmail.com>
 *
 */
public class NormalEncoder implements Encoder {

    @Override
    public String encode(int c) {
        return (c < 62 ? "" : encode(c / 62)) + ((c = c % 62) > 35 ? String.valueOf((char) (c + 29)) : Integer.toString(c, 36));
    }
}
