package org.starobjects.jpa.metamodel.specloader;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.nakedobjects.metamodel.testutil.ReturnArgumentJMockAction.returnArgument;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nakedobjects.metamodel.config.ConfigurationBuilder;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.specloader.JavaReflector;
import org.nakedobjects.metamodel.specloader.NakedObjectReflector;

@RunWith(JMock.class)
public class GivenJpaJavaReflectorInstallerWhenCreateTest {

    private Mockery mockery = new JUnit4Mockery();

    private JpaJavaReflectorInstaller reflectorInstaller;

    private ConfigurationBuilder mockConfigurationBuilder;

    private NakedObjectConfiguration mockConfiguration;

    @Before
    public void setUp() throws Exception {
        mockConfigurationBuilder = mockery.mock(ConfigurationBuilder.class);
        mockConfiguration = mockery.mock(NakedObjectConfiguration.class);
        mockery.checking(new Expectations() {

            {
                allowing(mockConfigurationBuilder).getConfiguration();
                will(returnValue(mockConfiguration));
                allowing(mockConfiguration).getList("nakedobjects.reflector.facets.include");
                will(returnValue(new String[0]));
                allowing(mockConfiguration).getList("nakedobjects.reflector.facets.exclude");
                will(returnValue(new String[0]));
                allowing(mockConfiguration).getString(with(any(String.class)), with(any(String.class)));
                will(returnArgument(1));
                allowing(mockConfiguration).getList("nakedobjects.reflector.facet-decorators");
                will(returnValue(new String[0]));
            }
        });
        reflectorInstaller = new JpaJavaReflectorInstaller();
        reflectorInstaller.setConfigurationBuilder(mockConfigurationBuilder);
    }

    @Test
    public void shouldCreateAJavaReflector() throws Exception {
        NakedObjectReflector reflector = reflectorInstaller.createReflector();
        assertThat(reflector, is(JavaReflector.class));
    }
}
