package com.jcorporate.expresso.ext.report;

import java.io.FilterReader;
import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

/**
 *  A FilterReader that understands the docbook entities and converts them to 
 *  their character codes. This class is not threadsafe within a single instance 
 *  and care should be taken.
 * @author David Lloyd
 */
public class DocBookFilterReader extends FilterReader {

    /** The buffer holding characters read after an amperstand that may be an entity. */
    private char[] lookahead = new char[32];

    /** The buffer holding characters to be read out first. */
    private char[] pushahead = new char[32];

    private int pushaheadOffset = 0;

    private int pushaheadLen = 0;

    /** Map entity to character code. */
    private static Map entities = new HashMap();

    static {
        loadEntites(entities);
    }

    /** 
     * Create the reader on a stream.
     * @param in The underlying input stream.
     */
    public DocBookFilterReader(Reader in) {
        super(new PushbackReader(in, 32));
    }

    /** Override from Reader. */
    public int read() throws IOException {
        int ch;
        if (pushaheadLen > 0) {
            ch = pushahead[pushaheadOffset++];
            pushaheadLen--;
        } else {
            do {
                ch = in.read();
            } while (ch == 0);
            if (ch == '&') {
                int ch2 = -1;
                int n = 0;
                lookahead[n++] = (char) ch;
                while (n < lookahead.length) {
                    ch2 = in.read();
                    if (ch2 == -1) break;
                    lookahead[n++] = (char) ch2;
                    if (!Character.isLetterOrDigit((char) ch2)) break;
                }
                if (ch2 == ';') {
                    ch2 = lookupEntityToChar(n);
                    if (ch2 == -1) ((PushbackReader) in).unread(lookahead, 1, n - 1); else ch = ch2;
                } else {
                    ((PushbackReader) in).unread(lookahead, 1, n - 1);
                }
            }
        }
        return ch;
    }

    /** Override from Reader. */
    public int read(char[] cbuf, int off, int len) throws IOException {
        int nread = 0;
        for (int i = off; nread < len; i++) {
            int c = read();
            if (c == -1) break;
            cbuf[i] = (char) c;
            nread++;
        }
        if (nread == 0) return -1;
        return nread;
    }

    /**
     * Lookup the entity and return the first char.
     * @param nlook The size of the lookahead buffer that contains the potential entity.
     * @return The first char of the transformation or -1 on error.
     * @postcondition The pushahead buffer is loaded with the full transformation.
     */
    private int lookupEntityToChar(int nlook) {
        if (lookupEntity(nlook)) {
            if (pushaheadLen > 0) {
                pushaheadLen--;
                return pushahead[pushaheadOffset++];
            }
        }
        return -1;
    }

    /**
     * Lookup the entity.
     * @param nlook The size of the lookahead buffer that contains the potential entity.
     * @return true if the entity was found.
     * @postcondition The pushahead buffer is loaded with the full transformation.
     */
    private boolean lookupEntity(int nlook) {
        String lookaheadStr = new String(lookahead, 0, nlook);
        String entity = (String) entities.get(lookaheadStr);
        if (entity != null) {
            entity.getChars(0, entity.length(), pushahead, 0);
            pushaheadOffset = 0;
            pushaheadLen = entity.length();
            return true;
        }
        return false;
    }

