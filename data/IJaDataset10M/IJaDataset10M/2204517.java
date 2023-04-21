package syntelos.dom.io;

import alto.io.u.Chbuf;

/**
 * Doubly indexed character entities for reading and writing, encoding
 * and decoding.
 * 
 * @author jdp
 */
public final class NamedCharacterEntities extends alto.io.u.Objmap {

    public static final NamedCharacterEntities Instance = new NamedCharacterEntities();

    /**
     * Permits the use of one Objmap with mixed indeces
     */
    public static final class NamedCharacterEntity extends Object {

        public final String name, string;

        public final char value;

        public NamedCharacterEntity(String name, char value) {
            super();
            if (null != name) {
                this.name = name;
                this.value = value;
                this.string = '&' + this.name + ';';
            } else throw new IllegalArgumentException();
        }

        public String getName() {
            return this.name;
        }

        public char getValue() {
            return this.value;
        }

        public int hashCode() {
            return this.string.hashCode();
        }

        public String toString() {
            return this.string;
        }

        public boolean equals(Object ano) {
            if (this == ano) return true; else if (null == ano) return false; else return (this.toString().equals(ano.toString()));
        }
    }

    private NamedCharacterEntities() {
        super(256);
        this.longGetCreateIndex(1);
        this.add("amp", ('&'));
        this.add("lt", ('<'));
        this.add("gt", ('>'));
        this.add("quot", ('"'));
        this.add("nbsp", ((char) 160));
        this.add("lsquo", ('‘'));
        this.add("rsquo", ('’'));
        this.add("frasl", ((char) 47));
        this.add("ndash", ((char) 8211));
        this.add("mdash", ((char) 8212));
        this.add("iexcl", ((char) 161));
        this.add("cent", ((char) 162));
        this.add("pound", ((char) 163));
        this.add("curren", ((char) 164));
        this.add("yen", ((char) 165));
        this.add("brvbar", ((char) 166));
        this.add("brkbar", ((char) 166));
        this.add("sect", ((char) 167));
        this.add("uml", ((char) 168));
        this.add("die", ((char) 168));
        this.add("copy", ((char) 169));
        this.add("ordf", ((char) 170));
        this.add("laquo", ((char) 171));
        this.add("not", ((char) 172));
        this.add("shy", ((char) 173));
        this.add("reg", ((char) 174));
        this.add("macr", ((char) 175));
        this.add("hibar", ((char) 175));
        this.add("deg", ((char) 176));
        this.add("plusmn", ((char) 177));
        this.add("sup2", ((char) 178));
        this.add("sup3", ((char) 179));
        this.add("acute", ((char) 180));
        this.add("micro", ((char) 181));
        this.add("para", ((char) 182));
        this.add("middot", ((char) 183));
        this.add("cedil", ((char) 184));
        this.add("sup1", ((char) 185));
        this.add("ordm", ((char) 186));
        this.add("raquo", ((char) 187));
        this.add("frac14", ((char) 188));
        this.add("frac12", ((char) 189));
        this.add("frac34", ((char) 190));
        this.add("iquest", ((char) 191));
        this.add("Agrave", ((char) 192));
        this.add("Aacute", ((char) 193));
        this.add("Acirc", ((char) 194));
        this.add("Atilde", ((char) 195));
        this.add("Auml", ((char) 196));
        this.add("Aring", ((char) 197));
        this.add("AElig", ((char) 198));
        this.add("Ccedil", ((char) 199));
        this.add("Egrave", ((char) 200));
        this.add("Eacute", ((char) 201));
        this.add("Ecirc", ((char) 202));
        this.add("Euml", ((char) 203));
        this.add("Igrave", ((char) 204));
        this.add("Iacute", ((char) 205));
        this.add("Icirc", ((char) 206));
        this.add("Iuml", ((char) 207));
        this.add("ETH", ((char) 208));
        this.add("Ntilde", ((char) 209));
        this.add("Ograve", ((char) 210));
        this.add("Oacute", ((char) 211));
        this.add("Ocirc", ((char) 212));
        this.add("Otilde", ((char) 213));
        this.add("Ouml", ((char) 214));
        this.add("times", ((char) 215));
        this.add("Oslash", ((char) 216));
        this.add("Ugrave", ((char) 217));
        this.add("Uacute", ((char) 218));
        this.add("Ucirc", ((char) 219));
        this.add("Uuml", ((char) 220));
        this.add("Yacute", ((char) 221));
        this.add("THORN", ((char) 222));
        this.add("szlig", ((char) 223));
        this.add("agrave", ((char) 224));
        this.add("aacute", ((char) 225));
        this.add("acirc", ((char) 226));
        this.add("atilde", ((char) 227));
        this.add("auml", ((char) 228));
        this.add("aring", ((char) 229));
        this.add("aelig", ((char) 230));
        this.add("ccedil", ((char) 231));
        this.add("egrave", ((char) 232));
        this.add("eacute", ((char) 233));
        this.add("ecirc", ((char) 234));
        this.add("euml", ((char) 235));
        this.add("igrave", ((char) 236));
        this.add("iacute", ((char) 237));
        this.add("icirc", ((char) 238));
        this.add("iuml", ((char) 239));
        this.add("eth", ((char) 240));
        this.add("ntilde", ((char) 241));
        this.add("ograve", ((char) 242));
        this.add("oacute", ((char) 243));
        this.add("ocirc", ((char) 244));
        this.add("otilde", ((char) 245));
        this.add("ouml", ((char) 246));
        this.add("divide", ((char) 247));
        this.add("oslash", ((char) 248));
        this.add("ugrave", ((char) 249));
        this.add("uacute", ((char) 250));
        this.add("ucirc", ((char) 251));
        this.add("uuml", ((char) 252));
        this.add("yacute", ((char) 253));
        this.add("thorn", ((char) 254));
        this.add("yuml", ((char) 255));
        this.add("Alpha", ((char) 913));
        this.add("Beta", ((char) 914));
        this.add("Gamma", ((char) 915));
        this.add("Delta", ((char) 916));
        this.add("Epsilon", ((char) 917));
        this.add("Zeta", ((char) 918));
        this.add("Eta", ((char) 919));
        this.add("Theta", ((char) 920));
        this.add("Iota", ((char) 921));
        this.add("Kappa", ((char) 922));
        this.add("Lambda", ((char) 923));
        this.add("Mu", ((char) 924));
        this.add("Nu", ((char) 925));
        this.add("Xi", ((char) 926));
        this.add("Omicron", ((char) 927));
        this.add("Pi", ((char) 928));
        this.add("Rho", ((char) 929));
        this.add("Sigma", ((char) 930));
        this.add("Sigmaf", ((char) 931));
        this.add("Tau", ((char) 932));
        this.add("Upsilon", ((char) 933));
        this.add("Phi", ((char) 934));
        this.add("Chi", ((char) 935));
        this.add("Psi", ((char) 936));
        this.add("Omega", ((char) 937));
        this.add("alpha", ((char) 945));
        this.add("beta", ((char) 946));
        this.add("gamma", ((char) 947));
        this.add("delta", ((char) 948));
        this.add("epsilon", ((char) 949));
        this.add("zeta", ((char) 950));
        this.add("eta", ((char) 951));
        this.add("theta", ((char) 952));
        this.add("iota", ((char) 953));
        this.add("kappa", ((char) 954));
        this.add("lambda", ((char) 955));
        this.add("mu", ((char) 956));
        this.add("nu", ((char) 957));
        this.add("xi", ((char) 958));
        this.add("omicron", ((char) 959));
        this.add("pi", ((char) 960));
        this.add("rho", ((char) 961));
        this.add("sigma", ((char) 962));
        this.add("sigmaf", ((char) 963));
        this.add("tau", ((char) 964));
        this.add("upsilon", ((char) 965));
        this.add("phi", ((char) 966));
        this.add("chi", ((char) 967));
        this.add("psi", ((char) 968));
        this.add("omega", ((char) 969));
        this.add("thetasym", ((char) 977));
        this.add("upsih", ((char) 978));
        this.add("piv", ((char) 982));
        this.add("forall", ((char) 8704));
        this.add("part", ((char) 8706));
        this.add("exist", ((char) 8707));
        this.add("empty", ((char) 8709));
        this.add("nabla", ((char) 8711));
        this.add("isin", ((char) 8712));
        this.add("notin", ((char) 8713));
        this.add("ni", ((char) 8715));
        this.add("prod", ((char) 8719));
        this.add("sum", ((char) 8721));
        this.add("minus", ((char) 8722));
        this.add("lowast", ((char) 8727));
        this.add("radic", ((char) 8730));
        this.add("prop", ((char) 8733));
        this.add("infin", ((char) 8734));
        this.add("ang", ((char) 8736));
        this.add("and", ((char) 8743));
        this.add("or", ((char) 8744));
        this.add("cap", ((char) 8745));
        this.add("cup", ((char) 8746));
        this.add("int", ((char) 8747));
        this.add("there4", ((char) 8756));
        this.add("sim", ((char) 8764));
        this.add("cong", ((char) 8773));
        this.add("asymp", ((char) 8776));
        this.add("ne", ((char) 8800));
        this.add("equiv", ((char) 8801));
        this.add("le", ((char) 8804));
        this.add("ge", ((char) 8805));
        this.add("sub", ((char) 8834));
        this.add("sup", ((char) 8835));
        this.add("nsub", ((char) 8836));
        this.add("sube", ((char) 8838));
        this.add("supe", ((char) 8839));
        this.add("oplus", ((char) 8853));
        this.add("otimes", ((char) 8855));
        this.add("perp", ((char) 8869));
        this.add("sdot", ((char) 8901));
        this.add("loz", ((char) 9674));
        this.add("lceil", ((char) 8968));
        this.add("rceil", ((char) 8969));
        this.add("lfloor", ((char) 8970));
        this.add("rfloor", ((char) 8971));
        this.add("lang", ((char) 9001));
        this.add("rang", ((char) 9002));
        this.add("larr", ((char) 8592));
        this.add("uarr", ((char) 8593));
        this.add("rarr", ((char) 8594));
        this.add("darr", ((char) 8595));
        this.add("harr", ((char) 8596));
        this.add("crarr", ((char) 8629));
        this.add("lArr", ((char) 8656));
        this.add("uArr", ((char) 8657));
        this.add("rArr", ((char) 8658));
        this.add("dArr", ((char) 8659));
        this.add("hArr", ((char) 8960));
        this.add("bull", ((char) 8226));
        this.add("prime", ((char) 8242));
        this.add("Prime", ((char) 8243));
        this.add("oline", ((char) 8254));
        this.add("weierp", ((char) 8472));
        this.add("image", ((char) 8465));
        this.add("real", ((char) 8476));
        this.add("trade", ((char) 8482));
        this.add("euro", ((char) 8364));
        this.add("alefsym", ((char) 8501));
        this.add("spades", ((char) 9824));
        this.add("clubs", ((char) 9827));
        this.add("hearts", ((char) 9829));
        this.add("diams", ((char) 9830));
        this.add("OElig", ((char) 338));
        this.add("oelig", ((char) 339));
        this.add("Scaron", ((char) 352));
        this.add("scaron", ((char) 353));
        this.add("fnof", ((char) 402));
        this.add("ensp", ((char) 8194));
        this.add("emsp", ((char) 8195));
        this.add("thinsp", ((char) 8201));
        this.add("zwnj", ((char) 8204));
        this.add("zwj", ((char) 8205));
        this.add("lrm", ((char) 8206));
        this.add("rlm", ((char) 8207));
        this.add("sbquo", ((char) 8218));
        this.add("ldquo", ((char) 8220));
        this.add("rdquo", ((char) 8221));
        this.add("bdquo", ((char) 8222));
        this.add("dagger", ((char) 8224));
        this.add("Dagger", ((char) 8225));
        this.add("hellip", ((char) 8230));
        this.add("permil", ((char) 8240));
        this.add("lsaquo", ((char) 8249));
        this.add("rsaquo", ((char) 8250));
        this.add("circ", ((char) 710));
        this.add("tilde", ((char) 732));
    }

