package gnagck.block;

import gnagck.actor.ActorType;
import gnagck.event.CueList;
import java.awt.image.*;
import java.awt.*;
import java.awt.geom.*;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * The representation of a level block including graphics and behaviour.
 *
 * @author Dale Royer
 * @author Mark Royer
 *
 */
public class BlockType {

    /**
     * In pixels.
     */
    protected final int COLSIZE = 8;

    /** In pixels */
    protected final int ROWSIZE = 8;

    /**
     * Dynamic double array of pixel (graphic) data.
     */
    protected BufferedImage theblock;

    /** Block Type. */
    protected boolean Barrier;

    /** Block Type. */
    protected boolean Ladder;

    /** Block Type. */
    protected boolean Deadly;

    /** Block Type. */
    protected boolean Background;

    /** Block Type. */
    protected boolean Teleport;

    /**
     * Block Power.
     */
    protected boolean Normal;

    /** Block Power. */
    protected boolean Switch;

    /** Block Power. */
    protected boolean Attract;

    /** Block Power. */
    protected boolean Repel;

    /** Block Power. */
    protected boolean Shoot;

    /**
     * Block Label for referencing this block.
     */
    protected String name;

    /** Global Block Label for referencing this block. */
    protected String GUID;

    /**
     * The amount of slide on this block or ladder.
     */
    protected float friction;

    /**
     * The amount of bounce this block has.
     */
    protected float elasticity;

    /**
     * The amount of repel or attract.
     */
    protected float forcevector;

    /**
     * The speed of this conveyor belt.
     */
    protected float conveyorspeed;

    /**
     * The direction this block shoots or repels
     */
    protected char direction;

    /**
     * Shooting frequency.
     */
    protected float frequency;

    /**
     * The actor type of bullets shot from this block.
     */
    protected ActorType linkedactor;

    /**
     * Special Effect Cue Array.
     */
    protected CueList cuelist;

    /**
     * The replacement block on a switch.
     */
    protected BlockType replacement;

    /**
     * Trigger Source.
     */
    protected boolean Automatic;

    /** Trigger Source. */
    protected boolean Player;

    /** Trigger Source. */
    protected boolean Drone;

    /** Trigger Source. */
    protected boolean Missile;

    /** Trigger Source. */
    protected boolean Computer;

    /** Trigger Source. */
    protected boolean FXCue;

    /**
     * Linked to all blocks of the same type on the same level.
     */
    protected boolean chain;

    /**
     * This block produces a sound.
     */
    protected boolean sound;

    /** End of Level trigger for this block. */
    protected boolean endleveloff;

    /** End of Level trigger for this block. */
    protected boolean endlevelcountdown;

    /** End of Level trigger for this block. */
    protected boolean endlevelon;

    /**
     * Switch delay in frames.
     */
    protected long delayamount;

    /**
     * Award or Penalize for triggered event.
     */
    protected long points;

    /** Award or Penalize for triggered event. */
    protected long lives;

    /**
     * Default constructor sets the block to a default color of black.
     */
    public BlockType() {
        theblock = new BufferedImage(COLSIZE, ROWSIZE, BufferedImage.TYPE_INT_ARGB);
        ClearBlock(new Color(0));
        Background = true;
        Barrier = false;
        Ladder = false;
        Deadly = false;
        Teleport = false;
        Normal = true;
        Switch = false;
        Attract = false;
        Repel = false;
        Shoot = false;
        name = "";
        GUID = "";
        friction = 0;
        elasticity = 0;
        forcevector = 0;
        conveyorspeed = 0;
        direction = 'L';
        frequency = 0;
        linkedactor = null;
        replacement = null;
        Automatic = true;
        Player = false;
        Drone = false;
        Missile = false;
        Computer = false;
        FXCue = false;
        chain = false;
        sound = false;
        endleveloff = true;
        endlevelcountdown = false;
        endlevelon = false;
        delayamount = 0;
        points = 0;
        lives = 0;
    }

