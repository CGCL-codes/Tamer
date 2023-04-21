package mylittlestudent.views;

import javax.microedition.lcdui.*;
import mylittlestudent.controller.interfaces.*;
import javax.microedition.lcdui.game.*;
import mylittlestudent.controller.interfaces.IViewDataFacade;

public class GraduatedView extends GameCanvas implements CommandListener, ITimerObserver {

    private IViewDataFacade viewData;

    private Image eyeleftHappy, eyerightHappy, mouthHappy;

    private int moveh = 0, movew = 0;

    private int facewspoint, facehspoint, eyeleftwspoint, eyelefthspoint, eyerightwspoint;

    private int eyerighthspoint, mouthhspoint, hairwspoint, hairhspoint, mouthwspoint, woodhspoint, woodwspoint;

    private int flaglearning, flagdhungry, flagparty, flagdsleep, flagmm, flagjob, flagpp, flagppb, flagill, flagvisitlecture, flagsport, flagwoodenboard;

    private int statelearningflag, statepartyflag, statehungerflag, statetirednessflag, statejobbingflag;

    private int statesportsflag, statevisits_lectureflag, statepp, statemm, hutwspoint;

    private Graphics g;

    private Image faceimg, ball, eyeleft, eyeright, mouth, hut;

    private final Command endCommand = new Command("Beenden", Command.OK, 1);

    /** Creates a new instance of GraduatedView */
    public GraduatedView() {
        super(false);
        setCommandListener(this);
        addCommand(endCommand);
        moveh = 0;
        this.viewData = ViewDataFactory.getViewData();
        System.gc();
    }

    public void paint(Graphics g) {
        synchronized (this) {
            try {
                if (hut == null) {
                    hut = Image.createImage("/images/magisterhut.gif");
                    faceimg = Image.createImage("/images/face.gif");
                    ball = Image.createImage("/images/ball.gif");
                    eyeleftHappy = Image.createImage("/images/eyelefttype1.gif");
                    eyerightHappy = Image.createImage("/images/eyerighttype1.gif");
                    mouthHappy = Image.createImage("/images/mouthlaugh.gif");
                }
                eyeleft = eyeleftHappy;
                eyeright = eyerightHappy;
                mouth = mouthHappy;
                hutwspoint = (g.getClipWidth() / 2) - (hut.getWidth() / 2);
                facewspoint = (g.getClipWidth() / 2) - (faceimg.getWidth() / 2);
                facehspoint = ((g.getClipHeight() / 2) - faceimg.getHeight() / 2) + 2 * (mouth.getHeight());
                System.out.println("GVIEW");
                eyeleftwspoint = (g.getClipWidth() / 2) - eyeleft.getWidth();
                eyelefthspoint = (g.getClipHeight() / 2 - eyeleft.getHeight() * 8 / 10 + 2 * (mouth.getHeight()));
                eyerightwspoint = ((g.getClipWidth() / 2));
                eyerighthspoint = (g.getClipHeight() / 2 - eyeleft.getHeight() * 8 / 10 + 2 * (mouth.getHeight()));
                mouthwspoint = ((g.getClipWidth() / 2) - (mouth.getWidth() / 2));
                mouthhspoint = (g.getClipHeight() / 2 + mouth.getHeight() + 2 * (mouth.getHeight()));
                System.out.println(hut.getHeight());
                System.out.println(facehspoint);
                if (moveh + (3 * ball.getHeight()) <= facehspoint) {
                    g.setColor(0xffffff);
                    g.fillRect(0, 0, g.getClipWidth(), g.getClipHeight());
                    g.setColor(0xecaa2b);
                    g.fillRect((g.getClipWidth() / 2) - (faceimg.getWidth() / 2), (g.getClipHeight() / 2) - faceimg.getHeight() / 2 + 2 * mouth.getHeight(), faceimg.getWidth(), faceimg.getHeight());
                    g.drawImage(faceimg, facewspoint, facehspoint, g.LEFT | g.TOP);
                    g.drawImage(eyeleft, eyeleftwspoint, eyelefthspoint, g.LEFT | g.TOP);
                    g.drawImage(eyeright, eyerightwspoint, eyerighthspoint, g.LEFT | g.TOP);
                    g.drawImage(mouth, mouthwspoint, mouthhspoint, g.LEFT | g.TOP);
                    g.drawImage(hut, hutwspoint, 0 + moveh, g.LEFT | g.TOP);
                    System.out.println("moveh: " + moveh + " facehspoint " + facehspoint);
                    moveh = moveh + 8;
                    Thread.sleep(200);
                }
            } catch (Exception e) {
                System.out.println("Error in choising hairtyp");
            }
        }
        repaint();
    }

    public void timeElapsed(int ID) {
        if (ID == 1) {
            this.paint(g);
        }
    }

    public void commandAction(Command command, Displayable displayable) {
        if (command == endCommand) {
            try {
                viewData.performAction(ActionTypes.EXIT_WITHOUT_SAVE_ACTION);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
