package test.bittorrent4j.encoding;

import org.bittorrent4j.encoding.BencodeReader;

/**
 * @author <a href="mailto:opalka.richard@gmail.com">Richard Opalka</a>
 */
public final class BencodeReaderStatesTestCase extends AbstractBencodeTestCase {

    public BencodeReaderStatesTestCase() {
    }

    public void testNegativeEndDictionary() throws Exception {
        final BencodeReader bencodeReader = newBencodeReader("e");
        assertIncorrectState(bencodeReader);
    }

    public void testNegativeString() throws Exception {
        final BencodeReader bencodeReader = newBencodeReader("4:data");
        assertIncorrectState(bencodeReader);
    }

    public void testNegativeInteger() throws Exception {
        final BencodeReader bencodeReader = newBencodeReader("i4224696320");
        assertIncorrectState(bencodeReader);
    }

    public void testNegativeList() throws Exception {
        final BencodeReader bencodeReader = newBencodeReader("l");
        assertIncorrectState(bencodeReader);
    }

    public void testPositiveDictionaryEndDictionary() throws Exception {
        final BencodeReader bencodeReader = newBencodeReader("de");
        assertStartDictionaryState(bencodeReader);
        assertEndDictionaryState(bencodeReader);
        assertStreamEnd(bencodeReader);
    }

    public void testPositiveDictionary() throws Exception {
        final BencodeReader bencodeReader = newBencodeReader("d");
        assertStartDictionaryState(bencodeReader);
        assertNotStreamEnd(bencodeReader);
    }

    public void testPositiveDictionaryStringString() throws Exception {
        final BencodeReader bencodeReader = newBencodeReader("d3:key5:value");
        assertStartDictionaryState(bencodeReader);
        assertStringState(bencodeReader, "key");
        assertStringState(bencodeReader, "value");
        assertNotStreamEnd(bencodeReader);
    }

    public void testPositiveDictionaryStringStringString() throws Exception {
        final BencodeReader bencodeReader = newBencodeReader("d4:key15:value4:key2");
        assertStartDictionaryState(bencodeReader);
        assertStringState(bencodeReader, "key1");
        assertStringState(bencodeReader, "value");
        assertStringState(bencodeReader, "key2");
        assertNotStreamEnd(bencodeReader);
    }

    public void testPositiveDictionaryStringListInteger() throws Exception {
        final BencodeReader bencodeReader = newBencodeReader("d4:key1li0e");
        assertStartDictionaryState(bencodeReader);
        assertStringState(bencodeReader, "key1");
        assertStartListState(bencodeReader);
        assertIntegerState(bencodeReader, 0L);
        assertNotStreamEnd(bencodeReader);
    }

    public void testPositiveDictionaryStringListString() throws Exception {
        final BencodeReader bencodeReader = newBencodeReader("d4:key1l4:key1");
        assertStartDictionaryState(bencodeReader);
        assertStringState(bencodeReader, "key1");
        assertStartListState(bencodeReader);
        assertStringState(bencodeReader, "key1");
        assertNotStreamEnd(bencodeReader);
    }

    public void testPositiveDictionaryStringListDictionary() throws Exception {
        final BencodeReader bencodeReader = newBencodeReader("d4:key1ld");
        assertStartDictionaryState(bencodeReader);
        assertStringState(bencodeReader, "key1");
        assertStartListState(bencodeReader);
        assertStartDictionaryState(bencodeReader);
        assertNotStreamEnd(bencodeReader);
    }

    public void testPositiveDictionaryStringListList() throws Exception {
        final BencodeReader bencodeReader = newBencodeReader("d4:key1ll");
        assertStartDictionaryState(bencodeReader);
        assertStringState(bencodeReader, "key1");
        assertStartListState(bencodeReader);
        assertStartListState(bencodeReader);
        assertNotStreamEnd(bencodeReader);
    }

    public void testPositiveDictionaryStringListStringString() throws Exception {
        final BencodeReader bencodeReader = newBencodeReader("d4:key1l6:value16:value2");
        assertStartDictionaryState(bencodeReader);
        assertStringState(bencodeReader, "key1");
        assertStartListState(bencodeReader);
        assertStringState(bencodeReader, "value1");
        assertStringState(bencodeReader, "value2");
        assertNotStreamEnd(bencodeReader);
    }

