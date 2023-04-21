package samples.junit4.rules;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

@RunWith(PowerMockRunner.class)
public class RuleOrderTest {

    private static final String EMPTY_STRING = "";

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private String temporaryFileName = EMPTY_STRING;

    @Before
    public void setup() throws Exception {
        temporaryFileName = folder.newFile("tempFile").getPath();
    }

    @Test
    public void rulesAreExecutedBeforeSetupMethods() throws Exception {
        assertThat(temporaryFileName, not(nullValue()));
        assertThat(temporaryFileName, not(equalTo(EMPTY_STRING)));
    }
}
