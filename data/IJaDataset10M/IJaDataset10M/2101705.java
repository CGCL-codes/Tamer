package de.rockon.fuzzy.simulation.view.frames;

import mdes.slick.sui.Button;
import mdes.slick.sui.event.ActionEvent;
import mdes.slick.sui.event.ActionListener;
import mdes.slick.sui.layout.RowLayout;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import de.rockon.fuzzy.controller.model.commands.ActionCommandPool;
import de.rockon.fuzzy.controller.util.factories.IconFactory;
import de.rockon.fuzzy.simulation.cases.BaseSimulation;

/**
 * Frame zum Laden einesControllers und zum Restart der Simulation
 * 
 * <pre>
 *  load
 *  restart
 * </pre>
 */
public class LoadSaveRestartFrame extends FuzzyFrame {

    /**
	 * Konstruktor
	 * 
	 * @param parent
	 *            Die Simulation
	 */
    public LoadSaveRestartFrame(final BaseSimulation parent) {
        super("Load", parent);
        setFrameIcon(IconFactory.ICON_FRAME_LOAD);
        setBackground(new Color(1.0f, 1.0f, 1.0f, 0.3f));
        setResizable(false);
        setMinimumSize(100, 100);
        setMaximumSize(100, 100);
    }

    /**
	 * setzt alles wieder auf die Standardwerte zur�ck
	 */
    @Override
    public void initFrame() {
        RowLayout layout = new RowLayout();
        layout.setSpacing(0);
        layout.setHorizontalAlignment(RowLayout.CENTER);
        setLayout(layout);
        Button btnLoad = new Button("Load");
        btnLoad.setActionCommand(ActionCommandPool.SIM_LOAD_LOAD);
        btnLoad.addActionListener(parent);
        Button btnLoadDefault = new Button("Load Default");
        btnLoadDefault.setActionCommand(ActionCommandPool.SIM_LOAD_DEF);
        btnLoadDefault.addActionListener(parent);
        Button btnRestart = new Button("Restart");
        btnRestart.setActionCommand(ActionCommandPool.SIM_LOAD_RESTART);
        btnRestart.addActionListener(parent);
        btnLoad.setWidth(85);
        btnLoadDefault.setWidth(85);
        btnRestart.setWidth(85);
        btnRestart.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                parent.resetPhysics();
                parent.resetFrames();
            }
        });
        add(btnLoad);
        add(btnLoadDefault);
        add(btnRestart);
    }

    @Override
    public void paint(Graphics g) {
    }

    @Override
    public void reset() {
    }
}
