package tests.com.scholardesk.db.mock;

import com.scholardesk.annotation.DbColumn;

public class AuthorMock extends PersonMock implements com.scholardesk.model.ModelObject {

    private String abstract_id;

    @DbColumn
    public String getAuthorMockId() {
        return getId();
    }

    @DbColumn
    public void setAuthorMockId(String author_id) {
        setId(author_id);
    }

    @DbColumn
    public String getPersonMockId() {
        return super.getPersonMockId();
    }

    @DbColumn
    public String getAbstractMockId() {
        return abstract_id;
    }

    @DbColumn
    public void setAbstractMockId(String abstract_id) {
        this.abstract_id = abstract_id;
    }
}