    /**
     * Constructor sets theblock to a specified color using RGB color amounts.
     *
     * @param Red The amount of red in the range 0..255
     * @param Green The amount of green in the range 0..255
     * @param Blue The amount of blue in the range 0..255
     */
    public BlockType(char Red, char Green, char Blue) {
        int rgbColor = new Color(Red, Green, Blue).getRGB();
        theblock = new BufferedImage(COLSIZE, ROWSIZE, BufferedImage.TYPE_INT_ARGB);
        ClearBlock(new Color((float) Red / 256.0f, (float) Green / 256.0f, (float) Blue / 256.0f));
        for (int i = 0; i < COLSIZE; i++) for (int n = 0; n < ROWSIZE; n++) theblock.setRGB(i, n, rgbColor);
        Background = true;
        Barrier = false;
        Ladder = false;
        Deadly = false;
        Teleport = false;
        Normal = true;
        Switch = false;
        Attract = false;
        Repel = false;
        Shoot = false;
        name = "";
        GUID = "";
        friction = 0;
        elasticity = 0;
        forcevector = 0;
        conveyorspeed = 0;
        direction = 'L';
        frequency = 0;
        linkedactor = null;
        replacement = null;
        Automatic = true;
        Player = false;
        Drone = false;
        Missile = false;
        Computer = false;
        FXCue = false;
        chain = false;
        sound = false;
        endleveloff = true;
        endlevelcountdown = false;
        endlevelon = false;
        delayamount = 0;
        points = 0;
        lives = 0;
    }

    /**
     * Sets a specific pixel at a location in the block using RGB color.
     *
     * @param col The coloumn location of the pixel to change.
     * @param row The row location of the pixel to change.
     * @param Red The amount of red in the range 0..255
     * @param Green The amount of green in the range 0..255
     * @param Blue The amount of blue in the range 0..255
     */
    public void SetPixel(int col, int row, char Red, char Green, char Blue) {
        theblock.setRGB(col, row, new Color(Red, Green, Blue).getRGB());
    }

    /**
     * Sets a specified pixel color using a pixel value.
     *
     * @param col The coloumn location of the pixel to change.
     * @param row The row location of the pixel to change.
     * @param pixel The 32 bit pixel color value in RGBA format.
     */
    public void SetPix(int col, int row, int pixel) {
        theblock.setRGB(col, row, pixel);
    }

    /** 
     * Returns the RGB color value of a specified pixel in this block.
     *
     * @param col The coloumn location of the pixel to get.
     * @param row The row location of the pixel to get.
     * @return The RGB color value.
     */
    public int GetPixel(int col, int row) {
        return theblock.getRGB(col, row);
    }

    /**
     * Draws this block on the screen at the given location.
     *
     * @param x The x location of the upper left corner of this block.
     * @param y The y location of the upper left corner of this block.
     * @param g2 The buffer this block is drawn to.
     */
    public void Draw(int x, int y, Graphics2D g2) {
        g2.drawImage(theblock, x, y, null);
    }

    /**
     * Draws this block on the screen at the given location and scale.
     *
     * @param x The x location of the upper left corner of this block.
     * @param y The y location of the upper left corner of this block.
     * @param scale The uniform scale factor.
     * @param g2 The buffer this block is drawn to.
     */
    public void Draw(int x, int y, double scale, Graphics2D g2) {
        AffineTransform at = new AffineTransform();
        at.translate(x, y);
        at.scale(scale, scale);
        g2.drawImage(theblock, at, null);
    }

    /**
     * Draws this block on the screen at the given location and size.
     *
     * @param x The x location of the upper left corner of this block.
     * @param y The y location of the upper left corner of this block.
     * @param width The width of the destination in pixels
     * @param height The height of the destination in pixels
     * @param g2 The buffer this block is drawn to.
     */
    public void Draw(int x, int y, int width, int height, Graphics2D g2) {
        AffineTransform at = new AffineTransform();
        at.translate(x, y);
        at.scale((double) width / (GetWidth()), (double) height / (GetHeight()));
        g2.drawImage(theblock, at, null);
    }

    /**
     * Makes all the pixels in this block the specified color.
     * 
     * @param color The color set all the pixels to in the block.
     */
    public void ClearBlock(Color color) {
        Graphics2D g2 = theblock.createGraphics();
        g2.setBackground(color);
        g2.clearRect(0, 0, COLSIZE, ROWSIZE);
    }

