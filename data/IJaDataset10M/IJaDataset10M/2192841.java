package mojasi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import mojasi.utils.FastClearHashMap;

public class MojasiParser {

    static Logger logger = Logger.getLogger(MojasiParser.class);

    public static final int MaxParseLevel = 32;

    private FastClearHashMap map;

    private int level = 0;

    public static final MojasiToken nullKv = new MojasiToken("n");

    public static final MojasiToken stringKv = new MojasiToken("S");

    public MojasiToken currentToken;

    public Object global;

    public Object socket;

    public MojasiParser() {
    }

    public static Object bytes2Object(byte[] bytes) {
        MojasiParser p = new MojasiParser();
        Object gg;
        try {
            gg = p.parseObject(new MojasiToken(bytes, 0));
        } catch (MojasiException e) {
            e.printStackTrace();
            return null;
        }
        return gg;
    }

    /**
     * Up to this point the MojasiToken's inside the value of kv have not even been looked at. One wouldn't even know
     * that the value of kv <i>was</i> tokens, but a class that knows this can call loadDictionary and have the tokens
     * rudely scanned and stuffed up into a fast clear hash map for later examination, if needed.
     * 
     * @param kv
     * @throws MojasiException
     */
    public void loadDictionary(MojasiToken kv) throws MojasiException {
        map = getMap(level);
        map.clear();
        int end = kv.end - 1;
        int i = kv.val + 2;
        while (i < end) {
            MojasiToken tmp = new MojasiToken(kv.chars, i);
            i = tmp.end;
            map.put(tmp);
        }
    }

    public Object parseObject(MojasiFactory f, MojasiToken kv) throws MojasiException {
        if (level > MaxParseLevel) {
            throw new MojasiException("Parser recursed too deep.");
        }
        Object o = null;
        level++;
        currentToken = kv;
        o = f.make(this, kv);
        o = f.read(this, kv, o);
        level--;
        map = getMap(level);
        return o;
    }

    public Object parseObject(MojasiToken kv) throws MojasiException {
        if (kv.val + 1 >= kv.end) return null;
        MojasiFactory f = ClassMappings.instance.name2factory(kv);
        if (f == null) {
            if (kv.cla == kv.key) {
                f = ClassMappings.instance.name2factory(stringKv);
            } else throw new MojasiException("Missing factory for class alias: " + kv.classString());
        }
        return parseObject(f, kv);
    }

    public final MojasiToken find(HashedByteWord key) {
        return map.get(key);
    }

    private static FastClearHashMap getMap(int level) {
        if (level > 64) logger.error("recursed too deep in parsing");
        List<FastClearHashMap> maps = maplist.get();
        while (level >= maps.size()) maps.add(new FastClearHashMap());
        return maps.get(level);
    }

    private static ThreadLocal<List<FastClearHashMap>> maplist = new ThreadLocal<List<FastClearHashMap>>() {

        protected synchronized List<FastClearHashMap> initialValue() {
            List<FastClearHashMap> tmp = new ArrayList<FastClearHashMap>();
            return tmp;
        }
    };

    public String parseString(MojasiToken kv) {
        if (kv.classhash == nullKv.classhash) {
            if (kv.equals(nullKv)) return null;
        }
        return kv.valueString();
    }

    public byte[] parseByteArray(MojasiToken kv) {
        if (kv.classhash == nullKv.classhash) {
            if (kv.equals(nullKv)) return null;
        }
        byte[] bytes = new byte[kv.end - kv.val - 1];
        System.arraycopy(kv.chars, kv.val + 1, bytes, 0, bytes.length);
        return bytes;
    }

    public int parseInt(MojasiToken kv) {
        return kv.decimal_intValue();
    }

    public long parseLong(MojasiToken kv) {
        return kv.decimal_longValue();
    }

    public byte parseByte(MojasiToken kv) {
        return (byte) kv.decimal_intValue();
    }

    public char parseChar(MojasiToken kv) {
        return (char) kv.decimal_intValue();
    }

    public short parseShort(MojasiToken kv) {
        return (short) kv.decimal_intValue();
    }

    public boolean parseBool(MojasiToken kv) {
        if (kv.chars[kv.val + 1] == 'T') return true;
        return false;
    }

    public double parseDouble(MojasiToken kv) {
        double val = Double.parseDouble(kv.valueString());
        return val;
    }

    public float parseFloat(MojasiToken kv) {
        float val = Float.parseFloat(kv.valueString());
        return val;
    }

    static ThreadLocal<SimpleDateFormat> threadLocalFormatter = new ThreadLocal<SimpleDateFormat>() {

        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S Z");
        }
    };

    public Date parseDate(MojasiToken kv) throws MojasiException {
        SimpleDateFormat formatter = threadLocalFormatter.get();
        if (kv.classhash == nullKv.classhash) {
            if (kv.equals(nullKv)) return null;
        }
        String input = kv.valueString();
        Date date = null;
        try {
            date = formatter.parse(input);
        } catch (ParseException e) {
        }
        return date;
    }

    public int[] XXXparseIntArray(MojasiToken kv) throws MojasiException {
        if (kv.classhash == nullKv.classhash) {
            if (kv.equals(nullKv)) return null;
        }
        List<Integer> list = new ArrayList<Integer>();
        byte[] chars = kv.chars;
        int pos = kv.val;
        if (chars[pos++] != ':') throw new MojasiException("Missing : in value");
        if (chars[pos++] != '[') throw new MojasiException("Missing { in value");
        while (chars[pos] != ']') {
            MojasiToken tmp = new MojasiToken(chars, pos);
            pos = tmp.end;
            map.put(tmp);
            if (pos >= kv.end) throw new MojasiException("Ran off the end while parsing");
        }
        pos++;
        int[] arr = new int[list.size()];
        pos = 0;
        for (Integer i : list) {
            arr[pos++] = i.intValue();
        }
        return arr;
    }
}
