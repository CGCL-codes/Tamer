package com.twolattes.json.visibility1;

import com.twolattes.json.Entity;
import com.twolattes.json.Value;

@Entity
public class PublicClassPackagePrivateConstructor {

    @Value
    public int value = 9;

    PublicClassPackagePrivateConstructor() {
    }

    public static PublicClassPackagePrivateConstructor create() {
        return new PublicClassPackagePrivateConstructor();
    }
}