    public void testPositiveDictionaryStringListEndList() throws Exception {
        final BencodeReader bencodeReader = newBencodeReader("d4:key1le");
        assertStartDictionaryState(bencodeReader);
        assertStringState(bencodeReader, "key1");
        assertStartListState(bencodeReader);
        assertEndListState(bencodeReader);
        assertNotStreamEnd(bencodeReader);
    }

    public void testPositiveDictionaryStringInteger() throws Exception {
        final BencodeReader bencodeReader = newBencodeReader("d3:keyi0e");
        assertStartDictionaryState(bencodeReader);
        assertStringState(bencodeReader, "key");
        assertIntegerState(bencodeReader, 0L);
        assertNotStreamEnd(bencodeReader);
    }

    public void testPositiveDictionaryString() throws Exception {
        final BencodeReader bencodeReader = newBencodeReader("d3:key");
        assertStartDictionaryState(bencodeReader);
        assertStringState(bencodeReader, "key");
        assertNotStreamEnd(bencodeReader);
    }

    public void testNegativeDictionaryStringIntegerDictionary() throws Exception {
        final BencodeReader bencodeReader = newBencodeReader("d3:keyi0ed");
        assertStartDictionaryState(bencodeReader);
        assertStringState(bencodeReader, "key");
        assertIntegerState(bencodeReader, 0L);
        assertIncorrectState(bencodeReader);
        assertNotStreamEnd(bencodeReader);
    }

    public void testPositiveDictionaryStringIntegerEndDictionary() throws Exception {
        final BencodeReader bencodeReader = newBencodeReader("d3:keyi0ee");
        assertStartDictionaryState(bencodeReader);
        assertStringState(bencodeReader, "key");
        assertIntegerState(bencodeReader, 0L);
        assertEndDictionaryState(bencodeReader);
        assertStreamEnd(bencodeReader);
    }

    public void testNegativeDictionaryStringIntegerInteger() throws Exception {
        final BencodeReader bencodeReader = newBencodeReader("d3:keyi0ei0e");
        assertStartDictionaryState(bencodeReader);
        assertStringState(bencodeReader, "key");
        assertIntegerState(bencodeReader, 0L);
        assertIncorrectState(bencodeReader);
        assertNotStreamEnd(bencodeReader);
    }

    public void testPositiveDictionaryStringIntegerString() throws Exception {
        final BencodeReader bencodeReader = newBencodeReader("d4:key1i0e4:key2");
        assertStartDictionaryState(bencodeReader);
        assertStringState(bencodeReader, "key1");
        assertIntegerState(bencodeReader, 0L);
        assertStringState(bencodeReader, "key2");
        assertNotStreamEnd(bencodeReader);
    }

    public void testNegativeDictionaryStringStringInteger() throws Exception {
        final BencodeReader bencodeReader = newBencodeReader("d3:key5:valuei0e");
        assertStartDictionaryState(bencodeReader);
        assertStringState(bencodeReader, "key");
        assertStringState(bencodeReader, "value");
        assertIncorrectState(bencodeReader);
        assertNotStreamEnd(bencodeReader);
    }

    public void testNegativeDictionaryStringStringDictionary() throws Exception {
        final BencodeReader bencodeReader = newBencodeReader("d3:key5:valued");
        assertStartDictionaryState(bencodeReader);
        assertStringState(bencodeReader, "key");
        assertStringState(bencodeReader, "value");
        assertIncorrectState(bencodeReader);
        assertNotStreamEnd(bencodeReader);
    }

    public void testPositiveDictionaryStringStringEndDictionary() throws Exception {
        final BencodeReader bencodeReader = newBencodeReader("d3:key5:valuee");
        assertStartDictionaryState(bencodeReader);
        assertStringState(bencodeReader, "key");
        assertStringState(bencodeReader, "value");
        assertEndDictionaryState(bencodeReader);
        assertStreamEnd(bencodeReader);
    }

