package com.dyuproject.protostuff;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Test case for tail-delimited protostuff messages.
 *
 * @author David Yu
 * @created Oct 5, 2010
 */
public class TailDelimiterTest extends AbstractTest {

    public <T> int writeListTo(OutputStream out, List<T> messages, Schema<T> schema) throws IOException {
        return ProtostuffIOUtil.writeListTo(out, messages, schema, new LinkedBuffer(LinkedBuffer.DEFAULT_BUFFER_SIZE));
    }

    public <T> List<T> parseListFrom(InputStream in, Schema<T> schema) throws IOException {
        return ProtostuffIOUtil.parseListFrom(in, schema);
    }

    public void testBar() throws Exception {
        ArrayList<Bar> bars = new ArrayList<Bar>();
        bars.add(SerializableObjects.bar);
        bars.add(SerializableObjects.negativeBar);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        writeListTo(out, bars, SerializableObjects.bar.cachedSchema());
        byte[] data = out.toByteArray();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        List<Bar> parsedBars = parseListFrom(in, SerializableObjects.bar.cachedSchema());
        assertTrue(parsedBars.size() == bars.size());
        int i = 0;
        for (Bar b : parsedBars) SerializableObjects.assertEquals(bars.get(i++), b);
    }

    public void testEmptyBar() throws Exception {
        ArrayList<Bar> bars = new ArrayList<Bar>();
        bars.add(new Bar());
        bars.add(new Bar());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        writeListTo(out, bars, SerializableObjects.bar.cachedSchema());
        byte[] data = out.toByteArray();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        List<Bar> parsedBars = parseListFrom(in, SerializableObjects.bar.cachedSchema());
        assertTrue(parsedBars.size() == bars.size());
        int i = 0;
        for (Bar b : parsedBars) SerializableObjects.assertEquals(bars.get(i++), b);
    }

    public void testEmptyBar2() throws Exception {
        ArrayList<Bar> bars = new ArrayList<Bar>();
        bars.add(new Bar());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        writeListTo(out, bars, SerializableObjects.bar.cachedSchema());
        byte[] data = out.toByteArray();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        List<Bar> parsedBars = parseListFrom(in, SerializableObjects.bar.cachedSchema());
        assertTrue(parsedBars.size() == bars.size());
        int i = 0;
        for (Bar b : parsedBars) SerializableObjects.assertEquals(bars.get(i++), b);
    }

    public void testEmptyBarInner() throws Exception {
        Bar bar = new Bar();
        bar.setSomeBaz(new Baz());
        ArrayList<Bar> bars = new ArrayList<Bar>();
        bars.add(bar);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        writeListTo(out, bars, SerializableObjects.bar.cachedSchema());
        byte[] data = out.toByteArray();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        List<Bar> parsedBars = parseListFrom(in, SerializableObjects.bar.cachedSchema());
        assertTrue(parsedBars.size() == bars.size());
        int i = 0;
        for (Bar b : parsedBars) SerializableObjects.assertEquals(bars.get(i++), b);
    }

    public void testFoo() throws Exception {
        ArrayList<Foo> foos = new ArrayList<Foo>();
        foos.add(SerializableObjects.foo);
        foos.add(SerializableObjects.foo);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        writeListTo(out, foos, SerializableObjects.foo.cachedSchema());
        byte[] data = out.toByteArray();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        List<Foo> parsedFoos = parseListFrom(in, SerializableObjects.foo.cachedSchema());
        assertTrue(parsedFoos.size() == foos.size());
        int i = 0;
        for (Foo f : parsedFoos) SerializableObjects.assertEquals(foos.get(i++), f);
    }

    public void testEmptyList() throws Exception {
        ArrayList<Foo> foos = new ArrayList<Foo>();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        writeListTo(out, foos, SerializableObjects.foo.cachedSchema());
        byte[] data = out.toByteArray();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        List<Foo> parsedFoos = parseListFrom(in, SerializableObjects.foo.cachedSchema());
        assertTrue(parsedFoos.size() == foos.size());
        int i = 0;
        for (Foo f : parsedFoos) SerializableObjects.assertEquals(foos.get(i++), f);
    }

    public void testEmptyFoo() throws Exception {
        ArrayList<Foo> foos = new ArrayList<Foo>();
        foos.add(new Foo());
        foos.add(new Foo());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        writeListTo(out, foos, SerializableObjects.foo.cachedSchema());
        byte[] data = out.toByteArray();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        List<Foo> parsedFoos = parseListFrom(in, SerializableObjects.foo.cachedSchema());
        assertTrue(parsedFoos.size() == foos.size());
        int i = 0;
        for (Foo f : parsedFoos) SerializableObjects.assertEquals(foos.get(i++), f);
    }

    public void testEmptyFoo2() throws Exception {
        ArrayList<Foo> foos = new ArrayList<Foo>();
        foos.add(new Foo());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        writeListTo(out, foos, SerializableObjects.foo.cachedSchema());
        byte[] data = out.toByteArray();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        List<Foo> parsedFoos = parseListFrom(in, SerializableObjects.foo.cachedSchema());
        assertTrue(parsedFoos.size() == foos.size());
        int i = 0;
        for (Foo f : parsedFoos) SerializableObjects.assertEquals(foos.get(i++), f);
    }

    public void testEmptyFooInner() throws Exception {
        ArrayList<Bar> bars = new ArrayList<Bar>();
        bars.add(new Bar());
        ArrayList<Foo> foos = new ArrayList<Foo>();
        Foo foo = new Foo();
        foo.setSomeBar(bars);
        foos.add(foo);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        writeListTo(out, foos, SerializableObjects.foo.cachedSchema());
        byte[] data = out.toByteArray();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        List<Foo> parsedFoos = parseListFrom(in, SerializableObjects.foo.cachedSchema());
        assertTrue(parsedFoos.size() == foos.size());
        int i = 0;
        for (Foo f : parsedFoos) SerializableObjects.assertEquals(foos.get(i++), f);
    }

    public void testEmptyFooInner2() throws Exception {
        ArrayList<Bar> bars = new ArrayList<Bar>();
        Bar bar = new Bar();
        bar.setSomeBaz(new Baz());
        bars.add(bar);
        ArrayList<Foo> foos = new ArrayList<Foo>();
        Foo foo = new Foo();
        foo.setSomeBar(bars);
        foos.add(foo);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        writeListTo(out, foos, SerializableObjects.foo.cachedSchema());
        byte[] data = out.toByteArray();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        List<Foo> parsedFoos = parseListFrom(in, SerializableObjects.foo.cachedSchema());
        assertTrue(parsedFoos.size() == foos.size());
        int i = 0;
        for (Foo f : parsedFoos) SerializableObjects.assertEquals(foos.get(i++), f);
    }
}
