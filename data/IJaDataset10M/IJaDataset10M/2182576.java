package org.vardb.util.tags;

import java.util.ArrayList;
import java.util.List;
import org.codehaus.jackson.annotate.JsonProperty;
import org.vardb.util.dao.CDataFrame;
import org.vardb.util.dao.CDataType;
import org.vardb.util.dao.CDataFrame.Row;
import org.vardb.util.tags.dao.CAttributeType;
import org.vardb.util.tags.dao.CTagQueryParams;
import org.vardb.util.tags.dao.CTagType;

public class CTagDataFrame extends CDataFrame {

    protected MetaData metaData = new MetaData();

    protected Integer totalCount;

    public CTagDataFrame() {
    }

    public CTagDataFrame(CTagType tagtype) {
        super(true);
        addTagFields();
        for (CAttributeType atttype : tagtype.getAttributeTypes()) {
            metaData.addField(new Field(atttype));
        }
    }

    public CTagDataFrame(CTagType tagtype, CTagQueryParams params) {
        super(true);
        addTagFields();
        for (String attribute : params.getAttributes()) {
            CAttributeType atttype = tagtype.getAttributeType(attribute);
            metaData.addField(new Field(atttype));
        }
    }

    private void addTagFields() {
        metaData.addField(new Field("tag_id", CDataType.INTEGER, "tag ID"));
        metaData.addField(new Field("tag_name", CDataType.STRING, "tag name"));
        metaData.addField(new Field("tag_description", CDataType.STRING, "tag description"));
        metaData.addField(new Field("tag_numitems", CDataType.INTEGER, "number of items with this tag"));
    }

    @JsonProperty
    public MetaData getMetaData() {
        return metaData;
    }

    @JsonProperty
    public Integer getTotalCount() {
        return this.totalCount;
    }

    public void setTotalCount(final Integer totalCount) {
        this.totalCount = totalCount;
    }

    public static class MetaData {

        protected String totalProperty = "totalCount";

        protected String root = "rows";

        protected String idProperty = "tag_id";

        protected List<Field> fields = new ArrayList<Field>();

        public void addField(Field field) {
            this.fields.add(field);
        }

        @JsonProperty
        public String getTotalProperty() {
            return this.totalProperty;
        }

        @JsonProperty
        public String getRoot() {
            return this.root;
        }

        @JsonProperty
        public String getIdProperty() {
            return this.idProperty;
        }

        @JsonProperty
        public List<Field> getFields() {
            return this.fields;
        }
    }

    public static class Field {

        protected String name;

        protected String type = CDataType.STRING.getJson();

        protected String description;

        public Field(String name, CDataType type, String description) {
            this.name = name;
            this.type = type.getJson();
            this.description = description;
        }

        public Field(CAttributeType atttype) {
            this.name = atttype.getName();
            this.type = atttype.getType().getJson();
            this.description = atttype.getDescription();
        }

        @JsonProperty
        public String getName() {
            return this.name;
        }

        @JsonProperty
        public String getType() {
            return this.type;
        }

        @JsonProperty
        public String getDescription() {
            return this.description;
        }
    }
}
