package russotto.zplet.zmachine;

import java.util.*;
import java.awt.*;
import java.net.*;
import java.io.*;
import java.applet.Applet;

public abstract class ZDictionary {

    public abstract void tokenise(int textloc, int textlength, int parseloc);

    public abstract boolean parse_word(int textloc, int wordloc, int wordlength, int parseloc);
}