    /**
     * Sets this block type to the block type specified.
     * 
     * @param thetype One of "Background", "Barrier", "Ladder", "Deadly",
     *                or "Teleport"
     */
    public void SetType(String thetype) {
        if (thetype.equalsIgnoreCase("Background")) {
            Background = true;
            Barrier = false;
            Ladder = false;
            Deadly = false;
            Teleport = false;
        } else if (thetype.equalsIgnoreCase("Barrier")) {
            Background = false;
            Barrier = true;
            Ladder = false;
            Deadly = false;
            Teleport = false;
        } else if (thetype.equalsIgnoreCase("Ladder")) {
            Background = false;
            Barrier = false;
            Ladder = true;
            Deadly = false;
            Teleport = false;
        } else if (thetype.equalsIgnoreCase("Deadly")) {
            Background = false;
            Barrier = false;
            Ladder = false;
            Deadly = true;
            Teleport = false;
        } else if (thetype.equalsIgnoreCase("Teleport")) {
            Background = false;
            Barrier = false;
            Ladder = false;
            Deadly = false;
            Teleport = true;
        }
    }

    /**
     * Gets this block's characteristic (block type).
     *
     * @return One of "Background", "Barrier", "Ladder", "Deadly",
     *                or "Teleport"
     */
    public String GetType() {
        if (Background) return "Background"; else if (Barrier) return "Barrier"; else if (Ladder) return "Ladder"; else if (Deadly) return "Deadly"; else if (Teleport) return "Teleport"; else return "An error occurred while trying to return the block type";
    }

    /**
     * Gets the width of this block in pixels.
     *
     * @return The width of this block in pixels.
     */
    public int GetWidth() {
        return COLSIZE;
    }

    /**
     * Gets the height of this block in pixels.
     *
     * @return The height of this block in pixels.
     */
    public int GetHeight() {
        return ROWSIZE;
    }

    /**
     * Sets this block's name.
     *
     * @param NAME The name to assign this block.
     */
    public void Setname(String NAME) {
        name = NAME;
    }

    /**
     * Gets this block's name.
     *
     * @return The name of this block.
     */
    public String Getname() {
        return name;
    }

    /**
     * Sets this block's Global Unique IDentifier (GUID).
     *
     * @param ID The GUID to assign to this block.
     */
    public void SetGUID(String ID) {
        GUID = ID;
    }

    /**
     * Gets the block's Global Unique IDentifier (GUID).
     *
     * @return The GUID assigned to this block.
     */
    public String GetGUID() {
        return GUID;
    }

    /**
     * Sets this blocks coefficient friction.
     *
     * @param frict The coefficient of friction for this block.
     */
    public void Setfriction(float frict) {
        friction = frict;
    }

    /**
     * Gets this blocks coefficient of friction.
     *
     * @return The coefficient of friction for this block.
     */
    public float Getfriction() {
        return friction;
    }

    /**
     * Sets the amount of elasticity this block has.
     *
     * @param elast The amount of elasticity to assign this block.
     */
    public void Setelasticity(float elast) {
        elasticity = elast;
    }

    /**
     * Gets the blocks elasticity value.
     *
     * @return The amount of elasticity.
     */
    public float Getelasticity() {
        return elasticity;
    }

    /**
     * Sets the force of the vector towards, or against.
     *
     * @param forcev The direction and strength of the force.
     */
    public void Setfocevector(float forcev) {
        forcevector = forcev;
    }

    /**
     * Gets the amount of forcevector
     *
     * @return The strength of the force.
     */
    public float Getforcevector() {
        return forcevector;
    }

    /**
     * Sets this conveyor belt speed
     *
     * @param cspeed The speed in ????.
     */
    public void Setconveyorspeed(float cspeed) {
        conveyorspeed = cspeed;
    }

    /**
     * Gets the conveyor belt speed
     *
     * @return The speed in ????.
     */
    public float Getconveyorspeed() {
        return conveyorspeed;
    }

    /**
     * Sets the direction this block is shooting, or force vectoring.
     *
     * @param dir The direction.
     */
    public void Setdirection(char dir) {
        direction = dir;
    }

    /**
     * Gets the direction this block is shooting, or force vectoring.
     *
     * @return The direction.
     */
    public char Getdirection() {
        return direction;
    }

    /**
     * Sets the shooting frequency for this block.
     *
     * @param freq Shots per second.
     */
    public void Setfrequency(float freq) {
        frequency = freq;
    }

