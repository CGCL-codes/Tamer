package net.sf.sit.values.impl;

import static org.junit.Assert.assertNull;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import net.sf.sit.selftest.facade.FA111;
import net.sf.sit.selftest.provider.PA111;
import net.sf.sit.test.util.MyEnum;
import org.jmock.Expectations;
import org.junit.Test;

public class DefaultConverterTest {

    @Test
    public void convert_Null() {
        new DefaultConverterTestBase() {

            @Override
            public Expectations getExpectations() {
                return new Expectations();
            }

            @Override
            public void exPost() {
                assertNull("Returns null", actual);
            }
        }.doTest();
    }

    @Test
    public void convert_StringToString() {
        new DefaultConverterSimpleTypeTestBase() {

            @Override
            public void afterSetUp() {
                src = "abc";
                type = String.class;
                expected = "def";
            }
        }.doTest();
    }

    @Test
    public void convert_StringToInteger() {
        new DefaultConverterSimpleTypeTestBase() {

            @Override
            public void afterSetUp() {
                src = "abc";
                type = Integer.class;
                expected = 456;
            }
        }.doTest();
    }

    @Test
    public void convert_IntegerToString() {
        new DefaultConverterSimpleTypeTestBase() {

            @Override
            public void afterSetUp() {
                src = 123;
                type = String.class;
                expected = "klm";
            }
        }.doTest();
    }

    @Test
    public void convert_StringToEnum() {
        new DefaultConverterTestBase() {

            @Override
            public void afterSetUp() {
                src = "beta";
                type = MyEnum.class;
                expected = MyEnum.gamma;
            }

            @Override
            public Expectations getExpectations() throws Exception {
                return new Expectations() {

                    {
                        one(d).map(src, type);
                        will(returnValue(MyEnum.gamma));
                    }
                };
            }
        }.doTest();
    }

    @Test
    public void convert_EnumToString() {
        new DefaultConverterTestBase() {

            @Override
            public void afterSetUp() {
                src = MyEnum.gamma;
                type = String.class;
                expected = "beta";
            }

            @Override
            public Expectations getExpectations() throws Exception {
                return new Expectations() {

                    {
                        one(d).map(src, type);
                        will(returnValue("beta"));
                    }
                };
            }
        }.doTest();
    }

    @Test
    public void convert_StructureToString() {
        new DefaultConverterSimpleTypeTestBase() {

            @Override
            public void afterSetUp() {
                src = new FA111("abc");
                type = String.class;
                expected = "abc";
            }
        }.doTest();
    }

    @Test
    public void convert_StringToStructure() {
        new DefaultConverterSimpleTypeTestBase() {

            @Override
            public void afterSetUp() {
                src = "abc";
                type = FA111.class;
                expected = "nop";
            }
        }.doTest();
    }

    @Test
    public void convert_StructureToStructure() {
        new DefaultConverterTestBase() {

            @Override
            public void afterSetUp() {
                src = new FA111("abc");
                type = PA111.class;
                expected = new PA111("qrs");
            }

            @Override
            public Expectations getExpectations() throws Exception {
                return new Expectations() {

                    {
                        one(d).map(src, type);
                        will(returnValue(new PA111("qrs")));
                    }
                };
            }
        }.doTest();
    }

    @Test
    public void convert_BeanArrayToArray() {
        new DefaultConverterArrayTestBase() {

            @Override
            public void afterSetUp() {
                src = tdc.create(FA111[].class);
                type = PA111[].class;
                expected = tdc.create(PA111[].class);
            }

            @Override
            public Expectations getExpectations() throws Exception {
                return new Expectations() {

                    {
                        one(d).map(((Object[]) src)[0], PA111.class);
                        will(returnValue(new PA111("s952")));
                        one(d).map(((Object[]) src)[1], PA111.class);
                        will(returnValue(new PA111("s953")));
                        one(d).map(((Object[]) src)[2], PA111.class);
                        will(returnValue(new PA111("s954")));
                    }
                };
            }
        }.doTest();
    }