    public void testNegativeDictionaryStringStringList() throws Exception {
        final BencodeReader bencodeReader = newBencodeReader("d3:key5:valuel");
        assertStartDictionaryState(bencodeReader);
        assertStringState(bencodeReader, "key");
        assertStringState(bencodeReader, "value");
        assertIncorrectState(bencodeReader);
        assertNotStreamEnd(bencodeReader);
    }

    public void testPositiveDictionaryStringDictionary() throws Exception {
        final BencodeReader bencodeReader = newBencodeReader("d4:key1d");
        assertStartDictionaryState(bencodeReader);
        assertStringState(bencodeReader, "key1");
        assertStartDictionaryState(bencodeReader);
        assertNotStreamEnd(bencodeReader);
    }

    public void testPositiveDictionaryStringList() throws Exception {
        final BencodeReader bencodeReader = newBencodeReader("d4:key1l");
        assertStartDictionaryState(bencodeReader);
        assertStringState(bencodeReader, "key1");
        assertStartListState(bencodeReader);
        assertNotStreamEnd(bencodeReader);
    }

    public void testNegativeDictionaryStringIntegerList() throws Exception {
        final BencodeReader bencodeReader = newBencodeReader("d3:keyi0el");
        assertStartDictionaryState(bencodeReader);
        assertStringState(bencodeReader, "key");
        assertEquals(bencodeReader.getString(), "key");
        assertIntegerState(bencodeReader, 0L);
        assertIncorrectState(bencodeReader);
        assertNotStreamEnd(bencodeReader);
    }

    public void testNegativeDictionaryStringEndDictionary() throws Exception {
        final BencodeReader bencodeReader = newBencodeReader("d3:keye");
        assertStartDictionaryState(bencodeReader);
        assertStringState(bencodeReader, "key");
        assertIncorrectState(bencodeReader);
        assertNotStreamEnd(bencodeReader);
    }

    public void testNegativeDictionaryNullString() throws Exception {
        final BencodeReader bencodeReader = newBencodeReader("d0:");
        assertStartDictionaryState(bencodeReader);
        assertIncorrectState(bencodeReader);
        assertNotStreamEnd(bencodeReader);
    }

    public void testNegativeDictionaryStringNullString() throws Exception {
        final BencodeReader bencodeReader = newBencodeReader("d3:key0:");
        assertStartDictionaryState(bencodeReader);
        assertStringState(bencodeReader, "key");
        assertIncorrectState(bencodeReader);
        assertNotStreamEnd(bencodeReader);
    }

    public void testNegativeDictionaryStringNullInteger() throws Exception {
        final BencodeReader bencodeReader = newBencodeReader("d3:keyie");
        assertStartDictionaryState(bencodeReader);
        assertStringState(bencodeReader, "key");
        assertIncorrectState(bencodeReader);
        assertNotStreamEnd(bencodeReader);
    }

    public void testNegativeDictionaryDictionary() throws Exception {
        final BencodeReader bencodeReader = newBencodeReader("dd");
        assertStartDictionaryState(bencodeReader);
        assertIncorrectState(bencodeReader);
        assertNotStreamEnd(bencodeReader);
    }

    public void testNegativeDictionaryInteger() throws Exception {
        final BencodeReader bencodeReader = newBencodeReader("di0e");
        assertStartDictionaryState(bencodeReader);
        assertIncorrectState(bencodeReader);
        assertNotStreamEnd(bencodeReader);
    }

    public void testNegativeDictionaryList() throws Exception {
        final BencodeReader bencodeReader = newBencodeReader("dl");
        assertStartDictionaryState(bencodeReader);
        assertIncorrectState(bencodeReader);
        assertNotStreamEnd(bencodeReader);
    }

    public void testNegativeDictionaryEndDictionaryDictionary() throws Exception {
        final BencodeReader bencodeReader = newBencodeReader("ded");
        assertStartDictionaryState(bencodeReader);
        assertEndDictionaryState(bencodeReader);
        assertIncorrectState(bencodeReader);
        assertNotStreamEnd(bencodeReader);
    }

