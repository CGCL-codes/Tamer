package com.netx.config.R1;

import java.util.List;
import java.util.ArrayList;

public class SymbolContext {

    public String pName;

    public final List<SymbolContext> pSubContexts = new ArrayList<SymbolContext>();

    public final List<SymbolProperty> pProperties = new ArrayList<SymbolProperty>();

    public Context aCtx;
}