    @Test
    public void convert_BeanArrayToList() {
        new DefaultConverterListTestBase() {

            @Override
            public void afterSetUp() {
                src = tdc.create(FA111[].class);
                type = Collection.class;
                componentTypeHint = PA111.class;
                expected = tdc.create(PA111[].class);
            }

            @Override
            public Expectations getExpectations() throws Exception {
                return new Expectations() {

                    {
                        one(d).map(((Object[]) src)[0], PA111.class);
                        will(returnValue(new PA111("s952")));
                        one(d).map(((Object[]) src)[1], PA111.class);
                        will(returnValue(new PA111("s953")));
                        one(d).map(((Object[]) src)[2], PA111.class);
                        will(returnValue(new PA111("s954")));
                    }
                };
            }
        }.doTest();
    }

    @Test
    public void convert_BeanListToArray() {
        new DefaultConverterArrayTestBase() {

            @Override
            public void afterSetUp() {
                src = Arrays.asList(tdc.create(FA111[].class));
                type = PA111[].class;
                expected = tdc.create(PA111[].class);
            }

            @Override
            public Expectations getExpectations() throws Exception {
                return new Expectations() {

                    {
                        one(d).map(((List<?>) src).get(0), PA111.class);
                        will(returnValue(new PA111("s952")));
                        one(d).map(((List<?>) src).get(1), PA111.class);
                        will(returnValue(new PA111("s953")));
                        one(d).map(((List<?>) src).get(2), PA111.class);
                        will(returnValue(new PA111("s954")));
                    }
                };
            }
        }.doTest();
    }

    @Test
    public void convert_BeanListToList() {
        new DefaultConverterListTestBase() {

            @Override
            public void afterSetUp() {
                src = Arrays.asList(tdc.create(FA111[].class));
                type = Collection.class;
                componentTypeHint = PA111.class;
                expected = tdc.create(PA111[].class);
            }

            @Override
            public Expectations getExpectations() throws Exception {
                return new Expectations() {

                    {
                        one(d).map(((List<?>) src).get(0), PA111.class);
                        will(returnValue(new PA111("s952")));
                        one(d).map(((List<?>) src).get(1), PA111.class);
                        will(returnValue(new PA111("s953")));
                        one(d).map(((List<?>) src).get(2), PA111.class);
                        will(returnValue(new PA111("s954")));
                    }
                };
            }
        }.doTest();
    }

    @Test
    public void convert_SimpleArrayToArray() {
        new DefaultConverterArrayTestBase() {

            @Override
            public void afterSetUp() {
                src = tdc.create(Integer[].class);
                type = String[].class;
                expected = tdc.create(String[].class);
            }

            @Override
            public Expectations getExpectations() throws Exception {
                return new Expectations() {

                    {
                        one(d).map(((Object[]) src)[0], String.class);
                        will(returnValue("String461"));
                        one(d).map(((Object[]) src)[1], String.class);
                        will(returnValue("String462"));
                        one(d).map(((Object[]) src)[2], String.class);
                        will(returnValue("String463"));
                    }
                };
            }
        }.doTest();
    }

    @Test
    public void convert_SimpleListToList() {
        new DefaultConverterListTestBase() {

            @Override
            public void afterSetUp() {
                src = Arrays.asList(tdc.create(Integer[].class));
                type = Collection.class;
                componentTypeHint = String.class;
                expected = tdc.create(String[].class);
            }

            @Override
            public Expectations getExpectations() throws Exception {
                return new Expectations() {

                    {
                        one(d).map(((List<?>) src).get(0), String.class);
                        will(returnValue("String461"));
                        one(d).map(((List<?>) src).get(1), String.class);
                        will(returnValue("String462"));
                        one(d).map(((List<?>) src).get(2), String.class);
                        will(returnValue("String463"));
                    }
                };
            }
        }.doTest();
    }
}
