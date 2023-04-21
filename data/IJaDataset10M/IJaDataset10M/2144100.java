package ac.jp.u_tokyo.SyncLib.tests;

import static org.junit.Assert.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.Map.Entry;
import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;
import ac.jp.u_tokyo.SyncLib.Mod;
import ac.jp.u_tokyo.SyncLib.NullMod;
import ac.jp.u_tokyo.SyncLib.PrimMod;
import ac.jp.u_tokyo.SyncLib.Sync;
import ac.jp.u_tokyo.SyncLib.SynchronizationFailedException;
import ac.jp.u_tokyo.SyncLib.dictionaries.DictMod;
import ac.jp.u_tokyo.SyncLib.dictionaries.DictModFactory;
import ac.jp.u_tokyo.SyncLib.language.EvaluationFailedException;
import ac.jp.u_tokyo.SyncLib.language3.GlobalIncrementalFactory;
import ac.jp.u_tokyo.SyncLib.language3.LanguageLexer;
import ac.jp.u_tokyo.SyncLib.language3.LanguageParser;
import ac.jp.u_tokyo.SyncLib.language.Program;
import ac.jp.u_tokyo.SyncLib.modParser.ModParser;
import ac.jp.u_tokyo.SyncLib.util.Helper;
import ac.jp.u_tokyo.SyncLib.util.StringHelper;
import ac.jp.u_tokyo.SyncLib.valueParser.ValueParser;

public abstract class InputOutputTestBase extends SyncTestBase {

    protected abstract String getTestFilePath();

    protected abstract Sync getSync();

    @Test
    public void test() throws IOException, RecognitionException, SynchronizationFailedException {
        _keyMap = new KeyMap();
        InputStream is = new FileInputStream(getTestFilePath());
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line;
        int lineNumber = 0;
        Sync s = getSync();
        Object[] values = new Object[s.getParaCount()];
        Mod[] mod;
        Mod[] result = null;
        Object[] newValues = null;
        while (true) {
            lineNumber++;
            line = reader.readLine();
            if (line == null) return;
            if (line.startsWith("sync")) {
                mod = ModParser.parse(getRightPart(line));
                mod = values2mods((Object[]) convertValue(mods2values(mod)));
                if (line.startsWith("sync fail")) {
                    try {
                        checkedSynchronize(s, mod);
                        fail();
                    } catch (SynchronizationFailedException e) {
                    }
                } else {
                    try {
                        result = checkedSynchronize(s, mod);
                    } catch (SynchronizationFailedException e) {
                        SynchronizationFailedException err = new SynchronizationFailedException("line:" + lineNumber);
                        err.setStackTrace(e.getStackTrace());
                        throw err;
                    }
                    Helper.applyMods(values, result);
                }
            } else if (line.startsWith("resync")) {
                newValues = ValueParser.parse(getRightPart(line).split(";")[0]);
                newValues = (Object[]) convertValue(newValues);
                mod = ModParser.parse(getRightPart(line).split(";")[1]);
                mod = values2mods((Object[]) convertValue(mods2values(mod)));
                if (line.startsWith("resync fail")) {
                    try {
                        checkedResynchronize(s, newValues, mod);
                        fail("Ssynchronization succeeded but should have failed. line:" + lineNumber);
                    } catch (SynchronizationFailedException e) {
                    }
                } else if (line.startsWith("resync")) {
                    try {
                        result = checkedResynchronize(s, newValues, mod);
                    } catch (SynchronizationFailedException e) {
                        throw new SynchronizationFailedException("line:" + lineNumber);
                    }
                    values = newValues;
                    Helper.applyMods(values, result);
                }
            } else if (line.startsWith("values")) {
                assertWildcardEqual("line:" + lineNumber, ValueParser.parse(getRightPart(line)), values);
            } else if (line.startsWith("output")) {
                assertWildcardEqual("line:" + lineNumber, mods2values(ModParser.parse(getRightPart(line))), mods2values(result));
            }
        }
    }

    private void assertWildcardEqual(String lineMsg, Object a1, Object a2) {
        String result = isWildCardEqual(a1, a2, _keyMap);
        if (result != null) {
            fail(lineMsg + ". " + result);
        }
    }

    private static final String[] NEW_KEY_PREFIX = new String[] { "__new", "*" };

    private KeyMap _keyMap = new KeyMap();

    private static class KeyMap {

        private Map<String, Object> _keyMap = new HashMap<String, Object>();

        private Stack<String> _matchedKeys = new Stack<String>();

