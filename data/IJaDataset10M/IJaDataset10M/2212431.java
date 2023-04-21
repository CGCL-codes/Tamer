package net.sourceforge.nconfigurations;

import net.sourceforge.nconfigurations.ImmNodeTreeBuilder.KeyValueAssoc;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author Petr Novotník
 */
public class MergingConfigurationTest {

    @Test
    public void testGetChildren() {
        final List<KeyValueAssoc> aAssocs = Arrays.asList(new KeyValueAssoc("a.aa1", "a-assoc: a.aa1"), new KeyValueAssoc("a.aa2", "a-assoc: a.aa2"), new KeyValueAssoc("b.ab1", "a-assoc: b.ab1"));
        final List<KeyValueAssoc> bAssocs = Arrays.asList(new KeyValueAssoc("a.ba1", "b-assoc: a.ba1"), new KeyValueAssoc("a.aa1", "b-assoc: a.aa1"));
        final List<KeyValueAssoc> cAssocs = Arrays.asList(new KeyValueAssoc("a.ca1", "c-assoc: a.ca1"), new KeyValueAssoc("b.cb1", "c-assoc: b.cb1"));
        final Configuration merged = new MergingConfiguration(ImmNodeTreeBuilder.buildTree("a", KeyFactory.getInstance('.'), aAssocs), ImmNodeTreeBuilder.buildTree("b", KeyFactory.getInstance('.'), bAssocs), ImmNodeTreeBuilder.buildTree("c", KeyFactory.getInstance('.'), cAssocs));
        final Properties expMerged = new Properties();
        expMerged.setProperty("a.aa1", "a-assoc: a.aa1");
        expMerged.setProperty("a.aa2", "a-assoc: a.aa2");
        expMerged.setProperty("b.ab1", "a-assoc: b.ab1");
        expMerged.setProperty("a.ba1", "b-assoc: a.ba1");
        expMerged.setProperty("a.ca1", "c-assoc: a.ca1");
        expMerged.setProperty("b.cb1", "c-assoc: b.cb1");
        Assert.assertEquals(merged.toProperties(), expMerged);
        final Map<String, ? extends Configuration> children = merged.getChildrenMap();
        final List<String> expChildrenNames = Arrays.asList("a", "b");
        Assert.assertTrue(children.keySet().containsAll(expChildrenNames));
        Assert.assertTrue(expChildrenNames.containsAll(children.keySet()));
        final Configuration aChild = children.get("a");
        Assert.assertNotNull(aChild);
        final Properties expAChild = new Properties();
        expAChild.setProperty("aa1", "a-assoc: a.aa1");
        expAChild.setProperty("aa2", "a-assoc: a.aa2");
        expAChild.setProperty("ba1", "b-assoc: a.ba1");
        expAChild.setProperty("ca1", "c-assoc: a.ca1");
        Assert.assertEquals(aChild.toProperties(), expAChild);
        final Configuration bChild = children.get("b");
        Assert.assertNotNull(bChild);
        final Properties expBChild = new Properties();
        expBChild.setProperty("ab1", "a-assoc: b.ab1");
        expBChild.setProperty("cb1", "c-assoc: b.cb1");
        Assert.assertEquals(bChild.toProperties(), expBChild);
        final Configuration doesntExist = children.get("b").getChild(KeyFactory.keyFrom("hello.world.foo.bar", Configuration.class), null);
        Assert.assertNull(doesntExist);
        final Configuration doesExist = children.get("b").getChild(KeyFactory.keyFrom("cb1", Configuration.class), null);
        Assert.assertNotNull(doesExist);
    }

