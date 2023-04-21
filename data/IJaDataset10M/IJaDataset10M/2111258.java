package tests.com.scholardesk.db.mock;

import com.scholardesk.db.DbMapper;

public class AbstractMockMapper extends DbMapper {

    public AbstractMockMapper() {
        super(AbstractMock.class);
    }

    public AbstractMock find(String _id) {
        return (AbstractMock) super.find(_id);
    }
}
