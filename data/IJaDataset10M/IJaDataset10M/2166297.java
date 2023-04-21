package hu.schmidtsoft.parser.language.impl;

import hu.schmidtsoft.parser.language.EType;
import hu.schmidtsoft.parser.language.ILanguage;
import hu.schmidtsoft.parser.language.ITerm;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public abstract class Term implements ITerm {

    int id;

    String name;

    List<String> subsStr = new ArrayList<String>();

    public List<String> getSubsStr() {
        return subsStr;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Term(String name) {
        super();
        this.name = name;
    }

    public abstract EType getType();

    @Override
    public String toString() {
        return ": " + getType();
    }

    @Override
    public void toString(StringWriter wri, String prefix) {
        wri.append(prefix + getName() + ": " + toString());
    }

    ILanguage language;

    public ITerm setLanguage(ILanguage language) {
        this.language = language;
        return this;
    }

    public ILanguage getLanguage() {
        return language;
    }

    @Override
    public boolean isFiltered() {
        boolean ret = false;
        if (EType.token.equals(getType())) {
            ret = language.getTokenFilterDef().getToFilter().contains(((TermToken) this).getTokenType().getName());
        }
        if (!ret) {
            ret = !language.getTermFilterDef().getRemainingTerms().contains(getName());
        }
        return ret;
    }
}
