package org.eiichiro.monophony.json;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;
import java.net.URL;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import org.eiichiro.monophony.UserDefinedValue1;
import org.eiichiro.monophony.UserDefinedValue2;
import org.eiichiro.monophony.UserDefinedObject1;
import org.eiichiro.monophony.annotation.Application;
import org.eiichiro.monophony.annotation.Body;
import org.eiichiro.monophony.annotation.Endpoint;

@Endpoint("/JSONRequestTest")
public class JSONRequestTestEndpoint {

    public void testJSONBody(@Body("booleans") boolean[] bs, @Body("notexist_obj") Object object, @Body("notexist_url") Collection<URL> urls, @Body("notexist_double") double d, @Body("int") int i, @Application("notexist_char") @Body("char") char c, @Body("strings") List<String> strings, @Body("type1") UserDefinedValue1 type1, @Body("type2") UserDefinedValue2 type2, @Body("type3") UserDefinedObject1 type3, @Body("value1s") List<UserDefinedValue1> value1s, @Body("object1s") List<UserDefinedObject1> object1s, @Body Long long1, @Body Collection<Date> dates, @Body UserDefinedValue1 type12, @Body UserDefinedValue2 type22, @Body UserDefinedObject1 type32, @Body Set<UserDefinedValue1> value1s2, @Body Set<UserDefinedObject1> object1s2) {
        System.out.println("JSONRequestTestEndpoint#testJSONBody");
        assertThat(bs, nullValue());
        assertThat(object, nullValue());
        assertThat(urls.isEmpty(), is(true));
        assertThat(d, is(0.0));
        assertThat(i, is(9));
        assertThat(c, is('c'));
        assertThat(strings.size(), is(2));
        assertThat(strings.get(0), is("aaa"));
        assertThat(strings.get(1), is("bbb"));
        assertThat(type1.getValue(), is("hello"));
        assertThat(type2.getValue(), is("hi"));
        assertThat(type3.getUserDefinedType3Value(), is("bonjour"));
        assertThat(value1s.size(), is(2));
        assertThat(value1s.get(0).getValue(), is("aloha"));
        assertThat(value1s.get(1).getValue(), is("jambo"));
        assertThat(object1s.size(), is(2));
        assertThat(object1s.get(0).getUserDefinedType3Value(), is("Adieu"));
        assertThat(object1s.get(1).getUserDefinedType3Value(), is("Adios"));
        assertThat(long1, nullValue());
        assertThat(dates.isEmpty(), is(true));
        assertThat(type12, nullValue());
        assertThat(type22, nullValue());
        assertThat(type32.getUserDefinedType3Value(), is("goodbye"));
        assertThat(value1s2.isEmpty(), is(true));
        assertThat(object1s2.isEmpty(), is(true));
    }

    public void testJSONCollection1(@Body Collection<Integer> integers) {
        assertThat(integers.size(), is(2));
        Integer[] is = new Integer[2];
        integers.toArray(is);
        assertThat(is[0], is(1));
        assertThat(is[1], is(2));
    }

    public void testJSONCollection2(@Body Set<UserDefinedValue1> value1s) {
        assertThat(value1s.size(), is(2));
        UserDefinedValue1[] userDefinedValue1s = new UserDefinedValue1[2];
        value1s.toArray(userDefinedValue1s);
        if (userDefinedValue1s[0].getValue().equals("a")) {
            assertThat(userDefinedValue1s[1].getValue(), is("b"));
        } else if (userDefinedValue1s[0].getValue().equals("b")) {
            assertThat(userDefinedValue1s[1].getValue(), is("a"));
        } else {
            fail();
        }
    }

    public void testJSONCollection3(@Body Set<UserDefinedObject1> object1s) {
        assertThat(object1s.size(), is(2));
        UserDefinedObject1[] userDefinedObject1s = new UserDefinedObject1[2];
        object1s.toArray(userDefinedObject1s);
        if (userDefinedObject1s[0].getUserDefinedType3Value().equals("c")) {
            assertThat(userDefinedObject1s[1].getUserDefinedType3Value(), is("d"));
        } else if (userDefinedObject1s[0].getUserDefinedType3Value().equals("d")) {
            assertThat(userDefinedObject1s[1].getUserDefinedType3Value(), is("c"));
        } else {
            fail();
        }
    }
}
