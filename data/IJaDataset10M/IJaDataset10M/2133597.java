package sfa.projetIHM.VolleyBall.Action.control;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import sfa.projetIHM.VolleyBall.Action.model.TempsMort;
import sfa.projetIHM.VolleyBall.FeuilleDeMatch.model.FeuilleDeMatchModel;
import sfa.projetIHM.VolleyBall.FeuilleDeMatch.view.FeuilleDeMatchVue;
import sfa.projetIHM.VolleyBall.ScoreCompteur.model.ScoreGlobalModel;
import sfa.projetIHM.VolleyBall.constants.Constant;
import sfa.projetIHM.VolleyBall.equipe.Equipe;
import sfa.projetIHM.VolleyBall.equipe.Joueur;
import sfa.projetIHM.VolleyBall.view.PanneauVue;
import sfa.projetIHM.horloge.models.HorlogeM;
import sfa.projetIHM.horloge.postWIMP.Menu;
import fr.lri.swingstates.applets.MenuItem;
import fr.lri.swingstates.canvas.CRectangle;
import fr.lri.swingstates.canvas.CShape;
import fr.lri.swingstates.canvas.CStateMachine;
import fr.lri.swingstates.canvas.CText;
import fr.lri.swingstates.canvas.Canvas;
import fr.lri.swingstates.canvas.CStateMachine.EnterOnShape;
import fr.lri.swingstates.canvas.CStateMachine.EnterOnTag;
import fr.lri.swingstates.canvas.CStateMachine.ReleaseOnTag;
import fr.lri.swingstates.sm.JStateMachine;
import fr.lri.swingstates.sm.StateMachine;
import fr.lri.swingstates.sm.BasicInputStateMachine.Press;
import fr.lri.swingstates.sm.BasicInputStateMachine.Release;
import fr.lri.swingstates.sm.StateMachine.State;
import fr.lri.swingstates.sm.StateMachine.State.TimeOut;
import fr.lri.swingstates.sm.StateMachine.State.Transition;

public class Action extends Menu {

    private PanneauVue terrain;

    private JStateMachine plateau;

    private boolean depart = false;

    private boolean bon = false;

    private Equipe equipe1;

    private Equipe equipe2;

    private ScoreGlobalModel score;

    private FeuilleDeMatchModel feuille;

    private CText[] labelItem;

    private CShape menu;

    private CRectangle[] bgItem;

    public Action(PanneauVue terrain, Canvas c, Equipe e1, Equipe e2, ScoreGlobalModel score, FeuilleDeMatchModel feuille) {
        super(c, new Color(240, 213, 50), new Color(154, 12, 240));
        this.score = score;
        equipe1 = e1;
        equipe2 = e2;
        canvas = c;
        this.terrain = terrain;
        labelItem = new CText[2];
        bgItem = new CRectangle[2];
        menuLayout();
        hideMenu();
        StateMachine();
        plateau.attachTo(terrain);
        interaction.attachTo(canvas);
        Menu.getHiliteMachine().attachTo(canvas);
        this.feuille = feuille;
    }

    public void attachTo() {
        plateau.attachTo(terrain);
    }

    public void detach() {
        plateau.detachFrom(terrain);
    }