        public void push(String wildcard, Object value) {
            _keyMap.put(wildcard, value);
            _matchedKeys.push(wildcard);
        }

        public boolean contains(String wildcard) {
            return _keyMap.containsKey(wildcard);
        }

        public Object get(String wildcard) {
            return _keyMap.get(wildcard);
        }

        public int nextIndex() {
            return _matchedKeys.size();
        }

        public void pop(int startIndex) {
            int size = _matchedKeys.size() - startIndex;
            for (int i = 0; i < size; i++) {
                _keyMap.remove(_matchedKeys.pop());
            }
        }
    }

    private Object[] mods2values(Mod[] m) {
        Object[] result = new Object[m.length];
        for (int i = 0; i < m.length; i++) {
            result[i] = mod2value(m[i]);
        }
        return result;
    }

    private Object mod2value(Mod m) {
        if (m instanceof PrimMod) {
            return ((PrimMod) m).getNewValue();
        }
        if (m instanceof DictMod) {
            DictMod dictMod = (DictMod) m;
            Map<Object, Object> result = new HashMap<Object, Object>();
            for (Object key : dictMod.getKeys()) {
                result.put(key, mod2value(dictMod.getMod(key)));
            }
            return result;
        }
        return m;
    }

    private Mod[] values2mods(Object[] v) {
        Mod[] result = new Mod[v.length];
        for (int i = 0; i < v.length; i++) {
            result[i] = value2mod(v[i]);
        }
        return result;
    }

    private Mod value2mod(Object v) {
        if (v instanceof NullMod) {
            return (NullMod) v;
        } else if (v instanceof Map) {
            DictModFactory factory = new DictModFactory();
            for (Entry entry : ((Map<Object, Object>) v).entrySet()) {
                factory.addPut(entry.getKey(), value2mod(entry.getValue()));
            }
            return factory.create();
        } else return new PrimMod(v);
    }

    private Object convertValue(Object value) {
        if (value instanceof Object[]) {
            Object[] values = (Object[]) value;
            Object[] result = new Object[values.length];
            for (int i = 0; i < result.length; i++) {
                result[i] = convertValue(values[i]);
            }
            return result;
        } else if (value instanceof Map) {
            Map<Object, Object> map = (Map) value;
            Map<Object, Object> result = new HashMap<Object, Object>();
            for (Entry entry : map.entrySet()) {
                result.put(convertValue(entry.getKey()), convertValue(entry.getValue()));
            }
            return result;
        } else if (isWildCard(value)) {
            if (!_keyMap.contains((String) value)) {
                _keyMap.push((String) value, GlobalIncrementalFactory.staticCreateNewKey());
            }
            return _keyMap.get((String) value);
        } else return value;
    }

    private String isWildCardEqual(Object pattern, Object value, KeyMap keyMap) {
        if (pattern instanceof Object[]) {
            Object[] patternArray = ((Object[]) pattern);
            if (false == value instanceof Object[]) {
                return ("type is different. expected: " + pattern.getClass().getName() + " actual: " + value.getClass().getName());
            }
            Object[] valueArray = ((Object[]) value);
            if (valueArray.length != patternArray.length) {
                return ("Array length is different. expected: " + Arrays.toString(patternArray) + " actual: " + Arrays.toString(valueArray));
            }
            for (int i = 0; i < patternArray.length; i++) {
                String result = isWildCardEqual(patternArray[i], valueArray[i], keyMap);
                if (result != null) return ("different at index " + i + ". expected: " + Arrays.toString(patternArray) + " actual: " + Arrays.toString(valueArray) + ". inner differnce: " + result);
            }
            return null;
        } else if (pattern instanceof Map) {
            Map patternMap = (Map) pattern;
            if (false == value instanceof Map) {
                return ("type is different. expected: " + getTypeName(pattern) + " actual: " + getTypeName(value));
            }
            Map valueMap = (Map) value;
            Map<Object, Object> unmatchedPatternMap = new HashMap<Object, Object>(), unmatchedValueMap = new HashMap(valueMap);
            for (Object k : patternMap.keySet()) {
                Object valueK = k;
                if (isWildCard(k)) {
                    if (_keyMap.contains((String) k)) valueK = _keyMap.get((String) k); else {
                        unmatchedPatternMap.put(k, patternMap.get(k));
                        continue;
                    }
                }
                if (valueMap.containsKey(valueK)) {
                    String result = isWildCardEqual(patternMap.get(k), valueMap.get(valueK), keyMap);
                    if (result != null) return ("different at " + k + ". expected: " + pattern + " actual: " + value + ". inner difference: " + result);
                    unmatchedValueMap.remove(valueK);
                } else return ("No correspondance found at " + k + ". expected: " + pattern + " actual: " + value);
            }
            if (unmatchedValueMap.size() != unmatchedPatternMap.size()) {
                return "size not equal. expected: " + pattern + " actual: " + value;
            }
            return isWildCardOnlyEqual(unmatchedPatternMap, unmatchedValueMap, keyMap);
        } else if (isWildCard(pattern)) {
            if (keyMap.contains((String) pattern)) {
                pattern = keyMap.get((String) pattern);
            } else {
                keyMap.push((String) pattern, value);
                return null;
            }
        }
        if (Helper.areEqual(pattern, value)) return null; else return "different. expected: " + pattern + " actual: " + value;
    }

