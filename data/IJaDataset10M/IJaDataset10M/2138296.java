package org.fest.swingx.fixture;

import java.util.Date;
import org.fest.swing.core.KeyPressInfo;
import org.fest.swing.core.MouseButton;
import org.fest.swing.core.MouseClickInfo;
import org.fest.swing.core.Robot;
import org.fest.swing.fixture.CommonComponentFixture;
import org.fest.swing.fixture.EditableComponentFixture;
import org.fest.swing.fixture.JPanelFixture;
import org.fest.swing.fixture.JPopupMenuInvokerFixture;
import org.fest.swing.fixture.JTextComponentFixture;
import org.fest.swing.timing.Timeout;
import org.fest.swingx.driver.JXDatePickerDriver;
import org.jdesktop.swingx.JXDatePicker;

/**
 * Understands simulation of user events on a <code>{@link JXDatePicker}</code>
 * and verification of the state of such <code>{@link JXDatePicker}</code>.
 * <p>
 * The conversion between the values given in tests and the values being
 * displayed by a <code>{@link JXDatePicker}</code> renderer is performed by a
 * <code>{@link JXDatePickerCellReader}</code>. This fixture uses a
 * <code>{@link JXDatePickerCellReader}</code> by default.
 * </p>
 *
 *
 * @author Alex Ruiz
 * @author Frederic Gedin
 *
 */
public class JXDatePickerFixture extends JPopupMenuInvokerFixture<JXDatePicker> implements CommonComponentFixture, EditableComponentFixture {

    /** Driver. */
    private JXDatePickerDriver driver;

    /**
     * Creates a new <code>{@link JXDatePickerFixture}</code>.
     *
     * @param robot
     *            performs simulation of user events on the given
     *            <code>JXDatePicker</code>.
     * @param target
     *            the <code>JXDatePicker</code> to be managed by this fixture.
     */
    public JXDatePickerFixture(final Robot robot, final JXDatePicker target) {
        super(robot, target);
        createDriver();
    }

    /**
     * Creates a new <code>{@link JXDatePickerFixture}</code>.
     *
     * @param robot
     *            performs simulation of user events on a
     *            <code>JXFatePicker</code>.
     * @param name
     *            the name of the <code>JXFatePicker</code> to find using the
     *            given <code>Robot</code>.
     */
    public JXDatePickerFixture(final Robot robot, final String name) {
        super(robot, name, JXDatePicker.class);
        createDriver();
    }

    /**
     * Create the driver.
     */
    private void createDriver() {
        updateDriver(new JXDatePickerDriver(robot));
    }

