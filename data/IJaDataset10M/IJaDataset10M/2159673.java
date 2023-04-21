package com.twolattes.json.visibility1;

import com.twolattes.json.Entity;
import com.twolattes.json.Value;

@Entity
public class PublicClassPrivateConstructor {

    @Value
    public int value = 9;

    private PublicClassPrivateConstructor() {
    }

    public static PublicClassPrivateConstructor create() {
        return new PublicClassPrivateConstructor();
    }
}
