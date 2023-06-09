package org.fest.swing.fixture;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import org.fest.swing.core.*;
import org.fest.swing.driver.FrameDriver;
import org.fest.swing.exception.ActionFailedException;
import org.fest.swing.exception.ComponentLookupException;
import org.fest.swing.exception.WaitTimedOutError;
import org.fest.swing.timing.Timeout;

/**
 * Understands simulation of user events on a <code>{@link Frame}</code> and verification of the state of such
 * <code>{@link Frame}</code>.
 *
 * @author Yvonne Wang
 * @author Alex Ruiz
 */
public class FrameFixture extends WindowFixture<Frame> implements FrameLikeFixture {

    private FrameDriver driver;

    /**
   * Creates a new <code>{@link FrameFixture}</code>. This constructor creates a new <code>{@link Robot}</code>
   * containing the current AWT hierarchy.
   * @param target the <code>Frame</code> to be managed by this fixture.
   * @throws NullPointerException if the given frame is <code>null</code>.
   * @see BasicRobot#robotWithCurrentAwtHierarchy()
   */
    public FrameFixture(Frame target) {
        super(target);
        createDriver();
    }

    /**
   * Creates a new <code>{@link FrameFixture}</code>.
   * @param robot performs user events on the given window and verifies expected output.
   * @param target the <code>Frame</code> to be managed by this fixture.
   * @throws NullPointerException if the given robot is <code>null</code>.
   * @throws NullPointerException if the given frame is <code>null</code>.
   */
    public FrameFixture(Robot robot, Frame target) {
        super(robot, target);
        createDriver();
    }

    /**
   * Creates a new <code>{@link FrameFixture}</code>.
   * @param robot performs user events on the given window and verifies expected output.
   * @param name the name of the <code>Frame</code> to find using the given <code>Robot</code>.
   * @throws NullPointerException if the given robot is <code>null</code>.
   * @throws ComponentLookupException if a <code>Frame</code> having a matching name could not be found.
   * @throws ComponentLookupException if more than one <code>Frame</code> having a matching name is found.
   */
    public FrameFixture(Robot robot, String name) {
        super(robot, name, Frame.class);
        createDriver();
    }

    /**
   * Creates a new <code>{@link FrameFixture}</code>. This constructor creates a new <code>{@link Robot}</code>
   * containing the current AWT hierarchy.
   * @param name the name of the <code>Frame</code> to find.
   * @throws ComponentLookupException if a <code>Frame</code> having a matching name could not be found.
   * @throws ComponentLookupException if more than one <code>Frame</code> having a matching name is found.
   */
    public FrameFixture(String name) {
        super(name, Frame.class);
        createDriver();
    }

    private void createDriver() {
        updateDriver(new FrameDriver(robot));
    }

    void updateDriver(FrameDriver newDriver) {
        driver = newDriver;
    }

    /**
   * Simulates a user clicking this fixture's <code>{@link Frame}</code>.
   * @return this fixture.
   */
    public FrameFixture click() {
        driver.click(target);
        return this;
    }

    /**
   * Simulates a user clicking this fixture's <code>{@link Frame}</code>.
   * @param button the button to click.
   * @return this fixture.
   */
    public FrameFixture click(MouseButton button) {
        driver.click(target, button);
        return this;
    }

    /**
   * Simulates a user clicking this fixture's <code>{@link Frame}</code>.
   * @param mouseClickInfo specifies the button to click and the times the button should be clicked.
   * @return this fixture.
   * @throws NullPointerException if the given <code>MouseClickInfo</code> is <code>null</code>.
   */
    public FrameFixture click(MouseClickInfo mouseClickInfo) {
        driver.click(target, mouseClickInfo);
        return this;
    }

    /**
   * Simulates a user double-clicking this fixture's <code>{@link Frame}</code>.
   * @return this fixture.
   */
    public FrameFixture doubleClick() {
        driver.doubleClick(target);
        return this;
    }