    /**
     * Update the driver.
     *
     * @param newDriver
     *            new driver.
     */
    final void updateDriver(final JXDatePickerDriver newDriver) {
        driver = newDriver;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final JXDatePickerFixture focus() {
        driver.focus(target);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final JXDatePickerFixture pressAndReleaseKey(final KeyPressInfo keyPressInfo) {
        driver.pressAndReleaseKey(target, keyPressInfo);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final JXDatePickerFixture pressAndReleaseKeys(final int... keyCodes) {
        driver.pressAndReleaseKeys(target, keyCodes);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final JXDatePickerFixture pressKey(final int keyCode) {
        driver.pressKey(target, keyCode);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final JXDatePickerFixture releaseKey(final int keyCode) {
        driver.releaseKey(target, keyCode);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final JXDatePickerFixture click() {
        driver.click(target);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final JXDatePickerFixture click(final MouseButton button) {
        driver.click(target, button);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final JXDatePickerFixture click(final MouseClickInfo mouseClickInfo) {
        driver.click(target, mouseClickInfo);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final JXDatePickerFixture doubleClick() {
        driver.doubleClick(target);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final JXDatePickerFixture rightClick() {
        driver.rightClick(target);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final JXDatePickerFixture requireDisabled() {
        driver.requireDisabled(target);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final JXDatePickerFixture requireEnabled() {
        driver.requireEnabled(target);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final JXDatePickerFixture requireEnabled(final Timeout timeout) {
        driver.requireEnabled(target, timeout);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final JXDatePickerFixture requireNotVisible() {
        driver.requireNotVisible(target);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final JXDatePickerFixture requireVisible() {
        driver.requireVisible(target);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final JXDatePickerFixture requireEditable() {
        driver.requireEditable(target);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final JXDatePickerFixture requireNotEditable() {
        driver.requireNotEditable(target);
        return this;
    }

    /**
     * Returns the <code>String</code> representation of the elements in this
     * fixture's <code>{@link JXDatePicker}</code>, using this fixture's
     * <code>{@link JXDatePickerCellReader}</code>.
     *
     * @return the <code>String</code> representation of the elements in this
     *         fixture's <code>JXDatePicker</code>.
     * @see #cellReader(JXDatePickerCellReader)
     */
    public final String[] contents() {
        return driver.contentsOf(target);
    }

    /**
     * Simulates a user entering the specified text in the
     * <code>{@link JXDatePicker}</code>, replacing any text. This action is
     * executed only if the <code>{@link JXDatePicker}</code> is editable.
     *
     * @param text
     *            the text to enter.
     * @return this fixture.
     */
    public final JXDatePickerFixture replaceText(final String text) {
        driver.replaceText(target, text);
        return this;
    }

    /**
     * Simulates a user selecting the text in the
     * <code>{@link JXDatePicker}</code>. This action is executed only if the
     * <code>{@link JXDatePicker}</code> is editable.
     *
     * @return this fixture.
     */
    public final JXDatePickerFixture selectAllText() {
        driver.selectAllText(target);
        return this;
    }

    /**
     * Simulates a user entering the specified text in this fixture's
     * <code>{@link JXDatePicker}</code>. This action is executed only if the
     * <code>{@link JXDatePicker}</code> is editable.
     *
     * @param text
     *            the text to enter.
     * @return this fixture.
     */
    public final JXDatePickerFixture enterText(final String text) {
        driver.enterText(target, text);
        return this;
    }

    /**
     * Finds and returns the {@link JXMonthViewFixture} in the pop-up raised by
     * this fixture's <code>{@link JXDatePicker}</code>.
     *
     * @return the <code>JXMonthViewFixture</code> in the pop-up raised by
     *         this fixture's <code>JXDatePicker</code>.
     */
    public final JXMonthViewFixture monthView() {
        return new JXMonthViewFixture(robot, target.getMonthView());
    }

    /**
     * Simulates a user selecting a date in this fixture's
     * <code>{@link JXDatePicker}</code>.
     *
     * @param date
     *            the date to select.
     * @return this fixture.
     */
    public final JXDatePickerFixture selectDate(final Date date) {
        driver.selectDate(target, date);
        return this;
    }

    /**
     * Verifies that this fixture's <code>{@link JXDatePicker}</code> has a
     * selected date corresponding to the given value.
     *
     * @param date
     *            value to check
     * @return this fixture.
     */
    public final JXDatePickerFixture requireSelectedDate(final Date date) {
        driver.requireSelectedDate(target, date);
        return this;
    }

    /**
     * Verifies that this fixture's <code>{@link JXDatePicker}</code> does not
     * have any selected date.
     *
     * @return this fixture.
     */
    public final JXDatePickerFixture requireNoSelectedDate() {
        driver.requireNoSelectedDate(target);
        return this;
    }

    /**
     * Updates the implementation of <code>{@link JXDatePickerCellReader}</code>
     * to use when comparing internal values of this fixture's
     * <code>{@link JXDatePicker}</code> and the values expected in a test.
     * The default implementation to use is
     * <code>{@link BasicJXDatePickerCellReader}</code>.
     *
     * @param cellReader
     *            the new <code>JXDatePickerCellValueReader</code> to use.
     * @return this fixture.
     */
    public final JXDatePickerFixture cellReader(final JXDatePickerCellReader cellReader) {
        driver.cellReader(cellReader);
        return this;
    }

    /**
     * @return the editor fixture
     */
    public final JTextComponentFixture editor() {
        return new JTextComponentFixture(robot, target.getEditor());
    }

    /**
     * @return the link panel associated with the date picker
     */
    public final JPanelFixture linkPanel() {
        return new JPanelFixture(robot, target.getLinkPanel());
    }
}
