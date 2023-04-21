package ftc;

import java.util.*;

/**
 *
 * @author Colin
 */
public class Pattern implements TuneItem {

    String m_pattern;

    int m_repeats;

    ArrayList<TuneItem> m_items;

    /** Creates a new instance of Pattern */
    public Pattern(String pattern, int repeats) {
        m_pattern = pattern;
        m_repeats = repeats;
        m_items = new ArrayList<TuneItem>();
    }

    public void AddItem(TuneItem item) {
        m_items.add(item);
    }

    public int GetItemCount() {
        return m_items.size();
    }

    public void encode(BitStream stream) {
        stream.Append("000");
        stream.Append(encodePattern(m_pattern));
        stream.Append(FormatTune.encodeInt(m_repeats, 4));
        int len = m_items.size();
        stream.Append(FormatTune.encodeInt(len, 8));
        for (int index = 0; index < len; ++index) {
            m_items.get(index).encode(stream);
        }
    }

    String encodePattern(String patternText) {
        if (Global.lookup.patternEncodings.containsKey(patternText)) {
            return Global.lookup.patternEncodings.get(patternText);
        }
        return "???";
    }
}
