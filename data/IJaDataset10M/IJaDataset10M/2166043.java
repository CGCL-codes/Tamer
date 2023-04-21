package com.googlecode.torrent4j.bencoding;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;

public class BEEncoderTest {

    BEEncoder encoder;

    @Before
    public void setUp() {
        encoder = new BEEncoder();
    }

    @Test
    public void encodingASimpleArray() {
        String origValue = "hola";
        BEValue value = new BEValue(origValue);
        byte[] result = encoder.encode(value);
        assertThat(new String(result), equalTo(origValue.length() + ":" + origValue));
    }

    @Test
    public void encodingANumber() {
        BEValue value = new BEValue(164);
        byte[] result = encoder.encode(value);
        assertThat(new String(result), equalTo("i164e"));
    }

    @Test
    public void encodingAList() {
        BEValue value1 = new BEValue(164);
        BEValue value2 = new BEValue("hola");
        List<BEValue> list = new ArrayList<BEValue>();
        list.add(value1);
        list.add(value2);
        BEValue value = new BEValue(list);
        byte[] result = encoder.encode(value);
        assertThat(new String(result), equalTo("li164e4:holae"));
    }

    @Test
    public void encodingAMap() {
        BEValue value1 = new BEValue(164);
        BEValue value2 = new BEValue("hola");
        Map<String, BEValue> map = new LinkedHashMap<String, BEValue>();
        map.put("key1", value1);
        map.put("key2", value2);
        BEValue value = new BEValue(map);
        byte[] result = encoder.encode(value);
        assertThat(new String(result), equalTo("d4:key1i164e4:key24:holae"));
    }
}
