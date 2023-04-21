package ro.cuzma.larry.persistance.xml.samples.sample2.objectxmlconnection;

import ro.cuzma.larry.persistance.common.Entity;
import ro.cuzma.larry.persistance.xml.XMLNode;
import ro.cuzma.larry.persistance.xml.XMLTagValue;
import ro.cuzma.larry.persistance.xml.XMLTagValueList;
import ro.cuzma.larry.persistance.xml.exception.XMLException;
import ro.cuzma.larry.persistance.xml.samples.sample2.objects.CalibreBook;

public class CalibreBookHelper extends XMLNode {

    XMLTagValue identifier;

    XMLTagValueList<Entity<String>> metas;

    XMLTagValue title;

    XMLTagValueList<Entity<String>> creator;

    XMLTagValue publishedDate;

    XMLTagValue publisher;

    XMLTagValue language;

    public CalibreBookHelper(CalibreBook calibreBook) throws XMLException {
        super(XMLTags.CALIBREBOOK, calibreBook);
    }

    public CalibreBookHelper() {
        super(XMLTags.CALIBREBOOK);
    }

    @Override
    protected void initTags() {
        addAtribute("xmlns:dc", "http://purl.org/dc/elements/1.1/");
        addAtribute("xmlns:opf", "http://www.idpf.org/2007/opf");
        identifier = new XMLTagValue("dc:identifier");
        identifier.addAtribute("opf:scheme", "ISBN");
        xmlObjects.add(identifier);
        title = new XMLTagValue("dc:title");
        xmlObjects.add(title);
        creator = new XMLTagValueList<Entity<String>>(XMLTags.CALIBREBOOK_AUTHOR, XMLTags.CALIBREBOOK_AUTHOR);
        creator.setShowMasterTag(false);
        xmlObjects.add(creator);
        publisher = new XMLTagValue("dc:publisher");
        xmlObjects.add(publisher);
        publishedDate = new XMLTagValue("dc:date");
        xmlObjects.add(publishedDate);
        language = new XMLTagValue("dc:language");
        xmlObjects.add(language);
        metas = new XMLTagValueList<Entity<String>>("metas", "meta");
        xmlObjects.add(metas);
        metas.setShowMasterTag(false);
    }

