package org.monet.kernel.model;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import net.sf.json.JSONObject;
import org.jdom.Element;

public class TermList extends BaseModelList<Term> {

    private String language;

    public TermList() {
        super();
        this.language = Language.getCurrent();
        this.totalCount = 0;
    }

    public TermList(int totalCount) {
        this();
        this.totalCount = totalCount;
    }

    public String getLanguage() {
        return this.language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public MonetHashMap<Term> get() {
        return this.items;
    }

    public Term get(String code) {
        if (!this.items.containsKey(code)) return null;
        return this.items.get(code);
    }

    public void add(Term term) {
        if (this.items.containsKey(term.getCode())) return;
        this.items.put(term.getCode(), term);
    }

    public Boolean setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
        return true;
    }

    public Term first() {
        Iterator<Term> iter = this.items.values().iterator();
        if (!iter.hasNext()) return null;
        return iter.next();
    }

    public TermList subList(Integer startPos, Integer limit) {
        TermList result = new TermList(this.totalCount);
        Iterator<String> iter = this.items.keySet().iterator();
        Integer pos = 0;
        Integer count = 0;
        while ((count < limit) && (iter.hasNext())) {
            String codeTerm = iter.next();
            pos++;
            if (pos >= startPos) {
                result.add(this.items.get(codeTerm));
                count++;
            }
        }
        return result;
    }

    public TermList subList(Integer startPos) {
        return this.subList(startPos, this.items.size());
    }

    public String toJSON() {
        Iterator<String> iter = this.items.keySet().iterator();
        Vector<String> resultVector = new Vector<String>();
        JSONObject result = new JSONObject();
        while (iter.hasNext()) {
            Term term = this.items.get(iter.next());
            resultVector.add(term.toJSON());
        }
        result.put("nrows", String.valueOf(this.totalCount));
        result.put("rows", resultVector.toArray());
        return result.toString();
    }

    @SuppressWarnings("unchecked")
    public Boolean unserializeFromXML(Element element) {
        List<Element> terms;
        Iterator<Element> iter;
        if (element.getAttribute("language") != null) this.language = element.getAttributeValue("language");
        this.items.clear();
        terms = element.getChildren("term");
        iter = terms.iterator();
        while (iter.hasNext()) {
            Element termElement = iter.next();
            Term term = new Term();
            term.unserializeFromXML(termElement);
            this.items.put(term.getCode(), term);
        }
        return true;
    }

    public StringBuffer serializeToXML(boolean bAddHeader) {
        Iterator<String> iter = this.items.keySet().iterator();
        StringBuffer result = new StringBuffer();
        if (bAddHeader) result.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        result.append("<term-list language=\"" + this.language + "\">");
        while (iter.hasNext()) {
            String id = iter.next();
            Term term = (Term) this.items.get(id);
            result.append(term.serializeToXML());
        }
        result.append("</term-list>");
        return result;
    }
}