    public void add(String name, char value) {
        NamedCharacterEntity ent = new NamedCharacterEntity(name, value);
        int idx = this.put2(0, name, ent);
        this.longPutKey(1, idx, value);
    }

    public NamedCharacterEntity getEntityForName(String name) {
        NamedCharacterEntity ent = (NamedCharacterEntity) this.get(name);
        if (null != ent) return ent; else {
            String namelc = name.toLowerCase();
            if (namelc == name) return null; else {
                return (NamedCharacterEntity) this.get(name);
            }
        }
    }

    public NamedCharacterEntity getEntityForValue(char value) {
        int idx = this.longIndexOfKey(1, value);
        return (NamedCharacterEntity) this.objectGetValue(0, idx);
    }

    public boolean hasName(String name) {
        return (null != this.get(name));
    }

    public char getCharacterForName(String name) {
        NamedCharacterEntity ent = this.getEntityForName(name);
        if (null != ent) return ent.value; else return 0;
    }

    public int getCharacterForName(String name, int defv) {
        NamedCharacterEntity ent = this.getEntityForName(name);
        if (null != ent) return ent.value; else return defv;
    }

    public boolean hasCharacter(char value) {
        return (-1 < this.longIndexOfKey(1, value));
    }

    public String getNameForCharacter(char value) {
        NamedCharacterEntity ent = this.getEntityForValue(value);
        if (null != ent) return ent.name; else return null;
    }

