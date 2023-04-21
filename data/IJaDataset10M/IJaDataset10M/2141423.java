package common;

import org.hibernate.Criteria;
import org.orm.PersistentException;
import org.orm.PersistentSession;
import org.orm.criteria.*;

public class ChapterCriteria extends AbstractORMCriteria {

    public final IntegerExpression ID;

    public final StringExpression ChapterName;

    public final IntegerExpression Page;

    public ChapterCriteria(Criteria criteria) {
        super(criteria);
        ID = new IntegerExpression("ID", this);
        ChapterName = new StringExpression("ChapterName", this);
        Page = new IntegerExpression("Page", this);
    }

    public ChapterCriteria(PersistentSession session) {
        this(session.createCriteria(Chapter.class));
    }

    public ChapterCriteria() throws PersistentException {
        this(common.GoodReading3PersistentManager.instance().getSession());
    }

    public BookCriteria createBookCriteria() {
        return new BookCriteria(createCriteria("book"));
    }

    public Chapter uniqueChapter() {
        return (Chapter) super.uniqueResult();
    }

    public Chapter[] listChapter() {
        java.util.List list = super.list();
        return (Chapter[]) list.toArray(new Chapter[list.size()]);
    }
}
