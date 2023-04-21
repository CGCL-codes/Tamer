package argo.jdom;

import org.junit.Test;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import static argo.jdom.JsonNodeBuilders.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public final class JsonNodeBuildersTest {

    @Test
    public void nullBuilderBuildsNull() throws Exception {
        assertThat(aNullBuilder().build(), equalTo(JsonNodeFactories.nullNode()));
    }

    @Test
    public void trueBuilderBuildsTrue() throws Exception {
        assertThat(aTrueBuilder().build(), equalTo(JsonNodeFactories.trueNode()));
    }

    @Test
    public void falseBuilderBuildsFalse() throws Exception {
        assertThat(aFalseBuilder().build(), equalTo(JsonNodeFactories.falseNode()));
    }

    @Test
    public void numberBuilderBuildsANumber() throws Exception {
        assertThat(aNumberBuilder("10.1").build(), equalTo(JsonNodeFactories.number("10.1")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void numberBuilderFailsEarlyOnAnInvalidNumber() throws Exception {
        assertThat(aNumberBuilder("-010.1e12").build(), equalTo(JsonNodeFactories.number("10.1")));
    }

    @Test
    public void stringBuilderBuildsAString() throws Exception {
        assertThat(aStringBuilder("Hello").build(), equalTo(JsonNodeFactories.string("Hello")));
    }

    @Test
    public void arrayBuilderBuildsAnArrayWithNoElements() throws Exception {
        assertThat(anArrayBuilder().build(), equalTo(JsonNodeFactories.array(new LinkedList<JsonNode>())));
    }

    @Test
    public void arrayBuilderBuildsAnArrayWithElements() throws Exception {
        assertThat(anArrayBuilder().withElement(aStringBuilder("Bob")).build(), equalTo(JsonNodeFactories.array(new LinkedList<JsonNode>(Arrays.asList(JsonNodeFactories.string("Bob"))))));
    }

    @Test
    public void objectBuilderBuildsAnObjectWithNoFields() throws Exception {
        assertThat(anObjectBuilder().build(), equalTo(JsonNodeFactories.object(new HashMap<JsonStringNode, JsonNode>())));
    }

    @Test
    public void objectBuilderBuildsAnObjectWithFields() throws Exception {
        assertThat(anObjectBuilder().withField("Hunky", aStringBuilder("dory")).build(), equalTo(JsonNodeFactories.object(new HashMap<JsonStringNode, JsonNode>() {

            {
                put(JsonNodeFactories.string("Hunky"), JsonNodeFactories.string("dory"));
            }
        })));
    }
}
