package com.wrupple.muba.catalogs.client.module.services.presentation.impl.JavaScriptAdapter;

import java.util.List;
import com.wrupple.muba.catalogs.client.widgets.fields.cells.JSOAdapterCell.JSOAdapter;

public class StringListJSOadapter extends ListJSOadapter<String> implements JSOAdapter<List<String>> {

    public StringListJSOadapter() {
        super(StringJSOadapter.getInstance());
    }
}