    /**
     * Gets the frequency this block shoots.
     *
     * @return Shots per second.
     */
    public float Getfrequency() {
        return frequency;
    }

    /**
     * Sets the actor that this block shoots.
     *
     * @param lactor The the actor to use as a bullet.
     */
    public void Setlinkedactor(ActorType lactor) {
        linkedactor = lactor;
    }

    /**
     * Gets the actor that is linked to this block.
     *
     * @return The linked actor.
     */
    public ActorType Getlinkedactor() {
        return linkedactor;
    }

    /** Adds a cue to the array of cues
     * @param thename The cue name
     * @param thecommand The cue command
     */
    public void AddCue(String thename, String thecommand) {
        cuelist.AddCue(thename, thecommand);
    }

    /** Removes a cue from the list
     * @param cuename Removes cuename from cue list
     */
    public void RemoveCue(String cuename) {
        cuelist.RemoveCue(cuename);
    }

    /** Cues the block
     * @param cuename The cues name
     * @param command The property that the block will become
     */
    public void CueBlock(String cuename, String command) {
        if (cuelist.IsIn(cuename)) {
            if (command == "Background") {
                Background = true;
                Barrier = false;
                Ladder = false;
                Deadly = false;
                Teleport = false;
                Shoot = false;
            } else if (command == "Barrier") {
                Background = false;
                Barrier = true;
                Ladder = false;
                Deadly = false;
                Teleport = false;
                Shoot = false;
            } else if (command == "Ladder") {
                Background = false;
                Barrier = false;
                Ladder = true;
                Deadly = false;
                Teleport = false;
                Shoot = false;
            } else if (command == "Deadly") {
                Background = false;
                Barrier = false;
                Ladder = false;
                Deadly = true;
                Teleport = false;
                Shoot = false;
            } else if (command == "Teleport") {
                Background = false;
                Barrier = false;
                Ladder = false;
                Deadly = false;
                Teleport = true;
                Shoot = false;
            } else if (command == "Shoot") {
                Background = false;
                Barrier = false;
                Ladder = false;
                Deadly = false;
                Teleport = false;
                Shoot = true;
            } else if (command == "Switch") SwitchToTarget();
        }
    }

    /** Sets the target pointer to the proper block
     * @param target The target block
     */
    public void SetTarget(BlockType target) {
        replacement = target;
    }

    /**
     * Makes the block switch to the target block
     */
    public void SwitchToTarget() {
    }

    /** Sets the block Power to one specified
     * @param thepower The power the block will have
     */
    public void SetPower(String thepower) {
        if (thepower.equalsIgnoreCase("Normal")) {
            Normal = true;
            Switch = false;
            Attract = false;
            Repel = false;
            Shoot = false;
        } else if (thepower.equalsIgnoreCase("Switch")) {
            Normal = false;
            Switch = true;
            Attract = false;
            Repel = false;
            Shoot = false;
        } else if (thepower.equalsIgnoreCase("Attract")) {
            Normal = false;
            Switch = false;
            Attract = true;
            Repel = false;
            Shoot = false;
        } else if (thepower.equalsIgnoreCase("Repel")) {
            Normal = false;
            Switch = false;
            Attract = false;
            Repel = true;
            Shoot = false;
        } else if (thepower.equalsIgnoreCase("Shoot")) {
            Normal = false;
            Switch = false;
            Attract = false;
            Repel = false;
            Shoot = true;
        }
    }

    /** Gets the block's power
     * @return The block's power
     */
    public String GetPower() {
        if (Normal) return "Normal"; else if (Switch) return "Switch"; else if (Attract) return "Attract"; else if (Repel) return "Repel"; else if (Shoot) return "Shoot"; else return "An error occurred while trying to return the block type";
    }

    /** Sets the switch power automatic
     * @param trueorfalse The switch power automatic or not
     */
    public void SetAutomatic(boolean trueorfalse) {
        Automatic = trueorfalse;
    }

    /** Gets if the switch power automatic is true or false
     * @return automatic
     */
    public boolean GetAutomatic() {
        return Automatic;
    }

    /** Sets the switch power Player
     * @param trueorfalse The switch power Player
     */
    public void SetPlayer(boolean trueorfalse) {
        Player = trueorfalse;
    }

