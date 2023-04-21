package net.sf.josceleton.core.api.entity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import net.sf.josceleton.commons.exception.InvalidArgumentException;
import net.sf.josceleton.commons.test.AbstractEqualsTest;
import net.sf.josceleton.commons.test.EqualsDescriptor;
import net.sf.josceleton.commons.test.util.TestUtil;
import net.sf.josceleton.core.api.entity.user.User;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@SuppressWarnings("boxing")
public abstract class UserTest extends AbstractEqualsTest<User> {

    protected abstract User createTestee(int uniqueId, int osceletonId, int color);

    @Override
    protected final Object createSameTesteeForEquals() {
        return this.createTestee(42, 21, 12);
    }

    @Override
    protected final EqualsDescriptor<User> createEqualsDescriptor() {
        final User sameA = this.createTestee(1, 1, 0xFF0000);
        final User sameB = this.createTestee(1, 1, 0xFF0000);
        final User differentX = this.createTestee(2, 1, 0xFF0000);
        final User differentY = this.createTestee(1, 2, 0xFF0000);
        final User differentZ = this.createTestee(1, 1, 0x0000FF);
        return new EqualsDescriptor<User>(sameA, sameB, differentX, differentY, differentZ);
    }

    @DataProvider(name = "provideIllegalConstructorArguments")
    public final Object[][] provideIllegalConstructorArguments() {
        return new Object[][] { new Object[] { 1, 0 }, new Object[] { 1, -1 }, new Object[] { 0, 1 }, new Object[] { -1, 1 } };
    }

    @Test(expectedExceptions = InvalidArgumentException.class, dataProvider = "provideIllegalConstructorArguments")
    public final void passingIllegalConstructorArgumentsFails(final int uniqueId, final int osceletonId) {
        this.createTestee(uniqueId, osceletonId, 0x000000);
    }

    @DataProvider(name = "provideValidConstructorArguments")
    public final Object[][] provideValidConstructorArguments() {
        return new Object[][] { new Object[] { 1, 1, 0xFF0000 }, new Object[] { 2, 1, 0x00FF00 }, new Object[] { 2, 4, 0x0000FFF }, new Object[] { 10, 10, 0xFFCC00 }, new Object[] { 42, 21, 0x00FFCC } };
    }

    @Test(dataProvider = "provideValidConstructorArguments")
    public final void passingValidConstructorArgumentsCheckGetters(final int uniqueId, final int osceletonId, final int color) {
        final User actualUser = this.createTestee(uniqueId, osceletonId, color);
        assertThat(actualUser.getUniqueId(), equalTo(uniqueId));
        assertThat(actualUser.getOsceletonId(), equalTo(osceletonId));
    }

    @Test
    public final void someEqualsTests() {
        assertThat(this.createTestee(1, 1, 0), not(equalTo(null)));
        assertThat(this.createTestee(1, 1, 0), equalTo(this.createTestee(1, 1, 0)));
        assertThat(this.createTestee(1, 1, 0), not(equalTo(this.createTestee(2, 2, 0))));
        assertThat(this.createTestee(1, 1, 0), not(equalTo(this.createTestee(2, 1, 0))));
    }

    @Test
    public final void equalsTostring() {
        final User u1 = this.createTestee(2, 1, 375);
        TestUtil.assertObjectToString(u1, "2", "1", "375");
    }
}
