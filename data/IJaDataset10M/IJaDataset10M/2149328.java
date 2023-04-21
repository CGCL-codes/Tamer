package com.idna.trace.searchrulesengine.rulesengine.rules;

import com.idna.trace.utils.SearchTypeContainer;

public interface Rules<K, V> {

    public void execute(K idea, V parameters, SearchTypeContainer searchTypeContainer);
}