    /**
   * Simulates a user right-clicking this fixture's <code>{@link Frame}</code>.
   * @return this fixture.
   */
    public FrameFixture rightClick() {
        driver.rightClick(target);
        return this;
    }

    /**
   * Simulates a user iconifying this fixture's <code>{@link Frame}</code>.
   * @return this fixture.
   */
    public FrameFixture iconify() {
        driver.iconify(target);
        return this;
    }

    /**
   * Simulates a user deiconifying this fixture's <code>{@link Frame}</code>.
   * @return this fixture.
   */
    public FrameFixture deiconify() {
        driver.deiconify(target);
        return this;
    }

    /**
   * Simulates a user maximizing this fixture's <code>{@link Frame}</code>.
   * @return this fixture.
   * @throws ActionFailedException if the operating system does not support maximizing frames.
   */
    public FrameFixture maximize() {
        driver.maximize(target);
        return this;
    }

    /**
   * Simulates a user normalizing this fixture's <code>{@link Frame}</code>.
   * @return this fixture.
   */
    public FrameFixture normalize() {
        driver.normalize(target);
        return this;
    }

    /**
   * Gives input focus to this fixture's <code>{@link Frame}</code>.
   * @return this fixture.
   */
    public FrameFixture focus() {
        driver.focus(target);
        return this;
    }

    /**
   * Simulates a user moving this fixture's <code>{@link Frame}</code> to the given point.
   * @param p the point to move this fixture's <code>Frame</code> to.
   * @return this fixture.
   */
    public FrameFixture moveTo(Point p) {
        driver.moveTo(target, p);
        return this;
    }

    /**
   * Simulates a user pressing given key with the given modifiers on this fixture's <code>{@link Frame}</code>.
   * Modifiers is a mask from the available <code>{@link java.awt.event.InputEvent}</code> masks.
   * @param keyPressInfo specifies the key and modifiers to press.
   * @return this fixture.
   * @throws NullPointerException if the given <code>KeyPressInfo</code> is <code>null</code>.
   * @throws IllegalArgumentException if the given code is not a valid key code.
   * @see KeyPressInfo
   */
    public FrameFixture pressAndReleaseKey(KeyPressInfo keyPressInfo) {
        driver.pressAndReleaseKey(target, keyPressInfo);
        return this;
    }

    /**
   * Simulates a user pressing and releasing the given keys on this fixture's <code>{@link Frame}</code>.
   * @param keyCodes one or more codes of the keys to press.
   * @return this fixture.
   * @throws NullPointerException if the given array of codes is <code>null</code>.
   * @throws IllegalArgumentException if any of the given code is not a valid key code.
   * @see java.awt.event.KeyEvent
   */
    public FrameFixture pressAndReleaseKeys(int... keyCodes) {
        driver.pressAndReleaseKeys(target, keyCodes);
        return this;
    }

    /**
   * Simulates a user pressing the given key on this fixture's <code>{@link Frame}</code>.
   * @param keyCode the code of the key to press.
   * @return this fixture.
   * @throws IllegalArgumentException if any of the given code is not a valid key code.
   * @see java.awt.event.KeyEvent
   */
    public FrameFixture pressKey(int keyCode) {
        driver.pressKey(target, keyCode);
        return this;
    }

    /**
   * Simulates a user releasing the given key on this fixture's <code>{@link Frame}</code>.
   * @param keyCode the code of the key to release.
   * @return this fixture.
   * @throws IllegalArgumentException if any of the given code is not a valid key code.
   * @see java.awt.event.KeyEvent
   */
    public FrameFixture releaseKey(int keyCode) {
        driver.releaseKey(target, keyCode);
        return this;
    }

    /**
   * Asserts that this fixture's <code>{@link Frame}</code> has input focus.
   * @return this fixture.
   * @throws AssertionError if this fixture's <code>Frame</code> does not have input focus.
   */
    public FrameFixture requireFocused() {
        driver.requireFocused(target);
        return this;
    }