    void StateMachine() {
        plateau = new JStateMachine() {

            public State On = new State() {

                Color c = null;

                public Transition enterSanction = new EnterOnTag("sanction") {

                    public boolean guard() {
                        return depart;
                    }

                    ;

                    public void action() {
                        Component selected = getComponent();
                        selected.setForeground(Color.white);
                        selected.repaint();
                    }

                    ;
                };

                public Transition leaveSanction = new LeaveOnTag("sanction") {

                    public boolean guard() {
                        return depart;
                    }

                    ;

                    public void action() {
                        Component selected = getComponent();
                        selected.setForeground(Color.black);
                        selected.repaint();
                    }

                    ;
                };

                public Transition pressSanction = new PressOnTag("sanction", BUTTON1) {

                    public boolean guard() {
                        return depart;
                    }

                    ;

                    public void action() {
                        if (!terrain.IsTerrain()) terrain.showTerrain("terrain");
                        terrain.detach();
                        terrain.attach("sanction");
                    }

                    ;
                };

                public Transition enterRemplacant = new EnterOnTag("remplacant") {

                    public boolean guard() {
                        return depart;
                    }

                    ;

                    public void action() {
                        Component selected = getComponent();
                        selected.setForeground(Color.white);
                        selected.repaint();
                    }

                    ;
                };

                public Transition leaveRemplacant = new LeaveOnTag("remplacant") {

                    public boolean guard() {
                        return depart;
                    }

                    ;

                    public void action() {
                        Component selected = getComponent();
                        selected.setForeground(Color.black);
                        selected.repaint();
                    }

                    ;
                };

                public Transition remplacement = new PressOnTag("remplacant", BUTTON1) {

                    public boolean guard() {
                        return depart;
                    }

                    ;

                    public void action() {
                        if (!terrain.IsTerrain()) terrain.showTerrain("terrain");
                        terrain.detach();
                        terrain.attach("remplacement");
                    }

                    ;
                };

                public Transition enterFeuilleMatch = new EnterOnTag("feuilleMatch") {

                    public boolean guard() {
                        return depart;
                    }

                    ;

                    public void action() {
                        Component selected = getComponent();
                        selected.setForeground(Color.white);
                        selected.repaint();
                    }

                    ;
                };

                public Transition leaveFeuilleMatch = new LeaveOnTag("feuilleMatch") {

                    public boolean guard() {
                        return depart;
                    }

                    ;

                    public void action() {
                        Component selected = getComponent();
                        selected.setForeground(Color.black);
                        selected.repaint();
                    }

                    ;
                };

                public Transition pressFeuilleMatch = new PressOnTag("feuilleMatch", BUTTON1) {

                    public boolean guard() {
                        return depart;
                    }

                    ;

                    public void action() {
                        JFrame frame = new JFrame("Feuille de match");
                        frame.getContentPane().add(new FeuilleDeMatchVue("test", feuille));
                        frame.setVisible(true);
                        frame.pack();
                    }

                    ;
                };

                public Transition enterPoint = new EnterOnTag("marquer") {

                    public boolean guard() {
                        return depart;
                    }

                    ;

                    public void action() {
                        Component selected = getComponent();
                        selected.setForeground(Color.white);
                        selected.repaint();
                    }

                    ;
                };

                public Transition leavePoint = new LeaveOnTag("marquer") {

                    public boolean guard() {
                        return depart;
                    }

                    ;

                    public void action() {
                        Component selected = getComponent();
                        selected.setForeground(Color.black);
                        selected.repaint();
                    }

                    ;
                };

                public Transition marquer = new PressOnTag("marquer", BUTTON1) {

                    public boolean guard() {
                        return depart;
                    }

                    ;

                    public void action() {
                        if (terrain.IsTerrain()) terrain.showTerrain("score");
                        terrain.detach();
                        terrain.attach("marquer");
                    }

                    ;
                };

                public Transition enterTempsMort = new EnterOnTag("temps") {

                    public boolean guard() {
                        return depart;
                    }

                    ;

                    public void action() {
                        Component selected = getComponent();
                        selected.setForeground(Color.white);
                        selected.repaint();
                    }

                    ;
                };

                public Transition leaveTempsMort = new LeaveOnTag("temps") {

                    public boolean guard() {
                        return depart;
                    }

                    ;

                    public void action() {
                        Component selected = getComponent();
                        selected.setForeground(Color.black);
                        selected.repaint();
                    }

                    ;
                };

                public Transition tempsMort = new PressOnTag("temps", BUTTON1, ">> tempsMort") {

                    public boolean guard() {
                        return depart;
                    }

                    public void action() {
                        bon = true;
                        terrain.detach();
                        Menu.getHiliteMachine().attachTo(canvas);
                        terrain.setMode("appuyer sur Temps Mort pour reprendre le match");
                    }

                    ;
                };
            };

            public State tempsMort = new State() {

                public Transition enterTempsMort = new EnterOnTag("temps") {

                    public boolean guard() {
                        return depart;
                    }

                    ;

                    public void action() {
                        JLabel selected = (JLabel) getComponent();
                        selected.setForeground(Color.white);
                        selected.repaint();
                    }

                    ;
                };

                public Transition leaveTempsMort = new LeaveOnTag("temps") {

                    public boolean guard() {
                        return depart;
                    }

                    ;

                    public void action() {
                        JLabel selected = (JLabel) getComponent();
                        selected.setForeground(Color.black);
                        selected.repaint();
                    }

                    ;
                };

                public Transition pressTempsMort = new PressOnTag("temps", BUTTON1, ">> On") {

                    public void action() {
                        bon = false;
                        terrain.setMode("le match peut reprendre");
                    }
                };
            };
        };
        interaction = new CStateMachine() {

            public State menuOff = new State() {

                Transition invoke = new Press(BUTTON1, ">> menuOn") {

                    public boolean guard() {
                        return bon;
                    }

                    public void action() {
                        showMenu(getPoint());
                    }
                };

                public void leave() {
                    tagWhole.setPickable(true);
                    tagLabels.setPickable(false);
                    armTimer(30, false);
                }
            };

            public State menuOn = new State() {

                String lastItemVisited;

                Transition showMenu = new TimeOut() {

                    public void action() {
                        tagWhole.setDrawable(true);
                    }
                };

                Transition release = new Release(">> menuOff") {

                    public void action() {
                        disarmTimer();
                    }
                };

                Transition select = new ReleaseOnTag(MenuItem.class, BUTTON1, ">> menuOff") {

                    public boolean guard() {
                        return bon;
                    }

                    public void action() {
                        if (lastItemVisited != null) {
                            addTempsMort(lastItemVisited);
                            bon = false;
                        }
                    }
                };

                Transition changeItem = new EnterOnTag(MenuItem.class) {

                    public void action() {
                        lastItemVisited = ((MenuItem) getTag()).getName();
                    }
                };

                Transition cancel = new EnterOnShape() {

                    public void action() {
                        lastItemVisited = null;
                    }
                };

                public void leave() {
                    hidemenu();
                }
            };
        };
    }

