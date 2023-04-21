package jnewsgate.auth;

/**
 * A hash that does not hash anything.
 */
public class ClearTextHash implements Hash {

    public String hashData(String data, String saltTemplate) {
        return data;
    }
}