    @Override
    protected void initWithEntity(Entity<?> entity) {
        CalibreBook bk = (CalibreBook) entity;
        title.setValue(bk.getTitle());
        XMLTagValue auth;
        for (String aut : bk.getCreator()) {
            auth = new XMLTagValue(XMLTags.CALIBREBOOK_AUTHOR);
            auth.addAtribute("opf:file-as", aut);
            auth.addAtribute("opf:role", "aut");
            auth.setValue(aut);
            creator.add(auth);
        }
        identifier.setValue(bk.getIsbn());
        publisher.setValue(bk.getPublisher());
        publishedDate.setValue(bk.getDate());
        language.setValue(bk.getLanguage());
        XMLTagValue meta;
        String content;
        {
            content = bk.getSeries();
            if (content != null && !content.equals("")) {
                meta = new XMLTagValue("meta");
                metas.add(meta);
                meta.addAtribute("name", "calibre:series");
                meta.addAtribute("content", content);
            }
        }
        {
            content = bk.getSeries_index();
            if (content != null && !content.equals("")) {
                meta = new XMLTagValue("meta");
                metas.add(meta);
                meta.addAtribute("name", "calibre:series_index");
                meta.addAtribute("content", content);
            }
        }
        {
            content = bk.getRating();
            if (content != null && !content.equals("")) {
                meta = new XMLTagValue("meta");
                metas.add(meta);
                meta.addAtribute("name", "calibre:rating");
                meta.addAtribute("content", content);
            }
        }
        {
            content = bk.getBoughtDate();
            if (content != null && !content.equals("")) {
                meta = new XMLTagValue("meta");
                metas.add(meta);
                meta.addAtribute("name", "calibre:timestamp");
                meta.addAtribute("content", content);
            }
        }
        {
            content = bk.getToRead();
            if (content != null && !content.equals("")) {
                meta = new XMLTagValue("meta");
                metas.add(meta);
                meta.addAtribute("name", "calibre:user_metadata:#toread");
                meta.addAtribute("content", "{&quot;is_category&quot;: false, &quot;#extra#&quot;: null, &quot;kind&quot;: &quot;field&quot;, &quot;is_custom&quot;: true, &quot;name&quot;: &quot;Want to Read?&quot;, &quot;column&quot;: &quot;value&quot;, &quot;rec_index&quot;: 21, &quot;search_terms&quot;: [&quot;#toread&quot;], &quot;link_column&quot;: &quot;value&quot;, &quot;label&quot;: &quot;toread&quot;, &quot;is_multiple&quot;: null, &quot;datatype&quot;: &quot;bool&quot;, &quot;#value#&quot;: " + content + ", &quot;category_sort&quot;: &quot;value&quot;, &quot;table&quot;: &quot;custom_column_1&quot;, &quot;colnum&quot;: 1, &quot;is_editable&quot;: true, &quot;display&quot;: {}, &quot;is_csp&quot;: false}");
            }
        }
        {
            Long contentL = bk.getColnr();
            if (contentL != null) {
                meta = new XMLTagValue("meta");
                metas.add(meta);
                meta.addAtribute("name", "calibre:user_metadata:#publishercollectionnr");
                meta.addAtribute("content", "{&quot;is_category&quot;: false, &quot;#extra#&quot;: null, &quot;kind&quot;: &quot;field&quot;, &quot;is_custom&quot;: true, &quot;name&quot;: &quot;Collection Number&quot;, &quot;column&quot;: &quot;value&quot;, &quot;rec_index&quot;: 23, &quot;search_terms&quot;: [&quot;#publishercollectionnr&quot;], &quot;link_column&quot;: &quot;value&quot;, &quot;label&quot;: &quot;publishercollectionnr&quot;, &quot;is_multiple&quot;: null, &quot;datatype&quot;: &quot;int&quot;, &quot;#value#&quot;: " + contentL.toString() + ", &quot;category_sort&quot;: &quot;value&quot;, &quot;table&quot;: &quot;custom_column_3&quot;, &quot;colnum&quot;: 3, &quot;is_editable&quot;: true, &quot;display&quot;: {}, &quot;is_csp&quot;: false}");
            }
        }
        {
            Double contentL = bk.getPrice();
            if (contentL != null) {
                meta = new XMLTagValue("meta");
                metas.add(meta);
                meta.addAtribute("name", "calibre:user_metadata:#price");
                meta.addAtribute("content", "{&quot;is_category&quot;: false, &quot;#extra#&quot;: null, &quot;kind&quot;: &quot;field&quot;, &quot;is_custom&quot;: true, &quot;name&quot;: &quot;Price&quot;, &quot;column&quot;: &quot;value&quot;, &quot;rec_index&quot;: 24, &quot;search_terms&quot;: [&quot;#price&quot;], &quot;link_column&quot;: &quot;value&quot;, &quot;label&quot;: &quot;price&quot;, &quot;is_multiple&quot;: null, &quot;datatype&quot;: &quot;float&quot;, &quot;#value#&quot;: " + contentL.toString() + ", &quot;category_sort&quot;: &quot;value&quot;, &quot;table&quot;: &quot;custom_column_4&quot;, &quot;colnum&quot;: 4, &quot;is_editable&quot;: true, &quot;display&quot;: {}, &quot;is_csp&quot;: false}");
            }
        }
        {
            content = bk.getCollection();
            if (content != null && !content.equals("")) {
                meta = new XMLTagValue("meta");
                metas.add(meta);
                meta.addAtribute("name", "calibre:user_metadata:#publishercollection");
                meta.addAtribute("content", "{&quot;is_category&quot;: true, &quot;#extra#&quot;: null, &quot;kind&quot;: &quot;field&quot;, &quot;is_custom&quot;: true, &quot;name&quot;: &quot;Collection&quot;, &quot;column&quot;: &quot;value&quot;, &quot;rec_index&quot;: 25, &quot;search_terms&quot;: [&quot;#publishercollection&quot;], &quot;link_column&quot;: &quot;value&quot;, &quot;label&quot;: &quot;publishercollection&quot;, &quot;is_multiple&quot;: null, &quot;datatype&quot;: &quot;text&quot;, &quot;#value#&quot;: &quot;" + content + "&quot;, &quot;category_sort&quot;: &quot;value&quot;, &quot;table&quot;: &quot;custom_column_5&quot;, &quot;colnum&quot;: 5, &quot;is_editable&quot;: true, &quot;display&quot;: {&quot;use_decorations&quot;: 0}, &quot;is_csp&quot;: false}");
            }
        }
        {
            content = bk.getType();
            if (content != null && !content.equals("")) {
                meta = new XMLTagValue("meta");
                metas.add(meta);
                meta.addAtribute("name", "calibre:user_metadata:#type");
                meta.addAtribute("content", "{&quot;is_category&quot;: true, &quot;#extra#&quot;: null, &quot;kind&quot;: &quot;field&quot;, &quot;is_custom&quot;: true, &quot;name&quot;: &quot;Type&quot;, &quot;column&quot;: &quot;value&quot;, &quot;rec_index&quot;: 27, &quot;search_terms&quot;: [&quot;#type&quot;], &quot;link_column&quot;: &quot;value&quot;, &quot;label&quot;: &quot;type&quot;, &quot;is_multiple&quot;: null, &quot;datatype&quot;: &quot;text&quot;, &quot;#value#&quot;: &quot;" + content + "&quot;, &quot;category_sort&quot;: &quot;value&quot;, &quot;table&quot;: &quot;custom_column_7&quot;, &quot;colnum&quot;: 7, &quot;is_editable&quot;: true, &quot;display&quot;: {&quot;use_decorations&quot;: 0}, &quot;is_csp&quot;: false}");
            }
        }
        {
            Long contentL = bk.getReadNr();
            if (contentL != null) {
                meta = new XMLTagValue("meta");
                metas.add(meta);
                meta.addAtribute("name", "calibre:user_metadata:#read");
                meta.addAtribute("content", "{&quot;is_category&quot;: false, &quot;#extra#&quot;: null, &quot;kind&quot;: &quot;field&quot;, &quot;is_custom&quot;: true, &quot;name&quot;: &quot;Read&quot;, &quot;column&quot;: &quot;value&quot;, &quot;rec_index&quot;: 22, &quot;search_terms&quot;: [&quot;#read&quot;], &quot;link_column&quot;: &quot;value&quot;, &quot;label&quot;: &quot;read&quot;, &quot;is_multiple&quot;: null, &quot;datatype&quot;: &quot;int&quot;, &quot;#value#&quot;: " + contentL.toString() + ", &quot;category_sort&quot;: &quot;value&quot;, &quot;table&quot;: &quot;custom_column_2&quot;, &quot;colnum&quot;: 2, &quot;is_editable&quot;: true, &quot;display&quot;: {}, &quot;is_csp&quot;: false}");
            }
        }
        {
            content = bk.getLanguage();
            if (content != null && !content.equals("")) {
                meta = new XMLTagValue("meta");
                metas.add(meta);
                meta.addAtribute("name", "calibre:user_metadata:#language");
                meta.addAtribute("content", "{&quot;is_category&quot;: true, &quot;#extra#&quot;: null, &quot;kind&quot;: &quot;field&quot;, &quot;is_custom&quot;: true, &quot;name&quot;: &quot;Language&quot;, &quot;column&quot;: &quot;value&quot;, &quot;rec_index&quot;: 28, &quot;search_terms&quot;: [&quot;#language&quot;], &quot;link_column&quot;: &quot;value&quot;, &quot;label&quot;: &quot;language&quot;, &quot;is_multiple&quot;: null, &quot;datatype&quot;: &quot;text&quot;, &quot;#value#&quot;: &quot;" + content + "&quot;, &quot;category_sort&quot;: &quot;value&quot;, &quot;table&quot;: &quot;custom_column_8&quot;, &quot;colnum&quot;: 8, &quot;is_editable&quot;: true, &quot;display&quot;: {&quot;use_decorations&quot;: 0}, &quot;is_csp&quot;: false}");
            }
        }
        {
            content = bk.getCurrency();
            if (content != null && !content.equals("")) {
                meta = new XMLTagValue("meta");
                metas.add(meta);
                meta.addAtribute("name", "calibre:user_metadata:#currency");
                meta.addAtribute("content", "{&quot;is_category&quot;: true, &quot;#extra#&quot;: null, &quot;kind&quot;: &quot;field&quot;, &quot;is_custom&quot;: true, &quot;name&quot;: &quot;Currency&quot;, &quot;column&quot;: &quot;value&quot;, &quot;rec_index&quot;: 29, &quot;search_terms&quot;: [&quot;#currency&quot;], &quot;link_column&quot;: &quot;value&quot;, &quot;label&quot;: &quot;currency&quot;, &quot;is_multiple&quot;: null, &quot;datatype&quot;: &quot;text&quot;, &quot;#value#&quot;: &quot;" + content + "&quot;, &quot;category_sort&quot;: &quot;value&quot;, &quot;table&quot;: &quot;custom_column_9&quot;, &quot;colnum&quot;: 9, &quot;is_editable&quot;: true, &quot;display&quot;: {&quot;use_decorations&quot;: 0}, &quot;is_csp&quot;: false}");
            }
        }
        {
            content = bk.getOriginalTitle();
            if (content != null && !content.equals("")) {
                meta = new XMLTagValue("meta");
                metas.add(meta);
                meta.addAtribute("name", "calibre:originaltitle");
                meta.addAtribute("content", "{&quot;is_category&quot;: false, &quot;#extra#&quot;: null, &quot;kind&quot;: &quot;field&quot;, &quot;is_custom&quot;: true, &quot;name&quot;: &quot;Original Title&quot;, &quot;column&quot;: &quot;value&quot;, &quot;rec_index&quot;: 29, &quot;search_terms&quot;: [&quot;#originaltitle&quot;], &quot;link_column&quot;: &quot;value&quot;, &quot;label&quot;: &quot;originaltitle&quot;, &quot;is_multiple&quot;: null, &quot;datatype&quot;: &quot;comments&quot;, &quot;#value#&quot;: &quot;" + content + "&quot;, &quot;category_sort&quot;: &quot;value&quot;, &quot;table&quot;: &quot;custom_column_11&quot;, &quot;colnum&quot;: 11, &quot;is_editable&quot;: true, &quot;display&quot;: {}, &quot;is_csp&quot;: false}");
            }
        }
        {
            content = bk.getDigitalBook();
            if (content != null && !content.equals("")) {
                meta = new XMLTagValue("meta");
                metas.add(meta);
                meta.addAtribute("name", "calibre:user_metadata:#digitalbook");
                meta.addAtribute("content", "{&quot;is_category&quot;: false, &quot;#extra#&quot;: null, &quot;kind&quot;: &quot;field&quot;, &quot;is_custom&quot;: true, &quot;name&quot;: &quot;Digital Book?&quot;, &quot;column&quot;: &quot;value&quot;, &quot;rec_index&quot;: 30, &quot;search_terms&quot;: [&quot;#digitalbook&quot;], &quot;link_column&quot;: &quot;value&quot;, &quot;label&quot;: &quot;digitalbook&quot;, &quot;is_multiple&quot;: null, &quot;datatype&quot;: &quot;bool&quot;, &quot;#value#&quot;: " + content + ", &quot;category_sort&quot;: &quot;value&quot;, &quot;table&quot;: &quot;custom_column_13&quot;, &quot;colnum&quot;: 13, &quot;is_editable&quot;: true, &quot;display&quot;: {}, &quot;is_csp&quot;: false}");
            }
        }
    }
}