    void addTempsMort(String s) {
        int a = score.getScore(1);
        int b = score.getScore(2);
        if (s.equals(equipe1.getVille())) {
            equipe1.addTempsMort(a, b);
            feuille.addTempsMort(equipe1);
        } else {
            equipe2.addTempsMort(a, b);
            feuille.addTempsMort(equipe2);
        }
    }

    public boolean isDepart() {
        return depart;
    }

    public void setDepart(boolean depart) {
        this.depart = depart;
    }

    public void hidemenu() {
        this.hideMenu();
    }

    void showmenu(double x, double y) {
        parent.translateTo(x, y).setDrawable(true);
        tagWhole.setPickable(true);
        tagLabels.setPickable(false);
        tagWhole.aboveAll();
        tagLabels.aboveAll();
    }

    void showMenu(Point2D pt) {
        showmenu(pt.getX(), pt.getY());
    }

    void menuLayout() {
        parent = canvas.newRectangle(-10, -15, 0, 0);
        menu = canvas.newRectangle(-10, -5, 0, 0);
        menu.setFilled(false).setOutlined(false).setTransparencyFill(0.3f).setReferencePoint(0, 0).translateTo(-10, -5);
        menu.addTag(tagWhole).setParent(parent);
        bgItem[0] = canvas.newRectangle(-10, -5, 150, 25);
        bgItem[0].setReferencePoint(0, 0).translateTo(-10, -5).setFillPaint(BG_COLOR).setOutlinePaint(BORDER_COLOR);
        bgItem[0].addTag(tagWhole).addTag(new MenuItem(equipe1.getVille()));
        parent.addChild(bgItem[0]);
        labelItem[0] = (CText) canvas.newText(0, 0, equipe1.getVille(), Menu.FONT).setPickable(false).addTag(tagWhole).addTag(tagLabels);
        parent.addChild(labelItem[0]);
        bgItem[1] = canvas.newRectangle(-10, -5, 150, 25);
        bgItem[1].setReferencePoint(0, 0).translateTo(-10, -5).setFillPaint(BG_COLOR).setOutlinePaint(BORDER_COLOR);
        bgItem[1].addTag(tagWhole).addTag(new MenuItem(equipe2.getVille())).translateTo(100, 0);
        parent.addChild(bgItem[1]);
        labelItem[1] = (CText) canvas.newText(100, 0, equipe2.getVille(), Menu.FONT).setPickable(false).addTag(tagWhole).addTag(tagLabels);
        parent.addChild(labelItem[1]);
        parent.addTag(tagWhole).setTransparencyFill(0.3f);
        tagLabels.aboveAll();
        parent.aboveAll();
    }

    public StateMachine getPlateau() {
        return plateau;
    }
}