    private boolean isWildCard(Object k) {
        if (false == k instanceof String) return false;
        for (String s : NEW_KEY_PREFIX) {
            if (((String) k).startsWith(s)) return true;
        }
        return false;
    }

    private String isWildCardOnlyEqual(Map<Object, Object> unmatchedPattern, Map<Object, Object> unmatchedValue, KeyMap keyMap) {
        if (unmatchedPattern.size() == 0) {
            assert unmatchedValue.size() == 0;
            return null;
        }
        Entry patternEntry = unmatchedPattern.entrySet().iterator().next();
        assert patternEntry.getKey() instanceof String && !_keyMap.contains((String) patternEntry.getKey());
        for (Entry valueEntry : unmatchedValue.entrySet()) {
            int index = keyMap.nextIndex();
            if (isWildCardEqual(patternEntry.getValue(), valueEntry.getValue(), keyMap) == null) {
                _keyMap.push((String) patternEntry.getKey(), valueEntry.getKey());
                Map<Object, Object> restPattern = new HashMap<Object, Object>(unmatchedPattern);
                restPattern.remove(patternEntry.getKey());
                Map<Object, Object> restValue = new HashMap<Object, Object>(unmatchedValue);
                restValue.remove(valueEntry.getKey());
                if (isWildCardEqual(restPattern, restValue, keyMap) == null) return null;
            }
            keyMap.pop(index);
        }
        return "different. expected: " + unmatchedPattern + " actual: " + unmatchedValue;
    }

    private static String getTypeName(Object a1) {
        String type;
        if (a1 == null) type = "null"; else type = a1.getClass().getName();
        return type;
    }

    private static String getRightPart(String line) {
        return line.substring(line.indexOf(':') + 1).trim();
    }

    public static void main(String[] args) {
        for (String file : args[0].split(File.pathSeparator)) {
            try {
                InputStream is = new FileInputStream(file);
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String line = reader.readLine();
                ANTLRStringStream input;
                String fileName;
                String sourceName;
                if (line.matches("file[ \t]*\\:.*")) {
                    fileName = getRightPart(line);
                    sourceName = getRightPart(reader.readLine());
                    input = new ANTLRFileStream(new File(fileName).getAbsolutePath());
                } else {
                    StringBuilder sb = new StringBuilder();
                    do {
                        sb.append(line);
                        line = reader.readLine();
                    } while (line != null && !line.matches("source[ \t]*\\:.*"));
                    fileName = file;
                    sourceName = getRightPart(line);
                    assert sourceName != null;
                    input = new ANTLRStringStream(sb.toString());
                }
                LanguageLexer lexer = new LanguageLexer(input);
                lexer.setBasePath(new File(fileName).getParent());
                LanguageParser parser = new LanguageParser(new CommonTokenStream(lexer));
                Program p = parser.prog().compile();
                FileWriter fileWriter = new FileWriter(sourceName, false);
                try {
                    String name = new File(sourceName).getName();
                    name = name.substring(0, name.indexOf('.'));
                    fileWriter.append(String.format(Header, name, StringHelper.escapeJavaStyleString(file, true)));
                    p.write(fileWriter);
                    fileWriter.append(Footer);
                } finally {
                    fileWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (RecognitionException e) {
                e.printStackTrace();
            } catch (EvaluationFailedException e) {
                e.printStackTrace();
            }
        }
    }

    static final String Header = "package ac.jp.u_tokyo.SyncLib.tests.generated; " + "import ac.jp.u_tokyo.SyncLib.tests.InputOutputTestBase;" + "public class %1s extends InputOutputTestBase {" + "protected String getTestFilePath(){" + "return \"%2s\";" + "}" + "protected ac.jp.u_tokyo.SyncLib.Sync getSync() {" + "return getmainFactory().create();" + "}";

    static final String Footer = "}";
}
