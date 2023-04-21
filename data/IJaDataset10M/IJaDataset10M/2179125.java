package mylittlestudent.views;

import mylittlestudent.controller.interfaces.IViewDataFacade;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;
import mylittlestudent.controller.interfaces.ActionTypes;
import mylittlestudent.controller.interfaces.IViewDataFacade;
import mylittlestudent.data.interfaces.IReadStudentData;
import mylittlestudent.data.interfaces.ItemTypes;
import application.MainMidlet;
import mylittlestudent.data.interfaces.*;
import mylittlestudent.controller.interfaces.*;
import java.lang.Thread.*;
import java.io.IOException.*;

/**
 * Bildschirm zum festlegen des Aussehens
 */
public class SetAppearanceView extends GameCanvas implements CommandListener {

    private String name = "";

    private IViewDataFacade viewData;

    private int facewspoint, facehspoint, eyeleftwspoint, eyelefthspoint, eyerightwspoint;

    private int eyerighthspoint, mouthhspoint, mouthwspoint, hairindex, hairwspoint, hairhspoint;

    private Command okCommand = new Command("OK", Command.OK, 1);

    private Command exitCommand = new Command("Beenden", Command.EXIT, 2);

    private Graphics g;

    private Image faceimg, eyeleft, eyeright, mouth, ball, hair;

    /** Creates a new instance of SetAppearanceView */
    public SetAppearanceView(String newName) {
        super(false);
        name = newName;
        viewData = ViewDataFactory.getViewData();
        try {
            eyeleft = Image.createImage("/images/eyelefttype1.gif");
            eyeright = Image.createImage("/images/eyerighttype3.gif");
            mouth = Image.createImage("/images/mouthlaugh.gif");
            faceimg = Image.createImage("/images/face.gif");
            ball = Image.createImage("/images/ball.gif");
        } catch (Exception e) {
            System.out.println("Fehler in SetAppearanceView SetAppearanceView(): " + e.getMessage());
        }
        this.addCommand(okCommand);
        this.addCommand(exitCommand);
        this.setCommandListener(this);
        hairindex = 2;
        loadHair();
        this.run();
    }

    private void run() {
        this.flushGraphics();
        g = getGraphics();
        try {
            g.setColor(0xffffff);
            g.fillRect(0, 0, g.getClipWidth(), g.getClipHeight());
            g.setColor(0xecaa2b);
            g.fillRect((g.getClipWidth() / 2) - (faceimg.getWidth() / 2), (g.getClipHeight() / 2) + ball.getHeight() - faceimg.getHeight() / 2, faceimg.getWidth(), faceimg.getHeight());
        } catch (Exception e) {
            System.out.println("Error printing pricture in run()");
        }
        facewspoint = (g.getClipWidth() / 2) - (faceimg.getWidth() / 2);
        facehspoint = ((g.getClipHeight() / 2) + ball.getHeight() - faceimg.getHeight() / 2);
        eyeleftwspoint = (g.getClipWidth() / 2) - eyeleft.getWidth();
        eyelefthspoint = (g.getClipHeight() / 2 + ball.getHeight() - eyeleft.getHeight() * 8 / 10);
        eyerightwspoint = ((g.getClipWidth() / 2));
        eyerighthspoint = (g.getClipHeight() / 2 + ball.getHeight() - eyeleft.getHeight() * 8 / 10);
        mouthhspoint = ((g.getClipWidth() / 2) - (mouth.getWidth() / 2));
        mouthwspoint = (g.getClipHeight() / 2 + ball.getHeight() + mouth.getHeight());
        hairwspoint = (g.getClipWidth() / 2) - (hair.getWidth() / 2);
        hairhspoint = (((g.getClipHeight() / 2) + ball.getHeight() - hair.getHeight() / 2) - hair.getHeight() * 68 / 303);
        g.drawImage(faceimg, facewspoint, facehspoint, g.LEFT | g.TOP);
        g.drawImage(eyeleft, eyeleftwspoint, eyelefthspoint, g.LEFT | g.TOP);
        g.drawImage(eyeright, eyerightwspoint, eyerighthspoint, g.LEFT | g.TOP);
        g.drawImage(mouth, mouthhspoint, mouthwspoint, g.LEFT | g.TOP);
        g.drawImage(hair, hairwspoint, hairhspoint, g.LEFT | g.TOP);
        g.setColor(0x000000);
        if (this.getWidth() < 216) {
            g.drawString("W�hlen Sie bitte", 1, 1, g.LEFT | g.TOP);
            g.drawString("den Haartypen.", 1, 16, g.LEFT | g.TOP);
        } else g.drawString("W�hlen Sie bitte den Haartypen.", 1, 1, g.LEFT | g.TOP);
        this.flushGraphics();
    }

    public void commandAction(Command command, Displayable displayable) {
        if (command == okCommand) {
            try {
                viewData.performNewStudentAction(name, hairindex);
                viewData.performAction(ActionTypes.BEGIN_GAME);
            } catch (Exception ex) {
            }
        } else if (command == exitCommand) {
            try {
                ViewDataFactory.getViewData().performAction(ActionTypes.EXIT_ACTION);
            } catch (Exception ex) {
            }
        }
    }

    protected void keyPressed(int keyCode) {
        if (keyCode == getKeyCode(LEFT) || keyCode == KEY_NUM0) {
            try {
                if (this.hairindex == 8) {
                    this.hairindex = 1;
                } else {
                    this.hairindex++;
                }
            } catch (Exception ex) {
                System.out.println("Error in keyPressed");
            }
        } else if (keyCode == getKeyCode(RIGHT) || keyCode == KEY_NUM6) {
            try {
                if (this.hairindex == 1) {
                    this.hairindex = 8;
                } else {
                    this.hairindex--;
                }
            } catch (Exception ex) {
                System.out.println("Error in keyPressed");
            }
        }
        loadHair();
        this.run();
    }

    private void loadHair() {
        try {
            if (hairindex == 1) {
                hair = Image.createImage("/images/hairstyle1.gif");
            } else if (hairindex == 2) {
                hair = Image.createImage("/images/hairstyle2.gif");
            } else if (hairindex == 3) {
                hair = Image.createImage("/images/hairstyle3.gif");
            } else if (hairindex == 4) {
                hair = Image.createImage("/images/hairstyle4.gif");
            } else if (hairindex == 5) {
                hair = Image.createImage("/images/hairstyle5.gif");
            } else if (hairindex == 6) {
                hair = Image.createImage("/images/hairstyle6.gif");
            } else if (hairindex == 7) {
                hair = Image.createImage("/images/hairstyle7.gif");
            } else if (hairindex == 8) {
                hair = Image.createImage("/images/hairstyle8.gif");
            }
        } catch (Exception e) {
            System.out.println("Fehler in SetAppearanceView loadHair(): " + e.getMessage());
        }
    }
}
