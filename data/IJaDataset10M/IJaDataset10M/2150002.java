package org.fedoracommons.unapi;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.fedoracommons.unapi.fedora.FedoraResolverTest;
import org.fedoracommons.unapi.pmh.dspace.DSpacePmhResolverTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({ FormatsSerializerTest.class, FedoraResolverTest.class, DSpacePmhResolverTest.class })
public class AllTests {
}
