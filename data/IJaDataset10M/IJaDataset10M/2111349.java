package jf.exam.paint.util;

import java.util.*;
import java.io.*;
import java.awt.Image;
import java.awt.image.*;

class GifEncoderHashitem {

    public int rgb;

    public int count;

    public int index;

    public boolean isTransparent;

    public GifEncoderHashitem(int rgb, int count, int index, boolean isTransparent) {
        this.rgb = rgb;
        this.count = count;
        this.index = index;
        this.isTransparent = isTransparent;
    }
}