    /**
   * Asserts that this fixture's <code>{@link Frame}</code> is disabled.
   * @return this fixture.
   * @throws AssertionError if this fixture's <code>Frame</code> is enabled.
   */
    public FrameFixture requireDisabled() {
        driver.requireDisabled(target);
        return this;
    }

    /**
   * Asserts that this fixture's <code>{@link Frame}</code> is enabled.
   * @return this fixture.
   * @throws AssertionError if this fixture's <code>Frame</code> is disabled.
   */
    public FrameFixture requireEnabled() {
        driver.requireEnabled(target);
        return this;
    }

    /**
   * Asserts that this fixture's <code>{@link Frame}</code> is enabled.
   * @param timeout the time this fixture will wait for the component to be enabled.
   * @return this fixture.
   * @throws WaitTimedOutError if this fixture's <code>Frame</code> is never enabled.
   */
    public FrameFixture requireEnabled(Timeout timeout) {
        driver.requireEnabled(target, timeout);
        return this;
    }

    /**
   * Asserts that this fixture's <code>{@link Frame}</code> is not visible.
   * @return this fixture.
   * @throws AssertionError if this fixture's <code>Frame</code> is visible.
   */
    public FrameFixture requireNotVisible() {
        driver.requireNotVisible(target);
        return this;
    }

    /**
   * Asserts that the size of this fixture's <code>{@link Frame}</code> is equal to given one.
   * @param size the given size to match.
   * @return this fixture.
   * @throws AssertionError if the size of this fixture's <code>Frame</code> is not equal to the given size.
   */
    public FrameFixture requireSize(Dimension size) {
        driver.requireSize(target, size);
        return this;
    }

    /**
   * Asserts that this fixture's <code>{@link Frame}</code> is visible.
   * @return this fixture.
   * @throws AssertionError if this fixture's <code>Frame</code> is not visible.
   */
    public FrameFixture requireVisible() {
        driver.requireVisible(target);
        return this;
    }

    /**
   * Simulates a user resizing vertically this fixture's <code>{@link Frame}</code>.
   * @param height the height that this fixture's <code>Frame</code> should have after being resized.
   * @return this fixture.
   */
    public FrameFixture resizeHeightTo(int height) {
        driver.resizeHeightTo(target, height);
        return this;
    }

    /**
   * Simulates a user resizing this fixture's <code>{@link Frame}</code>.
   * @param size the size that the target window should have after being resized.
   * @return this fixture.
   */
    public FrameFixture resizeTo(Dimension size) {
        driver.resizeTo(target, size);
        return this;
    }

    /**
   * Simulates a user resizing horizontally this fixture's <code>{@link Frame}</code>.
   * @param width the width that this fixture's <code>Frame</code> should have after being resized.
   * @return this fixture.
   */
    public FrameFixture resizeWidthTo(int width) {
        driver.resizeWidthTo(target, width);
        return this;
    }

    /**
   * Shows this fixture's <code>{@link Frame}</code>.
   * @return this fixture.
   */
    public FrameFixture show() {
        driver.show(target);
        return this;
    }

    /**
   * Shows this fixture's <code>{@link Frame}</code>, resized to the given size.
   * @param size the size to resize this fixture's <code>Frame</code> to.
   * @return this fixture.
   */
    public FrameFixture show(Dimension size) {
        driver.show(target, size);
        return this;
    }

    /**
   * If fixture's <code>{@link Frame}</code> is visible, brings it to the front and may make it the focused one.
   * @return this fixture.
   */
    public FrameFixture moveToFront() {
        driver.moveToFront(target);
        return this;
    }

    /**
   * If the given <code>{@link Frame}</code> is visible, sends it to the back and may cause it to lose focus or 
   * activation if it is the focused or active.
   * @return this fixture.
   */
    public FrameFixture moveToBack() {
        driver.moveToBack(target);
        return this;
    }

    /**
   * Simulates a user closing this fixture's <code>{@link Frame}</code>.
   */
    public void close() {
        driver.close(target);
    }
}
