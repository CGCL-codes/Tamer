package com.twolattes.json.nativetypes;

import static com.google.common.collect.Maps.immutableMap;
import static com.twolattes.json.Json.number;
import static com.twolattes.json.Json.string;
import static java.math.BigDecimal.valueOf;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import com.twolattes.json.Entity;
import com.twolattes.json.EntityMarshaller;
import com.twolattes.json.Json;
import com.twolattes.json.TwoLattes;
import com.twolattes.json.Value;

public class BigDecimalTest {

    private EntityMarshaller<BigDecimals> marshaller;

    @Before
    public void setUp() throws Exception {
        marshaller = TwoLattes.createEntityMarshaller(BigDecimals.class);
    }

    @Test
    public void nativeBigDecimalSuppot1() throws Exception {
        Json.Object object = marshaller.marshall(new BigDecimals() {

            {
                this.number = valueOf(6);
            }
        });
        assertEquals(1, object.size());
        assertEquals(number(6), object.get(string("number")));
        BigDecimals entity = marshaller.unmarshall(object);
        assertTrue(valueOf(6).compareTo(entity.number) == 0);
    }

    @Test
    public void nativeBigDecimalSuppot2() throws Exception {
        Json.Object object = marshaller.marshall(new BigDecimals() {

            {
                this.list = asList(valueOf(6.906), valueOf(-7));
            }
        });
        assertEquals(1, object.size());
        Json.Array list = (Json.Array) object.get(string("list"));
        assertEquals(2, list.size());
        assertEquals(number(6.906), list.get(0));
        assertEquals(number(-7), list.get(1));
        BigDecimals entity = marshaller.unmarshall(object);
        assertTrue(valueOf(6.906).compareTo(entity.list.get(0)) == 0);
        assertTrue(valueOf(-7).compareTo(entity.list.get(1)) == 0);
        assertEquals(2, entity.list.size());
    }

    @Test
    public void nativeBigDecimalSuppot3() throws Exception {
        Json.Object object = marshaller.marshall(new BigDecimals() {

            {
                this.map = immutableMap("hello", valueOf(90));
            }
        });
        assertEquals(1, object.size());
        Json.Object map = (Json.Object) object.get(string("map"));
        assertEquals(1, map.size());
        assertEquals(number(90), map.get(string("hello")));
        BigDecimals entity = marshaller.unmarshall(object);
        assertTrue(valueOf(90).compareTo(entity.map.get("hello")) == 0);
        assertEquals(1, entity.map.size());
    }

    @Entity
    static class BigDecimals {

        @Value(optional = true)
        BigDecimal number;

        @Value(optional = true)
        List<BigDecimal> list;

        @Value(optional = true)
        Map<String, BigDecimal> map;
    }
}