    /** Gets if the switch power Player is true or false
     * @return Player true or false
     */
    public boolean GetPlayer() {
        return Player;
    }

    /** Sets the switch power Drone
     * @param trueorfalse Switch power Drone
     */
    public void SetDrone(boolean trueorfalse) {
        Drone = trueorfalse;
    }

    /** Gets if the switch power Drone is true or false
     * @return The switch power Drone true or false
     */
    public boolean GetDrone() {
        return Drone;
    }

    /** Sets the switch power Missile
     * @param trueorfalse The switch power Missile
     */
    public void SetMissile(boolean trueorfalse) {
        Missile = trueorfalse;
    }

    /** Gets if the switch power Missile is true or false
     * @return The switch power Missile true or false
     */
    public boolean GetMissile() {
        return Missile;
    }

    /** Sets the switch power Computer
     * @param trueorfalse The switch power Computer
     */
    public void SetComputer(boolean trueorfalse) {
        Computer = trueorfalse;
    }

    /** Gets if the switch power Computer is true or false
     * @return The switch power Computer true or false
     */
    public boolean GetComputer() {
        return Computer;
    }

    /** Sets the switch power FXCue
     * @param trueorfalse The switch power FXCue
     */
    public void SetFXCue(boolean trueorfalse) {
        FXCue = trueorfalse;
    }

    /** Gets if the switch power FXCue is true or false
     * @return The switch power FXCue is true or false
     */
    public boolean GetFXCue() {
        return FXCue;
    }

    /**
     * Sets the chain to either true or false
     * @param trueorfalse The chain to true or false
     */
    public void SetChain(boolean trueorfalse) {
        chain = trueorfalse;
    }

    /**
     * Gets whether the chain is true or false
     * @return The chain true or false
     */
    public boolean GetChain() {
        return chain;
    }

    /**
     * Sets the sound to either true or false
     * @param trueorfalse The sound
     */
    public void SetSound(boolean trueorfalse) {
        sound = trueorfalse;
    }

    /**
     * Gets whether the sound is true or false
     * @return The sound true or false
     */
    public boolean GetSound() {
        return sound;
    }

    /**
     * Sets the End of Level to off, on , or countdown
     * @param type The end of level "endleveloff","endlevelon","endlevelcountdown"
     */
    public void SetEndoflevel(String type) {
        if (type.equalsIgnoreCase("endleveloff")) {
            endleveloff = true;
            endlevelon = false;
            endlevelcountdown = false;
        } else if (type.equalsIgnoreCase("endlevelon")) {
            endleveloff = false;
            endlevelon = true;
            endlevelcountdown = false;
        } else if (type.equalsIgnoreCase("endlevelcountdown")) {
            endleveloff = false;
            endlevelon = false;
            endlevelcountdown = true;
        }
    }

    /**
     * Gets the type of End of Level
     * @return "endleveloff","endlevelon", or "endlevelcountdown"
     */
    public String GetEndoflevel() {
        if (endleveloff) return "endleveloff"; else if (endlevelon) return "endlevelon"; else if (endlevelcountdown) return "endlevelcountdown"; else return "ERROR";
    }

    /**
     * Sets the Switch Delay
     * @param amount The time delay in seconds?
     */
    public void SetSwitchDelay(long amount) {
        delayamount = amount;
    }

    /**
     * Gets the amount of Switch Delay
     * @return delayamount The amount of delay for the switch
     */
    public long GetSwitchDelay() {
        return delayamount;
    }

    /**
     * Sets the amount of award/penalty of points
     * @param amount The amount of points for this block
     */
    public void SetPoints(long amount) {
        points = amount;
    }

    /**
     * Gets the amount of award/penalty of points
     *  @return points The amount for this block
     */
    public long GetPoints() {
        return points;
    }

    /**
     * Sets the amount of award/penalty of lives
     * @param amount The lives for this block
     */
    public void Setlives(long amount) {
        lives = amount;
    }

    /**
     * Gets the amount of award/penalty of lives
     * @return lives The amount of lives for this block
     */
    public long Getlives() {
        return lives;
    }

    public Icon GetIcon() {
        return new ImageIcon(theblock.getScaledInstance(16, 16, BufferedImage.SCALE_DEFAULT));
    }
}
