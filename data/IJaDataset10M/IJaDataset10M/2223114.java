package dryven.model.binding.form;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import dryven.request.http.HttpUtils;
import dryven.util.crypt.SHA1Hash;
import dryven.util.crypt.SHA256Hash;

public class FormSignature {

    private Map<Character, Collection<String>> _values;

    public static final char ModelType = 'M';

    public static final char FieldSetType = 'F';

    public static final char ErrorActionType = 'E';

    public FormSignature() {
        super();
        _values = new HashMap<Character, Collection<String>>();
    }

    public void addValue(char name, String value) {
        Collection<String> valuesForType = _values.get(name);
        if (valuesForType == null) {
            valuesForType = new LinkedList<String>();
            _values.put(name, valuesForType);
        }
        valuesForType.add(value);
    }

    public void read(String signature, String signSecret) {
        String partsAndHash[] = signature.split("/");
        if (partsAndHash.length != 2) {
            throw new RuntimeException("Invalid signature, too many or too little slashes");
        }
        String partsString = partsAndHash[0];
        String signatureHash = partsAndHash[1];
        String expectedSignatureHash = new SHA256Hash(partsString + signSecret).hash();
        if (!expectedSignatureHash.equals(signatureHash)) {
            throw new RuntimeException("data signatures don't match");
        }
        String parts[] = partsAndHash[0].split(",");
        for (String part : parts) {
            if (part.length() < 3 || part.charAt(1) != ':') {
                throw new RuntimeException("Invalid signature part syntax: " + part);
            }
            char name = part.charAt(0);
            String value = HttpUtils.URLDecode(part.substring(2));
            addValue(name, value);
        }
    }

    public String write(String signSecret) {
        StringBuilder buffer = new StringBuilder();
        for (Map.Entry<Character, Collection<String>> entry : _values.entrySet()) {
            Character name = entry.getKey();
            for (String value : entry.getValue()) {
                buffer.append(name.charValue());
                buffer.append(':');
                buffer.append(encodeValueString(value));
                buffer.append(',');
            }
        }
        if (buffer.length() != 0) {
            buffer.setLength(buffer.length() - 1);
        }
        String value = buffer.toString();
        buffer.append('/');
        buffer.append(new SHA256Hash(value + signSecret).hash());
        return buffer.toString();
    }

    public String encodeValueString(String value) {
        char escapedChars[] = { '%', ',', ':', '/' };
        for (char c : escapedChars) {
            value = value.replace(Character.toString(c), "%" + Integer.toHexString(c).toUpperCase());
        }
        return value;
    }

    public Iterable<String> getValuesForType(char name) {
        return _values.get(name);
    }

    public String getFirstValueForType(char name) {
        Collection<String> values = _values.get(name);
        if (values == null) {
            return null;
        }
        Iterator<String> it = values.iterator();
        if (it.hasNext()) {
            return it.next();
        }
        return null;
    }
}
