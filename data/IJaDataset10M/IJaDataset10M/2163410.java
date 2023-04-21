package com.iona.json.builders;

import java.util.List;
import com.iona.domain.Authors;
import com.iona.json.JSONArray;
import com.iona.json.JSONBuilder;
import com.iona.json.JSONException;

public class AuthorsBuilder extends JSONBuilder {

    private final List<Authors> author;

    public AuthorsBuilder(List<Authors> findAll) {
        this.author = findAll;
    }

    @Override
    public Object build() throws JSONException {
        JSONArray root = new JSONArray();
        for (Authors au : author) {
            JSONArray arr = new JSONArray();
            arr.put(au.getId());
            arr.put(au.getName());
            root.put(arr);
        }
        return root;
    }

    @Override
    public String name() {
        return getClass().getSimpleName();
    }
}