    @Test
    public void testGetValueDefault() throws URISyntaxException {
        final URI nconfigURL = new URI("http://nconfigurations.sourceforge.net");
        final List<KeyValueAssoc> oneAssocs = Arrays.asList(new KeyValueAssoc("foo.bar", null), new KeyValueAssoc("foo.bar.hello.world", 42));
        final List<KeyValueAssoc> twoAssocs = Arrays.asList(new KeyValueAssoc("foo.bar", "not-null"), new KeyValueAssoc("foo.bar.hello.world", 84), new KeyValueAssoc("foo.bar.wow", "dup"), new KeyValueAssoc("foo.bar.fallback", nconfigURL));
        final Configuration merged = new MergingConfiguration(ImmNodeTreeBuilder.buildTree("one", KeyFactory.getInstance('.'), oneAssocs.iterator()), ImmNodeTreeBuilder.buildTree("default", KeyFactory.getInstance('.'), twoAssocs.iterator()));
        final Key<String> kOne = KeyFactory.keyFrom("foo.bar", String.class);
        final String vOne = merged.getValue(kOne, "default");
        Assert.assertNull(vOne);
        final Key<Integer> kTwo = KeyFactory.keyFrom("foo.bar.hello.world", Integer.class);
        final Integer vTwo = merged.getValue(kTwo, 0);
        Assert.assertEquals(vTwo, Integer.valueOf(42));
        final Key<String> kThree = KeyFactory.keyFrom("foo.bar.wow", String.class);
        final String vThree = merged.getValue(kThree, "blub");
        Assert.assertEquals(vThree, "dup");
        final Key<URI> kFour = KeyFactory.keyFrom("foo.bar.fallback", URI.class);
        final URI vFour = merged.getValue(kFour, new URI("http://easyconf.sourceforge.net"));
        Assert.assertEquals(vFour, nconfigURL);
        final Key<Object> kFive = KeyFactory.keyFrom("non.existing.node", Object.class);
        final Object vFive = merged.getValue(kFive, nconfigURL);
        Assert.assertSame(vFive, nconfigURL);
    }

    @Test
    public void testGetMissingValue() throws URISyntaxException {
        final List<KeyValueAssoc> oneAssocs = Arrays.asList(new KeyValueAssoc("one.two.three", null), new KeyValueAssoc("one.two.three.four.five", 42), new KeyValueAssoc("foo.bar", "world"));
        final List<KeyValueAssoc> twoAssocs = Arrays.asList(new KeyValueAssoc("one.two.three", new Object()), new KeyValueAssoc("one.two.three.four.five", new URI("http://nconfigurations.sourceforge.net")), new KeyValueAssoc("one.two.six", "hello"));
        final Configuration merged = new MergingConfiguration(ImmNodeTreeBuilder.buildTree("one", KeyFactory.getInstance('.'), oneAssocs.iterator()), ImmNodeTreeBuilder.buildTree("two", KeyFactory.getInstance('.'), twoAssocs.iterator()));
        final Key<Object> kOne = KeyFactory.keyFrom("one.two.three", Object.class);
        Assert.assertNull(merged.getValue(kOne));
        final Key<Integer> kTwo = KeyFactory.keyFrom("one.two.three.four.five", Integer.class);
        Assert.assertEquals(merged.getValue(kTwo), Integer.valueOf(42));
        final Key<URI> kTwoWrong = KeyFactory.keyFrom("one.two.three.four.five", URI.class);
        try {
            merged.getValue(kTwoWrong);
            Assert.fail("type incompatibility not detected!");
        } catch (final IncompatibleTypeException e) {
            Assert.assertEquals(e.getKey(), kTwoWrong);
        }
        final URI _defaultURI = new URI("http://javaconfig.sourceforge.net");
        Assert.assertEquals(merged.getValue(kTwoWrong, _defaultURI), _defaultURI);
        final Key<String> kThree = KeyFactory.keyFrom("foo.bar", String.class);
        Assert.assertEquals(merged.getValue(kThree), "world");
        final Key<String> kFour = KeyFactory.keyFrom("one.two.six", String.class);
        Assert.assertEquals(merged.getValue(kFour), "hello");
    }

    @Test(dataProvider = "test-different-properties-dp")
    public void testToProperties(final Properties[] differentProperties) throws BuilderException {
        final Properties configProperties = buildMergedConfiguration(differentProperties).toProperties();
        for (int i = differentProperties.length - 1; i >= 0; i--) {
            final Enumeration<?> keys = differentProperties[i].propertyNames();
            while (keys.hasMoreElements()) {
                final String key = (String) keys.nextElement();
                boolean isOverriden = false;
                for (int j = i - 1; j >= 0; j--) {
                    isOverriden = (differentProperties[j].getProperty(key) != null);
                    if (isOverriden) {
                        break;
                    }
                }
                if (!isOverriden) {
                    final String configValue = configProperties.getProperty(key);
                    Assert.assertEquals(configValue, differentProperties[i].getProperty(key), key);
                }
            }
        }
    }

