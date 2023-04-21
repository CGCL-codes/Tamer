package jbomberman.game;

/**
 * This represents an abstract command issued by a computer
 * or human player. It can contain a direction and up to
 * 8 button states.
 *
 * @author Lukas Zebedin
 * @version 1.1
 * @see Directions
 */
public class Command {

    /**
   * This is the direction where the player wants to
   * go. Default is no direction.
   */
    protected byte direction_ = Directions.NONE;

    /**
   * This byte represents the states of the buttons.
   * Each button is encoded as one bit, therefore 8
   * buttons can be modeled. Default value is no
   * buttons pressed at all.
   */
    public byte buttons_ = 0;

    /**
   * This default constructor creates a command with
   * movement and button states set to their default values.
   */
    public Command() {
    }

    /**
   * This constructor creates a new command with the specified
   * movement and all button states set to their default value.
   *
   * @param direction is the new direction the player wants to go
   *                  according to the Directions interface.
   */
    public Command(byte direction) {
        direction_ = direction;
    }

    /**
   * This constructor creates a new command with the first
   * button set to the specified value and the movement set
   * to the default value.
   *
   * @param pressed_button indicates if the first button is pressed.
   */
    public Command(boolean pressed_button) {
        if (pressed_button) {
            pressButton(0);
        }
    }

    /**
   * Get function for the movement direction.
   *
   * @return byte which is a direction according to the Directions interface.
   */
    public byte getDirection() {
        return direction_;
    }

    /**
   * Set function for the movement direction.
   *
   * @param direction is the new direction the player wants to go
   *                  according to the Directions interface.
   */
    public void setDirection(byte direction) {
        direction_ = direction;
    }

    /**
   * Activates the specified button.
   *
   * @param num is the number of the button which should be pressed.
   */
    public void pressButton(int num) {
        buttons_ |= (1 << num);
    }

    /**
   * Deactivates the specified button.
   *
   * @param num is the number of the button which should be released.
   */
    public void releaseButton(int num) {
        buttons_ &= (255 & (0 << num));
    }

    /**
   * Returns the state of the specified button.
   *
   * @param num is the number of the button to be checked.
   * @return boolean which indicates if the specified button is pressed.
   */
    public boolean isButtonPressed(int num) {
        return (buttons_ & (1 << num)) == (1 << num);
    }

    /**
   * Returns the state of all buttons.
   *
   * @return byte which represents the states of the buttons. every bit for one button.
   */
    public byte getButtonStates() {
        return buttons_;
    }

    /**
   * Sets the state of all buttons.
   *
   * @param byte which represents the states of the buttons. every bit for one button.
   */
    public void setButtonStates(byte buttons) {
        buttons_ = buttons;
    }

    /**
   * Tests if two commands are equal.
   *
   * @return boolean which is the result of the comparison between the commands.
   */
    public boolean equals(Command command) {
        if (command == null) {
            return false;
        }
        return ((command.getButtonStates() == buttons_) && (command.getDirection() == direction_));
    }
}
