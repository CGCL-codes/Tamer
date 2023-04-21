package net.taylor.fitnesse;

import org.junit.Test;
import org.junit.runner.RunWith;
import fitnesse.junit.FitNesseSuite;
import fitnesse.junit.FitNesseSuite.FitnesseDir;
import fitnesse.junit.FitNesseSuite.Name;
import fitnesse.junit.FitNesseSuite.OutputDir;

@RunWith(FitNesseSuite.class)
@Name("FitNesse.SuiteAcceptanceTests.SuiteSlimTests")
@FitnesseDir("./src/test/fitnesse")
@OutputDir("./target/fitnesse")
public class LocalTest {

    @Test
    public void dummy() {
    }
}
