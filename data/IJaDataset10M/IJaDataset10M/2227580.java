package Graphics;

import javax.swing.ImageIcon;

public class FightImages {

    private ImageIcon blockhighLeft;

    private ImageIcon blockhighRight;

    private ImageIcon blocklowRight;

    private ImageIcon blocklowLeft;

    private ImageIcon crawlRight;

    private ImageIcon crawlLeft;

    private ImageIcon kickhighRight;

    private ImageIcon kickhighLeft;

    private ImageIcon kicklowRight;

    private ImageIcon kicklowLeft;

    private ImageIcon punchhighRight;

    private ImageIcon punchhighLeft;

    private ImageIcon punchlowRight;

    private ImageIcon punchlowLeft;

    private ImageIcon deadRight;

    private ImageIcon deadLeft;

    private ImageIcon stanceLeft;

    private ImageIcon stanceRight;

    public FightImages() {
        String defaultpath = "/home/jos/workspace/SuperAwesomeFighters/bot_sprites/";
        stanceLeft = new ImageIcon(defaultpath + "stance_left.gif");
        blockhighLeft = new ImageIcon(defaultpath + "block_high.gif");
        blocklowLeft = new ImageIcon(defaultpath + "block_low.gif");
        crawlLeft = new ImageIcon(defaultpath + "crouch.gif");
        kickhighLeft = new ImageIcon(defaultpath + "kick_high.gif");
        kicklowLeft = new ImageIcon(defaultpath + "kick_low.gif");
        punchhighLeft = new ImageIcon(defaultpath + "punch_high.gif");
        punchlowLeft = new ImageIcon(defaultpath + "punch_low.gif");
        deadLeft = new ImageIcon(defaultpath + "dead_left.gif");
        stanceRight = new ImageIcon(defaultpath + "stance_right_red.gif");
        blockhighRight = new ImageIcon(defaultpath + "block_high_right_red.gif");
        blocklowRight = new ImageIcon(defaultpath + "block_low_right_red.gif");
        crawlRight = new ImageIcon(defaultpath + "crouch_right_red.gif");
        kickhighRight = new ImageIcon(defaultpath + "kick_high_right_red.gif");
        kicklowRight = new ImageIcon(defaultpath + "kick_low_right_red.gif");
        punchhighRight = new ImageIcon(defaultpath + "punch_high_right_red.gif");
        deadRight = new ImageIcon(defaultpath + "dead_right_red.gif");
    }

    public ImageIcon getImage(String name) {
        if (name.contentEquals("standLeft")) {
            return stanceLeft;
        }
        if (name.contentEquals("block_highLeft")) {
            return blockhighLeft;
        }
        if (name.contentEquals("block_lowLeft")) {
            return blocklowLeft;
        }
        if (name.contentEquals("kick_highLeft")) {
            return kickhighLeft;
        }
        if (name.contentEquals("kick_lowLeft")) {
            return kicklowLeft;
        }
        if (name.contentEquals("punch_lowLeft")) {
            return punchlowLeft;
        }
        if (name.contentEquals("punch_highLeft")) {
            return punchhighLeft;
        }
        if (name.contentEquals("crouchLeft")) {
            return crawlLeft;
        }
        if (name.contentEquals("deadLeft")) {
            return deadLeft;
        }
        if (name.contentEquals("standRight")) {
            return stanceRight;
        }
        if (name.contentEquals("block_highRight")) {
            return blockhighRight;
        }
        if (name.contentEquals("block_lowRight")) {
            return blocklowRight;
        }
        if (name.contentEquals("kick_highRight")) {
            return kickhighRight;
        }
        if (name.contentEquals("kick_lowRight")) {
            return kicklowRight;
        }
        if (name.contentEquals("punch_lowRight")) {
            return punchlowRight;
        }
        if (name.contentEquals("punch_highRight")) {
            return punchhighRight;
        }
        if (name.contentEquals("crouchRight")) {
            return crawlRight;
        }
        if (name.contentEquals("deadRight")) {
            return deadRight;
        }
        return stanceRight;
    }
}
