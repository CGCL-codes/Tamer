package com.netx.data;

import com.netx.generics.translation.Results;
import com.netx.generics.translation.TranslationStep;

public class DatabaseChecker extends TranslationStep {

    public DatabaseChecker(DatabaseAnalyzer analyzer) {
        super(analyzer);
    }

    public Object performWork(Results r) {
        return r.getContents();
    }
}