    public void testNegativeDictionaryEndDictionaryList() throws Exception {
        final BencodeReader bencodeReader = newBencodeReader("del");
        assertStartDictionaryState(bencodeReader);
        assertEndDictionaryState(bencodeReader);
        assertIncorrectState(bencodeReader);
        assertNotStreamEnd(bencodeReader);
    }

    public void testNegativeDictionaryEndDictionaryString() throws Exception {
        final BencodeReader bencodeReader = newBencodeReader("de3:key");
        assertStartDictionaryState(bencodeReader);
        assertEndDictionaryState(bencodeReader);
        assertIncorrectState(bencodeReader);
        assertNotStreamEnd(bencodeReader);
    }

    public void testNegativeDictionaryEndDictionaryInteger() throws Exception {
        final BencodeReader bencodeReader = newBencodeReader("dei0e");
        assertStartDictionaryState(bencodeReader);
        assertEndDictionaryState(bencodeReader);
        assertIncorrectState(bencodeReader);
        assertNotStreamEnd(bencodeReader);
    }

    public void testNegativeDictionaryEndDictionaryEndDictionary() throws Exception {
        final BencodeReader bencodeReader = newBencodeReader("dee");
        assertStartDictionaryState(bencodeReader);
        assertEndDictionaryState(bencodeReader);
        assertIncorrectState(bencodeReader);
        assertNotStreamEnd(bencodeReader);
    }

    public void testNegativeIncorrectString() throws Exception {
        BencodeReader bencodeReader = newBencodeReader("d4:key");
        assertStartDictionaryState(bencodeReader);
        assertIncorrectState(bencodeReader);
        assertNotStreamEnd(bencodeReader);
        bencodeReader = newBencodeReader("d3a:key1");
        assertStartDictionaryState(bencodeReader);
        assertIncorrectState(bencodeReader);
        assertNotStreamEnd(bencodeReader);
        bencodeReader = newBencodeReader("d3");
        assertStartDictionaryState(bencodeReader);
        assertIncorrectState(bencodeReader);
        assertNotStreamEnd(bencodeReader);
        bencodeReader = newBencodeReader("d3a");
        assertStartDictionaryState(bencodeReader);
        assertIncorrectState(bencodeReader);
        assertNotStreamEnd(bencodeReader);
    }

    public void testNegativeIncorrectIntegerSerialization() throws Exception {
        negativeIncorrectIntegerSerialization("d3:keyi0");
        negativeIncorrectIntegerSerialization("d3:keyi03e");
        negativeIncorrectIntegerSerialization("d3:keyi+3e");
        negativeIncorrectIntegerSerialization("d3:keyi0-e");
        negativeIncorrectIntegerSerialization("d3:keyi--3e");
        negativeIncorrectIntegerSerialization("d3:keyia3e");
        negativeIncorrectIntegerSerialization("d3:keyii");
        negativeIncorrectIntegerSerialization("d3:keyi");
        negativeIncorrectIntegerSerialization("d3:keyi-0e");
    }

    private void negativeIncorrectIntegerSerialization(final String incorrectStream) throws Exception {
        final BencodeReader bencodeReader = newBencodeReader(incorrectStream);
        assertStartDictionaryState(bencodeReader);
        assertStringState(bencodeReader, "key");
        assertIncorrectState(bencodeReader);
        assertNotStreamEnd(bencodeReader);
    }

    public void testPositiveCorrectIntegerSerialization() throws Exception {
        BencodeReader bencodeReader = newBencodeReader("d3:keyi" + Long.MAX_VALUE + "ee");
        assertStartDictionaryState(bencodeReader);
        assertStringState(bencodeReader, "key");
        assertIntegerState(bencodeReader, Long.MAX_VALUE);
        assertEndDictionaryState(bencodeReader);
        assertStreamEnd(bencodeReader);
        bencodeReader = newBencodeReader("d3:keyi" + Long.MIN_VALUE + "ee");
        assertStartDictionaryState(bencodeReader);
        assertStringState(bencodeReader, "key");
        assertIntegerState(bencodeReader, Long.MIN_VALUE);
        assertEndDictionaryState(bencodeReader);
        assertStreamEnd(bencodeReader);
    }
}
