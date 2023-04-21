package persistence;

import model.Store;
import model.StoreFixture;
import security.Users;
import security.UsersFixture;

public class ModelFixture {

    public static Model simpleModel() {
        Store store = StoreFixture.simpleStore();
        Users users = UsersFixture.simpleUsers();
        return new Model(store, users);
    }

    public static Model simpleModelWithEmptyStore() {
        Store store = StoreFixture.emptyStore();
        Users users = UsersFixture.simpleUsers();
        return new Model(store, users);
    }
}
