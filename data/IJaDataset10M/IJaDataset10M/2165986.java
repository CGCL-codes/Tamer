package View.GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.sun.org.apache.xerces.internal.impl.dv.dtd.NMTOKENDatatypeValidator;
import Controller.Game.GamePanelController;
import Model.Visible.Fleet;
import Model.Visible.Planet;
import Utils.ActionButton;
import Utils.ButtonImage;
import Utils.ConstantsImplement;

public class AttackPanel extends JPanel implements ConstantsImplement {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private JLabel numberOfShipsLabel_;

    private int numberOfShips_;

    private ButtonImage increaseShip_;

    private ButtonImage decreaseShip_;

    private ButtonImage readyButton_;

    private BufferedImage background;

    private boolean ready_;

    private GamePanelController gamePanelController_;

    private Planet planet_;

    public AttackPanel(GamePanelController gamePanelController, Planet planet) {
        planet_ = planet;
        setLayout(null);
        setLocation(planet_.getPosition_().x - 40, planet_.getPosition_().y - 45);
        background = gamePanelController.getMedia_().attackPanelBackground_;
        setSize(background.getWidth(), background.getHeight());
        ready_ = false;
        numberOfShips_ = 0;
        numberOfShipsLabel_ = new JLabel(String.valueOf(numberOfShips_));
        numberOfShipsLabel_.setSize(50, 18);
        numberOfShipsLabel_.setLocation(35, 11);
        Font font = new Font("Verdana", Font.BOLD, 18);
        numberOfShipsLabel_.setFont(font);
        numberOfShipsLabel_.setForeground(Color.WHITE);
        add(numberOfShipsLabel_);
        gamePanelController_ = gamePanelController;
        increaseShip_ = new ButtonImage(this, gamePanelController_.getMedia_().increaseFleet_, gamePanelController_.getMedia_().increaseFleet_, 90, 12);
        decreaseShip_ = new ButtonImage(this, gamePanelController_.getMedia_().decreaseFleet_, gamePanelController_.getMedia_().decreaseFleet_, 10, 12);
        readyButton_ = new ButtonImage(this, gamePanelController_.getMedia_().increaseFleet_, gamePanelController_.getMedia_().increaseFleet_, 150, 12);
        addMouseListener(increaseShip_);
        addMouseListener(decreaseShip_);
        addMouseListener(readyButton_);
        addActions();
    }

    private void addActions() {
        increaseShip_.addActionButton(new ActionButton() {

            @Override
            public void exitAction() {
            }

            @Override
            public void enterAction() {
            }

            @Override
            public void click(MouseEvent paramMouseEvent) {
                if (numberOfShips_ < planet_.getFleet_().getNumShips()) numberOfShips_++;
            }
        });
        decreaseShip_.addActionButton(new ActionButton() {

            @Override
            public void exitAction() {
            }

            @Override
            public void enterAction() {
            }

            @Override
            public void click(MouseEvent paramMouseEvent) {
                if (numberOfShips_ > 0) {
                    numberOfShips_--;
                }
            }
        });
        readyButton_.addActionButton(new ActionButton() {

            @Override
            public void exitAction() {
            }

            @Override
            public void enterAction() {
            }

            @Override
            public void click(MouseEvent paramMouseEvent) {
                ready_ = true;
            }
        });
    }

    public void paintComponent(Graphics g) {
        updateLabel();
        super.paintComponents(g);
        g.setColor(new Color(240, 230, 140, 0));
        g.fillRect(0, 0, getWidth(), getHeight());
        g.drawImage(background, 0, 0, this);
        increaseShip_.draw(g);
        decreaseShip_.draw(g);
        readyButton_.draw(g);
    }

    private void updateLabel() {
        numberOfShipsLabel_.setText(String.valueOf(numberOfShips_));
    }

    public int getNumberOfShips_() {
        return numberOfShips_;
    }

    public boolean isReady_() {
        return ready_;
    }

    public void setReady_(boolean ready_) {
        this.ready_ = ready_;
    }
}
