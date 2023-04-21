package com.modelmetrics.cloudconverter.engine;

import java.util.List;

public interface PicklistProvider {

    public List<String> getPicklistValues() throws Exception;
}
