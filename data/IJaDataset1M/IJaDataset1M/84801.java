package com.google.code.ibear.where;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import com.google.code.ibear.select.config.ISelectConfig;
import com.google.code.ibear.select.config.ISelectConfigs;
import com.google.code.ibear.select.config.SelectConfig;
import com.google.code.ibear.select.config.SelectConfigs;

public class XmlWhereBuilderTest {

    XmlWhereBuilder builder = new XmlWhereBuilder();

    @Test
    public void testWhere() throws Exception {
        ISelectConfigs selectConfigs = new SelectConfigs();
        ISelectConfig iconfig = new SelectConfig();
        SelectConfig config = (SelectConfig) iconfig;
        config.setSqlID("first");
        List<String> Ncondition = new ArrayList<String>();
        Ncondition.add(" and a.field=? ");
        Ncondition.add(" and b.field=? ");
        config.setWhereConditions(Ncondition);
        selectConfigs.addOne(iconfig);
        builder.setSelectConfigs(selectConfigs);
        String wherecondition = builder.getWhereConditions("first").toString();
    }
}
