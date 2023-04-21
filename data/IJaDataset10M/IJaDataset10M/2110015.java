package referee.diary.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Masafumi Koba
 */
public class DiaryTest extends AbstractEntityTest<Diary, Long> {

    @Override
    public void testFind() {
        super.testFind();
        assertTrue(getEntity().getNullObject().sameIdentityAs(getEntity().find(Long.valueOf(-1))));
    }

    @Override
    public void testJaxb() throws Exception {
        final Diary source = new Diary();
        source.setTitle(getClass().getSimpleName());
        final Diary diary = testJaxb(source);
        assertEquals(source.getTitle(), diary.getTitle());
    }

    @Override
    public void testMerge() {
        final String title = getClass().getSimpleName();
        final Diary merge = testMerge(new SetterCallback<Diary>() {

            @Override
            public void setValues(final Diary entity) {
                entity.setTitle(title);
            }
        });
        assertEquals(title, merge.getTitle());
    }

    @Override
    public void testPersist() {
        final String title = getClass().getSimpleName();
        final Diary persist = testPersist(new SetterCallback<Diary>() {

            @Override
            public void setValues(final Diary entity) {
                entity.setTitle(title);
            }
        });
        assertEquals(title, persist.getTitle());
    }

    @Override
    protected Class<Diary> entityType() {
        return Diary.class;
    }
}