    /**
     * Convert characters to entities
     */
    public String encode(String string) {
        if (null == string) return null; else {
            Chbuf strbuf = new Chbuf();
            char[] cary = string.toCharArray();
            char ch;
            for (int cc = 0, count = cary.length; cc < count; cc++) {
                ch = cary[cc];
                NamedCharacterEntity entity = this.getEntityForValue(ch);
                if (null != entity) strbuf.append(entity.string); else strbuf.append(ch);
            }
            return strbuf.toString();
        }
    }

    /**
     * Convert entities to characters
     */
    public String decode(String string) {
        if (null == string) return null; else {
            Chbuf strbuf = new Chbuf();
            char[] cary = string.toCharArray();
            char ch;
            scanl: for (int cc = 0, count = cary.length; cc < count; cc++) {
                ch = cary[cc];
                if ('&' == ch) {
                    cc += 1;
                    int start = (cc);
                    int end = -1;
                    for (; cc < count; cc++) {
                        ch = cary[cc];
                        if (';' == ch) {
                            end = cc;
                            break;
                        }
                    }
                    if (cc < end) {
                        String name = new String(cary, start, end);
                        NamedCharacterEntity entity = this.getEntityForName(name);
                        if (null != entity) {
                            strbuf.append(entity.value);
                            continue scanl;
                        }
                    }
                }
                strbuf.append(ch);
            }
            return strbuf.toString();
        }
    }
}