    private static void loadEntites(Map entities) {
        entities.put("&half;", "&#x00BD;");
        entities.put("&emsp;", "&#x2003;");
        entities.put("&ensp;", "&#x2002;");
        entities.put("&emsp13;", "&#x2004;");
        entities.put("&emsp14;", "&#x2005;");
        entities.put("&numsp;", "&#x2007;");
        entities.put("&puncsp;", "&#x2008;");
        entities.put("&thinsp;", "&#x2009;");
        entities.put("&hairsp;", "&#x200A;");
        entities.put("&mdash;", "&#x2014;");
        entities.put("&ndash;", "&#x2013;");
        entities.put("&dash;", "&#x2010;");
        entities.put("&blank;", "&#x2423;");
        entities.put("&hellip;", "&#x2026;");
        entities.put("&nldr;", "&#x2025;");
        entities.put("&frac13;", "&#x2153;");
        entities.put("&frac23;", "&#x2154;");
        entities.put("&frac15;", "&#x2155;");
        entities.put("&frac25;", "&#x2156;");
        entities.put("&frac35;", "&#x2157;");
        entities.put("&frac45;", "&#x2158;");
        entities.put("&frac16;", "&#x2159;");
        entities.put("&frac56;", "&#x215A;");
        entities.put("&incare;", "&#x2105;");
        entities.put("&block;", "&#x2588;");
        entities.put("&uhblk;", "&#x2580;");
        entities.put("&lhblk;", "&#x2584;");
        entities.put("&blk14;", "&#x2591;");
        entities.put("&blk12;", "&#x2592;");
        entities.put("&blk34;", "&#x2593;");
        entities.put("&marker;", "&#x25AE;");
        entities.put("&cir;", "&#x25CB;");
        entities.put("&squ;", "&#x25A1;");
        entities.put("&rect;", "&#x25AD;");
        entities.put("&utri;", "&#x25B5;");
        entities.put("&dtri;", "&#x25BF;");
        entities.put("&star;", "&#x22C6;");
        entities.put("&bull;", "&#x2022;");
        entities.put("&squf;", "&#x25AA;");
        entities.put("&utrif;", "&#x25B4;");
        entities.put("&dtrif;", "&#x25BE;");
        entities.put("&ltrif;", "&#x25C2;");
        entities.put("&rtrif;", "&#x25B8;");
        entities.put("&clubs;", "&#x2663;");
        entities.put("&diams;", "&#x2666;");
        entities.put("&hearts;", "&#x2665;");
        entities.put("&spades;", "&#x2660;");
        entities.put("&malt;", "&#x2720;");
        entities.put("&dagger;", "&#x2020;");
        entities.put("&Dagger;", "&#x2021;");
        entities.put("&check;", "&#x2713;");
        entities.put("&cross;", "&#x2717;");
        entities.put("&sharp;", "&#x266F;");
        entities.put("&flat;", "&#x266D;");
        entities.put("&male;", "&#x2642;");
        entities.put("&female;", "&#x2640;");
        entities.put("&phone;", "&#x260E;");
        entities.put("&telrec;", "&#x2315;");
        entities.put("&copysr;", "&#x2117;");
        entities.put("&caret;", "&#x2041;");
        entities.put("&lsquor;", "&#x201A;");
        entities.put("&ldquor;", "&#x201E;");
        entities.put("&fflig;", "&#xFB00;");
        entities.put("&filig;", "&#xFB01;");
        entities.put("&ffilig;", "&#xFB03;");
        entities.put("&ffllig;", "&#xFB04;");
        entities.put("&fllig;", "&#xFB02;");
        entities.put("&mldr;", "&#x2026;");
        entities.put("&rdquor;", "&#x201C;");
        entities.put("&rsquor;", "&#x2018;");
        entities.put("&vellip;", "&#x22EE;");
        entities.put("&hybull;", "&#x2043;");
        entities.put("&loz;", "&#x25CA;");
        entities.put("&lozf;", "&#x2726;");
        entities.put("&ltri;", "&#x25C3;");
        entities.put("&rtri;", "&#x25B9;");
        entities.put("&starf;", "&#x2605;");
        entities.put("&natur;", "&#x266E;");
        entities.put("&rx;", "&#x211E;");
        entities.put("&sext;", "&#x2736;");
        entities.put("&target;", "&#x2316;");
        entities.put("&dlcrop;", "&#x230D;");
        entities.put("&drcrop;", "&#x230C;");
        entities.put("&ulcrop;", "&#x230F;");
        entities.put("&urcrop;", "&#x230E;");
        entities.put("&agr;", "&#x03B1;");
        entities.put("&Agr;", "&#x0391;");
        entities.put("&bgr;", "&#x03B2;");
        entities.put("&Bgr;", "&#x0392;");
        entities.put("&ggr;", "&#x03B3;");
        entities.put("&Ggr;", "&#x0393;");
        entities.put("&dgr;", "&#x03B4;");
        entities.put("&Dgr;", "&#x0394;");
        entities.put("&egr;", "&#x03B5;");
        entities.put("&Egr;", "&#x0395;");
        entities.put("&zgr;", "&#x03B6;");
        entities.put("&Zgr;", "&#x0396;");
        entities.put("&eegr;", "&#x03B7;");
        entities.put("&EEgr;", "&#x0397;");
        entities.put("&thgr;", "&#x03B8;");
        entities.put("&THgr;", "&#x0398;");
        entities.put("&igr;", "&#x03B9;");
        entities.put("&Igr;", "&#x0399;");
        entities.put("&kgr;", "&#x03BA;");
        entities.put("&Kgr;", "&#x039A;");
        entities.put("&lgr;", "&#x03BB;");
        entities.put("&Lgr;", "&#x039B;");
        entities.put("&mgr;", "&#x03BC;");
        entities.put("&Mgr;", "&#x039C;");
        entities.put("&ngr;", "&#x03BD;");
        entities.put("&Ngr;", "&#x039D;");
        entities.put("&xgr;", "&#x03BE;");
        entities.put("&Xgr;", "&#x039E;");
        entities.put("&ogr;", "&#x03BF;");
        entities.put("&Ogr;", "&#x039F;");
        entities.put("&pgr;", "&#x03C0;");
        entities.put("&Pgr;", "&#x03A0;");
        entities.put("&rgr;", "&#x03C1;");
        entities.put("&Rgr;", "&#x03A1;");
        entities.put("&sgr;", "&#x03C3;");
        entities.put("&Sgr;", "&#x03A3;");
        entities.put("&sfgr;", "&#x03C2;");
        entities.put("&tgr;", "&#x03C4;");
        entities.put("&Tgr;", "&#x03A4;");
        entities.put("&ugr;", "&#x03C5;");
        entities.put("&Ugr;", "&#x03A5;");
        entities.put("&phgr;", "&#x03C6;");
        entities.put("&PHgr;", "&#x03A6;");
        entities.put("&khgr;", "&#x03C7;");
        entities.put("&KHgr;", "&#x03A7;");
        entities.put("&psgr;", "&#x03C8;");
        entities.put("&PSgr;", "&#x03A8;");
        entities.put("&ohgr;", "&#x03C9;");
        entities.put("&OHgr;", "&#x03A9;");
        entities.put("&aacgr;", "&#x03AC;");
        entities.put("&Aacgr;", "&#x0386;");
        entities.put("&eacgr;", "&#x03AD;");
        entities.put("&Eacgr;", "&#x0388;");
        entities.put("&eeacgr;", "&#x03AE;");
        entities.put("&EEacgr;", "&#x0389;");
        entities.put("&idigr;", "&#x03CA;");
        entities.put("&Idigr;", "&#x03AA;");
        entities.put("&iacgr;", "&#x03AF;");
        entities.put("&Iacgr;", "&#x038A;");
        entities.put("&idiagr;", "&#x0390;");
        entities.put("&oacgr;", "&#x03CC;");
        entities.put("&Oacgr;", "&#x038C;");
        entities.put("&udigr;", "&#x03CB;");
        entities.put("&Udigr;", "&#x03AB;");
        entities.put("&uacgr;", "&#x03CD;");
        entities.put("&Uacgr;", "&#x038E;");
        entities.put("&udiagr;", "&#x03B0;");
        entities.put("&ohacgr;", "&#x03CE;");
        entities.put("&OHacgr;", "&#x038F;");
        entities.put("&alpha;", "&#x03B1;");
        entities.put("&beta;", "&#x03B2;");
        entities.put("&gamma;", "&#x03B3;");
        entities.put("&Gamma;", "&#x0393;");
        entities.put("&gammad;", "&#x03DC;");
        entities.put("&delta;", "&#x03B4;");
        entities.put("&Delta;", "&#x0394;");
        entities.put("&epsi;", "&#x220A;");
        entities.put("&epsiv;", "&#x03B5;");
        entities.put("&epsis;", "&#x220A;");
        entities.put("&zeta;", "&#x03B6;");
        entities.put("&eta;", "&#x03B7;");
        entities.put("&thetas;", "&#x03B8;");
        entities.put("&Theta;", "&#x0398;");
        entities.put("&thetav;", "&#x03D1;");
        entities.put("&iota;", "&#x03B9;");
        entities.put("&kappa;", "&#x03BA;");
        entities.put("&kappav;", "&#x03F0;");
        entities.put("&lambda;", "&#x03BB;");
        entities.put("&Lambda;", "&#x039B;");
        entities.put("&mu;", "&#x03BC;");
        entities.put("&nu;", "&#x03BD;");
        entities.put("&xi;", "&#x03BE;");
        entities.put("&Xi;", "&#x039E;");
        entities.put("&pi;", "&#x03C0;");
        entities.put("&piv;", "&#x03D6;");
        entities.put("&Pi;", "&#x03A0;");
        entities.put("&rho;", "&#x03C1;");
        entities.put("&rhov;", "&#x03F1;");
        entities.put("&sigma;", "&#x03C3;");
        entities.put("&Sigma;", "&#x03A3;");
        entities.put("&sigmav;", "&#x03C2;");
        entities.put("&tau;", "&#x03C4;");
        entities.put("&upsi;", "&#x03C5;");
        entities.put("&Upsi;", "&#x03D2;");
        entities.put("&phis;", "&#x03C6;");
        entities.put("&Phi;", "&#x03A6;");
        entities.put("&phiv;", "&#x03D5;");
        entities.put("&chi;", "&#x03C7;");
        entities.put("&psi;", "&#x03C8;");
        entities.put("&Psi;", "&#x03A8;");
        entities.put("&omega;", "&#x03C9;");
        entities.put("&Omega;", "&#x03A9;");
        entities.put("&b.alpha;", "&#x03B1;");
        entities.put("&b.beta;", "&#x03B2;");
        entities.put("&b.gamma;", "&#x03B3;");
        entities.put("&b.Gamma;", "&#x0393;");
        entities.put("&b.gammad;", "&#x03DC;");
        entities.put("&b.delta;", "&#x03B4;");
        entities.put("&b.Delta;", "&#x0394;");
        entities.put("&b.epsi;", "&#x03B5;");
        entities.put("&b.epsiv;", "&#x03B5;");
        entities.put("&b.epsis;", "&#x03B5;");
        entities.put("&b.zeta;", "&#x03B6;");
        entities.put("&b.eta;", "&#x03B7;");
        entities.put("&b.thetas;", "&#x03B8;");
        entities.put("&b.Theta;", "&#x0398;");
        entities.put("&b.thetav;", "&#x03D1;");
        entities.put("&b.iota;", "&#x03B9;");
        entities.put("&b.kappa;", "&#x03BA;");
        entities.put("&b.kappav;", "&#x03F0;");
        entities.put("&b.lambda;", "&#x03BB;");
        entities.put("&b.Lambda;", "&#x039B;");
        entities.put("&b.mu;", "&#x03BC;");
        entities.put("&b.nu;", "&#x03BD;");
        entities.put("&b.xi;", "&#x03BE;");
        entities.put("&b.Xi;", "&#x039E;");
        entities.put("&b.pi;", "&#x03C0;");
        entities.put("&b.Pi;", "&#x03A0;");
        entities.put("&b.piv;", "&#x03D6;");
        entities.put("&b.rho;", "&#x03C1;");
        entities.put("&b.rhov;", "&#x03F1;");
        entities.put("&b.sigma;", "&#x03C3;");
        entities.put("&b.Sigma;", "&#x03A3;");
        entities.put("&b.sigmav;", "&#x03C2;");
        entities.put("&b.tau;", "&#x03C4;");
        entities.put("&b.upsi;", "&#x03C5;");
        entities.put("&b.Upsi;", "&#x03D2;");
        entities.put("&b.phis;", "&#x03C6;");
        entities.put("&b.Phi;", "&#x03A6;");
        entities.put("&b.phiv;", "&#x03D5;");
        entities.put("&b.chi;", "&#x03C7;");
        entities.put("&b.psi;", "&#x03C8;");
        entities.put("&b.Psi;", "&#x03A8;");
        entities.put("&b.omega;", "&#x03C9;");
        entities.put("&b.Omega;", "&#x03A9;");
        entities.put("&aacute;", "&#x00E1;");
        entities.put("&Aacute;", "&#x00C1;");
        entities.put("&acirc;", "&#x00E2;");
        entities.put("&Acirc;", "&#x00C2;");
        entities.put("&agrave;", "&#x00E0;");
        entities.put("&Agrave;", "&#x00C0;");
        entities.put("&aring;", "&#x00E5;");
        entities.put("&Aring;", "&#x00C5;");
        entities.put("&atilde;", "&#x00E3;");
        entities.put("&Atilde;", "&#x00C3;");
        entities.put("&auml;", "&#x00E4;");
        entities.put("&Auml;", "&#x00C4;");
        entities.put("&aelig;", "&#x00E6;");
        entities.put("&AElig;", "&#x00C6;");
        entities.put("&ccedil;", "&#x00E7;");
        entities.put("&Ccedil;", "&#x00C7;");
        entities.put("&eth;", "&#x00D0;");
        entities.put("&ETH;", "&#x00F0;");
        entities.put("&eacute;", "&#x00E9;");
        entities.put("&Eacute;", "&#x00C9;");
        entities.put("&ecirc;", "&#x00EA;");
        entities.put("&Ecirc;", "&#x00CA;");
        entities.put("&egrave;", "&#x00E8;");
        entities.put("&Egrave;", "&#x00C8;");
        entities.put("&euml;", "&#x00EB;");
        entities.put("&Euml;", "&#x00CB;");
        entities.put("&iacute;", "&#x00ED;");
        entities.put("&Iacute;", "&#x00CD;");
        entities.put("&icirc;", "&#x00EE;");
        entities.put("&Icirc;", "&#x00CE;");
        entities.put("&igrave;", "&#x00EC;");
        entities.put("&Igrave;", "&#x00CC;");
        entities.put("&iuml;", "&#x00EF;");
        entities.put("&Iuml;", "&#x00CF;");
        entities.put("&ntilde;", "&#x00F1;");
        entities.put("&Ntilde;", "&#x00D1;");
        entities.put("&oacute;", "&#x00F3;");
        entities.put("&Oacute;", "&#x00D3;");
        entities.put("&ocirc;", "&#x00F4;");
        entities.put("&Ocirc;", "&#x00D4;");
        entities.put("&ograve;", "&#x00F2;");
        entities.put("&Ograve;", "&#x00D2;");
        entities.put("&oslash;", "&#x2298;");
        entities.put("&Oslash;", "&#x00D8;");
        entities.put("&otilde;", "&#x00F5;");
        entities.put("&Otilde;", "&#x00D5;");
        entities.put("&ouml;", "&#x00F6;");
        entities.put("&Ouml;", "&#x00D6;");
        entities.put("&szlig;", "&#x00DF;");
        entities.put("&thorn;", "&#x00FE;");
        entities.put("&THORN;", "&#x00DE;");
        entities.put("&uacute;", "&#x00FA;");
        entities.put("&Uacute;", "&#x00DA;");
        entities.put("&ucirc;", "&#x00DB;");
        entities.put("&Ucirc;", "&#x00FB;");
        entities.put("&ugrave;", "&#x00F9;");
        entities.put("&Ugrave;", "&#x00D9;");
        entities.put("&uuml;", "&#x00FC;");
        entities.put("&Uuml;", "&#x00DC;");
        entities.put("&yacute;", "&#x00FD;");
        entities.put("&Yacute;", "&#x00DD;");
        entities.put("&yuml;", "&#x00FF;");
        entities.put("&abreve;", "&#x0103;");
        entities.put("&Abreve;", "&#x0102;");
        entities.put("&amacr;", "&#x0101;");
        entities.put("&Amacr;", "&#x0100;");
        entities.put("&aogon;", "&#x0105;");
        entities.put("&Aogon;", "&#x0104;");
        entities.put("&cacute;", "&#x0107;");
        entities.put("&Cacute;", "&#x0106;");
        entities.put("&ccaron;", "&#x010D;");
        entities.put("&Ccaron;", "&#x010C;");
        entities.put("&ccirc;", "&#x0109;");
        entities.put("&Ccirc;", "&#x0108;");
        entities.put("&cdot;", "&#x22C5;");
        entities.put("&Cdot;", "&#x010A;");
        entities.put("&dcaron;", "&#x010F;");
        entities.put("&Dcaron;", "&#x010E;");
        entities.put("&dstrok;", "&#x0111;");
        entities.put("&Dstrok;", "&#x0110;");
        entities.put("&ecaron;", "&#x011B;");
        entities.put("&Ecaron;", "&#x011A;");
        entities.put("&edot;", "&#x0117;");
        entities.put("&Edot;", "&#x0116;");
        entities.put("&emacr;", "&#x0113;");
        entities.put("&Emacr;", "&#x0112;");
        entities.put("&eogon;", "&#x0119;");
        entities.put("&Eogon;", "&#x0118;");
        entities.put("&gacute;", "&#x01F5;");
        entities.put("&gbreve;", "&#x011F;");
        entities.put("&Gbreve;", "&#x011E;");
        entities.put("&Gcedil;", "&#x0122;");
        entities.put("&gcirc;", "&#x011D;");
        entities.put("&Gcirc;", "&#x011C;");
        entities.put("&gdot;", "&#x0121;");
        entities.put("&Gdot;", "&#x0120;");
        entities.put("&hcirc;", "&#x0125;");
        entities.put("&Hcirc;", "&#x0124;");
        entities.put("&hstrok;", "&#x0127;");
        entities.put("&Hstrok;", "&#x0126;");
        entities.put("&Idot;", "&#x0130;");
        entities.put("&Imacr;", "&#x012A;");
        entities.put("&imacr;", "&#x012B;");
        entities.put("&ijlig;", "&#x0133;");
        entities.put("&IJlig;", "&#x0132;");
        entities.put("&inodot;", "&#x0131;");
        entities.put("&iogon;", "&#x012F;");
        entities.put("&Iogon;", "&#x012E;");
        entities.put("&itilde;", "&#x0129;");
        entities.put("&Itilde;", "&#x0128;");
        entities.put("&jcirc;", "&#x0135;");
        entities.put("&Jcirc;", "&#x0134;");
        entities.put("&kcedil;", "&#x0137;");
        entities.put("&Kcedil;", "&#x0136;");
        entities.put("&kgreen;", "&#x0138;");
        entities.put("&lacute;", "&#x013A;");
        entities.put("&Lacute;", "&#x0139;");
        entities.put("&lcaron;", "&#x013E;");
        entities.put("&Lcaron;", "&#x013D;");
        entities.put("&lcedil;", "&#x013C;");
        entities.put("&Lcedil;", "&#x013B;");
        entities.put("&lmidot;", "&#x0140;");
        entities.put("&Lmidot;", "&#x013F;");
        entities.put("&lstrok;", "&#x0142;");
        entities.put("&Lstrok;", "&#x0141;");
        entities.put("&nacute;", "&#x0144;");
        entities.put("&Nacute;", "&#x0143;");
        entities.put("&eng;", "&#x014B;");
        entities.put("&ENG;", "&#x014A;");
        entities.put("&napos;", "&#x0149;");
        entities.put("&ncaron;", "&#x0148;");
        entities.put("&Ncaron;", "&#x0147;");
        entities.put("&ncedil;", "&#x0146;");
        entities.put("&Ncedil;", "&#x0145;");
        entities.put("&odblac;", "&#x0151;");
        entities.put("&Odblac;", "&#x0150;");
        entities.put("&Omacr;", "&#x014C;");
        entities.put("&omacr;", "&#x014D;");
        entities.put("&oelig;", "&#x0153;");
        entities.put("&OElig;", "&#x0152;");
        entities.put("&racute;", "&#x0155;");
        entities.put("&Racute;", "&#x0154;");
        entities.put("&rcaron;", "&#x0159;");
        entities.put("&Rcaron;", "&#x0158;");
        entities.put("&rcedil;", "&#x0157;");
        entities.put("&Rcedil;", "&#x0156;");
        entities.put("&sacute;", "&#x015B;");
        entities.put("&Sacute;", "&#x015A;");
        entities.put("&scaron;", "&#x0161;");
        entities.put("&Scaron;", "&#x0160;");
        entities.put("&scedil;", "&#x015F;");
        entities.put("&Scedil;", "&#x015E;");
        entities.put("&scirc;", "&#x015D;");
        entities.put("&Scirc;", "&#x015C;");
        entities.put("&tcaron;", "&#x0165;");
        entities.put("&Tcaron;", "&#x0164;");
        entities.put("&tcedil;", "&#x0163;");
        entities.put("&Tcedil;", "&#x0162;");
        entities.put("&tstrok;", "&#x0167;");
        entities.put("&Tstrok;", "&#x0166;");
        entities.put("&ubreve;", "&#x016D;");
        entities.put("&Ubreve;", "&#x016C;");
        entities.put("&udblac;", "&#x0171;");
        entities.put("&Udblac;", "&#x0170;");
        entities.put("&umacr;", "&#x016B;");
        entities.put("&Umacr;", "&#x016A;");
        entities.put("&uogon;", "&#x0173;");
        entities.put("&Uogon;", "&#x0172;");
        entities.put("&uring;", "&#x016F;");
        entities.put("&Uring;", "&#x016E;");
        entities.put("&utilde;", "&#x0169;");
        entities.put("&Utilde;", "&#x0168;");
        entities.put("&wcirc;", "&#x0175;");
        entities.put("&Wcirc;", "&#x0174;");
        entities.put("&ycirc;", "&#x0177;");
        entities.put("&Ycirc;", "&#x0176;");
        entities.put("&Yuml;", "&#x0178;");
        entities.put("&zacute;", "&#x017A;");
        entities.put("&Zacute;", "&#x0179;");
        entities.put("&zcaron;", "&#x017E;");
        entities.put("&Zcaron;", "&#x017D;");
        entities.put("&zdot;", "&#x017C;");
        entities.put("&Zdot;", "&#x017B;");
        entities.put("&aleph;", "&#x2135;");
        entities.put("&and;", "&#x2227;");
        entities.put("&ang90;", "&#x221F;");
        entities.put("&angsph;", "&#x2222;");
        entities.put("&ap;", "&#x2248;");
        entities.put("&becaus;", "&#x2235;");
        entities.put("&bottom;", "&#x22A5;");
        entities.put("&cap;", "&#x2229;");
        entities.put("&cong;", "&#x2245;");
        entities.put("&conint;", "&#x222E;");
        entities.put("&cup;", "&#x222A;");
        entities.put("&equiv;", "&#x2261;");
        entities.put("&exist;", "&#x2203;");
        entities.put("&forall;", "&#x2200;");
        entities.put("&fnof;", "&#x0192;");
        entities.put("&ge;", "&#x2265;");
        entities.put("&iff;", "&#xE365;");
        entities.put("&infin;", "&#x221E;");
        entities.put("&int;", "&#x222B;");
        entities.put("&isin;", "&#x220A;");
        entities.put("&lang;", "&#x3008;");
        entities.put("&lArr;", "&#x21D0;");
        entities.put("&le;", "&#x2264;");
        entities.put("&minus;", "&#x2212;");
        entities.put("&mnplus;", "&#x2213;");
        entities.put("&nabla;", "&#x2207;");
        entities.put("&ne;", "&#x2260;");
        entities.put("&ni;", "&#x220D;");
        entities.put("&or;", "&#x2228;");
        entities.put("&par;", "&#x2225;");
        entities.put("&part;", "&#x2202;");
        entities.put("&permil;", "&#x2030;");
        entities.put("&perp;", "&#x22A5;");
        entities.put("&prime;", "&#x2032;");
        entities.put("&Prime;", "&#x2033;");
        entities.put("&prop;", "&#x221D;");
        entities.put("&radic;", "&#x221A;");
        entities.put("&rang;", "&#x3009;");
        entities.put("&rArr;", "&#x21D2;");
        entities.put("&sim;", "&#x223C;");
        entities.put("&sime;", "&#x2243;");
        entities.put("&square;", "&#x25A1;");
        entities.put("&sub;", "&#x2282;");
        entities.put("&sube;", "&#x2286;");
        entities.put("&sup;", "&#x2283;");
        entities.put("&supe;", "&#x2287;");
        entities.put("&there4;", "&#x2234;");
        entities.put("&Verbar;", "&#x2016;");
        entities.put("&angst;", "&#x212B;");
        entities.put("&bernou;", "&#x212C;");
        entities.put("&compfn;", "&#x2218;");
        entities.put("&Dot;", "&#x0308;");
        entities.put("&DotDot;", "&#x20DC;");
        entities.put("&hamilt;", "&#x210B;");
        entities.put("&lagran;", "&#x2112;");
        entities.put("&lowast;", "&#x2217;");
        entities.put("&notin;", "&#x2209;");
        entities.put("&order;", "&#x2134;");
        entities.put("&phmmat;", "&#x2133;");
        entities.put("&tdot;", "&#x20DB;");
        entities.put("&tprime;", "&#x2034;");
        entities.put("&wedgeq;", "&#x2259;");
        entities.put("&half;", "&#x00BD;");
        entities.put("&frac12;", "&#x00BD;");
        entities.put("&frac14;", "&#x00BC;");
        entities.put("&frac34;", "&#x00BE;");
        entities.put("&frac18;", "&#x215B;");
        entities.put("&frac38;", "&#x215C;");
        entities.put("&frac58;", "&#x215D;");
        entities.put("&frac78;", "&#x215E;");
        entities.put("&sup1;", "&#x00B9;");
        entities.put("&sup2;", "&#x00B2;");
        entities.put("&sup3;", "&#x00B3;");
        entities.put("&plus;", "&#x002B;");
        entities.put("&plusmn;", "&#x00B1;");
        entities.put("&lt;", "&#x003C;");
        entities.put("&equals;", "&#x003D;");
        entities.put("&gt;", "&#x003E;");
        entities.put("&divide;", "&#x00F7;");
        entities.put("&times;", "&#x00D7;");
        entities.put("&curren;", "&#x00A4;");
        entities.put("&pound;", "&#x00A3;");
        entities.put("&dollar;", "&#x0024;");
        entities.put("&cent;", "&#x00A2;");
        entities.put("&yen;", "&#x00A5;");
        entities.put("&num;", "&#x0023;");
        entities.put("&percnt;", "&#x0025;");
        entities.put("&amp;", "&#x0026;");
        entities.put("&ast;", "&#x2217;");
        entities.put("&commat;", "&#x0040;");
        entities.put("&lsqb;", "&#x005B;");
        entities.put("&bsol;", "&#x005C;");
        entities.put("&rsqb;", "&#x005D;");
        entities.put("&lcub;", "&#x007B;");
        entities.put("&horbar;", "&#x2015;");
        entities.put("&verbar;", "&#x007C;");
        entities.put("&rcub;", "&#x007D;");
        entities.put("&micro;", "&#x00B5;");
        entities.put("&ohm;", "&#x2126;");
        entities.put("&deg;", "&#x00B0;");
        entities.put("&ordm;", "&#x00BA;");
        entities.put("&ordf;", "&#x00AA;");
        entities.put("&sect;", "&#x00A7;");
        entities.put("&para;", "&#x00B6;");
        entities.put("&middot;", "&#x00B7;");
        entities.put("&larr;", "&#x2190;");
        entities.put("&rarr;", "&#x2192;");
        entities.put("&uarr;", "&#x2191;");
        entities.put("&darr;", "&#x2193;");
        entities.put("&copy;", "&#x00A9;");
        entities.put("&reg;", "&#x00AE;");
        entities.put("&trade;", "&#x2122;");
        entities.put("&brvbar;", "&#x00A6;");
        entities.put("&not;", "&#x00AC;");
        entities.put("&sung;", "&#x2669;");
        entities.put("&excl;", "&#x0021;");
        entities.put("&iexcl;", "&#x00A1;");
        entities.put("&quot;", "&#x0022;");
        entities.put("&apos;", "&#x0027;");
        entities.put("&lpar;", "&#x0028;");
        entities.put("&rpar;", "&#x0029;");
        entities.put("&comma;", "&#x002C;");
        entities.put("&lowbar;", "&#x005F;");
        entities.put("&hyphen;", "&#xE4F8;");
        entities.put("&period;", "&#x002E;");
        entities.put("&sol;", "&#x002F;");
        entities.put("&colon;", "&#x003A;");
        entities.put("&semi;", "&#x003B;");
        entities.put("&quest;", "&#x003F;");
        entities.put("&iquest;", "&#x00BF;");
        entities.put("&laquo;", "&#x00AB;");
        entities.put("&raquo;", "&#x00BB;");
        entities.put("&lsquo;", "&#x2018;");
        entities.put("&rsquo;", "&#x2019;");
        entities.put("&ldquo;", "&#x201C;");
        entities.put("&rdquo;", "&#x201D;");
        entities.put("&nbsp;", "&#x00A0;");
        entities.put("&shy;", "&#x00AD;");
    }
}
