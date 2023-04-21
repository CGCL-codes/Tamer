package com.googlecode.batchfb.test;

import java.util.List;
import org.testng.annotations.Test;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.googlecode.batchfb.Later;

/**
 * Basic unit tests for the batching features
 * 
 * @author Jeff Schnitzer
 */
public class BasicBatchTests extends TestBase {

    /** */
    static class User {

        public String name;
    }

    /**
	 */
    @Test
    public void singleGraphRequestAsNode() throws Exception {
        Later<JsonNode> node = this.authBatcher.graph("/1047296661");
        assert "Robert Dobbs".equals(node.get().get("name").textValue());
    }

    /**
	 */
    @Test
    public void singleGraphRequestAsObject() throws Exception {
        Later<User> user = this.authBatcher.graph("/1047296661", User.class);
        assert "Robert Dobbs".equals(user.get().name);
    }

    /**
	 */
    @Test
    public void singleFqlAsNode() throws Exception {
        Later<ArrayNode> array = this.authBatcher.query("SELECT name FROM user WHERE uid = 1047296661");
        assert 1 == array.get().size();
        assert "Robert Dobbs".equals(array.get().get(0).get("name").textValue());
    }

    /**
	 */
    @Test
    public void singleFqlAsNodeUsingQueryFirst() throws Exception {
        Later<JsonNode> node = this.authBatcher.queryFirst("SELECT name FROM user WHERE uid = 1047296661");
        assert "Robert Dobbs".equals(node.get().get("name").textValue());
    }

    /**
	 */
    @Test
    public void singleFqlAsObject() throws Exception {
        Later<List<User>> array = this.authBatcher.query("SELECT name FROM user WHERE uid = 1047296661", User.class);
        assert 1 == array.get().size();
        assert "Robert Dobbs".equals(array.get().get(0).name);
    }

    /**
	 */
    @Test
    public void singleFqlAsObjectUsingQueryFirst() throws Exception {
        Later<User> array = this.authBatcher.queryFirst("SELECT name FROM user WHERE uid = 1047296661", User.class);
        assert "Robert Dobbs".equals(array.get().name);
    }
}