    @Test(dataProvider = "test-different-properties-dp")
    public void testGetValue(final Properties[] differentProperties) throws BuilderException {
        final Configuration config = buildMergedConfiguration(differentProperties);
        for (int i = differentProperties.length - 1; i >= 0; i--) {
            final Enumeration<?> keys = differentProperties[i].propertyNames();
            while (keys.hasMoreElements()) {
                final String key = (String) keys.nextElement();
                boolean isOverriden = false;
                for (int j = i - 1; j >= 0; j--) {
                    isOverriden = differentProperties[j].getProperty(key) != null;
                    if (isOverriden) {
                        break;
                    }
                }
                if (!isOverriden) {
                    final String configValue = config.getValue(KeyFactory.keyFrom(key, String.class));
                    Assert.assertEquals(configValue, differentProperties[i].getProperty(key));
                }
            }
        }
    }

    @Test
    public void testGetChild() throws BuilderException {
        final Properties one = new Properties();
        one.setProperty("foo.bar.hello", "hello");
        one.setProperty("foo.bar.wow", "wow");
        one.setProperty("one.two.three", "2");
        final Properties two = new Properties();
        two.setProperty("foo.bar.hello", "overriden-hello");
        two.setProperty("foo.bar.world", "world");
        two.setProperty("one.two.four", "4");
        two.setProperty("five.jive.jazz", "it's all that jazz");
        final Configuration oneConfig = new PropertiesConfigurationBuilder(one).buildConfiguration("one");
        final Configuration twoConfig = new PropertiesConfigurationBuilder(two).buildConfiguration("two");
        final Configuration mergedConfig = new MergingConfiguration(twoConfig, oneConfig);
        final Configuration fooBarSubTree = mergedConfig.getChild(KeyFactory.keyFrom("foo.bar", Configuration.class));
        Assert.assertNotNull(mergedConfig.getChild(KeyFactory.keyFrom("one.two", Configuration.class)));
        final Key<Configuration> missingConfigKey = KeyFactory.keyFrom("blobl.blub", Configuration.class);
        try {
            Assert.assertNull(mergedConfig.getChild(missingConfigKey));
            Assert.fail("failed to detect missing child!");
        } catch (final MissingChildException e) {
            Assert.assertEquals(e.getKey(), missingConfigKey);
        }
        Assert.assertNotNull(mergedConfig.getChild(KeyFactory.keyFrom("five.jive", Configuration.class)));
        assertValueEquals(fooBarSubTree, "hello", "overriden-hello");
        assertValueEquals(fooBarSubTree, "world", "world");
        assertValueEquals(fooBarSubTree, "wow", "wow");
        try {
            assertValueEquals(fooBarSubTree, "non-existent", null);
            Assert.fail("missing value not detected!");
        } catch (MissingValueException e) {
        }
    }

    private void assertValueEquals(final Configuration config, final String key, final String expectedValue) {
        Assert.assertNotNull(config);
        Assert.assertNotNull(key);
        final String value = config.getValue(KeyFactory.keyFrom(key, String.class));
        if (expectedValue == null) {
            Assert.assertNull(value);
        } else {
            Assert.assertNotNull(value);
            Assert.assertEquals(value, expectedValue);
        }
    }

    @DataProvider(name = "test-different-properties-dp")
    public Object[][] testToPropertiesDP() {
        final Properties one = new Properties();
        one.setProperty("foo.bar.hello.world", "1");
        one.setProperty("foo.bar.world", "2");
        final Properties two = new Properties();
        two.setProperty("foo.bar.hello.world", "2");
        two.setProperty("foo.bar.flup", "3");
        return new Object[][] { new Object[] { new Properties[] { one, two } } };
    }

    private MergingConfiguration buildMergedConfiguration(final Properties[] propertieses) throws BuilderException {
        Assert.assertNotNull(propertieses);
        Assert.assertTrue(propertieses.length > 1);
        switch(propertieses.length) {
            case 2:
                return new MergingConfiguration(toConfiguration(propertieses[0], "one"), toConfiguration(propertieses[1], "two"));
            case 3:
                return new MergingConfiguration(toConfiguration(propertieses[0], "one"), toConfiguration(propertieses[1], "two"), toConfiguration(propertieses[2], "three"));
            default:
                Assert.fail("building merged-configuration out of more than 3 configs is not supported yet!");
                return null;
        }
    }

    private Configuration toConfiguration(final Properties props, final String name) throws BuilderException {
        return new PropertiesConfigurationBuilder(props).buildConfiguration(name);
    }
}
