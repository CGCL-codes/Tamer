package net.entropysoft.transmorph.converters.collections;

public class DefaultStringMapFormatter implements IStringMapFormatter {

    public String format(String[] keys, String[] values) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        for (int i = 0; i < keys.length; i++) {
            if (i != 0) {
                stringBuilder.append(", ");
            }
            stringBuilder.append(keys[i]);
            stringBuilder.append('=');
            stringBuilder.append(values[i]);
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
